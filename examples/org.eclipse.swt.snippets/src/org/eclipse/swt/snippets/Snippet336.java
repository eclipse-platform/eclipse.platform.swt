/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
package org.eclipse.swt.snippets;

/*
 * Taskbar snippet: customize the taskbar item with progress indicator, overlay image, and overlay text
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet336 {
	static Display display;
	static Shell shell;

static TaskItem getTaskBarItem () {
	TaskBar bar = display.getSystemTaskBar();
	if (bar == null) return null;
	TaskItem item = bar.getItem(shell);
	if (item == null) item = bar.getItem(null);
	return item;
}

public static void main(String[] args) {
	display = new Display();
	shell = new Shell(display);
	shell.setLayout(new GridLayout());
	TabFolder folder = new TabFolder(shell, SWT.NONE);
	folder.setLayoutData(new GridData(GridData.FILL_BOTH));

	//Progress tab
	TabItem item = new TabItem(folder, SWT.NONE);
	item.setText("Progress");
	Composite composite = new Composite(folder, SWT.NONE);
	composite.setLayout(new GridLayout());
	item.setControl(composite);
	Listener listener = event -> {
		Button button = (Button)event.widget;
		if (!button.getSelection()) return;
		TaskItem item1 = getTaskBarItem();
		if (item1 != null) {
			int state = ((Integer)button.getData()).intValue();
			item1.setProgressState(state);
		}
	};
	Group group = new Group (composite, SWT.NONE);
	group.setText("State");
	group.setLayout(new GridLayout());
	group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	Button button;
	String[] stateLabels = {"SWT.DEFAULT", "SWT.INDETERMINATE", "SWT.NORMAL", "SWT.ERROR", "SWT.PAUSED"};
	int[] states = {SWT.DEFAULT, SWT.INDETERMINATE, SWT.NORMAL, SWT.ERROR, SWT.PAUSED };
	for (int i = 0; i < states.length; i++) {
		button = new Button(group, SWT.RADIO);
		button.setText(stateLabels[i]);
		button.setData(Integer.valueOf(states[i]));
		button.addListener(SWT.Selection, listener);
		if (i==0) button.setSelection(true);
	}
	group = new Group (composite, SWT.NONE);
	group.setText("Value");
	group.setLayout(new GridLayout(2, false));
	group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	Label label = new Label(group, SWT.NONE);
	label.setText("Progress");
	final Scale scale = new Scale (group, SWT.NONE);
	scale.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	scale.addListener(SWT.Selection, event -> {
		TaskItem item1 = getTaskBarItem();
		if (item1 != null) item1.setProgress(scale.getSelection());
	});

	//Overlay text tab
	item = new TabItem(folder, SWT.NONE);
	item.setText("Text");
	composite = new Composite(folder, SWT.NONE);
	composite.setLayout(new GridLayout());
	item.setControl(composite);
	group = new Group (composite, SWT.NONE);
	group.setText("Enter a short text:");
	group.setLayout(new GridLayout(2, false));
	group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	final Text text = new Text(group, SWT.BORDER | SWT.SINGLE);
	GridData data = new GridData(GridData.FILL_HORIZONTAL);
	data.horizontalSpan = 2;
	text.setLayoutData(data);
	button = new Button(group, SWT.PUSH);
	button.setText("Set");
	button.addListener(SWT.Selection, event -> {
		TaskItem item1 = getTaskBarItem();
		if (item1 != null) item1.setOverlayText(text.getText());
	});
	button = new Button(group, SWT.PUSH);
	button.setText("Clear");
	button.addListener(SWT.Selection, event -> {
		text.setText("");
		TaskItem item1 = getTaskBarItem();
		if (item1 != null) item1.setOverlayText("");
	});

	//Overlay image tab
	item = new TabItem(folder, SWT.NONE);
	item.setText("Image");
	composite = new Composite(folder, SWT.NONE);
	composite.setLayout(new GridLayout());
	item.setControl(composite);
	Listener listener3 = event -> {
		Button button1 = (Button)event.widget;
		if (!button1.getSelection()) return;
		TaskItem item1 = getTaskBarItem();
		if (item1 != null) {
			String text1 = button1.getText();
			Image image = null;
			if (!text1.equals("NONE")) image = new Image (display, Snippet336.class.getResourceAsStream(text1));
			Image oldImage = item1.getOverlayImage();
			item1.setOverlayImage (image);
			if (oldImage != null) oldImage.dispose();
		}
	};
	group = new Group (composite, SWT.NONE);
	group.setText("Images");
	group.setLayout(new GridLayout());
	group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	button = new Button(group, SWT.RADIO);
	button.setText("NONE");
	button.addListener(SWT.Selection, listener3);
	button.setSelection(true);
	String[] images = {"eclipse.png", "pause.gif", "run.gif", "warning.gif"};
	for (int i = 0; i < images.length; i++) {
		button = new Button(group, SWT.RADIO);
		button.setText(images[i]);
		button.addListener(SWT.Selection, listener3);
	}
    shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}

}
