package org.golde.discord.pumpbot.states;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class UserState {
	
	private boolean isAcceptingAlts = false;
	private static final int TIMEOUT_SECONDS = 30;
	private int sent = 0;
	private static final String[] QUESTIONS = {
			"Have you read the TOS? [y/n]",
			"Do you agree to everything the TOS says? [y/n]",
			"Do you agree to not try to get us into legal trouble if you get in trouble for giving us the account(s)? [y/n]",
			"Do you agree to the fact that we/the TOS can change at any time without notifying users? [y/n]",
			};
	
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
	
	public void recieved(IUser user, IChannel dm, String text) {
		if(sent > QUESTIONS.length) {
			//No more yes or no questions, ask for alts
			if(isAcceptingAlts) {
				parseAlt(user, dm, text);
			} else {
				dm.sendMessage("Great! Your elegiable to send alts! Please send me a hastebin link, a pastebin link, or paste the alts directly into this chat! Type \"done\" when you are finished.");
				isAcceptingAlts = true;
			}
		} 
		else {
			//They responded to the question, now get their responce
			int answer = getAnswer(text);
			if(answer == -1) {
				//Did not answer Yes or no
				dm.sendMessage("Please answer with Yes or No, or wait " + TIMEOUT_SECONDS + " seconds to quit.");
			}
			else if(answer == 1){
				//Yes
				dm.sendMessage("OK! Next question!");
				dm.sendMessage(QUESTIONS[sent++]);
				sent++;
			} 
			else {
				//No
				dm.sendMessage("Sorry, but you are now currently ineligible to give accounts. Please try again later.");
			}
		}
		
	}
	
	private void parseAlt(IUser user, IChannel dm, String text) {
		
	}

	public void begin(IChannel dm) {
		dm.sendMessage(QUESTIONS[0]);
		sent++;
	}
	
	
	
}
