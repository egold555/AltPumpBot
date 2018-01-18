package org.golde.discord.pumpbot.commands;

import java.time.Duration;
import java.util.Arrays;

import org.golde.java.discordbotapi.DiscordAPIException;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class CommandSetPump extends PCommandBase{

	public CommandSetPump() throws DiscordAPIException {
		super("nextpump");
	}

	@Override
	public void execute(IUser user, IChannel channel, IMessage message, String[] args) {
		
		if(!isAdmin(user)) {
			noPerm(channel, user);
			return;
		}
		
		if(args.length == 0) {
			showExample(channel);
			return;
		} 
		else {
			Duration dur = Duration.ofSeconds(Arrays.stream(args[0].split(":")).mapToInt(n -> Integer.parseInt(n)).reduce(0, (n, m) -> n * 60 + m)); //https://stackoverflow.com/questions/8257641/java-how-to-convert-a-string-hhmmss-to-a-duration
			channel.sendMessage("To Days: " + dur.toDays() + "\nTo Hours: " + dur.toHours() + "\nTo Min: " + dur.toMinutes() + "\nToMills: " + dur.toMillis());
			channel.sendMessage("toStringDur: " + dur.toString());
		}
		
	}
	
	private void showExample(IChannel channel) {
		channel.sendMessage("Please enter the time like so: HH:MM:SS . \nExample. 5 hours 32 min and 1 second would look like 05:32:01.");
	}

}
