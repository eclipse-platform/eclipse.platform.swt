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
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class Bug483112_TreeColumnsTest {
	public static void main (String [] args) {
		 Display display = Display.getDefault();
		    Shell shell = new Shell(display);
		    shell.setLayout(new FillLayout());

		    Tree tree = new Tree(shell, SWT.NONE);
		    tree.setHeaderVisible(true);        

		    TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
		    column1.setText("Column 1");
		    column1.setWidth(50);
		    TreeColumn column2 = new TreeColumn(tree, SWT.LEFT);
		    column2.setText("Column 2");
		    column2.setWidth(50);
		    TreeColumn column3 = new TreeColumn(tree, SWT.LEFT);
		    column3.setText("Column 3");
		    column3.setWidth(50);

		    int W = 100, H = 100;
			final Image xImage = new Image (display, W, H);
			GC gc = new GC(xImage);
			gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
			gc.drawLine(0, 0,     W - 1, H -1);
			gc.drawLine(0, H - 1, W - 1, 0);
			gc.drawOval(1, 1,     W - 2, H - 2);
			gc.dispose();
			
			int Wz = 40, Hz = 40;
			final Image zImage = new Image (display, Wz, Hz);
			GC gcz = new GC(zImage);
			gcz.setForeground(display.getSystemColor(SWT.COLOR_RED));
			gcz.drawLine(0, 0,     Wz - 1, Hz -1);
			gcz.drawLine(0, Hz - 1, Wz - 1, 0);
			gcz.drawOval(1, 1,     Wz - 2, Hz - 2);
			gcz.dispose();
			

		    TreeItem item = new TreeItem(tree, SWT.NONE);
		    System.out.println("Image dimensions and size of column renderers is " + xImage.getBounds());
		    item.setImage(0, xImage);
		    item.setImage(1, xImage);
		    item.setImage(2, zImage);
		    item.setText(2, "Test");
		    System.out.println("column 0 getImageBounds() is " + item.getImageBounds(0));
		    System.out.println("column 1 getImageBounds() is " + item.getImageBounds(1));
		    System.out.println("column 2 getImageBounds() is " + item.getImageBounds(2));
		    
		    shell.pack();
		    shell.open();

		    while(!shell.isDisposed())
		    {
		        if(!display.readAndDispatch())
		            display.sleep();
		    }
	}

}
