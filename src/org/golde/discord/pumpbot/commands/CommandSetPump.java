package org.golde.discord.pumpbot.commands;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;

import org.golde.discord.pumpbot.Main;
import org.golde.java.discordbotapi.DiscordAPIException;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class CommandSetPump extends PCommandBase{

	private SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
	
	public CommandSetPump() throws DiscordAPIException {
		super("pump");
	}

	@Override
	public void execute(IUser user, IChannel channel, IMessage message, String[] args) {
		
		if(!isAdmin(user)) {
			noPerm(channel, user);
			return;
		}
		
		if(args.length == 0) {
			channel.sendMessage("Please enter the time like so: HH:MM:SS . \nExample. 5 hours 32 min and 1 second would look like 05:32:01.");
			return;
		} 
		else {
			Duration dur = Duration.ofSeconds(Arrays.stream(args[0].split(":")).mapToInt(n -> Integer.parseInt(n)).reduce(0, (n, m) -> n * 60 + m)); //https://stackoverflow.com/questions/8257641/java-how-to-convert-a-string-hhmmss-to-a-duration
			String formattedDur = formatDuration(dur);
			channel.sendMessage("Next pump set for: " + formattedDur + " from now.");
			
			Main.getPumpBot().CHANNEL_PUMP.sendMessage("**" + df.format(new Date()) + "**\nCurrent Alts in Pump: " + Main.getPumpBot().pot.size() + "\nEstimated Pump Time: " + formattedDur + " from now.");
		}
		
	}
	
	private String formatDuration(Duration dur) {
		long s = dur.getSeconds();
		return String.format("%dh %02dm %02ds", s / 3600, (s % 3600) / 60, (s % 60));
	}
	
}
