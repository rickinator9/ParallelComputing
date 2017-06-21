import java.io.Serializable;

/**
 * Created by Rick on 21-6-2017.
 */
public class ActiveMQSortedData implements Serializable {
    public int[] data;

    public ActiveMQSortedData(int[] dataSet) {
        this.data = dataSet;
    }
}
