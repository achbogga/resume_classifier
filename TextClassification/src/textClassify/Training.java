package textClassify;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

import org.apache.tika.exception.TikaException;

public class Training {
	
	public Inference train(Path path2, String TrainingBase, String TrainingParameters) throws IOException, TikaException {
		if (Files.notExists(path2)) {
			System.out.print("\nTraining the system.....  ");
			BagOfWords bow = new BagOfWords();
			bow.readTrainingFiles(TrainingBase);
			WordFrequencyTable wf = new WordFrequencyTable();
			wf.ComputeFrequencies(bow);
			File f = new File(TrainingParameters);
			try{
			    if(f.mkdir()) { 
			        //System.out.println("Directory Created to store Training Parameters");
			    } else {
			        //System.out.println("Directory is not created");
			    }
			} catch(Exception e){
			    e.printStackTrace();
			}
			PrintWriter writer1 = new PrintWriter(TrainingParameters+File.separator+"ClassProbablities.txt", "UTF-8");
			PrintWriter writer2 = new PrintWriter(TrainingParameters+File.separator+"WordProbablities.txt", "UTF-8");
			for (int j=0;j<bow.NumberOfClasses;j++) {
				writer1.println(bow.DocumentClasses[j]+" "+wf.ProbablitiesOfClasses[j]);
				//System.out.println(bow.DocumentClasses[j]+" "+wf.ProbablitiesOfClasses[j]);
				Iterator<String> it = bow.Vocabulary.iterator();
				for (int k=0;k<bow.Vocabulary.size();k++) {
					String temp = it.next();
					writer2.println(bow.DocumentClasses[j]+" "+temp+" "+wf.ProbablitiesOfWords[j][k]);
					//System.out.println(bow.DocumentClasses[j]+" "+temp+" "+wf.ProbablitiesOfWords[j][k]);
				}
			}
			writer1.close();
			writer2.close();
			Inference inf = new Inference();
			inf.Vocabulary=bow.Vocabulary.toArray(new String[bow.Vocabulary.size()]);
			inf.ProbablitiesOfTrainedClasses = wf.ProbablitiesOfClasses;
			inf.ProbablitiesOfWords = wf.ProbablitiesOfWords;
			inf.TrainedClasses = bow.DocumentClasses;
			System.out.println("Done!");
			return inf;
		}
		else {
			Inference inf = new Inference();
			inf.readData(TrainingParameters);
			return inf;
		}
	}
}
