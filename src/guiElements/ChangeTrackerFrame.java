package guiElements;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

import java.io.File;

import fileHandling.FileHandler;
import fileHandling.Transaction;

@SuppressWarnings("serial")
public class ChangeTrackerFrame extends JInternalFrame {
	
	public ReplacerFrame parentFrame;
	public JList<Transaction> transactionList;
	public JScrollPane transactionScroller;
	public JButton remove;
	public JButton exporter;
	
	
	public ChangeTrackerFrame (ReplacerFrame parent) {
		super("File Changes", true, false, true);
		this.parentFrame = parent;
		initChangeList();
		initButtons();
		initLayout();
	}
	
	private void initChangeList() {
		this.transactionList = new JList<Transaction>(FileHandler.getChanges());
		transactionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		transactionList.setLayoutOrientation(JList.VERTICAL);
		transactionList.setVisibleRowCount(-1);
		transactionScroller = new JScrollPane(transactionList);
		transactionScroller.setPreferredSize(new Dimension(ReplacerFrame.LEFT_WIDTH, 500));
		transactionScroller.setAlignmentX(LEFT_ALIGNMENT);
	}
	
	private void initButtons() {
		remove = new JButton("Remove change");
		exporter = new JButton("Export changes");
		
		exporter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String workingDirPath = System.getProperty("user.dir");
				JFileChooser outPckExportSetter = new JFileChooser(workingDirPath);
				JFileChooser arm9ExportSetter = new JFileChooser(workingDirPath);
				File newOutPck, newArm9;
				
				outPckExportSetter.setDialogTitle("Save modified _out.pck as:");
				outPckExportSetter.setApproveButtonText("Save");
				outPckExportSetter.setMultiSelectionEnabled(false);
				outPckExportSetter.setApproveButtonMnemonic(KeyEvent.VK_ENTER);
				FileNameExtensionFilter pckFilter = new FileNameExtensionFilter("folter", "pck");
				outPckExportSetter.setFileFilter(pckFilter);
				int outPckAnswer = outPckExportSetter.showSaveDialog(parentFrame);
				if (outPckAnswer == JFileChooser.CANCEL_OPTION || outPckAnswer == JFileChooser.ERROR_OPTION) {
					return;
				}
				newOutPck = outPckExportSetter.getSelectedFile();
				
				arm9ExportSetter.setDialogTitle("Save modified arm9.bin as: ");
				arm9ExportSetter.setApproveButtonText("Save");
				arm9ExportSetter.setMultiSelectionEnabled(false);
				arm9ExportSetter.setApproveButtonMnemonic(KeyEvent.VK_ENTER);
				FileNameExtensionFilter binFilter = new FileNameExtensionFilter("falter", "bin");
				arm9ExportSetter.setFileFilter(binFilter);
				int arm9Answer = arm9ExportSetter.showSaveDialog(parentFrame);
				if (arm9Answer == JFileChooser.CANCEL_OPTION || arm9Answer == JFileChooser.ERROR_OPTION) {
					return;
				}
				newArm9 = arm9ExportSetter.getSelectedFile();
				
				FileHandler.export(newOutPck, newArm9);
			}
		});
	}
	
	private void initLayout() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JLabel text1 = new JLabel("Changes", JLabel.CENTER);
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 1.0;
		c.weighty = .5;
		this.add(text1, c);
		c.gridy = 1;
		c.weighty = 4.0;
		this.add(transactionScroller, c);
		c.gridy = 2;
		c.weighty = 1.0;
		this.add(exporter, c);
	}
}
