package textClassify;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

//A table to hold word frequencies for each word in the vocabulary over each document vector and class
public class WordFrequencyTable {
	int[][][] FrequencyTable;
	int[][] nk;//Used in computing the Word Probablities
	int[] n;
	double[] ProbablitiesOfClasses;//Ex: Number of + docs/Total number of docs is stored in 0, and so on...
	double[][] ProbablitiesOfDocs;//Ex: Product of Word Probablities i.e., P(doc/Vj); Ex: P(doc/+) = for all words in the vocabulary -> Product(P(W1/+)*P(W2/+)*...P(Wk/+))
	double[][] ProbablitiesOfWords;//P(Wk/Vj) as said above
	
	public int GetWordIndexInVocabulary(String word, Set<String> vocab) {
		Iterator<String> itr = vocab.iterator();
		int index=-1;
        while(itr.hasNext()) {
        	String w = itr.next();
        	index++;
        	if(w.equals(word)) {
        		//System.out.println(w+" "+index); for debugging purposes
        		return index;
        	}
        }
        System.out.println("Trouble here: "+word);//for future usage and debugging purposes
		return -1;
	}
	
	public void ComputeFrequencies(BagOfWords bow) throws FileNotFoundException, UnsupportedEncodingException {
		ProbablitiesOfClasses = new double[bow.NumberOfClasses];
		ProbablitiesOfDocs = new double[bow.NumberOfClasses][];
		ProbablitiesOfWords = new double[bow.NumberOfClasses][bow.Vocabulary.size()];
		nk = new int[bow.NumberOfClasses][bow.Vocabulary.size()];
		n = new int[bow.NumberOfClasses];
		FrequencyTable = new int[bow.NumberOfClasses][][];
		for (int k=0;k<bow.NumberOfClasses;k++) {
			FrequencyTable[k] = new int[bow.NumberOfDocumentsPerClass[k]][];
			ProbablitiesOfDocs[k] = new double[bow.NumberOfDocumentsPerClass[k]];
			int TotalNumberOfDocs = 0;
			for (int kk=0;kk<bow.NumberOfDocumentsPerClass[k];kk++) {
				TotalNumberOfDocs += bow.NumberOfDocumentsPerClass[k];
				FrequencyTable[k][kk] = new int[bow.Vocabulary.size()];
				Map<String, Integer> myMap = new HashMap<String, Integer>();
				Iterator<String> itr = bow.Vocabulary.iterator();
		        while(itr.hasNext())
		        {
		        	String word = itr.next();
		        	myMap.put(word, 0);
		            //Create an empty hashmap to compute frequencies
		        }
		        for (int i=0;i<bow.DocumentsAsWords[k][kk].length;i++) {
		        	int updatedFrequency = myMap.get(bow.DocumentsAsWords[k][kk][i])+1;
		        	myMap.put(bow.DocumentsAsWords[k][kk][i], updatedFrequency);
		        }
		        itr = bow.Vocabulary.iterator();
		        for (int l=0;l<FrequencyTable[k][kk].length;l++) {
		        	FrequencyTable[k][kk][l] = myMap.get(itr.next());
		        }
			}
			ProbablitiesOfClasses[k] = ((double)bow.NumberOfDocumentsPerClass[k])/((double)TotalNumberOfDocs);
		}
		for (int j=0;j<bow.NumberOfClasses;j++) {
			n[j]=0;
			for (int k=0;k<bow.DocumentsAsWords[j].length;k++) {
				for (int l=0;l<bow.DocumentsAsWords[j][k].length;l++) {
					n[j]++;
				}
			}
			for (int k=0;k<bow.Vocabulary.size();k++) {
				nk[j][k]=1;
				for (int d=0;d<FrequencyTable[j].length;d++){
					nk[j][k] += FrequencyTable[j][d][k];
				}
				ProbablitiesOfWords[j][k] = ((double)nk[j][k])/((double)(n[j]+bow.Vocabulary.size()));
			}
			for (int kk=0;kk<bow.NumberOfDocumentsPerClass[j];kk++) {
				ProbablitiesOfDocs[j][kk]=1;
				for (int l=0;l<bow.DocumentsAsWords[j][kk].length;l++) {
					ProbablitiesOfDocs[j][kk] *= ProbablitiesOfWords[j][GetWordIndexInVocabulary(bow.DocumentsAsWords[j][kk][l],bow.Vocabulary)];
				}
			}
		}
	}
}
