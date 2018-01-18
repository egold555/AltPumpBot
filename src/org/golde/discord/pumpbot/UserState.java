package org.golde.discord.pumpbot;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;

public class UserState {

	private final IUser USER;
	private final IPrivateChannel DM;
	
	private long startTime = -1;
	
	private boolean isAcceptingAlts = false;
	private static final int TIMEOUT_SECONDS = 30;
	private int sent = 0;
	private static final String[] QUESTIONS = {
			"Have you read the TOS? [y/n]",
			"Do you agree to everything the TOS says? [y/n]",
			"Do you agree to not try to get us into legal trouble if you get in trouble for giving us the account(s)? [y/n]",
			"Do you agree to the fact that we/the TOS can change at any time without notifying users? [y/n]",
	};
	
	public UserState(IUser user, IPrivateChannel dm) {
		this.USER = user;
		this.DM = dm;
	}
	
	/*
	 * Set user id long on begin, as well as DM channel
	 * No need to pass it throughout functions
	 */

	private int getAnswer(String t) {
		t = t.toLowerCase();
		if(t.contains("y")) {
			return 1;
		}
		else if(t.contains("n")) {
			return 0;
		} else {
			return -1;
		}
	}

	public void recieved(String text) throws Exception {
		startTime = System.currentTimeMillis();
		if(sent > QUESTIONS.length) {
			//No more yes or no questions, ask for alts
			if(isAcceptingAlts) {
				if(text.equalsIgnoreCase("done")) {
					done();
					return;
				}
				parseAlt(text);
			} else {
				DM.sendMessage("Great! Your elegiable to send alts! Please send me a hastebin link, a pastebin link, or paste the alts directly into this chat! Type \"done\" when you are finished.");
				isAcceptingAlts = true;
				
			}
		} 
		else {
			//They responded to the question, now get their responce
			int answer = getAnswer(text);
			if(answer == -1) {
				//Did not answer Yes or no
				DM.sendMessage("Please answer with Yes or No, or wait " + TIMEOUT_SECONDS + " seconds to quit.");
			}
			else if(answer == 1){
				//Yes
				DM.sendMessage("OK! Next question!");
				DM.sendMessage(QUESTIONS[sent++]);
				sent++;
			} 
			else {
				//No
				DM.sendMessage("Sorry, but you are now currently ineligible to give accounts. Please try again later.");
				remove();
			}
		}
	}
	
	private void done() {
		DM.sendMessage("Thanks for adding " + alts.size() + " to the pump!");
		Main.getPumpBot().addAltsToPump(alts, USER.getLongID());
		remove();
	}

	private List<String> alts = new ArrayList<String>();
	private void parseAlt(String text) {
		String[] lines = text.split("\\r?\\n");
		for(String line:lines) {

			if(line.startsWith("https") && line.contains("astebin.com/")) {
				String inserted = line;
				if(!line.contains(".com/raw/")) {
					inserted = insertAfter(line, "https://pastebin.com/", "raw/");
					inserted = insertAfter(inserted, "https://hastebin.com/", "raw/");
				}
				try {
					URLConnection openConnection = new URL(inserted).openConnection();
					openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
					InputStream is = openConnection.getInputStream();
					Scanner sc = new Scanner(is);
					while(sc.hasNext()) {
					    String scanned = sc.next();
					    if(isAlt(scanned)) {
							alts.add(scanned);
						}
					}
					sc.close();
				} 
				catch (MalformedURLException e) {
					DM.sendMessage("That is not a vallid url, or something went wrong.. ");
				} 
				catch (IOException e) {
					DM.sendMessage("That website didn't work. It returned a " + e.toString());
				}
			}
			else {
				if(isAlt(line)) {
					alts.add(line);
				}
			}
		}
		DM.sendMessage(alts.size() + " added so far! Type in \"done\" or wait " + TIMEOUT_SECONDS + " seconds to finish.");
	}
	
	private boolean isAlt(String line) {
		return (line.contains(":") && line.length() > 10);
	}

	private String insertAfter(String before, String afterThis, String insert)
	{
		int index = before.indexOf(afterThis);
		if (index < 0) 
			return before;
		index += afterThis.length();
		return before.substring(0, index) + insert + before.substring(index);
	}

	public void begin() {
		DM.sendMessage(QUESTIONS[0]);
		sent++;
	}
	
	private void timesUp() {
		DM.sendMessage("Timed out.");
		remove();
	}
	
	private void remove() {
		Main.getPumpBot().userStates.remove(USER.getLongID());
	}

	public void tick() {
		/*if(startTime != -1) {
			long estimatedTime = System.currentTimeMillis() - startTime;
			Main.getPumpBot().log("ETA: " + estimatedTime);
			if(estimatedTime < TIMEOUT_SECONDS) {
				timesUp();
				startTime = -1;
			}
		}*/
	}

}
