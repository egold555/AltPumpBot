package org.golde.java.discordbotapi;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public abstract class DiscordCommand {

	private final String theCMD;
	private DiscordBot bot;
	
	public DiscordCommand(String cmd) throws DiscordAPIException {
		if(cmd.equalsIgnoreCase("about") && !(this instanceof EricCommand)) {
			throw new DiscordAPIException("Can not override built in command!");
		}
		
		if(cmd.contains(" ")) {
			throw new DiscordAPIException("Command name can not have spaces in it!");
		}
		
		this.theCMD = cmd;
	}
	
	public final void setup(DiscordBot bot) {
		this.bot = bot;
	}
	
	public String getCmd() {
		return theCMD;
	}
	
	public void shutdown() {}
	public void tick() {}
	public void onReady() {};
	
	public DiscordBot getDiscordBot() {
		return bot;
	}
	
	public abstract void execute(IUser user, IChannel channel, IMessage message, String[] args);
	
}

