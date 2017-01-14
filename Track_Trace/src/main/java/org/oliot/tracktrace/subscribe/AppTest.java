package org.oliot.tracktrace.subscribe;

import java.io.IOException;

import javax.xml.bind.JAXBException;

/**
 * Hello world!
 *
 */
public class AppTest
{
    public static void main( String[] args )
    {
    	sample samp=new sample();
    	try {
			samp.transform();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println( "END APP" );
    }
}
