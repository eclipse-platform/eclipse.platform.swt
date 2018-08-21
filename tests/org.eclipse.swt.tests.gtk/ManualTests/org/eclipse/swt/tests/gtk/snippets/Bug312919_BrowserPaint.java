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
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class Bug312919_BrowserPaint {
static Region region;
public static void main(String[] args) {
final Display display = new Display();
final Shell shell = new Shell(display);
shell.setBounds(10, 10, 400, 400);
shell.setLayout(new FillLayout());
final Browser browser = new Browser(shell, SWT.NONE);
browser.setUrl("eclipse.org");
shell.open();
display.timerExec(3333, new Runnable() {
@Override
public void run() {
Rectangle bounds = display.map(shell, null,
browser.getBounds());
Shell shell2 = new Shell(display, SWT.NO_TRIM | SWT.ON_TOP);
shell2.setBounds(bounds);
final int width = bounds.width;
region = new Region();
Rectangle pixel = new Rectangle(0, 0, 1, 1);
for (int i = 0; i < width; i++) {
pixel.x = pixel.y = i;
region.add(pixel);
}
shell2.setRegion(region);
shell2.addListener(SWT.Paint, new Listener() {
@Override
public void handleEvent(Event event) {
event.gc.drawLine(0, 0, width, width);
}
});
shell2.open();
}
});
while (!shell.isDisposed()) {
if (!display.readAndDispatch()) display.sleep();
}
region.dispose();
shell.dispose();
display.dispose();
}
}