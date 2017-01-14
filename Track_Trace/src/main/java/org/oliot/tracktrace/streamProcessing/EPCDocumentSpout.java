package org.oliot.tracktrace.streamProcessing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.xml.bind.JAXB;

import org.oliot.epcis.configuration.Configuration;
import org.oliot.model.epcis.EPCISDocumentType;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class EPCDocumentSpout extends BaseRichSpout {
	   //Collector used to emit output
	  SpoutOutputCollector _collector;
	  
	  
	  //-------------------------------------
	  private final static String QUEUE_NAME = "TrackTraceStorm";
	  ConnectionFactory factory; 
	  Connection connection;
	  Channel channel;
	  QueueingConsumer consumerque;
	  
	  //--------------------------------------

	  //Open is called when an instance of the class is created
	  @Override
	  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
	  //Set the instance collector to the one passed in
	    _collector = collector;
	    
	 
	    
	     try {
	    	System.out.println(" [*] Creating connection factory");
	    	factory = new ConnectionFactory();
		    factory.setUsername("user");
		    factory.setPassword("123");
		    factory.setVirtualHost("/");
		    factory.setHost("143.248.57.21");
		    factory.setPort(5672);
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	  }
	  
	//  Consumer consumer = new DefaultConsumer(channel) {
//	      @Override
//	      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
//	          throws IOException {
//	        String message = new String(body, "UTF-8");
//	        System.out.println(" [x] Received '" + message + "'");
//	        
//	        _collector.emit(new Values(message));
//	      }
//	    };
	    
	  //Emit data to the stream
	  @Override
	  public void nextTuple() {
		  
		  try {
			consumerque =new QueueingConsumer(channel);
			channel.basicConsume(QUEUE_NAME, true, consumerque);
			while(true){
				  System.out.println(" Ready to accept EPC document... ");
	              QueueingConsumer.Delivery delivery = consumerque.nextDelivery();
	              String message = new String(delivery.getBody());
	              System.out.println(" EPC document recived ");
	              //System.out.println(" [x] Received '" + message + "'");
	             // System.out.println("After the message is recived");
	              //-----
	           
	  			InputStream epcisStream = new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8));
	  			System.out.println("AEPC document validated");
	  			//Configuration.logger.info(" EPCIS Document : Validated ");
				EPCISDocumentType epcisDocument = JAXB.unmarshal(epcisStream,EPCISDocumentType.class);
				System.out.println("EPC document converted");
	              _collector.emit(new Values(epcisDocument));
	             // _collector.emit(new Values(message));
	              System.out.println("EPC document emited");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ShutdownSignalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConsumerCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
	  
	  }
	  
	   public void nextTupleTobe(String sente){
		  _collector.emit(new Values(sente));
		  
	  }

	  //Ack is not implemented since this is a basic example
	  @Override
	  public void ack(Object id) {
	  }

	  //Fail is not implemented since this is a basic example
	  @Override
	  public void fail(Object id) {
	  }

	  //Declare the output fields. In this case, an sentence
	  @Override
	  public void declareOutputFields(OutputFieldsDeclarer declarer) {
	    declarer.declare(new Fields("EPCDocument"));
	  }
	  
	  
		private static InputStream getXMLDocumentInputStream(String xmlString) {
			InputStream stream = new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8));
			return stream;
		}

	  
}
