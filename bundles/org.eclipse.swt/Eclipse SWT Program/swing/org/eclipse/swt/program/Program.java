/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.program;

 
import java.awt.Desktop;
import java.io.File;
import java.net.URI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.internal.swing.Compatibility;
import org.eclipse.swt.internal.swing.Utils;

/**
 * Instances of this class represent programs and
 * their associated file extensions in the operating
 * system.
 */
public final class Program {
	String name;
	String command;
	String iconName;

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Program () {
}

/**
 * Finds the program that is associated with an extension.
 * The extension may or may not begin with a '.'.  Note that
 * a <code>Display</code> must already exist to guarantee that
 * this method returns an appropriate result.
 *
 * @param extension the program extension
 * @return the program or <code>null</code>
 *
 * @exception IllegalArgumentException <ul>
 *		<li>ERROR_NULL_ARGUMENT when extension is null</li>
 *	</ul>
 */
public static Program findProgram (String extension) {
	if (extension == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (extension.length () == 0) return null;
	if (extension.charAt (0) != '.') extension = "." + extension; //$NON-NLS-1$
  Utils.notImplemented(); return null;
}

/**
 * Answer all program extensions in the operating system.  Note
 * that a <code>Display</code> must already exist to guarantee
 * that this method returns an appropriate result.
 *
 * @return an array of extensions
 */
public static String [] getExtensions () {
  Utils.notImplemented(); return new String[0];
}

/**
 * Answers all available programs in the operating system.  Note
 * that a <code>Display</code> must already exist to guarantee
 * that this method returns an appropriate result.
 *
 * @return an array of programs
 */
public static Program [] getPrograms () {
  Utils.notImplemented(); return new Program[0];
}

/**
 * Launches the executable associated with the file in
 * the operating system.  If the file is an executable,
 * then the executable is launched.  Note that a <code>Display</code>
 * must already exist to guarantee that this method returns
 * an appropriate result.
 *
 * @param fileName the file or program name
 * @return <code>true</code> if the file is launched, otherwise <code>false</code>
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when fileName is null</li>
 * </ul>
 */
public static boolean launch (String fileName) {
	if (fileName == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if(!Compatibility.IS_JAVA_6_OR_GREATER) {
	  if(!Compatibility.IS_JAVA_5_OR_GREATER) {
	    try {
	      Runtime.getRuntime().exec(fileName);
	      return true;
	    } catch(Exception e) {
	    }
	    return false;
	  }
	  ProcessBuilder pb = new ProcessBuilder(new String[] {fileName});
	  try {
	    pb.start();
	    return true;
	  } catch(Exception e) {
	  }
	  return false;
	}
	Desktop desktop = Desktop.getDesktop();
  try {
    desktop.open(new File(fileName));
    return true;
  } catch(Exception e) {
    try {
      URI uri = new URI(fileName);
      if(fileName.startsWith("mailto:")) {
        desktop.mail(uri);
      } else if(fileName.startsWith("http:") || fileName.startsWith("https:") || fileName.startsWith("ftp:")) {
        desktop.browse(uri);
      }
    } catch(Exception ex) {
    }
  }
  return false;
}

/**
 * Executes the program with the file as the single argument
 * in the operating system.  It is the responsibility of the
 * programmer to ensure that the file contains valid data for 
 * this program.
 *
 * @param fileName the file or program name
 * @return <code>true</code> if the file is launched, otherwise <code>false</code>
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when fileName is null</li>
 * </ul>
 */
public boolean execute (String fileName) {
	if (fileName == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
  if (fileName == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
  if(!Compatibility.IS_JAVA_5_OR_GREATER) {
    try {
      Runtime.getRuntime().exec(fileName);
      return true;
    } catch(Exception e) {
    }
    return false;
  }
  ProcessBuilder pb = new ProcessBuilder(new String[] {fileName});
  try {
    pb.start();
    return true;
  } catch(Exception e) {
    return false;
  }
}

/**
 * Returns the receiver's image data.  This is the icon
 * that is associated with the receiver in the operating
 * system.
 *
 * @return the image data for the program, may be null
 */
public ImageData getImageData () {
  Utils.notImplemented(); return null;
//	int nIconIndex = 0;
//	String fileName = iconName;
//	int index = iconName.indexOf (',');
//	if (index != -1) {
//		fileName = iconName.substring (0, index);
//		String iconIndex = iconName.substring (index + 1, iconName.length ()).trim ();
//		try {
//			nIconIndex = Integer.parseInt (iconIndex);
//		} catch (NumberFormatException e) {}
//	}
//	/* Use the character encoding for the default locale */
//	TCHAR lpszFile = new TCHAR (0, fileName, true);
//	int [] phiconSmall = new int[1], phiconLarge = null;
//	OS.ExtractIconEx (lpszFile, nIconIndex, phiconLarge, phiconSmall, 1);
//	if (phiconSmall [0] == 0) return null;
//	Image image = Image.win32_new (null, SWT.ICON, phiconSmall[0]);
//	ImageData imageData = image.getImageData ();
//	image.dispose ();
//	return imageData;
}

/**
 * Returns the receiver's name.  This is as short and
 * descriptive a name as possible for the program.  If
 * the program has no descriptive name, this string may
 * be the executable name, path or empty.
 *
 * @return the name of the program
 */
public String getName () {
	return name;
}

/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param other the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode()
 */
public boolean equals(Object other) {
	if (this == other) return true;
	if (other instanceof Program) {
		final Program program = (Program) other;
		return name.equals(program.name) && command.equals(program.command)
			&& iconName.equals(program.iconName);
	}
	return false;
}

/**
 * Returns an integer hash code for the receiver. Any two 
 * objects that return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals(Object)
 */
public int hashCode() {
	return name.hashCode() ^ command.hashCode() ^ iconName.hashCode();
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the program
 */
public String toString () {
	return "Program {" + name + "}"; //$NON-NLS-1$ //$NON-NLS-2$
}

}
