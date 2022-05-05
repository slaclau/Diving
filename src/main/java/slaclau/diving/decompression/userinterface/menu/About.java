package slaclau.diving.decompression.userinterface.menu;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class About extends JDialog {
	private JLabel versionLabel = new JLabel("Version"), version = new JLabel();
	
	public About(JFrame jframe) {
		super(jframe, "About");
		int w = 300;
		int h = 200;
		setSize(w, h);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = ( dim.width - w ) / 2;
		int y = ( dim.height - h ) / 2;
		setLocation(x,y);
		
		setLayout(new GridLayout(1,2));
		
		versionLabel.setPreferredSize(new Dimension(100,20));
		version.setText("Test");
		version.setPreferredSize(new Dimension(100,20));
		add(versionLabel);
		add(version);
		
		setVisible(true);
	}
}
