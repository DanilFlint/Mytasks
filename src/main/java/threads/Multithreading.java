package threads;

import threads.easy.counter.Counter;
import threads.middle.ParallelProcessing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Multithreading {

    public static void main(String[] args) {
        ParallelProcessing parallelProcessing = new ParallelProcessing();
        long start = System.currentTimeMillis();
        System.out.println(parallelProcessing.calculateListParallel().stream().map(Object::toString).collect(Collectors.joining(" ")));
        System.out.println(System.currentTimeMillis() - start);
    }
}
