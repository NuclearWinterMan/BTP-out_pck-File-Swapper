import fileHandling.FileHandler;
import guiElements.ReplacerFrame;

import java.io.File;

import java.awt.GridLayout;
import java.awt.Container;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.SwingUtilities;


public class Driver implements Runnable{
	
	public void run() {
		//Invoked on the event dispatching thread
		// Construct and show gui
		initialization();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Driver());
	}

	public static void initialization() {
		setLAF();
		ReplacerFrame mainFrame = new ReplacerFrame();
		//TODO uncomment this
		//FileSelectionHandler.initialFileOpens(mainFrame);
		//Hardcoding values for testing purposes
		FileHandler.setArm9BinFHandle(new File("arm9.bin"));
		FileHandler.setOutPckFHandle(new File("_out.pck"));
		
		// Read game files from _out.pck
		FileHandler.initOffsets();
		if(!FileHandler.readAllAssets()) {
			System.out.println("Error detected while reading");
			System.exit(1);
		}
		
		mainFrame.addPanels();
		//addWIPPanel(mainFrame.fileFrame);
		addWIPPanel(mainFrame.hexViewFrame);
		//addWIPPanel(mainFrame.changeFrame);
		mainFrame.pack();
		mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		mainFrame.revalidate();
		mainFrame.repaint();
	}
	
	public static void setLAF() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	
	public static void addWIPPanel(Container box) {
		ImageIcon wipIcon = new ImageIcon("WIP.png", "A red square with an X within it");
		
		JLabel wipText = new JLabel("Under Construction", wipIcon, JLabel.CENTER);
		wipText.setVerticalTextPosition(JLabel.BOTTOM);
		wipText.setHorizontalTextPosition(JLabel.CENTER);
		
		JPanel wipPanel = new JPanel(new GridLayout(1,1));
		wipPanel.add(wipText);
		box.add(wipPanel);
		box.revalidate();
		box.repaint();
	}
}
