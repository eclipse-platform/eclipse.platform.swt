/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/*
 * Title: Bug 290650 - [Layout] If a composite with a FillLayout (with marginWidth != 0)
 * contains a widget which could expand depending on a fixed width or height, than computeSize()
 * doesn't calculate the expanding size correctly.
 * How to run: launch snippet and observe size of Text widgets
 * Bug description: One widget is only one line in height when it should be two
 * Expected results: Both widgets should be 2 lines in height
 * GTK Version(s): GTK2.x, GTK3.x
 */
public class Bug290650_FillLayoutComputeSize {
   private static int margin = 100;
   private static String msg = "This is a message with 2 lines and some text";

   private static void init(Shell shell) {
      Composite container1 = new Composite(shell, SWT.NONE);
      FillLayout fillLayout1 = new FillLayout();
      fillLayout1.marginWidth = margin;
      container1.setLayout(fillLayout1);
      container1.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));

      Text text1 = new Text(container1, SWT.WRAP);
      text1.setText(msg);

      Point containerSize1 = container1.computeSize(shell.getClientArea().width, SWT.DEFAULT);
      container1.setSize(containerSize1);

      Composite container2 = new Composite(shell, SWT.NONE);
      container2.setLocation(margin, 50);
      FillLayout fillLayout2 = new FillLayout();
      container2.setLayout(fillLayout2);
      container2.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));

      Text text2 = new Text(container2, SWT.WRAP);
      text2.setText(msg);

      Point containerSize2 = container2.computeSize(shell.getClientArea().width-2*margin, SWT.DEFAULT);
      container2.setSize(containerSize2);

   }

   public static void main(String[] args) {
      final Display display = new Display();
      Shell shell = new Shell(display);
      shell.setText("Fill Layout with Text-Widget Test");
      shell.setSize(190 + 2 * margin, 400);
      init(shell);
      shell.open();
      while (!shell.isDisposed()) {
         if (!display.readAndDispatch())
            display.sleep();
      }
      display.dispose();
   }

}
