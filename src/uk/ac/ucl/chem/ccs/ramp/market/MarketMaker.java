/*
 * Created on 11:40:36 20 May 2011
 * Project: RAMP 
 * File: MarketMaker.java
 * 
 * @author stefan
 * 
 * TODO: 
 */
package uk.ac.ucl.chem.ccs.ramp.market;

import java.util.HashMap;

import uk.ac.ucl.chem.ccs.ramp.user.Offers;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class MarketMaker extends Agent {

	
	HashMap<String, Integer> bank = new HashMap<String, Integer>();
	HashMap<String, Offers> reservations = new HashMap<String, Offers>();
	
	protected void setup () {
		
		//add some users to bank
		//TODO: make the banking data persistent
		
		bank.put("stef", 100);
		bank.put("Able", 100);
		
	}
	
	protected class BankingBehaviour extends CyclicBehaviour {


		public void action() {
			// TODO Auto-generated method stub
			
			
			
		}
		
	}
	
}
