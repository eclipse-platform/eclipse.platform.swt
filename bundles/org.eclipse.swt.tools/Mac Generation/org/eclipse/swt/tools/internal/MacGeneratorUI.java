/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class MacGeneratorUI {
	String[] xmls;

	Display display;
	Shell shell;
	Tree nodesTree;

	public MacGeneratorUI(String[] xmls) {
		this.xmls = xmls;
	}

	void cleanup() {
		display.dispose();
	}
	
	int getColumnFor(String attribute) {
		TreeColumn[] columns = nodesTree.getColumns();
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].getText().equals(attribute)) return i;
		}
		TreeColumn column = new TreeColumn(nodesTree, SWT.NONE);
		column.setText(attribute);
		return columns.length;
	}

	Document getDocument(String xmlPath) throws Exception {
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(xmlPath));
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(is));
	}

	public void open() {
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));

		Composite parent = shell;

		nodesTree = new Tree(parent, SWT.MULTI | SWT.CHECK | SWT.BORDER);
		nodesTree.setLayoutData(new GridData(GridData.FILL_BOTH));
		nodesTree.setHeaderVisible(true);
		nodesTree.setLinesVisible(true);

		TreeColumn nodesColumn = new TreeColumn(nodesTree, SWT.NONE);
		nodesColumn.setText("Name");
		nodesColumn.setWidth(300);

		updateNodes();
	}

	public void run() {
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		cleanup();
	}

	String getPrettyText(String text) {
		if (text.equals("class")) return "Classes";
		if (text.equals("depends_on")) return "Depends_on";
		return text.substring(0, 1).toUpperCase() + text.substring(1) + "s";
	}
	
	TreeItem addChild (Node node, TreeItem superItem) {
		String name = node.getNodeName();
		if (name.equals("#text")) return null;
		TreeItem parentItem = null;
		TreeItem[] items = superItem.getItems();
		for (int i = 0; i < items.length; i++) {
			if (name.equals(items[i].getData())) {
				parentItem = items[i];
				break;
			}
		}
		if (parentItem == null) {
			parentItem = new TreeItem(superItem, SWT.NONE);
			parentItem.setData(name);
			parentItem.setText(getPrettyText(name));
		}
		NamedNodeMap attributes = node.getAttributes();
		TreeItem item = new TreeItem(parentItem, SWT.NONE);
		Node nameAttrib = attributes.getNamedItem("name");
		if (nameAttrib == null) nameAttrib = attributes.getNamedItem("selector");
		if (nameAttrib == null) nameAttrib = attributes.getNamedItem("path");
		String text = nameAttrib != null ? nameAttrib.getNodeValue() : name;
		item.setText(text);
		for (int i = 0; i < attributes.getLength(); i++) {
			Node attrib = attributes.item(i);
			text = attrib.getNodeName();
			if (attrib.equals(nameAttrib)) continue;
			int columnIndex = getColumnFor(text);
			item.setText(columnIndex, attrib.getNodeValue());
		}
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			addChild(childNodes.item(i), item);
		}
		return item;
	}
	
	void updateNodes() {
		if (xmls == null) return;
		try {
			for (int x = 0; x < xmls.length; x++) {
				String xmlPath = xmls[x];
				TreeItem xmlItem = new TreeItem(nodesTree, SWT.NONE);
				String xmlText = xmlPath;
				int index = xmlText.lastIndexOf(File.separatorChar);
				if (index != -1) xmlText = xmlText.substring(index + 1);
				xmlItem.setText(xmlText);

				Document document = getDocument(xmlPath);
				NodeList list = document.getDocumentElement().getChildNodes();
				for (int i = 0; i < list.getLength(); i++) {
					addChild(list.item(i), xmlItem);
				}
			}
			TreeColumn[] columns = nodesTree.getColumns();
			for (int i = 0; i < columns.length; i++) {
				columns[i].pack();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		MacGeneratorUI ui = new MacGeneratorUI(args);
		ui.open();
		ui.run();
	}
}
