/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
package org.eclipse.swt.examples.browser.demos.views;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class BrowserDemoView extends ViewPart {
	Action pawnAction;
	Action editAction;
	Composite parent;
	
	public BrowserDemoView() {
	}
	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		parent.setLayout(new FillLayout());
		try {
			Browser browser = new Browser(parent, SWT.NONE);
			browser.dispose();
		} catch (SWTError e) {
			Text text = new Text(parent, SWT.MULTI | SWT.READ_ONLY);
			text.setText("Browser widget cannot be instantiated. The exact error is:\r\n"+e);
			text.requestLayout();
			return;
		}
		TabFolder folder = new TabFolder(parent, SWT.NONE);
		TabItem item = new TabItem(folder, SWT.NONE);
		new PawnTab(item);
		
		item = new TabItem(folder, SWT.NONE);
		new EditorTab(item);
	}
	
	@Override
	public void setFocus() {
	}
	

}
