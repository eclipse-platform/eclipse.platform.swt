/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.clipboard;
 
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class ClipboardExample {

	Clipboard clipboard;
	Shell shell;
	Text copyText;
	Text pasteText;
	Text copyRtfText;
	Text pasteRtfText;
	Text copyHtmlText;
	Text pasteHtmlText;
	Table copyFileTable;
	Table pasteFileTable;
	Text text;
	Combo combo;
	StyledText styledText;
	Label status;
	static final int SIZE = 60;
	
public static void main( String[] args) {
	Display display = new Display();
	new ClipboardExample().open(display);
	display.dispose();
}
public void open(Display display) {
	clipboard = new Clipboard(display);
	shell = new Shell (display);
	shell.setText("SWT Clipboard");
	shell.setLayout(new FillLayout());
	
	ScrolledComposite sc = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL);
	Composite parent = new Composite(sc, SWT.NONE);
	sc.setContent(parent);
	parent.setLayout(new GridLayout(2, true));
	
	Group copyGroup = new Group(parent, SWT.NONE);
	copyGroup.setText("Copy From:");
	GridData data = new GridData(GridData.FILL_BOTH);
	copyGroup.setLayoutData(data);
	copyGroup.setLayout(new GridLayout(3, false));
	
	Group pasteGroup = new Group(parent, SWT.NONE);
	pasteGroup.setText("Paste To:");
	data = new GridData(GridData.FILL_BOTH);
	pasteGroup.setLayoutData(data);
	pasteGroup.setLayout(new GridLayout(3, false));
	
	Group controlGroup = new Group(parent, SWT.NONE);
	controlGroup.setText("Control API:");
	data = new GridData(GridData.FILL_BOTH);
	data.horizontalSpan = 2;
	controlGroup.setLayoutData(data);
	controlGroup.setLayout(new GridLayout(5, false));
	
	Group typesGroup = new Group(parent, SWT.NONE);
	typesGroup.setText("Available Types");
	data = new GridData(GridData.FILL_BOTH);
	data.horizontalSpan = 2;
	typesGroup.setLayoutData(data);
	typesGroup.setLayout(new GridLayout(2, false));
	
	status = new Label(parent, SWT.BORDER);
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.horizontalSpan = 2;
	data.heightHint = 60;
	status.setLayoutData(data);
	
	createTextTransfer(copyGroup, pasteGroup);
	createRTFTransfer(copyGroup, pasteGroup);
	createHTMLTransfer(copyGroup, pasteGroup);
	createFileTransfer(copyGroup, pasteGroup);
	createMyTransfer(copyGroup, pasteGroup);
	createControlTransfer(controlGroup);
	createAvailableTypes(typesGroup);
	
	sc.setMinSize(parent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	sc.setExpandHorizontal(true);
	sc.setExpandVertical(true);
	
	Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	Rectangle monitorArea = shell.getMonitor().getClientArea();
	shell.setSize(Math.min(size.x, monitorArea.width - 20), Math.min(size.y, monitorArea.height - 20));
	shell.open();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	clipboard.dispose();
}
void createTextTransfer(Composite copyParent, Composite pasteParent) {
	
	// TextTransfer
	Label l = new Label(copyParent, SWT.NONE);
	l.setText("TextTransfer:"); //$NON-NLS-1$
	copyText = new Text(copyParent, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	copyText.setText("some\nplain\ntext");
	GridData data = new GridData(GridData.FILL_HORIZONTAL);
	data.heightHint = data.widthHint = SIZE;
	copyText.setLayoutData(data);
	Button b = new Button(copyParent, SWT.PUSH);
	b.setText("Copy");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			String data = copyText.getText();
			if (data.length() > 0) {
				status.setText("");
				clipboard.setContents(new Object[] {data}, new Transfer[] {TextTransfer.getInstance()});
			} else {
				status.setText("nothing to copy");
			}
		}
	});
	
	l = new Label(pasteParent, SWT.NONE);
	l.setText("TextTransfer:"); //$NON-NLS-1$
	pasteText = new Text(pasteParent, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.heightHint = data.widthHint = SIZE;
	pasteText.setLayoutData(data);
	b = new Button(pasteParent, SWT.PUSH);
	b.setText("Paste");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			String data = (String)clipboard.getContents(TextTransfer.getInstance());
			if (data != null && data.length() > 0) {
				status.setText("");
				pasteText.setText("begin paste>"+data+"<end paste");
			} else {
				status.setText("nothing to paste");
			}
		}
	});
}
void createRTFTransfer(Composite copyParent, Composite pasteParent){
	//	RTF Transfer
	Label l = new Label(copyParent, SWT.NONE);
	l.setText("RTFTransfer:"); //$NON-NLS-1$
	copyRtfText = new Text(copyParent, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	copyRtfText.setText("some\nrtf\ntext");
	GridData data = new GridData(GridData.FILL_HORIZONTAL);
	data.heightHint = data.widthHint = SIZE;
	copyRtfText.setLayoutData(data);
	Button b = new Button(copyParent, SWT.PUSH);
	b.setText("Copy");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			String data = copyRtfText.getText();
			if (data.length() > 0) {
				status.setText("");
				data = "{\\rtf1{\\colortbl;\\red255\\green0\\blue0;}\\uc1\\b\\i " + data + "}";
				clipboard.setContents(new Object[] {data}, new Transfer[] {RTFTransfer.getInstance()});
			} else {
				status.setText("nothing to copy");
			}
		}
	});
	  
	l = new Label(pasteParent, SWT.NONE);
	l.setText("RTFTransfer:"); //$NON-NLS-1$
	pasteRtfText = new Text(pasteParent, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.heightHint = data.widthHint = SIZE;
	pasteRtfText.setLayoutData(data);
	b = new Button(pasteParent, SWT.PUSH);
	b.setText("Paste");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			String data = (String)clipboard.getContents(RTFTransfer.getInstance());
			if (data != null && data.length() > 0) {
				status.setText("");
				pasteRtfText.setText("start paste>"+data+"<end paste");
			} else {
				status.setText("nothing to paste");
			}
		}
	});
}
void createHTMLTransfer(Composite copyParent, Composite pasteParent){
	//	HTML Transfer
	Label l = new Label(copyParent, SWT.NONE);
	l.setText("HTMLTransfer:"); //$NON-NLS-1$
	copyHtmlText = new Text(copyParent, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	copyHtmlText.setText("<b>Hello World</b>");
	GridData data = new GridData(GridData.FILL_HORIZONTAL);
	data.heightHint = data.widthHint = SIZE;
	copyHtmlText.setLayoutData(data);
	Button b = new Button(copyParent, SWT.PUSH);
	b.setText("Copy");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			String data = copyHtmlText.getText();
			if (data.length() > 0) {
				status.setText("");
				clipboard.setContents(new Object[] {data}, new Transfer[] {HTMLTransfer.getInstance()});
			} else {
				status.setText("nothing to copy");
			}
		}
	});
	  
	l = new Label(pasteParent, SWT.NONE);
	l.setText("HTMLTransfer:"); //$NON-NLS-1$
	pasteHtmlText = new Text(pasteParent, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.heightHint = data.widthHint = SIZE;
	pasteHtmlText.setLayoutData(data);
	b = new Button(pasteParent, SWT.PUSH);
	b.setText("Paste");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			String data = (String)clipboard.getContents(HTMLTransfer.getInstance());
			if (data != null && data.length() > 0) {
				status.setText("");
				pasteHtmlText.setText("start paste>"+data+"<end paste");
			} else {
				status.setText("nothing to paste");
			}
		}
	});
}
void createFileTransfer(Composite copyParent, Composite pasteParent){
	//File Transfer
	Label l = new Label(copyParent, SWT.NONE);
	l.setText("FileTransfer:"); //$NON-NLS-1$
	
	Composite c = new Composite(copyParent, SWT.NONE);
	c.setLayout(new GridLayout(2, false));
	GridData data = new GridData(GridData.FILL_HORIZONTAL);
	c.setLayoutData(data);
	
	copyFileTable = new Table(c, SWT.MULTI | SWT.BORDER);
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.heightHint = data.widthHint = SIZE;
	data.horizontalSpan = 2;
	copyFileTable.setLayoutData(data);
	
	Button b = new Button(c, SWT.PUSH);
	b.setText("Select file(s)");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
			String result = dialog.open();
			if (result != null && result.length() > 0){
				//copyFileTable.removeAll();
				String separator = System.getProperty("file.separator");
				String path = dialog.getFilterPath();
				String[] names = dialog.getFileNames();
				for (int i = 0; i < names.length; i++) {
					TableItem item = new TableItem(copyFileTable, SWT.NONE);
					item.setText(path+separator+names[i]);
				}
			}
		}
	});
	b = new Button(c, SWT.PUSH);
	b.setText("Select directory");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
			String result = dialog.open();
			if (result != null && result.length() > 0){
				//copyFileTable.removeAll();
				TableItem item = new TableItem(copyFileTable, SWT.NONE);
				item.setText(result);
			}
		}
	});
	
	b = new Button(copyParent, SWT.PUSH);
	b.setText("Copy");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			TableItem[] items = copyFileTable.getItems();
			if (items.length > 0){
				status.setText("");
				String[] data = new String[items.length];
				for (int i = 0; i < data.length; i++) {
					data[i] = items[i].getText();
				}
				clipboard.setContents(new Object[] {data}, new Transfer[] {FileTransfer.getInstance()});
			} else {
				status.setText("nothing to copy");
			}
		}
	});
	
	l = new Label(pasteParent, SWT.NONE);
	l.setText("FileTransfer:"); //$NON-NLS-1$
	pasteFileTable = new Table(pasteParent, SWT.MULTI | SWT.BORDER);
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.heightHint = data.widthHint = SIZE;
	pasteFileTable.setLayoutData(data);
	b = new Button(pasteParent, SWT.PUSH);
	b.setText("Paste");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			String[] data = (String[])clipboard.getContents(FileTransfer.getInstance());
			if (data != null && data.length > 0) {
				status.setText("");
				pasteFileTable.removeAll();
				for (int i = 0; i < data.length; i++) {
					TableItem item = new TableItem(pasteFileTable, SWT.NONE);
					item.setText(data[i]);
				}
			} else {
				status.setText("nothing to paste");
			}
		}
	});	 
}
void createMyTransfer(Composite copyParent, Composite pasteParent){
	//	MyType Transfer
	// TODO
}
void createControlTransfer(Composite parent){
	Label l = new Label(parent, SWT.NONE);
	l.setText("Text:");
	Button b = new Button(parent, SWT.PUSH);
	b.setText("Cut");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			text.cut();
		}
	});
	b = new Button(parent, SWT.PUSH);
	b.setText("Copy");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			text.copy();
		}
	});
	b = new Button(parent, SWT.PUSH);
	b.setText("Paste");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			text.paste();
		}
	});
	text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
	GridData data = new GridData(GridData.FILL_HORIZONTAL);
	data.heightHint = data.widthHint = SIZE;
	text.setLayoutData(data);
	
	l = new Label(parent, SWT.NONE);
	l.setText("Combo:");
	b = new Button(parent, SWT.PUSH);
	b.setText("Cut");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			combo.cut();
		}
	});
	b = new Button(parent, SWT.PUSH);
	b.setText("Copy");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			combo.copy();
		}
	});
	b = new Button(parent, SWT.PUSH);
	b.setText("Paste");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			combo.paste();
		}
	});
	combo = new Combo(parent, SWT.NONE);
	combo.setItems(new String[] {"Item 1", "Item 2", "Item 3", "A longer Item"});
	
	l = new Label(parent, SWT.NONE);
	l.setText("StyledText:");
	l = new Label(parent, SWT.NONE);
	l.setVisible(false);
	b = new Button(parent, SWT.PUSH);
	b.setText("Copy");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			styledText.copy();
		}
	});
	b = new Button(parent, SWT.PUSH);
	b.setText("Paste");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			styledText.paste();
		}
	});
	styledText = new StyledText(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.heightHint = data.widthHint = SIZE;
	styledText.setLayoutData(data);
}
void createAvailableTypes(Composite parent){
	final List list = new List(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	GridData data = new GridData(GridData.FILL_BOTH);
	data.heightHint = 100;
	list.setLayoutData(data);
	Button b = new Button(parent, SWT.PUSH);
	b.setText("Get Available Types");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			list.removeAll();
			String[] names = clipboard.getAvailableTypeNames();
			for (int i = 0; i < names.length; i++) {
				list.add(names[i]);
			}
		}
	});
}
}