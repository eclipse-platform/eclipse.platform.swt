/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * Portion Copyright (c) 2009-2010 compeople AG (http://www.compeople.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Compeople AG	- QtJambi/Qt based implementation for Windows/Mac OS X/Linux
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.ArrayList;
import java.util.List;

import com.trolltech.qt.gui.QFileDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;

/**
 * Instances of this class allow the user to navigate the file system and select
 * or enter a file name.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SAVE, OPEN, MULTI</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles SAVE and OPEN may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * SWT implementation.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#filedialog">FileDialog
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class FileDialog extends Dialog {
	String[] filterNames = new String[0];
	String[] filterExtensions = new String[0];
	String[] fileNames = new String[0];
	String filterPath = "", fileName = "";//$NON-NLS-1$ //$NON-NLS-2$
	int filterIndex = 0;
	boolean overwrite = false;
	static final String FILTER = "*.*";//$NON-NLS-1$
	QFileDialog fd;

	/**
	 * Constructs a new instance of this class given only its parent.
	 * 
	 * @param parent
	 *            a shell which will be the parent of the new instance
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 */
	public FileDialog(Shell parent) {
		this(parent, SWT.APPLICATION_MODAL);
	}

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * 
	 * @param parent
	 *            a shell which will be the parent of the new instance
	 * @param style
	 *            the style of dialog to construct
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 */
	public FileDialog(Shell parent, int style) {
		super(parent, checkStyle(parent, style));
		checkSubclass();
		fd = new QFileDialog();
		if ((style & SWT.SAVE) != 0) {
			fd.setFileMode(QFileDialog.FileMode.AnyFile);
		} else if ((style & SWT.MULTI) != 0) {
			fd.setFileMode(QFileDialog.FileMode.ExistingFiles);
		} else {
			fd.setFileMode(QFileDialog.FileMode.ExistingFile);
		}
	}

	/**
	 * Returns the path of the first file that was selected in the dialog
	 * relative to the filter path, or an empty string if no such file has been
	 * selected.
	 * 
	 * @return the relative path of the file
	 */
	public String getFileName() {
		String[] files = getSplittedFileNames();
		if (files.length == 1) {
			return files[0];
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * Returns a (possibly empty) array with the paths of all files that were
	 * selected in the dialog relative to the filter path.
	 * 
	 * @return the relative paths of the files
	 */
	public String[] getFileNames() {
		return getSplittedFileNames();
	}

	private String[] getSplittedFileNames() {
		String[] split = { null, null };
		List<String> files = fd.selectedFiles();

		if (!files.isEmpty()) {
			for (int i = 0; i < files.size(); i++) {
				String file = files.get(i);
				split = file.split("/"); //$NON-NLS-1$

				if (split != null) {
					files.set(i, split[split.length - 1]);
				}
			}
		}
		return files.toArray(new String[] {});
	}

	/**
	 * Returns the file extensions which the dialog will use to filter the files
	 * it shows.
	 * 
	 * @return the file extensions filter
	 */
	@SuppressWarnings("nls")
	public String[] getFilterExtensions() {
		List<String> filters = fd.nameFilters();
		ArrayList<String> result = new ArrayList<String>();
		String[] split1 = null;
		String[] split2 = null;
		/*
		 * just get the extensions out of the nameFilters, not the names (split
		 * between "(" and ")"
		 */
		for (String filter : filters) {
			split1 = filter.split("\\(");
			split2 = split1[1].split("\\)");
			result.add(split2[0]);
		}

		/*
		 * replace all " " by ";" because that's the way swt returns the
		 * extensions
		 */
		for (String r : result) {
			r = r.replaceAll(" ", ";");
		}

		return result.toArray(new String[] {});
	}

	/**
	 * Get the 0-based index of the file extension filter which was selected by
	 * the user, or -1 if no filter was selected.
	 * <p>
	 * This is an index into the FilterExtensions array and the FilterNames
	 * array.
	 * </p>
	 * 
	 * @return index the file extension filter index
	 * 
	 * @see #getFilterExtensions
	 * @see #getFilterNames
	 * 
	 * @since 3.4
	 */
	public int getFilterIndex() {
		//TODO see Bug 166
		return filterIndex;
	}

	/**
	 * Returns the names that describe the filter extensions which the dialog
	 * will use to filter the files it shows.
	 * 
	 * @return the list of filter names
	 */
	@SuppressWarnings("nls")
	public String[] getFilterNames() {
		List<String> filters = fd.nameFilters();
		ArrayList<String> result = new ArrayList<String>();
		String[] name = null;

		/*
		 * just get the names out of the nameFilters, not the extensions(split
		 * between "("
		 */
		for (String filter : filters) {
			name = filter.split("\\(");
			result.add(name[0]);
		}

		return result.toArray(new String[] {});
	}

	/**
	 * Returns the directory path that the dialog will use, or an empty string
	 * if this is not set. File names in this path will appear in the dialog,
	 * filtered according to the filter extensions.
	 * 
	 * @return the directory path string
	 * 
	 * @see #setFilterExtensions
	 */
	public String getFilterPath() {
		return fd.directory().path();
	}

	/**
	 * Returns the flag that the dialog will use to determine whether to prompt
	 * the user for file overwrite if the selected file already exists.
	 * 
	 * @return true if the dialog will prompt for file overwrite, false
	 *         otherwise
	 * 
	 * @since 3.4
	 */
	public boolean getOverwrite() {
		return fd.confirmOverwrite();
	}

	/**
	 * Makes the dialog visible and brings it to the front of the display.
	 * 
	 * @return a string describing the absolute path of the first selected file,
	 *         or null if the dialog was cancelled or an error occurred
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the dialog has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the dialog</li>
	 *                </ul>
	 */
	public String open() {
		fd.exec();
		java.util.List<String> files = fd.selectedFiles();
		fileName = null;
		fileNames = new String[files.size()];
		if (files.size() > 0) {
			fileNames[0] = fileName = files.get(0);
		}
		for (int i = 1; i < files.size(); i++) {
			fileNames[i] = files.get(i);
		}
		fd.disposeLater();
		return fileName;
	}

	/**
	 * Set the initial filename which the dialog will select by default when
	 * opened to the argument, which may be null. The name will be prefixed with
	 * the filter path when one is supplied.
	 * 
	 * @param string
	 *            the file name
	 */
	public void setFileName(String string) {
		fd.selectFile(string);
	}

	/**
	 * Set the file extensions which the dialog will use to filter the files it
	 * shows to the argument, which may be null.
	 * <p>
	 * The strings are platform specific. For example, on some platforms, an
	 * extension filter string is typically of the form "*.extension", where
	 * "*.*" matches all files. For filters with multiple extensions, use
	 * semicolon as a separator, e.g. "*.jpg;*.png".
	 * </p>
	 * 
	 * @param extensions
	 *            the file extension filter
	 * 
	 * @see #setFilterNames to specify the user-friendly names corresponding to
	 *      the extensions
	 */
	public void setFilterExtensions(String[] extensions) {
		filterExtensions = extensions;
		makeFilter();

	}

	/**
	 * Set the 0-based index of the file extension filter which the dialog will
	 * use initially to filter the files it shows to the argument.
	 * <p>
	 * This is an index into the FilterExtensions array and the FilterNames
	 * array.
	 * </p>
	 * 
	 * @param index
	 *            the file extension filter index
	 * 
	 * @see #setFilterExtensions
	 * @see #setFilterNames
	 * 
	 * @since 3.4
	 */
	public void setFilterIndex(int index) {
		//TODO see Bug 166
		filterIndex = index;
	}

	/**
	 * Sets the names that describe the filter extensions which the dialog will
	 * use to filter the files it shows to the argument, which may be null.
	 * <p>
	 * Each name is a user-friendly short description shown for its
	 * corresponding filter. The <code>names</code> array must be the same
	 * length as the <code>extensions</code> array.
	 * </p>
	 * 
	 * @param names
	 *            the list of filter names, or null for no filter names
	 * 
	 * @see #setFilterExtensions
	 */
	public void setFilterNames(String[] names) {
		filterNames = names;
		makeFilter();
	}

	/**
	 * constructs a filter for the fileDialog like this:
	 * "Images (*.png *.xpm *.jpg);;Text files (*.txt);;XML files (*.xml)"
	 * 
	 * @param names
	 * @return
	 */
	private void makeFilter() {
		java.util.ArrayList<String> filters = new java.util.ArrayList<String>();

		if (filterNames.length == 0 && filterExtensions.length == 0) {
			// nothing to do
		} else {
			/*
			 * If there are more filterNames than filterExtensions, just use as
			 * much filterNames as FilterExtensions.length. Ignore the rest.
			 */
			for (int i = 0; i < filterExtensions.length; i++) {
				if (filterNames.length >= i + 1) {
					/*
					 * If there's a matching filterName for the
					 * filterExtension-Field, the filterExtension-Field has to
					 * get rid of the ";". The ";" are only allowed, when the
					 * filterExtension hasn't a matching name.
					 */
					String ex = filterExtensions[i].replace(";", " "); //$NON-NLS-1$ //$NON-NLS-2$
					filters.add(filterNames[i] + "(" + ex + ")"); //$NON-NLS-1$//$NON-NLS-2$

				} else {
					/*
					 * just a filterExtension without a name
					 */
					filters.add(filterExtensions[i]);
				}
			}

			fd.setNameFilters(filters);
		}
	}

	/**
	 * Sets the directory path that the dialog will use to the argument, which
	 * may be null. File names in this path will appear in the dialog, filtered
	 * according to the filter extensions. If the string is null, then the
	 * operating system's default filter path will be used.
	 * <p>
	 * Note that the path string is platform dependent. For convenience, either
	 * '/' or '\' can be used as a path separator.
	 * </p>
	 * 
	 * @param string
	 *            the directory path
	 * 
	 * @see #setFilterExtensions
	 */
	public void setFilterPath(String string) {
		fd.setDirectory(string);
	}

	/**
	 * Sets the flag that the dialog will use to determine whether to prompt the
	 * user for file overwrite if the selected file already exists.
	 * 
	 * @param overwrite
	 *            true if the dialog will prompt for file overwrite, false
	 *            otherwise
	 * 
	 * @since 3.4
	 */
	public void setOverwrite(boolean overwrite) {
		fd.setConfirmOverwrite(overwrite);
	}
}
