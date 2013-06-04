package com.ncgeek.games.shattered.dialog;

public class Conversation implements DialogListener {

	private String[] lines;
	
	public Conversation() {
	}
	
	public Conversation(String[] lines) {
		setLines(lines);
	}
	
	public final String[] getLines() { return lines; }
	public final String getLine(int idx) { return lines[idx]; }
	public final void setLines(String[] lines) {
		this.lines = lines;
	}
	
	@Override
	public void dialogFinished(int id) {
		id++;
		if(id < lines.length)
			Dialog.getInstance().setText(lines[id], this, id);
	}

	public void begin() {
		Dialog.getInstance().setText(lines[0], this, 0);
	}
}
