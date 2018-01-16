package org.golde.discord.pumpbot.commands;

import org.golde.java.discordbotapi.DiscordAPIException;
import org.golde.java.discordbotapi.DiscordCommand;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class CommandAddAlts extends DiscordCommand{

	public CommandAddAlts() throws DiscordAPIException {
		super("addalts");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(IUser user, IChannel channel, IMessage message, String[] args) {
		// TODO Auto-generated method stub
		
	}

}
