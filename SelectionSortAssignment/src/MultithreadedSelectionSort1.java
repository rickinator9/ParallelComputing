import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by Rick on 16-5-2017.
 * <p>
 * Each thread puts elements in a range of values in their own list.
 * Other threads then merge these lists into an array.
 */
public class MultithreadedSelectionSort1 implements ISortingAlgorithm {
    private class SortingThread extends Thread {
        private int id;
        private int minValue, maxValue;

        public SortingThread(int id, int minValue, int maxValue) {
            this.id = id;
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        @Override
        public void run() {
            while (minValue <= maxValue) {
                int smallest = Integer.MAX_VALUE;
                for (int element : dataSet) {
                    if (element >= minValue && element <= maxValue && element < smallest) {

                        smallest = element;
                    }
                }
                minValue = smallest + 1;
                if (smallest == Integer.MAX_VALUE)
                    break;
                queuePerThread[id].offer(smallest);
            }
        }
    }

    private class MergingThread extends Thread {
        private int id;
        private int startIndex;

        public MergingThread(int id, int startIndex) {
            this.id = id;
            this.startIndex = startIndex;
        }

        @Override
        public void run() {
            int i = startIndex;
            Queue<Integer> queue = queuePerThread[id];

            while (!queue.isEmpty() && i < outDataSet.length) {
                outDataSet[i] = queue.poll();
                i++;
            }
        }
    }

    private int minValue, maxValue;
    private int numThreads;
    private int[] dataSet;
    private int[] outDataSet;
    private Queue<Integer>[] queuePerThread;


    public MultithreadedSelectionSort1(int minValue, int maxValue, int numThreads) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.numThreads = numThreads;
    }

    @Override
    public int[] sort(int[] toBeSorted) {
        EventProfiler profiler = new EventProfiler(true);
        profiler.start();

        dataSet = toBeSorted;
        outDataSet = new int[maxValue-minValue];
        queuePerThread = new Queue[numThreads];


        SortingThread[] sortingThreads = new SortingThread[numThreads];

        int valuesPerThread = (maxValue-minValue) / numThreads;
        int accumulatedValues = minValue;

        for (int i = 0; i < numThreads; i++) {
            int min = accumulatedValues;
            int max = accumulatedValues + valuesPerThread;
            if(i+1 == numThreads) {
                max = maxValue;
            }

            sortingThreads[i] = new SortingThread(i, min, max);
            accumulatedValues += valuesPerThread + 1;
            queuePerThread[i] = new ConcurrentLinkedDeque<>();
            sortingThreads[i].start();
        }
        profiler.log("CONSTANT starting threads.");
        for (int i = 0; i < numThreads; i++) {
            try {
                sortingThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        profiler.log("PARALLEL Sorting values");

        MergingThread[] mergingThreads = new MergingThread[numThreads];
        int startIndexAccumulator = 0;
        for (int i = 0; i < numThreads; i++) {
            mergingThreads[i] = new MergingThread(i, startIndexAccumulator);
            startIndexAccumulator += queuePerThread[i].size();
            mergingThreads[i].start();
        }

        profiler.log("CONSTANT starting merge threads");
        for (int i = 0; i < numThreads; i++) {
            try {
                mergingThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        profiler.log("PARALLEL merging values");

        return outDataSet;
    }
}
