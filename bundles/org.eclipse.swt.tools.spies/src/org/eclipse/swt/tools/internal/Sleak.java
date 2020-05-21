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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

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
	Button snapshot, diff, stackTrace, saveAs, save;
	Text text;
	Label label;
	
	String filterPath = "";
	String fileName = "sleakout";
	String selectedName = null;
	boolean incrementFileNames = true;
	boolean saveImages = true;
	int fileCount = 0;

	Object [] oldObjects = new Object [0];
	Error [] oldErrors = new Error [0];
	Object [] objects = new Object [0];
	Error [] errors = new Error [0];

public static void main (String [] args) {
	DeviceData data = new DeviceData();
	data.tracking = true;
	Display display = new Display (data);
	Sleak sleak = new Sleak ();
	Shell shell = new Shell(display);
	shell.setText ("S-Leak");
	Point size = shell.getSize ();
	shell.setSize (size.x / 2, size.y / 2);
	sleak.create (shell);
	shell.open();
	
	// Launch your application here
	// e.g.		
//	Shell shell = new Shell(display);
//	Button button1 = new Button(shell, SWT.PUSH);
//	button1.setBounds(10, 10, 100, 50);
//	button1.setText("Hello World");
//	Image image = new Image(display, 20, 20);
//	Button button2 = new Button(shell, SWT.PUSH);
//	button2.setBounds(10, 70, 100, 50);
//	button2.setImage(image);
//	shell.open();
	
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

public void create (Composite parent) {
	list = new List (parent, SWT.BORDER | SWT.V_SCROLL);
	list.addListener (SWT.Selection, event -> refreshObject ());
	text = new Text (parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	canvas = new Canvas (parent, SWT.BORDER);
	canvas.addListener (SWT.Paint, event -> paintCanvas (event));
	stackTrace = new Button (parent, SWT.CHECK);
	stackTrace.setText ("Stack");
	stackTrace.addListener (SWT.Selection, e -> toggleStackTrace ());
	snapshot = new Button (parent, SWT.PUSH);
	snapshot.setText ("Snap");
	snapshot.addListener (SWT.Selection, event -> refreshAll ());
	diff = new Button (parent, SWT.PUSH);
	diff.setText ("Diff");
	diff.addListener (SWT.Selection, event -> refreshDifference ());
	label = new Label (parent, SWT.BORDER);
	label.setText ("0 object(s)");
	saveAs = new Button (parent, SWT.PUSH);
	saveAs.setText ("Save As...");
	saveAs.setToolTipText("Saves the contents of the list to a file, optionally with the stack traces if selected.");
	saveAs.addListener (SWT.Selection, event -> saveToFile (true));
	save = new Button (parent, SWT.PUSH);
	save.setText ("Save");
	save.setToolTipText("Saves to the previously selected file.");
	save.addListener (SWT.Selection, event -> saveToFile (false));
	parent.addListener (SWT.Resize, e -> layout ());
	stackTrace.setSelection (false);
	text.setVisible (false);
	layout();
}

void refreshLabel () {
	int colors = 0, cursors = 0, fonts = 0, gcs = 0, images = 0;
	int paths = 0, patterns = 0, regions = 0, textLayouts = 0, transforms= 0;
	for (Object object : objects) {
		if (object instanceof Color) colors++;
		if (object instanceof Cursor) cursors++;
		if (object instanceof Font) fonts++;
		if (object instanceof GC) gcs++;
		if (object instanceof Image) images++;
		if (object instanceof Path) paths++;
		if (object instanceof Pattern) patterns++;
		if (object instanceof Region) regions++;
		if (object instanceof TextLayout) textLayouts++;
		if (object instanceof Transform) transforms++;
	}
	String string = "";
	if (colors != 0) string += colors + " Color(s)\n";
	if (cursors != 0) string += cursors + " Cursor(s)\n";
	if (fonts != 0) string += fonts + " Font(s)\n";
	if (gcs != 0) string += gcs + " GC(s)\n";
	if (images != 0) string += images + " Image(s)\n";
	if (paths != 0) string += paths + " Paths(s)\n";
	if (patterns != 0) string += patterns + " Pattern(s)\n";
	if (regions != 0) string += regions + " Region(s)\n";
	if (textLayouts != 0) string += textLayouts + " TextLayout(s)\n";
	if (transforms != 0) string += transforms + " Transform(s)\n";
	if (string.length () != 0) {
		string = string.substring (0, string.length () - 1);
	}
	label.setText (string);
}

void refreshDifference () {
	Display display = canvas.getDisplay();
	DeviceData info = display.getDeviceData ();
	if (!info.tracking) {
		Shell shell = canvas.getShell();
		MessageBox dialog = new MessageBox (shell, SWT.ICON_WARNING | SWT.OK);
		dialog.setText (shell.getText ());
		dialog.setMessage ("Warning: Device is not tracking resource allocation");
		dialog.open ();
	}
	Object [] newObjects = info.objects;
	Error [] newErrors = info.errors;
	Object [] diffObjects = new Object [newObjects.length];
	Error [] diffErrors = new Error [newErrors.length];
	int count = 0;
	for (int i=0; i<newObjects.length; i++) {
		int index = 0;
		while (index < oldObjects.length) {
			if (newObjects [i] == oldObjects [index]) break;
			index++;
		}
		if (index == oldObjects.length) {
			diffObjects [count] = newObjects [i];
			diffErrors [count] = newErrors [i];
			count++;
		}
	}
	objects = new Object [count];
	errors = new Error [count];
	System.arraycopy (diffObjects, 0, objects, 0, count);
	System.arraycopy (diffErrors, 0, errors, 0, count);
	list.removeAll ();
	text.setText ("");
	canvas.redraw ();
	for (Object object : objects) {
		list.add (object.toString());
	}
	refreshLabel ();
	layout ();
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

		for (int i = 0; i < errors.length; i++) {
			Object object = objects[i];
			Error error = errors[i];
			file.print(object.toString());
			if (saveImages) {
				String suffix = String.format("%05d.png", i);
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
	layout ();
}

void paintCanvas (Event event) {
	canvas.setCursor (null);
	int index = list.getSelectionIndex ();
	if (index == -1) return;
	GC gc = event.gc;
	Object object = objects [index];
	draw(gc, object);
}

void draw(GC gc, Object object) {
	if (object instanceof Color) {
		if (((Color)object).isDisposed ()) return;
		gc.setBackground ((Color) object);
		gc.fillRectangle (canvas.getClientArea());
		return;
	}
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
		ByteArrayOutputStream stream = new ByteArrayOutputStream ();
		PrintStream s = new PrintStream (stream);
		errors [index].printStackTrace (s);
		text.setText (stream.toString ());
		text.setVisible (true);
		canvas.setVisible (false);
	} else {
		canvas.setVisible (true);
		text.setVisible (false);
		canvas.redraw ();
	}
}

void refreshAll () {
	oldObjects = new Object [0];
	oldErrors = new Error [0];
	refreshDifference ();
	oldObjects = objects;
	oldErrors = errors;
}

void layout () {
	Composite parent = canvas.getParent();
	Rectangle rect = parent.getClientArea ();
	int width = 0;
	String [] items = list.getItems ();
	GC gc = new GC (list);
	for (int i=0; i<objects.length; i++) {
		width = Math.max (width, gc.stringExtent (items [i]).x);
	}
	gc.dispose ();
	Point snapshotSize = snapshot.computeSize (SWT.DEFAULT, SWT.DEFAULT);
	Point diffSize = diff.computeSize (SWT.DEFAULT, SWT.DEFAULT);
	Point stackSize = stackTrace.computeSize (SWT.DEFAULT, SWT.DEFAULT);
	Point labelSize = label.computeSize (SWT.DEFAULT, SWT.DEFAULT);
	Point saveAsSize = saveAs.computeSize (SWT.DEFAULT, SWT.DEFAULT);
	Point saveSize = save.computeSize (SWT.DEFAULT, SWT.DEFAULT);
	width = Math.max (snapshotSize.x, Math.max (diffSize.x, Math.max (stackSize.x, width)));
	width = Math.max (saveAsSize.x, Math.max (saveSize.x, width));
	width = Math.max (labelSize.x, list.computeSize (width, SWT.DEFAULT).x);
	width = Math.max (64, width);
	snapshot.setBounds (0, 0, width, snapshotSize.y);
	diff.setBounds (0, snapshotSize.y, width, diffSize.y);
	stackTrace.setBounds (0, snapshotSize.y + diffSize.y, width, stackSize.y);
	label.setBounds (0, rect.height - saveSize.y - saveAsSize.y - labelSize.y, width, labelSize.y);
	saveAs.setBounds (0, rect.height - saveSize.y - saveAsSize.y, width, saveAsSize.y);
	save.setBounds (0, rect.height - saveSize.y, width, saveSize.y);
	int height = snapshotSize.y + diffSize.y + stackSize.y;
	list.setBounds (0, height, width, rect.height - height - labelSize.y - saveAsSize.y -saveSize.y);
	text.setBounds (width, 0, rect.width - width, rect.height);
	canvas.setBounds (width, 0, rect.width - width, rect.height);
}
		
}
