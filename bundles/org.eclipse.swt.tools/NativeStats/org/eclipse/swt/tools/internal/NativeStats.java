/*******************************************************************************
 * Copyright (c) 2004, 2016 IBM Corporation and others.
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
package org.eclipse.swt.tools.internal;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Instructions on how to use the NativeStats tool with a standlaone SWT example:
 * 
 * 1) Compile the SWT native libraries defining the NATIVE_STATS flag (i.e. uncomment line in makefile).
 * 2) Add the following code around the sections of interest to dump the
 * native calls done in that section.
 * 
 * 		NativeStats stats = new NativeStats();
 * 		...
 * 		<code section>
 * 		...
 * 		stats.dumpDiff(System.out);
 * 
 * 3) Or add the following code at a given point to dump a snapshot of
 * the native calls done until that point.
 * 
 * 		new NativeStats().dumpSnapshot(System.out); 
 */
public class NativeStats {
	
	Map<String, NativeFunction[]> snapshot;
	
	final static String[] classes = new String[]{"OS", "ATK", "GTK", "XPCOM", "COM", "AGL", "Gdip", "GLX", "Cairo", "WGL"};

	
	public static class NativeFunction implements Comparable<Object> {
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
	@Override
	public int compareTo(Object func) {
		return ((NativeFunction)func).callCount - callCount;
	}
	}
	
public NativeStats() {
	snapshot = snapshot();
}
	
public Map<String, NativeFunction[]> diff() {
	Map<String, NativeFunction[]> newSnapshot = snapshot();
	for (Map.Entry<String, NativeFunction[]> entry : newSnapshot.entrySet()) {
		String className = entry.getKey();
		NativeFunction[] newFuncs = entry.getValue();
		NativeFunction[] funcs = snapshot.get(className);
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
	Map<String, NativeFunction[]> snapshot = new HashMap<>();
	snapshot(className, snapshot);
	dump(className, snapshot.get(className), ps);
}

public void dump(Map<String, NativeFunction[]> snapshot, PrintStream ps) {
	for (Map.Entry<String, NativeFunction[]> entry : snapshot.entrySet()) {
		String className = entry.getKey();
		dump(className, entry.getValue(), ps);
	}
}
	
void dump(String className, NativeFunction[] funcs, PrintStream ps) {
	if (funcs == null) return;
	Arrays.sort(funcs);
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

public Map<String, NativeFunction[]> snapshot() {
	Map<String, NativeFunction[]> snapshot = new HashMap<>();
	for (int i = 0; i < classes.length; i++) {
		String className = classes[i];
		snapshot(className, snapshot);
	}
	return snapshot;
}

public Map<String, NativeFunction[]> snapshot(String className, Map<String, NativeFunction[]> snapshot) {
	try {
		Class<? extends NativeStats> clazz = getClass();
		Method functionCount = clazz.getMethod(className + "_GetFunctionCount");
		Method functionCallCount = clazz.getMethod(className + "_GetFunctionCallCount", int.class);
		Method functionName = clazz.getMethod(className + "_GetFunctionName", int.class);
		int count = ((Integer)functionCount.invoke(clazz)).intValue();
		NativeFunction[] funcs = new NativeFunction[count];
		Object[] index = new Object[1];
		for (int i = 0; i < count; i++) {
			index[0] = Integer.valueOf(i);
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

public static final native int AGL_GetFunctionCount();
public static final native String AGL_GetFunctionName(int index);
public static final native int AGL_GetFunctionCallCount(int index);

public static final native int Gdip_GetFunctionCount();
public static final native String Gdip_GetFunctionName(int index);
public static final native int Gdip_GetFunctionCallCount(int index);

public static final native int GLX_GetFunctionCount();
public static final native String GLX_GetFunctionName(int index);
public static final native int GLX_GetFunctionCallCount(int index);

public static final native int GTK_GetFunctionCount();
public static final native String GTK_GetFunctionName(int index);
public static final native int GTK_GetFunctionCallCount(int index);

public static final native int XPCOM_GetFunctionCount();
public static final native String XPCOM_GetFunctionName(int index);
public static final native int XPCOM_GetFunctionCallCount(int index);

public static final native int COM_GetFunctionCount();
public static final native String COM_GetFunctionName(int index);
public static final native int COM_GetFunctionCallCount(int index);

public static final native int WGL_GetFunctionCount();
public static final native String WGL_GetFunctionName(int index);
public static final native int WGL_GetFunctionCallCount(int index);

public static final native int Cairo_GetFunctionCount();
public static final native String Cairo_GetFunctionName(int index);
public static final native int Cairo_GetFunctionCallCount(int index);

}
