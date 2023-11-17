package com.example.springboottutorial;

import java.util.ArrayList;

public class Sentence extends TextEntity {
	private String original;
	private ArrayList<String> terms;
	
	public Sentence() {
		terms = new ArrayList<>();
		original = "";
	}
	
	public Sentence(String sentence) {
		this();
		original = sentence;
	}

	public String getOriginal() {
		return original;
	}


	
}
