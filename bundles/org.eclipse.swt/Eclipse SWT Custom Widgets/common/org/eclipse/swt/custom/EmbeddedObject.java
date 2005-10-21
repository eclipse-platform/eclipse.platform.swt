
/* UNDER CONSTRUCTION - DO NOT USE THIS CLASS */

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
package org.eclipse.swt.custom;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * The Interface that needs to be implemented by object to be embedded in
 * a StyledText.  
 * <p>
 *
 * @see StyledText
 */
public abstract class EmbeddedObject {
	public abstract int getAscent();
	public abstract int getDescent();
	public abstract int getAdvance();
	public abstract void draw(GC gc, int x, int y, int ascent, int descent);

	/**
	 * Helper class to embedded a Control in a StyledText
	 * 
	 */
	public class EmbeddedControl extends EmbeddedObject {
		Control control;
		int alignment;
		
		/**
		 * 
		 * 
		 * @param control the control
		 * @param alignment the vertical alignment for the control
		 * SWT.TOP == top of the line
		 * SWT.CENTER == baseline
		 * SWT.BOTTOM == bottom of the line
		 */		
		public EmbeddedControl(Control control, int alignment) {
			if (control == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
			if (control.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			this.control = control;
			this.alignment = alignment & (SWT.TOP | SWT.CENTER | SWT.BOTTOM);
			if (this.alignment == 0) this.alignment = SWT.CENTER;
		}
		
		public int getAscent() {
			return control.getSize().y;
		}
		public int getDescent() {
			return 0;
		}
		public int getAdvance() {
			return control.getSize().x;
		}
		public void draw(GC gc, int x, int y, int ascent, int descent) {
			switch (alignment) {
				case SWT.TOP:
					control.setLocation(x, y);
					break;
				case SWT.CENTER:
					control.setLocation(x, y + ascent - getAscent());
					break;
				case SWT.BOTTOM:
					control.setLocation(x, y + ascent + descent - getAscent());
					break;
			}
		}
	}
	
	/**
	 * Helper class to embedded a Image in a StyledText
	 * 
	 */
	public class EmbeddedImage extends EmbeddedObject {
		Image image;
		int alignment;
		
		/**
		 * 
		 * 
		 * @param control the control
		 * @param alignment the vertical alignment for the control
		 * SWT.TOP == top of the line
		 * SWT.CENTER == baseline
		 * SWT.BOTTOM == bottom of the line
		 */		
		public EmbeddedImage(Image image, int alignment) {
			if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
			if (image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			this.image = image;
			this.alignment = alignment & (SWT.TOP | SWT.CENTER | SWT.BOTTOM);
			if (this.alignment == 0) this.alignment = SWT.CENTER;
		}
		
		public int getAscent() {
			return image.getBounds().height;
		}
		public int getDescent() {
			return 0;
		}
		public int getAdvance() {
			return image.getBounds().width;
		}
		public void draw(GC gc, int x, int y, int ascent, int descent) {
			switch (alignment) {
				case SWT.TOP:
					gc.drawImage(image, x, y);
					break;
				case SWT.CENTER:
					gc.drawImage(image, x, y + ascent - getAscent());				
					break;
				case SWT.BOTTOM:
					gc.drawImage(image, x, y + ascent + descent - getAscent());
					break;
			}
		}
	}
	
	/**
	 * 
	 * 
	 *
	 */	
	public class Bullet extends EmbeddedObject {
		
		/**
		 * @param style bullet image, check mark image, number, roman, 
		 */		
		public Bullet(int style, int level, int index) {
			
		}
		public int getAscent() {			
			return 0;
		}
		public int getDescent() {
			return 0;
		}
		public int getAdvance() {
			return 0;
		}
		public void draw(GC gc, int x, int y, int ascent, int descent) {
		}
	}
}

