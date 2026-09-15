package threads.easy.list_adder;

import java.util.ArrayList;
import java.util.List;

/**
 * 5. Безопасный доступ к общему списку
 *
 * Есть:
 *
 * List<Integer> numbers = new ArrayList<>();
 *
 * Запусти 5 потоков, каждый из которых добавляет в список числа от 1 до 1000.
 *
 * После завершения всех потоков:
 *
 * выведи размер списка;
 * проверь, что размер равен 5000.
 *
 * Дополнительное условие: решение должно быть потокобезопасным.
 */
public class ListAdder {
    List<Integer> numbers = new ArrayList<>();
    private final Object lock = new Object();

    private void add(int number) {
        synchronized(lock) {
            numbers.add(number);
        }
    }

    public void fillList() {
        int countNumbers = 1000;

        Runnable runnable = () -> {
            for (int i = 0; i < countNumbers; i++) {
                add(i);
            }
        };

        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);
        Thread t4 = new Thread(runnable);
        Thread t5 = new Thread(runnable);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getNumbers() {
        return numbers;
    }
}
