import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by Rick on 21-6-2017.
 */
public class ActiveMQClient {

    private static int clientId;

    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQGlobals.url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        updateClientNumbers(session);

        listenForData(session);

        connection.close();
    }

    private static void updateClientNumbers(Session session) throws JMSException {
        Destination clientNumberDestination = session.createQueue(ActiveMQGlobals.clientNumberQueue);
        MessageConsumer clientNumberConsumer = session.createConsumer(clientNumberDestination);
        Message message = clientNumberConsumer.receive(1000);
        if(message == null) {
            clientId = 0;
        }
        else {
            TextMessage textMessage = (TextMessage) message;
            clientId = Integer.parseInt(textMessage.getText());
        }
        clientNumberConsumer.close();

        MessageProducer clientNumberProducer = session.createProducer(clientNumberDestination);
        TextMessage newMessage = session.createTextMessage(""+(clientId+1));
        clientNumberProducer.send(newMessage);
        clientNumberProducer.close();
    }

    private static void listenForData(Session session) throws JMSException {
        Destination clientTaskQueue = session.createQueue(ActiveMQGlobals.clientTaskQueue + clientId);

        // Listen for a task
        while(true) {
            MessageConsumer consumer = session.createConsumer(clientTaskQueue);
            Message message = consumer.receive();
            consumer.close(); // Close the consumer so it doesn't grab messages produced by the client.

            if(message instanceof BytesMessage) {
                BytesMessage bytesMessage = (BytesMessage)message;
                byte[] bytes = new byte[(int) bytesMessage.getBodyLength()];
                bytesMessage.readBytes(bytes);

                try {
                    ActiveMQSortingData data = (ActiveMQSortingData) Utils.deserialize(bytes);
                    System.out.println("Received dataset with min " + data.minimumValue + " max " + data.maximumValue);

                    ActiveMQSortedData sortedData = sortReceivedData(data);

                    Destination clientResultQueue = session.createQueue(ActiveMQGlobals.clientResultQueue + clientId);
                    MessageProducer producer = session.createProducer(clientResultQueue);
                    BytesMessage sortedBytesMessage = session.createBytesMessage();
                    byte[] sortedBytes = Utils.serialize(sortedData);
                    sortedBytesMessage.writeBytes(sortedBytes);
                    producer.send(sortedBytesMessage);

                    producer.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static ActiveMQSortedData sortReceivedData(ActiveMQSortingData sortingData) {
        int processors = Runtime.getRuntime().availableProcessors();
        ISortingAlgorithm algorithm = new MultithreadedSelectionSort1(sortingData.minimumValue, sortingData.maximumValue, processors);
        int[] sortedDataSet = algorithm.sort(sortingData.dataSet);

        return new ActiveMQSortedData(sortedDataSet);
    }
}
