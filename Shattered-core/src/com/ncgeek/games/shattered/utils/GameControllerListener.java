package com.ncgeek.games.shattered.utils;

public interface GameControllerListener {
	public void action();
	public void cancel();
	public void move(float x, float y);
	public void menu();
	public void pause();
}
