/*
 * Created on 14:14:09 20 Jun 2011
 * Project: RAMP 
 * File: QstatInterface.java
 * 
 * @author stefan
 * 
 * TODO: 
 */
package uk.ac.ucl.chem.ccs.ramp.resourceiface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import uk.ac.ucl.chem.ccs.ramp.resource.ResourceOfferRecord;
import uk.ac.ucl.chem.ccs.ramp.rfq.Request;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.RFQ;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.Offer;

public class QstatInterface implements ResourceInterface {

	private String qstat="/home/stef/workspace/RAMP/data/queuedata/fake_qstat2.pl";
	private String qrstat="/home/stef/workspace/RAMP/data/queuedata/fake_qrstat2.pl";
	
	public ResourceOfferRecord canSatisfy(RFQ c) {
		// TODO Auto-generated method stub
		
		int cpucount = c.getTOTALCORES();
		String when = c.getNOTBEFORE();
		
		Date d = new Date();
		long l = d.parse(when);
		long n = System.currentTimeMillis();
		
		l=(l-n)/1000;
				
		int price = Integer.parseInt(System.getProperty("ramp.price")); 	
		String confile=System.getProperty("ramp.conffile");
		
		System.out.println("Looking to buy " + cpucount + " in " + l + " seconds");
		
		float factor=0.0f; 
		String result="";
		
	       try {
	            Runtime rt = Runtime.getRuntime();
	            Process pr = rt.exec(qstat + " " + l + " " + cpucount + " " + confile);

	            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

	            String line=null;
	            int cnt=0;
	            while((line=input.readLine()) != null) {
	            	//System.err.println("Line" + line);
	            	if (cnt==0) {
	            		factor=Float.parseFloat(line);
	            	} else if (cnt==1) {
	            		result=line;
	            	}
	            	cnt++;
	            } 
	            
	       } catch (Exception e) {
	    	   e.printStackTrace();
	       }
	
	       int offerprice=Math.round(price*factor);
	       
	       System.out.println("resource: " + result + " @ cost " + offerprice);
	       
	       int requestprice = Integer.parseInt(c.getCPUHOURCOST());
	       
	       if (result.equals("Accept") && offerprice <= requestprice) {
	    	   ResourceOfferRecord ror = new ResourceOfferRecord(offerprice, c);
	    	   return ror;
	       }
	       
		return null;
	}

	public String makeReservation(Offer c) {
		
		int cpucount = c.getOTOTALCORES();
		String when = c.getONOTBEFORE();
		
		Date d = new Date();
		long l = d.parse(when);
		long n = System.currentTimeMillis();
		
		l=(l-n)/1000;
				
		String confile=System.getProperty("ramp.conffile");
		
		System.out.println("Looking to reserve " + cpucount + " in " + l + " seconds");
		
		String result="";
		
		  try {
	            Runtime rt = Runtime.getRuntime();
	            Process pr = rt.exec(qrstat + " " + l + " " + cpucount + " " + confile);

	            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

	            String line=null;
	            int cnt=0;
	            while((line=input.readLine()) != null) {
	            	//System.err.println("Line" + line);
	            	if (cnt==0) {
	            		result=line;
	            	}
	            	cnt++;
	            } 
	            
	       } catch (Exception e) {
	    	   e.printStackTrace();
	       }
		
		  if (!result.equals("Fail")) {
			  return result;
		  }
		
		return null;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.setProperty("ramp.price", "10");
		System.setProperty("ramp.conffile", "/tmp/dibble.timefile");

		QstatInterface qi = new QstatInterface();
		
		Request myRequest = new Request();	
		myRequest.load("/home/stef/workspace/RAMP/RFQs/rfq.xml");
		
		ResourceOfferRecord ro = qi.canSatisfy(myRequest.getRFQObject());

	      System.out.println("cost " + ro.getMinCPUCost() + " offer " + ro.getOffer().getOCPUHOURCOST());

		String resID = qi.makeReservation(ro.getOffer());
		
		System.err.println("Reservation " + resID);

		
	}
	
	

	
	

}
