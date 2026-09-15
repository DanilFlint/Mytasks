package threads.middle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.stream.LongStream;

/**
 * 1. Параллельная обработка с ограничением количества задач
 *
 * Есть список из 10 000 чисел.
 * Для каждого числа необходимо выполнить операцию:
 *
 * result = calculate(number);
 * где calculate() имитирует долгую операцию:
 * Thread.sleep(10);
 * return number * number;
 *
 * Требования:
 * обработка должна выполняться параллельно;
 * одновременно должно выполняться не более 10 операций;
 * необходимо получить List<Long> со всеми результатами;
 * каждое число должно быть обработано ровно один раз;
 * порядок результатов должен соответствовать исходному порядку чисел.
 */
public class ParallelProcessing {
    private static final int PERMITS = 10;
    private static final int COUNT_NUMBERS = 10_000;
    private static final int COUNT_THREADS = 15;

    List<Long> numbers = LongStream.rangeClosed(1, COUNT_NUMBERS).boxed().toList();
    Semaphore semaphore = new Semaphore(PERMITS);
    Helper helper = new Helper();

    private class Helper {
        private Long calculate(Long number) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return number * number;
        }
    }

    private Long calculate(Long number) {
        Helper helper = new Helper();
        boolean acquired = false;

        try {
            semaphore.acquire();
            acquired = true;

            return helper.calculate(number);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally {
            if (acquired) {
                semaphore.release();
            }
        }
    }

    public List<Long> calculateListParallel() {
        ExecutorService executorService = Executors.newFixedThreadPool(COUNT_THREADS);
        List<Future<Long>> futureList = new ArrayList<>();

        for (int i = 0; i < numbers.size(); i++) {
            final int index = i;
            futureList.add(executorService.submit(() -> calculate(numbers.get(index))));
        }

        executorService.shutdown();
        return futureList.stream().map(futureLong -> {
            try {
                return futureLong.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}
