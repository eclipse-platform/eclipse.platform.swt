package org.eclipse.swt.internal.image;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */

/* Class Definition */
final class LZWNode {
	public LZWNode left, right, children;
	public int code, prefix, suffix;
}
