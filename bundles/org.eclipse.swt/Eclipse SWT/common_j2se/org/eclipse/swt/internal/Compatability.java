package org.eclipse.swt.internal;

public class Compatability {

/**
 * Answers the double conversion of the most negative (i.e.
 * closest to negative infinity) integer value which is
 * greater than the argument.
 *
 * @author		OTI
 * @version		initial
 *
 * @param		d		the value to be converted
 * @return		the ceiling of the argument.
 */
public static double ceil (double d) {
	return Math.ceil(d);
}

}

