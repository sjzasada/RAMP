/*
 * Created on 16:15:43 11 May 2011
 * Project: RAMP 
 * File: UserAgent.java
 * 
 * @author stefan
 * 
 * TODO: 
 */
package uk.ac.ucl.chem.ccs.ramp.user;

import jade.core.Agent;

public class UserAgent extends Agent {

	protected void setup () {
		
		
	}
	
	protected void takeDown() {
		System.out.println("Agent " + getAID().getName() + " terminated");
	}
	
}
