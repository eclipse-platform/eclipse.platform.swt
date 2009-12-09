package org.eclipse.swt.snippets;

import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/*
 * SWT skin snippet: Listen for and print out skin events
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.6
 */
public class Snippet333 {

	static final int colors[] = {
		SWT.COLOR_RED,
		SWT.COLOR_MAGENTA,
		SWT.COLOR_GREEN,
		SWT.COLOR_YELLOW,
		SWT.COLOR_BLUE,
		SWT.COLOR_CYAN,
		SWT.COLOR_DARK_RED,
		SWT.COLOR_DARK_MAGENTA,
		SWT.COLOR_DARK_GREEN,
		SWT.COLOR_DARK_YELLOW,
		SWT.COLOR_DARK_BLUE,
		SWT.COLOR_DARK_CYAN
	};
	
	public static void main(String[] arg) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Skin example");
		shell.setLayout (new GridLayout());
		
		Group container = new Group(shell, SWT.None);
		container.setText("Container");
		container.setLayout(new GridLayout(3, false));
		
		Composite child1 = new Composite(container,SWT.BORDER);
		child1.setLayout(new GridLayout());
		new Label(child1,SWT.NONE).setText("Label in pane 1");
		
		Composite child2 = new Composite(container,SWT.BORDER);
		child2.setLayout(new GridLayout());
		new Button(child2,SWT.PUSH).setText("Button in pane2");

		final Composite child3 = new Composite(container,SWT.BORDER);
		child3.setLayout(new GridLayout());
		new Text(child3, SWT.BORDER).setText("Text in pane3");
		
		display.addListener(SWT.Skin, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("Skin: " + event.widget);
				setBackground (event.display, (Control) event.widget);
			}});
		
		Composite buttonContainer = new Composite(shell, SWT.NONE);
		buttonContainer.setLayout(new GridLayout(3, false));
		Button reskin = new Button(buttonContainer, SWT.PUSH);
		reskin.setText("Reskin All");
		reskin.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("=======");
				shell.reskin(SWT.ALL);
			}
		});
		Button reskin2 = new Button(buttonContainer, SWT.PUSH);
		reskin2.setText("Reskin Shell");
		reskin2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("=======");
				shell.reskin(SWT.None);
			}
		});
		Button reskin3 = new Button(buttonContainer, SWT.PUSH);
		reskin3.setText("Reskin Right Composite");
		reskin3.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("=======");
				child3.reskin(SWT.ALL);
			}
		});
		
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	protected static void setBackground(Display display, Control control) {
		 Random randomGenerator = new Random();
		 int nextColor = randomGenerator.nextInt(100) + randomGenerator.nextInt(100);
		 control.setBackground(display.getSystemColor(colors[nextColor % colors.length]));
	}

	
}
