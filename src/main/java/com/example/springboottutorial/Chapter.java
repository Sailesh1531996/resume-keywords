package com.example.springboottutorial;

import java.util.ArrayList;

public class Chapter extends TextEntity {
	private String text;
	private ArrayList<Sentence> sentences;

	public Chapter() {
		sentences = new ArrayList<>();
	}


	public void setSentences(ArrayList<Sentence> sentences) {
		this.sentences = sentences;
	}
	
}
