package threads.easy.waiting_task;

/**
 * 4. Ожидание завершения задач
 *
 * Есть три независимые задачи:
 *
 * Task 1 — sleep 1 секунда
 * Task 2 — sleep 2 секунды
 * Task 3 — sleep 3 секунды
 *
 * Запусти их параллельно.
 *
 * Главный поток должен вывести:
 *
 * All tasks completed
 *
 * только после завершения всех трёх задач.
 *
 * При последовательном выполнении программа заняла бы около 6 секунд. При правильном параллельном выполнении — около 3 секунд.
 */
public class Waiting {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
