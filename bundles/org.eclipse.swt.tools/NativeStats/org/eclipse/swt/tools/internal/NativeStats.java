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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Instructions on how to use the NativeStats tool with a standlaone SWT example:
 * 
 * 1) Compile the SWT native libraries defining the NATIVE_STATS flag.
 * 2) Add the following code around the sections of interest to dump the
 * native calls done in that section.
 * 
 * NativeStats stats = new NativeStats();
 * ...
 * <code section>
 * ...
 * starts.dumpDiff(System.out);
 * 
 * 3) Or add the following code at given points to dump a snapshot of
 * the native calls done until that point.
 * 
 * new NativeStats().dumpSnapshot(System.out); 
 */
public class NativeStats {
	
	Hashtable snapshot;
	
	final static String[] classes = new String[]{"OS", "ATK", "CDE", "GNOME", "GTK", "KDE", "XPCOM", "COM"};

	
	public static class NativeFunction {
		String name;
		int callCount;
		
	public NativeFunction(String name, int callCount) {
		this.name = name;
		this.callCount = callCount;
	}

	void subtract(NativeFunction func) {
		this.callCount -= func.callCount;
	}

	public int getCallCount() {
		return callCount;
	}

	public String getName() {
		return name;
	}
	}
	
public NativeStats() {
	snapshot = snapshot();
}
	
public Hashtable diff() {
	Hashtable newSnapshot = snapshot();
	Enumeration keys = newSnapshot.keys();
	while (keys.hasMoreElements()) {
		String className = (String)keys.nextElement();
		NativeFunction[] newFuncs = (NativeFunction[])newSnapshot.get(className);
		NativeFunction[] funcs = (NativeFunction[])snapshot.get(className);
		if (funcs != null) {
			for (int i = 0; i < newFuncs.length; i++) {
				newFuncs[i].subtract(funcs[i]);
			}
		}
	}
	return newSnapshot;
}

public void dumpDiff(PrintStream ps) {
	dump(diff(), ps);
}

public void dumpSnapshot(PrintStream ps) {
	dump(snapshot(), ps);
}

public void dumpSnapshot(String className, PrintStream ps) {
	Hashtable snapshot = new Hashtable();
	snapshot(className, snapshot);
	dump(className, (NativeFunction[])snapshot.get(className), ps);
}

public void dump(Hashtable snapshot, PrintStream ps) {
	Enumeration keys = snapshot.keys();
	while (keys.hasMoreElements()) {
		String className = (String)keys.nextElement();
		dump(className, (NativeFunction[])snapshot.get(className), ps);
	}
}
	
void dump(String className, NativeFunction[] funcs, PrintStream ps) {
	if (funcs == null) return;
	Arrays.sort(funcs, new Comparator() {
		public int compare(Object a, Object b) {
			return ((NativeFunction)b).getCallCount() - ((NativeFunction)a).getCallCount();
		}
	});
	int total = 0;
	for (int i = 0; i < funcs.length; i++) {
		NativeFunction func = funcs[i];
		total += func.getCallCount();
	}
	ps.print(className);
	ps.print("=");
	ps.print(total);
	ps.println();
	for (int i = 0; i < funcs.length; i++) {
		NativeFunction func = funcs[i];
		if (func.getCallCount() > 0) {
			ps.print("\t");
			ps.print(func.getName());
			ps.print("=");
			ps.print(func.getCallCount());
			ps.println();
		}
	}
}

public void reset() {
	snapshot = snapshot(); 
}

public Hashtable snapshot() {
	Hashtable snapshot = new Hashtable();
	for (int i = 0; i < classes.length; i++) {
		String className = classes[i];
		snapshot(className, snapshot);
	}
	return snapshot;
}

public Hashtable snapshot(String className, Hashtable snapshot) {
	try {
		Class clazz = getClass();
		Method functionCount = clazz.getMethod(className + "_GetFunctionCount", new Class[0]);
		Method functionCallCount = clazz.getMethod(className + "_GetFunctionCallCount", new Class[]{int.class});
		Method functionName = clazz.getMethod(className + "_GetFunctionName", new Class[]{int.class});
		int count = ((Integer)functionCount.invoke(clazz, new Object[0])).intValue();
		NativeFunction[] funcs = new NativeFunction[count];
		Object[] index = new Object[1];
		for (int i = 0; i < count; i++) {
			index[0] = new Integer(i);
			int callCount = ((Integer)functionCallCount.invoke(clazz, index)).intValue();
			String name = (String)functionName.invoke(clazz, index);
			funcs[i] = new NativeFunction(name, callCount);
		}
		snapshot.put(className, funcs);
	} catch (Throwable e) {
//		e.printStackTrace(System.out);
	}
	return snapshot;
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
