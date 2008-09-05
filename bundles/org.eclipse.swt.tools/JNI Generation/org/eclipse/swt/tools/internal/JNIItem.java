package org.eclipse.swt.tools.internal;

public interface JNIItem extends Flags {
	
	public static final boolean GEN64 = true;

public String[] getFlags();

public boolean getFlag(String flag);

public Object getParam(String key);

public boolean getGenerate();

public void setFlags(String[] flags);

public void setFlag(String flag, boolean value);

public void setGenerate(boolean value);

public void setParam(String key, Object value);
		
}
