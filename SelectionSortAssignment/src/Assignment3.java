/**
 * Created by Rick on 21-6-2017.
 */
public class Assignment3 {
    public static void main(String[] args) {
        sort(1);
        sort(2);
        sort(4);
        sort(8);
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

        ISortingAlgorithm sort = new MultithreadedSelectionSort1(0, elements, threads);
        int[] toBeSorted = Utils.generateRandomDataSet(elements);

        profiler.start();
        sort.sort(toBeSorted);

        profiler.log("Sorting " + elements + " elements with " + threads + " threads");
    }
}
