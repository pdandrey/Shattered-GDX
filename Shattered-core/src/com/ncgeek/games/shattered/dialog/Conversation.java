package com.ncgeek.games.shattered.dialog;

public class Conversation implements DialogListener {

	private final String[] lines;
	
	public Conversation(String[] lines) {
		this.lines = lines;
	}
	
	@Override
	public void dialogFinished(int id) {
		id++;
		if(id < lines.length)
			Dialog.getInstance().setText(lines[id], this, id);
	}

	public void being() {
		Dialog.getInstance().setText(lines[0], this, 0);
	}
}
