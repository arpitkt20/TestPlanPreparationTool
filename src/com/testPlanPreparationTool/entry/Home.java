package com.testPlanPreparationTool.entry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.jnativehook.GlobalScreen;

import com.testPlanPreparationTool.service.CaptureShot;
import com.testPlanPreparationTool.service.Export2Word;
import com.testPlanPreparationTool.service.KeyLogger;
import com.testPlanPreparationTool.util.Constants;

public class Home {

	private JFrame frame;
	private String msg = null;
	private JLabel lblMsg = null;
	private JButton btnExport = null;
	private JButton btnCapture = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		 try {
			 JWindow window = new JWindow();
			 JLabel splash = new JLabel("", new ImageIcon(Home.class.getResource("/resource/loading.gif")), SwingConstants.CENTER);
			 window.getContentPane().add(splash);
			 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			 Dimension labelSize = splash.getPreferredSize();
			 window.setLocation(screenSize.width/2 - (labelSize.width/2),
			                    screenSize.height/2 - (labelSize.height/2));
//			 window.setBounds(500, 150, 256, 256);
			 window.setVisible(true);			 
			@SuppressWarnings("unused")
			MainDocumentPart wordDocumentPart = new MainDocumentPart();
			window.setVisible(false);
		} catch (InvalidFormatException e1) {
			e1.printStackTrace();
		} 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GlobalScreen.registerNativeHook();
					GlobalScreen.addNativeKeyListener(new KeyLogger());
					Home app = new Home();
					app.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Home() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Home.class.getResource("/resource/logo.png")));
		frame.setTitle(Constants.APPLICATION_TITLE);
		frame.setBounds(100, 100, 450, 300);
		frame.setLocation(800, 450);
		frame.setSize(320, 150);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(115, 122, 45));
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		lblMsg = new JLabel("");
		lblMsg.setBounds(10, 106, 310, 12);
		panel.add(lblMsg);

		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Select a Folder to Save Files");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			msg = Constants.FOLDER_SELECTED;
			Constants.SRC_FOLDER = chooser.getSelectedFile().getAbsolutePath() + Constants.SEPERATOR;
		} else {
			msg = Constants.NO_FOLDER_SELECTED;
		}

		lblMsg.setText(msg);
		btnCapture = new JButton();
		btnCapture.setToolTipText(Constants.CAPTURE_BUTTON_TOOLTIP);
		btnCapture.setIcon(new ImageIcon(Home.class.getResource("/resource/capture.png")));
		btnCapture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.setState(Frame.ICONIFIED);
				CaptureShot capShot = new CaptureShot();
				boolean status = capShot.takeScreenShot();
				if (status == true) {
					msg = Constants.SHOT_CAPTURED_SUCCESS;
				} else {
					msg = Constants.SHOT_CAPTURED_FAILED;
				}
				frame.setState(Frame.NORMAL);
				lblMsg.setText(msg);
				Timer t = new Timer(2000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						lblMsg.setText(null);
					}
				});
				t.setRepeats(false);
				t.start();
			}
		});
		btnCapture.setBounds(38, 10, 85, 85);
		panel.add(btnCapture);

		btnExport = new JButton("");
		btnExport.setToolTipText(Constants.EXPORT_BUTTON_TOOLTIP);
		btnExport.setBackground(Color.BLACK);
		btnExport.setIcon(new ImageIcon(Home.class.getResource("/resource/export.png")));
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
					@Override
					protected Boolean doInBackground() throws Exception {
						lblMsg.setText("");
						lblMsg.setIcon(new ImageIcon(Home.class.getResource("/resource/Saving.gif"))); 
						btnExport.setEnabled(false);
						btnCapture.setEnabled(false);
						Export2Word export = new Export2Word();
						msg = export.exportImg2Word();
						return true;
					}

					@Override
					protected void done() {
						boolean status;
						try {
							status = get();
							if (status == true) {
								lblMsg.setIcon(null);
								lblMsg.setText(msg);
								btnExport.setEnabled(true);
								btnCapture.setEnabled(true);
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
					}
				};
				worker.execute();
			}
		});
		btnExport.setBounds(172, 7, 85, 85);
		panel.add(btnExport);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 100, 320, 3);
		panel.add(separator_1);
		
		
		
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            System.out.println("SplashScreen.getSplashScreen() returned null");
            return;
        }
        Graphics2D g = splash.createGraphics();
        if (g == null) {
            System.out.println("g is null");
            return;
        }
        splash.close();
        frame.setVisible(true);
        frame.toFront();		

		
		
	}
}