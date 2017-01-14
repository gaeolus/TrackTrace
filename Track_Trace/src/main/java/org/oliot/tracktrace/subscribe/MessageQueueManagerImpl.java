package org.oliot.tracktrace.subscribe;

import java.util.HashMap;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.oliot.tracktrace.backend.TrackTraceCapture;
import org.oliot.tracktrace.service.capture.TrEventCapure;
import org.oliot.tracktrace.subscribe.MessageQueueManager;

@Service("messageQueueManager")
public class MessageQueueManagerImpl implements MessageQueueManager{
@Autowired
private AmqpAdmin admin;
@Autowired
private AmqpTemplate template;
@Autowired
private ConnectionFactory connectionFactory;
@Autowired
private SimpleMessageListenerContainer container;

@Override
public String createQueue(String queueName) {
   //Log.debug("creating queue with name: " + queueName);

   //create queue
	for(int i=0; i<3;i++){
   Queue newQueue = new Queue(queueName,true,false,true);
   queueName = admin.declareQueue(newQueue);

   //create binding with exchange
   admin.declareBinding(new Binding(queueName, DestinationType.QUEUE, "directExchange", queueName, new HashMap<String,Object>()));

   //Log.debug("queue successfully created: " + queueName);

   //add queue to listener
   container.addQueues(newQueue);

   //start listener
   container.start();
	}
   return queueName;
}
@Override
public void sendMessage(String message,String destinationQueueName) throws Exception {
   template.convertAndSend("directExchange", destinationQueueName,   MessageBuilder.withBody(message.getBytes()).build());
}

@Override
public void onMessage(Message message) {
	
	System.out.println("recived");
	
	try{
		byte[] bytes = message.getBody();
		String xmlString = new String(bytes, "UTF-8");
		TrEventCapure trEventCapure=new TrEventCapure();
		
		//TrackTraceCapture trackTraceCapture= new TrackTraceCapture();
		//trackTraceCapture.transform(xmlString);
		long start_time = System.currentTimeMillis();
		trEventCapure.EventCapture(xmlString);
    	long end_time = System.currentTimeMillis();
    	long result = end_time-start_time;
    	System.out.println(result);
		
		System.out.println("recived");
	}catch(Exception e){
		
	}
	
   //Log.debug(new String(message.getBody()));
}
}