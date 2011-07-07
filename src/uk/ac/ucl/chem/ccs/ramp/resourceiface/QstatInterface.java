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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import uk.ac.ucl.chem.ccs.ramp.resource.ResourceOfferRecord;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.Cost;

public class QstatInterface implements ResourceInterface {

	private String qstat="/home/stefan/.workspace/RAMP/data/queuedata/fake_qstat.pl";
	private String qrstat="/home/stefan/.workspace/RAMP/data/queuedata/fake_qrstat.pl";
	
	
	private int timeperiod=600;
	private int maxcores = 163840;
	
	
	private HashMap<Long, Integer> model = new HashMap<Long, Integer>();
	
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
		
		Vector<JobObject> lines = new Vector<JobObject>();
		
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(qstat);

            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line=null;

            int usedCors = 0;
            
            while((line=input.readLine()) != null) {
            	
            	String [] vals = line.split("\t");
            	
            	boolean running=false;
            	
            	//model assumes FIFO
            	if (vals[4].equals("R")) {
            		running=true;

            	}
        		lines.add(new JobObject(Integer.parseInt(vals[3]), Integer.parseInt(vals[2]), running));
            	
            	
            }

            
            //int exitVal = pr.waitFor();
            
            long time = 0;//seconds
            
            //build model from data
            while (!lines.isEmpty()) {
            	
            	int cores=0;
            	
            	int index=0;
            	Enumeration<JobObject> e = lines.elements();
            	 while (e.hasMoreElements()) {
            		 JobObject jo = e.nextElement();
            	
            		 //need to check if job has finished at this timepoint
            		 
            		 if (jo.isRunning() && jo.getEnd() < time) {
            			lines.remove(index); 
            			 
            			//TODO: what next? 
            			
            		 } 
            		 
            		 if (jo.isRunning()) {
            			 cores=cores+jo.getCores();
            		 }
            		 
            	 }
            	 
            	 
            	 
            	//add this timepoint to the model
            	model.put(new Long(time), new Integer(cores));
            	
            	time = time + timeperiod;
            	index++;
            }
            
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
	
	
//	private class LineData {
//		
//		private int procs;
//		private int time;
//		/**
//		 * @return the procs
//		 */
//		public int getProcs() {
//			return procs;
//		}
//		/**
//		 * @param procs the procs to set
//		 */
//		public void setProcs(int procs) {
//			this.procs = procs;
//		}
//		/**
//		 * @return the time
//		 */
//		public int getTime() {
//			return time;
//		}
//		/**
//		 * @param time the time to set
//		 */
//		public void setTime(int time) {
//			this.time = time;
//		}
//		/**
//		 * @param procs
//		 * @param time
//		 */
//		public LineData(int procs, int time) {
//			this.procs = procs;
//			this.time = time;
//		}
//		
//		
//		
//	}
	
	private class JobObject {
		
		private int end;
		private int cores;
		private boolean running;
		/**
		 * @param end
		 * @param cores
		 * @param running
		 */
		public JobObject(int end, int cores, boolean running) {
			this.end = end;
			this.cores = cores;
			this.running = running;
		}
		/**
		 * @return the end
		 */
		public int getEnd() {
			return end;
		}
		/**
		 * @param end the end to set
		 */
		public void setEnd(int end) {
			this.end = end;
		}
		/**
		 * @return the cores
		 */
		public int getCores() {
			return cores;
		}
		/**
		 * @param cores the cores to set
		 */
		public void setCores(int cores) {
			this.cores = cores;
		}
		/**
		 * @return the running
		 */
		public boolean isRunning() {
			return running;
		}
		/**
		 * @param running the running to set
		 */
		public void setRunning(boolean running) {
			this.running = running;
		}
		
		
		
		
	}

}
