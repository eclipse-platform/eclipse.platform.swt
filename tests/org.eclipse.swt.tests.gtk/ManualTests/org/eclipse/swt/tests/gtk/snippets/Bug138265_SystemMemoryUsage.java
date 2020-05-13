/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug138265_SystemMemoryUsage {

private Display display;
private Shell shell;
private Composite mainComposite;
private Color color;

public Bug138265_SystemMemoryUsage() {

display = new Display ();
shell = new Shell(display);
shell.setLayout(new GridLayout());

init();

shell.pack();
shell.open ();

}

private void init() {
mainComposite = new Composite(shell, SWT.NONE);
mainComposite.setLayoutData(getGridData());
mainComposite.setLayout(new GridLayout());

Composite buttonComposite = new Composite(mainComposite, SWT.NONE);
buttonComposite.setLayoutData(getGridData());
buttonComposite.setLayout(new GridLayout(2, true));

Button button = new Button(buttonComposite, SWT.PUSH);
button.setText("With setBackground");
GridData gridData = getGridData();
gridData.grabExcessVerticalSpace = false;
button.setLayoutData(gridData);

button.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent arg0) {
createComposites(true);
}
});

Button button2 = new Button(buttonComposite, SWT.PUSH);
button2.setText("Without setBackground");
gridData = getGridData();
gridData.grabExcessVerticalSpace = false;
button2.setLayoutData(gridData);

button2.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent arg0) {
createComposites(false);
}
});

color = new Color(new RGB(31,150,192));

}

private void createComposites(boolean colorise) {
for (int i = 0; i < 1500; i++) {
Composite composite = new Composite(mainComposite, SWT.BORDER);
if (colorise) {
composite.setBackground(color);
}
composite.setLayoutData(getGridData());
}
}

public void run() {
while (!shell.isDisposed()) {
if (!display.readAndDispatch()) {
display.sleep(); }
} display.dispose();
}

public GridData getGridData() {
GridData gridData = new GridData();
gridData.grabExcessHorizontalSpace = true;
gridData.grabExcessVerticalSpace = true;
gridData.horizontalAlignment = SWT.FILL;
gridData.verticalAlignment = SWT.FILL;
return gridData;
	}


public static void main(String[] args) {
Bug138265_SystemMemoryUsage swtMemoryTest = new Bug138265_SystemMemoryUsage();
swtMemoryTest.run();
}

}