/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.paint;


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;

/**
 * A text drawing tool.
 */
public class TextTool extends BasicPaintSession implements PaintTool {
	private ToolSettings settings;
	private String drawText = PaintExample.getResourceString("tool.Text.settings.defaulttext");

	/**
	 * Constructs a PaintTool.
	 * 
	 * @param toolSettings the new tool settings
	 * @param paintSurface the PaintSurface we will render on.
	 */
	public TextTool(ToolSettings toolSettings, PaintSurface paintSurface) {
		super(paintSurface);
		set(toolSettings);
	}
	
	/**
	 * Sets the tool's settings.
	 * 
	 * @param toolSettings the new tool settings
	 */
	public void set(ToolSettings toolSettings) {
		settings = toolSettings;
	}
	
	/**
	 * Returns name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintExample.getResourceString("tool.Text.label");
	}
	
	/**
	 * Activates the tool.
	 */
	public void beginSession() {
		getPaintSurface().setStatusMessage(PaintExample.getResourceString(
			"session.Text.message"));
	}
	
	/**
	 * Deactivates the tool.
     */
	public void endSession() {
		getPaintSurface().clearRubberbandSelection();
	}
	
	/**
	 * Aborts the current operation.
	 */
	public void resetSession() {
		getPaintSurface().clearRubberbandSelection();
	}
	
	/**
	 * Handles a mouseDown event.
	 * 
	 * @param event the mouse event detail information
	 */
	public void mouseDown(MouseEvent event) {
		if (event.button == 1) {
			// draw with left mouse button
			getPaintSurface().commitRubberbandSelection();
		} else {
			// set text with right mouse button
			getPaintSurface().clearRubberbandSelection();
			Shell shell = getPaintSurface().getShell();
			final Shell dialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			dialog.setText(PaintExample.getResourceString("tool.Text.dialog.title"));
			dialog.setLayout(new GridLayout());
			Label label = new Label(dialog, SWT.NONE);
			label.setText(PaintExample.getResourceString("tool.Text.dialog.message"));
			label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
			final Text field = new Text(dialog, SWT.SINGLE | SWT.BORDER);
			field.setText(drawText);
			field.selectAll();
			field.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			Composite buttons = new Composite(dialog, SWT.NONE);
			GridLayout layout = new GridLayout(2, true);
			layout.marginWidth = 0;
			buttons.setLayout(layout);
			buttons.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));
			Button ok = new Button(buttons, SWT.PUSH);
			ok.setText(PaintExample.getResourceString("OK"));
			ok.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
			ok.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					drawText = field.getText();
					dialog.dispose();
				}
			});
			Button cancel = new Button(buttons, SWT.PUSH);
			cancel.setText(PaintExample.getResourceString("Cancel"));
			cancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					dialog.dispose();
				}
			});
			dialog.setDefaultButton(ok);
			dialog.pack();
			dialog.open();
			Display display = dialog.getDisplay();
			while (! shell.isDisposed() && ! dialog.isDisposed()) {
				if (! display.readAndDispatch()) display.sleep();
			}
		}	
	}

	/**
	 * Handles a mouseDoubleClick event.
	 * 
	 * @param event the mouse event detail information
	 */
	public void mouseDoubleClick(MouseEvent event) {
	}

	/**
	 * Handles a mouseUp event.
	 * 
	 * @param event the mouse event detail information
	 */
	public void mouseUp(MouseEvent event) {
	}
	
	/**
	 * Handles a mouseMove event.
	 * 
	 * @param event the mouse event detail information
	 */
	public void mouseMove(MouseEvent event) {
		final PaintSurface ps = getPaintSurface();
		ps.setStatusCoord(ps.getCurrentPosition());
		ps.clearRubberbandSelection();
		ps.addRubberbandSelection(
			new TextFigure(settings.commonForegroundColor, settings.commonFont,
				drawText, event.x, event.y));
	}
}
