/*******************************************************************************
 * Copyright (c) 2008, 2018 IBM Corporation and others.
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

import java.util.*;

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
	StringBuilder buffer = new StringBuilder();
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
			StringBuilder valueBuffer = new StringBuilder();
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

@Override
public String[] getFlags() {
	Object flags = getParam("flags");
	if (flags == null) return new String[0];
	if (flags instanceof String[]) return (String[])flags;
	String[] result = split((String)flags, " ");
	setParam("flags", result);
	return result;
}

@Override
public boolean getFlag(String flag) {
	String[] flags = getFlags();
	for (int i = 0; i < flags.length; i++) {
		if (flags[i].equals(flag)) return true;
	}
	return false;
}

public abstract String getMetaData();

@Override
public Object getParam(String key) {
	checkParams();
	Object value = params.get(key);
	return value == null ? "" : value;
}

@Override
public boolean getGenerate() {
	return !getFlag(FLAG_NO_GEN);
}

@Override
public void setFlags(String[] flags) { 
	setParam("flags", flags);
}

public void parse(String str) {
	this.params = new HashMap<>();
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

@Override
public void setFlag(String flag, boolean value) {
	String[] flags = getFlags();
	HashSet<String> set = new HashSet<>(Arrays.asList(flags));
	if (value) {
		set.add(flag);
	} else {
		set.remove(flag);
	}
	setFlags(set.toArray(new String[set.size()]));
}

@Override
public void setGenerate(boolean value) {
	setFlag(FLAG_NO_GEN, !value);
}

public abstract void setMetaData(String value);

@Override
public void setParam(String key, Object value) {
	checkParams();
	params.put(key, value);
	setMetaData(flatten());
}
	
}
