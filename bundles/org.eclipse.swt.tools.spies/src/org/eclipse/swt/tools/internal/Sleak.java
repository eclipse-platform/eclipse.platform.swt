/*******************************************************************************
 * Copyright (c) 2007, 2020 IBM Corporation and others.
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
package org.eclipse.swt.tools.internal;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.List;

/**
 * Instructions on how to use the Sleak tool with a standlaone SWT example:
 *
 * Modify the main method below to launch your application.
 * Run Sleak.
 *
 */
public class Sleak {
	List list;
	Canvas canvas;
	Button enableTracking, snapshot, diff, stackTrace, saveAs, save;
	Combo diffType;
	Text text;
	Label label;

	String filterPath = "";
	String fileName = "sleakout";
	String selectedName = null;
	boolean incrementFileNames = true;
	boolean saveImages = true;
	int fileCount = 0;

	java.util.List<ObjectWithError> oldObjects = new ArrayList<> ();
	java.util.List<ObjectWithError> objects = new ArrayList<> ();

public static void main (String [] args) {
	DeviceData data = new DeviceData();
	data.tracking = true;
	Display display = new Display (data);
	Sleak sleak = new Sleak ();
	Shell shell = new Shell(display);
	shell.setText ("S-Leak");
	Point size = shell.getSize ();
	shell.setSize (size.x / 2, size.y / 2);
	GridLayout layout = new GridLayout(2, false);
	layout.horizontalSpacing = 0;
	layout.verticalSpacing = 0;
	shell.setLayout(layout);
	sleak.create (shell);
	shell.open();

	// Launch your application here
	// e.g.
//	Shell shell2 = new Shell(display);
//	Button button1 = new Button(shell2, SWT.PUSH);
//	button1.setBounds(10, 10, 100, 50);
//	button1.setText("Hello World");
//	Image image = new Image(display, 20, 20);
//	Button button2 = new Button(shell2, SWT.PUSH);
//	button2.setBounds(10, 70, 100, 50);
//	button2.setImage(image);
//	button1.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
//		Font oldFont = button1.getFont();
//		int style = oldFont.getFontData()[0].getStyle() ^ SWT.BOLD;
//		button1.setFont(new Font(display, "Arial", 10, style));
//		button1.setForeground(new Color(100,200,100));
//		oldFont.dispose();
//	}));
//	shell2.open();

	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

public void create (Composite parent) {
	enableTracking = new Button (parent, SWT.CHECK);
	enableTracking.setText ("Enable");
	enableTracking.setToolTipText("Enable Device resource tracking. Only resources allocated once enabled will be tracked. To track devices created before view is created, turn on tracing options, see https://www.eclipse.org/swt/tools.php");
	enableTracking.addListener (SWT.Selection, e -> toggleEnableTracking ());
	enableTracking.setSelection(enableTracking.getDisplay().isTracking());
	canvas = new Canvas (parent, SWT.BORDER);
	canvas.addListener (SWT.Paint, event -> paintCanvas (event));
	canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 10));
	text = new Text (parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 10));
	setVisible(text,  false);
	snapshot = new Button (parent, SWT.PUSH);
	snapshot.setText ("Snap");
	snapshot.addListener (SWT.Selection, event -> refreshAll ());
	snapshot.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
	diff = new Button (parent, SWT.PUSH);
	diff.setText ("Diff");
	diff.addListener (SWT.Selection, event -> refreshDifference ());
	diff.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
	diffType = new Combo (parent, SWT.CHECK);
	diffType.add ("Object identity");
	diffType.add ("Creator class and line");
	diffType.add ("Creator class");
	diffType.select(0);
	diffType.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
	stackTrace = new Button (parent, SWT.CHECK);
	stackTrace.setText ("Stack");
	stackTrace.addListener (SWT.Selection, e -> toggleStackTrace ());
	list = new List (parent, SWT.BORDER | SWT.V_SCROLL);
	list.addListener (SWT.Selection, event -> refreshObject ());
	list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
	label = new Label (parent, SWT.NONE);
	label.setText ("0 object(s)");
	saveAs = new Button (parent, SWT.PUSH);
	saveAs.setText ("Save As...");
	saveAs.setToolTipText("Saves the contents of the list to a file, optionally with the stack traces if selected.");
	saveAs.addListener (SWT.Selection, event -> saveToFile (true));
	saveAs.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
	save = new Button (parent, SWT.PUSH);
	save.setText ("Save");
	save.setToolTipText("Saves to the previously selected file.");
	save.addListener (SWT.Selection, event -> saveToFile (false));
	save.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
	stackTrace.setSelection (false);
}

private void toggleEnableTracking() {
	Display display = enableTracking.getDisplay();
	boolean tracking = display.isTracking();
	display.setTracking(!tracking);
}

void refreshLabel () {
	Map<String, Long> deleted = oldObjects.stream().collect(
			Collectors.groupingBy(o -> o.object.getClass().getSimpleName(), Collectors.counting()));
	Map<String, Long> created = objects.stream().collect(
			Collectors.groupingBy(o -> o.object.getClass().getSimpleName(), Collectors.counting()));
	StringBuilder sb = new StringBuilder();
	Stream.concat(deleted.keySet().stream(), created.keySet().stream()).distinct().sorted().
		forEach(type -> addCounts(sb, type, deleted.get(type), created.get(type)));
	label.setText (sb.length() > 0 ? sb.toString() : "0 object(s)");
	canvas.getParent().layout();
}

void addCounts (StringBuilder string, String type, Long deleted, Long created) {
	if (deleted != null || created != null) {
		if (deleted != null) {
			string.append("-" + deleted);
			if (created != null) {
				string.append(" / ");
			}
		}
		if (created != null) {
			string.append(created);
		}
		string.append(" " + type + "(s)\n");
	}
}

void refreshDifference () {
	Display display = canvas.getDisplay();
	DeviceData info = display.getDeviceData ();
	if (!info.tracking) {
		Shell shell = canvas.getShell();
		MessageBox dialog = new MessageBox (shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
		dialog.setText (shell.getText ());
		dialog.setMessage ("Warning: Device is not tracking resource allocation\nWould you like to enable tracking now for future created resources?");
		if (SWT.YES == dialog.open ()) {
			enableTracking.setSelection(true);
			toggleEnableTracking();
		}
	}

	oldObjects = new ArrayList<> (objects);
	objects.clear ();
	for (int i=0; i<info.objects.length; i++) {
		boolean found = false;
		Iterator<ObjectWithError> oldObject = oldObjects.iterator ();
		if (!(info.objects [i] instanceof Color)) {
			// Bug 563018: Colors don't require disposal, so exclude them from the list of allocated objects.
			while (oldObject.hasNext () && !found) {
				if (info.objects [i] == oldObject.next ().object) {
					oldObject.remove ();
					found = true;
				}
			}
			if (!found) {
				objects.add (new ObjectWithError (info.objects [i], info.errors[i]));
			}
		}
	}
	if (diffType.getSelectionIndex() > 0) {
		Iterator<ObjectWithError> object = objects.iterator ();
		while (object.hasNext ()) {
			StackTraceElement stack = object.next ().getCreator ();
			Iterator<ObjectWithError> old = oldObjects.iterator ();
			while (old.hasNext ()) {
				if (creatorEquals(stack, old.next().getCreator ())) {
					old.remove ();
					object.remove ();
				}
			}
		}
	}
	list.removeAll ();
	text.setText ("");
	canvas.redraw ();
	for (ObjectWithError object : objects) {
		list.add (object.object.toString());
	}
	refreshLabel ();
}

boolean creatorEquals (StackTraceElement first, StackTraceElement second) {
	switch (diffType.getSelectionIndex()) {
		case 1: return first.equals(second);
		case 2: return first.getClassName().equals(second.getClassName());
		default: throw new IllegalArgumentException();
	}
}
private void saveToFile(boolean prompt) {
	if (prompt || selectedName == null) {
		FileDialog dialog = new FileDialog(saveAs.getShell(), SWT.SAVE);
		dialog.setFilterPath(filterPath);
		dialog.setFileName(fileName);
		dialog.setOverwrite(true);
		selectedName = dialog.open();
		fileCount = 0;
		if (selectedName == null) {
			return;
		}
		filterPath = dialog.getFilterPath();
		fileName = dialog.getFileName();

		MessageBox msg = new MessageBox(saveAs.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		msg.setText("Append incrementing file counter?");
		msg.setMessage("Append an incrementing file count to the file name on each save, starting at 000?");
		incrementFileNames = msg.open() == SWT.YES;

		msg = new MessageBox(saveAs.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		msg.setText("Save images for each resource?");
		msg.setMessage("Save an image (png) for each resource?");
		saveImages = msg.open() == SWT.YES;
	}

	String fileName = selectedName;
	if (incrementFileNames) {
		fileName = String.format("%s_%03d", fileName, fileCount++);
	}
	try (PrintWriter file = new PrintWriter(new FileOutputStream(fileName))) {
		
		int i = 0;
		for (ObjectWithError o : objects) {
			Object object = o.object;
			Error error = o.error;
			file.print(object.toString());
			if (saveImages) {
				String suffix = String.format("%05d.png", i++);
				String pngName = String.format("%s_%s", fileName, suffix);
				Image image = new Image(saveAs.getDisplay(), 100, 100);
				try {
					GC gc = new GC(image);
					try {
						draw(gc, object);
					} finally {
						gc.dispose();
					}
					ImageLoader loader = new ImageLoader();
					loader.data = new ImageData[] { image.getImageData() };
					loader.save(pngName, SWT.IMAGE_PNG);
				} finally {
					image.dispose();
				}

				file.print(" -> ");
				file.print(suffix);
			}
			file.println();
			if (stackTrace.getSelection()) {
				error.printStackTrace(file);
				System.out.println();
			}
		}
	} catch (IOException e1) {
		MessageBox msg = new MessageBox(saveAs.getShell(), SWT.ICON_ERROR | SWT.OK);
		msg.setText("Failed to save");
		msg.setMessage("Failed to save S-Leak file.\n" + e1.getMessage());
		msg.open();
	}
}

void toggleStackTrace () {
	refreshObject ();
	canvas.getParent().layout ();
}

void paintCanvas (Event event) {
	canvas.setCursor (null);
	int index = list.getSelectionIndex ();
	if (index == -1) return;
	GC gc = event.gc;
	Object object = objects.get(index).object;
	draw(gc, object);
}

void draw(GC gc, Object object) {
	if (object instanceof Cursor) {
		if (((Cursor)object).isDisposed ()) return;
		canvas.setCursor ((Cursor) object);
		return;
	}
	if (object instanceof Font) {
		if (((Font)object).isDisposed ()) return;
		gc.setFont ((Font) object);
		String string = "";
		String lf = text.getLineDelimiter ();
		for (FontData data : gc.getFont ().getFontData ()) {
			String style = "NORMAL";
			int bits = data.getStyle ();
			if (bits != 0) {
				if ((bits & SWT.BOLD) != 0) style = "BOLD ";
				if ((bits & SWT.ITALIC) != 0) style += "ITALIC";
			}
			string += data.getName () + " " + data.getHeight () + " " + style + lf;
		}
		gc.drawString (string, 0, 0);
		return;
	}
	//NOTHING TO DRAW FOR GC
//	if (object instanceof GC) {
//		return;
//	}
	if (object instanceof Image) {
		if (((Image)object).isDisposed ()) return;
		gc.drawImage ((Image) object, 0, 0);
		return;
	}
	if (object instanceof Path) {
		if (((Path)object).isDisposed ()) return;
		gc.drawPath ((Path) object);
		return;
	}
	if (object instanceof Pattern) {
		if (((Pattern)object).isDisposed ()) return;
		gc.setBackgroundPattern ((Pattern)object);
		gc.fillRectangle (canvas.getClientArea ());
		gc.setBackgroundPattern (null);
		return;
	}
	if (object instanceof Region) {
		if (((Region)object).isDisposed ()) return;
		String string = ((Region)object).getBounds().toString();
		gc.drawString (string, 0, 0);
		return;
	}
	if (object instanceof TextLayout) {
		if (((TextLayout)object).isDisposed ()) return;
		((TextLayout)object).draw (gc, 0, 0);
		return;
	}
	if (object instanceof Transform) {
		if (((Transform)object).isDisposed ()) return;
		String string = ((Transform)object).toString();
		gc.drawString (string, 0, 0);
		return;
	}
}

void refreshObject () {
	int index = list.getSelectionIndex ();
	if (index == -1) return;
	if (stackTrace.getSelection ()) {
		text.setText (objects.get(index).getStack());
		setVisible(text, true);
		setVisible(canvas, false);
		canvas.getParent().layout();
	} else {
		setVisible(canvas, true);
		setVisible(text, false);
		canvas.redraw ();
	}
}

private void setVisible(Control control, boolean visible) {
	control.setVisible(visible);
	((GridData)control.getLayoutData()).exclude = !visible;
}

void refreshAll () {
	objects.clear();
	refreshDifference ();
	oldObjects = new ArrayList<>(objects);
}

private static final class ObjectWithError {
	final Object object;
	final Error error;
	String stack;
	StackTraceElement creator;

	ObjectWithError(Object o, Error e) {
		this.object = o;
		this.error = e;
	}

	StackTraceElement getCreator() {
		if (creator == null) {
			String objectType = object.getClass().getName();
			Iterator<StackTraceElement> stack = Arrays.asList(error.getStackTrace()).iterator();
			while (stack.hasNext()) {
				StackTraceElement element = stack.next();
				if (element.getClassName().equals(objectType) && element.getMethodName().equals("<init>")) {
					creator = stack.hasNext() ? stack.next() : null;
					break;
				}
			}
		}
		return creator;
	}

	String getStack() {
		if (stack == null) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			PrintStream s = new PrintStream(stream);
			error.printStackTrace(s);
			stack = stream.toString();
		}
		return stack;
	}
}
}
