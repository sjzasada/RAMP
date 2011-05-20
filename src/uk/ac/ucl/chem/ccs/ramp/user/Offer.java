/*
 * Created on 10:30:32 20 May 2011
 * Project: RAMP 
 * File: Offer.java
 * 
 * @author stefan
 * 
 * TODO: 
 */
package uk.ac.ucl.chem.ccs.ramp.user;

import jade.core.AID;

public class Offer {

	AID agent;
	int price;
	String conversation;
	/**
	 * @param agent
	 * @param price
	 */
	public Offer(AID agent, int price, String coversation) {
		this.agent = agent;
		this.price = price;
		this.conversation = conversation;
	}
	/**
	 * @return the agent
	 */
	public AID getAgent() {
		return agent;
	}
	/**
	 * @param agent the agent to set
	 */
	public void setAgent(AID agent) {
		this.agent = agent;
	}
	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Offer [agent=" + agent + ", price=" + price + "]";
	}
	/**
	 * @return the conversation
	 */
	public String getConversation() {
		return conversation;
	}
	/**
	 * @param conversation the conversation to set
	 */
	public void setConversation(String conversation) {
		this.conversation = conversation;
	}
	
	
	
	
}
