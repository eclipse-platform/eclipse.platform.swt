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

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/*
 * Title: Bug 326117 - SWT_AWT bridge - SWT in Swing problem
 * How to run: launch snippet and try to use the browser widget
 * Bug description: Nothing is visible except the text bar
 * Expected results: The browser should be rendering web content
 * GTK Version(s): GTK2 all
 */
public class Bug326117_EmbeddedBrowser{

   public static void main(String[] args)
   {
       final Display display = Display.getDefault();

       Frame frm = new Frame("MyBrowser");
       Canvas embedded = new Canvas();
       frm.add(embedded, BorderLayout.CENTER);

       frm.pack();

       final Shell composite = SWT_AWT.new_Shell(display, embedded);
       composite.setLayout(new FillLayout(SWT.VERTICAL));
       final Browser browser = installBrowser(composite, "http://www.baidu.com");
       frm.addWindowListener(new WindowAdapter()
       {
           @Override
		public void windowClosing(WindowEvent e)
           {
           e.getWindow().dispose();
           //composite.dispose();
//            display.dispose();
           }
       });

       JTextField addr = new JTextField(80);
       addr.addActionListener(e -> display.syncExec(() -> browser.setUrl(((JTextComponent) e.getSource()).getText())));
       frm.add(addr, BorderLayout.NORTH);

       frm.setSize(800, 600);
       frm.setVisible(true);
       while (frm.isDisplayable())
           if (!display.readAndDispatch())
               display.sleep();
//       display.dispose();
   }

   public static Browser installBrowser(Composite parent, String homeURL)
   {
       Browser browser = new Browser(parent, SWT.EMBEDDED);
       browser.setUrl(homeURL);
       return browser;
   }
   }