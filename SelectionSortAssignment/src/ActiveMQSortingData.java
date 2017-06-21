import java.io.Serializable;

/**
 * Created by Rick on 21-6-2017.
 */
public class ActiveMQSortingData implements Serializable {
    public int minimumValue, maximumValue;
    public int[] dataSet;

    public ActiveMQSortingData(int min, int max, int[] data) {
        this.minimumValue = min;
        this.maximumValue = max;
        this.dataSet = data;
    }
}
