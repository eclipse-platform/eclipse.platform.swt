package org.eclipse.swt.internal.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class LRESULT {
	public int value;
	public static final LRESULT ONE = new LRESULT (1);
	public static final LRESULT ZERO = new LRESULT (0);
public LRESULT (int value) {
	this.value = value;
}
}
