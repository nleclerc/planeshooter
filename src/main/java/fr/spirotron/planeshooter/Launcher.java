package fr.spirotron.planeshooter;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Launcher {
	public static void main(String[] args) {
		final Engine engine = new Engine();
		
		Frame appFrame = new Frame("Plane Shooter");
		Dimension screenDimension = new Dimension(800, 600);
		
		appFrame.add(engine.createCanvas(screenDimension));
		appFrame.setSize(screenDimension);
		appFrame.pack();
		appFrame.setResizable(false);
		appFrame.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {
				try {
					engine.init();
				} catch (Exception e1) {
					throw new Error(e1);
				}
			}
			
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
			public void windowIconified(WindowEvent e) {
			}
			
			public void windowDeiconified(WindowEvent e) {
			}
			
			public void windowDeactivated(WindowEvent e) {
			}
			
			public void windowClosed(WindowEvent e) {
			}
			
			public void windowActivated(WindowEvent e) {
			}
		});
		
		appFrame.setVisible(true);
	}
}
