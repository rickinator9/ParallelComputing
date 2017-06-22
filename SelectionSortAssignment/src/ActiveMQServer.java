import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;

public class ActiveMQServer {
    private static int numberOfClients;

    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQGlobals.url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        numberOfClients = getNumberOfClients(session);
        System.out.println("Number of Clients: " + numberOfClients);

        sort(10000, session);
        sort(20000, session);
        sort(40000, session);
        sort(80000, session);
        sort(160000, session);

        connection.close();
    }

    private static int getNumberOfClients(Session session) throws JMSException {
        Destination clientNumberDestination = session.createQueue(ActiveMQGlobals.clientNumberQueue);
        MessageConsumer consumer = session.createConsumer(clientNumberDestination);

        Message clientNumberMessage = consumer.receive();
        int numberOfClients = 0;
        if(clientNumberMessage instanceof TextMessage) {
            numberOfClients = Integer.parseInt(((TextMessage) clientNumberMessage).getText());
        }

        MessageProducer producer = session.createProducer(clientNumberDestination);
        producer.send(clientNumberMessage);

        // Cleanup
        consumer.close();
        producer.close();

        return numberOfClients;
    }

    private static void sort(int n, Session session) throws JMSException {
        int[] dataSet = Utils.generateRandomDataSet(n);
        sortElements(dataSet, session, n);
    }

    private static void sortElements(int[] dataSet, Session session, int maximum) throws JMSException {
        EventProfiler profiler = new EventProfiler(true);

        int accumulatedValues = 0;
        int valuesPerClient = maximum/numberOfClients;

        profiler.start();
        for (int i = 0; i < numberOfClients; i++) {
            Destination clientTaskQueue = session.createQueue(ActiveMQGlobals.clientTaskQueue + i);
            MessageProducer producer = session.createProducer(clientTaskQueue);
            BytesMessage sortingDataMessage = session.createBytesMessage();

            int min = accumulatedValues;
            int max = accumulatedValues + valuesPerClient;

            accumulatedValues += valuesPerClient;
            ActiveMQSortingData data = new ActiveMQSortingData(min, max, dataSet);
            try {
                byte[] bytes = Utils.serialize(data);
                sortingDataMessage.writeBytes(bytes);
                producer.send(sortingDataMessage);

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Cleanup
            producer.close();
        }

        ArrayList<ActiveMQSortedData> sortedDatas = new ArrayList<ActiveMQSortedData>();
        for (int i = 0; i < numberOfClients; i++) {
            Destination clientResultQueue = session.createQueue(ActiveMQGlobals.clientResultQueue + i);
            MessageConsumer consumer = session.createConsumer(clientResultQueue);
            Message message = consumer.receive();
            consumer.close();

            if(message instanceof BytesMessage) {
                BytesMessage bytesMessage = (BytesMessage)message;
                byte[] bytes = new byte[(int) bytesMessage.getBodyLength()];
                bytesMessage.readBytes(bytes);

                try {
                    ActiveMQSortedData sortedData = (ActiveMQSortedData)Utils.deserialize(bytes);
                    sortedDatas.add(sortedData);
                    //System.out.println("Received SortedData with length: " + sortedData.data.length);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }

        int[] mergedData;
        if(numberOfClients > 1) {
            mergedData = new int[dataSet.length];
            int index = 0;
            for (ActiveMQSortedData sortedData :
                    sortedDatas) {
                int[] data = sortedData.data;
                for (int i = 0; i < data.length; i++) {
                    mergedData[index] = data[i];
                    index++;
                }
            }
        }
        else {
            mergedData = sortedDatas.get(0).data;
        }
        profiler.log("Sorting " + dataSet.length + " elements with " + numberOfClients + " clusters");

        //Utils.printArray(mergedData);
        //boolean isSorted = Utils.isDataSetSorted(mergedData, dataSet.length);
        //System.out.println(isSorted);
    }
}
