package com.ncgeek.games.shattered;

import com.ncgeek.games.shattered.utils.ILogger;

import android.util.Log;

public class Logger implements ILogger {

	@Override
	public void log(String tag, String message, Object... args) {
		Log.v(tag, String.format(message, args));
	}

	@Override
	public void debug(String tag, String message, Object... args) {
		Log.d(tag, String.format(message, args));
	}

	@Override
	public void warn(String tag, String message, Object... args) {
		Log.w(tag, String.format(message, args));
	}

	@Override
	public void error(String tag, String message, Object... args) {
		Log.e(tag, String.format(message, args));
	}

	@Override
	public void error(String tag, Throwable throwable, String message, Object... args) {
		Log.e(tag, String.format(message, args), throwable);
	}

}
