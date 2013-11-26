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
import java.util.Set;

public abstract class AbstractItem implements JNIItem {

	HashMap<String, Object> params;

static String[] split(String str, String separator) {
	return JNIGenerator.split(str, separator);
}

void checkParams() {
	if (params != null) return;
	parse(getMetaData());
}

public String flatten() {
	checkParams();
	StringBuffer buffer = new StringBuffer();
	Set<String> set = params.keySet();
	String[] keys = set.toArray(new String[set.size()]);
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
			String quote = "";
			if (valueStr.indexOf(',') != -1) {
				quote = valueStr.indexOf('"') != -1 ? "'" : "\""; 
			}
			buffer.append(quote);
			buffer.append(valueStr);
			buffer.append(quote);
		}
	}
	return buffer.toString();
}

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

public void parse(String str) {
	this.params = new HashMap<String, Object>();
	int length = str.length();
	if (length == 0) return;
	int index = 0;
	while (index < length) {
		int equals = str.indexOf('=', index);
		if (equals ==  -1) {
			System.out.println("Error: " + str + " index=" + index + " length=" + length);
			break;
		}
		String key = str.substring(index, equals).trim();
		equals++;
		while (equals < length && Character.isWhitespace(str.charAt(equals))) equals++;
		char c = str.charAt(equals), ending = ',';
		switch (c) {
			case '"':
			case '\'':
				equals++;
				ending = c;
				break;
		}
		int end = equals;
		while (end < length && str.charAt(end) != ending) end++;
		String value = str.substring(equals, end).trim();
		setParam(key, value);
		if (ending != ',') {
			while (end < length && str.charAt(end) != ',') end++;
		}
		index = end + 1;
	}
}

public void setFlag(String flag, boolean value) {
	String[] flags = getFlags();
	HashSet<String> set = new HashSet<String>(Arrays.asList(flags));
	if (value) {
		set.add(flag);
	} else {
		set.remove(flag);
	}
	setFlags(set.toArray(new String[set.size()]));
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
