package textClassify;
//General overview of what this class does
//Training Phase
//Code to compute the following in order
/*
 * 1. Vocabulary of all the unique words in the training data
 * 2. A table with all the word frequencies along with which document class and which document they belong to
 * 3. for each document class Vj,  P(Vj)
 * 4. for each class and each word in the vocabulary Wk, P(Wk/Vj)
 * 5. Write the Vocabulary, and probabilities to a file.
 * */
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

public class BagOfWords {
	Set<String> Vocabulary = new HashSet<String>();
	int NumberOfClasses;
	int[] NumberOfDocumentsPerClass;
	String [] DocumentClasses;
	String [][][] DocumentsAsWords;

//Bad Data Management
	public String [] getWords(String s) {
		String[] words = s.split("\\s+");
		for (int i = 0; i < words.length; i++) {
		    // You may want to check for a non-word character before blindly performing a replacement
		    // It may also be necessary to adjust the character class
		    words[i] = words[i].replaceAll("[^\\w]", "");
		    words[i] = words[i].toLowerCase();// Also convert all the words into smaller case for consistency
		    if ( (words[i].equals(" ")) || words[i].equals("") ) words[i] = "whitespace";
		}
		return words;
	}

//Reads all the docs and parses them into word vectors.
	public void readTrainingFiles(String tbase) throws IOException, TikaException {
		//tbase is a directory of the training database
		File DirectoryPath = new File(tbase);
		File[] folders = DirectoryPath.listFiles();
		NumberOfClasses = folders.length;
		DocumentClasses = new String[NumberOfClasses];
		DocumentsAsWords = new String[NumberOfClasses][][];
		NumberOfDocumentsPerClass = new int[NumberOfClasses];
		for (int k=0;k<folders.length;k++) {
			DocumentClasses[k] = folders[k].getName();
			File pdir = new File(tbase+File.separator+DocumentClasses[k]);//Give the directory name of document class
			File[] files = pdir.listFiles();
			Tika tika = new Tika();
			NumberOfDocumentsPerClass[k] = files.length;
			DocumentsAsWords[k] = new String[NumberOfDocumentsPerClass[k]][];
			for (int kk=0;kk<files.length;kk++) {
				String file = files[kk].getName();
				String doc = pdir + File.separator + file;
				File d = new File(doc);
				String fc = tika.parseToString(d);
				String[] words = getWords(fc);
				DocumentsAsWords[k][kk] = new String[words.length];
				for (int i=0;i<words.length;i++) {
					Vocabulary.add(words[i]);
					DocumentsAsWords[k][kk][i] = words[i];
				}
			}
		}
	}
}
