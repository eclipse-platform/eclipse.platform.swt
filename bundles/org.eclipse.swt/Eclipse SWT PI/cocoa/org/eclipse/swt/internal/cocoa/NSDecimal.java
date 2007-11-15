package org.eclipse.swt.internal.cocoa;

public class NSDecimal {
	public int _exponent;
	public int _length;
	public int _isNegative;
	public int _isCompact;
	public int _reserved;
	public short[] _mantissa = new short[OS.NSDecimalMaxSize];
}
