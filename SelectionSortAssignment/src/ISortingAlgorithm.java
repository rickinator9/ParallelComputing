/**
 * Created by Rick on 22-4-2017.
 */
public interface ISortingAlgorithm {
     class SortingResults {
        public SortingResults(Benchmark benchmark, int[] sortedData) {
            this.benchmark = benchmark;
            this.sortedData = sortedData;
        }

        Benchmark benchmark;
        public Benchmark getBenchmark() {
            return benchmark;
        }

        int[] sortedData;
        public int[] getSortedData() {
            return sortedData;
        }
    };
    SortingResults sort(int[] toBeSorted);
}
