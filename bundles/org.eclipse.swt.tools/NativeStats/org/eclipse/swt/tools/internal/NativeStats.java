/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.io.PrintStream;
import java.lang.reflect.Method;

/**
 * Instructions on how to use the NativeStats tool with a standlaone SWT example:
 * 
 * Compile the SWT native libraries defining the NATIVE_STATS flag.
 * 
 */
public class NativeStats {
	
public void dump(PrintStream ps) {
	dump("OS", ps);
	dump("ATK", ps);
	dump("CDE", ps);
	dump("GNOME", ps);
	dump("GTK", ps);
	dump("KDE", ps);
	dump("XPCOM", ps);
	dump("COM", ps);
}

public void dump(String className, PrintStream ps) {
	try {
		Class clazz = getClass();
		Method functionCount = clazz.getMethod(className + "_GetFunctionCount", new Class[0]);
		Method functionCallCount = clazz.getMethod(className + "_GetFunctionCallCount", new Class[]{int.class});
		Method functionName = clazz.getMethod(className + "_GetFunctionName", new Class[]{int.class});
		int count = ((Integer)functionCount.invoke(clazz, new Object[0])).intValue();
		ps.println(className);
		Object[] arg = new Object[1];
		for (int i = 0; i < count; i++) {
			arg[0] = new Integer(i);
			int ncalls = ((Integer)functionCallCount.invoke(clazz, arg)).intValue();
			if (ncalls > 0) {
				ps.print("\t");
				ps.print(functionName.invoke(clazz, arg));
				ps.print("=");
				ps.print(ncalls);
				ps.println();
			}
		}		
	} catch (Throwable e) {
//		e.printStackTrace(System.out);
	}
}

	
public static final native int OS_GetFunctionCount();
public static final native String OS_GetFunctionName(int index);
public static final native int OS_GetFunctionCallCount(int index);

public static final native int ATK_GetFunctionCount();
public static final native String ATK_GetFunctionName(int index);
public static final native int ATK_GetFunctionCallCount(int index);

public static final native int CDE_GetFunctionCount();
public static final native String CDE_GetFunctionName(int index);
public static final native int CDE_GetFunctionCallCount(int index);

public static final native int GNOME_GetFunctionCount();
public static final native String GNOME_GetFunctionName(int index);
public static final native int GNOME_GetFunctionCallCount(int index);

public static final native int GTK_GetFunctionCount();
public static final native String GTK_GetFunctionName(int index);
public static final native int GTK_GetFunctionCallCount(int index);

public static final native int KDE_GetFunctionCount();
public static final native String KDE_GetFunctionName(int index);
public static final native int KDE_GetFunctionCallCount(int index);

public static final native int XPCOM_GetFunctionCount();
public static final native String XPCOM_GetFunctionName(int index);
public static final native int XPCOM_GetFunctionCallCount(int index);

public static final native int COM_GetFunctionCount();
public static final native String COM_GetFunctionName(int index);
public static final native int COM_GetFunctionCallCount(int index);

}
