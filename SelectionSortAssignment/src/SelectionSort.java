import java.util.Arrays;

/**
 * Created by Rick on 22-4-2017.
 */
public class SelectionSort implements ISortingAlgorithm {
    @Override
    public SortingResults sort(int[] toBeSorted) {
        // TODO Multithread this!
        Benchmark benchmark = new Benchmark();
        benchmark.start();

        // Duplicate this array in case we want to use it to sort multiple times(to get averages).
        int[] sortedArray = Arrays.copyOfRange(toBeSorted, 0, toBeSorted.length);
        benchmark.log("Duplicated array");
        for(int i = 0; i < sortedArray.length-1; i++) {
            int minimum = i;

            for(int j = i+1; j < sortedArray.length; j++) {
                if(sortedArray[j] < sortedArray[minimum]) {
                    minimum = j;
                }
            }

            if(minimum != i) {
                int temp = sortedArray[i];
                sortedArray[i] = sortedArray[minimum];
                sortedArray[minimum] = temp;
            }
        }
        benchmark.log("Selectionsort done");

        return new SortingResults(benchmark, sortedArray);
    }
}
