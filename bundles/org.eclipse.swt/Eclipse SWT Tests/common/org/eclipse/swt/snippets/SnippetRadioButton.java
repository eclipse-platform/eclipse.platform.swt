package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SnippetRadioButton {

	public static void main(String[] args) {
		// Display und Shell initialisieren
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Radio Buttons Example");
		shell.setSize(300, 200);
		shell.setLayout(new GridLayout(1, false));

		// Label hinzufügen
		Label label = new Label(shell, SWT.NONE);
		label.setText("Choose an option:");

		// Radio Buttons hinzufügen
		Button radio1 = new Button(shell, SWT.RADIO);
		radio1.setText("Option 1");
		radio1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Button radio2 = new Button(shell, SWT.RADIO);
		radio2.setText("Option 2");
		radio2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Button radio3 = new Button(shell, SWT.RADIO);
		radio3.setText("Option 3");
		radio3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// Event-Handling
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button selectedButton = (Button) e.widget;
				System.out.println("Selected: " + selectedButton.getText());
				System.out.println(selectedButton.getSelection());
			}
		};

		radio1.addSelectionListener(selectionAdapter);
		radio2.addSelectionListener(selectionAdapter);
		radio3.addSelectionListener(selectionAdapter);

		// Shell öffnen
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
