import java.util.Random;

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
        ISortingAlgorithm sort = new RickMultithreadedSelectionSort(16);

        Benchmark[] benchmarks = new Benchmark[SORT_COUNT];
        int[] toBeSorted = generateRandomDataSet(200000, 10000, false);
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
        for(int i = 0; i < dataSet.length-1; i++) {
            if(dataSet[i] > dataSet[i+1]) return false;
        }
        return true;
    }

    private static int[] generateRandomDataSet(int numElements, int maxValue, boolean allowNegativeValues) {
        Random random = new Random();

        int[] dataSet = new int[numElements];
        for(int i = 0; i < numElements; i++) {
            float randomFactor = random.nextFloat();

            if(allowNegativeValues) {
                // Multiply by 2 and negate by 1 to allow for negative values.
                randomFactor *= 2.0f;
                randomFactor -= 1.0f;
            }

            int randomValue = (int)((float) maxValue * randomFactor);
            dataSet[i] = randomValue;
        }

        return dataSet;
    }
}
