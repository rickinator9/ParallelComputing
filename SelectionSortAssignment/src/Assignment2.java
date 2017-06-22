/**
 * Created by Rick on 20-4-2017.
 */
public class Assignment2 {
    public static void main(String[] args) {
        sort(1);
        sort(2);
    }

    private static void sort(int threads) {
        sort(10000, threads);
        sort(20000, threads);
        sort(40000, threads);
        sort(80000, threads);
        sort(160000, threads);
    }

    private static void sort(int elements, int threads) {
        EventProfiler profiler = new EventProfiler(true);

        ISortingAlgorithm sort;
        if(threads == 1) {
            sort = new SelectionSort(); // Singlethreaded
        } else {
            sort = new DoubleThreadedSelectionSort(0, elements, threads);
        }
        int[] toBeSorted = Utils.generateRandomDataSet(elements);

        profiler.start();
        sort.sort(toBeSorted);

        profiler.log("Sorting " + elements + " elements with " + threads + " threads");
    }
}
