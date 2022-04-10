package slaclau.diving.decompression.userinterface;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

@SuppressWarnings("serial")
public class DivePlanPanel extends JPanel implements ActionListener {
	private ArrayList<Level> levels;
	private int numberOfLevels;
	private JButton addButton;
	private JPanel innerPanel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane(innerPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	DivePlanPanel() {
		super();
		levels = new ArrayList<Level>();
		numberOfLevels = 0;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createTitledBorder("Dive plan"));

		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		add(scrollPane);
		addLevel();
		addLevel();
		addLevel();
		
		addButton = new JButton("Add level");
		addButton.setActionCommand("add");
		addButton.addActionListener(this);
		add(addButton);
		
		setVisible(true);
	}
	
	void addLevel() {
		numberOfLevels++;
		Level level = new Level(numberOfLevels,this);
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
		int level;
		
		Level(int i,ActionListener a) {
			super(new FlowLayout());
			level = i;
			
			levelLabel = new JLabel("Level " + level);
			
			add(levelLabel);
			
			add(new JTextField(20));
			
			levelButton = new JButton("Remove level");
			levelButton.setEnabled(false);
			levelButton.setActionCommand(((Integer) level).toString());
			levelButton.addActionListener(a);
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
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand() == "add") {
			addLevel();
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
