import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * Created by Rick on 16-5-2017.
 *
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

            while(minValue <= maxValue) {
                int smallest = Integer.MAX_VALUE;
                for (int element :
                        dataSet) {
                    if (element >= minValue && element <= maxValue && element < smallest) {
                        smallest = element;
                    }
                }
                minValue = smallest+1;
                if(smallest == Integer.MAX_VALUE)
                    break;
                listPerThread[id].add(smallest);
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
            ArrayList<Integer> list = listPerThread[id];

            while (!list.isEmpty()) {
                outDataSet[i] = list.remove(0);
                i++;
            }
        }
    }

    private int minValue, maxValue;
    private int numThreads;
    private int[] dataSet;
    private int[] outDataSet;
    private ArrayList<Integer>[] listPerThread;

    public MultithreadedSelectionSort1(int minValue, int maxValue, int numThreads) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.numThreads = numThreads;
    }

    @Override
    public SortingResults sort(int[] toBeSorted) {
        dataSet = toBeSorted;
        outDataSet = new int[toBeSorted.length];
        listPerThread = new ArrayList[numThreads];

        Benchmark benchmark = new Benchmark();
        benchmark.start();

        SortingThread[] sortingThreads = new SortingThread[numThreads];
        int valuesPerThread = maxValue / numThreads;
        int accumulatedValues = 0;
        for(int i = 0; i < numThreads; i++) {
            sortingThreads[i] = new SortingThread(i, accumulatedValues, accumulatedValues+valuesPerThread);
            accumulatedValues+=valuesPerThread+1;
            listPerThread[i] = new ArrayList<>();
            sortingThreads[i].start();
        }
        for(int i = 0; i < numThreads; i++) {
            try {
                sortingThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        benchmark.log("Sorted values.");

        MergingThread[] mergingThreads = new MergingThread[numThreads];
        int startIndexAccumulator = 0;
        for(int i = 0; i < numThreads; i++) {
            mergingThreads[i] = new MergingThread(i, startIndexAccumulator);
            startIndexAccumulator += listPerThread[i].size();
            mergingThreads[i].start();
        }
        for(int i = 0; i < numThreads; i++) {
            try {
                mergingThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        benchmark.log("Merged values");

        SortingResults results = new SortingResults(benchmark, outDataSet);

        return results;
    }
}
