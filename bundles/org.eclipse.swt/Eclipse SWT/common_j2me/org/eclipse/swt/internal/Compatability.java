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
	long l = (long) d;
	if (d == l) return d;
	if (d < 0)
		return (double) l;
	else
		return (double) l + 1;
}

}

