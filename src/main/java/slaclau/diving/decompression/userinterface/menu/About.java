package slaclau.diving.decompression.userinterface.menu;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class About extends JDialog {
	private JLabel product = new JLabel("Decompression planner");
	private JLabel version = new JLabel("Version: not found");
	private JLabel copyright;
	
	public About(JFrame jframe) {
		super(jframe, "About");
		
		JPanel panel = new JPanel();		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		add(panel);
		
		panel.add(product);
				
		try (InputStream input = About.class.getClassLoader().getResourceAsStream("version.properties") ) {
			Properties properties = new Properties();
			if ( input == null ) return;
			
			properties.load(input);
			version.setText("Version: " + properties.getProperty("version"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		panel.add(version);
		
		panel.add(Box.createVerticalStrut(20));
				
		try (InputStream input = About.class.getClassLoader().getResourceAsStream("copyright") ) {
			BufferedReader reader = new BufferedReader( new InputStreamReader( input ) );
			String string = "";
			while ( ( string=reader.readLine() ) != null ) {
				copyright = new JLabel(string);
				panel.add(copyright);
			}
			
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
