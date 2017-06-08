/**
 * Created by Rick on 20-4-2017.
 */
public class main {
    static final int ELEMENT_COUNT = 100;
    static final int THREAD_COUNT = 8;
    private static final int SORT_COUNT = 1;
    private static EventProfiler profiler = new EventProfiler(true);

    private static boolean isDataSetSorted(int[] dataSet) {
        if (dataSet.length != ELEMENT_COUNT) return false;
        for (int i = 0; i < dataSet.length - 1; i++) {
            if (dataSet[i] > dataSet[i + 1]) {
                System.out.println(i);
                return false;
            }
        }
        return true;
    }

    private static int[] generateRandomDataSet(int numElements) {
        int[] dataSet = Utils.fillArray(numElements);
        profiler.log("Filling array");
        Utils.shuffleArray(dataSet);
        profiler.log("Shuffling array");
        System.out.println(" ");

        return dataSet;
    }


    public static void main(String[] args) {

        // int processors = Runtime.getRuntime().availableProcessors();
        //System.out.println("Processors " + processors);

        //ISortingAlgorithm sort = new MultithreadedSelectionSort1(0, ELEMENT_COUNT, THREAD_COUNT);
        ISortingAlgorithm sort = new DoubleThreadedSelectionSort(0, ELEMENT_COUNT, 2);
        //ISortingAlgorithm sort = new MultithreadedSelectionSort1(0, ELEMENT_COUNT, processors);

        profiler.start();

        //A dataset will be generated and shuffled
        int[] toBeSorted = generateRandomDataSet(ELEMENT_COUNT);

        profiler.start();

        for (int i = 0; i < SORT_COUNT; i++) {
            int[] sorted = sort.sort(toBeSorted);  // Note that this method returns a new array.

            // toBeSorted is not changed in any way.

            Utils.printArray(toBeSorted);
            profiler.start();
            System.out.println("Dataset is sorted: " + isDataSetSorted(sorted));
            profiler.log("Checking sorted array");
            Utils.printArray(sorted);
        }
    }
}
