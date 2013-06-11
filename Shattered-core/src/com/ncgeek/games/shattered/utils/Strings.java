package com.ncgeek.games.shattered.utils;

import java.util.Collection;
import java.util.Iterator;

public final class Strings {

	public static String join(String delim, Collection<String> strings) {
		StringBuilder buf = new StringBuilder();
		for(Iterator<String> i = strings.iterator(); i.hasNext(); ) {
			buf.append(i.next());
			if(i.hasNext())
				buf.append(delim);
		}
		return buf.toString();
	}
	
	public static String join(String delim, String[] strings) {
		StringBuilder buf = new StringBuilder();
		for(int i=0; i<strings.length; ++i) {
			buf.append(strings[i]);
			if(i < strings.length-1)
				buf.append(delim);
		}
		return buf.toString();
	}
	
	private Strings() {}
}
