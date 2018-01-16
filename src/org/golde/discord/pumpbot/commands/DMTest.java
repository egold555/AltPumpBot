package org.golde.discord.pumpbot.commands;

import org.golde.java.discordbotapi.DiscordAPIException;
import org.golde.java.discordbotapi.DiscordCommand;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class DMTest extends DiscordCommand{

	public DMTest() throws DiscordAPIException {
		super("dmtest");
	}

	@Override
	public void execute(IUser user, IChannel channel, IMessage message, String[] args) {
		getDiscordBot().getDMWithUser(user).sendMessage("DM Test!");
	}

}
