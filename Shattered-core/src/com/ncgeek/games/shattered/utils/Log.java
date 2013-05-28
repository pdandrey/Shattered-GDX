package com.ncgeek.games.shattered.utils;

import java.util.HashSet;
import java.util.Set;

public class Log {

	private static ILogger logger;
	private static Set<String> setLogOnce;
	
	static {
		setLogOnce = new HashSet<String>();
	}
	
	public static void setLogger(ILogger logger) { Log.logger = logger; }
	
	public static void log(String tag, String msg) { log(tag, msg, new Object[0]); }
	public static void log(String tag, String message, Object...args) {
		logger.log(tag, message, args);
	}
	
	public static void logOnce(String tag, String key, String message, Object...args) {
		if(!setLogOnce.contains(key)) {
			setLogOnce.add(key);
			log(tag, message, args);
		}
	}
	
	public static void debug(String tag, String msg) { debug(tag, msg, new Object[0]); }
	public static void debug(String tag, String message, Object...args) {
		logger.debug(tag, message, args);
	}
	
	public static void warn(String tag, String msg) { warn(tag, msg, new Object[0]); }
	public static void warn(String tag, String msg, Object...args) {
		logger.warn(tag, msg, args);
	}
	
	public static void error(String tag, String msg) { error(tag, msg, new Object[0]); }
	public static void error(String tag, String msg, Object...args) { logger.error(tag, msg,  new Object[0]); }
	
	public static void error(String tag, Throwable tr, String msg) { error(tag, msg, tr, new Object[0]); }
	public static void error(String tag, Throwable tr, String msg, Object...args) {
		logger.error(tag, tr, msg, args);
	}
	
	private Log() { }
}
