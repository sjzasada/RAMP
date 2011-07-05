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
import java.util.Vector;

import uk.ac.ucl.chem.ccs.ramp.resource.ResourceOfferRecord;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.Cost;

public class QstatInterface implements ResourceInterface {

	private String qstat="/home/stefan/.workspace/RAMP/data/queuedata/fake_qstat.pl";
	private String qrstat="/home/stefan/.workspace/RAMP/data/queuedata/fake_qrstat.pl";
	
	@Override
	public ResourceOfferRecord canSatisfy(Cost c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String makeReservation(Cost c) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public void updateState() {
		
		Vector lines = new Vector();
		
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(qstat);

            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line=null;

            int usedCors = 0;
            
            while((line=input.readLine()) != null) {
            	
            	String [] vals = line.split("\t");
            	
            	if (vals[4].equals("R")) {
            		usedCors = usedCors + Integer.parseInt(vals[2]);
            	}
            	
            }

            System.out.println("Cores used = "+usedCors);
            
            //int exitVal = pr.waitFor();
            
        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }

        //now process vector data into bins;
        
        int timer=0;
        
        
        
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		QstatInterface qi = new QstatInterface();
		
		qi.updateState();
		
	}
	
	
	private class LineData {
		
		private int procs;
		private int time;
		/**
		 * @return the procs
		 */
		public int getProcs() {
			return procs;
		}
		/**
		 * @param procs the procs to set
		 */
		public void setProcs(int procs) {
			this.procs = procs;
		}
		/**
		 * @return the time
		 */
		public int getTime() {
			return time;
		}
		/**
		 * @param time the time to set
		 */
		public void setTime(int time) {
			this.time = time;
		}
		/**
		 * @param procs
		 * @param time
		 */
		public LineData(int procs, int time) {
			this.procs = procs;
			this.time = time;
		}
		
		
		
	}

}
