package org.golde.discord.pumpbot.commands;

import org.golde.discord.pumpbot.Main;
import org.golde.discord.pumpbot.UserState;
import org.golde.java.discordbotapi.DiscordAPIException;
import org.golde.java.discordbotapi.DiscordCommand;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;

public class CommandAddAlts extends DiscordCommand {

	public CommandAddAlts() throws DiscordAPIException {
		super("addalts");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(IUser user, IChannel channel, IMessage message, String[] args) {
		IPrivateChannel dm = getDiscordBot().getDMWithUser(user);
		dm.sendMessage("You would like to add some alts I see, please answer the following question.");
		UserState newState = new UserState();
		Main.getPumpBot().userStates.put(user.getLongID(), newState);
		newState.begin(dm);
	}

}
