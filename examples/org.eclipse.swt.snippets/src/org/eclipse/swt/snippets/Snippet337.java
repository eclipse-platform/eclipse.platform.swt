package org.eclipse.swt.snippets;

/*
 * SWT_AWT example snippet: launch SWT from AWT and keep both active
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import java.awt.*;
import java.awt.Canvas;
import java.awt.event.*;

import javax.swing.*;

import org.eclipse.swt.*;
import org.eclipse.swt.awt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Event;

public class Snippet337 {

public static void main(String args[]) {
	final Display display = new Display();

	EventQueue.invokeLater(new Runnable() {
		@Override
		public void run() {
			JFrame mainFrame = new JFrame("Main Window");
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new FlowLayout());
			JButton launchBrowserButton = new JButton("Launch Browser");
			launchBrowserButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFrame childFrame = new JFrame();
					final Canvas canvas = new Canvas();
					childFrame.setSize(850, 650);
					childFrame.getContentPane().add(canvas);
					childFrame.setVisible(true);
					display.asyncExec(() -> {
						Shell shell = SWT_AWT.new_Shell(display, canvas);
						shell.setSize(800, 600);
						Browser browser = new Browser(shell, SWT.NONE);
						browser.setLayoutData(new GridData(GridData.FILL_BOTH));
						browser.setSize(800, 600);
						browser.setUrl("http://www.eclipse.org");
						shell.open();
					});
				}
			});

			mainPanel.add(new JTextField("a JTextField"));
			mainPanel.add(launchBrowserButton);
			mainFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			mainFrame.pack();
			mainFrame.setVisible(true);
		}
	});
	display.addListener(SWT.Close, new Listener() {
		@Override
		public void handleEvent(Event event) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					Frame[] frames = Frame.getFrames();
					for (int i = 0; i < frames.length; i++) {
						frames[i].dispose();
					}
				}
			});
		}
	});
	while (!display.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
}

}
