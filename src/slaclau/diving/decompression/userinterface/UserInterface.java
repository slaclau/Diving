package slaclau.diving.decompression.userinterface;

import javax.swing.*;

public class UserInterface {
	private JFrame jframe;
	private JSplitPane jsplitpane;
	
	public UserInterface() {
		jframe = new JFrame();
		jframe.setSize(1000, 1000);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jsplitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,new DivePlanPanel(),null);
		jsplitpane.setOneTouchExpandable(true);

		jframe.add(jsplitpane);
		jframe.pack();
		
		jframe.setVisible(true);
	}
}
