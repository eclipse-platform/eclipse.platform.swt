package org.eclipse.swt.internal.image;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

final class LZWNode {
	public LZWNode left, right, children;
	public int code, prefix, suffix;
}
