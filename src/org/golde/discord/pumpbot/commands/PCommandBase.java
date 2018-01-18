package org.golde.discord.pumpbot.commands;

import org.golde.discord.pumpbot.Main;
import org.golde.java.discordbotapi.DiscordAPIException;
import org.golde.java.discordbotapi.DiscordCommand;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public abstract class PCommandBase extends DiscordCommand {

	public PCommandBase(String cmd) throws DiscordAPIException {
		super(cmd);
	}
	
	public final boolean isAdmin(IUser user) {
		return Main.getPumpBot().isAdmin(user);
	}
	
	public final void noPerm(IChannel channel, IUser user) {
		channel.sendMessage("You do not have permission to run that command " + user.mention() + ".");
	}

}
