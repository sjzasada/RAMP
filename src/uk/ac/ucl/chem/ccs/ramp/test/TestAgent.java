/*
 * Created on 15:20:23 11 May 2011
 * Project: RAMP 
 * File: TestAgent.java
 * 
 * @author stefan
 * 
 * TODO: 
 */
package uk.ac.ucl.chem.ccs.ramp.test;

import java.util.Iterator;
import jade.core.Agent;

public class TestAgent extends Agent {

	protected void setup () {
		System.out.println("My name is " + getAID().getLocalName());
		System.out.println("My GUID is " + getAID().getName());
		System.out.println("My addresses are:");
		Iterator<String> it = getAID().getAllAddresses();
		while (it.hasNext()) {
			System.out.println("- " +it.next());
		}
		
	}
	
	protected void takeDown() {
		System.out.println("Agent " + getAID().getName() + " terminated");
	}
}
