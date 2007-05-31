/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.browser.demos.views;

import org.eclipse.ui.part.*;
import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.browser.*;

public class BrowserDemoView extends ViewPart {
	Action pawnAction;
	Action editAction;
	Composite parent;
	
	public BrowserDemoView() {
	}
	public void createPartControl(Composite parent) {
		this.parent = parent;
		parent.setLayout(new FillLayout());
		try {
			Browser browser = new Browser(parent, SWT.NONE);
			browser.dispose();
		} catch (SWTError e) {
			Text text = new Text(parent, SWT.MULTI | SWT.READ_ONLY);
			text.setText("Browser widget cannot be instantiated. The exact error is:\r\n"+e);
			parent.layout(true);
			return;
		}
		TabFolder folder = new TabFolder(parent, SWT.NONE);
		TabItem item = new TabItem(folder, SWT.NONE);
		new PawnTab(item);
		
		item = new TabItem(folder, SWT.NONE);
		new EditorTab(item);
	}
	
	public void setFocus() {
	}
	

}
