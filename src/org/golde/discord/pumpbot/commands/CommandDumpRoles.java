package org.golde.discord.pumpbot.commands;

import org.golde.java.discordbotapi.DiscordAPIException;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class CommandDumpRoles extends PCommandBase{

	public CommandDumpRoles() throws DiscordAPIException {
		super("dumproles");
	}

	@Override
	public void execute(IUser user, IChannel channel, IMessage message, String[] args) {
		String msg = "";
		for(IRole role:getDiscordBot().getDiscordServer().getRoles()) {
			msg += role.getName() + ": " + role.getLongID() + "\n";
		}
		channel.sendMessage(msg);
	}

}
