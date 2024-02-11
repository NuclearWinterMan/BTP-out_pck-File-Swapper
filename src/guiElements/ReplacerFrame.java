package guiElements;

import javax.swing.JFrame;
import javax.swing.JDesktopPane;
import java.awt.Dimension;

@SuppressWarnings("serial")
public class ReplacerFrame extends JFrame {
	
	public JDesktopPane desktop;
	public FileSelectorPanel fileFrame;
	public ChangeTrackerFrame changeFrame;
	public HexViewFrame hexViewFrame;
	
	public static final int LEFT_WIDTH = 600;
	public static final int RIGHT_WIDTH = 1200;
	
	public ReplacerFrame() {
		super("NuclearWinterMan's Spectrobes BTP _out.pck file replacer");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.setVisible(true);
		desktop = new JDesktopPane();
		desktop.setBounds(getBounds());
		desktop.setPreferredSize(new Dimension(LEFT_WIDTH + RIGHT_WIDTH, 1000));
		this.add(desktop);
		
	}
	
	public void addPanels() {
		addFileSelector();
		addChangeTracker();
		addHexView();
	}
	
	private void addFileSelector() {
		fileFrame = new FileSelectorPanel(this);
		fileFrame.setPreferredSize(new Dimension(LEFT_WIDTH, 500));
		fileFrame.setBounds(0,0,LEFT_WIDTH, 500);
		fileFrame.setVisible(true);
		desktop.add(fileFrame);
	}
	
	private void addChangeTracker() {
		changeFrame = new ChangeTrackerFrame(this);
		changeFrame.setPreferredSize(new Dimension(LEFT_WIDTH,500));
		changeFrame.setBounds(0,500,LEFT_WIDTH,500);
		changeFrame.setVisible(true);
		desktop.add(changeFrame);
	}
	
	private void addHexView() {
		hexViewFrame = new HexViewFrame();
		hexViewFrame.setPreferredSize(new Dimension(RIGHT_WIDTH, 750));
		hexViewFrame.setBounds(LEFT_WIDTH,0,RIGHT_WIDTH,750);
		hexViewFrame.setVisible(true);
		desktop.add(hexViewFrame);
	}
}
