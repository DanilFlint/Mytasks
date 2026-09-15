package threads.easy.list_processing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.stream.IntStream;

/**
 * 2. Параллельная обработка списка
 *
 * Есть список:
 *
 * List<Integer> numbers = IntStream.rangeClosed(1, 1000)
 *         .boxed()
 *         .toList();
 *
 * Необходимо обработать его в нескольких потоках.
 *
 * Каждый элемент должен быть умножен на 2, после чего необходимо получить:
 *
 * List<Integer>
 *
 * с результатами.
 *
 * Требования:
 *
 * использовать несколько потоков;
 * каждый элемент должен быть обработан ровно один раз;
 * итоговый список должен содержать 1000 элементов.
 */
public class ProcessList {
    private static final int NUMBER_OF_THREADS = 10;
    private static final int NUMBER_OF_ELEMENTS = 1000;
    private final List<Integer> numbers = IntStream.rangeClosed(1, NUMBER_OF_ELEMENTS).boxed().toList();

    private class NumberMulti implements Callable<List<Integer>> {
        private final List<Integer> numbers;

        public NumberMulti(int start, int end, List<Integer> numbers) {
            this.numbers = numbers.subList(start, end);
        }

        @Override
        public List<Integer> call() {
            return this.numbers.stream().map(i -> i * 2).toList();
        }

    }

    public List<Integer> processList() {
        List<FutureTask<List<Integer>>> result = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            Callable<List<Integer>> nm = new NumberMulti(
                    i * NUMBER_OF_ELEMENTS / NUMBER_OF_THREADS,
                    (i + 1) * NUMBER_OF_ELEMENTS/NUMBER_OF_THREADS,
                    numbers
            );

            FutureTask<List<Integer>> ft = new FutureTask<>(nm);
            Thread thread = new Thread(ft);
            try {
                result.add(ft);
                thread.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return result.stream().flatMap(t -> {
            try {
                return t.get().stream();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public List<Integer> processList2() {
        List<Future<List<Integer>>> resultExecutors = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        try {
            for (int i = 0; i < NUMBER_OF_THREADS; i++) {
                Callable<List<Integer>> nm = new NumberMulti(
                        i * NUMBER_OF_ELEMENTS / NUMBER_OF_THREADS,
                        (i + 1) * NUMBER_OF_ELEMENTS/NUMBER_OF_THREADS,
                        numbers
                );
                resultExecutors.add(executorService.submit(nm));
            }
        } finally {
            executorService.shutdown();
        }

        return resultExecutors.stream().flatMap(t -> {
            try {
                return t.get().stream();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}
