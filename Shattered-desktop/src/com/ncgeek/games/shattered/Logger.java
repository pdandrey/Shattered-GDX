package com.ncgeek.games.shattered;

import com.ncgeek.games.shattered.utils.ILogger;

public class Logger implements ILogger {

	@Override
	public void log(String tag, String message, Object... args) {
		System.out.format("[VERBOSE] %s:\t", tag);
		System.out.format(message, args);
		System.out.println();
	}

	@Override
	public void debug(String tag, String message, Object... args) {
		System.out.format("[DEBUG] %s:\t", tag);
		System.out.format(message, args);
		System.out.println();
	}

	@Override
	public void warn(String tag, String message, Object... args) {
		System.out.format("[WARN] %s:\t", tag);
		System.out.format(message, args);
		System.out.println();
	}

	@Override
	public void error(String tag, String message, Object... args) {
		System.out.format("[ERROR] %s:\t", tag);
		System.out.format(message, args);
		System.out.println();
	}

	@Override
	public void error(String tag, Throwable throwable, String message, Object... args) {
		System.out.format("[ERROR] %s:\t", tag);
		System.out.format(message, args);
		if(throwable != null)
			throwable.printStackTrace();
		System.out.println();
	}

}
