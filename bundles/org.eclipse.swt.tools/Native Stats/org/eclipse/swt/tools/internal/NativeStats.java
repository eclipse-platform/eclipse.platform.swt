package org.eclipse.swt.tools.internal;

public class NativeStats {

public static final native int GetClassCount();
public static final native int GetFunctionCount(int clazz);
public static final native String GetFunctionName(int clazz, int function);
public static final native int GetFunctionCallCount(int clazz, int function);
	
public static void dump(){
	int total = 0;
	int classCount = GetClassCount(); 
	for (int clazz=0; clazz<classCount; clazz++) {
		int funcCount = GetFunctionCount(clazz);
		for (int func=0; func<funcCount; func++) {
			int count = GetFunctionCallCount(clazz, func);
			if (count != 0){
				System.out.println(GetFunctionName(clazz, func) + " = " + count);
				total += count;
			}
		}
	}
	System.out.println("TOTAL: " + total);
}
}
