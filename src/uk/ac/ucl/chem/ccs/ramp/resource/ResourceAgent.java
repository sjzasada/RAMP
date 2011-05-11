/*
 * Created on 16:15:09 11 May 2011
 * Project: RAMP 
 * File: ResourceAgent.java
 * 
 * @author stefan
 * 
 * TODO: 
 */
package uk.ac.ucl.chem.ccs.ramp.resource;

import jade.core.Agent;

public class ResourceAgent extends Agent {

	protected void setup () {
		
		
	}
	
	protected void takeDown() {
		System.out.println("Agent " + getAID().getName() + " terminated");
	}
	
}
