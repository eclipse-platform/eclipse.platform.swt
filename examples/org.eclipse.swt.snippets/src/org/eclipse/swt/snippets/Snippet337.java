package org.eclipse.swt.snippets;

// IN PROGRESS

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Snippet337 {

	static Display display;

	static boolean exit;

	public Snippet337() {
		JFrame frame = new JFrame("Main Window");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout());

		JButton launchBrowserButton = new JButton("Launch Browser");
		launchBrowserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame f = new JFrame();
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				final Canvas canvas = new Canvas();
				f.setSize(850, 650);
				f.getContentPane().add(canvas);
				f.setVisible(true);
				display.asyncExec(new Runnable() {
					public void run() {
						Shell shell = SWT_AWT.new_Shell(display, canvas);
						shell.setSize(800, 600);
						Browser browser = new Browser(shell, SWT.NONE);
						browser.setLayoutData(new GridData(GridData.FILL_BOTH));
						browser.setSize(800, 600);
						browser.setUrl("http://www.google.com");
						shell.open();
					}
				});
			}
		});
		launchBrowserButton.setActionCommand("launchbrowser");

		mainPanel.add(new JTextField(15));
		mainPanel.add(launchBrowserButton);

		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}


	public static void main(String args[]) {
		display = new Display();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Snippet337();
			}
		});
		while (!exit) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}