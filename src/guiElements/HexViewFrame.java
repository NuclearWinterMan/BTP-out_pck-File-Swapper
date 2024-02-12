package guiElements;
import fileHandling.AssetFile;

import javax.swing.JInternalFrame;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class HexViewFrame extends JInternalFrame {
	
	public ReplacerFrame parentFrame;
	private JTextArea targetFileReadout;
	private JTextArea replacementFileReadout;
	
	public HexViewFrame() {
		super("Hex View", true, false, true);
	}
}
