package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * A text drawing tool.
 */
public class TextTool extends BasicPaintSession implements PaintTool {
	private ToolSettings settings;
	private String drawText = PaintPlugin.getResourceString("tool.Text.settings.defaulttext");

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
		return PaintPlugin.getResourceString("tool.Text.label");
	}
	
	/**
	 * Activates the tool.
	 */
	public void beginSession() {
		getPaintSurface().setStatusMessage(PaintPlugin.getResourceString(
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
			InputDialog inputDialog = new InputDialog(getPaintSurface().getShell(),
				PaintPlugin.getResourceString("tool.Text.dialog.title"),
				PaintPlugin.getResourceString("tool.Text.dialog.message"),
				drawText, null);
			inputDialog.setBlockOnOpen(true);
			inputDialog.open();
			if (inputDialog.getReturnCode() == InputDialog.OK) drawText = inputDialog.getValue();
			inputDialog.close();
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
