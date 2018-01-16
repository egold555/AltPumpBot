package org.golde.discord.pumpbot.states;

public abstract class StateMachineAbstract {
	
	public abstract Question[] getQuestions();
	
	int sent = 0;
	
	public void recieved() {
		sent++;
		if(getQuestions().length > sent) {
			askQuestion(getQuestions()[sent]);
		} 
		else {
			finished();
		}
	}
	
	public abstract void askQuestion(Question question);
	public abstract void finished();
}

abstract class Question {
	
	private final String question;
	
	public Question(String question) {
		this.question = question;
	}
	
	public abstract boolean isAccepted(String input);

	public String getQuestion() {
		return question;
	}
}

/*abstract class QuestionInput extends Question {

	public QuestionInput(String question) {
		super(question);
	}
	
}*/

class QuestionYesNo extends Question {

	public QuestionYesNo(String question) {
		super(question);
	}

	@Override
	public boolean isAccepted(String input) {
		return (input.contains("y") || input.contains("n"));
	}
	
}
