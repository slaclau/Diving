package slaclau.diving.decompression.userinterface.diveplan;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

@SuppressWarnings("serial")
public class DivePlanPanel extends JPanel implements ActionListener {
	private ArrayList<Level> levels;
	private int numberOfLevels;
	private JButton addButton, saveButton;
	private JPanel innerPanel = new JPanel();
	private JPanel outerInnerPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane(outerInnerPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	private DivePlanListener divePlanListener;
	
	private boolean init = false;
	
	public DivePlanPanel() {
		super();
		
		outerInnerPanel.add(innerPanel);
		outerInnerPanel.add(Box.createVerticalGlue());
		
		levels = new ArrayList<Level>();
		numberOfLevels = 0;
		
		setLayout(new BorderLayout());

		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
	}
	
	public void addListenerAndInit(DivePlanListener divePlanListener) {
		this.divePlanListener = divePlanListener;
		if ( !init ) init();
	}
	
	private void init() {
		add(scrollPane);
		addLevel();
		
		addButton = new JButton("Add level");
		addButton.setActionCommand("add");
		addButton.addActionListener(this);
		saveButton = new JButton("Save dive");
		saveButton.setActionCommand("save");
		saveButton.addActionListener(this);
		buttonPanel.add(addButton);
		buttonPanel.add(saveButton);
		add(buttonPanel,BorderLayout.SOUTH);

		setVisible(true);
	}
	
	void addLevel() {
		numberOfLevels++;
		Level level = new Level(numberOfLevels);
		levels.add(level);
		innerPanel.add(level);
		if (levels.size() > 1) {
			for (Level l : levels) l.enableRemove();
		} else {
			for (Level l : levels) l.disableRemove();
		}
	}
	
	class Level extends JPanel {
		private JButton levelButton;
		private JLabel levelLabel;
		private JTextField depthField, timeField;
		int level;
		
		Level(int i) {
			super(new FlowLayout());
			level = i;
			
			levelLabel = new JLabel("Level " + level);
			levelLabel.setPreferredSize(new Dimension(100,20));
			add(levelLabel);
			
			depthField = new JTextField(10);
			depthField.setText("10");
			depthField.addActionListener(divePlanListener);
			add(depthField);
			
			timeField = new JTextField(10);
			timeField.setText("10");
			timeField.addActionListener(divePlanListener);
			add(timeField);
			
			levelButton = new JButton("Remove level");
			levelButton.setEnabled(false);
			levelButton.setActionCommand(((Integer) level).toString());
			levelButton.addActionListener(DivePlanPanel.this);
			add(levelButton);
		}
		
		void enableRemove() {
			levelButton.setEnabled(true);
		}
		void disableRemove() {
			levelButton.setEnabled(false);
		}
		void setLevelNumber(int i) {
			level = i;
			levelLabel.setText("Level " + level);
			levelButton.setActionCommand(((Integer) level).toString());
		}
		double getDepth() {
			return Double.valueOf( depthField.getText() );
		}
		double getTime() {
			return Double.valueOf( timeField.getText() );
		}
	}
	
	public int getNumberOfLevels() {
		return numberOfLevels;
	}
	public double getDepthOfLevel(int i) {
		return levels.get(i).getDepth();
	}
	public double getTimeOfLevel(int i) {
		return levels.get(i).getTime();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand() == "add") {
			addLevel();
		} else if (ae.getActionCommand() == "save" ) {
			System.out.println("save");
		} else {
			int i = Integer.parseInt(ae.getActionCommand());
			innerPanel.remove(levels.get(i-1));
			levels.remove(i-1);
			if (levels.size() > 1) {
				for ( int j = 0 ; j < levels.size(); j++ ) {
					levels.get(j).setLevelNumber( j + 1 );
					levels.get(j).enableRemove();
				}
			} else {
				levels.get(0).disableRemove();
				levels.get(0).setLevelNumber(1);
			}
			numberOfLevels--;
		}
		
		revalidate();
		repaint();
	}
}
