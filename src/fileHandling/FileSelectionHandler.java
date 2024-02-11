package fileHandling;

import javax.swing.JFileChooser;

import java.io.File;
import javax.swing.JFrame;

public class FileSelectionHandler {
	
	public static void initialFileOpens(JFrame mainFrame) {
		String workingDirPath = System.getProperty("user.dir");
		
		
		//Get arm9 binary file handle
		//Create File Selector
		JFileChooser binSelector = new JFileChooser(workingDirPath);
		
		binSelector.setDialogTitle("Select arm9 binary from Spectrobes BTP");
		
		//Set filter
		SimpleFileFilter binFilter = new SimpleFileFilter("bin");
		binSelector.setFileFilter(binFilter);
		
		/*
		 * Subject to change
		 * 
		 */
		int returnVal = binSelector.showOpenDialog(mainFrame);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			System.exit(1);
		}
		
		File arm9Bin = binSelector.getSelectedFile();
		FileHandler.setArm9BinFHandle(arm9Bin);
		
		
		//Get _out.pck file
		//Create new File Selector
		JFileChooser pckSelector = new JFileChooser(workingDirPath);
		
		pckSelector.setDialogTitle("Select _out.pck from Spectrobes BTP");
		
		
		//Create and set filter for .pck files
		SimpleFileFilter pckFilter = new SimpleFileFilter("pck");
		pckSelector.setFileFilter(pckFilter);
		
		/*
		 * Prompt user for file
		 * Implementation subject to change
		 */
		returnVal = pckSelector.showOpenDialog(mainFrame);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			System.exit(1);
		}
		
		File outPck = pckSelector.getSelectedFile();
		FileHandler.setOutPckFHandle(outPck);
	}
}
