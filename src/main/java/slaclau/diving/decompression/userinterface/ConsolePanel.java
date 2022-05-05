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
package slaclau.diving.decompression.userinterface;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class ConsolePanel extends JPanel {
	private JTextArea textArea = new JTextArea();
	private JScrollPane scrollPane = new JScrollPane(textArea);
	
	public ConsolePanel() {
		setLayout(new BorderLayout());
		setMinimumSize(new Dimension((int) getMinimumSize().getWidth(), (int) getMinimumSize().getHeight() + 100));
		add(scrollPane);
		textArea.setText("Console output:\n");
		textArea.setEditable(false);
	}
	
	public void println(String string) {
		textArea.append(string + "\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
	public void print(String string) {
		textArea.append(string);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
}
