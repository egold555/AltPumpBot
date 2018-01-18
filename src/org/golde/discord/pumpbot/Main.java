package org.golde.discord.pumpbot;

public class Main {

	private static PumpBot bot;
	
	public static void main(String[] args) {
		bot = new PumpBot();
		System.out.println("Calling run!");
		bot.run();
	}
	
	public static PumpBot getPumpBot() {
		return bot;
	}

}
