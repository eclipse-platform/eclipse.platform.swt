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
	
	Display display;

	DragSource dragSource = null;
	DropTarget dropTarget = null;
	
	Control dragSourceCtrl = null;
	Control dropTargetCtrl = null;

	Image sourceImage = null;
	Image targetImage = null;

	// Controls
	Button dragDetectedHooked;
	Button dropTargetRegistered;
	
	Button[] targetTypeString = new Button[2];
	Button[] targetTypePixmap = new Button[2];
	Button[] targetTypeOther = new Button[2];
	
	Button[] opsMove = new Button[2];
	Button[] opsCopy = new Button[2];
	Button[] opsLink = new Button[2];

	Combo[] widgetCombo = new Combo[2];

	Text msgText;

	SelectionListener sl = null;
	
	static final int BUTTON_PUSH = 0;
	static final int CANVAS = 1;
	static final int BUTTON_CHECK = 2;
	static final int LABEL = 3;
	static final int LIST = 4;
	static final int BUTTON_RADIO = 5;
	static final int TEXT = 6;
	
private void clearDragSourceData() {
	switch(widgetCombo[0].getSelectionIndex()) {
		case BUTTON_CHECK:
		case BUTTON_PUSH:
		case BUTTON_RADIO:				
			((Button)dragSourceCtrl).setText("");
			break;
		case CANVAS:
			if (sourceImage != null)	sourceImage.dispose();
			sourceImage = null;
			dragSourceCtrl.redraw();	
			break;
		case LABEL:		
			((Label)dragSourceCtrl).setText("");
			break;
		case LIST:	
			if (((List)dragSourceCtrl).getSelectionCount() == 0) break;	
			((List)dragSourceCtrl).remove(((List)dragSourceCtrl).getSelectionIndex());
			break;
		case TEXT:		
			((Text)dragSourceCtrl).setText("");
			break;
	}
}

private void createDragDetectHookedGroup(Composite parent) {
	Group group = new Group(parent, 0);
	group.setLayoutData(createGridData());
	group.setLayout(createGridLayout(1));

	// Create radio button
	dragDetectedHooked = new Button(group, SWT.CHECK);
	dragDetectedHooked.setLayoutData(createGridData());	
	dragDetectedHooked.setText("DragDetect hooked");
	dragDetectedHooked.addSelectionListener(sl);
}
private void createDragImage() {
	//if (sourceImage != null) sourceImage.dispose();
	//sourceImage = new Image(shell.getDisplay(), "c:\\x.bmp");
	
	sourceImage = new Image(display, 100, 100);
	GC gc = new GC(sourceImage);
	Color oldCol = gc.getForeground();
	
	for (int x = 0; x < 100; x+=5) {
		for (int y = 0; y < 100; y+=5) {
			Color color = new Color(display, Math.round(x * 2.5f), Math.round(y * 2.5f), 128);
			gc.setForeground(color);
			
			gc.drawLine(x, y, x + 5, y);
			gc.drawLine(x, y+1, x + 5, y+1);
			gc.drawLine(x, y+2, x + 5, y+2);
			gc.drawLine(x, y+3, x + 5, y+3);
			gc.drawLine(x, y+4, x + 5, y+4);
			
			gc.setForeground(oldCol);
			color.dispose();
		}
	}

	gc.drawString("CANVAS", 0, 0, true);
	
	gc.dispose();
}
private void createDragSide(Composite parent) {
	Composite c = new Composite(parent, 0);
	c.setLayoutData(createGridData());
	c.setLayout(new GridLayout(1, false));	
	
	Label label = new Label(c, SWT.LEFT);
	label.setText("Drag from:");
	GridData data = createGridData();
	data.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
	data.grabExcessVerticalSpace = false;	
	label.setLayoutData(data);
	
	Composite composite = new Composite(c, 0);
	composite.setLayoutData(createGridData());
	composite.setLayout(createGridLayout(2));	

	createWidgetSelectionGroup(composite, 0);
	createDragSourceCtrl(composite, 0);
	createDragDetectHookedGroup(composite);
	createTargetTypesGroup(composite, 0);
	createOperationsGroup(composite, 0);
}
private void createDragSource() {
	DragSource myDragSource = dragSource;
	dragSource = null;
	if (myDragSource != null) myDragSource.dispose();
		
	if (!dragDetectedHooked.getSelection()) return;

	Transfer[] types = null;
	if (targetTypeString[0].getSelection() && targetTypePixmap[0].getSelection()) {
		outputMsg("Source types: TextTransfer, ImageTransfer");
		types = new Transfer[] {TextTransfer.getInstance(), ImageTransfer.getInstance()};
	}
	else if (targetTypePixmap[0].getSelection()) {
		outputMsg("Source types: ImageTransfer");
		types = new Transfer[] {ImageTransfer.getInstance()};
	}
	else { //if (targetTypeString[0].getSelection()) {
		outputMsg("Source types: TextTransfer");
		targetTypeString[0].setSelection(true);
		types = new Transfer[] {TextTransfer.getInstance()};
	}
	
	int operations = 0;
	if (opsMove[0].getSelection()) operations |= DND.DROP_MOVE;
	if (opsCopy[0].getSelection()) operations |= DND.DROP_COPY;
	if (opsLink[0].getSelection()) operations |= DND.DROP_LINK;

	outputMsg("Source ops: " + operations);
	
	dragSource = new DragSource (dragSourceCtrl, operations);
	dragSource.setTransfer(types);
	dragSource.addDragListener(new DragSourceListener () {
		public void dragStart(DragSourceEvent e) {
			outputMsg("dragStart");
		}
		public void dragSetData(DragSourceEvent e) {
			outputMsg("dragSetData");
			e.data = getDragSourceData(e.dataType);
		}
		public void dragFinished(DragSourceEvent e) {
			outputMsg("dragFinished");
			if (e.detail == DND.DROP_MOVE)
				clearDragSourceData();
		}
	});	
}
private void createDragSourceCtrl(Composite parent, int nr) {
	GridData data = createGridData();
	data.verticalSpan = 4;
	
	switch (nr) {
		case BUTTON_PUSH: {
			Button button = new Button(parent, 0);
			button.setLayoutData(data);	
			button.setText("DragSource");
			dragSourceCtrl = button;
			break;
		}
		case CANVAS: {
			createDragImage();

			final Canvas canvas = new Canvas(parent, SWT.BORDER);
			canvas.setLayoutData(data);
			canvas.addPaintListener(new PaintListener() {
				public void paintControl(PaintEvent e) {
					if (sourceImage != null)
						e.gc.drawImage(sourceImage, 0, 0);
				}
			});
			
			dragSourceCtrl = canvas;
			break;
		}
		case BUTTON_CHECK: {
			Button button = new Button(parent, SWT.CHECK);
			button.setLayoutData(data);	
			button.setText("DragSource");
			dragSourceCtrl = button;
			break;
		}
		case LABEL: {
			Label label = new Label(parent, 0);
			label.setLayoutData(data);	
			label.setText("DragSource");
			dragSourceCtrl = label;
			break;
		}
		case LIST: {
			List list = new List(parent, SWT.BORDER);
			list.setLayoutData(data);	
			list.add("DragSource");
			list.add("Item A");
			list.add("Item B");
			list.add("Item C");			
			dragSourceCtrl = list;
			break;
		}
		case BUTTON_RADIO: {
			Button button = new Button(parent, SWT.RADIO);
			button.setLayoutData(data);	
			button.setText("DragSource");
			dragSourceCtrl = button;
			break;
		}
		case TEXT: {
			Text text = new Text(parent, SWT.BORDER);
			text.setLayoutData(data);	
			text.setText("DragSource");
			dragSourceCtrl = text;
			break;
		}
		
		
	}	
}
	
private void createDropImage(String s) {
	if (targetImage != null) targetImage.dispose();
	targetImage = new Image(display, 100, 100);
	GC gc = new GC(targetImage);
	gc.drawString(s, 0, 0);	
	gc.dispose();
}
private void createDropSide(Composite parent) {
	Composite c = new Composite(parent, 0);
	c.setLayoutData(createGridData());
	c.setLayout(createGridLayout(1));	
	
	Label label = new Label(c, SWT.LEFT);
	label.setText("Drop onto:");
	GridData data = createGridData();
	data.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
	data.grabExcessVerticalSpace = false;	
	label.setLayoutData(data);
	
	Composite composite = new Composite(c, 0);
	composite.setLayoutData(createGridData());
	composite.setLayout(createGridLayout(2));	

	createDropTargetCtrl(composite, 0);
	createWidgetSelectionGroup(composite, 1);
	createDropTargetRegisteredGroup(composite);
	createTargetTypesGroup(composite, 1);
	createOperationsGroup(composite, 1);
}
private void createDropTarget() {
	DropTarget myDropTarget = dropTarget;
	dropTarget = null;
	if (myDropTarget != null) myDropTarget.dispose();
	
	if (!dropTargetRegistered.getSelection()) return;

	Transfer[] types = null;
	if (targetTypeString[1].getSelection() && targetTypePixmap[1].getSelection()) {
		outputMsg("Target types: TextTransfer, ImageTransfer");
		types = new Transfer[] {TextTransfer.getInstance(), ImageTransfer.getInstance()};
	}
	else if (targetTypePixmap[1].getSelection()) {
		outputMsg("Target types: ImageTransfer");
		types = new Transfer[] {ImageTransfer.getInstance()};
	}
	else { // if (targetTypeString[1].getSelection()) {
		outputMsg("Target types: TextTransfer");
		targetTypeString[1].setSelection(true);
		types = new Transfer[] {TextTransfer.getInstance()};
	}
	
	int operations = 0;
	if (opsMove[1].getSelection()) operations |= DND.DROP_MOVE;
	if (opsCopy[1].getSelection()) operations |= DND.DROP_COPY;
	if (opsLink[1].getSelection()) operations |= DND.DROP_LINK;

	outputMsg("Target ops: " + operations);
	
	dropTarget = new DropTarget(dropTargetCtrl, operations);
	dropTarget.setTransfer(types);
	dropTarget.addDropListener (new DropTargetListener() {
		public void dragEnter(DropTargetEvent event) {
			outputMsg("dragEnter");
		}
		public void dragOver(DropTargetEvent event) {
			outputMsg("dragOver");
		}
		public void dragLeave(DropTargetEvent event) {
			outputMsg("dragLeave");
		}
		public void dragOperationChanged(DropTargetEvent event) {
			outputMsg("dragOperationChanged");
		}
		public void dropAccept(DropTargetEvent event) {
			outputMsg("dragAccept");
		}
		public void drop(DropTargetEvent event) {
			outputMsg("drop");
			if (event.data == null) {
				event.detail = DND.DROP_NONE;
				return;
			}
			setDropTargetData(event.data);
		}
	});
}
private void createDropTargetCtrl(Composite parent, int nr) {
	GridData data = createGridData();
	data.verticalSpan = 4;
	
	switch (nr) {
		case BUTTON_PUSH: {
			Button button = new Button(parent, 0);
			button.setLayoutData(data);	
			button.setText("DropTarget");
			dropTargetCtrl = button;
			break;
		}
		case CANVAS: {
			createDropImage("");
			Canvas canvas = new Canvas(parent, SWT.BORDER);
			canvas.setLayoutData(data);
			canvas.addPaintListener(new PaintListener() {
				public void paintControl(PaintEvent e) {
					if (targetImage != null)
						e.gc.drawImage(targetImage, 0, 0);
				}
			});
			
			dropTargetCtrl = canvas;
			break;
		}
		case BUTTON_CHECK: {
			Button button = new Button(parent, SWT.CHECK);
			button.setLayoutData(data);	
			button.setText("DropTarget");
			dropTargetCtrl = button;
			break;
		}
		case LABEL: {
			Label label = new Label(parent, 0);
			label.setLayoutData(data);	
			label.setText("DropTarget");
			dropTargetCtrl = label;
			break;
		}
		case LIST: {
			List list = new List(parent, SWT.BORDER);
			list.setLayoutData(data);	
			list.add("DropTarget");
			dropTargetCtrl = list;
			break;
		}
		case BUTTON_RADIO: {
			Button button = new Button(parent, SWT.RADIO);
			button.setLayoutData(data);	
			button.setText("DropTarget");
			dropTargetCtrl = button;
			break;
		}
		case TEXT: {
			Text text = new Text(parent, SWT.BORDER);
			text.setLayoutData(data);	
			text.setText("DropTarget");
			dropTargetCtrl = text;
			break;
		}
		
		
	}	
}
	
private void createDropTargetRegisteredGroup(Composite parent) {
	Group group = new Group(parent, 0);
	group.setLayoutData(createGridData());
	group.setLayout(createGridLayout(1));

	// Create radio button
	dropTargetRegistered = new Button(group, SWT.CHECK);
	dropTargetRegistered.setLayoutData(createGridData());	
	dropTargetRegistered.setText("DropTarget registered");
	dropTargetRegistered.addSelectionListener(sl);
}
private GridData createGridData() {
	GridData gridData = new GridData();
	gridData.horizontalAlignment = GridData.FILL;
	gridData.verticalAlignment = GridData.FILL;
	gridData.grabExcessHorizontalSpace = true;
	gridData.grabExcessVerticalSpace = true;	
	return gridData;	
}
private GridLayout createGridLayout(int cols) {
	GridLayout layout = new GridLayout();
	layout.numColumns = cols;
	layout.marginHeight = layout.marginWidth = 0;
	return layout;
}
private void createMessageGroup(Composite parent) {
	Composite c = new Composite(parent, 0);
	GridData data = createGridData();
	data.horizontalSpan = 2;
	c.setLayoutData(data);
	c.setLayout(createGridLayout(1));	
	
	Button button = new Button(c, 0);
	button.setText("Clear message text");
	data = createGridData();
	data.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
	data.grabExcessVerticalSpace = false;	
	button.setLayoutData(data);
	button.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			msgText.setText("");
		}
	});
	
	msgText = new Text(c, SWT.BORDER | SWT.VERTICAL | SWT.HORIZONTAL);
	msgText.setText("Information messages are written to this text widget.");
	msgText.setLayoutData(createGridData());
}
private void createOperationsGroup(Composite parent, int nr) {
	Group group = new Group(parent, 0);
	group.setLayoutData(createGridData());
	group.setLayout(createGridLayout(1));
	group.setText("Operations(s)");

	// Create check buttons
	opsMove[nr] = new Button(group, SWT.CHECK);
	opsMove[nr].setLayoutData(createGridData());	
	opsMove[nr].setText("DND.DROP_MOVE");
	opsMove[nr].addSelectionListener(sl);
	opsMove[nr].setSelection(true);

	opsCopy[nr] = new Button(group, SWT.CHECK);
	opsCopy[nr].setLayoutData(createGridData());	
	opsCopy[nr].setText("DND.DROP_COPY");
	opsCopy[nr].addSelectionListener(sl);		
		
	opsLink[nr] = new Button(group, SWT.CHECK);
	opsLink[nr].setLayoutData(createGridData());	
	opsLink[nr].setText("DND.DROP_LINK");
	opsLink[nr].addSelectionListener(sl);			

}
private void createTargetTypesGroup(Composite parent, int nr) {
	Group group = new Group(parent, 0);
	group.setLayoutData(createGridData());
	group.setLayout(createGridLayout(1));
	group.setText("Target type(s)");

	// Create check buttons
	targetTypeString[nr] = new Button(group, SWT.CHECK);
	targetTypeString[nr].setLayoutData(createGridData());	
	targetTypeString[nr].setText("String");
	targetTypeString[nr].addSelectionListener(sl);
	targetTypeString[nr].setSelection(true);

	targetTypePixmap[nr] = new Button(group, SWT.CHECK);
	targetTypePixmap[nr].setLayoutData(createGridData());	
	targetTypePixmap[nr].setText("Image");
	targetTypePixmap[nr].addSelectionListener(sl);
		
	//targetTypeOther[nr] = new Button(group, SWT.CHECK);
	//targetTypeOther[nr].setLayoutData(createGridData());	
	//targetTypeOther[nr].setText("OTHER");
	//targetTypeOther[nr].addSelectionListener(sl);	

}
private void createWidgetSelectionGroup(Composite parent, int n) {
	final int nr = n;
	final Group group = new Group(parent, 0);
	group.setLayoutData(createGridData());
	group.setLayout(createGridLayout(1));
	group.setText("Widget");

	// Create combo list
	widgetCombo[n] = new Combo(group, SWT.DROP_DOWN | SWT.READ_ONLY);
	widgetCombo[n].setLayoutData(createGridData());
	widgetCombo[n].add("Button");
	widgetCombo[n].add("Canvas");
	widgetCombo[n].add("Check button");	
	widgetCombo[n].add("Label");
	widgetCombo[n].add("List");	
	widgetCombo[n].add("Radio button");
	widgetCombo[n].add("Text");
	widgetCombo[n].addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			// Called for drag side or drop side
			if (nr == 0) {
				if (dragSourceCtrl != null) {
					Composite parent = dragSourceCtrl.getParent();
					dragSourceCtrl.dispose();
					dragSourceCtrl = null;
					dragSource = null;
					createDragSourceCtrl(parent, widgetCombo[nr].getSelectionIndex());
					dragSourceCtrl.moveBelow(group);
					parent.layout();
					createDragSource();
				}
			}
			else {
				if (dropTargetCtrl != null) {
					Composite parent = dropTargetCtrl.getParent();
					dropTargetCtrl.dispose();
					dropTargetCtrl = null;
					dropTarget = null;
					createDropTargetCtrl(parent, widgetCombo[nr].getSelectionIndex());
					dropTargetCtrl.moveAbove(group);
					parent.layout();
					createDropTarget();
				}
			}
		}
	});
	widgetCombo[n].select(0);
}
private Object getDragSourceData(TransferData type) {
	Object obj = null;
	
	switch(widgetCombo[0].getSelectionIndex()) {
		case BUTTON_CHECK:
		case BUTTON_PUSH:
		case BUTTON_RADIO:		
			obj = ((Button)dragSourceCtrl).getText();
			break;
		case CANVAS:
			if (sourceImage != null && ImageTransfer.getInstance().isSupportedType(type))
				obj = sourceImage.getImageData(); 
			else 
				obj = "CANVAS";
				
			break;
		case LABEL:		
			obj = ((Label)dragSourceCtrl).getText();
			break;
		case LIST:	
			if (((List)dragSourceCtrl).getSelectionCount() != 0) 
				obj = ((List)dragSourceCtrl).getSelection()[0];
			break;
		case TEXT:		
			obj = ((Text)dragSourceCtrl).getText();
			break;
	}

	if (obj instanceof String)
		outputMsg("\"" + ((String)obj) + "\" dragged from " + dragSourceCtrl);

	return obj;
}
public static void main(String[] args) {
	try {
		DNDExample example = new DNDExample();
		example.open();
	}
	catch (Throwable e) {
		e.printStackTrace();
	}	
}
/**
 * Creates the shell window and everything in it.
 *
 */
private void open() {
	display = new Display();
	Shell shell = new Shell(display);
	shell.setText("DragAndDropUseCase");
	shell.setLayout(createGridLayout(1));
	shell.setSize(550, 550);

	sl = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			createDragSource();
			createDropTarget();
		}
	};

	// Create composite to hold drag and drop sides
	Composite composite = new Composite(shell, 0);
	composite.setLayoutData(createGridData());
	composite.setLayout(createGridLayout(2));	

	createDragSide(composite);
	createDropSide(composite);
	createMessageGroup(composite);
	
	shell.open();
		
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	
	if (sourceImage != null) sourceImage.dispose();
	if (targetImage != null) targetImage.dispose();
	
}
private void outputMsg(String msg) {
	if (msg.length() > 0) msgText.append("\n");
	msgText.append(msg);
}

private void setDropTargetData(Object obj) {
	if (obj instanceof String)
		outputMsg("\"" + ((String)obj)+"\" dropped onto " + dropTargetCtrl);
	
	switch(widgetCombo[1].getSelectionIndex()) {
		case BUTTON_CHECK:
		case BUTTON_PUSH:
		case BUTTON_RADIO:				
			((Button)dropTargetCtrl).setText((String)obj);
			break;
		case CANVAS:
			if (obj instanceof String)
				createDropImage((String)obj);
			else 
				targetImage = new Image(display, (ImageData)obj);
			dropTargetCtrl.redraw();

			break;
		case LABEL:		
			((Label)dropTargetCtrl).setText((String)obj);
			break;
		case LIST:
			((List)dropTargetCtrl).add((String)obj);
			break;
		case TEXT:		
			((Text)dropTargetCtrl).setText((String)obj);
			break;
	}
}
}
