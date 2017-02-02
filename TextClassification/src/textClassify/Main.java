package textClassify;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.tika.exception.TikaException;

//Document Classifier
//Uses Naive Bayesian Inference to classify
//Author: achyutsarmaboggaram@gmail.com

public class Main {

	public static void ReceiveDocumentsFromEmail(String WorkingPath) throws IOException, TikaException {
		EmailAttachmentRipper EAP = new EmailAttachmentRipper();
		String saveDirectory = WorkingPath+File.separator+"EmailAttachments";
		String host = "pop.bizmail.yahoo.com";
		String port = "995";
		String userName = "example@example.com";
		String password = "password";
		EAP.RipAndFilterTextDocuments(WorkingPath, saveDirectory, host, password, userName, port);
	}
	
	public static void main(String[] args) throws IOException, TikaException {
		
		String WorkingPath = "C:\\Users\\Downloads\\";
		//ReceiveDocumentsFromEmail(WorkingPath);
		
		String TrainingBase = WorkingPath+File.separator+"learn";
		String TrainingParameters = WorkingPath+File.separator+"trained";
		String TestingBase = WorkingPath+File.separator+"test";
		String ClassifiedFilesPath = WorkingPath+File.separator+"classifiedFiles";
		Path path2trained = Paths.get(TrainingParameters);
		
		//Training the classifier using naive bayes
		Training training = new Training();
		Inference inf = training.train(path2trained, TrainingBase, TrainingParameters);
		
		//Testing -> The actual classification of new files into classes
		System.out.println("Classifying the files from test folder...");
		Testing testing = new Testing();
		testing.readTestData(TestingBase, inf);
		testing.classify(inf,TestingBase,ClassifiedFilesPath);
		System.out.println("Done...!");
	}
}
