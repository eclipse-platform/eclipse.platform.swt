/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import org.apache.tools.ant.*;

public class SwtJniGen extends Task {

	/* Attributes */
	String classpath;
	String outputdir;
	
	public SwtJniGen() {
	}
	
	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}
	
	public void setOutputdir(String outputdir) {
		this.outputdir = outputdir;
	}
	
	public void execute() throws BuildException {
		try {
			System.out.println(new java.io.File(".").getAbsolutePath());
			JNIGeneratorApp.main(new String[] {JNIGeneratorApp.getDefaultMainClass(), outputdir, classpath});
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
}
