import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rick on 5-5-2017.
 */
public class RickMultithreadedSelectionSort implements ISortingAlgorithm {
    private class SelectionSortThread implements Runnable {
        public int startIndex, endIndex;

        public SelectionSortThread(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public void run() {
            for(int i = startIndex; i < endIndex-1; i++) {
                int minimum = i;

                for(int j = i+1; j < endIndex; j++) {
                    if(dataSet[j] < dataSet[minimum]) {
                        minimum = j;
                    }
                }

                if(minimum != i) {
                    int temp = dataSet[i];
                    dataSet[i] = dataSet[minimum];
                    dataSet[minimum] = temp;
                }
            }
        }
    }

    private int dataSet[];
    private int numThreads;
    private ExecutorService executorService;

    public RickMultithreadedSelectionSort(int numThreads) {
        this.numThreads = numThreads;
    }

    @Override
    public SortingResults sort(int[] toBeSorted) {
        // Duplicate this array in case we want to use it to sort multiple times(to get averages).
        dataSet = Arrays.copyOfRange(toBeSorted, 0, toBeSorted.length);

        executorService = Executors.newFixedThreadPool(numThreads);
        Benchmark benchmark = new Benchmark();
        benchmark.start();

        RunSort();
        for(int i = 0; i < numThreads-1; i++) {
            RunMerge();
            RunSort();
        }

        executorService.shutdown();

        benchmark.log("Selectionsort done");

        SortingResults results = new SortingResults(benchmark, dataSet);
        return results;
    }

    private void RunSort() {
        int elementsPerThread = getElementsPerThread();

        Collection<Callable<Object>> tasks = new ArrayList<Callable<Object>>();

        for(int i = 0; i < numThreads; i++) {
            SelectionSortThread thread = new SelectionSortThread(elementsPerThread*i, elementsPerThread*(i+1));

            if(i == 0)
                thread.startIndex = 0;
            if(i == numThreads-1)
                thread.endIndex = dataSet.length;
            tasks.add(Executors.callable(thread));
        }
        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void RunMerge() {
        int elementsPerThread = getElementsPerThread();

        Collection<Callable<Object>> tasks = new ArrayList<Callable<Object>>();
        for(int i = 0; i < numThreads; i++) { // Don't use all threads.
            int startIndex = (elementsPerThread*i)+elementsPerThread/2;
            int endIndex = (elementsPerThread*(i+1))+elementsPerThread/2;

            SelectionSortThread thread = new SelectionSortThread(startIndex, endIndex);
            tasks.add(Executors.callable(thread));
        }
        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getElementsPerThread() {
        return dataSet.length/numThreads;
    }
}
