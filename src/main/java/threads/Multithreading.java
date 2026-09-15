package threads;

import threads.easy.counter.Counter;

import java.util.ArrayList;
import java.util.List;

public class Multithreading {

    public static void main(String[] args) {
        Counter counter = new Counter();
        List<Thread> threads = new ArrayList<>();

        //1 решение
        /*Runnable runnable = () -> {
            for(int i = 0; i < 1000; i++) counter.atomicIncrement();
        };*/

        //2 решение
        /*Runnable runnable = () -> {
            for(int i = 0; i < 1000; i++) counter.synchronizeIncrement();
        };*/

        //3 решение
        /*Runnable runnable = () -> {
            for(int i = 0; i < 1000; i++) counter.lockIncrement();
        };*/

        //4 решение
        Runnable runnable = () -> {
            for(int i = 0; i < 1000; i++) counter.longAdderIncrement();
        };

        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(runnable));
        }

        threads.forEach(Thread::start);

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println(counter.getAtomic());
        System.out.println(counter.getSynchronizedAccumulator());
        System.out.println(counter.getLockAccumulator());
        System.out.println(counter.getLongAdder());

    }
}
