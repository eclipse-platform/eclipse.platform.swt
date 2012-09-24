/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

/** @param ptr cast=(void *) */
public static final native void free (long /*int*/ ptr);
/** @param env cast=(const char *) */
public static final native long /*int*/ getenv (byte[] env);
public static final native long /*int*/ malloc (long /*int*/ size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out critical
 * @param size cast=(size_t)
 */
public static final native void memmove (long /*int*/ dest, byte[] src, long /*int*/ size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out critical
 * @param size cast=(size_t)
 */
public static final native void memmove (long /*int*/ dest, char[] src, long /*int*/ size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out critical
 * @param size cast=(size_t)
 */
public static final native void memmove (long /*int*/ dest, double[] src, long /*int*/ size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out critical
 * @param size cast=(size_t)
 */
public static final native void memmove (long /*int*/ dest, float[] src, long /*int*/ size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out critical
 * @param size cast=(size_t)
 */
public static final native void memmove (long /*int*/ dest, int[] src, long /*int*/ size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out critical
 * @param size cast=(size_t)
 */
public static final native void memmove (long /*int*/ dest, long[] src, long /*int*/ size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out critical
 * @param size cast=(size_t)
 */
public static final native void memmove (long /*int*/ dest, short[] src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(const void *),flags=no_out critical
 * @param size cast=(size_t)
 */
public static final native void memmove (byte[] dest, char[] src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove (byte[] dest, long /*int*/ src, long /*int*/ size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove (long /*int*/ dest, long /*int*/ src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove (char[] dest, long /*int*/ src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove (double[] dest, long /*int*/ src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove (float[] dest, long /*int*/ src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove (int[] dest, byte[] src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove (short[] dest, long /*int*/ src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove (int[] dest, long /*int*/ src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove (long[] dest, long /*int*/ src, long /*int*/ size);
/**
 * @param buffer cast=(void *),flags=critical
 * @param num cast=(size_t)
 */
public static final native long /*int*/ memset (long /*int*/ buffer, int c, long /*int*/ num);
public static final native int PTR_sizeof ();
/** @param s cast=(char *) */
public static final native int strlen (long /*int*/ s);
}
