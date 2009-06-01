/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public abstract class AbstractItem implements JNIItem {

	HashMap params;

static String[] split(String str, String separator) {
	return JNIGenerator.split(str, separator);
}

void checkParams() {
	if (params != null) return;
	parse(getMetaData());
}

public abstract String flatten();

public String[] getFlags() {
	Object flags = getParam("flags");
	if (flags == null) return new String[0];
	if (flags instanceof String[]) return (String[])flags;
	String[] result = split((String)flags, " ");
	setParam("flags", result);
	return result;
}

public boolean getFlag(String flag) {
	String[] flags = getFlags();
	for (int i = 0; i < flags.length; i++) {
		if (flags[i].equals(flag)) return true;
	}
	return false;
}

public abstract String getMetaData();

public Object getParam(String key) {
	checkParams();
	Object value = params.get(key);
	return value == null ? "" : value;
}

public boolean getGenerate() {
	return !getFlag(FLAG_NO_GEN);
}

public void setFlags(String[] flags) { 
	setParam("flags", flags);
}

public abstract void parse(String str);

public void setFlag(String flag, boolean value) {
	String[] flags = getFlags();
	HashSet set = new HashSet(Arrays.asList(flags));
	if (value) {
		set.add(flag);
	} else {
		set.remove(flag);
	}
	setFlags((String[])set.toArray(new String[set.size()]));
}

public void setGenerate(boolean value) {
	setFlag(FLAG_NO_GEN, !value);
}

public abstract void setMetaData(String value);

public void setParam(String key, Object value) {
	checkParams();
	params.put(key, value);
	setMetaData(flatten());
}
	
}
