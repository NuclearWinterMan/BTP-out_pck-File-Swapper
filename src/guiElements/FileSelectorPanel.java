package guiElements;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JFileChooser;


import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

import java.io.File;

import fileHandling.FileHandler;
import fileHandling.AssetFile;
import fileHandling.ReplacementFile;

@SuppressWarnings("serial")
public class FileSelectorPanel extends JInternalFrame {
	public ReplacerFrame parentFrame;
	public JList<AssetFile> outPckFiles;
	public JList<ReplacementFile> replacementFiles;
	public JButton replaceButton;
	public JButton importButton;
	public JScrollPane targetScroller;
	public JScrollPane replacementScroller;
	
	
	public FileSelectorPanel(ReplacerFrame parent) {
		super("File Selection", true, false, true);
		this.parentFrame = parent;
		initTargetList();
		initReplacementList();
		initButtons();
		initLayout();
	}
	
	private void initLayout() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JLabel text1 = new JLabel("Targets");
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 1.0;
		c.weighty = .5;
		this.add(text1, c);
		c.gridx=1;
		JLabel text2 = new JLabel("Replacements");
		this.add(text2, c);
		c.gridy=1;
		c.gridx = 0;
		c.weighty=5.0;
		
		this.add(targetScroller, c);
		c.gridx = 1;
		this.add(replacementScroller, c);
		c.gridy=2;
		c.gridx=0;
		c.weighty=1.0;
		this.add(replaceButton, c);
		c.gridx = 1;
		this.add(importButton, c);
	}
	
	private void initTargetList() {
		outPckFiles = new JList<AssetFile>(FileHandler.getOutPckContents());
		outPckFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		outPckFiles.setLayoutOrientation(JList.VERTICAL);
		outPckFiles.setVisibleRowCount(-1);
		targetScroller = new JScrollPane(outPckFiles);
		targetScroller.setPreferredSize(new Dimension(300, 500));
		targetScroller.setAlignmentX(LEFT_ALIGNMENT);
	}
	
	private void initReplacementList() {
		replacementFiles = new JList<ReplacementFile>(FileHandler.getReplacementList());
		replacementFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		replacementFiles.setLayoutOrientation(JList.VERTICAL);
		replacementFiles.setVisibleRowCount(-1);
		replacementScroller = new JScrollPane(replacementFiles);
		replacementScroller.setPreferredSize(new Dimension(300, 500));
		replacementScroller.setAlignmentX(LEFT_ALIGNMENT);
	}
	
	private void initButtons() {
		replaceButton = new JButton("Replace file");
		importButton = new JButton("Import file");
		
		importButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String workingDirPath = System.getProperty("user.dir");
				JFileChooser importFinder = new JFileChooser(workingDirPath);
				importFinder.setDialogTitle("Sponsored by Wow! Industries");
				importFinder.setApproveButtonText("Import");
				importFinder.setMultiSelectionEnabled(false);
				
				int returnVal = importFinder.showOpenDialog(parentFrame);
				if (returnVal != JFileChooser.APPROVE_OPTION) {
					return;
				}
				File importableFile = importFinder.getSelectedFile();
				FileHandler.importAsset(importableFile);
				
			}
		});
		
		replaceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedTargetIndex, selectedReplacementIndex;
				selectedTargetIndex = outPckFiles.getSelectedIndex();
				selectedReplacementIndex = replacementFiles.getSelectedIndex();
				if(selectedTargetIndex < 0 || selectedReplacementIndex < 0) {
					return;
				}
				ReplacementFile selectedReplacement = replacementFiles.getSelectedValue();
				FileHandler.registerTransaction(selectedTargetIndex, selectedReplacement);
			}
		});
	}
	
}
