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

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.*;

import slaclau.diving.decompression.model.ModelledDive.DataRecord;

@SuppressWarnings("serial")
public class ExtraInfoPanel extends JPanel {
	private JPanel innerPanel = new JPanel();
		
	public ExtraInfoPanel() {
		add(innerPanel);
	}
	
	public void setInfo(ArrayList<DataRecord<String>> list) {
		innerPanel.removeAll();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		
		for ( DataRecord<?> record : list ) {			
			addLine(record.getLabel(), record.getValue() );
		}
	}
	
	private <T> void addLine(String label, T value) {
		JPanel line = new JPanel();
		innerPanel.add(line);
		
		JLabel jlabel, valueLabel;
		
		jlabel = new JLabel( label );
		jlabel.setPreferredSize(new Dimension(400,20));
		
		valueLabel = new JLabel( value.toString() );
		valueLabel.setPreferredSize(new Dimension(100,20));

		line.add(jlabel);
		line.add(valueLabel);
	}
}
