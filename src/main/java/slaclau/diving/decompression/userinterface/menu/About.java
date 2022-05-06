package slaclau.diving.decompression.userinterface.menu;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class About extends JDialog {
	private JLabel product = new JLabel("Decompression planner");
	private JLabel version = new JLabel("Version: not found");
	private JTextArea copyright;
	private JPanel productPanel = new JPanel();
	private JPanel versionPanel = new JPanel();
	
	public About(JFrame jframe) {
		super(jframe, "About");
		setMinimumSize(new Dimension(500,400));
		
		JPanel panel = new JPanel();		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		add(panel);
		
		productPanel.add(product);
		productPanel.add(Box.createHorizontalGlue());
		panel.add(productPanel);
				
		try (InputStream input = About.class.getClassLoader().getResourceAsStream("version.properties") ) {
			Properties properties = new Properties();
			if ( input == null ) return;
			
			properties.load(input);
			version.setText("Version: " + properties.getProperty("version"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		versionPanel.add(version);
		versionPanel.add(Box.createHorizontalGlue());
		panel.add(versionPanel);
		
		panel.add(Box.createVerticalStrut(20));
		
		File input = new File( About.class.getClassLoader().getResource("copyright").getPath() );
		try (Scanner scanner = new Scanner(input) ) {
			String string = "";
			while ( scanner.hasNextLine() ) {
				String line = scanner.nextLine();
				string+=line;
				string+="\n";
			}
			copyright = new JTextArea(string);
			copyright.setLineWrap(true);
			copyright.setWrapStyleWord(true);
			copyright.setOpaque(false);
			copyright.setEditable(false);
			panel.add(copyright);
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
		pack();
		
		int w = getWidth();
		int h = getHeight();
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = ( dim.width - w ) / 2;
		int y = ( dim.height - h ) / 2;
		setLocation(x,y);
		
		setVisible(true);
	}
}
