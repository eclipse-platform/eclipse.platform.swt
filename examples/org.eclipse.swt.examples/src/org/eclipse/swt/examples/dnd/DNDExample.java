/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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
package org.eclipse.swt.examples.dnd;


import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.HTMLTransfer;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.URLTransfer;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class DNDExample {

	private int dragOperation = 0;
	private Transfer[] dragTypes = new Transfer[0];
	private Control dragControl;
	private int dragControlType = 0;
	private DragSource dragSource;
	private String dragDataText;
	private String dragDataRTF;
	private String dragDataHTML;
	private String dragDataURL;
	private String[] dragDataFiles;
	private List fileList;
	private boolean dragEnabled = false;

	private int dropOperation = 0;
	private int dropFeedback = 0;
	private int dropDefaultOperation = 0;
	private Transfer[] dropTypes = new Transfer[0];
	private DropTarget dropTarget;
	private Control dropControl;
	private int dropControlType = 0;
	private Composite defaultParent;
	private boolean dropEnabled = false;

	private Text dragConsole;
	private boolean dragEventDetail = false;
	private Text dropConsole;
	private boolean dropEventDetail = false;

	private Image itemImage;

	private static final int BUTTON_TOGGLE = 0;
	private static final int BUTTON_RADIO = 1;
	private static final int BUTTON_CHECK = 2;
	private static final int CANVAS = 3;
	private static final int LABEL = 4;
	private static final int LIST = 5;
	private static final int TABLE = 6;
	private static final int TREE = 7;
	private static final int TEXT = 8;
	private static final int STYLED_TEXT = 9;
	private static final int COMBO = 10;

public static void main(String[] args) {
	Display display = new Display();
	DNDExample example = new DNDExample();
	example.open(display);
	display.dispose();
}

private void addDragTransfer(Transfer transfer){
	Transfer[] newTypes = new Transfer[dragTypes.length + 1];
	System.arraycopy(dragTypes, 0, newTypes, 0, dragTypes.length);
	newTypes[dragTypes.length] = transfer;
	dragTypes = newTypes;
	if (dragSource != null) {
		dragSource.setTransfer(dragTypes);
	}
}

private void addDropTransfer(Transfer transfer){
	Transfer[] newTypes = new Transfer[dropTypes.length + 1];
	System.arraycopy(dropTypes, 0, newTypes, 0, dropTypes.length);
	newTypes[dropTypes.length] = transfer;
	dropTypes = newTypes;
	if (dropTarget != null) {
		dropTarget.setTransfer(dropTypes);
	}
}

private void createDragOperations(Composite parent) {
	parent.setLayout(new RowLayout(SWT.VERTICAL));
	final Button moveButton = new Button(parent, SWT.CHECK);
	moveButton.setText("DND.DROP_MOVE");
	moveButton.addSelectionListener(widgetSelectedAdapter(e -> {
		Button b = (Button) e.widget;
		if (b.getSelection()) {
			dragOperation |= DND.DROP_MOVE;
		} else {
			dragOperation = dragOperation & ~DND.DROP_MOVE;
			if (dragOperation == 0) {
				dragOperation = DND.DROP_MOVE;
				moveButton.setSelection(true);
			}
		}
		if (dragEnabled) {
			createDragSource();
		}
	}));


	Button copyButton = new Button(parent, SWT.CHECK);
	copyButton.setText("DND.DROP_COPY");
	copyButton.addSelectionListener(widgetSelectedAdapter(e -> {
		Button b = (Button) e.widget;
		if (b.getSelection()) {
			dragOperation |= DND.DROP_COPY;
		} else {
			dragOperation = dragOperation & ~DND.DROP_COPY;
			if (dragOperation == 0) {
				dragOperation = DND.DROP_MOVE;
				moveButton.setSelection(true);
			}
		}
		if (dragEnabled) {
			createDragSource();
		}
	}));

	Button linkButton = new Button(parent, SWT.CHECK);
	linkButton.setText("DND.DROP_LINK");
	linkButton.addSelectionListener(widgetSelectedAdapter(e -> {
		Button b = (Button) e.widget;
		if (b.getSelection()) {
			dragOperation |= DND.DROP_LINK;
		} else {
			dragOperation = dragOperation & ~DND.DROP_LINK;
			if (dragOperation == 0) {
				dragOperation = DND.DROP_MOVE;
				moveButton.setSelection(true);
			}
		}
		if (dragEnabled) {
			createDragSource();
		}
	}));

	//initialize state
	moveButton.setSelection(true);
	copyButton.setSelection(true);
	linkButton.setSelection(true);
	dragOperation |= DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
}

private void createDragSource() {
	if (dragSource != null) dragSource.dispose();
	dragSource = new DragSource(dragControl, dragOperation);
	dragSource.setTransfer(dragTypes);
	dragSource.addDragListener(new DragSourceListener() {
		@Override
		public void dragFinished(org.eclipse.swt.dnd.DragSourceEvent event) {
			dragConsole.append(">>dragFinished\n");
			printEvent(event);
			dragDataText = dragDataRTF = dragDataHTML = dragDataURL = null;
			dragDataFiles = null;
			if (event.detail == DND.DROP_MOVE) {
				switch(dragControlType) {
					case BUTTON_CHECK:
					case BUTTON_TOGGLE:
					case BUTTON_RADIO: {
						Button b = (Button)dragControl;
						b.setText("");
						break;
					}
					case STYLED_TEXT: {
						StyledText text = (StyledText)dragControl;
						text.insert("");
						break;
					}
					case TABLE: {
						Table table = (Table)dragControl;
						TableItem[] items = table.getSelection();
						for (TableItem item : items) {
							item.dispose();
						}
						break;
					}
					case TEXT: {
						Text text = (Text)dragControl;
						text.insert("");
						break;
					}
					case TREE: {
						Tree tree = (Tree)dragControl;
						TreeItem[] items = tree.getSelection();
						for (TreeItem item : items) {
							item.dispose();
						}
						break;
					}
					case CANVAS: {
						dragControl.setData("STRINGS", null);
						dragControl.redraw();
						break;
					}
					case LABEL: {
						Label label = (Label)dragControl;
						label.setText("");
						break;
					}
					case LIST: {
						List list = (List)dragControl;
						int[] indices = list.getSelectionIndices();
						list.remove(indices);
						break;
					}
					case COMBO:{
						Combo combo = (Combo)dragControl;
						combo.setText("");
						break;
					}
				}
			}
		}
		@Override
		public void dragSetData(org.eclipse.swt.dnd.DragSourceEvent event) {
			dragConsole.append(">>dragSetData\n");
			printEvent(event);
			if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
				event.data = dragDataText;
			}
			if (RTFTransfer.getInstance().isSupportedType(event.dataType)) {
				event.data = dragDataRTF;
			}
			if (HTMLTransfer.getInstance().isSupportedType(event.dataType)) {
				event.data = dragDataHTML;
			}
			if (URLTransfer.getInstance().isSupportedType(event.dataType)) {
				event.data = dragDataURL;
			}
			if (FileTransfer.getInstance().isSupportedType(event.dataType)) {
				event.data = dragDataFiles;
			}
		}
		@Override
		public void dragStart(org.eclipse.swt.dnd.DragSourceEvent event) {
			dragConsole.append(">>dragStart\n");
			printEvent(event);
			dragDataFiles = fileList.getItems();
			switch(dragControlType) {
				case BUTTON_CHECK:
				case BUTTON_TOGGLE:
				case BUTTON_RADIO: {
					Button b = (Button)dragControl;
					dragDataText = b.getSelection() ? "true" : "false";
					break;
				}
				case STYLED_TEXT: {
					StyledText text = (StyledText)dragControl;
					String s = text.getSelectionText();
					if (s.length() == 0) {
						event.doit = false;
					} else {
						dragDataText = s;
					}
					break;
				}
				case TABLE: {
					Table table = (Table)dragControl;
					TableItem[] items = table.getSelection();
					if (items.length == 0) {
						event.doit = false;
					} else {
						StringBuilder buffer = new StringBuilder();
						for (int i = 0; i < items.length; i++) {
							buffer.append(items[i].getText());
							if (items.length > 1 && i < items.length - 1) {
								buffer.append("\n");
							}
						}
						dragDataText = buffer.toString();
					}
					break;
				}
				case TEXT: {
					Text text = (Text)dragControl;
					String s = text.getSelectionText();
					if (s.length() == 0) {
						event.doit = false;
					} else {
						dragDataText = s;
					}
					break;
				}
				case TREE: {
					Tree tree = (Tree)dragControl;
					TreeItem[] items = tree.getSelection();
					if (items.length == 0) {
						event.doit = false;
					} else {
						StringBuilder buffer = new StringBuilder();
						for (int i = 0; i < items.length; i++) {
							buffer.append(items[i].getText());
							if (items.length > 1 && i < items.length - 1) {
								buffer.append("\n");
							}
						}
						dragDataText = buffer.toString();
					}
					break;
				}
				case CANVAS: {
					String[] strings = (String[])dragControl.getData("STRINGS");
					if (strings == null || strings.length == 0) {
						event.doit = false;
					} else {
						StringBuilder buffer = new StringBuilder();
						for (int i = 0; i < strings.length; i++) {
							buffer.append(strings[i]);
							if (strings.length > 1 && i < strings.length - 1) {
								buffer.append("\n");
							}
						}
						dragDataText = buffer.toString();
					}
					break;
				}
				case LABEL: {
					Label label = (Label)dragControl;
					String string = label.getText();
					if (string.length() == 0) {
						event.doit = false;
					} else {
						dragDataText = string;
					}
					break;
				}
				case LIST: {
					List list = (List)dragControl;
					String[] selection = list.getSelection();
					if (selection.length == 0) {
						event.doit = false;
					} else {
						StringBuilder buffer = new StringBuilder();
						for (int i = 0; i < selection.length; i++) {
							buffer.append(selection[i]);
							if (selection.length > 1 && i < selection.length - 1) {
								buffer.append("\n");
							}
						}
						dragDataText = buffer.toString();
					}
					break;
				}
				case COMBO: {
					Combo combo = (Combo) dragControl;
					String string = combo.getText();
					Point selection = combo.getSelection();
					if (selection.x == selection.y) {
						event.doit = false;
					} else {
						dragDataText = string.substring(selection.x, selection.y);
					}
					break;
				}
				default:
					throw new SWTError(SWT.ERROR_NOT_IMPLEMENTED);
			}
			if (dragDataText != null) {
				dragDataRTF = "{\\rtf1{\\colortbl;\\red255\\green0\\blue0;}\\cf1\\b "+dragDataText+"}";
				dragDataHTML = "<b>"+dragDataText+"</b>";
				dragDataURL = "http://" + dragDataText.replace(SWT.SPACE, '.');
				try {
					new URL(dragDataURL);
				} catch (MalformedURLException e) {
					dragDataURL = null;
				}
			}

			for (Transfer dragType : dragTypes) {
				if (dragType instanceof TextTransfer && dragDataText == null) {
					event.doit = false;
				}
				if (dragType instanceof RTFTransfer && dragDataRTF == null) {
					event.doit = false;
				}
				if (dragType instanceof HTMLTransfer && dragDataHTML == null) {
					event.doit = false;
				}
				if (dragType instanceof URLTransfer && dragDataURL == null) {
					event.doit = false;
				}
				if (dragType instanceof FileTransfer && (dragDataFiles == null || dragDataFiles.length == 0)) {
					event.doit = false;
				}
			}
		}
	});
}

private void createDragTypes(Composite parent) {
	parent.setLayout(new GridLayout());
	Button textButton = new Button(parent, SWT.CHECK);
	textButton.setText("Text Transfer");
	textButton.addSelectionListener(widgetSelectedAdapter(e -> {
		Button b = (Button) e.widget;
		if (b.getSelection()) {
			addDragTransfer(TextTransfer.getInstance());
		} else {
			removeDragTransfer(TextTransfer.getInstance());
		}
	}));

	Button b = new Button(parent, SWT.CHECK);
	b.setText("RTF Transfer");
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button b1 = (Button) e.widget;
		if (b1.getSelection()) {
			addDragTransfer(RTFTransfer.getInstance());
		} else {
			removeDragTransfer(RTFTransfer.getInstance());
		}
	}));

	b = new Button(parent, SWT.CHECK);
	b.setText("HTML Transfer");
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button b2 = (Button) e.widget;
		if (b2.getSelection()) {
			addDragTransfer(HTMLTransfer.getInstance());
		} else {
			removeDragTransfer(HTMLTransfer.getInstance());
		}
	}));

	b = new Button(parent, SWT.CHECK);
	b.setText("URL Transfer");
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button b3 = (Button) e.widget;
		if (b3.getSelection()) {
			addDragTransfer(URLTransfer.getInstance());
		} else {
			removeDragTransfer(URLTransfer.getInstance());
		}
	}));

	b = new Button(parent, SWT.CHECK);
	b.setText("File Transfer");
	b.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button b4 = (Button) e.widget;
		if (b4.getSelection()) {
			addDragTransfer(FileTransfer.getInstance());
		} else {
			removeDragTransfer(FileTransfer.getInstance());
		}
	}));
	b = new Button(parent, SWT.PUSH);
	b.setText("Select File(s)");
	b.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		FileDialog dialog = new FileDialog(fileList.getShell(), SWT.OPEN | SWT.MULTI);
		String result = dialog.open();
		if (result != null && result.length() > 0) {
			fileList.removeAll();
			String path = dialog.getFilterPath();
			String[] names = dialog.getFileNames();
			for (String name : names) {
				fileList.add(path + File.separatorChar + name);
			}
		}
	}));
	fileList = new List(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	GridData data = new GridData();
	data.grabExcessHorizontalSpace = true;
	data.horizontalAlignment = GridData.FILL;
	data.verticalAlignment = GridData.BEGINNING;
	fileList.setLayoutData(data);

	// initialize state
	textButton.setSelection(true);
	addDragTransfer(TextTransfer.getInstance());
}

private void createDragWidget(Composite parent) {
	parent.setLayout(new FormLayout());
	Combo combo = new Combo(parent, SWT.READ_ONLY);
	combo.setItems("Toggle Button", "Radio Button", "Checkbox", "Canvas", "Label", "List", "Table", "Tree", "Text", "StyledText", "Combo");
	combo.select(LABEL);
	dragControlType = combo.getSelectionIndex();
	dragControl = createWidget(dragControlType, parent, "Drag Source");

	combo.addSelectionListener(widgetSelectedAdapter(e -> {
		Object data = dragControl.getLayoutData();
		Composite dragParent = dragControl.getParent();
		dragControl.dispose();
		Combo c = (Combo) e.widget;
		dragControlType = c.getSelectionIndex();
		dragControl = createWidget(dragControlType, dragParent, "Drag Source");
		dragControl.setLayoutData(data);
		if (dragEnabled)
			createDragSource();
		dragParent.layout();
	}));

	Button b = new Button(parent, SWT.CHECK);
	b.setText("DragSource");
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button b1 = (Button) e.widget;
		dragEnabled = b1.getSelection();
		if (dragEnabled) {
			createDragSource();
		} else {
			if (dragSource != null) {
				dragSource.dispose();
			}
			dragSource = null;
		}
	}));
	b.setSelection(true);
	dragEnabled = true;

	FormData data = new FormData();
	data.top = new FormAttachment(0, 10);
	data.bottom = new FormAttachment(combo, -10);
	data.left = new FormAttachment(0, 10);
	data.right = new FormAttachment(100, -10);
	dragControl.setLayoutData(data);

	data = new FormData();
	data.bottom = new FormAttachment(100, -10);
	data.left = new FormAttachment(0, 10);
	combo.setLayoutData(data);

	data = new FormData();
	data.bottom = new FormAttachment(100, -10);
	data.left = new FormAttachment(combo, 10);
	b.setLayoutData(data);
}

private void createDropOperations(Composite parent) {
	parent.setLayout(new RowLayout(SWT.VERTICAL));
	final Button moveButton = new Button(parent, SWT.CHECK);
	moveButton.setText("DND.DROP_MOVE");
	moveButton.addSelectionListener(widgetSelectedAdapter(e -> {
		Button b = (Button) e.widget;
		if (b.getSelection()) {
			dropOperation |= DND.DROP_MOVE;
		} else {
			dropOperation = dropOperation & ~DND.DROP_MOVE;
			if (dropOperation == 0 || (dropDefaultOperation & DND.DROP_MOVE) != 0) {
				dropOperation |= DND.DROP_MOVE;
				moveButton.setSelection(true);
			}
		}
		if (dropEnabled) {
			createDropTarget();
		}
	}));


	final Button copyButton = new Button(parent, SWT.CHECK);
	copyButton.setText("DND.DROP_COPY");
	copyButton.addSelectionListener(widgetSelectedAdapter(e -> {
		Button b = (Button) e.widget;
		if (b.getSelection()) {
			dropOperation |= DND.DROP_COPY;
		} else {
			dropOperation = dropOperation & ~DND.DROP_COPY;
			if (dropOperation == 0 || (dropDefaultOperation & DND.DROP_COPY) != 0) {
				dropOperation = DND.DROP_COPY;
				copyButton.setSelection(true);
			}
		}
		if (dropEnabled) {
			createDropTarget();
		}
	}));

	final Button linkButton = new Button(parent, SWT.CHECK);
	linkButton.setText("DND.DROP_LINK");
	linkButton.addSelectionListener(widgetSelectedAdapter(e -> {
		Button eb = (Button) e.widget;
		if (eb.getSelection()) {
			dropOperation |= DND.DROP_LINK;
		} else {
			dropOperation = dropOperation & ~DND.DROP_LINK;
			if (dropOperation == 0 || (dropDefaultOperation & DND.DROP_LINK) != 0) {
				dropOperation = DND.DROP_LINK;
				linkButton.setSelection(true);
			}
		}
		if (dropEnabled) {
			createDropTarget();
		}
	}));

	Button b = new Button(parent, SWT.CHECK);
	b.setText("DND.DROP_DEFAULT");
	defaultParent = new Composite(parent, SWT.NONE);
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button eb = (Button) e.widget;
		if (eb.getSelection()) {
			dropOperation |= DND.DROP_DEFAULT;
			defaultParent.setVisible(true);
		} else {
			dropOperation = dropOperation & ~DND.DROP_DEFAULT;
			defaultParent.setVisible(false);
		}
		if (dropEnabled) {
			createDropTarget();
		}
	}));

	defaultParent.setVisible(false);
	GridLayout layout = new GridLayout();
	layout.marginWidth = 20;
	defaultParent.setLayout(layout);
	Label label = new Label(defaultParent, SWT.NONE);
	label.setText("Value for default operation is:");
	b = new Button(defaultParent, SWT.RADIO);
	b.setText("DND.DROP_MOVE");
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button eb = (Button) e.widget;
		if (eb.getSelection()) {
			dropDefaultOperation = DND.DROP_MOVE;
			dropOperation |= DND.DROP_MOVE;
			moveButton.setSelection(true);
			if (dropEnabled) {
				createDropTarget();
			}
		}
	}));

	b = new Button(defaultParent, SWT.RADIO);
	b.setText("DND.DROP_COPY");
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button eb = (Button) e.widget;
		if (eb.getSelection()) {
			dropDefaultOperation = DND.DROP_COPY;
			dropOperation |= DND.DROP_COPY;
			copyButton.setSelection(true);
			if (dropEnabled) {
				createDropTarget();
			}
		}
	}));

	b = new Button(defaultParent, SWT.RADIO);
	b.setText("DND.DROP_LINK");
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button eb = (Button) e.widget;
		if (eb.getSelection()) {
			dropDefaultOperation = DND.DROP_LINK;
			dropOperation |= DND.DROP_LINK;
			linkButton.setSelection(true);
			if (dropEnabled) {
				createDropTarget();
			}
		}
	}));

	b = new Button(defaultParent, SWT.RADIO);
	b.setText("DND.DROP_NONE");
	b.setSelection(true);
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button eb = (Button) e.widget;
		if (eb.getSelection()) {
			dropDefaultOperation = DND.DROP_NONE;
			dropOperation &= ~DND.DROP_DEFAULT;
			if (dropEnabled) {
				createDropTarget();
			}
		}
	}));

	// initialize state
	moveButton.setSelection(true);
	copyButton.setSelection(true);
	linkButton.setSelection(true);
	dropOperation = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
}

private void createDropTarget() {
	if (dropTarget != null) dropTarget.dispose();
	dropTarget = new DropTarget(dropControl, dropOperation);
	dropTarget.setTransfer(dropTypes);
	dropTarget.addDropListener(new DropTargetListener() {
		@Override
		public void dragEnter(DropTargetEvent event) {
			dropConsole.append(">>dragEnter\n");
			printEvent(event);
			if (event.detail == DND.DROP_DEFAULT) {
				event.detail = dropDefaultOperation;
			}
			event.feedback = dropFeedback;
		}
		@Override
		public void dragLeave(DropTargetEvent event) {
			dropConsole.append(">>dragLeave\n");
			printEvent(event);
		}
		@Override
		public void dragOperationChanged(DropTargetEvent event) {
			dropConsole.append(">>dragOperationChanged\n");
			printEvent(event);
			if (event.detail == DND.DROP_DEFAULT) {
				event.detail = dropDefaultOperation;
			}
			event.feedback = dropFeedback;
		}
		@Override
		public void dragOver(DropTargetEvent event) {
			dropConsole.append(">>dragOver\n");
			printEvent(event);
			event.feedback = dropFeedback;
		}
		@Override
		public void drop(DropTargetEvent event) {
			dropConsole.append(">>drop\n");
			printEvent(event);
			String[] strings = null;
			if (TextTransfer.getInstance().isSupportedType(event.currentDataType) ||
				RTFTransfer.getInstance().isSupportedType(event.currentDataType) ||
				HTMLTransfer.getInstance().isSupportedType(event.currentDataType) ||
				URLTransfer.getInstance().isSupportedType(event.currentDataType)) {
				strings = new String[] {(String)event.data};
			}
			if (FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
				strings = (String[])event.data;
			}
			if (strings == null || strings.length == 0) {
				dropConsole.append("!!Invalid data dropped");
				return;
			}

			if (strings.length == 1 && (dropControlType == TABLE ||
										dropControlType == TREE ||
										dropControlType == LIST)) {
				// convert string separated by "\n" into an array of strings
				String string = strings[0];
				int count = 0;
				int offset = string.indexOf("\n", 0);
				while (offset > 0) {
					count++;
					offset = string.indexOf("\n", offset + 1);
				}
				if (count > 0) {
					strings = new String[count + 1];
					int start = 0;
					int end = string.indexOf("\n");
					int index = 0;
					while (start < end) {
						strings[index++] = string.substring(start, end);
						start = end + 1;
						end = string.indexOf("\n", start);
						if (end == -1) end = string.length();
					}
				}
			}
			switch(dropControlType) {
				case BUTTON_CHECK:
				case BUTTON_TOGGLE:
				case BUTTON_RADIO: {
					Button b = (Button)dropControl;
					b.setText(strings[0]);
					break;
				}
				case STYLED_TEXT: {
					StyledText text = (StyledText)dropControl;
					for (String string : strings) {
						text.insert(string);
					}
					break;
				}
				case TABLE: {
					Table table = (Table)dropControl;
					Point p = event.display.map(null, table, event.x, event.y);
					TableItem dropItem = table.getItem(p);
					int index = dropItem == null ? table.getItemCount() : table.indexOf(dropItem);
					for (String string : strings) {
						TableItem item = new TableItem(table, SWT.NONE, index);
						item.setText(0, string);
						item.setText(1, "dropped item");
					}
					TableColumn[] columns = table.getColumns();
					for (TableColumn column : columns) {
						column.pack();
					}
					break;
				}
				case TEXT: {
					Text text = (Text)dropControl;
					for (String string : strings) {
						text.append(string+"\n");
					}
					break;
				}
				case TREE: {
					Tree tree = (Tree)dropControl;
					Point p = event.display.map(null, tree, event.x, event.y);
					TreeItem parentItem = tree.getItem(p);
					for (String string : strings) {
						TreeItem item = parentItem != null ? new TreeItem(parentItem, SWT.NONE) : new TreeItem(tree, SWT.NONE);
						item.setText(string);
					}
					break;
				}
				case CANVAS: {
					dropControl.setData("STRINGS", strings);
					dropControl.redraw();
					break;
				}
				case LABEL: {
					Label label = (Label)dropControl;
					label.setText(strings[0]);
					break;
				}
				case LIST: {
					List list = (List)dropControl;
					for (String string : strings) {
						list.add(string);
					}
					break;
				}
				case COMBO: {
					Combo combo = (Combo)dropControl;
					combo.setText(strings[0]);
					break;
				}
				default:
					throw new SWTError(SWT.ERROR_NOT_IMPLEMENTED);
			}
		}
		@Override
		public void dropAccept(DropTargetEvent event) {
			dropConsole.append(">>dropAccept\n");
			printEvent(event);
		}
	});
}

private void createFeedbackTypes(Group parent) {
	parent.setLayout(new RowLayout(SWT.VERTICAL));
	Button b = new Button(parent, SWT.CHECK);
	b.setText("FEEDBACK_SELECT");
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button eb = (Button) e.widget;
		if (eb.getSelection()) {
			dropFeedback |= DND.FEEDBACK_SELECT;
		} else {
			dropFeedback &= ~DND.FEEDBACK_SELECT;
		}
	}));

	b = new Button(parent, SWT.CHECK);
	b.setText("FEEDBACK_SCROLL");
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button eb = (Button) e.widget;
		if (eb.getSelection()) {
			dropFeedback |= DND.FEEDBACK_SCROLL;
		} else {
			dropFeedback &= ~DND.FEEDBACK_SCROLL;
		}
	}));


	b = new Button(parent, SWT.CHECK);
	b.setText("FEEDBACK_INSERT_BEFORE");
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button eb = (Button) e.widget;
		if (eb.getSelection()) {
			dropFeedback |= DND.FEEDBACK_INSERT_BEFORE;
		} else {
			dropFeedback &= ~DND.FEEDBACK_INSERT_BEFORE;
		}
	}));

	b = new Button(parent, SWT.CHECK);
	b.setText("FEEDBACK_INSERT_AFTER");
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button eb = (Button) e.widget;
		if (eb.getSelection()) {
			dropFeedback |= DND.FEEDBACK_INSERT_AFTER;
		} else {
			dropFeedback &= ~DND.FEEDBACK_INSERT_AFTER;
		}
	}));

	b = new Button(parent, SWT.CHECK);
	b.setText("FEEDBACK_EXPAND");
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button eb = (Button) e.widget;
		if (eb.getSelection()) {
			dropFeedback |= DND.FEEDBACK_EXPAND;
		} else {
			dropFeedback &= ~DND.FEEDBACK_EXPAND;
		}
	}));
}

private void createDropTypes(Composite parent) {
	parent.setLayout(new RowLayout(SWT.VERTICAL));
	Button textButton = new Button(parent, SWT.CHECK);
	textButton.setText("Text Transfer");
	textButton.addSelectionListener(widgetSelectedAdapter(e -> {
		Button b = (Button) e.widget;
		if (b.getSelection()) {
			addDropTransfer(TextTransfer.getInstance());
		} else {
			removeDropTransfer(TextTransfer.getInstance());
		}
	}));

	Button b = new Button(parent, SWT.CHECK);
	b.setText("RTF Transfer");
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button eb = (Button) e.widget;
		if (eb.getSelection()) {
			addDropTransfer(RTFTransfer.getInstance());
		} else {
			removeDropTransfer(RTFTransfer.getInstance());
		}
	}));


	b = new Button(parent, SWT.CHECK);
	b.setText("HTML Transfer");
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button eb = (Button) e.widget;
		if (eb.getSelection()) {
			addDropTransfer(HTMLTransfer.getInstance());
		} else {
			removeDropTransfer(HTMLTransfer.getInstance());
		}
	}));

	b = new Button(parent, SWT.CHECK);
	b.setText("URL Transfer");
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button eb = (Button) e.widget;
		if (eb.getSelection()) {
			addDropTransfer(URLTransfer.getInstance());
		} else {
			removeDropTransfer(URLTransfer.getInstance());
		}
	}));

	b = new Button(parent, SWT.CHECK);
	b.setText("File Transfer");
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button eb = (Button) e.widget;
		if (eb.getSelection()) {
			addDropTransfer(FileTransfer.getInstance());
		} else {
			removeDropTransfer(FileTransfer.getInstance());
		}
	}));

	// initialize state
	textButton.setSelection(true);
	addDropTransfer(TextTransfer.getInstance());
}

private void createDropWidget(Composite parent) {
	parent.setLayout(new FormLayout());
	Combo combo = new Combo(parent, SWT.READ_ONLY);
	combo.setItems("Toggle Button", "Radio Button", "Checkbox", "Canvas", "Label", "List", "Table", "Tree", "Text", "StyledText", "Combo");
	combo.select(LABEL);
	dropControlType = combo.getSelectionIndex();
	dropControl = createWidget(dropControlType, parent, "Drop Target");
	combo.addSelectionListener(widgetSelectedAdapter(e -> {
		Object data = dropControl.getLayoutData();
		Composite dropParent = dropControl.getParent();
		dropControl.dispose();
		Combo c = (Combo) e.widget;
		dropControlType = c.getSelectionIndex();
		dropControl = createWidget(dropControlType, dropParent, "Drop Target");
		dropControl.setLayoutData(data);
		if (dropEnabled)
			createDropTarget();
		dropParent.layout();
	}));

	Button b = new Button(parent, SWT.CHECK);
	b.setText("DropTarget");
	b.addSelectionListener(widgetSelectedAdapter(e -> {
		Button eb = (Button) e.widget;
		dropEnabled = eb.getSelection();
		if (dropEnabled) {
			createDropTarget();
		} else {
			if (dropTarget != null) {
				dropTarget.dispose();
			}
			dropTarget = null;
		}
	}));
	// initialize state
	b.setSelection(true);
	dropEnabled = true;

	FormData data = new FormData();
	data.top = new FormAttachment(0, 10);
	data.bottom = new FormAttachment(combo, -10);
	data.left = new FormAttachment(0, 10);
	data.right = new FormAttachment(100, -10);
	dropControl.setLayoutData(data);

	data = new FormData();
	data.bottom = new FormAttachment(100, -10);
	data.left = new FormAttachment(0, 10);
	combo.setLayoutData(data);

	data = new FormData();
	data.bottom = new FormAttachment(100, -10);
	data.left = new FormAttachment(combo, 10);
	b.setLayoutData(data);
}

private Control createWidget(int type, Composite parent, String prefix){
	switch (type) {
		case BUTTON_CHECK: {
			Button button = new Button(parent, SWT.CHECK);
			button.setText(prefix+" Check box");
			return button;
		}
		case BUTTON_TOGGLE: {
			Button button = new Button(parent, SWT.TOGGLE);
			button.setText(prefix+" Toggle button");
			return button;
		}
		case BUTTON_RADIO: {
			Button button = new Button(parent, SWT.RADIO);
			button.setText(prefix+" Radio button");
			return button;
		}
		case STYLED_TEXT: {
			StyledText text = new StyledText(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
			text.setText(prefix+" Styled Text");
			return text;
		}
		case TABLE: {
			Table table = new Table(parent, SWT.BORDER | SWT.MULTI);
			table.setHeaderVisible(true);
			TableColumn column0 = new TableColumn(table, SWT.LEFT);
			column0.setText("Name");
			TableColumn column1 = new TableColumn(table, SWT.RIGHT);
			column1.setText("Value");
			TableColumn column2 = new TableColumn(table, SWT.CENTER);
			column2.setText("Description");
			for (int i = 0; i < 10; i++) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, prefix+" name "+i);
				item.setText(1, prefix+" value "+i);
				item.setText(2, prefix+" description "+i);
				item.setImage(itemImage);
			}
			column0.pack();
			column1.pack();
			column2.pack();
			return table;
		}
		case TEXT: {
			Text text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
			text.setText(prefix+" Text");
			return text;
		}
		case TREE: {
			Tree tree = new Tree(parent, SWT.BORDER | SWT.MULTI);
			tree.setHeaderVisible(true);
			TreeColumn column0 = new TreeColumn(tree, SWT.LEFT);
			column0.setText("Name");
			TreeColumn column1 = new TreeColumn(tree, SWT.RIGHT);
			column1.setText("Value");
			TreeColumn column2 = new TreeColumn(tree, SWT.CENTER);
			column2.setText("Description");
			for (int i = 0; i < 3; i++) {
				TreeItem item = new TreeItem(tree, SWT.NONE);
				item.setText(0, prefix+" name "+i);
				item.setText(1, prefix+" value "+i);
				item.setText(2, prefix+" description "+i);
				item.setImage(itemImage);
				for (int j = 0; j < 3; j++) {
					TreeItem subItem = new TreeItem(item, SWT.NONE);
					subItem.setText(0, prefix+" name "+i+" "+j);
					subItem.setText(1, prefix+" value "+i+" "+j);
					subItem.setText(2, prefix+" description "+i+" "+j);
					subItem.setImage(itemImage);
					for (int k = 0; k < 3; k++) {
						TreeItem subsubItem = new TreeItem(subItem, SWT.NONE);
						subsubItem.setText(0, prefix+" name "+i+" "+j+" "+k);
						subsubItem.setText(1, prefix+" value "+i+" "+j+" "+k);
						subsubItem.setText(2, prefix+" description "+i+" "+j+" "+k);
						subsubItem.setImage(itemImage);
					}
				}
			}
			column0.pack();
			column1.pack();
			column2.pack();
			return tree;
		}
		case CANVAS: {
			Canvas canvas = new Canvas(parent, SWT.BORDER);
			canvas.setData("STRINGS", new String[] {prefix+" Canvas widget"});
			canvas.addPaintListener(e -> {
				Canvas c = (Canvas)e.widget;
				Image image = (Image)c.getData("IMAGE");
				if (image != null) {
					e.gc.drawImage(image, 5, 5);
				} else {
					String[] strings = (String[])c.getData("STRINGS");
					if (strings != null) {
						FontMetrics metrics = e.gc.getFontMetrics();
						int height = metrics.getHeight();
						int y = 5;
						for (String string : strings) {
							e.gc.drawString(string, 5, y);
							y += height + 5;
						}
					}
				}
			});
			return canvas;
		}
		case LABEL: {
			Label label = new Label(parent, SWT.BORDER);
			label.setText(prefix+" Label");
			return label;
		}
		case LIST: {
			List list = new List(parent, SWT.BORDER|SWT.MULTI);
			list.setItems(prefix+" Item a", prefix+" Item b",  prefix+" Item c",  prefix+" Item d");
			return list;
		}
		case COMBO:{
			Combo combo = new Combo(parent, SWT.BORDER);
			combo.setItems("Item a", "Item b", "Item c", "Item d");
			return combo;
		}
		default:
			throw new SWTError(SWT.ERROR_NOT_IMPLEMENTED);
	}
}

public void open(Display display) {
	Shell shell = new Shell(display);
	shell.setText("Drag and Drop Example");
	shell.setLayout(new FillLayout());

	itemImage = new Image (display, DNDExample.class.getResourceAsStream("openFolder.gif"));

	ScrolledComposite sc = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL);
	Composite parent = new Composite(sc, SWT.NONE);
	sc.setContent(parent);
	parent.setLayout(new FormLayout());

	Label dragLabel = new Label(parent, SWT.LEFT);
	dragLabel.setText("Drag Source:");

	Group dragWidgetGroup = new Group(parent, SWT.NONE);
	dragWidgetGroup.setText("Widget");
	createDragWidget(dragWidgetGroup);

	Composite cLeft = new Composite(parent, SWT.NONE);
	cLeft.setLayout(new GridLayout(2, false));

	Group dragOperationsGroup = new Group(cLeft, SWT.NONE);
	dragOperationsGroup.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
	dragOperationsGroup.setText("Allowed Operation(s):");
	createDragOperations(dragOperationsGroup);

	Group dragTypesGroup = new Group(cLeft, SWT.NONE);
	dragTypesGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
	dragTypesGroup.setText("Transfer Type(s):");
	createDragTypes(dragTypesGroup);

	dragConsole = new Text(cLeft, SWT.READ_ONLY | SWT.BORDER |SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
	dragConsole.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	Menu menu = new Menu (shell, SWT.POP_UP);
	MenuItem item = new MenuItem (menu, SWT.PUSH);
	item.setText ("Clear");
	item.addSelectionListener(widgetSelectedAdapter(e -> dragConsole.setText("")));
	item = new MenuItem (menu, SWT.CHECK);
	item.setText ("Show Event detail");
	item.addSelectionListener(widgetSelectedAdapter(e -> {
		MenuItem eItem = (MenuItem) e.widget;
		dragEventDetail = eItem.getSelection();
	}));
	dragConsole.setMenu(menu);

	Label dropLabel = new Label(parent, SWT.LEFT);
	dropLabel.setText("Drop Target:");

	Group dropWidgetGroup = new Group(parent, SWT.NONE);
	dropWidgetGroup.setText("Widget");
	createDropWidget(dropWidgetGroup);

	Composite cRight = new Composite(parent, SWT.NONE);
	cRight.setLayout(new GridLayout(2, false));

	Group dropOperationsGroup = new Group(cRight, SWT.NONE);
	dropOperationsGroup.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 2));
	dropOperationsGroup.setText("Allowed Operation(s):");
	createDropOperations(dropOperationsGroup);

	Group dropTypesGroup = new Group(cRight, SWT.NONE);
	dropTypesGroup.setText("Transfer Type(s):");
	createDropTypes(dropTypesGroup);

	Group feedbackTypesGroup = new Group(cRight, SWT.NONE);
	feedbackTypesGroup.setText("Feedback Type(s):");
	createFeedbackTypes(feedbackTypesGroup);

	dropConsole = new Text(cRight, SWT.READ_ONLY | SWT.BORDER |SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
	dropConsole.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	menu = new Menu (shell, SWT.POP_UP);
	item = new MenuItem (menu, SWT.PUSH);
	item.setText ("Clear");
	item.addSelectionListener(widgetSelectedAdapter(e -> dropConsole.setText("")));
	item = new MenuItem (menu, SWT.CHECK);
	item.setText ("Show Event detail");
	item.addSelectionListener(widgetSelectedAdapter(e -> {
			MenuItem eItem = (MenuItem)e.widget;
			dropEventDetail = eItem.getSelection();
		}
	));
	dropConsole.setMenu(menu);

	if (dragEnabled) createDragSource();
	if (dropEnabled) createDropTarget();

	int height = 200;
	FormData data = new FormData();
	data.top = new FormAttachment(0, 10);
	data.left = new FormAttachment(0, 10);
	dragLabel.setLayoutData(data);

	data = new FormData();
	data.top = new FormAttachment(dragLabel, 10);
	data.left = new FormAttachment(0, 10);
	data.right = new FormAttachment(50, -10);
	data.height = height;
	dragWidgetGroup.setLayoutData(data);

	data = new FormData();
	data.top = new FormAttachment(dragWidgetGroup, 10);
	data.left = new FormAttachment(0, 10);
	data.right = new FormAttachment(50, -10);
	data.bottom = new FormAttachment(100, -10);
	cLeft.setLayoutData(data);

	data = new FormData();
	data.top = new FormAttachment(0, 10);
	data.left = new FormAttachment(cLeft, 10);
	dropLabel.setLayoutData(data);

	data = new FormData();
	data.top = new FormAttachment(dropLabel, 10);
	data.left = new FormAttachment(cLeft, 10);
	data.right = new FormAttachment(100, -10);
	data.height = height;
	dropWidgetGroup.setLayoutData(data);

	data = new FormData();
	data.top = new FormAttachment(dropWidgetGroup, 10);
	data.left = new FormAttachment(cLeft, 10);
	data.right = new FormAttachment(100, -10);
	data.bottom = new FormAttachment(100, -10);
	cRight.setLayoutData(data);

	sc.setMinSize(parent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	sc.setExpandHorizontal(true);
	sc.setExpandVertical(true);

	Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	Rectangle monitorArea = shell.getMonitor().getClientArea();
	shell.setSize(Math.min(size.x, monitorArea.width - 20), Math.min(size.y, monitorArea.height - 20));
	shell.open();

	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	itemImage.dispose();
}

private void printEvent(DragSourceEvent e) {
	if (!dragEventDetail) return;
	dragConsole.append(e.toString() + "\n");
}

private void printEvent(DropTargetEvent e) {
	if (!dropEventDetail) return;
	dropConsole.append(e.toString() + "\n");
}

private void removeDragTransfer(Transfer transfer){
	if (dragTypes.length == 1) {
		dragTypes = new Transfer[0];
	} else {
		int index = -1;
		for(int i = 0; i < dragTypes.length; i++) {
			if (dragTypes[i] == transfer) {
				index = i;
				break;
			}
		}
		if (index == -1) return;
		Transfer[] newTypes = new Transfer[dragTypes.length - 1];
		System.arraycopy(dragTypes, 0, newTypes, 0, index);
		System.arraycopy(dragTypes, index + 1, newTypes, index, dragTypes.length - index - 1);
		dragTypes = newTypes;
	}
	if (dragSource != null) {
		dragSource.setTransfer(dragTypes);
	}
}

private void removeDropTransfer(Transfer transfer){
	if (dropTypes.length == 1) {
		dropTypes = new Transfer[0];
	} else {
		int index = -1;
		for(int i = 0; i < dropTypes.length; i++) {
			if (dropTypes[i] == transfer) {
				index = i;
				break;
			}
		}
		if (index == -1) return;
		Transfer[] newTypes = new Transfer[dropTypes.length - 1];
		System.arraycopy(dropTypes, 0, newTypes, 0, index);
		System.arraycopy(dropTypes, index + 1, newTypes, index, dropTypes.length - index - 1);
		dropTypes = newTypes;
	}
	if (dropTarget != null) {
		dropTarget.setTransfer(dropTypes);
	}
}
}
