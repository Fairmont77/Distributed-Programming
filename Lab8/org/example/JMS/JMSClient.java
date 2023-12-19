package org.example.JMS;

import javax.jms.*;
import com.rabbitmq.jms.admin.RMQConnectionFactory;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;

public class JMSClient {
    private static final String QUEUE_NAME = "testQueue";

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new RMQConnectionFactory();
        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
             MessageProducer producer = session.createProducer(session.createQueue(QUEUE_NAME));
             MessageConsumer consumer = session.createConsumer(session.createQueue(QUEUE_NAME))) {

            connection.start();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введіть команду (або 'exit' для виходу):");

            while (true) {
                String input = scanner.nextLine();
                if ("exit".equalsIgnoreCase(input)) {
                    break;
                }

                sendMessage(session, producer, input);

                Message response = consumer.receive(5000);
                if (response instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) response;
                    System.out.println("Отримано відповідь: " + textMessage.getText());
                } else {
                    System.out.println("Немає відповіді або відповідь не є текстовим повідомленням");
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendMessage(Session session, MessageProducer producer, String text) throws JMSException {
        TextMessage message = session.createTextMessage(text);
        producer.send(message);
    }
}
