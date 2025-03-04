package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class ThreadedQuick implements Runnable {

    public static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    static final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

    final int[] my_array;
    final int start, end;

    private final int minPartitionSize;

    public ExecutorService getExecutor() {
        return executor;
    }

    public ThreadedQuick(int minPartitionSize, int[] array, int start, int end) {
        this.minPartitionSize = minPartitionSize;
        this.my_array = array;
        this.start = start;
        this.end = end;
    }

    public void run() {
        quicksort(my_array, start, end);
    }

    public static void swap(int[] array, int index1, int index2){
        int tmp = array[index2];
        array[index2] = array[index1];
        array[index1] = tmp;
    }

    public void quicksort(int[] array, int start, int end) {
        int len = end - start + 1;
        //System.out.println(len);
        if (len <= 1)
            return;


        int pivotValue = array[end];

        //swap(array, pivot_index, end);

        int storeIndex = start;
        for (int i = start; i < end; i++) {
            if (array[i] <= pivotValue) {
                swap(array, i, storeIndex);
                storeIndex++;
            }
        }

        swap(array, storeIndex, end);

        if (len > minPartitionSize) {

            ThreadedQuick quick = new ThreadedQuick(minPartitionSize, array, start, storeIndex - 1);
            Future<?> future = executor.submit(quick);
            quicksort(array, storeIndex + 1, end);

            try {
                future.get(1000, TimeUnit.SECONDS);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            quicksort(array, start, storeIndex - 1);
            quicksort(array, storeIndex + 1, end);
        }
    }

}

