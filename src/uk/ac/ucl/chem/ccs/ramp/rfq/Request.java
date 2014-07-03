/*
 * Created on 12:17:29 19 May 2011
 * Project: RAMP 
 * File: Request.java
 * 
 * @author stefan
 * 
 * TODO: 
 */
package uk.ac.ucl.chem.ccs.ramp.rfq;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.*;

//This class contains details of each request, and a copy of the marshalled RequestforQuotation document loaded by the user. 
//It converts this to an RFQ object for agent interchange as necessary

public class Request {

	private ObjectFactory of;
	private RequestForQuotation rfq;
	private String requestID;
	
	

	public String getRequestID() {
		return requestID;
	}



	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}



	public Request () {
		of = new ObjectFactory();
		rfq = of.createRequestForQuotation();
	}
	
	

	public Request (String filename) {
		this();
		load(filename);
	}
	
	public int getCPUCount () {
		return rfq.getRequest().getCores().getTotalCores().intValue();
	}
	
	public int getCPUCost () {
		return rfq.getRequest().getCPUHourCost().intValue();
	}
	
	public void setCPUCost (int c) {
		RequestForQuotation.Request rfqr = rfq.getRequest();
		rfqr.setCPUHourCost(BigInteger.valueOf(c));
		rfq.setRequest(rfqr);
	}
	
	public String getArch () {
		return rfq.getRequest().getArchitecture();
	}
	
	public Date getStart () {
	
		GregorianCalendar date = rfq.getRequest().getStartDate().toGregorianCalendar();
		GregorianCalendar time = rfq.getRequest().getStartTime().toGregorianCalendar();
		
		
		
		date.add(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
		date.add(Calendar.MINUTE, time.get(Calendar.MINUTE));
		date.add(Calendar.SECOND, time.get(Calendar.SECOND));	
				
		return date.getTime();
	}
	
	public Date getEnd () {
		
		GregorianCalendar date = rfq.getRequest().getEndDate().toGregorianCalendar();
		GregorianCalendar time = rfq.getRequest().getEndTime().toGregorianCalendar();
		
		
		
		date.add(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
		date.add(Calendar.MINUTE, time.get(Calendar.MINUTE));
		date.add(Calendar.SECOND, time.get(Calendar.SECOND));	
				
		return date.getTime();
	}
	
	@SuppressWarnings("deprecation")
	public void AddRequest (String arch, int cores, int cost, Date s, Date e) {
		
		RequestForQuotation.Request rfqr = of.createRequestForQuotationRequest();
		rfqr.setArchitecture(arch);
		
		
		rfqr.setCPUHourCost(BigInteger.valueOf(cost));
		
		//dates
		Calendar start = Calendar.getInstance();
		start.setTime(s);
			
		Calendar end = Calendar.getInstance();
		end.setTime(e);

			
		DatatypeFactory df=null;
		try {
			df = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		XMLGregorianCalendar xgdate = df.newXMLGregorianCalendarDate(
		        start.get( Calendar.YEAR ),
		        start.get( Calendar.MONTH ),
		        start.get( Calendar.DAY_OF_MONTH ),
		        DatatypeConstants.FIELD_UNDEFINED );

		
		XMLGregorianCalendar gcTime =
		    df.newXMLGregorianCalendarTime(
		        start.get( Calendar.HOUR_OF_DAY ),
		        start.get( Calendar.MINUTE ),
		        start.get( Calendar.SECOND ),
		        null,                               // no fraction
		        DatatypeConstants.FIELD_UNDEFINED );
		
		rfqr.setStartTime(gcTime);
		rfqr.setStartDate(xgdate);
		
		
		//end
		xgdate = df.newXMLGregorianCalendarDate(
	        end.get( Calendar.YEAR ),
		        end.get( Calendar.MONTH ),
		        end.get( Calendar.DAY_OF_MONTH ),
		        DatatypeConstants.FIELD_UNDEFINED );

		
		gcTime = df.newXMLGregorianCalendarTime(
		        end.get( Calendar.HOUR_OF_DAY ),
		        end.get( Calendar.MINUTE ),
		        end.get( Calendar.SECOND ),
		        null,                               // no fraction
		        DatatypeConstants.FIELD_UNDEFINED );
		
		rfqr.setEndTime(gcTime);
		rfqr.setEndDate(xgdate);
		

		
		//parse and set date and time
	
		
		//set the cores
		RequestForQuotation.Request.Cores cours = of.createRequestForQuotationRequestCores();				
		cours.setTotalCores(BigInteger.valueOf(cores));
		rfqr.setCores(cours);
		
		rfq.setRequest(rfqr);
		
		
	}
	
	
	public boolean load ( String filename ) {
		
		try {
		    FileInputStream fstream = new FileInputStream(filename);
		    
		    JAXBContext jc = JAXBContext.newInstance( "uk.ac.ucl.chem.ccs.ramp.rfq" );
		    Unmarshaller u = jc.createUnmarshaller();
		   // JAXBElement<RequestForQuotation> doc = (JAXBElement<RequestForQuotation>);
		    rfq = (RequestForQuotation)u.unmarshal( fstream );
		    fstream.close();
		    return true;
    
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
}

	
	
    public void marshal() {

        try {
    	JAXBContext jc = JAXBContext.newInstance( "uk.ac.ucl.chem.ccs.ramp.rfq" );

        // marshal to System.out
        Marshaller m = jc.createMarshaller();

			m.marshal( rfq, System.out );
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    }

	
	
    public boolean save (String filename) {

        try {
        	
       	FileOutputStream fstream = new FileOutputStream(filename);
    	JAXBContext jc = JAXBContext.newInstance( "uk.ac.ucl.chem.ccs.ramp.rfq" );

        // marshal to System.out
        Marshaller m = jc.createMarshaller();

			m.marshal( rfq, fstream );
			fstream.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

    	
    }
	

	public RFQ getRFQObject() {
    	
		RFQ rfq2 = new RFQ();
    	
    	if (rfq.getRequest().getArchitecture() != null) {
    		rfq2.setARCHITECTURE(rfq.getRequest().getArchitecture());
    	}
    	
    	if (rfq.getRequest().getCPUSpeed() != null) {
        	rfq2.setCPUSPEED(rfq.getRequest().getCPUSpeed().toString()); //need to change this to check for nulls in the doc
    	}
    	
    	if (rfq.getRequest().getInterNodeBandwidth() != null) {
        	rfq2.setINTERNODEBANDWIDTH(rfq.getRequest().getInterNodeBandwidth().toString());
    	}

    	if (rfq.getRequest().getDisk() != null) {
        	rfq2.setNODEDISKSPACE(rfq.getRequest().getDisk().toString());
    	}
    	
    	if (rfq.getRequest().getOperatingSystem() != null) {
        	rfq2.setOPERATINGSYSTEM(rfq.getRequest().getOperatingSystem());
    	}
    	
    	if (rfq.getRequest().getOSVersion() != null) {
        	rfq2.setOSVERSION(rfq.getRequest().getOSVersion());
    	}
    	
    	if (rfq.getRequest().getRAMPerCore() != null) {
        	rfq2.setRAMPERCORE(rfq.getRequest().getRAMPerCore().toString());
    	}	
    	
    	if (rfq.getRequest().getDisk() != null) {
        	rfq2.setTOTALDISKSPACE(rfq.getRequest().getDisk().toString());
    	}
    	
  	
    	String duration;
    	
    	if (rfq.getRequest().getWallTime()==null) {
    		duration="12";
    	} else {
    		duration=rfq.getRequest().getWallTime().toString();
    	}
    	
    	
    	rfq2.setDURATION(duration);
    	rfq2.setNODECORES(0);
    	rfq2.setNODECOUNT(0);
    	rfq2.setTOTALCORES(getCPUCount());
    	    	
    	rfq2.setCPUHOURCOST(Integer.toString(getCPUCost()));
    	rfq2.setDEADLINE(getEnd().toString());
    	rfq2.setNOTBEFORE(getStart().toString());
    	
		rfq2.setREQUESTID(requestID);

    	
    	return rfq2;
    }
    
		

    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		Request myr = new Request();
		//myr.AddRequest("Intel", 5, 5, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 240000));
		//myr.save("/home/stefan/rfq.xml");
		
		
		myr.load("/home/stefan/rfq.xml");
		
		System.out.println(myr.getArch());
		System.out.println(myr.getCPUCost());
		System.out.println(myr.getCPUCount());
		System.out.println(myr.getEnd());
		System.out.println(myr.getStart());
	}

}
