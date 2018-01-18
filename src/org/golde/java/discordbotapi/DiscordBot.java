package org.golde.java.discordbotapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.audio.AudioPlayer;

public abstract class DiscordBot {

	private String prefix = ".";
	private IDiscordClient _client;
	private EventDispatcher _dispatcher;
	private IGuild _guild;
	private List<DiscordCommand> cmds = new ArrayList<DiscordCommand>();
	private boolean botIsRunning = false;
	private volatile boolean botIsReady = false;

	public DiscordBot(String token) {
		logapi("Starting bot...");
		ClientBuilder clientBuilder = new ClientBuilder(); 
		clientBuilder.withToken(token); 
		_client = clientBuilder.login();
		_dispatcher = _client.getDispatcher();

		_dispatcher.registerListener(this);

		if(_client != null && _dispatcher != null) {
			botIsRunning = true;
		}

		final Thread mainThread = Thread.currentThread();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				botIsRunning = false;
				try {
					mainThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		try {
			registerCommands(cmds);
			//cmds.add(new EricCommand());
		} catch (DiscordAPIException e) {
			e.printStackTrace();
		}

		for(DiscordCommand cmd:cmds) {
			_dispatcher.registerListener(cmd);
		}
		logapi("Logging in..");

	}

	public final void run() {
		logapi("Run called!");
		while(botIsRunning) {

			if(botIsReady) {
				logapi("Hello World");
				onTick();
				for(DiscordCommand command:cmds) {
					command.tick();
				}
			} 
			
		}

		shutdown();
	}

	public abstract void registerCommands(List<DiscordCommand> cmds) throws DiscordAPIException;
	public abstract void onReady();
	public abstract void onTick();
	public abstract void onMessage(IUser user, IChannel channel, IMessage message);
	public abstract void onShutdown();

	public final void shutdown() {
		for(IVoiceChannel channel : _client.getConnectedVoiceChannels()) {
			channel.leave();
		}
		logapi("Shutting down...");
		botIsRunning = false;
		onShutdown();
		for(DiscordCommand command:cmds) {
			command.tick();
		}
		_client.invisible();
		_client.logout();
		System.exit(0);
	}

	@EventSubscriber
	public final void onReady(ReadyEvent event) {
		try {
			_guild = _client.getGuilds().get(0);
		} 
		catch(Exception e) {
			logapi("Join this bot to the server: ");
			logapi("    (Recommended) All permissions: https://discordapp.com/oauth2/authorize?client_id=INSERT_CLIENT_ID_HERE&scope=bot&permissions=2146958591");
			logapi("                  No  Permissions: https://discordapp.com/oauth2/authorize?client_id=INSERT_CLIENT_ID_HERE&scope=bot&permissions=0");
			shutdown();
		}


		for(DiscordCommand command:cmds) {
			command.setup(this);
			command.onReady();
		}

		_client.online();

		logapi("Enabled!");

		botIsReady = true;
		onReady();
	}



	@EventSubscriber
	public final void onMessage(MessageReceivedEvent event) {
		try {
			IMessage message = event.getMessage();
			IUser user = message.getAuthor();
			IChannel channel = message.getChannel();

			if (user.isBot()) return;

			String[] split = message.getContent().split(" ");

			if (split.length >= 1 && split[0].startsWith(prefix)) {

				String command = split[0].replaceFirst(prefix, "");
				String[] args = split.length >= 2 ? Arrays.copyOfRange(split, 1, split.length) : new String[0];
				for(DiscordCommand ac:cmds) {
					if(ac.getCmd().equals(command)) {
						ac.execute(user, channel, message, args);
					}
				}

			} else {
				try {
					onMessage(user, channel, message);
				}
				catch(RateLimitException e) {
					logapi("Rate limit exceeded");
					e.printStackTrace();
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}



	public final IPrivateChannel getDMWithUser(IUser user) {
		return _client.getOrCreatePMChannel(user);
	}

	public final String getNicknameForUser(IUser user) {
		return user.getNicknameForGuild(_guild);
	}

	public final void setUsername(String name) {
		_client.changeUsername(name);
	}

	public final void setPlaying(String msg) {
		_client.changePlayingText(msg);
	}

	public final AudioPlayer getAudioPlayer() {
		return AudioPlayer.getAudioPlayerForGuild(_guild);
	}

	public final String getPrefix() {
		return prefix;
	}

	public final void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public final IGuild getDiscordServer() {
		return _guild;
	}

	public final IDiscordClient getClient() {
		return _client;
	}


	//MiscUtil
	private final void logapi(String msg) {
		System.out.println("[DiscordBotAPI] " + msg);
	}

	public final void log(String msg) {
		System.out.println("[DiscordBot] " + msg);
	}
}
