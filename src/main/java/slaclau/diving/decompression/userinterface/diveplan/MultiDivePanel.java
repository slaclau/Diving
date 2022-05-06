/* Copyright (C) 2022 Sebastien Laclau
   
   This file belongs to the Diving project.
   
   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>. */
package slaclau.diving.decompression.userinterface.diveplan;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

@SuppressWarnings("serial")
public class MultiDivePanel extends JPanel implements ActionListener {
	private ArrayList<Dive> dives;
	private int numberOfDives;
	private JButton addButton, saveButton;
	private JPanel innerPanel = new JPanel();
	private JPanel outerInnerPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane(outerInnerPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	private DivePlanListener divePlanListener;
	
	private boolean init = false;
	
	public MultiDivePanel() {
		super();
		
		outerInnerPanel.add(innerPanel);
		outerInnerPanel.add(Box.createVerticalGlue());
		
		dives = new ArrayList<Dive>();
		numberOfDives = 0;
		
		setLayout(new BorderLayout());

		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
	}
	
	public void addListenerAndInit(DivePlanListener divePlanListener) {
		this.divePlanListener = divePlanListener;
		if ( !init ) init();
	}
	
	private void init() {
		add(scrollPane);
		addDive();
		
		addButton = new JButton("Add level");
		addButton.setActionCommand("add");
		addButton.addActionListener(this);
		saveButton = new JButton("Save dives");
		saveButton.setActionCommand("save");
		saveButton.addActionListener(this);
		buttonPanel.add(addButton);
		buttonPanel.add(saveButton);
		add(buttonPanel,BorderLayout.SOUTH);

		setVisible(true);
	}
	
	void addDive() {
		numberOfDives++;
		Dive dive = new Dive(numberOfDives);
		dives.add(dive);
		innerPanel.add(dive);
		if (dives.size() > 1) {
			for (Dive l : dives) l.enableRemove();
		} else {
			for (Dive l : dives) l.disableRemove();
		}
	}
	
	class Dive extends JPanel {
		private JButton diveButton;
		private JLabel diveLabel, diveDescription;
		int dive;
		
		Dive(int i) {
			super(new FlowLayout());
			dive = i;
			
			diveLabel = new JLabel("Dive " + dive);
			diveLabel.setPreferredSize(new Dimension(100,20));
			add(diveLabel);
			
			diveDescription = new JLabel();
			// TODO add contents to label
			diveDescription.setPreferredSize(new Dimension(100,20));
			add(diveDescription);
			
			diveButton = new JButton("Remove dive");
			diveButton.setEnabled(false);
			diveButton.setActionCommand(((Integer) dive).toString());
			diveButton.addActionListener(MultiDivePanel.this);
			add(diveButton);
		}
		
		void enableRemove() {
			diveButton.setEnabled(true);
		}
		void disableRemove() {
			diveButton.setEnabled(false);
		}
		void setDiveNumber(int i) {
			dive = i;
			diveLabel.setText("Dive " + dive);
			diveButton.setActionCommand(((Integer) dive).toString());
		}
	}
	
	public int getNumberOfDives() {
		return numberOfDives;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand() == "add") {
			addDive();
		} else if (ae.getActionCommand() == "save" ) {
			System.out.println("save");
		} else {
			int i = Integer.parseInt(ae.getActionCommand());
			innerPanel.remove(dives.get(i-1));
			dives.remove(i-1);
			if (dives.size() > 1) {
				for ( int j = 0 ; j < dives.size(); j++ ) {
					dives.get(j).setDiveNumber( j + 1 );
					dives.get(j).enableRemove();
				}
			} else {
				dives.get(0).disableRemove();
				dives.get(0).setDiveNumber(1);
			}
			numberOfDives--;
		}
		
		revalidate();
		repaint();
		divePlanListener.onUpdate();
	}
}
