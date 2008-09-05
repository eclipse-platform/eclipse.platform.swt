package org.eclipse.swt.tools.internal;

public interface JNIType {

public boolean isPrimitive();

public boolean isArray();

public JNIType getComponentType();

public boolean isType(String type);

public String getName();

public String getSimpleName();

public String getTypeSignature(boolean define);

public String getTypeSignature1(boolean define);

public String getTypeSignature2(boolean define);

public String getTypeSignature3(boolean define);

public String getTypeSignature4(boolean define, boolean struct);

}
