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

import java.io.*;
import java.util.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.FileSet;

public class SwtReplace extends Task {

	/* Attributes */
	String from;
	String to;
	Vector filesets = new Vector();
		
	public SwtReplace() {
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
    /**
     * Adds a set of files to preprocess.
     * @param set a set of files to preprocess
     */
    public void addFileset(FileSet set) {
        filesets.addElement(set);
    }
			
	public void execute() throws BuildException {
		try {
			for (int i = 0; i < filesets.size(); i++) {
				FileSet fs = (FileSet)filesets.elementAt(i);
				DirectoryScanner ds = fs.getDirectoryScanner(getProject());
				String[] srcFiles = ds.getIncludedFiles();
				for (int j = 0; j < srcFiles.length; j++) preProcess(ds.getBasedir().toString()+File.separator+srcFiles[j]);
			}
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
	
	public void preProcess(String src) throws Exception {
		String dst = src+".swt";
		File srcFile = new File(src);
		File dstFile = new File(dst);
		FileReader fr = new FileReader(srcFile);
		FileWriter fw = new FileWriter(dstFile);
		BufferedReader reader = new BufferedReader(fr);
		BufferedWriter writer = new BufferedWriter(fw);
		boolean modified = false;
		while (true) {
			String line = reader.readLine();
			if (line == null) break;

			int index;
			while ((index = line.lastIndexOf(from)) != -1) {
				modified = true;
				line = line.substring(0, index) + to + line.substring(index + from.length());
			}
			writer.write(line);
			writer.newLine();
		}
		reader.close();
		writer.close();
		if (modified) {
			srcFile.delete();
			dstFile.renameTo(srcFile);
		} else {
			dstFile.delete();
		}
	}


}
