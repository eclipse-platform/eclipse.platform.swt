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
package org.eclipse.swt.widgets;


import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.internal.swing.Compatibility;

/**
 * Instances of this class allow the user to navigate
 * the file system and select or enter a file name.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SAVE, OPEN, MULTI</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles SAVE and OPEN may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class FileDialog extends Dialog {
	String [] filterNames = new String [0];
	String [] filterExtensions = new String [0];
	String [] fileNames = new String [0];
	String filterPath = "", fileName = "";
	static final String FILTER = "*.*";

/**
 * Constructs a new instance of this class given only its parent.
 *
 * @param parent a shell which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public FileDialog (Shell parent) {
	this (parent, SWT.PRIMARY_MODAL);
}

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a shell which will be the parent of the new instance
 * @param style the style of dialog to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public FileDialog (Shell parent, int style) {
	super (parent, style);
	checkSubclass ();
}

/**
 * Returns the path of the first file that was
 * selected in the dialog relative to the filter path, or an
 * empty string if no such file has been selected.
 * 
 * @return the relative path of the file
 */
public String getFileName () {
	return fileName;
}

/**
 * Returns a (possibly empty) array with the paths of all files
 * that were selected in the dialog relative to the filter path.
 * 
 * @return the relative paths of the files
 */
public String [] getFileNames () {
	return fileNames;
}

/**
 * Returns the file extensions which the dialog will
 * use to filter the files it shows.
 *
 * @return the file extensions filter
 */
public String [] getFilterExtensions () {
	return filterExtensions;
}

/**
 * Returns the names that describe the filter extensions
 * which the dialog will use to filter the files it shows.
 *
 * @return the list of filter names
 */
public String [] getFilterNames () {
	return filterNames;
}

/**
 * Returns the directory path that the dialog will use, or an empty
 * string if this is not set.  File names in this path will appear
 * in the dialog, filtered according to the filter extensions.
 *
 * @return the directory path string
 * 
 * @see #setFilterExtensions
 */
public String getFilterPath () {
	return filterPath;
}

protected static String currentDirectoryPath;

/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return a string describing the absolute path of the first selected file,
 *         or null if the dialog was cancelled or an error occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
public String open () {
  final JFileChooser fileChooser = new JFileChooser(currentDirectoryPath);
  String title = getText();
  if(title != null && title.length() > 0) {
    fileChooser.setDialogTitle(title);
  }
  if(filterPath != null && filterPath.length() > 0) {
    fileChooser.setCurrentDirectory(new File(filterPath));
  }
  if(fileName != null && fileName.length() > 0) {
    fileChooser.setSelectedFile(new File(fileChooser.getCurrentDirectory().getAbsolutePath() + "/" + fileName));
  }
  fileName = "";
  fileNames = null;
  String fullPath = null;
  fileChooser.setMultiSelectionEnabled((style & SWT.MULTI) != 0);
  if(filterExtensions != null && filterExtensions.length > 0) {
    fileChooser.setAcceptAllFileFilterUsed(false);
    for(int i=0; i<filterExtensions.length; i++) {
      String filterExtension = filterExtensions[i];
      final String[] filters = filterExtensions[i].split(";");
      final String description = (filterNames == null || i >= filterNames.length)? filterExtension: filterNames[i];
      fileChooser.addChoosableFileFilter(new FileFilter() {
        public boolean accept(File f) {
          if(f.isDirectory()) {
            return true;
          }
          for(int i=0; i<filters.length; i++) {
            String filterExtension = filters[i].trim();
            String regExp;
            if(!Compatibility.IS_JAVA_5_OR_GREATER) {
              filterExtension = Pattern.compile("\\E").matcher(filterExtension).replaceAll("\\\\E");
              filterExtension = Pattern.compile("\\Q").matcher(filterExtension).replaceAll("\\\\Q");
              filterExtension = Pattern.compile("?").matcher(filterExtension).replaceAll("\\E.\\Q");
              filterExtension = Pattern.compile("*").matcher(filterExtension).replaceAll("\\E.*\\Q");
              regExp = "\\Q" + filterExtension;
            } else {
              regExp = "\\Q" + filterExtension.replace("\\E", "\\\\E").replace("\\Q", "\\\\Q").replace("?", "\\E.\\Q").replace("*", "\\E.*\\Q");
            }
            final Pattern pattern = Pattern.compile(regExp.toString());
            Matcher m = pattern.matcher(f.getName());
            if(m.matches()) {
              return true;
            }
          }
          return false;
        }
        public String getDescription() {
          return description;
        }
      });
      
    }
    fileChooser.setFileFilter(fileChooser.getChoosableFileFilters()[0]);
  }
  int returnValue;
  if((style & SWT.SAVE) != 0) {
    returnValue = fileChooser.showSaveDialog(getParent().handle);
  } else {
    returnValue = fileChooser.showOpenDialog(getParent().handle);
  }
  if(returnValue == JFileChooser.APPROVE_OPTION) {
    currentDirectoryPath = fileChooser.getSelectedFile().getParent();
    if((style & SWT.MULTI) != 0) {
      File[] selectedFiles = fileChooser.getSelectedFiles();
      fileNames = new String[selectedFiles.length];
      for(int i=0; i<fileNames.length; i++) {
        fileNames[i] = selectedFiles[i].getName();
      }
      if(selectedFiles.length > 0) {
        fullPath = selectedFiles[0].getParentFile().getAbsolutePath();
      }
    } else {
      File selectedFile = fileChooser.getSelectedFile();
      fileName = selectedFile.getName();
      fullPath = selectedFile.getAbsolutePath();
    }
    filterPath = new String(fullPath);
  }
  return fullPath;
}

/**
 * Set the initial filename which the dialog will
 * select by default when opened to the argument,
 * which may be null.  The name will be prefixed with
 * the filter path when one is supplied.
 * 
 * @param string the file name
 */
public void setFileName (String string) {
	fileName = string;
}

/**
 * Set the file extensions which the dialog will
 * use to filter the files it shows to the argument,
 * which may be null.
 * <p>
 * The strings are platform specific. For example, on
 * Windows, an extension filter string is typically of
 * the form "*.extension", where "*.*" matches all files.
 * </p>
 *
 * @param extensions the file extension filter
 * 
 * @see #setFilterNames to specify the user-friendly
 * names corresponding to the extensions
 */
public void setFilterExtensions (String [] extensions) {
	filterExtensions = extensions;
}

/**
 * Sets the the names that describe the filter extensions
 * which the dialog will use to filter the files it shows
 * to the argument, which may be null.
 * <p>
 * Each name is a user-friendly short description shown for
 * its corresponding filter. The <code>names</code> array must
 * be the same length as the <code>extensions</code> array.
 * </p>
 *
 * @param names the list of filter names, or null for no filter names
 * 
 * @see #setFilterExtensions
 */
public void setFilterNames (String [] names) {
	filterNames = names;
}

/**
 * Sets the directory path that the dialog will use
 * to the argument, which may be null. File names in this
 * path will appear in the dialog, filtered according
 * to the filter extensions. If the string is null,
 * then the operating system's default filter path
 * will be used.
 * <p>
 * Note that the path string is platform dependent.
 * For convenience, either '/' or '\' can be used
 * as a path separator.
 * </p>
 *
 * @param string the directory path
 * 
 * @see #setFilterExtensions
 */
public void setFilterPath (String string) {
	filterPath = string;
}

}
