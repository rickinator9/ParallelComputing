import java.util.Random;

/**
 * Created by Rick on 20-4-2017.
 */
public class main {
    static final int SORT_COUNT = 5;
    static final int ELEMENT_COUNT = 50000;
    static final int THREAD_COUNT = 8;

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
        ISortingAlgorithm sort = new MultithreadedSelectionSort1(0, ELEMENT_COUNT, THREAD_COUNT);

        Benchmark[] benchmarks = new Benchmark[SORT_COUNT];
        int[] toBeSorted = generateRandomDataSet(ELEMENT_COUNT);
        //PrintArray(toBeSorted);
        for(int i = 0; i < SORT_COUNT; i++) {
            ISortingAlgorithm.SortingResults sortingResults = sort.sort(toBeSorted);
            int[] sorted = sortingResults.getSortedData();  // Note that this method returns a new array.
                                                            // toBeSorted is not changed in any way.

            //PrintArray(sorted);
            benchmarks[i] = sortingResults.benchmark;
            System.out.println("Dataset is sorted: " + isDataSetSorted(sorted));
        }

        Benchmark average = Benchmark.getAverage(benchmarks);
        average.print();
    }

    private static boolean isDataSetSorted(int[] dataSet) {
        if(dataSet.length != ELEMENT_COUNT) return false;
        for(int i = 0; i < dataSet.length-1; i++) {
            if(dataSet[i] > dataSet[i+1]) return false;
        }
        return true;
    }

    private static int[] generateRandomDataSet(int numElements) {
        int[] dataSet = Utils.fillArray(numElements);
        Utils.shuffleArray(dataSet);

        return dataSet;
    }
}
