/**
 * Created by Rick on 21-6-2017.
 */
public class ActiveMQGlobals {
    // either connect to the remote ActiveMQ running on the PI, or on the localhost
    public static final String url = "failover:(tcp://169.254.1.1:61616,tcp://localhost:61616)";

    // Queue Names
    public static final String clientNumberQueue = "clientNumber";
    public static final String clientTaskQueue = "client";
    public static final String clientResultQueue = "clientResult";

    public static String testSubject = "testQueueRick"; // Queue Name
}
