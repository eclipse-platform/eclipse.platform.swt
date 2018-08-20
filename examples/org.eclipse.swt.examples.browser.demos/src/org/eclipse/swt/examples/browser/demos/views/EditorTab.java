/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

public class EditorTab {
	Browser browser;
	Text htmlText, scriptText;
	Button htmlButton, scriptButton;
	static String html = 
		"<html>\r\n"+
		"	<body>\r\n"+
		"		<h1 id='myid'>HTML Document</h1>\r\n"+
		"		<h2>Set HTML content</h2>\r\n"+
		"		<ol>\r\n"+
		"			<li>Enter html data into the 'setText' pane</li>\r\n"+
		"			<li>Click on 'setText' to set the new content</li>\r\n"+
		"		</ol>\r\n"+
		"		<h2>Query or modify HTML document</h2>\r\n"+
		"		<ol>\r\n"+
		"		<li>Enter javascript commands into the 'execute' pane</li>\r\n"+
		"		<li>Click on 'execute' to run the javascript in the current document</li>\r\n"+
		"		</ol>\r\n"+
		"	</body>\r\n"+
		"</html>";
	
	static String script = 
		"var node = document.createElement('P');\r\n"+
		"var text = document.createTextNode('Content inserted!');\r\n"+
		"node.appendChild(text);\r\n"+
		"document.getElementById('myid').appendChild(node);\r\n\r\n"+
		"document.bgColor = 'yellow';";
	
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
		sash.addListener(SWT.Selection, e -> {
			Rectangle rect = sash.getBounds();
			Rectangle parentRect = sash.getParent().getClientArea();
			int right = parentRect.width - rect.width - 20;
			e.x = Math.max(Math.min(e.x, right), 20);
			if (e.x != rect.x) {
				sashData.left = new FormAttachment(0, e.x);
				parent.layout();
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
		
		browser.setText(html);
		htmlText.setText(html);
		scriptText.setText(script);
		parent.layout();
		
		Listener listener = e -> {
			Widget w = e.widget;
			if (w == htmlButton) browser.setText(htmlText.getText());
			if (w == scriptButton) browser.execute(scriptText.getText());
		};
		
		htmlButton.addListener(SWT.Selection, listener);
		scriptButton.addListener(SWT.Selection, listener);
	}
}
