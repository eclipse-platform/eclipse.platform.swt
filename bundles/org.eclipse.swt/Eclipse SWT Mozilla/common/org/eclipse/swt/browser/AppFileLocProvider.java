/*******************************************************************************
 * Copyright (c) 2003, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import java.util.Vector;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.mozilla.*;

class AppFileLocProvider {
	XPCOMObject supports;
	XPCOMObject directoryServiceProvider;
	XPCOMObject directoryServiceProvider2;	
	int refCount = 0;
	String mozillaPath, profilePath, componentsPath;
	String[] pluginDirs;
	boolean isXULRunner;
	
	static final String SEPARATOR_OS = System.getProperty ("file.separator"); //$NON-NLS-1$
	static final String CHROME_DIR = "chrome"; //$NON-NLS-1$
	static final String COMPONENTS_DIR = "components"; //$NON-NLS-1$
	static final String HISTORY_FILE = "history.dat"; //$NON-NLS-1$
	static final String LOCALSTORE_FILE = "localstore.rdf"; //$NON-NLS-1$
	static final String MIMETYPES_FILE = "mimeTypes.rdf"; //$NON-NLS-1$
	static final String PLUGINS_DIR = "plugins"; //$NON-NLS-1$
	static final String USER_PLUGINS_DIR = ".mozilla" + SEPARATOR_OS + "plugins"; //$NON-NLS-1$ //$NON-NLS-2$
	static final String PREFERENCES_FILE = "prefs.js"; //$NON-NLS-1$

	static boolean IsSparc;
	static {
		String osName = System.getProperty ("os.name").toLowerCase (); //$NON-NLS-1$
		String osArch = System.getProperty ("os.arch").toLowerCase (); //$NON-NLS-1$
		IsSparc = (osName.startsWith ("sunos") || osName.startsWith ("solaris")) && osArch.startsWith("sparc"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
AppFileLocProvider (String path) {
	mozillaPath = path + SEPARATOR_OS;
	createCOMInterfaces ();
}

int AddRef () {
	refCount++;
	return refCount;
}

void createCOMInterfaces () {
	/* Create each of the interfaces that this object implements */
	supports = new XPCOMObject (new int[] {2, 0, 0}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
	};
	
	directoryServiceProvider = new XPCOMObject (new int[] {2, 0, 0, 3}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return getFile (args[0], args[1], args[2]);}
	};
		
	directoryServiceProvider2 = new XPCOMObject (new int[] {2, 0, 0, 3, 2}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return getFile (args[0], args[1], args[2]);}
		public int /*long*/ method4 (int /*long*/[] args) {return getFiles (args[0], args[1]);}
	};
}

void disposeCOMInterfaces () {
	if (supports != null) {
		supports.dispose ();
		supports = null;
	}	
	if (directoryServiceProvider != null) {
		directoryServiceProvider.dispose ();
		directoryServiceProvider = null;	
	}
	if (directoryServiceProvider2 != null) {
		directoryServiceProvider2.dispose ();
		directoryServiceProvider2 = null;	
	}	
}

int /*long*/ getAddress () {
	return directoryServiceProvider.getAddress ();
}

int QueryInterface (int /*long*/ riid, int /*long*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID ();
	XPCOM.memmove (guid, riid, nsID.sizeof);
	
	if (guid.Equals (nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {supports.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIDirectoryServiceProvider.NS_IDIRECTORYSERVICEPROVIDER_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {directoryServiceProvider.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIDirectoryServiceProvider2.NS_IDIRECTORYSERVICEPROVIDER2_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {directoryServiceProvider2.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	
	XPCOM.memmove (ppvObject, new int /*long*/[] {0}, C.PTR_SIZEOF);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}

int Release () {
	refCount--;
	if (refCount == 0) disposeCOMInterfaces ();
	return refCount;
}

void setComponentsPath (String path) {
	componentsPath = path;
}

void setProfilePath (String path) {
	profilePath = path;
	if (!Compatibility.fileExists (path, "")) { //$NON-NLS-1$
		int /*long*/[] result = new int /*long*/[1];
		nsEmbedString pathString = new nsEmbedString (path);
		int rc = XPCOM.NS_NewLocalFile (pathString.getAddress (), 1, result);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		if (result[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NULL_POINTER);
		pathString.dispose ();

		nsILocalFile file = new nsILocalFile (result [0]);
		rc = file.Create (nsILocalFile.DIRECTORY_TYPE, 0700);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		file.Release ();
	}
}

/* nsIDirectoryServiceProvider2 */

int getFiles (int /*long*/ prop, int /*long*/ _retval) {
	int size = XPCOM.strlen (prop);
	byte[] bytes = new byte[size];
	XPCOM.memmove (bytes, prop, size);
	String propertyName = new String (MozillaDelegate.mbcsToWcs (null, bytes));
	String[] propertyValues = null;

	if (propertyName.equals (XPCOM.NS_APP_PLUGINS_DIR_LIST)) {
		if (pluginDirs == null) {
			int index = 0;
			/* set the first value(s) to the MOZ_PLUGIN_PATH environment variable value if it's defined */
			int /*long*/ ptr = C.getenv (MozillaDelegate.wcsToMbcs (null, XPCOM.MOZILLA_PLUGIN_PATH, true));
			if (ptr != 0) {
				int length = C.strlen (ptr);
				byte[] buffer = new byte[length];
				C.memmove (buffer, ptr, length);
				String value = new String (MozillaDelegate.mbcsToWcs (null, buffer));
				if (value.length () > 0) {
					String separator = System.getProperty ("path.separator"); // $NON-NLS-1$
					Vector segments = new Vector ();
					int start, end = -1;
					do {
						start = end + 1;
						end = value.indexOf (separator, start);
						String segment;
						if (end == -1) {
							segment = value.substring (start);
						} else {
							segment = value.substring (start, end);
						}
						if (segment.length () > 0) segments.addElement (segment);
					} while (end != -1);
					int segmentsSize = segments.size ();
					pluginDirs = new String [segmentsSize + (IsSparc ? 1 : 2)];
					for (index = 0; index < segmentsSize; index++) {
						pluginDirs[index] = (String)segments.elementAt (index);
					}
				}
			}
			if (pluginDirs == null) {
				pluginDirs = new String[IsSparc ? 1 : 2];
			}

			/* set the next value to the GRE path + "plugins" */

			/*
			* Bug on Solaris SPARC.  Attempting to start the java plug-in fails with an
			* error indicating that PR_NewMonitor could not be found.  This is a well-
			* known problem that many other apps have also encountered, with no
			* resolution other than to remove this plug-in.  The Browser workaround is
			* to not add the directory containing this plug-in to the plug-in search path. 
			*/
			if (!IsSparc) {
				pluginDirs[index++] = mozillaPath + PLUGINS_DIR;
			}

			/* set the next value to the home directory + "/.mozilla/plugins" */
			pluginDirs[index++] = System.getProperty("user.home") + SEPARATOR_OS + USER_PLUGINS_DIR;
		}
		propertyValues = pluginDirs;
	}

	XPCOM.memmove(_retval, new int /*long*/[] {0}, C.PTR_SIZEOF);
	if (propertyValues != null) {
		int /*long*/[] result = new int /*long*/[1];
		nsISupports[] files = new nsISupports [propertyValues.length];
		int index = 0;
		for (int i = 0; i < propertyValues.length; i++) {
			nsEmbedString pathString = new nsEmbedString (propertyValues[i]);
			int rc = XPCOM.NS_NewLocalFile (pathString.getAddress (), 1, result);
			if (rc != XPCOM.NS_ERROR_FILE_UNRECOGNIZED_PATH) {
				/* value appears to be a valid pathname */
				if (rc != XPCOM.NS_OK) Mozilla.error (rc);
				if (result[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NULL_POINTER);

				nsILocalFile localFile = new nsILocalFile (result[0]);
				result[0] = 0;
				rc = localFile.QueryInterface (nsIFile.NS_IFILE_IID, result); 
				if (rc != XPCOM.NS_OK) Mozilla.error (rc);
				if (result[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NO_INTERFACE);
				localFile.Release ();

				nsIFile file = new nsIFile (result[0]);
				files[index++] = file;
			}
			pathString.dispose ();
			result[0] = 0;
		}

		if (index < propertyValues.length) {
			/* there were some invalid values so remove the trailing empty array slots */
			nsISupports[] temp = new nsISupports [index];
			System.arraycopy (files, 0, temp, 0, index);
			files = temp;
		}

		SimpleEnumerator enumerator = new SimpleEnumerator (files);
		enumerator.AddRef ();
		XPCOM.memmove (_retval, new int /*long*/[] {enumerator.getAddress ()}, C.PTR_SIZEOF);
		return XPCOM.NS_OK;
	}

	return XPCOM.NS_ERROR_FAILURE;
}	
	
/* nsIDirectoryServiceProvider implementation */

int getFile(int /*long*/ prop, int /*long*/ persistent, int /*long*/ _retval) {
	int size = XPCOM.strlen (prop);
	byte[] bytes = new byte[size];
	XPCOM.memmove (bytes, prop, size);
	String propertyName = new String (MozillaDelegate.mbcsToWcs (null, bytes));
	String propertyValue = null;

	if (propertyName.equals (XPCOM.NS_APP_HISTORY_50_FILE)) {
		propertyValue = profilePath + HISTORY_FILE;
	} else if (propertyName.equals (XPCOM.NS_APP_USER_MIMETYPES_50_FILE)) {
		propertyValue = profilePath + MIMETYPES_FILE;
	} else if (propertyName.equals (XPCOM.NS_APP_PREFS_50_FILE)) {
		propertyValue = profilePath + PREFERENCES_FILE;
	} else if (propertyName.equals (XPCOM.NS_APP_PREFS_50_DIR)) {
		propertyValue = profilePath;
	} else if (propertyName.equals (XPCOM.NS_APP_USER_CHROME_DIR)) {
		propertyValue = profilePath + CHROME_DIR;
	} else if (propertyName.equals (XPCOM.NS_APP_USER_PROFILE_50_DIR)) {
		propertyValue = profilePath;
	} else if (propertyName.equals (XPCOM.NS_APP_LOCALSTORE_50_FILE)) {
		propertyValue = profilePath + LOCALSTORE_FILE;
	} else if (propertyName.equals (XPCOM.NS_APP_CACHE_PARENT_DIR)) {
		propertyValue = profilePath;
	} else if (propertyName.equals (XPCOM.NS_OS_HOME_DIR)) {
		propertyValue = System.getProperty("user.home");	//$NON-NLS-1$
	} else if (propertyName.equals (XPCOM.NS_OS_TEMP_DIR)) {
		propertyValue = System.getProperty("java.io.tmpdir");	//$NON-NLS-1$
	} else if (propertyName.equals (XPCOM.NS_GRE_DIR)) {
		propertyValue = mozillaPath;
	} else if (propertyName.equals (XPCOM.NS_GRE_COMPONENT_DIR)) {
		propertyValue = componentsPath != null ? componentsPath : mozillaPath + COMPONENTS_DIR;
	} else if (propertyName.equals (XPCOM.NS_XPCOM_INIT_CURRENT_PROCESS_DIR)) {
		propertyValue = mozillaPath;
	} else if (propertyName.equals (XPCOM.NS_OS_CURRENT_PROCESS_DIR)) {
		propertyValue = mozillaPath;
	} else if (propertyName.equals (XPCOM.NS_XPCOM_COMPONENT_DIR)) {
		propertyValue = mozillaPath + COMPONENTS_DIR;
	} else if (propertyName.equals (XPCOM.NS_XPCOM_CURRENT_PROCESS_DIR)) {
		propertyValue = mozillaPath;
	} else if (propertyName.equals (XPCOM.NS_APP_PREF_DEFAULTS_50_DIR)) {
		/*
		* Answering a value for this property causes problems in Mozilla versions
		* < 1.7.  Unfortunately this property is queried early enough in the
		* Browser creation process that the Mozilla version being used is not
		* yet determined.  However it is known if XULRunner is being used or not.
		* 
		* For now answer a value for this property iff XULRunner is the GRE.
		* If the range of Mozilla versions supported by the Browser is changed
		* in the future to be >= 1.7 then this value can always be answered.  
		*/
		if (isXULRunner) propertyValue = profilePath;
	}

	XPCOM.memmove (persistent, new int[] {1}, 4); /* PRBool */
	XPCOM.memmove (_retval, new int /*long*/[] {0}, C.PTR_SIZEOF);
	if (propertyValue != null && propertyValue.length () > 0) {
		int /*long*/[] result = new int /*long*/[1];
		nsEmbedString pathString = new nsEmbedString (propertyValue);
		int rc = XPCOM.NS_NewLocalFile (pathString.getAddress (), 1, result);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		if (result[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NULL_POINTER);
		pathString.dispose ();
		
		nsILocalFile localFile = new nsILocalFile (result [0]);
		result[0] = 0;
	    rc = localFile.QueryInterface (nsIFile.NS_IFILE_IID, result); 
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		if (result[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NO_INTERFACE);

		XPCOM.memmove (_retval, new int /*long*/[] {result[0]}, C.PTR_SIZEOF);
		localFile.Release ();
		return XPCOM.NS_OK;
	}

	return XPCOM.NS_ERROR_FAILURE;
}		
}
