/**
 * Created by Rick on 20-4-2017.
 */
public class main {
    static final int SORT_COUNT = 5;

    public static void PrintArray(int[] array) {
        System.out.print("Array: ");
        System.out.print(array[0]);

        for(int i = 1; i < array.length; i++) {
            System.out.print(", ");
            System.out.print(array[i]);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        ISortingAlgorithm sort = new SelectionSort();

        Benchmark[] benchmarks = new Benchmark[SORT_COUNT];
        int[] toBeSorted = { 100, 4, 2, 1, 5, 10 }; // TODO: Randomly generate this set
        for(int i = 0; i < SORT_COUNT; i++) {
            ISortingAlgorithm.SortingResults sortingResults = sort.sort(toBeSorted);
            int[] sorted = sortingResults.getSortedData(); // Note that this method returns a new array. toBeSorted is not changed in any way.

            PrintArray(sorted);
            benchmarks[i] = sortingResults.benchmark;
        }

        Benchmark average = Benchmark.getAverage(benchmarks);
        average.print();
    }
}
