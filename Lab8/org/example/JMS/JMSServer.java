package org.example.JMS;

import com.rabbitmq.jms.admin.RMQConnectionFactory;

import javax.jms.*;

public class JMSServer {

    public static void main(String[] args) {
        // Створення ConnectionFactory
        ConnectionFactory connectionFactory = new RMQConnectionFactory();

        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {

            Queue queue = session.createQueue("testQueue");
            MessageConsumer consumer = session.createConsumer(queue);

            connection.start();

            while (true) {
                Message message = consumer.receive();
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String[] command = textMessage.getText().split(" ");
                    executeCommand(command, session);
                }
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private static void executeCommand(String[] command, Session session) throws JMSException {
        switch (command[0]) {
            case "createCity":
                DAO.createCity(command[1], Integer.parseInt(command[2]), Integer.parseInt(command[3]), Boolean.parseBoolean(command[4]));
                break;
            case "deleteCity":
                DAO.deleteCity(Integer.parseInt(command[1]));
                break;
            case "editCity":
                DAO.editCity(Integer.parseInt(command[1]), command[2], Integer.parseInt(command[3]), Boolean.parseBoolean(command[4]));
                break;
            case "getCities":
                // Відправка результатів назад до клієнта (при необхідності)
                break;
            case "getCountries":
                // Відправка результатів назад до клієнта (при необхідності)
                break;
            default:
                System.out.println("Unknown command");
                break;
        }
    }
}
