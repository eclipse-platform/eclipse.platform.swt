/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

public class C extends Platform {

	static {
		Library.loadLibrary ("swt"); //$NON-NLS-1$
	}

	public static final int PTR_SIZEOF = PTR_sizeof ();

/** @param ptr cast=(void *) */
public static final native void free (long ptr);
/** @param env cast=(const char *) */
public static final native long getenv (byte[] env);
/**
 * @param env cast=(const char *)
 * @param value cast=(const char *)
 */
public static final native int setenv (byte[] env, byte[] value, int overwrite);
public static final native long malloc (long size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out critical
 * @param size cast=(size_t)
 */
public static final native void memmove (long dest, byte[] src, long size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out critical
 * @param size cast=(size_t)
 */
public static final native void memmove (long dest, char[] src, long size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out critical
 * @param size cast=(size_t)
 */
public static final native void memmove (long dest, double[] src, long size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out critical
 * @param size cast=(size_t)
 */
public static final native void memmove (long dest, float[] src, long size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out critical
 * @param size cast=(size_t)
 */
public static final native void memmove (long dest, int[] src, long size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out critical
 * @param size cast=(size_t)
 */
public static final native void memmove (long dest, long[] src, long size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out critical
 * @param size cast=(size_t)
 */
public static final native void memmove (long dest, short[] src, long size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(const void *),flags=no_out critical
 * @param size cast=(size_t)
 */
public static final native void memmove (byte[] dest, char[] src, long size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove (byte[] dest, long src, long size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove (long dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove (char[] dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove (double[] dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove (float[] dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove (int[] dest, byte[] src, long size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove (short[] dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove (int[] dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove (long[] dest, long src, long size);
/**
 * @param buffer cast=(void *),flags=critical
 * @param num cast=(size_t)
 */
public static final native long memset (long buffer, int c, long num);
public static final native int PTR_sizeof ();
/** @param s cast=(char *) */
public static final native int strlen (long s);
}
