package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 4;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    protected ArrayList<String> wordList;
    protected HashSet<String> wordSet;
    protected HashMap<String,ArrayList<String>> letterToWord;
    protected HashMap<Integer,ArrayList<String>> sizeToWords;
    int wordLength;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        wordLength=DEFAULT_WORD_LENGTH;
        wordList=new ArrayList<>();
        wordSet=new HashSet<>();
        letterToWord=new HashMap<>();
        sizeToWords=new HashMap<>();
        String line;
        String sortedString;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            sortedString=sortString(word);
            if(letterToWord.containsKey(sortedString))
                letterToWord.get(sortedString).add(word);
            else {
                ArrayList<String> stringList=new ArrayList<>();
                stringList.add(word);
                letterToWord.put(sortedString,stringList);
            }
            int lengthOfWord =word.length();
            if(word==null)
            if (sizeToWords.containsKey(lengthOfWord)){
                sizeToWords.get(lengthOfWord).add(word);
            }else {
                ArrayList<String> stringList=new ArrayList<>();
                stringList.add(word);
                sizeToWords.put(lengthOfWord,stringList);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        //if(!word.contains(base)&&wordList.contains(word))
        if(!word.contains(base)&&wordSet.contains(word))
            return true;
        return false;
    }

    public ArrayList<String> getAnagrams(String targetWord) {
//        ArrayList<String> result = new ArrayList<>();
        String sortedString=sortString(targetWord);
        /*for (String word:wordList) {
            if (sortString(word).equals(sortedString))
                result.add(word);
        }*/
        return letterToWord.get(sortedString);
    }

    private String sortString(String word){
        char[] charArray=word.toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
    }



    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<>();
        for (char alphabet='a';alphabet<'z';alphabet++){
            String temp=word.concat(String.valueOf(alphabet));
            String sortedString=sortString(temp);
            if (letterToWord.containsKey(sortedString))
                for (String wordInList:letterToWord.get(sortedString)) {
                    if (isGoodWord(wordInList,word)){
                        result.add(wordInList);
                    }
                }
        }
        return result;
    }

    public ArrayList<String> getAnagramsWithTwoMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<>();
        for (char alphabet='a';alphabet<'z';alphabet++){
            for (char alphabet2='a';alphabet2<'z';alphabet2++) {
                String temp = word.concat(String.valueOf(alphabet)).concat(String.valueOf(alphabet2));
                String sortedString = sortString(temp);
                if (letterToWord.containsKey(sortedString))
                    for (String wordInList : letterToWord.get(sortedString)) {
                        if (isGoodWord(wordInList, word)) {
                            result.add(wordInList);
                        }
                    }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> stringListOfGivenSize=sizeToWords.get(wordLength);
        Log.e("asd",sizeToWords.keySet().toString());
        int index=random.nextInt(stringListOfGivenSize.size());
        int noOfAnagrams;
        do {
            noOfAnagrams=getAnagramsWithOneMoreLetter(stringListOfGivenSize.get(index)).size();
            index++;
        } while (noOfAnagrams<MIN_NUM_ANAGRAMS);
        if (wordLength<MAX_WORD_LENGTH)
            wordLength++;
        return stringListOfGivenSize.get(index-1);
    }
}
