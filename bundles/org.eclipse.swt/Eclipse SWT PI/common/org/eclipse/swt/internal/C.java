/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

public class C extends Platform {

	static {
		if ("Linux".equals (System.getProperty ("os.name")) && "motif".equals (Platform.PLATFORM)) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			try {
				Library.loadLibrary ("libXm.so.2", false); //$NON-NLS-1$
			} catch (Throwable ex) {}
		}
		Library.loadLibrary ("swt"); //$NON-NLS-1$
	}

	public static final int PTR_SIZEOF = PTR_sizeof ();

public static final native void free (int /*long*/ ptr);
public static final native int /*long*/ getenv (byte[] wcsToMbcs);
public static final native int /*long*/ malloc (int /*long*/ size);
public static final native void memmove (int /*long*/ dest, byte[] src, int /*long*/ size);
public static final native void memmove (int /*long*/ dest, char[] src, int /*long*/ size);
public static final native void memmove (int /*long*/ dest, double[] src, int /*long*/ size);
public static final native void memmove (int /*long*/ dest, float[] src, int /*long*/ size);
public static final native void memmove (int /*long*/ dest, int[] src, int /*long*/ size);
public static final native void memmove (int /*long*/ dest, long[] src, int /*long*/ size);
public static final native void memmove (int /*long*/ dest, short[] src, int /*long*/ size);
public static final native void memmove (byte[] dest, char[] src, int /*long*/ size);
public static final native void memmove (byte[] dest, int /*long*/ src, int /*long*/ size);
public static final native void memmove (int /*long*/ dest, int /*long*/ src, int /*long*/ size);
public static final native void memmove (char[] dest, int /*long*/ src, int /*long*/ size);
public static final native void memmove (double[] dest, int /*long*/ src, int /*long*/ size);
public static final native void memmove (float[] dest, int /*long*/ src, int /*long*/ size);
public static final native void memmove (int[] dest, byte[] src, int /*long*/ size);
public static final native void memmove (short[] dest, int /*long*/ src, int /*long*/ size);
public static final native void memmove (int[] dest, int /*long*/ src, int /*long*/ size);
public static final native void memmove (long[] dest, int /*long*/ src, int /*long*/ size);
public static final native int /*long*/ memset (int /*long*/ buffer, int c, int /*long*/ num);
public static final native int PTR_sizeof ();
public static final native int strlen (int /*long*/ s);
}
