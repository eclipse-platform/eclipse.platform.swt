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
package org.eclipse.swt.graphics;


import org.eclipse.swt.internal.SerializableCompatibility;
import org.eclipse.swt.*;

/**
 * Instances of this class are descriptions of colors in
 * terms of the primary additive color model (red, green and
 * blue). A color may be described in terms of the relative
 * intensities of these three primary colors. The brightness
 * of each color is specified by a value in the range 0 to 255,
 * where 0 indicates no color (blackness) and 255 indicates
 * maximum intensity.
 * <p>
 * The hashCode() method in this class uses the values of the public
 * fields to compute the hash value. When storing instances of the
 * class in hashed collections, do not modify these fields after the
 * object has been inserted.  
 * </p>
 * <p>
 * Application code does <em>not</em> need to explicitly release the
 * resources managed by each instance when those instances are no longer
 * required, and thus no <code>dispose()</code> method is provided.
 * </p>
 *
 * @see Color
 */

public final class RGB implements SerializableCompatibility {
	
	/**
	 * the red component of the RGB
	 */
	public int red;
	
	/**
	 * the green component of the RGB
	 */
	public int green;
	
	/**
	 * the blue component of the RGB
	 */
	public int blue;
	
	static final long serialVersionUID = 3258415023461249074L;
	
/**
 * Constructs an instance of this class with the given
 * red, green and blue values.
 *
 * @param red the red component of the new instance
 * @param green the green component of the new instance
 * @param blue the blue component of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the red, green or blue argument is not between 0 and 255</li>
 * </ul>
 */
public RGB(int red, int green, int blue) {
	if ((red > 255) || (red < 0) ||
		(green > 255) || (green < 0) ||
		(blue > 255) || (blue < 0))
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.red = red;
	this.green = green;
	this.blue = blue;
}

/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param object the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode()
 */
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof RGB)) return false;
	RGB rgb = (RGB)object;
	return (rgb.red == this.red) && (rgb.green == this.green) && (rgb.blue == this.blue);
}

/**
 * Returns an integer hash code for the receiver. Any two 
 * objects that return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals(Object)
 */
public int hashCode () {
	return (blue << 16) | (green << 8) | red;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the <code>RGB</code>
 */
public String toString () {
	return "RGB {" + red + ", " + green + ", " + blue + "}"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
}

}
