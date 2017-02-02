package textClassify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

public class Inference {
	String[] Vocabulary, TrainedClasses, TestDocumentNames;
	int NumberOfTrainedClasses,NumberOfTestDocuments;
	String[][] TestDocumentsAsWords;
	double[] ProbablitiesOfTrainedClasses;
	BigDecimal[][] ProbablitiesOfTestDocs;
	double[][] ProbablitiesOfWords;
	int[] ClassificationResults;
	
	public void readData(String TrainingParameters) throws FileNotFoundException, IOException{
		//String everything;
		//Reading the trained parameters into Inference attributes if exist already
		try(BufferedReader br = new BufferedReader(new FileReader(TrainingParameters+File.separator+"ClassProbablities.txt"))) {
		    String line = br.readLine();
		    int j=0;
		    while (line != null) {
		    	j++;
		        line = br.readLine();
		    }
		    TrainedClasses = new String[j];
		    ProbablitiesOfTrainedClasses = new double[j];
		    ProbablitiesOfWords = new double[j][];
		    NumberOfTrainedClasses = j;
		}
		try(BufferedReader br = new BufferedReader(new FileReader(TrainingParameters+File.separator+"ClassProbablities.txt"))) {
		    String line = br.readLine();
		    int j=0;
		    while (line != null) {
		    	String[] terms = line.split("\\s+");
			    TrainedClasses[j] = terms[0];
		    	ProbablitiesOfTrainedClasses[j] = Double.parseDouble(terms[1]);
		    	j++;
		        line = br.readLine();
		    }
		}
		try(BufferedReader br = new BufferedReader(new FileReader(TrainingParameters+File.separator+"WordProbablities.txt"))) {
		    String line = br.readLine();
		    int k=0;
		    while (line != null) {
		    	String[] terms = line.split("\\s+");
		    	if (terms[0].equals(TrainedClasses[0])) {
		    		k++;
		    	}
		    	else {
		    		//System.out.println(NumberOfTrainedClasses);
		    		//System.out.println("Inferred Vocab Length: "+k);
		    		Vocabulary = new String[k];
		    		for (int i=0;i<NumberOfTrainedClasses;i++) {
		    			ProbablitiesOfWords[i] = new double[k];
		    		}
		    		break;
		    	}
		        line = br.readLine();
		    }
		}
		try(BufferedReader br = new BufferedReader(new FileReader(TrainingParameters+File.separator+"WordProbablities.txt"))) {
		    String line = br.readLine();
		    int j=0;
		    int k=0;
		    while (line != null) {
		    	//System.out.println(j+" "+k);
		    	String[] terms = line.split("\\s+");
		    	if (terms[0].equals(TrainedClasses[0])) {
		    		Vocabulary[k] = terms[1];
		    		ProbablitiesOfWords[j][k] = Double.parseDouble(terms[2]);
		    		k++;
		    	}
		    	else if(terms[0].equals(TrainedClasses[j])) {
		    		ProbablitiesOfWords[j][k] = Double.parseDouble(terms[2]);
		    		k++;
		    	}
		    	else {
		    		j++;
		    		k=0;
		    		ProbablitiesOfWords[j][k] = Double.parseDouble(terms[2]);
		    		k++;
		    	}
		        line = br.readLine();
		    }
		}
		//Done Inferring Trained data from training parameters files into Inference Class;
		System.out.println("Found the previously trained parameters \n Reading the data from files....");
		System.out.println("Done Inferring Trained data from training parameters files into Inference Class");
	}
	
}
