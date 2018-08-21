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



import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug303710_ForceActive {

  public static void main(String[] args) {
    final Display display = new Display();
    final Shell shell = new Shell(display);
    final int time = 500;
    Runnable timer = new Runnable() {
      @Override
	public void run() {
        Point point = display.getCursorLocation();
        Rectangle rect = shell.getBounds();
        if (rect.contains(point)) {
          System.out.println("In");
        } else {
          System.out.println("Out");
        }
        shell.forceActive();
        display.timerExec(time, this);
      }
    };
    display.timerExec(time, timer);
    shell.setSize(200, 200);
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }
}