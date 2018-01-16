package org.golde.discord.pumpbot;

import java.util.List;

import org.golde.java.discordbotapi.DiscordAPIException;
import org.golde.java.discordbotapi.DiscordBot;
import org.golde.java.discordbotapi.DiscordCommand;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class PumpBot extends DiscordBot{

	public PumpBot() {
		super(BotConstants.TOKEN);
	}

	@Override
	public void onMessage(IUser arg0, IChannel arg1, IMessage arg2) {
		
	}

	@Override
	public void onReady() {
		
	}

	@Override
	public void onShutdown() {
		
	}

	@Override
	public void onTick() {
		
	}

	@Override
	public void registerCommands(List<DiscordCommand> arg0) throws DiscordAPIException {
		
	}

}
