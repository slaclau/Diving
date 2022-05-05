package slaclau.diving.decompression.userinterface.menu;

import java.awt.event.KeyEvent;

import javax.swing.*;

@SuppressWarnings("serial")
public class MainMenuBar extends JMenuBar {
	public MainMenuBar() {
		createHelpMenu();
	}
	
	private void createHelpMenu() {
		JMenu helpMenu = new JMenu("Help");
		JMenuItem about = new JMenuItem("About", KeyEvent.VK_T);
		helpMenu.add(about);
		add(helpMenu);
	}
	
}
