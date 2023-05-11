/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agents;


import jade.core.Agent;

import jade.wrapper.AgentController;


public class StarterAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static AgentController ac;
	static AgentController novoAgent ;

	protected void setup() 
	{ 
		System.out.println(getLocalName()); 

			// create agent
			try {

				ac = VMManager.platform.createNewAgent("Manager", "agents.ManagerG", null);
				ac.start();

			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			doDelete();
	}
	protected void takeDown(){
		System.out.println("Agent "+ getAID().getLocalName() +" finishing.");
		
	}


}
