/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.browser.demos.views;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.graphics.*;

public class EditorTab {
	Browser browser;
	Text htmlText, scriptText;
	Button htmlButton, scriptButton;
	
	public EditorTab(TabItem item) {
		final Composite parent = new Composite(item.getParent(), SWT.NONE);
		item.setText("Editor");
		item.setControl(parent);
		
		try {
			browser = new Browser(parent, SWT.NONE);
		} catch (SWTError e) {
			e.printStackTrace();
			return;
		}
		final Sash sash = new Sash(parent, SWT.VERTICAL);
		Composite panel = new Composite(parent, SWT.NONE);
		final FormLayout form = new FormLayout();
		parent.setLayout(form);
		
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(sash, 0);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		browser.setLayoutData(data);
		
		final FormData sashData = new FormData();
		sashData.left = new FormAttachment(50, 0);
		sashData.top = new FormAttachment(0, 0);
		sashData.bottom = new FormAttachment(100, 0);
		sash.setLayoutData(sashData);
		sash.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Rectangle rect = sash.getBounds();
				Rectangle parentRect = sash.getParent().getClientArea();
				int right = parentRect.width - rect.width - 20;
				e.x = Math.max(Math.min(e.x, right), 20);
				if (e.x != rect.x) {
					sashData.left = new FormAttachment(0, e.x);
					parent.layout();
				}
			}			
		});
		data = new FormData();
		data.left = new FormAttachment(sash, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		panel.setLayoutData(data);
		
		/* Initialize Panel */
		panel.setLayout(new FillLayout(SWT.VERTICAL));
		Group htmlGroup = new Group(panel, SWT.NONE);
		htmlGroup.setText("setText");
		htmlText = new Text(htmlGroup, SWT.MULTI);
		htmlButton = new Button(htmlGroup, SWT.PUSH);
		htmlButton.setText("setText");
		GridLayout gridLayout = new GridLayout();
		htmlGroup.setLayout(gridLayout);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		htmlText.setLayoutData(gridData);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.END;
		htmlButton.setLayoutData(gridData);
		htmlGroup.layout();
		
		Group scriptGroup = new Group(panel, SWT.NONE);
		scriptGroup.setText("execute");
		scriptText = new Text(scriptGroup, SWT.MULTI);
		scriptButton = new Button(scriptGroup, SWT.PUSH);
		scriptButton.setText("execute");
		gridLayout = new GridLayout();
		scriptGroup.setLayout(gridLayout);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		scriptText.setLayoutData(gridData);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.END;
		scriptButton.setLayoutData(gridData);
		scriptGroup.layout();
		
		browser.setUrl("http://www.eclipse.org");
		htmlText.setText("<html><title>Hello</title><p>Hello World</html>");
		scriptText.setText("document.bgColor = 'yellow'");
		parent.layout();
		
		Listener listener = new Listener() {
			public void handleEvent(Event e) {
				Widget w = e.widget;
				if (w == htmlButton) browser.setText(htmlText.getText());
				if (w == scriptButton) browser.execute(scriptText.getText());
			}
		};
		
		htmlButton.addListener(SWT.Selection, listener);
		scriptButton.addListener(SWT.Selection, listener);
	}
}
