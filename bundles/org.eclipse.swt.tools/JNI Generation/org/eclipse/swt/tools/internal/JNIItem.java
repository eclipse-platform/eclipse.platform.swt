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
