/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 /**
  * Original is available at https://github.com/apache/storm/blob/master/examples/storm-starter/src/jvm/storm/starter/spout/RandomSentenceSpout.java
  */

package org.oliot.tracktrace.streamProcessing;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

//This spout randomly emits sentences
public class RandomSentenceSpout extends BaseRichSpout {
   //Collector used to emit output
  SpoutOutputCollector _collector;
  //Used to generate a random number
  Random _rand;
  
  //-------------------------------------
  private final static String QUEUE_NAME = "hello";
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
    //For randomness
    _rand = new Random();
    
    //------------------------------
  //factory.setHost("localhost");
 
    
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
//      @Override
//      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
//          throws IOException {
//        String message = new String(body, "UTF-8");
//        System.out.println(" [x] Received '" + message + "'");
//        
//        _collector.emit(new Values(message));
//      }
//    };
    
  //Emit data to the stream
  @Override
  public void nextTuple() {
	  
	  try {
		consumerque =new QueueingConsumer(channel);
		channel.basicConsume(QUEUE_NAME, true, consumerque);
		while(true){
			  System.out.println(" trying to deliver..... ");
              QueueingConsumer.Delivery delivery = consumerque.nextDelivery();
              String message = new String(delivery.getBody());
              System.out.println(" getting string..... ");
              System.out.println(" [x] Received '" + message + "'");
              System.out.print("emitting Rabbitmq Queue tuple");
              _collector.emit(new Values(message));
              System.out.print("emitted Rabbitmq Queue tuple");
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
	  
  //Sleep for a bit
   // Utils.sleep(5000);
    {
    //	System.out.println("random generation");
    //The sentences that will be randomly emitted
    String[] sentences = new String[]{ "the cow jumped over the moon", "an apple a day keeps the doctor away",
        "four score and seven years ago", "snow white and the seven dwarfs", "i am at two with nature" };
    //Randomly pick a sentence
    String sentence = sentences[_rand.nextInt(sentences.length)];
    //Emit the sentence
   // _collector.emit(new Values(sentence));
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
    declarer.declare(new Fields("sentence"));
  }
  
  
}