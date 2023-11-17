package com.example.springboottutorial;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;

public class TextEntity {
	
	private String name;
	private Locale locale;
	private HashMap<String, Integer> frequencyMap = new HashMap<>();

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}


}
