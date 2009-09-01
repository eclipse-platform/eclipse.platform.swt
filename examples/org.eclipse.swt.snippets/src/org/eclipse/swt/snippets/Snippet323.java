/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Browser example snippet: View DOM tree and edit node values in a Mozilla Browser
 * 
 * IMPORTANT: For this snippet to work properly all of the requirements
 * for using JavaXPCOM in a stand-alone application must be satisfied
 * (see http://www.eclipse.org/swt/faq.php#howusejavaxpcom).
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.3
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.mozilla.interfaces.*;

public class Snippet323 {

public static void main (String[] args) {
	new Snippet323 ().run ();
}

void run () {
	final Display display = new Display ();
	final Shell shell = new Shell (display);
	shell.setBounds (10,10,500,500);
	shell.setLayout (new FillLayout ());
	final Browser browser = new Browser (shell, SWT.MOZILLA);
	browser.setUrl ("http://www.google.com");
	browser.addProgressListener (new ProgressAdapter () {
		public void completed (ProgressEvent event) {
			nsIWebBrowser webBrowser = (nsIWebBrowser)browser.getWebBrowser ();
			nsIDOMWindow domWindow = webBrowser.getContentDOMWindow ();
			nsIDOMDocument document = domWindow.getDocument ();
			nsIDOMElement documentElement = document.getDocumentElement ();
			DOMEditor domEditor = new DOMEditor (shell);
			domEditor.populate (documentElement);
		}
	});

	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

class DOMEditor {
	Tree tree;

	public DOMEditor (Shell parent) {
		super ();
		Shell shell = new Shell (parent, SWT.SHELL_TRIM);
		shell.setText ("DOM Editor");
		shell.setBounds (510,10,400,400);
		shell.setLayout (new FillLayout ());

		tree = new Tree (shell, SWT.NONE);
		shell.open ();
		final TreeItem[] lastItem = new TreeItem[1];
		final TreeEditor editor = new TreeEditor (tree);
		tree.addSelectionListener (new SelectionAdapter () {
			public void widgetDefaultSelected (SelectionEvent e) {
				final TreeItem item = (TreeItem)e.item;
				final nsIDOMNode node = (nsIDOMNode)item.getData ();
				if (node == null) return; 	/* not editable */
				if (item != null && item == lastItem[0]) {
					final Composite composite = new Composite (tree, SWT.NONE);
					final Text text = new Text (composite, SWT.NONE);
					final int inset = 1;
					composite.addListener (SWT.Resize, new Listener () {
						public void handleEvent (Event e) {
							Rectangle rect = composite.getClientArea ();
							text.setBounds (rect.x + inset, rect.y + inset, rect.width - inset * 2, rect.height - inset * 2);
						}
					});
					Listener textListener = new Listener () {
						public void handleEvent (final Event e) {
							switch (e.type) {
								case SWT.FocusOut:
									String string = text.getText ();
									node.setNodeValue (string);
									item.setText ("Node Value: " + node.getNodeValue ());
									composite.dispose ();
									break;
								case SWT.Verify:
									String newText = text.getText ();
									String leftText = newText.substring (0, e.start);
									String rightText = newText.substring (e.end, newText.length ());
									GC gc = new GC (text);
									Point size = gc.textExtent (leftText + e.text + rightText);
									gc.dispose ();
									size = text.computeSize (size.x, SWT.DEFAULT);
									editor.horizontalAlignment = SWT.LEFT;
									Rectangle itemRect = item.getBounds (), rect = tree.getClientArea ();
									editor.minimumWidth = Math.max (size.x, itemRect.width) + inset * 2;
									int left = itemRect.x, right = rect.x + rect.width;
									editor.minimumWidth = Math.min (editor.minimumWidth, right - left);
									editor.minimumHeight = size.y + inset * 2;
									editor.layout ();
									break;
								case SWT.Traverse:
									switch (e.detail) {
										case SWT.TRAVERSE_RETURN:
											string = text.getText ();
											node.setNodeValue (string);
											item.setText ("Node Value: " + node.getNodeValue ());
											//FALL THROUGH
										case SWT.TRAVERSE_ESCAPE:
											composite.dispose ();
											e.doit = false;
									}
									break;
							}
						}
					};
					text.addListener (SWT.FocusOut, textListener);
					text.addListener (SWT.Traverse, textListener);
					text.addListener (SWT.Verify, textListener);
					editor.setEditor (composite, item);
					String nodeValue = node.getNodeValue ();
					text.setText (nodeValue == null ? "null" : nodeValue);
					text.selectAll ();
					text.setFocus ();
				}
				lastItem [0] = item;
			}
		});
	}

	public void populate (nsIDOMElement element) {
		tree.removeAll ();
		TreeItem root = new TreeItem (tree, SWT.NONE);
		root.setText ("Root: " + element.getTagName ());
		populate (root, element);
	}

	void populate (TreeItem parentItem, nsIDOMNode node) {
		String nodeName = node.getNodeName ();
		if (nodeName.length () > 0) {
			new TreeItem (parentItem, SWT.NONE).setText ("Node Name: " + nodeName);
		}
		String localName = node.getLocalName ();
		if (localName != null && localName.length () > 0) {
			new TreeItem (parentItem, SWT.NONE).setText ("Local Name: " + localName);			
		}

		TreeItem valueItem = new TreeItem (parentItem, SWT.NONE);
		String nodeValue = node.getNodeValue ();
		valueItem.setText ("Node Value: " + nodeValue);
		if (node != null) {
			valueItem.setData (node);
			Color red = parentItem.getDisplay ().getSystemColor (SWT.COLOR_RED);
			valueItem.setForeground (red);
		}

		String prefix = node.getPrefix ();
		if (prefix != null && prefix.length () > 0) {
			new TreeItem (parentItem, SWT.NONE).setText ("Prefix: " + prefix);			
		}
		String namespaceURI = node.getNamespaceURI ();
		if (namespaceURI != null && namespaceURI.length () > 0) {
			new TreeItem (parentItem, SWT.NONE).setText ("Namespace URI: " + namespaceURI);			
		}

		nsIDOMNamedNodeMap attributes = node.getAttributes ();
		if (attributes != null) {
			int count = (int)attributes.getLength ();
			if (count > 0) {
				for (int i = 0; i < count; i++) {
					TreeItem attributeItem = new TreeItem (parentItem, SWT.NONE);
					nsIDOMNode child = attributes.item (i);
					attributeItem.setText ("Attribute " + i + " (" + child.getNodeName () + ")");
					populate (attributeItem, child);
				}
			}
		}
		String typeString;
		switch (node.getNodeType ()) {
			case 1: typeString = "ELEMENT_NODE"; break;
			case 2: typeString = "ATTRIBUTE_NODE"; break;
			case 3: typeString = "TEXT_NODE"; break;
			case 4: typeString = "CDATA_SECTION_NODE"; break;
			case 5: typeString = "ENTITY_REFERENCE_NODE"; break;
			case 6: typeString = "ENTITY_NODE"; break;
			case 7: typeString = "PROCESSING_INSTRUCTION_NODE"; break;
			case 8: typeString = "COMMENT_NODE"; break;
			case 9: typeString = "DOCUMENT_NODE"; break;
			case 10: typeString = "DOCUMENT_TYPE_NODE"; break;
			case 11: typeString = "DOCUMENT_FRAGMENT_NODE"; break;
			case 12: typeString = "NOTATION_NODE"; break;
			default: typeString = "unknown?!?";
		}
		new TreeItem (parentItem, SWT.NONE).setText ("Type: " + typeString);

		nsIDOMNodeList children = node.getChildNodes ();
		int count = (int)children.getLength ();
		if (count > 0) {
			for (int i = 0; i < count; i++) {
				TreeItem childItem = new TreeItem (parentItem, SWT.NONE);
				nsIDOMNode child = children.item (i);
				childItem.setText ("Child " + i + " (" + child.getNodeName () + ")");
				populate (childItem, child);
			}
		}
	}
}

}
