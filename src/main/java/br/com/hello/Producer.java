package br.com.hello;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Stateless
@Path("/producer")
public class Producer {

	@Resource(lookup = "java:/jms/queue/myqueue")
	private Queue queue;

	@Resource(lookup = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Path("/jms20")
	@GET
	@Asynchronous
	public void produce() {
		MessageProducer messageProducer;

		TextMessage textMessage;
		try {
			Connection connection = connectionFactory.createConnection();
			
			
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			
			messageProducer = session.createProducer(queue);

			textMessage = session.createTextMessage();

			for (int i = 0; i < 100; i++) {

				textMessage.setText("TESTEEEEE: " + i % 10);
				textMessage.setJMSPriority(i % 10);
				messageProducer.send(textMessage);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			messageProducer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}

		// return "OK";

	}

}
