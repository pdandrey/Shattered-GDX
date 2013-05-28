package com.ncgeek.games.shattered.utils;

public interface ILogger {
	public void log(String tag, String message, Object...args);
	public void debug(String tag, String message, Object...args);
	public void warn(String tag, String message, Object...args);
	public void error(String tag, String message, Object...args);
	public void error(String tag, Throwable throwable, String message, Object...args);
}
