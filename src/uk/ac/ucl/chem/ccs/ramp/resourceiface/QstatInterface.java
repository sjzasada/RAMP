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
	
	private int price;
	private String confile;
	public String message="";
	
	public QstatInterface (int i, String file) {
		price=i;
		confile=System.getProperty("ramp.conffile")+"/"+file+".timefile";
	}
	
	public float canSatisfy(RFQ c) {
		// TODO Auto-generated method stub
		message="";
		int cpucount = c.getTOTALCORES();
		String when = c.getNOTBEFORE();
		
		Date d = new Date();
		long l = d.parse(when);
		long n = System.currentTimeMillis();
		
		l=(l-n)/1000;
					
		message = "Looking to buy " + cpucount + " in " + l + " seconds\n";
		
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
		       
	       	       
	       if (result.equals("Accept")) {
	    	   return factor;
	       }
	       
		return -1.0f;
	}

	public String makeReservation(Offer c) {
		message="";
		int cpucount = c.getOTOTALCORES();
		String when = c.getONOTBEFORE();
		
		Date d = new Date();
		long l = d.parse(when);
		long n = System.currentTimeMillis();
		
		l=(l-n)/1000;
				
		//String confile=System.getProperty("ramp.conffile");
		
		message = "Looking to reserve " + cpucount + " in " + l + " seconds\n";
		
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

		System.setProperty("ramp.conffile", "/");

		QstatInterface qi = new QstatInterface(10, "dibble");
		
		Request myRequest = new Request();	
		myRequest.load("/home/stef/workspace/RAMP/RFQs/rfq.xml");
		
		//ResourceOfferRecord ro = qi.canSatisfy(myRequest.getRFQObject());

	   //   System.out.println("cost " + ro.getMinCPUCost() + " offer " + ro.getOffer().getOCPUHOURCOST());

		//String resID = qi.makeReservation(ro.getOffer());
		
		//System.err.println("Reservation " + resID);

		
	}
	
	

	
	

}
