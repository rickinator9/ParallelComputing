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
        ISortingAlgorithm sort = new SelectionSort();

        Benchmark[] benchmarks = new Benchmark[SORT_COUNT];
        int[] toBeSorted = generateRandomDataSet(100000, 10000, false);
        for(int i = 0; i < SORT_COUNT; i++) {
            ISortingAlgorithm.SortingResults sortingResults = sort.sort(toBeSorted);
            int[] sorted = sortingResults.getSortedData();  // Note that this method returns a new array.
                                                            // toBeSorted is not changed in any way.

            PrintArray(sorted);
            benchmarks[i] = sortingResults.benchmark;
        }

        Benchmark average = Benchmark.getAverage(benchmarks);
        average.print();
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
