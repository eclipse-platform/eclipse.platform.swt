/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.javaviewer;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class JavaViewer {
	Shell shell;
	StyledText text;
	JavaLineStyler lineStyler = new JavaLineStyler();
	FileDialog fileDialog;
	static ResourceBundle resources = ResourceBundle.getBundle("examples_javaviewer");

Menu createFileMenu() {
	Menu bar = shell.getMenuBar ();
	Menu menu = new Menu (bar);
	MenuItem item;

	// Open
	item = new MenuItem (menu, SWT.PUSH);
	item.setText (resources.getString("Open_menuitem"));
	item.setAccelerator(SWT.MOD1 + 'O');
	item.addSelectionListener(widgetSelectedAdapter(event -> openFile()));

	// Exit
	item = new MenuItem (menu, SWT.PUSH);
	item.setText (resources.getString("Exit_menuitem"));
	item.addSelectionListener (widgetSelectedAdapter(e -> menuFileExit ()));
	return menu;
}

void createMenuBar () {
	Menu bar = new Menu (shell, SWT.BAR);
	shell.setMenuBar (bar);

	MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
	fileItem.setText (resources.getString("File_menuitem"));
	fileItem.setMenu (createFileMenu ());

}

void createShell (Display display) {
	shell = new Shell (display);
	shell.setText (resources.getString("Window_title"));
	GridLayout layout = new GridLayout();
	layout.numColumns = 1;
	shell.setLayout(layout);
	shell.addShellListener(ShellListener.shellClosedAdapter(e -> {
		lineStyler.disposeColors();
		text.removeLineStyleListener(lineStyler);
	}));
}
void createStyledText() {
	text = new StyledText (shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
	GridData spec = new GridData();
	spec.horizontalAlignment = GridData.FILL;
	spec.grabExcessHorizontalSpace = true;
	spec.verticalAlignment = GridData.FILL;
	spec.grabExcessVerticalSpace = true;
	text.setLayoutData(spec);
	text.addLineStyleListener(lineStyler);
	text.setEditable(false);
	Color bg = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
	text.setBackground(bg);
}

void displayError(String msg) {
	MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
	box.setMessage(msg);
	box.open();
}

public static void main (String [] args) {
	Display display = new Display();
	JavaViewer example = new JavaViewer ();
	Shell shell = example.open (display);
	while (!shell.isDisposed ())
		if (!display.readAndDispatch ()) display.sleep ();
	display.dispose ();
}

public Shell open (Display display) {
	createShell (display);
	createMenuBar ();
	createStyledText ();
	shell.setSize(500, 400);
	shell.open ();
	return shell;
}

void openFile() {
	if (fileDialog == null) {
		fileDialog = new FileDialog(shell, SWT.OPEN);
	}

	fileDialog.setFilterExtensions(new String[] {"*.java", "*.*"});
	String name = fileDialog.open();

	open(name);
}

void open(String name) {
	final String textString;

	if ((name == null) || (name.length() == 0)) return;

	File file = new File(name);
	if (!file.exists()) {
		String message = MessageFormat.format(resources.getString("Err_file_no_exist"), file.getName());
		displayError(message);
		return;
	}

	try {
		FileInputStream stream= new FileInputStream(file.getPath());
		try (Reader in = new BufferedReader(new InputStreamReader(stream))) {

			char[] readBuffer= new char[2048];
			StringBuilder buffer= new StringBuilder((int) file.length());
			int n;
			while ((n = in.read(readBuffer)) > 0) {
				buffer.append(readBuffer, 0, n);
			}
			textString = buffer.toString();
			stream.close();
		} catch (IOException e) {
			// Err_file_io
			String message = MessageFormat.format(resources.getString("Err_file_io"), file.getName());
			displayError(message);
			return;
		}
	}
	catch (FileNotFoundException e) {
		String message = MessageFormat.format(resources.getString("Err_not_found"), file.getName());
		displayError(message);
		return;
	}
	// Guard against superfluous mouse move events -- defer action until later
	Display display = text.getDisplay();
	display.asyncExec(() -> text.setText(textString));

	// parse the block comments up front since block comments can go across
	// lines - inefficient way of doing this
	lineStyler.parseBlockComments(textString);
}

void menuFileExit () {
	shell.close ();
}
}
