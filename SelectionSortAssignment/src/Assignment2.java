/**
 * Created by Rick on 20-4-2017.
 */
public class Assignment2 {
    public static void main(String[] args) {
        sort(1);
        sort(2);
        sort(4);
        sort(8);
    }

    private static void sort(int threads) {
        sort(50000, threads);
        sort(100000, threads);
        sort(200000, threads);
        sort(400000, threads);
    }

    private static void sort(int elements, int threads) {
        EventProfiler profiler = new EventProfiler(true);

        ISortingAlgorithm sort = new DoubleThreadedSelectionSort(0, elements, threads);
        int[] toBeSorted = Utils.generateRandomDataSet(elements);

        profiler.start();
        sort.sort(toBeSorted);

        profiler.log("Sorting " + elements + " elements with " + threads + " threads");
    }
}
