package org.golde.discord.pumpbot;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.golde.discord.pumpbot.commands.CommandAddAlts;
import org.golde.discord.pumpbot.commands.CommandDumpRoles;
import org.golde.discord.pumpbot.commands.CommandSetPump;
import org.golde.java.discordbotapi.DiscordAPIException;
import org.golde.java.discordbotapi.DiscordBot;
import org.golde.java.discordbotapi.DiscordCommand;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class PumpBot extends DiscordBot {
	
	public HashMap<Long, UserState> userStates = new HashMap<Long, UserState>();
	private List<String> pot = new ArrayList<String>();
	private final Random RANDOM = new Random();
	
	private IChannel CHANNEL_DEBUG;
	private IChannel CHANNEL_ANNOUNCEMENTS;
	
	public PumpBot() {
		super(BotConstants.TOKEN);
		setPrefix(">");
	}

	@Override
	public void onMessage(IUser user, IChannel channel, IMessage msg) {
		
		if(channel instanceof IPrivateChannel) {
			
			//Any spam will be illiminated via this line
			if(userStates.containsKey(user.getLongID())) {
				UserState userState = userStates.get(user.getLongID());
				try {
					userState.recieved(msg.getContent());
				} catch(Exception e) {
					String ex = exceptionToString(e);
					channel.sendMessage("Bug! Please report this to @Eric Golde\n" +ex);
					CHANNEL_DEBUG.sendMessage("Bug!\n" + ex);
				}
				
			}
			
		}
		
	}

	@Override
	public void onReady() {
		CHANNEL_DEBUG = getClient().getChannelByID(402680152888442881L);
		CHANNEL_ANNOUNCEMENTS = getClient().getChannelByID(402661048106221569L);
		CHANNEL_DEBUG.sendMessage("Enabled: " + new Date());
		log("Ready!");
	}

	@Override
	public void onShutdown() {
		
	}

	@Override
	public void onTick() {
		for(UserState state : userStates.values()) {
			state.tick();
		}
	}

	@Override
	public void registerCommands(List<DiscordCommand> cmds) throws DiscordAPIException {
		cmds.add(new CommandAddAlts());
		cmds.add(new CommandSetPump());
		cmds.add(new CommandDumpRoles());
	}
	
	private String exceptionToString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
	
	public void addAltsToPump(List<String> alts, long userKey) {
		pot.addAll(alts);
		CHANNEL_ANNOUNCEMENTS.sendMessage(getDiscordServer().getUserByID(userKey).mention() + " added " + alts.size() + " alts to the pump! The pump currently has " + pot.size() + " alts!");
	}
	
	private void drawPot() {
		if(pot.size() > 2000) {
			//only 200 lines per message
			//need to figure this out
		}
		
		List<IUser> users = getDiscordServer().getUsers();
		IUser drawnUser = users.get(RANDOM.nextInt(users.size()));
		while (isAdmin(drawnUser) || drawnUser.isBot()) {
			drawnUser = users.get(RANDOM.nextInt(users.size()));
		}
		
		String alts = "Here are your alts!\n\n";
		for(String alt:pot) {
			alts += alt + "\n";
		}
		//getDMWithUser(drawnUser).sendMessage(alts);
		CHANNEL_DEBUG.sendMessage(alts);
		CHANNEL_DEBUG.sendMessage(drawnUser.mention() + " has won the pump! (" + pot.size() + ")");
		pot.clear();
	}
	
	public boolean isAdmin(IUser user) {
		IGuild server = getDiscordServer();
		return user.getRolesForGuild(server).contains(server.getRoleByID(403266831428747275L));
	}

}
