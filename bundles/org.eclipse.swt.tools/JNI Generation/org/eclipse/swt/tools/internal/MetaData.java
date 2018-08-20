/*******************************************************************************
 * Copyright (c) 2004, 2015 IBM Corporation and others.
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
import java.util.*;

public class MetaData {
	
	Properties data;

public MetaData(String mainClass) {
	data = new Properties();
	int index = 0;
	Class<?> clazz = getClass();
	int length = mainClass.length();
	while (index < length) {
		index = mainClass.indexOf('.', index);
		if (index == -1) index = length;
		try (InputStream is = clazz.getResourceAsStream(mainClass.substring(0, index) + ".properties")) {
			if (is != null) {
				data.load(is);
			}
		} catch (IOException e) {
		}
		index++;
	}
}

public MetaData(Properties data) {
	this.data = data;
}

public String getCopyright() {
	String copyright = getMetaData("swt_copyright", null);
	if (copyright == null) return "";
	if (copyright.length() == 0) return "";
	String end_year_tag = "%END_YEAR";
	int index = copyright.indexOf(end_year_tag);
	if (index != -1) {
		String temp = copyright.substring(0, index);
		temp += Calendar.getInstance().get(Calendar.YEAR);
		temp += copyright.substring(index + end_year_tag.length());
		copyright = temp;
	}
	return copyright;
}

public String getMetaData(String key, String defaultValue) {
	return data.getProperty(key, defaultValue);
}

public void setMetaData(String key, String value) {
	data.setProperty(key, value);
}

}
