/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public abstract class ItemData {

	HashMap params;

public ItemData(String str) {
	parse(str);
}

static String[] split(String str, String separator) {
	StringTokenizer tk = new StringTokenizer(str, separator);
	ArrayList result = new ArrayList();
	while (tk.hasMoreElements()) {
		result.add(tk.nextElement());
	}
	return (String[])result.toArray(new String[result.size()]);
}

public String[] getFlags() {
	Object flags = getParam("flags");
	if (flags == null) return new String[0];
	if (flags instanceof String[]) return (String[])flags;
	String[] result = split((String)flags, " ");
	params.put("flags", result);
	return result;
}

public boolean getFlag(String flag) {
	String[] flags = getFlags();
	for (int i = 0; i < flags.length; i++) {
		if (flags[i].equals(flag)) return true;
	}
	return false;
}

public Object getParam(String key) {
	Object value = params.get(key);
	return value == null ? "" : value;
}

public boolean getGenerate() {
	return !getFlag("no_gen");
}

public void parse(String str) {
	this.params = new HashMap();
	if (str.length() == 0) return;
	String[] params = split(str, ",");
	for (int i = 0; i < params.length; i++) {
		String param = params[i];
		int equals = param.indexOf('=');
		if (equals ==  -1) {
			System.out.println("Error: " + str + " param " + param);
		}
		String key = param.substring(0, equals).trim();
		String value = param.substring(equals + 1).trim();
		setParam(key, value);
	}
}

public void setFlags(String[] flags) { 
	setParam("flags", flags);
}

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
	setFlag("no_gen", !value);
}

public void setParam(String key, Object value) {
	params.put(key, value);
}

public String toString() {
	StringBuffer buffer = new StringBuffer();
	Set set = params.keySet();
	String[] keys = (String[])set.toArray(new String[set.size()]);
	Arrays.sort(keys);
	for (int j = 0; j < keys.length; j++) {
		String key = keys[j];
		Object value = params.get(key);
		String valueStr = "";
		if (value instanceof String) {
			valueStr = (String)value;
		} else if (value instanceof String[]) {
			String[] values = (String[])value;
			StringBuffer valueBuffer = new StringBuffer();
			for (int i = 0; i < values.length; i++) {
				if (i != 0) valueBuffer.append(" ");
				valueBuffer.append(values[i]);
			}
			valueStr = valueBuffer.toString();
		} else {
			valueStr = value.toString();
		}
		if (valueStr.length() > 0) {
			if (buffer.length() != 0) buffer.append(",");
			buffer.append(key);
			buffer.append("=");
			buffer.append(valueStr);
		}
	}
	return buffer.toString();
}

}
