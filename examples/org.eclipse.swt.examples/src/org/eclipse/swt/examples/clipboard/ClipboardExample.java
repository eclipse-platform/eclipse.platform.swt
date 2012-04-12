/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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
	Text text;
	Combo combo;
	StyledText styledText;
	Label status;
	static final int HSIZE = 100, VSIZE = 60;
	
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
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.horizontalSpan = 2;
	controlGroup.setLayoutData(data);
	controlGroup.setLayout(new GridLayout(5, false));
	
	Group typesGroup = new Group(parent, SWT.NONE);
	typesGroup.setText("Available Types");
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.horizontalSpan = 2;
	typesGroup.setLayoutData(data);
	typesGroup.setLayout(new GridLayout(2, false));
	
	status = new Label(parent, SWT.NONE);
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.horizontalSpan = 2;
	status.setLayoutData(data);
	
	createTextTransfer(copyGroup, pasteGroup);
	createRTFTransfer(copyGroup, pasteGroup);
	createHTMLTransfer(copyGroup, pasteGroup);
	createFileTransfer(copyGroup, pasteGroup);
	createImageTransfer(copyGroup, pasteGroup);
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
	final Text copyText = new Text(copyParent, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	copyText.setText("some\nplain\ntext");
	GridData data = new GridData(GridData.FILL_BOTH);
	data.widthHint = HSIZE;
	data.heightHint = VSIZE;
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
				status.setText("No text to copy");
			}
		}
	});
	
	l = new Label(pasteParent, SWT.NONE);
	l.setText("TextTransfer:"); //$NON-NLS-1$
	final Text pasteText = new Text(pasteParent, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	data = new GridData(GridData.FILL_BOTH);
	data.widthHint = HSIZE;
	data.heightHint = VSIZE;
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
				status.setText("No text to paste");
			}
		}
	});
}
void createRTFTransfer(Composite copyParent, Composite pasteParent){
	//	RTF Transfer
	Label l = new Label(copyParent, SWT.NONE);
	l.setText("RTFTransfer:"); //$NON-NLS-1$
	final Text copyRtfText = new Text(copyParent, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	copyRtfText.setText("some\nrtf\ntext");
	GridData data = new GridData(GridData.FILL_BOTH);
	data.widthHint = HSIZE;
	data.heightHint = VSIZE;
	copyRtfText.setLayoutData(data);
	Button b = new Button(copyParent, SWT.PUSH);
	b.setText("Copy");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			String data = copyRtfText.getText();
			if (data.length() > 0) {
				status.setText("");
				StringBuffer buffer = new StringBuffer();
				buffer.append("{\\rtf1\\ansi\\uc1{\\colortbl;\\red255\\green0\\blue0;}\\uc1\\b\\i ");
				for (int i = 0; i < data.length(); i++) {
					char ch = data.charAt(i);
					if (ch > 0xFF) {
						buffer.append("\\u");
						buffer.append(Integer.toString((short) ch));
						buffer.append('?');
					} else {
						if (ch == '}' || ch == '{' || ch == '\\') {
							buffer.append('\\');
						}
						buffer.append(ch);
						if (ch == '\n') buffer.append("\\par ");
						if (ch == '\r' && (i - 1 == data.length() || data.charAt(i + 1) != '\n')) {
							buffer.append("\\par ");
						}
					}
				}
				buffer.append("}");
				clipboard.setContents(new Object[] {buffer.toString()}, new Transfer[] {RTFTransfer.getInstance()});
			} else {
				status.setText("No RTF to copy");
			}
		}
	});
	  
	l = new Label(pasteParent, SWT.NONE);
	l.setText("RTFTransfer:"); //$NON-NLS-1$
	final Text pasteRtfText = new Text(pasteParent, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	data = new GridData(GridData.FILL_BOTH);
	data.widthHint = HSIZE;
	data.heightHint = VSIZE;
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
				status.setText("No RTF to paste");
			}
		}
	});
}
void createHTMLTransfer(Composite copyParent, Composite pasteParent){
	//	HTML Transfer
	Label l = new Label(copyParent, SWT.NONE);
	l.setText("HTMLTransfer:"); //$NON-NLS-1$
	final Text copyHtmlText = new Text(copyParent, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	copyHtmlText.setText("<b>Hello World</b>");
	GridData data = new GridData(GridData.FILL_BOTH);
	data.widthHint = HSIZE;
	data.heightHint = VSIZE;
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
				status.setText("No HTML to copy");
			}
		}
	});
	  
	l = new Label(pasteParent, SWT.NONE);
	l.setText("HTMLTransfer:"); //$NON-NLS-1$
	final Text pasteHtmlText = new Text(pasteParent, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	data = new GridData(GridData.FILL_BOTH);
	data.widthHint = HSIZE;
	data.heightHint = VSIZE;
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
				status.setText("No HTML to paste");
			}
		}
	});
}
void createFileTransfer(Composite copyParent, Composite pasteParent){
	//File Transfer
	Label l = new Label(copyParent, SWT.NONE);
	l.setText("FileTransfer:"); //$NON-NLS-1$
	GridData data = new GridData();
	data.verticalSpan = 3;
	l.setLayoutData(data);
	
	final Table copyFileTable = new Table(copyParent, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	data = new GridData(GridData.FILL_BOTH);
	data.widthHint = HSIZE;
	data.heightHint = VSIZE;
	data.verticalSpan = 3;
	copyFileTable.setLayoutData(data);
	
	Button b = new Button(copyParent, SWT.PUSH);
	b.setText("Select file(s)");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
			String result = dialog.open();
			if (result != null && result.length() > 0){
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
	b = new Button(copyParent, SWT.PUSH);
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
				status.setText("No file to copy");
			}
		}
	});
	
	l = new Label(pasteParent, SWT.NONE);
	l.setText("FileTransfer:"); //$NON-NLS-1$
	final Table pasteFileTable = new Table(pasteParent, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	data = new GridData(GridData.FILL_BOTH);
	data.widthHint = HSIZE;
	data.heightHint = VSIZE;
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
				status.setText("No file to paste");
			}
		}
	});	 
}

void createImageTransfer(Composite copyParent, Composite pasteParent){
	final Image[] copyImage = new Image[] {null};
	Label l = new Label(copyParent, SWT.NONE);
	l.setText("ImageTransfer:"); //$NON-NLS-1$
	GridData data = new GridData();
	data.verticalSpan = 2;
	l.setLayoutData(data);
	
	final Canvas copyImageCanvas = new Canvas(copyParent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	data = new GridData(GridData.FILL_BOTH);
	data.verticalSpan = 2;
	data.widthHint = HSIZE;
	data.heightHint = VSIZE;
	copyImageCanvas.setLayoutData(data);	
	
	final Point copyOrigin = new Point(0, 0);
	final ScrollBar copyHBar = copyImageCanvas.getHorizontalBar();
	copyHBar.setEnabled(false);	
	copyHBar.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event e) {
			if (copyImage[0] != null) {
				int hSelection = copyHBar.getSelection();
				int destX = -hSelection - copyOrigin.x;
				Rectangle rect = copyImage[0].getBounds();
				copyImageCanvas.scroll(destX, 0, 0, 0, rect.width, rect.height, false);
				copyOrigin.x = -hSelection;
			}
		}
	});
	final ScrollBar copyVBar = copyImageCanvas.getVerticalBar();
	copyVBar.setEnabled(false);
	copyVBar.addListener(SWT.Selection, new Listener() {
		public void handleEvent (Event e) {
			if (copyImage[0] != null) {
				int vSelection = copyVBar.getSelection();
				int destY = -vSelection - copyOrigin.y;
				Rectangle rect = copyImage[0].getBounds();
				copyImageCanvas.scroll(0, destY, 0, 0, rect.width, rect.height, false);
				copyOrigin.y = -vSelection;
			}
		}
	});
	copyImageCanvas.addListener(SWT.Paint, new Listener() {
		public void handleEvent(Event e) {
			if(copyImage[0] != null) {
				GC gc = e.gc;
				gc.drawImage(copyImage[0], copyOrigin.x, copyOrigin.y);
				Rectangle rect = copyImage[0].getBounds();
				Rectangle client = copyImageCanvas.getClientArea ();
				int marginWidth = client.width - rect.width;
				if (marginWidth > 0) {
					gc.fillRectangle (rect.width, 0, marginWidth, client.height);
				}
				int marginHeight = client.height - rect.height;
				if (marginHeight > 0) {
					gc.fillRectangle(0, rect.height, client.width, marginHeight);
				}
				gc.dispose();
			}
		}
	});
	Button openButton = new Button(copyParent, SWT.PUSH);
	openButton.setText("Open Image");
	openButton.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			FileDialog dialog = new FileDialog (shell, SWT.OPEN);
			dialog.setText("Open an image file or cancel");
			String string = dialog.open ();
			if (string != null) {
				if (copyImage[0] != null) {
					System.out.println("CopyImage");
					copyImage[0].dispose();
				}
				copyImage[0] = new Image(e.display, string);
				copyVBar.setEnabled(true);
				copyHBar.setEnabled(true);
				copyOrigin.x = 0; copyOrigin.y = 0;
				Rectangle rect = copyImage[0].getBounds();
				Rectangle client = copyImageCanvas.getClientArea();		
				copyHBar.setMaximum(rect.width);
				copyVBar.setMaximum(rect.height);
				copyHBar.setThumb(Math.min(rect.width, client.width));
				copyVBar.setThumb(Math.min(rect.height, client.height));
				copyImageCanvas.scroll(0, 0, 0, 0, rect.width, rect.height, true);
				copyVBar.setSelection(0);
				copyHBar.setSelection(0);
				copyImageCanvas.redraw();
			}
		}
	});
	Button b = new Button(copyParent, SWT.PUSH);
	b.setText("Copy");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			if (copyImage[0] != null) {
				status.setText("");
				clipboard.setContents(new Object[] {copyImage[0].getImageData()}, new Transfer[] {ImageTransfer.getInstance()});
			} else {
				status.setText("No image to copy");
			}
		}
	});
	
	final Image[] pasteImage = new Image[] {null};	  
	l = new Label(pasteParent, SWT.NONE);
	l.setText("ImageTransfer:");
	final Canvas pasteImageCanvas = new Canvas(pasteParent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	data = new GridData(GridData.FILL_BOTH);
	data.widthHint = HSIZE;
	data.heightHint = VSIZE;
	pasteImageCanvas.setLayoutData(data);
	final Point pasteOrigin = new Point(0, 0);
	final ScrollBar pasteHBar = pasteImageCanvas.getHorizontalBar();
	pasteHBar.setEnabled(false);	
	pasteHBar.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event e) {
			if (pasteImage[0] != null) {
				int hSelection = pasteHBar.getSelection();
				int destX = -hSelection - pasteOrigin.x;
				Rectangle rect = pasteImage[0].getBounds();
				pasteImageCanvas.scroll(destX, 0, 0, 0, rect.width, rect.height, false);
				pasteOrigin.x = -hSelection;
			}
		}
	});
	final ScrollBar pasteVBar = pasteImageCanvas.getVerticalBar();
	pasteVBar.setEnabled(false);
	pasteVBar.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event e) {
			if (pasteImage[0] != null) {
				int vSelection = pasteVBar.getSelection();
				int destY = -vSelection - pasteOrigin.y;
				Rectangle rect = pasteImage[0].getBounds();
				pasteImageCanvas.scroll(0, destY, 0, 0, rect.width, rect.height, false);
				pasteOrigin.y = -vSelection;
			}
		}
	});
	pasteImageCanvas.addListener(SWT.Paint, new Listener() {
		public void handleEvent(Event e) {
			if(pasteImage[0] != null) {
				GC gc = e.gc;
				gc.drawImage(pasteImage[0], pasteOrigin.x, pasteOrigin.y);
				Rectangle rect = pasteImage[0].getBounds();
				Rectangle client = pasteImageCanvas.getClientArea ();
				int marginWidth = client.width - rect.width;
				if (marginWidth > 0) {
					gc.fillRectangle(rect.width, 0, marginWidth, client.height);
				}
				int marginHeight = client.height - rect.height;
				if (marginHeight > 0) {
					gc.fillRectangle(0, rect.height, client.width, marginHeight);
				}
			}
		}
	});
	b = new Button(pasteParent, SWT.PUSH);
	b.setText("Paste");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {		
			ImageData data =(ImageData)clipboard.getContents(ImageTransfer.getInstance());
			if (data != null) {
				if (pasteImage[0] != null) {
					System.out.println("PasteImage");
					pasteImage[0].dispose();
				}
				status.setText("");
				pasteImage[0] = new Image(e.display, data);
				pasteVBar.setEnabled(true);
				pasteHBar.setEnabled(true);
				pasteOrigin.x = 0; pasteOrigin.y = 0;
				Rectangle rect = pasteImage[0].getBounds();
				Rectangle client = pasteImageCanvas.getClientArea();		
				pasteHBar.setMaximum(rect.width);
				pasteVBar.setMaximum(rect.height);
				pasteHBar.setThumb(Math.min(rect.width, client.width));
				pasteVBar.setThumb(Math.min(rect.height, client.height));
				pasteImageCanvas.scroll(0, 0, 0, 0, rect.width, rect.height, true);
				pasteVBar.setSelection(0);
				pasteHBar.setSelection(0);
				pasteImageCanvas.redraw();
			} else {
				status.setText("No image to paste");
			}			
		}
	});	
}
void createMyTransfer(Composite copyParent, Composite pasteParent){
	//	MyType Transfer
	// TODO
}
void createControlTransfer(Composite parent){
	// TODO: CCombo and Spinner also have cut(), copy() and paste() API
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
	data.widthHint = HSIZE;
	data.heightHint = VSIZE;
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
	combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	
	l = new Label(parent, SWT.NONE);
	l.setText("StyledText:");
	b = new Button(parent, SWT.PUSH);
	b.setText("Cut");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			styledText.cut();
		}
	});
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
	data.widthHint = HSIZE;
	data.heightHint = VSIZE;
	styledText.setLayoutData(data);
}
void createAvailableTypes(Composite parent){
	final List list = new List(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	GridData data = new GridData(GridData.FILL_BOTH);
	data.heightHint = VSIZE;
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