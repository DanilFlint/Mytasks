package threads.easy.counter;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1. Параллельный счётчик
 *
 * Создай класс Counter с методом:
 *
 * void increment()
 *
 * Запусти 10 потоков, каждый из которых вызывает increment() 1000 раз.
 *
 * После завершения всех потоков выведи значение счётчика.
 *
 * Ожидаемый результат:
 *
 * 10000
 *
 * Условие: результат должен быть корректным при каждом запуске.
 */
public class Counter {
    private final Object lock = new Object();
    private final Lock reentrantLock = new ReentrantLock();

    private final AtomicLong atomicAccumulator = new AtomicLong();
    private Long synchronizedAccumulator = 0L;
    private Long lockAccumulator = 0L;
    private final LongAdder longAdder = new LongAdder();

    //1 решение
    public void atomicIncrement() {
        atomicAccumulator.incrementAndGet();
    }

    //2 решение
    public void synchronizeIncrement() {
        synchronized (lock) {
            synchronizedAccumulator++;
        }
    }

    //3 решение
    public void lockIncrement() {
        try {
            reentrantLock.lock();
            lockAccumulator++;
        } finally {
            reentrantLock.unlock();
        }
    }

    //4 решение
    public void longAdderIncrement() {
        longAdder.increment();
    }

    public Long getAtomic() {
        return atomicAccumulator.get();
    }

    public Long getSynchronizedAccumulator() {
        return synchronizedAccumulator;
    }

    public Long getLockAccumulator() {
        return lockAccumulator;
    }

    public Long getLongAdder() {
        return longAdder.sum();
    }
}
