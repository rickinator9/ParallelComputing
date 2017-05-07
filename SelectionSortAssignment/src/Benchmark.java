import java.util.ArrayList;

public class Benchmark {

    public Benchmark() {
        benchmarkLogs = new ArrayList<>();
    }

    private ArrayList<BenchmarkLog> benchmarkLogs;
    private long startTime;

    public void start() {
        startTime = System.nanoTime();
    }

    public void log(String message) {
        long timestampOfPrevious;
        try {
            BenchmarkLog log = benchmarkLogs.get(benchmarkLogs.size()-1);
            timestampOfPrevious = log.getTimeStamp();
        }
        catch(ArrayIndexOutOfBoundsException ex) {
            timestampOfPrevious = 0;
        }
        BenchmarkLog log = new BenchmarkLog(System.nanoTime()-timestampOfPrevious-startTime, message);
        benchmarkLogs.add(log);
    }

    public void print() {
        System.out.println("BENCHMARK PRINT");

        long accumulatedTime = 0;
        for (BenchmarkLog log : benchmarkLogs) {
            double millis = (log.getTimeStamp()-accumulatedTime)/1e6;

            System.out.println(log.getMessage());
            System.out.println("Time: " + millis + "ms.");
            System.out.println();

            accumulatedTime += log.getTimeStamp();
        }
    }

    public int getLogCount() {
        return benchmarkLogs.size();
    }

    public BenchmarkLog getLogAtIndex(int index) {
        return benchmarkLogs.get(index);
    }

    public long getStartTime() {
        return startTime;
    }

    public static Benchmark getAverage(Benchmark[] benchmarks) {
        Benchmark averageBenchmark = new Benchmark();
        for(int i = 0; i < benchmarks[0].getLogCount(); i++) {
            String text = benchmarks[0].benchmarkLogs.get(i).getMessage();
            long averageTimeStamp = 0;
            for (Benchmark benchmark : benchmarks) {
                //NOTE: We have to immediately divide the timestamp by the amount of benchmarks to prevent overflow!
                averageTimeStamp += benchmark.benchmarkLogs.get(i).getTimeStamp()/benchmarks.length;
            }

            BenchmarkLog log = new BenchmarkLog(averageTimeStamp, text);
            averageBenchmark.benchmarkLogs.add(log);
        }
        return averageBenchmark;
    }
}
