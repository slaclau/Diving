package slaclau.diving.decompression.userinterface;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConsolePanel extends JPanel {
	private JTextArea textArea = new JTextArea();
	private JScrollPane scrollPane = new JScrollPane(textArea);
	
	public ConsolePanel() {
		setLayout(new BorderLayout());
		add(scrollPane);
		textArea.setText("Console output:\n");
		textArea.setEditable(false);
	}
	
	public void println(String string) {
		textArea.append(string + "\n");
	}
	public void print(String string) {
		textArea.append(string);
	}
}
