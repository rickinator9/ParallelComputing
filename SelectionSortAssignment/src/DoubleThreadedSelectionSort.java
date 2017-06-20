import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by joene on 6/8/2017.
 */
public class DoubleThreadedSelectionSort implements ISortingAlgorithm {

    private class SortingThreadMinimum extends Thread {

        @Override
        public void run() {

                for (; minimumIndex < maximumIndex; minimumIndex++) {
                    synchronized (this) {

                    int minimum = minimumIndex;

                    for (int j = minimumIndex + 1; j < maximumIndex+1; j++) {
                        if (dataSet[j] < dataSet[minimum]) {
                            minimum = j;
                        }
                    }

                    if (minimum != minimumIndex) {
                        int temp = dataSet[minimumIndex];
                        dataSet[minimumIndex] = dataSet[minimum];
                        dataSet[minimum] = temp;
                    }
                }
            }
        }
    }

    private class SortingThreadMaximum extends Thread {


        @Override
        public void run() {
                for (; maximumIndex >= minimumIndex; maximumIndex--) {
                    synchronized (this) {
                    int maximum = maximumIndex;


                    for (int j = maximumIndex - 1; j >= minimumIndex; j--) {
                        if (dataSet[j] > dataSet[maximum]) {
                            maximum = j;
                        }
                    }

                    if (maximum != maximumIndex) {
                        int temp = dataSet[maximumIndex];
                        dataSet[maximumIndex] = dataSet[maximum];
                        dataSet[maximum] = temp;
                    }
                }
            }

        }
    }


    private int minValue, maxValue;
    private int numThreads;
    private int[] dataSet;
    private Object lock;

    private int minimumIndex, maximumIndex;



    public DoubleThreadedSelectionSort(int minValue, int maxValue, int numThreads) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.numThreads = numThreads;
    }

    @Override
    public int[] sort(int[] toBeSorted) {
        dataSet = Arrays.copyOfRange(toBeSorted, 0, toBeSorted.length);

        EventProfiler profiler = new EventProfiler(true);
        profiler.start();

        minimumIndex = 0;
        maximumIndex = dataSet.length-1;


        SortingThreadMinimum STMinimum = new SortingThreadMinimum();
        SortingThreadMaximum STMaximum = new SortingThreadMaximum();

        STMinimum.start();
        STMaximum.start();

        try {
            STMinimum.join();
            STMaximum.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        profiler.log("Sorting values");


        return dataSet;
    }
}
