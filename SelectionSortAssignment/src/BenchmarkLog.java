/**
 * Created by Rick on 22-4-2017.
 */
public class BenchmarkLog {
    public BenchmarkLog(long nanoSeconds, String message) {
        this.timeStamp = nanoSeconds;
        this.message = message;
    }

    private long timeStamp;
    public long getTimeStamp() { return timeStamp; }

    private String message;
    public String getMessage() { return message; }
}
