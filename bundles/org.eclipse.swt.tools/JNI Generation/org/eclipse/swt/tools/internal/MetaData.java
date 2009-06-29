/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Calendar;

public class MetaData {
	
	Properties data;

public MetaData(String mainClass) {
	data = new Properties();
	int index = 0;
	Class clazz = getClass();
	int length = mainClass.length();
	while (index < length) {
		index = mainClass.indexOf('.', index);
		if (index == -1) index = length;
		InputStream is = clazz.getResourceAsStream(mainClass.substring(0, index) + ".properties");
		if (is != null) {
			try {
				data.load(is);
			} catch (IOException e) {
			} finally {
				try {
					is.close();
				} catch (IOException e) {}
			}
			
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
