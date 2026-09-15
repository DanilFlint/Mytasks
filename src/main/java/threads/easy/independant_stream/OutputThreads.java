package threads.easy.independant_stream;

/**
 * 3. Два независимых потока
 *
 * Создай два потока:
 *
 * первый выводит числа от 1 до 10;
 * второй выводит буквы от A до J.
 *
 * Потоки должны выполняться параллельно.
 *
 * Пример возможного результата:
 *
 * 1
 * A
 * 2
 * 3
 * B
 * C
 * 4
 * D
 * ...
 *
 * Важно: порядок между потоками заранее неизвестен и не должен предполагаться.
 */
public class OutputThreads {
    private static class LetterThread extends Thread {

        @Override
        public void run() {
            for (char i = 'A'; i < 'J'; i++) {
                System.out.println(i);
            }
        }
    }

    private static class NumberThread extends Thread {

        @Override
        public void run() {
            for (int i = 1; i < 11; i++) {
                System.out.println(i);
            }
        }
    }

    public void showOutput() {
        Thread thread = new LetterThread();
        Thread thread2 = new NumberThread();

        thread.start();
        thread2.start();
    }
}
