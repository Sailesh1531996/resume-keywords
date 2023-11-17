package com.example.springboottutorial;


import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Pattern;


@RestController
public class PDFReader {
    private Chapter chapter;

    private Norm normalize;

    private DocumentCollection documentCollection;

    @GetMapping("/read")
    public String PDFReader(@RequestParam(value = "strStart", defaultValue = "Content Page 4") String strStart , @RequestParam(value = "strEnd", defaultValue = "pdf end") String strEnd) throws IOException {
        String returnString = "";
        String returnStringReal = "";
        String returnStringFirst = "";
        String interResult = "";
        String pdfFileInText = "";
        String finalResult = "";
        String docResult = "";
        try {
            String pdfPath ="D:\\! Masters\\TAaDs\\module_manual_IMACS.pdf";
            PDDocument document = PDDocument.load(new File(pdfPath));
            document.getClass();
            if (!document.isEncrypted()) {
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                PDFTextStripper tStripper = new PDFTextStripper();
                tStripper.setStartPage(1);
                tStripper.setEndPage(100);
                pdfFileInText = tStripper.getText(document) + "pdf end";
                //System.out.println(pdfFileInText);
                int startIndex = pdfFileInText.indexOf("Content Page 4" );
                returnStringReal =pdfFileInText.substring(startIndex);
                startIndex = returnStringReal.indexOf(strStart);
                returnStringFirst =returnStringReal.substring(startIndex);
                startIndex = returnStringFirst.indexOf(strStart);
                int endIndex = 0;
                if(!strStart.equals("Content Page 4")) {
                    endIndex = returnStringFirst.indexOf("Name");
                    if(endIndex==-1)
                        endIndex = returnStringFirst.indexOf(strEnd);
                }
                else
                    endIndex = returnStringFirst.indexOf(strEnd);

                returnString = returnStringFirst.substring(startIndex, endIndex);
                //System.out.println(returnString);
                interResult = getResult(returnString);
                //System.out.println(interResult);
                normalize=new Norm();
                String s = normalize.generateNorm(interResult);
                System.out.println(s);
                docResult  = getResult(pdfFileInText);
                String s1 = normalize.generateNorm(docResult);
                System.out.println(s1);
                finalResult = getFrequency(s,s1);

                //System.out.println( interResult);
            }
        } catch (Exception e) {
            finalResult = "No ParaGraph Found";
        }
        return finalResult ;
    }


    private String getResult(String inputString) {
        String result = "";
        ArrayList<Sentence> sentences = sentencizeText(inputString);
        Analyzer analyzer = null;
        analyzer = new EnglishAnalyzer();
        chapter=new Chapter();
        chapter.setSentences(sentences);
        for (Sentence sentence : sentences) {
            //System.out.println(sentence.getOriginal());
            sentence.setLocale(chapter.getLocale());
            TokenStream tokenStream = analyzer.tokenStream("contents", new StringReader(sentence.getOriginal()));
            CharTermAttribute attribute = tokenStream.addAttribute(CharTermAttribute.class);
            try {
                tokenStream.reset();
                while (tokenStream.incrementToken()) {

                    result += attribute.toString()+" ";
                }
                tokenStream.close();
            } catch (IOException e) {}
        }
        result += System.getProperty("line.separator")+System.getProperty("line.separator");
        analyzer.close();
        return result;
    }


    private String getFrequency(String resultString,String docResult) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        HashMap<String, Integer> hashMapResult = getMap(resultString);
        HashMap<String, Integer> hashMapDoc = getMap(docResult);
        for (String key : hashMapResult.keySet()) {
            float moduleFrequency= hashMapResult.get(key);
            float documentFrequency = hashMapDoc.get(key);
            int value = Math.round(moduleFrequency / documentFrequency);
            if(value==1&&moduleFrequency>1)
                hashMap.put(key,hashMapResult.get(key));
        }
        return sortbykey(hashMap);
    }

    private HashMap<String, Integer> getMap(String resultString){
        HashMap<String, Integer> hashMap = new HashMap<>();
        String[] words = resultString.split(" | ");
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        for (String word : words) {
            if (word.length()>1 && !pattern.matcher(word).matches()) {
                Integer integer = hashMap.get(word);
                if (integer == null)
                    hashMap.put(word, 1);
                else {
                    hashMap.put(word, integer + 1);
                }
            }
        }
        return hashMap;
    }

    private ArrayList<Sentence> sentencizeText(String chapter) {
        String[] rawSentences = chapter.split("\\.");
        ArrayList<Sentence> sentences = new ArrayList<>();
        for (String rawSentence : rawSentences) {
            Sentence sentence = new Sentence(rawSentence);
            sentences.add(sentence);
        }
        return sentences;
    }

    private static String sortbykey(HashMap map)
    {
        Set<Map.Entry<String, Integer>> set = map.entrySet();
        List<Map.Entry<String, Integer>> list = new ArrayList<>(
                set);
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        return list.toString() ;
    }



}
