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
import java.util.Set;

public abstract class ASTItem extends AbstractItem {

public String flatten() {
	checkParams();
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

}
