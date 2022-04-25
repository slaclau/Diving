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
