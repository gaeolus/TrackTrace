package org.oliot.epcis.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import org.oliot.tracktrace.subscribe.MessageQueueManagerImpl;
import org.oliot.tracktrace.streamProcessing.RandomSentenceSpout;
import org.oliot.tracktrace.streamProcessing.SplitSentence;
import org.oliot.tracktrace.streamProcessing.StreamProcessingToplogy;
import org.oliot.tracktrace.streamProcessing.WordCount;
import org.oliot.tracktrace.streamProcessing.WordCountTopology;
import org.oliot.tracktrace.subscribe.MessageQueueManager;
/**
 * Copyright (C) 2015 KAIST RESL
 *
 * This project is part of Oliot (oliot.org), 
 * implements track and race using 

 * 
 *
 * @author Yalew Kidane, MSc student
 * 
 *         Korea Advanced Institute of Science and Technology (KAIST)
 * 
 *         Real-time Embedded System Laboratory(RESL)
 * 
 *         yalewkidane@kaist.ac.kr
 */
public class Configuration implements ServletContextListener {

//	public static String backend;
	public static String proccesingType;
	public static Logger logger;
	public static String webInfoPath;
	public static boolean isCaptureVerfificationOn;
	public static boolean isMessageQueueOn;
	public static int numCaptureListener;
	public static String captureQueue;
	public static String queryExchange;

	public static boolean isRestMessageQueueOn;
	public static int numRestCaptureListener;
	public static String restCaptureQueue;
	public static String restQueryExchange;

	public static List<SimpleMessageListenerContainer> MQContainerList;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {

		// Set Logger
		setLogger();

		// Set Basic Configuration with Configuration.json
		setBasicConfiguration(servletContextEvent.getServletContext());

		// Set Capture Message Queue
		MQContainerList = new ArrayList<SimpleMessageListenerContainer>();
		setCaptureMessageQueue();

		// load existing subscription
		loadExistingSubscription();
	}

	private void setLogger() {
		// Log4j Setting
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.INFO);
		Configuration.logger = Logger.getRootLogger();
	}

	private void setBasicConfiguration(ServletContext context) {
		String path = context.getRealPath("/WEB-INF");
		try {
			// Get Configuration.json
			File file = new File(path + "/Configuration.json");
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);

			String data = "";
			String line = null;
			while ((line = reader.readLine()) != null) {
				data += line;
			}
			reader.close();
			JSONObject json = new JSONObject(data);

			// Set up Backend
//			String backend = json.getString("backend");
//			if (backend == null) {
//				Configuration.logger
//						.error("Backend is null, please make sure Configuration.json is correct, and restart.");
//			} else {
//				Configuration.backend = backend;
//				Configuration.logger.info("Backend - " + Configuration.backend);
//			}
			// Set up Backend
			String processtype = json.getString("processtype");
			if (processtype == null) {
				Configuration.logger
						.error("processtype is null, please make sure Configuration.json is correct, and restart.");
			} else {
				Configuration.proccesingType = processtype;
				Configuration.logger.info("Backend - " + Configuration.proccesingType);
			}
			Configuration.webInfoPath = context.getRealPath("/WEB-INF");

			// Set up capture_verification
			String captureVerification = json.getString("capture_verification");
			if (captureVerification == null) {
				Configuration.logger
						.error("capture_verification is null, please make sure Configuration.json is correct, and restart.");
			}
			captureVerification = captureVerification.trim();
			if (captureVerification.equals("on")) {
				Configuration.isCaptureVerfificationOn = true;
				Configuration.logger.info("Capture_Verification - ON ");
			} else if (captureVerification.equals("off")) {
				Configuration.isCaptureVerfificationOn = false;
				Configuration.logger.info("Capture_Verification - OFF ");
			} else {
				Configuration.logger
						.error("capture_verification should be (on|off), please make sure Configuration.json is correct, and restart.");
			}

	String message_queue = json.getString("message_queue");
	if (message_queue.equals("on")) {
		Configuration.isMessageQueueOn = true;
		Configuration.logger.info("Message Queue - ON ");
	} else if (message_queue.equals("off")) {
		Configuration.isMessageQueueOn = false;
		Configuration.logger.info("Message Queue - OFF ");
	} else {
		Configuration.logger
				.error("message_queue should be (on|off), please make sure Configuration.json is correct, and restart.");
	}
	String numListener = json
			.getString("message_queue_capture_listener");
	captureQueue = json.getString("message_queue_capture_name");
	if (isMessageQueueOn == true
			&& (numListener == null || captureQueue == null)) {
		Configuration.logger
				.error("if message_queue on, number of capture listener should be described, please make sure Configuration.json is correct, and restart.");
	} else {
		try {
			numCaptureListener = Integer.parseInt(numListener);
		} catch (NumberFormatException e) {
			Configuration.logger
					.error("number of capture listener should be integer, please make sure Configuration.json is correct, and restart.");
		}
	}
	queryExchange = json.getString("message_queue_exchange_query");
	if (isMessageQueueOn == true && queryExchange == null) {
		Configuration.logger
				.error("if message_queue on, query exchange should be described, please make sure Configuration.json is correct, and restart.");
	}
//}
//			// Set up Message Queue
			
		} catch (Exception ex) {
			Configuration.logger.error(ex.toString());
		}

	}

	private void setCaptureMessageQueue() {
		if (Configuration.isMessageQueueOn == true) {
			Configuration.logger.info("Message Queue Service - Started ");
			Configuration.logger
					.info("Message Queue Service - Number of Capture Listener: "
							+ Configuration.numCaptureListener);
			Configuration.logger
					.info("Message Queue Service - Capture Queue Name: "
							+ Configuration.captureQueue);

			

			for (int i = 0; i < Configuration.numCaptureListener; i++) {
				ApplicationContext applicationContext = new ClassPathXmlApplicationContext("rabbitmqContext.xml");
		    	MessageQueueManager manager = applicationContext.getBean(MessageQueueManagerImpl.class);
		    	
		    	manager.createQueue("TrackTrace");
		    	
		    	
				Configuration.logger
						.info("Message Queue Service - Capture Listener "
								+ (i + 1) + " started");
			}
			
		}
	}

	private void loadExistingSubscription() {
//		if (Configuration.backend.equals("MongoDB")) {
//			
//		} else if (Configuration.backend.equals("Cassandra")) {
//
//		} else if (Configuration.backend.equals("MySQL")) {
//			//MysqlSubscription ms = new MysqlSubscription();
//			//ms.init();
//			
//		}
	}
	
//	public void loadTopology()throws Exception {
//		  //Used to build the topology
//	    TopologyBuilder builder = new TopologyBuilder();
//	    //Add the spout, with a name of 'spout'
//	    //and parallelism hint of 5 executors
//	    builder.setSpout("spout", new RandomSentenceSpout(), 5);
//	    //Add the SplitSentence bolt, with a name of 'split'
//	    //and parallelism hint of 8 executors
//	    //shufflegrouping subscribes to the spout, and equally distributes
//	    //tuples (sentences) across instances of the SplitSentence bolt
//	    builder.setBolt("split", new SplitSentence(), 8).shuffleGrouping("spout");
//	    //Add the counter, with a name of 'count'
//	    //and parallelism hint of 12 executors
//	    //fieldsgrouping subscribes to the split bolt, and
//	    //ensures that the same word is sent to the same instance (group by field 'word')
//	    builder.setBolt("count", new WordCount(), 12).fieldsGrouping("split", new Fields("word"));
//
//	    //new configuration
//	    Config conf = new Config();
//	    conf.setDebug(true);
//
//	 
//	    //Otherwise, we are running locally
//	    
//	      //Cap the maximum number of executors that can be spawned
//	      //for a component to 3
//	      conf.setMaxTaskParallelism(3);
//	      //LocalCluster is used to run locally
//	      LocalCluster cluster = new LocalCluster();
//	      //submit the topology
//	      cluster.submitTopology("word-count", conf, builder.createTopology());
//	      //sleep
//	      System.out.println("sleep starts here");
//	      Thread.sleep(50000);
//	      System.out.println("sleep ends here");
//	      //shut down the cluster
//	      cluster.shutdown();
//	    
//	  }
}
