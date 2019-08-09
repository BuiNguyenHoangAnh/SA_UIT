package com.journaldev.sparkdemo;

import java.util.Set;

public class Correction {
	/*
	 * declare variables
	 */
	private static Set<String> stopWordSet;
	
	/*
	 * loai bo stop word
	 */
	// read a file
	
	// read each line in file and push words into an array
	
	// check if word was a stop word
	public static boolean isStopWord(String word) {
		if(word.length() < 2)
			return true;
		if(word.charAt(0) >= '0' && word.charAt(0) <= '9')
			return true;
		if(stopWordSet.contains(word))
			return true;
		return false;
	}
	
	/*
	 * xu li tu viet tat
	 */
	
	/*
	 * xu li tu sai chinh ta
	 */
	 
	/*
	 * xu li tieng long/ vung mien
	 */
	
}
