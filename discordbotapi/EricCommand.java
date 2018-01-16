package org.golde.java.discordbotapi;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

public class EricCommand extends DiscordCommand {
	
	public EricCommand() throws DiscordAPIException {
		super("about");
	}

	@Override
	public void execute(IUser user, IChannel channel, IMessage message, String[] args) {
		EmbedBuilder builder = new EmbedBuilder();

		builder.appendDesc("Hi " + user.getName() + "! This bot has been made with Eric's DiscordBotAPI! Click my name to head on over to the github page to get started making your own bot!");
		
	    builder.withAuthorName("DiscordBotAPI");
	    builder.withAuthorUrl("https://github.com/egold555/DiscordBotAPI/");
	    builder.withAuthorIcon("https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png");
		
		
		channel.sendMessage(builder.build());
	}

	
	
}
