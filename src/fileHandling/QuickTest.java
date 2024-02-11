package fileHandling;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.*;
@SuppressWarnings("serial")
public class QuickTest extends JFrame{
	
	public JList<TestClass> listy;
	public JButton incrementy;
	public JScrollPane listScroller;
	public DefaultListModel<TestClass> numsList;
	
	public QuickTest () {
		super("testbox");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		GridLayout experimentLayout = new GridLayout(0,2);
		setBounds(0,0,400,400);
		
		setLayout(experimentLayout);
		incrementy = new JButton("Incrementy");
		initButton();
		initList();
		
		setVisible(true);
	}
	
	public void initButton() {
		incrementy.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = listy.getSelectedIndex();
				if (index < 0) {
					return;
				}
				numsList.get(index).x++;
				listy.repaint();
			}
		});
		this.add(incrementy);
	}
	
	private void initList() {
		numsList = new DefaultListModel<TestClass>();
		for (int i = 0; i < 34; i++) {
			numsList.add(0, new TestClass(i * 10));
		}
		listy = new JList<TestClass>(numsList);
		listy.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listy.setLayoutOrientation(JList.VERTICAL);
		listy.setVisibleRowCount(-1);
		listScroller = new JScrollPane(listy);
		listScroller.setPreferredSize(new Dimension(200, 500));
		listScroller.setAlignmentX(LEFT_ALIGNMENT);
		this.add(listScroller);
		
	}
	
	public static void main(String[] args) {
		QuickTest t = new QuickTest();
		t.toString();
		t.pack();
	}
}

