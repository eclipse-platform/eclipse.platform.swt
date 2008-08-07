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
import java.io.InputStream;
import java.util.Hashtable;

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
	
	String getKey (Node node) {
		StringBuffer buffer = new StringBuffer();
		while (node != null) {
			if (buffer.length() > 0) buffer.append("_");
			String name = node.getNodeName();
			StringBuffer key = new StringBuffer(name);
			NamedNodeMap attributes = node.getAttributes();
			Node nameAttrib = getNameAttrib(attributes);
			if (nameAttrib != null) {
				key.append("-");
				key.append(nameAttrib.getNodeValue());
			}
			buffer.append(key.reverse());
			node = node.getParentNode();
		}
		buffer.reverse();
		return buffer.toString();
	}

	void buildLookup(Node node, Hashtable table) {
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node childNode = list.item(i);
			String key = getKey(childNode);
			table.put(key, childNode);
			buildLookup(childNode, table);
		}
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

	Document getDocument(String xmlPath) {
		try {
			InputStream is = null;
			if (xmlPath.indexOf(File.separatorChar) == -1) {
				is = getClass().getResourceAsStream(xmlPath);
				if (is == null) is = new BufferedInputStream(new FileInputStream(xmlPath));
			}
			if (is != null) return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(is));
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return null;
	}
	
	Node getNameAttrib(NamedNodeMap attributes) {
		if (attributes == null) return null;
		Node nameAttrib = attributes.getNamedItem("name");
		if (nameAttrib == null) nameAttrib = attributes.getNamedItem("selector");
		if (nameAttrib == null) nameAttrib = attributes.getNamedItem("path");
		return nameAttrib;
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
	
	TreeItem addChild (Node node, TreeItem superItem, Hashtable extras) {
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
		Node nameAttrib = getNameAttrib(attributes);
		String text = nameAttrib != null ? nameAttrib.getNodeValue() : name;
		item.setText(text);
		Node extra = (Node)extras.get(getKey(node));
		if (extra != null) {
			NamedNodeMap extraAttributes = extra.getAttributes();
			Node gen = extraAttributes.getNamedItem("swt_gen");
			if (gen != null && gen.getNodeValue().equals("true")) {
				item.setChecked(true);
			}
		}
		for (int i = 0; i < attributes.getLength(); i++) {
			Node attrib = attributes.item(i);
			text = attrib.getNodeName();
			if (attrib.equals(nameAttrib)) continue;
			int columnIndex = getColumnFor(text);
			item.setText(columnIndex, attrib.getNodeValue());
		}
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			addChild(childNodes.item(i), item, extras);
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

				Hashtable extras = new Hashtable();
				Document extraDocument = getDocument(xmlText + ".extras");
				if (extraDocument != null) {
					buildLookup(extraDocument, extras);
				}

				Document document = getDocument(xmlPath);
				NodeList list = document.getDocumentElement().getChildNodes();
				for (int i = 0; i < list.getLength(); i++) {
					addChild(list.item(i), xmlItem, extras);
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
