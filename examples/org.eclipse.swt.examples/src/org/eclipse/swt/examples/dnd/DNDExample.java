/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.dnd;

 
import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class DNDExample {
	
	private int dragOperation = 0;
	private Transfer[] dragTypes = new Transfer[0];
	private Control dragControl;
	private int dragControlType = 0;
	private DragSource dragSource;
	private Object dragData;
	private boolean dragEnabled = false;
	
	private int dropOperation = 0;
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
	
	private static final int BUTTON_TOGGLE = 0;
	private static final int BUTTON_RADIO = 1;
	private static final int BUTTON_CHECK = 2;
	private static final int CANVAS = 3;
	private static final int LABEL = 4;
	private static final int LIST = 5;
	private static final int TEXT = 6;
	private static final int TABLE = 7;
	private static final int TREE = 8;
	
public static void main(String[] args) {
	DNDExample example = new DNDExample();
	example.open();
}
private void open() {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setText("Drag and Drop Example");
	shell.setLayout(new FormLayout());
	
	Label dragLabel = new Label(shell, SWT.LEFT);
	dragLabel.setText("Drag Source:");
	
	Group dragWidgetGroup = new Group(shell, SWT.NONE);
	dragWidgetGroup.setText("Widget");
	createDragWidget(dragWidgetGroup);
	
	Group dragOperationsGroup = new Group(shell, SWT.NONE);
	dragOperationsGroup.setText("Allowed Operation(s):");
	createDragOperations(dragOperationsGroup);
	
	Group dragTypesGroup = new Group(shell, SWT.NONE);
	dragTypesGroup.setText("Transfer Type(s):");
	createDragTypes(dragTypesGroup);
	
	Label dropLabel = new Label(shell, SWT.LEFT);
	dropLabel.setText("Drop Target:");
	
	Group dropWidgetGroup = new Group(shell, SWT.NONE);
	dropWidgetGroup.setText("Widget");
	createDropWidget(dropWidgetGroup);
	
	Group dropOperationsGroup = new Group(shell, SWT.NONE);
	dropOperationsGroup.setText("Allowed Operation(s):");
	createDropOperations(dropOperationsGroup);
	
	Group dropTypesGroup = new Group(shell, SWT.NONE);
	dropTypesGroup.setText("Transfer Type(s):");
	createDropTypes(dropTypesGroup);
	
	dragConsole = new Text(shell, SWT.READ_ONLY | SWT.BORDER |SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
	Menu menu = new Menu (shell, SWT.POP_UP);
	MenuItem item = new MenuItem (menu, SWT.PUSH);
	item.setText ("Clear");
	item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			dragConsole.setText("");
		}
	});
	item = new MenuItem (menu, SWT.CHECK);
	item.setText ("Show Event detail");
	item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			MenuItem item = (MenuItem)e.widget;
			dragEventDetail = item.getSelection();
		}
	});
	dragConsole.setMenu(menu);
	
	dropConsole = new Text(shell, SWT.READ_ONLY | SWT.BORDER |SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
	menu = new Menu (shell, SWT.POP_UP);
	item = new MenuItem (menu, SWT.PUSH);
	item.setText ("Clear");
	item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			dropConsole.setText("");
		}
	});
	item = new MenuItem (menu, SWT.CHECK);
	item.setText ("Show Event detail");
	item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			MenuItem item = (MenuItem)e.widget;
			dropEventDetail = item.getSelection();
		}
	});
	dropConsole.setMenu(menu);
	
	int height = 300;
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
	data.bottom = new FormAttachment(dropTypesGroup, -10);
	dragOperationsGroup.setLayoutData(data);
	
	data = new FormData();
	data.top = new FormAttachment(dragOperationsGroup, 10);
	data.left = new FormAttachment(0, 10);
	data.right = new FormAttachment(50, -10);
	dragTypesGroup.setLayoutData(data);
	
	data = new FormData();
	data.top = new FormAttachment(0, 10);
	data.left = new FormAttachment(dragTypesGroup, 10);
	dropLabel.setLayoutData(data);
	
	data = new FormData();
	data.top = new FormAttachment(dropLabel, 10);
	data.left = new FormAttachment(dragTypesGroup, 10);
	data.right = new FormAttachment(100, -10);
	data.height = height;
	dropWidgetGroup.setLayoutData(data);
	
	data = new FormData();
	data.top = new FormAttachment(dropWidgetGroup, 10);
	data.left = new FormAttachment(dragTypesGroup, 10);
	data.right = new FormAttachment(100, -10);
	dropOperationsGroup.setLayoutData(data);
	
	data = new FormData();
	data.top = new FormAttachment(dropOperationsGroup, 10);
	data.left = new FormAttachment(dragTypesGroup, 10);
	data.right = new FormAttachment(100, -10);
	dropTypesGroup.setLayoutData(data);
	
	data = new FormData();
	data.top = new FormAttachment(dropTypesGroup, 10);
	data.bottom = new FormAttachment(100, -10);
	data.left = new FormAttachment(0, 10);
	data.right = new FormAttachment(50, -10);
	dragConsole.setLayoutData(data);

	data = new FormData();
	data.top = new FormAttachment(dropTypesGroup, 10);
	data.bottom = new FormAttachment(100, -10);
	data.left = new FormAttachment(50, 10);
	data.right = new FormAttachment(100, -10);
	dropConsole.setLayoutData(data);
		
	shell.setSize(1000, 900);
	shell.open();
		
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}	
}

private void createDragWidget(Composite parent) {
	parent.setLayout(new FormLayout());
	Combo combo = new Combo(parent, SWT.READ_ONLY);
	combo.setItems(new String[] {"Toggle Button", "Radio Button", "Checkbox", "Canvas", "Label", "List", "Text", "Table", "Tree"});
	combo.select(LABEL);
	dragControlType = combo.getSelectionIndex();
	dragControl = createWidget(dragControlType, parent);
	
	combo.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Object data = dragControl.getLayoutData();
			Composite parent = dragControl.getParent();
			dragControl.dispose();
			Combo c = (Combo)e.widget; 
			dragControlType = c.getSelectionIndex();
			dragControl = createWidget(dragControlType, parent);
			dragControl.setLayoutData(data);
			if (dragEnabled) createDragSource();
			parent.layout();
		}
	});
	
	Button b = new Button(parent, SWT.CHECK);
	b.setText("DragSource");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			dragEnabled = b.getSelection();
			if (dragEnabled) {
				createDragSource();
			} else {
				if (dragSource != null){
					dragSource.dispose();
				}
				dragSource = null;
			}
		}
	});
	
	FormData data = new FormData();
	data.top = new FormAttachment(0, 10);
	data.bottom = new FormAttachment(combo, -10);
	data.left = new FormAttachment(0, 10);
	data.right = new FormAttachment(100, -10);
	dragControl.setLayoutData(data);
	
	data = new FormData();
	data.bottom = new FormAttachment(b, -10);
	data.left = new FormAttachment(0, 10);
	combo.setLayoutData(data);
	
	data = new FormData();
	data.bottom = new FormAttachment(100, -10);
	data.left = new FormAttachment(0, 10);
	b.setLayoutData(data);
}

private void createDropWidget(Composite parent) {
	parent.setLayout(new FormLayout());
	Combo combo = new Combo(parent, SWT.READ_ONLY);
	combo.setItems(new String[] {"Toggle Button", "Radio Button", "Checkbox", "Canvas", "Label", "List", "Text", "Table", "Tree"});
	combo.select(LABEL);
	dropControlType = combo.getSelectionIndex();
	dropControl = createWidget(dropControlType, parent);
	combo.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Object data = dropControl.getLayoutData();
			Composite parent = dropControl.getParent();
			dropControl.dispose();
			Combo c = (Combo)e.widget;
			dropControlType = c.getSelectionIndex(); 
			dropControl = createWidget(dropControlType, parent);
			dropControl.setLayoutData(data);
			if (dropEnabled) createDropTarget();
			parent.layout();
		}
	});
	
	Button b = new Button(parent, SWT.CHECK);
	b.setText("DropTarget");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			dropEnabled = b.getSelection();
			if (dropEnabled) {
				createDropTarget();
			} else {
				if (dropTarget != null){
					dropTarget.dispose();
				}
				dropTarget = null;
			}
		}
	});
	
	FormData data = new FormData();
	data.top = new FormAttachment(0, 10);
	data.bottom = new FormAttachment(combo, -10);
	data.left = new FormAttachment(0, 10);
	data.right = new FormAttachment(100, -10);
	dropControl.setLayoutData(data);
	
	data = new FormData();
	data.bottom = new FormAttachment(b, -10);
	data.left = new FormAttachment(0, 10);
	combo.setLayoutData(data);
	
	data = new FormData();
	data.bottom = new FormAttachment(100, -10);
	data.left = new FormAttachment(0, 10);
	b.setLayoutData(data);	
}

private void createDragOperations(Composite parent) {
	parent.setLayout(new RowLayout(SWT.VERTICAL));
	final Button moveButton = new Button(parent, SWT.CHECK);
	moveButton.setText("DND.DROP_MOVE");
	moveButton.setSelection(true);
	dragOperation = DND.DROP_MOVE;
	moveButton.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			if (b.getSelection()) {
				dragOperation |= DND.DROP_MOVE;			
			} else {
				dragOperation = dragOperation &~DND.DROP_MOVE;
				if (dragOperation == 0) {
					dragOperation = DND.DROP_MOVE;
					moveButton.setSelection(true);
				}
			}
			if (dragEnabled) {
				createDragSource();
			}
		}
	});
	

	Button b = new Button(parent, SWT.CHECK);
	b.setText("DND.DROP_COPY");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			if (b.getSelection()) {
				dragOperation |= DND.DROP_COPY;			
			} else {
				dragOperation = dragOperation &~DND.DROP_COPY;
				if (dragOperation == 0) {
					dragOperation = DND.DROP_MOVE;
					moveButton.setSelection(true);
				}
			}
			if (dragEnabled) {
				createDragSource();
			}
		}
	});

	b = new Button(parent, SWT.CHECK);
	b.setText("DND.DROP_LINK");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			if (b.getSelection()) {
				dragOperation |= DND.DROP_LINK;			
			} else {
				dragOperation = dragOperation &~DND.DROP_LINK;
				if (dragOperation == 0) {
					dragOperation = DND.DROP_MOVE;
					moveButton.setSelection(true);
				}
			}
			if (dragEnabled) {
				createDragSource();
			}
		}
	});
}

private void createDragTypes(Composite parent) {
	parent.setLayout(new RowLayout(SWT.VERTICAL));
	Button b = new Button(parent, SWT.CHECK);
	b.setText("Text Transfer");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			if (b.getSelection()) {
				addDragTransfer(TextTransfer.getInstance());			
			} else {
				removeDragTransfer(TextTransfer.getInstance());
			}
		}
	});
	
	b = new Button(parent, SWT.CHECK);
	b.setText("RTF Transfer");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			if (b.getSelection()) {
				addDragTransfer(RTFTransfer.getInstance());			
			} else {
				removeDragTransfer(RTFTransfer.getInstance());
			}
		}
	});
	
	b = new Button(parent, SWT.CHECK);
	b.setText("File Transfer");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			if (b.getSelection()) {
				addDragTransfer(FileTransfer.getInstance());			
			} else {
				removeDragTransfer(FileTransfer.getInstance());
			}
		}
	});
}

private void createDropOperations(Composite parent) {
	parent.setLayout(new RowLayout(SWT.VERTICAL));
	final Button moveButton = new Button(parent, SWT.CHECK);
	moveButton.setText("DND.DROP_MOVE");
	moveButton.setSelection(true);
	dropOperation = DND.DROP_MOVE;
	moveButton.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			if (b.getSelection()) {
				dropOperation |= DND.DROP_MOVE;			
			} else {
				dropOperation = dropOperation & ~DND.DROP_MOVE;
				if (dropOperation == 0) {
					dropOperation = DND.DROP_MOVE;
					moveButton.setSelection(true);
				}
			}
			if (dropEnabled) {
				createDropTarget();
			}
		}
	});
	

	Button b = new Button(parent, SWT.CHECK);
	b.setText("DND.DROP_COPY");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			if (b.getSelection()) {
				dropOperation |= DND.DROP_COPY;			
			} else {
				dropOperation = dropOperation & ~DND.DROP_COPY;
				if (dropOperation == 0) {
					dropOperation = DND.DROP_MOVE;
					moveButton.setSelection(true);
				}
			}
			if (dropEnabled) {
				createDropTarget();
			}
		}
	});

	b = new Button(parent, SWT.CHECK);
	b.setText("DND.DROP_LINK");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			if (b.getSelection()) {
				dropOperation |= DND.DROP_LINK;			
			} else {
				dropOperation = dropOperation & ~DND.DROP_LINK;
				if (dropOperation == 0) {
					dropOperation = DND.DROP_MOVE;
					moveButton.setSelection(true);
				}
			}
			if (dropEnabled) {
				createDropTarget();
			}
		}
	});
	
	b = new Button(parent, SWT.CHECK);
	b.setText("DND.DROP_DEFAULT");
	defaultParent = new Composite(parent, SWT.NONE);
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			if (b.getSelection()) {
				dropOperation |= DND.DROP_DEFAULT;
				defaultParent.setVisible(true);			
			} else {
				dropOperation = dropOperation & ~DND.DROP_DEFAULT;
				defaultParent.setVisible(false);
			}
			if (dropEnabled) {
				createDropTarget();
			}
		}
	});
	
	defaultParent.setVisible(false);
	GridLayout layout = new GridLayout();
	layout.marginWidth = 20;
	defaultParent.setLayout(layout);
	Label label = new Label(defaultParent, SWT.NONE);
	label.setText("Value for default operation is:");
	b = new Button(defaultParent, SWT.RADIO);
	b.setText("DND.DROP_MOVE");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			if (b.getSelection()) {
				dropDefaultOperation = DND.DROP_MOVE;
			}
		}
	});
	
	b = new Button(defaultParent, SWT.RADIO);
	b.setText("DND.DROP_COPY");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			if (b.getSelection()) {
				dropDefaultOperation = DND.DROP_COPY;
			}
		}
	});

	b = new Button(defaultParent, SWT.RADIO);
	b.setText("DND.DROP_LINK");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			if (b.getSelection()) {
				dropDefaultOperation = DND.DROP_LINK;
			}
		}
	});
	
	b = new Button(defaultParent, SWT.RADIO);
	b.setText("DND.DROP_NONE");
	b.setSelection(true);
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			if (b.getSelection()) {
				dropDefaultOperation = DND.DROP_NONE;
			}
		}
	});
}
private void createDropTypes(Composite parent) {
	parent.setLayout(new RowLayout(SWT.VERTICAL));
	Button b = new Button(parent, SWT.CHECK);
	b.setText("Text Transfer");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			if (b.getSelection()) {
				addDropTransfer(TextTransfer.getInstance());			
			} else {
				removeDropTransfer(TextTransfer.getInstance());
			}
		}
	});
	
	b = new Button(parent, SWT.CHECK);
	b.setText("RTF Transfer");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			if (b.getSelection()) {
				addDropTransfer(RTFTransfer.getInstance());			
			} else {
				removeDropTransfer(RTFTransfer.getInstance());
			}
		}
	});
	
	b = new Button(parent, SWT.CHECK);
	b.setText("File Transfer");
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button)e.widget;
			if (b.getSelection()) {
				addDropTransfer(FileTransfer.getInstance());			
			} else {
				removeDropTransfer(FileTransfer.getInstance());
			}
		}
	});
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

private void addDragTransfer(Transfer transfer){
	Transfer[] newTypes = new Transfer[dragTypes.length + 1];
	System.arraycopy(dragTypes, 0, newTypes, 0, dragTypes.length);
	newTypes[dragTypes.length] = transfer;
	dragTypes = newTypes;
	if (dragSource != null) {
		dragSource.setTransfer(dragTypes);
	}
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

private Control createWidget(int type, Composite parent){
	switch (type) {
		case BUTTON_CHECK: {
			Button button = new Button(parent, SWT.CHECK);
			button.setText("Check box");
			return button;
		}
		case BUTTON_TOGGLE: {
			Button button = new Button(parent, SWT.TOGGLE);
			button.setText("Toggle button");
			return button;
		}
		case BUTTON_RADIO: {
			Button button = new Button(parent, SWT.RADIO);
			button.setText("Radio button");
			return button;
		}
		case TABLE: {
			Table table = new Table(parent, SWT.BORDER | SWT.MULTI);
			TableColumn column1 = new TableColumn(table, SWT.NONE);
			TableColumn column2 = new TableColumn(table, SWT.NONE);
			for (int i = 0; i < 10; i++) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, "name " + i);
				item.setText(1, "value " + i);
			}
			column1.pack();
			column2.pack();
			return table;
		}
		case TEXT: {
			Text text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
			return text;
		}
		case TREE: {
			Tree tree = new Tree(parent, SWT.BORDER);
			for (int i = 0; i < 3; i++) {
				TreeItem item = new TreeItem(tree, SWT.NONE);
				item.setText("item " + i);
				for (int j = 0; j < 3; j++) {
					TreeItem subItem = new TreeItem(item, SWT.NONE);
					subItem.setText("item " + j);
					for (int k = 0; k < 3; k++) {
						TreeItem subsubItem = new TreeItem(subItem, SWT.NONE);
						subsubItem.setText("item " + k);
					}
				}
			}
			return tree;
		}
		case CANVAS: {
			Canvas canvas = new Canvas(parent, SWT.BORDER);
			canvas.setData("STRINGS", new String[] {"Canvas widget"});
			canvas.addPaintListener(new PaintListener() {
				public void paintControl(PaintEvent e) {
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
							for(int i = 0; i < strings.length; i++) {
								e.gc.drawString(strings[i], 5, y);
								y += height + 5;
							}
						}
					}
				}
			});
			return canvas;
		}
		case LABEL: {
			Label label = new Label(parent, SWT.BORDER);
			label.setText("Label");
			return label;
		}
		case LIST: {
			List list = new List(parent, SWT.BORDER);
			list.setItems(new String[] {"Item a", "Item b", "Item c", "Item d"});
			return list;
		}
		default:
			throw new SWTError(SWT.ERROR_NOT_IMPLEMENTED);
	}
}

private void createDropTarget() {
	if (dropTarget != null) dropTarget.dispose();
	dropTarget = new DropTarget(dropControl, dropOperation);
	dropTarget.setTransfer(dropTypes);
	dropTarget.addDropListener(new DropTargetListener() {
		public void dragEnter(DropTargetEvent event) {
			dropConsole.append(">>dragEnter\n");
			printEvent(event);
			if (event.detail == DND.DROP_DEFAULT) {
				event.detail = dropDefaultOperation;
			}
		}
		public void dragLeave(DropTargetEvent event) {
			dropConsole.append(">>dragLeave\n");
			printEvent(event);
		}
		public void dragOperationChanged(DropTargetEvent event) {
			dropConsole.append(">>dragOperationChanged\n");
			printEvent(event);
			if (event.detail == DND.DROP_DEFAULT) {
				event.detail = dropDefaultOperation;
			}
		}
		public void dragOver(DropTargetEvent event) {
			dropConsole.append(">>dragOver\n");
			printEvent(event);
		}
		public void drop(DropTargetEvent event) {
			dropConsole.append(">>drop\n");
			printEvent(event);
			String[] strings = null;
			if (TextTransfer.getInstance().isSupportedType(event.currentDataType)) {
				strings = new String[]{(String)event.data};
			}
			if (RTFTransfer.getInstance().isSupportedType(event.currentDataType)) {
				strings = new String[]{(String)event.data};
			}
			if (FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
				strings = (String[])event.data;
			}
			if (strings == null || strings.length == 0) {
				dropConsole.append("!!Invalid data dropped");
				return;
			}
			switch(dropControlType) {
				case BUTTON_CHECK:
				case BUTTON_TOGGLE:
				case BUTTON_RADIO: {
					Button b = (Button)dropControl;
					b.setText(strings[0]);
					break;
				}
				case TABLE: {
					Table table = (Table)dropControl;
					for(int i = 0; i < strings.length; i++) {
						TableItem item = new TableItem(table, SWT.NONE);
						item.setText(0, strings[i]);
						item.setText(1, "dropped item");
					}
					TableColumn[] columns = table.getColumns();
					for (int i = 0; i < columns.length; i++) {
						columns[i].pack();
					}
					break;
				}
				case TEXT: {
					Text text = (Text)dropControl;
					for(int i = 0; i < strings.length; i++) {
						text.append(strings[i]+"\n");
					}
					break;
				}
				case TREE: {
					Tree tree = (Tree)dropControl;
					for(int i = 0; i < strings.length; i++) {
						TreeItem item = new TreeItem(tree, SWT.NONE);
						item.setText(strings[i]);
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
					for(int i = 0; i < strings.length; i++) {
						list.add(strings[i]);
					}
					break;
				}
				default:
					throw new SWTError(SWT.ERROR_NOT_IMPLEMENTED);
			}
		}
		public void dropAccept(DropTargetEvent event) {
			dropConsole.append(">>dropAccept\n");
			printEvent(event);
		}
	});
}

private void createDragSource() {
	if (dragSource != null) dragSource.dispose();
	dragSource = new DragSource(dragControl, dragOperation);
	dragSource.setTransfer(dragTypes);
	dragSource.addDragListener(new DragSourceListener() {
		public void dragFinished(org.eclipse.swt.dnd.DragSourceEvent event) {
			dragConsole.append(">>dragFinished\n");
			printEvent(event);
			dragData = null;
			if (event.detail == DND.DROP_MOVE) {
				switch(dragControlType) {
					case BUTTON_CHECK:
					case BUTTON_TOGGLE:
					case BUTTON_RADIO: {
						Button b = (Button)dragControl;
						b.setText("");
						break;
					}
					case TABLE: {
						Table table = (Table)dragControl;
						TableItem[] items = table.getSelection();
						for (int i = 0; i < items.length; i++) {
							items[i].dispose();
						}
						break;
					}
					case TEXT: {
						Text text = (Text)dragControl;
						text.clearSelection();
						break;
					}
					case TREE: {
						Tree tree = (Tree)dragControl;
						TreeItem[] items = tree.getSelection();
						for (int i = 0; i < items.length; i++) {
							items[i].dispose();
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
				}
			}
		}
		public void dragSetData(org.eclipse.swt.dnd.DragSourceEvent event) {
			dragConsole.append(">>dragSetData\n");
			printEvent(event);
			if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
				if (dragData instanceof String) {
					event.data = dragData; 
				}else if (dragData instanceof String[]) {
					String[] strings = (String[])dragData;
					StringBuffer buffer = new StringBuffer();
					for (int i = 0; i < strings.length; i++) {
						buffer.append(strings[i]);
						buffer.append("\n");
					}
					event.data = buffer.toString();
				} else {
					event.data = "a string for transfer";
				}		
			}
			if (RTFTransfer.getInstance().isSupportedType(event.dataType)) {
				event.data = "{\\rtf1{\\colortbl;\\red255\\green0\\blue0;}\\uc1\\b\\i Hello World}";
			}
			if (FileTransfer.getInstance().isSupportedType(event.dataType)) {
				String[] files = new String[] {"a.txt", "b.txt", "c.txt"};
				event.data = files;
			}
			
		}
		public void dragStart(org.eclipse.swt.dnd.DragSourceEvent event) {
			dragConsole.append(">>dragStart\n");
			printEvent(event);
			switch(dragControlType) {
				case BUTTON_CHECK:
				case BUTTON_TOGGLE:
				case BUTTON_RADIO: {
					Button b = (Button)dragControl;
					dragData = b.getSelection() ? "true" : "false";
					break;
				}
				case TABLE: {
					Table table = (Table)dragControl;
					TableItem[] items = table.getSelection();
					if (items.length == 0) {
						event.doit = false;
					} else {
						String[] strings = new String[items.length];
						for (int i = 0; i < items.length; i++) {
							strings[i] = items[i].getText();
						}
						dragData = strings;
					}
					break;
				}
				case TEXT: {
					Text text = (Text)dragControl;
					String s = text.getSelectionText();
					if (s.length() == 0) {
						event.doit = false;
					} else {
						dragData = s;
					}
					break;
				}
				case TREE: {
					Tree tree = (Tree)dragControl;
					TreeItem[] items = tree.getSelection();
					if (items.length == 0) {
						event.doit = false;
					} else {
						String[] strings = new String[items.length];
						for (int i = 0; i < items.length; i++) {
							strings[i] = items[i].getText();
						}
						dragData = strings;
					}
					break;
				}
				case CANVAS: {
					String[] strings = (String[])dragControl.getData("STRINGS");
					if (strings == null || strings.length == 0) {
						event.doit = false;
					} else {
						dragData = strings;
					}
					break;
				}
				case LABEL: {
					Label label = (Label)dragControl;
					String string = label.getText();
					if (string.length() == 0) {
						event.doit = false;
					} else {
						dragData = string;
					}
					break;
				}
				case LIST: {
					List list = (List)dragControl;
					String[] selection = list.getSelection();
					if (selection.length == 0) {
						event.doit = false;
					} else {
						dragData = selection;
					}
					break;
				}
				default:
					throw new SWTError(SWT.ERROR_NOT_IMPLEMENTED);
			}
		}
	});
}
private void printEvent(DragSourceEvent e) {
	if (!dragEventDetail) return;
	StringBuffer sb = new StringBuffer();
	sb.append("widget: "); sb.append(e.widget);
	sb.append(", time: "); sb.append(e.time);
	sb.append(", operation: "); sb.append(e.detail);
	sb.append(", type: "); sb.append(e.dataType != null ? e.dataType.type : 0);
	sb.append(", doit: "); sb.append(e.doit);
	sb.append(", data: "); sb.append(e.data);
	sb.append("\n");
	dragConsole.append(sb.toString());
} 
private void printEvent(DropTargetEvent e) {
	if (!dropEventDetail) return;
	StringBuffer sb = new StringBuffer();
	sb.append("widget; "); sb.append(e.widget);
	sb.append(", time: "); sb.append(e.time);
	sb.append(", x: "); sb.append(e.x);
	sb.append(", y: "); sb.append(e.y);
	sb.append(", item: "); sb.append(e.item);
	sb.append(", operations: "); sb.append(e.operations);
	sb.append(", operation: "); sb.append(e.detail);
	sb.append(", feedback: "); sb.append(e.feedback);
	if (e.dataTypes != null) {
		for (int i = 0; i < e.dataTypes.length; i++) {
			sb.append(", dataType "); sb.append(i); sb.append(": "); sb.append(e.dataTypes[i].type);
		}
	} else {
		sb.append(", dataTypes: none");
	}
	sb.append(", currentDataType: "); sb.append(e.currentDataType);
	sb.append(", data: "); sb.append(e.data);
	sb.append("\n");
	dropConsole.append(sb.toString());
} 
}
