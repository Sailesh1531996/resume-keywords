package com.example.springboottutorial;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Norm {
	public  String  generateNorm( String input ) {
		Analyzer analyzer = createAnalyzer(2);
		List<String> nGrams = generateNgrams(analyzer, input);
		String output ="";
		for (String nGram : nGrams) {
			output = output + nGram + " | " ;
		}
		return output;
	}


	public static Analyzer createAnalyzer( final int shingles )
	{
		return new Analyzer() {
			@Override
			protected TokenStreamComponents createComponents(  String field )
			{
				final Tokenizer source = new WhitespaceTokenizer();
				final ShingleFilter shingleFilter = new ShingleFilter( new LowerCaseFilter( source ), shingles );
				shingleFilter.setOutputUnigrams( true );
				return new TokenStreamComponents( source, shingleFilter );
			}
		};
	}


	public static List<String> generateNgrams( Analyzer analyzer, String str )
	{
		List<String> result = new ArrayList<>();
		try {
			TokenStream stream = analyzer.tokenStream( null, new StringReader( str ) );
			stream.reset();
			while ( stream.incrementToken() ) {
				String nGram = stream.getAttribute( CharTermAttribute.class ).toString();
				result.add( nGram );
				//System.out.println( "Generated N-gram = {}"+ nGram );
			}
		} catch ( IOException e ) {
			System.out.println( "IO Exception occured! {}" + e );
		}
		return result;
	}
}
