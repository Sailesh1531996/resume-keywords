package com.example.springboottutorial;

import java.util.ArrayList;
import java.util.Locale;

public class Document extends TextEntity {
	private String text;
	private ArrayList<Chapter> chapters;
	
	public Document(String text, Locale locale) {
		setText(text);
		if(locale != null) {
			setLocale(locale);
		}
		else
			setLocale(new Locale("en", "US"));
	}

	public void setText(String text) {
		this.text = text;
	}



}
