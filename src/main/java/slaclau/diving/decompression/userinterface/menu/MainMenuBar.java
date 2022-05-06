package slaclau.diving.decompression.userinterface.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import slaclau.diving.decompression.userinterface.UserInterface;

@SuppressWarnings("serial")
public class MainMenuBar extends JMenuBar implements ActionListener {
	private UserInterface userInterface;
	
	public MainMenuBar(UserInterface userInterface) {
		this.userInterface = userInterface;
		createHelpMenu();
	}
	
	private void createHelpMenu() {
		JMenu helpMenu = new JMenu("Help");
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(this);
		helpMenu.add(about);
		add(helpMenu);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		JMenuItem source = (JMenuItem) ae.getSource();
		String string = source.getText();
		switch (string) {
		case "About":
			new About(userInterface.getJframe());
		}
	}
	
}
