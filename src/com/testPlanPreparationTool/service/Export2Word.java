package com.testPlanPreparationTool.service;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;

import com.testPlanPreparationTool.util.Constants;

public class Export2Word {

	public String exportImg2Word(){
		String msg = "";
		File folder = null;
	    try {
		folder = new File(Constants.SRC_FOLDER);
		if(folder.exists()){
			FileFilter filter = new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					String fileName = pathname.getName();
					String extension = fileName.substring(fileName.lastIndexOf(Constants.DOT)+1, fileName.length());
					if(extension.equalsIgnoreCase(Constants.IMG_FORMAT)){
						return true;
					} else{
						return false;						
					}
				}
			};
			File [] files = folder.listFiles(filter);
			if(files.length > 0){
				Arrays.sort(files, new Comparator<File>(){
				    public int compare(File f1, File f2)
				    {
				        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
				    } });
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		        Date today = new Date();
				String wordFileName = folder.getPath() + File.separator + Constants.WORD_FILE_NAME + Constants.HYPHEN + sdf.format(today) + Constants.DOT + Constants.WORD_FILE_FORMAT;
				File wordFile = new File(wordFileName);
				int counter = 1;
			    WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();				
			    InputStream is = null;
			    wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Title", "Testplan Flow");
			    for(File imgFile : files){
				     is = new java.io.FileInputStream(imgFile);
				     byte[] bytes = IOUtils.toByteArray(is);
				     int id1 = 0;
				     int id2 = 1;
				     wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Heading4", "ScreenShot - "+(counter++));
				     P imgPara = newImage( wordMLPackage, bytes,imgFile.getName(), imgFile.getName(),id1, id2,6000);
				     wordMLPackage.getMainDocumentPart().addObject(imgPara);
				     wordMLPackage.save(wordFile);
				     is.close();
				}
			    msg = Constants.WORD_EXPORT_SUCCESS;
			} else {
				msg = Constants.NO_FILES_TO_EXPORT;
			}
		} else {
			msg = Constants.SRC_FOLDER_NOT_FOUND;
		}
		} catch (Exception e) {
			msg = Constants.ERROR_OCCURED_IN_SAVING_FILE;
			e.printStackTrace();
		}
		return msg;  
	}
	
	public static P newImage( WordprocessingMLPackage wordMLPackage,
	        byte[] bytes,
	        String filenameHint, String altText, 
	        int id1, int id2, long cx) throws Exception {
	        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);
	        Inline inline = imagePart.createImageInline(filenameHint, altText,id1, id2, false);
	        ObjectFactory factory = new ObjectFactory();
	        P  p = factory.createP();
	        R  run = factory.createR();             
	        p.getContent().add(run);       
	        org.docx4j.wml.Drawing drawing = factory.createDrawing();               
	        run.getContent().add(drawing);               
	        drawing.getAnchorOrInline().add(inline);
	        return p;
}
}