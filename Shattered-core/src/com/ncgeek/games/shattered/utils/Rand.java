package com.ncgeek.games.shattered.utils;

import java.util.Random;

public final class Rand {
	
	private static final Random random = new Random();
	
	public static final int next() { return random.nextInt(); }
	public static final int next(int max) { return random.nextInt(max); }
	public static final int next(int min, int max) { return random.nextInt(max - min) + min; }
	
	private Rand() {}
}
