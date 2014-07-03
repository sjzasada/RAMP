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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import uk.ac.ucl.chem.ccs.ramp.resource.FirmOffer;
import uk.ac.ucl.chem.ccs.ramp.resource.ResourceOfferRecord;
import uk.ac.ucl.chem.ccs.ramp.rfq.Request;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.RFQ;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.Offer;

public class QstatInterface implements ResourceInterface {

	private String qstat="/home/stef/workspace/RAMP/data/queuedata/fake_qstat2.pl";
	private String qrstat="/home/stef/workspace/RAMP/data/queuedata/fake_qrstat2.pl";
	
	private int price;
	private int minPrice;
	private String confile;
	public String message="";
	
	public QstatInterface (int i, int j, String file) {
		minPrice=i;
		price=j;
		confile=System.getProperty("ramp.conffile")+"/"+file+".timefile";
	}
	
	public ResourceOfferRecord canSatisfy(RFQ c, FirmOffer oldRor) {
		// check whether resource can satisfy request
		message="";
		int cpucount = c.getTOTALCORES();
		String when = c.getNOTBEFORE();
		//System.err.println("ERROR DATE "+when);
		Date d = new Date();
		SimpleDateFormat parserSDF=new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
		try{ 
		d=parserSDF.parse(when);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		long l = d.getTime();
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
	            		message=message+"Machine Load: "+factor+"\n";
	            	} else if (cnt==1) {
	            		result=line;
	            	}
	            	cnt++;
	            } 
	            
	       } catch (Exception e) {
	    	   //e.printStackTrace();
	    	   message=message+"Error parsing qstat data\n";
	    	   return null;
	       }
	

	       	   
	       //if the resource is accepting the job, calculate the price
	       if (result.equals("Accept")) {
	       

	    	   
	    	   
		   int requestprice = Integer.parseInt(c.getCPUHOURCOST());
		    int previousprice = 0;
		    if (oldRor != null) {
		      previousprice = oldRor.getRor().getMinCPUCost();
		    }


		    
		    if (previousprice==requestprice) {
		    	message=message+"We're already offering a winning price - no bid\n";
		    	return null;//if we've already got a winning offer, don't bid
		    }
		    int offerprice;
	    //	   if (requestprice >= price) {
	    //		   offerprice=price;
	    //	   } else {
		    
	       int decreaseSteps=4;
	       factor=1.0f-factor;//%unallocated
	       float decrement=(price-minPrice)/decreaseSteps*factor;
	       
	       
	       
	       message=message+"decrement is "+decrement+" at"+System.currentTimeMillis()+"\n";
	       offerprice=price;
	       
		   offerprice=Math.round(requestprice-decrement);

	       
	       //decrease the price 
	       //for (int f=0; f<=decreaseSteps;f++) {
	    	   if (offerprice < minPrice) {
	    		   if (minPrice <= requestprice) {
	    			   offerprice=minPrice;
	    		   } else {
	    		   message=message+"we've gone too low\n";
	    		   return null;
	    		   }
	    	   }
	    	   
	     //  }
	       
	       
	       
	       if (offerprice>requestprice) {
	    	   message = message+"offer price greater than request price\n";
	    	   return null;
	       }
	    	//   }
	       message = message + "resource: " + result + " @ cost " + offerprice+"\n";
	       
	       
	    	   ResourceOfferRecord ror = new ResourceOfferRecord(offerprice, c);
	    	   return ror;
	       }
	       message = message + "qstat returned reject\n";
		return null;
	}

	public String makeReservation(Offer c) {
		message="";
		int cpucount = c.getOTOTALCORES();
		String when = c.getONOTBEFORE();
		
		Date d = new Date();
		SimpleDateFormat parserSDF=new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
		try{ 
		d=parserSDF.parse(when);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		long l = d.getTime();		
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

/*		System.setProperty("ramp.conffile", "/");

		QstatInterface qi = new QstatInterface(10, "dibble");
		
		Request myRequest = new Request();	
		myRequest.load("/home/stef/workspace/RAMP/RFQs/rfq.xml");
		
		ResourceOfferRecord ro = qi.canSatisfy(myRequest.getRFQObject());

	      System.out.println("cost " + ro.getMinCPUCost() + " offer " + ro.getOffer().getOCPUHOURCOST());

		String resID = qi.makeReservation(ro.getOffer());
		
		System.err.println("Reservation " + resID);*/

		
	}
	
	

	
	

}
