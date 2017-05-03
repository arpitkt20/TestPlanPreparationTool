package com.testPlanPreparationTool.service;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import com.testPlanPreparationTool.util.Constants;

public class CaptureShot {

	public boolean takeScreenShot(){
		boolean status = false;
        Robot robot = null;
        String format = "jpg";
        String fileName = null;
        Rectangle screenRect = null;
        BufferedImage screenFullImage = null;
        try { 
        File srcFolder = new File(Constants.SRC_FOLDER);
        if(!(srcFolder.exists())){
        	srcFolder.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        Date today = new Date();
        fileName = Constants.SRC_FOLDER + sdf.format(today) + Constants.DOT + Constants.IMG_FORMAT;
       	robot = new Robot();
        screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        screenFullImage = robot.createScreenCapture(screenRect);
       	ImageIO.write(screenFullImage, format, new File(fileName));
       	status = true;
		} catch (IOException e) {
			status = false;
			e.printStackTrace();
		} catch (AWTException e) {
			status = false;
			e.printStackTrace();
		}
		return status;
		
	}
}
