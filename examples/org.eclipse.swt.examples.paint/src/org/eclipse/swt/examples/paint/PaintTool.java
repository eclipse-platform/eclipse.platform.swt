package org.eclipse.swt.examples.paint;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public interface PaintTool extends PaintRenderer, PaintSession {
	/**
	 * Sets the tool's settings.
	 * 
	 * @param toolSettings the new tool settings
	 */
	public void set(ToolSettings toolSettings);
}
