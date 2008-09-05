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

public String getMetaData(String key, String defaultValue) {
	return data.getProperty(key, defaultValue);
}

public void setMetaData(String key, String value) {
	data.setProperty(key, value);
}

}
