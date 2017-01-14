package org.oliot.example.test;

import org.junit.Test;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

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
 
public class QueryTest {

	/**
	 * Default Initialization Map Data Send
	 */
	@SuppressWarnings("resource")
	@Test
	public void subqueryTest1() {
		
		
    	Cluster cluster;
    	Session session;
    	cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		session = cluster.connect("trackTrace");
	}
}
