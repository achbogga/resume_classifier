package textClassify;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

public class Testing {
	private static FileInputStream fileInputStream;
	private static FileOutputStream fileOutputStream;

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
	//Read Test documents into a vectors of words same way as we have done with the training documents
	public void readTestData(String TestingBase, Inference inf) throws IOException, TikaException {
		File DirectoryPath = new File(TestingBase);
		File[] TestFiles = DirectoryPath.listFiles();
		inf.NumberOfTestDocuments = TestFiles.length;
		inf.TestDocumentsAsWords = new String[inf.NumberOfTestDocuments][];
		inf.ProbablitiesOfTestDocs = new BigDecimal[inf.NumberOfTrainedClasses][inf.NumberOfTestDocuments];
		inf.ClassificationResults = new int[inf.NumberOfTestDocuments];
		Tika tika = new Tika();
		inf.TestDocumentNames = new String[TestFiles.length];
		for (int kk=0;kk<TestFiles.length;kk++) {
			String file = TestFiles[kk].getName();
			inf.TestDocumentNames[kk] = file;
			String doc = TestingBase + File.separator + file;
			File d = new File(doc);
			String fc = tika.parseToString(d);
			String[] words = getWords(fc);
			inf.TestDocumentsAsWords[kk] = new String[words.length];
			for (int i=0;i<words.length;i++) {
				inf.TestDocumentsAsWords[kk][i] = words[i];
				//System.out.println(words[i]);
			}
		}
	}
	
	public static void copyFile(File sourceFile, File destFile) throws IOException {
	    if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        fileInputStream = new FileInputStream(sourceFile);
			source = fileInputStream.getChannel();
	        fileOutputStream = new FileOutputStream(destFile);
			destination = fileOutputStream.getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}
	
	public void classify(Inference inf, String TestingBase, String ClassifiedFilesPath) throws IOException {
		for (int j=0;j<inf.NumberOfTrainedClasses;j++){
			for (int k=0;k<inf.NumberOfTestDocuments;k++) {
				inf.ProbablitiesOfTestDocs[j][k]=new BigDecimal(1);
				for (int l=0;l<inf.TestDocumentsAsWords[k].length;l++) {
					if (Arrays.asList(inf.Vocabulary).contains(inf.TestDocumentsAsWords[k][l])) {
						inf.ProbablitiesOfTestDocs[j][k] = inf.ProbablitiesOfTestDocs[j][k].multiply(new BigDecimal(inf.ProbablitiesOfWords[j][Arrays.asList(inf.Vocabulary).indexOf(inf.TestDocumentsAsWords[k][l])]));
						//System.out.println(j+" "+k+" "+l+" "+inf.ProbablitiesOfTestDocs[j][k]);
					}
					else {
						//System.out.println("Word not found");
						inf.ProbablitiesOfTestDocs[j][k] = inf.ProbablitiesOfTestDocs[j][k].multiply( new BigDecimal(0.0000000001));
					}
				}
			}
		}
		File f = new File(ClassifiedFilesPath);
		try{
		    if(f.mkdir()) { 
		        //System.out.println("Directory Created to store Classified Files");
		    } else {
		        //System.out.println("Directory already exists to store Classified Files");
		    }
		} catch(Exception e){
		    e.printStackTrace();
		}
		for (int j=0;j<inf.NumberOfTrainedClasses;j++) {
			File f1 = new File(ClassifiedFilesPath+File.separator+inf.TrainedClasses[j]);
			try{
			    if(f1.mkdir()) {
			        //System.out.println("Directory Created to store Classified Files");
			    } else {
			        //System.out.println("Directory already exists to store Classified Files");
			    }
			} catch(Exception e){
			    e.printStackTrace();
			}
		}
		for (int k=0;k<inf.NumberOfTestDocuments;k++) {
			BigDecimal MaximumProbablity=new BigDecimal(0.00);
			int MaximumProbableClassIndex=inf.NumberOfTrainedClasses-1;
			BigDecimal VNB;
			for (int j=0;j<inf.NumberOfTrainedClasses;j++) {
				VNB = (inf.ProbablitiesOfTestDocs[j][k].multiply(new BigDecimal(inf.ProbablitiesOfTrainedClasses[j])));
				//System.out.println(inf.ProbablitiesOfTrainedClasses[j]*inf.ProbablitiesOfTestDocs[j][k]);
				if(VNB.compareTo(MaximumProbablity)>=1) {
					MaximumProbablity = VNB;
					MaximumProbableClassIndex = j;
				}
				//else MaximumProbableClassIndex = -1;
			}
			inf.ClassificationResults[k] = MaximumProbableClassIndex;
			System.out.println("Document "+inf.TestDocumentNames[k]+" is classified into class "+inf.TrainedClasses[MaximumProbableClassIndex]);
			//System.out.println("Document "+k+" is classified into class "+MaximumProbableClassIndex+" with probablity "+MaximumProbablity);
			File source = new File(TestingBase+File.separator+inf.TestDocumentNames[k]);
			File dest = new File(ClassifiedFilesPath+File.separator+inf.TrainedClasses[MaximumProbableClassIndex]+File.separator+inf.TestDocumentNames[k]);
			copyFile(source, dest);
		}
	}
}
