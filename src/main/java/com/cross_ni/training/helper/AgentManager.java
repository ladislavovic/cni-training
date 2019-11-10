package com.cross_ni.training.helper;

import java.util.HashMap;
import java.util.Map;

public class AgentManager {
	
	private static Map<String, Agent> agents = new HashMap<>();
	
	public static void runAgent(String agentName) {
		assertAgent(agentName).run();
	}
	
	public static void runAgent(Class clazz) {
		runAgent(clazz.getName());
	}
	
	public static int getAgentRunCount(String agentName) {
		return assertAgent(agentName).getRunCount();
	}
	
	public static int getAgentRunCount(Class clazz) {
		return assertAgent(clazz.getName()).getRunCount();
	}
	
	private static Agent assertAgent(String agentName) {
		return agents.computeIfAbsent(agentName, x -> new Agent());
	}
	
	private static class Agent {
		
		private int runCount;
		
		public Agent() {
			
		}
		
		public void run() {
			runCount++;
		}

		public int getRunCount() {
			return runCount;
		}
	}
	
	
}
