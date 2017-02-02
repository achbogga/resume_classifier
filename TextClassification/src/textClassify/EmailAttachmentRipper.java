package textClassify;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

public class EmailAttachmentRipper {
	public void RipAndFilterTextDocuments(String WorkingPath, String saveDirectory, String host, String password, String userName, String port) throws IOException, TikaException{
		/*
		String host = "pop.bizmail.yahoo.com";//incoming server//gmail IMAP: imap.gmail.com
		String port = "995";//incoming port//gmail IMAP port: 993
		String userName = "resumes@skolix.com";
		String password = "Vision2020";
		*/
		//String saveDirectory = "C:"+File.separator+"Users"+File.separator+"BAchyut"+File.separator+"Desktop"+File.separator+"InnDesk"+File.separator+"skolix"+File.separator+"tikasample"+File.separator+"Email Attachments";
		File f = new File(saveDirectory);
		try{
		    if(f.mkdir()) { 
		        //System.out.println("Directory Created to store Classified Files");
		    } else {
		        //System.out.println("Directory already exists to store Classified Files");
		    }
		} catch(Exception e){
		    e.printStackTrace();
		}
		String TextDocumentsDirectoryPath = WorkingPath+File.separator+"TextDocumentAttachments";
		File f1 = new File(TextDocumentsDirectoryPath);
		try{
		    if(f1.mkdir()) { 
		        //System.out.println("Directory Created to store Classified Files");
		    } else {
		        //System.out.println("Directory already exists to store Classified Files");
		    }
		} catch(Exception e){
		    e.printStackTrace();
		}
		
		EmailAttachmentReceiver receiver = new EmailAttachmentReceiver();
		receiver.setSaveDirectory(saveDirectory);
		//receiver.downloadEmailAttachments(host, port, userName, password);
				
		File folder = new File(saveDirectory);
		File[] listOfFiles = folder.listFiles();
		Tika tika = new Tika();
		//String cur_path="C:"+File.separator+"Users"+File.separator+"BAchyut"+File.separator+"Desktop"+File.separator+"InnDesk"+File.separator+"skolix"+File.separator+"tikasample"+File.separator;
		for (File file : listOfFiles) {
			if (file.isFile()) {
			    String fc = tika.parseToString(file);
			    String str = file.getName();
			    String str1=str;
			    String str2=".docx";
			    String str3=".doc";
			    String str4=".pdf";
			    String replacedStr=null;
			    if(str1.toLowerCase().contains(str2.toLowerCase())){
			    	replacedStr = str.replaceAll("docx", "txt");
			    }
			    else if(str1.toLowerCase().contains(str3.toLowerCase())){
			    	replacedStr = str.replaceAll("doc", "txt");
			    }
			    else if(str1.toLowerCase().contains(str4.toLowerCase())){
			    	replacedStr = str.replaceAll("pdf", "txt");
			    }
			    replacedStr=TextDocumentsDirectoryPath+File.separator+replacedStr;
		    	PrintWriter writer = new PrintWriter(replacedStr, "UTF-8");
		    	writer.print(fc);
		    	writer.close();
			}
		}
	}
}
