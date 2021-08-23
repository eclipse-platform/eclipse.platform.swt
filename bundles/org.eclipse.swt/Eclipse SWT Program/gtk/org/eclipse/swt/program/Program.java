/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
package org.eclipse.swt.program;

import java.io.*;
import java.nio.file.*;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

/**
 * Instances of this class represent programs and
 * their associated file extensions in the operating
 * system.
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#program">Program snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class Program {
	String name = ""; //$NON-NLS-1$
	String command;
	String iconPath;

	/* GIO specific
	 * true if command expects a URI
	 * false if expects a path
	 */
	boolean gioExpectUri;

	static long modTime;
	static Map<String, List<String>> mimeTable;

	static final String PREFIX_HTTP = "http://"; //$NON-NLS-1$
	static final String PREFIX_HTTPS = "https://"; //$NON-NLS-1$

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Program() {
}

static String[] parseCommand(String cmd) {
	List<String> args = new ArrayList<>();
	int sIndex = 0;
	int eIndex;
	while (sIndex < cmd.length()) {
		/* Trim initial white space of argument. */
		while (sIndex < cmd.length() && Character.isWhitespace(cmd.charAt(sIndex))) {
			sIndex++;
		}
		if (sIndex < cmd.length()) {
			/* If the command is a quoted string */
			if (cmd.charAt(sIndex) == '"' || cmd.charAt(sIndex) == '\'') {
				/* Find the terminating quote (or end of line).
				 * This code currently does not handle escaped characters (e.g., " a\"b").
				 */
				eIndex = sIndex + 1;
				while (eIndex < cmd.length() && cmd.charAt(eIndex) != cmd.charAt(sIndex)) eIndex++;
				if (eIndex >= cmd.length()) {
					/* The terminating quote was not found
					 * Add the argument as is with only one initial quote.
					 */
					args.add(cmd.substring(sIndex, eIndex));
				} else {
					/* Add the argument, trimming off the quotes. */
					args.add(cmd.substring(sIndex + 1, eIndex));
				}
				sIndex = eIndex + 1;
			}
			else {
				/* Use white space for the delimiters. */
				eIndex = sIndex;
				while (eIndex < cmd.length() && !Character.isWhitespace(cmd.charAt(eIndex))) eIndex++;
				args.add(cmd.substring(sIndex, eIndex));
				sIndex = eIndex + 1;
			}
		}
	}

	return args.toArray(new String[args.size()]);
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
public static Program findProgram(String extension) {
	if (extension == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (extension.length() == 0) return null;
	if (extension.charAt(0) != '.') extension = "." + extension;
	String mimeType = gio_getMimeType(extension);
	if (mimeType == null) return null;
	return gio_getProgram(mimeType);
}

/**
 * Returns the receiver's image data.  This is the icon
 * that is associated with the receiver in the operating
 * system.
 *
 * @return the image data for the program, may be null
 */
public ImageData getImageData() {
	if (iconPath == null) return null;
	ImageData data = null;

	long gicon = OS.g_icon_new_for_string(Converter.javaStringToCString(iconPath), null);
	if (gicon != 0) {
		long pixbuf = 0;
		if (GTK.GTK4) {
			/*
			 * Specified size will not be respected as in order to get the pixbuf,
			 * we get the original image file which could be any size. Also, note
			 * that gtk_icon_theme_lookup_by_gicon never returns NULL, it will
			 * return a error icon if gicon can't be found.
			 */
			long icon_theme = GTK4.gtk_icon_theme_get_for_display(GDK.gdk_display_get_default()); // default display needs to exist
			long paintable = GTK4.gtk_icon_theme_lookup_by_gicon(icon_theme, gicon, 16, 1, GTK.GTK_TEXT_DIR_NONE, GTK.GTK_ICON_LOOKUP_FORCE_REGULAR);
			long file = GTK4.gtk_icon_paintable_get_file(paintable);
			long texture = GDK.gdk_texture_new_from_file(file, 0);
			pixbuf = GDK.gdk_pixbuf_get_from_texture(texture);

			OS.g_object_unref(texture);
			OS.g_object_unref(file);
			OS.g_object_unref(paintable);
		} else {
			long icon_theme = GTK3.gtk_icon_theme_get_default();
			long gicon_info = GTK3.gtk_icon_theme_lookup_by_gicon(icon_theme, gicon, 16/*size*/, 0);
			if (gicon_info != 0) {
				pixbuf = GTK3.gtk_icon_info_load_icon(gicon_info, null);
				OS.g_object_unref(gicon_info);
			}
		}

		// Unref gicon as we have retrieved the required pixbuf
		OS.g_object_unref(gicon);

		if (pixbuf != 0) {
			int stride = GDK.gdk_pixbuf_get_rowstride(pixbuf);
			long pixels = GDK.gdk_pixbuf_get_pixels(pixbuf);
			int height = GDK.gdk_pixbuf_get_height(pixbuf);
			int width = GDK.gdk_pixbuf_get_width(pixbuf);
			boolean hasAlpha = GDK.gdk_pixbuf_get_has_alpha(pixbuf);

			// Move pixbuf memory to srcData and unref pixbuf
			byte[] srcData = new byte[stride * height];
			C.memmove(srcData, pixels, srcData.length);
			OS.g_object_unref(pixbuf);

			if (hasAlpha) {
				PaletteData palette = new PaletteData(0xFF000000, 0xFF0000, 0xFF00);
				data = new ImageData(width, height, 32, palette, 4, srcData);
				data.bytesPerLine = stride;
				int s = 3, a = 0;
				byte[] alphaData = new byte[width * height];
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						alphaData[a++] = srcData[s];
						srcData[s] = 0;
						s+=4;
					}
				}
				data.alphaData = alphaData;
			} else {
				PaletteData palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
				data = new ImageData(width, height, 24, palette, 4, srcData);
				data.bytesPerLine = stride;
			}
		}
	}

	return data;
}

	static Map<String, List<String>> gio_getMimeInfo() {
		/*
		 * The file 'globs' contain the file extensions associated to the
		 * mime-types. Each line that has to be parsed corresponds to a
		 * different extension of a mime-type. The template of such line is -
		 * application/pdf:*.pdf
		 */
		Path path = Paths.get("/usr/share/mime/globs");
		long lastModified = 0;
		try {
			lastModified = Files.getLastModifiedTime(path).toMillis();
		} catch (IOException e) {
			// ignore and reparse the file
		}
		if (modTime != 0 && modTime == lastModified) {
			return mimeTable;
		} else {
			try {
				mimeTable = new HashMap<>();
				modTime = lastModified;
				for (String line : Files.readAllLines(path)) {
					int separatorIndex = line.indexOf(':');
					if (separatorIndex > 0) {
						List<String> mimeTypes = new ArrayList<>();
						String mimeType = line.substring(0, separatorIndex);
						String extensionFormat = line.substring(separatorIndex + 1);
						int extensionIndex = extensionFormat.indexOf('.');
						if (extensionIndex > 0) {
							String extension = extensionFormat.substring(extensionIndex);
							if (mimeTable.containsKey(extension)) {
								/*
								 * If mimeType already exists, it is required to
								 * update the existing key (mime-type) with the
								 * new extension.
								 */
								List<String> value = mimeTable.get(extension);
								mimeTypes.addAll(value);
							}
							mimeTypes.add(mimeType);
							mimeTable.put(extension, mimeTypes);
						}
					}
				}
				return mimeTable;
			} catch (IOException e) {
			}
		}
		return null;
	}

static String gio_getMimeType(String extension) {
	String mimeType = null;
	Map<String, List<String>> h = gio_getMimeInfo();
	if (h != null && h.containsKey(extension)) {
		List<String> mimeTypes = h.get(extension);
		mimeType = mimeTypes.get(0);
	}
	return mimeType;
}

static Program gio_getProgram(String mimeType) {
	Program program = null;
	byte[] mimeTypeBuffer = Converter.wcsToMbcs (mimeType, true);
	long application = OS.g_app_info_get_default_for_type (mimeTypeBuffer, false);
	if (application != 0) {
		program = gio_getProgram(application);
	}
	return program;
}

static Program gio_getProgram (long application) {
	Program program = new Program();
	int length;
	byte[] buffer;
	long applicationName = OS.g_app_info_get_name (application);
	if (applicationName != 0) {
		length = C.strlen (applicationName);
		if (length > 0) {
			buffer = new byte [length];
			C.memmove (buffer, applicationName, length);
			program.name = new String (Converter.mbcsToWcs (buffer));
		}
	}
	long applicationCommand = OS.g_app_info_get_executable (application);
	if (applicationCommand != 0) {
		length = C.strlen (applicationCommand);
		if (length > 0) {
			buffer = new byte [length];
			C.memmove (buffer, applicationCommand, length);
			program.command = new String (Converter.mbcsToWcs (buffer));
		}
	}
	program.gioExpectUri = OS.g_app_info_supports_uris(application);
	long icon = OS.g_app_info_get_icon(application);
	if (icon != 0) {
		long icon_name = OS.g_icon_to_string(icon);
		if (icon_name != 0) {
			length = C.strlen(icon_name);
			if (length > 0) {
				buffer = new byte[length];
				C.memmove(buffer, icon_name, length);
				program.iconPath = new String(Converter.mbcsToWcs(buffer));
			}
			OS.g_free(icon_name);
		}
	}
	return program.command != null ? program : null;
}

/**
 * Answers all available programs in the operating system.  Note
 * that a <code>Display</code> must already exist to guarantee
 * that this method returns an appropriate result.
 *
 * @return an array of programs
 */
public static Program[] getPrograms() {
	long applicationList = OS.g_app_info_get_all ();
	long list = applicationList;
	Program program;
	LinkedHashSet<Program> programs = new LinkedHashSet<>();
	while (list != 0) {
		long application = OS.g_list_data(list);
		if (application != 0) {
			//TODO: Should the list be filtered or not?
//			if (OS.g_app_info_should_show(application)) {
				program = gio_getProgram(application);
				if (program != null) programs.add(program);
//			}
				OS.g_object_unref(application);
		}
		list = OS.g_list_next(list);
	}
	if (applicationList != 0) OS.g_list_free(applicationList);
	return programs.toArray(new Program[programs.size()]);
}

static boolean isExecutable(String fileName) {
	byte[] fileNameBuffer = Converter.wcsToMbcs (fileName, true);
	if (OS.g_file_test(fileNameBuffer, OS.G_FILE_TEST_IS_DIR)) return false;
	if (!OS.g_file_test(fileNameBuffer, OS.G_FILE_TEST_IS_EXECUTABLE)) return false;
	long file = OS.g_file_new_for_path (fileNameBuffer);
	boolean result = false;
	if (file != 0) {
		byte[] buffer = Converter.wcsToMbcs ("*", true); //$NON-NLS-1$
		long fileInfo = OS.g_file_query_info(file, buffer, 0, 0, 0);
		if (fileInfo != 0) {
			long contentType = OS.g_file_info_get_content_type(fileInfo);
			if (contentType != 0) {
				byte[] exeType = Converter.wcsToMbcs ("application/x-executable", true); //$NON-NLS-1$
				result = OS.g_content_type_is_a(contentType, exeType);
				if (!result) {
					byte [] shellType = Converter.wcsToMbcs ("application/x-shellscript", true); //$NON-NLS-1$
					result = OS.g_content_type_equals(contentType, shellType);
				}
			}
			OS.g_object_unref(fileInfo);
		}
		OS.g_object_unref (file);
	}
	return result;
}

/**
 * GIO - Launch the default program for the given file.
 */
static boolean gio_launch(String fileName) {
	boolean result = false;
	byte[] fileNameBuffer = Converter.wcsToMbcs (fileName, true);
	long file = OS.g_file_new_for_commandline_arg (fileNameBuffer);
	if (file != 0) {
		long uri = OS.g_file_get_uri (file);
		if (uri != 0) {
			result = OS.g_app_info_launch_default_for_uri (uri, 0, 0);
			OS.g_free(uri);
		}
		OS.g_object_unref (file);
	}
	return result;
}

/**
 * GIO - Execute the program for the given file.
 */
boolean gio_execute(String fileName) {
	boolean result = false;
	byte[] commandBuffer = Converter.wcsToMbcs (command, true);
	byte[] nameBuffer = Converter.wcsToMbcs (name, true);
	long application = OS.g_app_info_create_from_commandline(commandBuffer, nameBuffer, gioExpectUri
				? OS.G_APP_INFO_CREATE_SUPPORTS_URIS : OS.G_APP_INFO_CREATE_NONE, 0);
	if (application != 0) {
		byte[] fileNameBuffer = Converter.wcsToMbcs (fileName, true);
		long file = 0;
		if (fileName.length() > 0) {
			if (OS.g_app_info_supports_uris (application)) {
				file = OS.g_file_new_for_uri (fileNameBuffer);
			} else {
				file = OS.g_file_new_for_path (fileNameBuffer);
			}
		}
		long list = 0;
		if (file != 0) list = OS.g_list_append (0, file);
		result = OS.g_app_info_launch (application, list, 0, 0);
		if (list != 0) {
			OS.g_list_free (list);
			OS.g_object_unref (file);
		}
		OS.g_object_unref (application);
	}
	return result;
}

/**
 * Answer all program extensions in the operating system.  Note
 * that a <code>Display</code> must already exist to guarantee
 * that this method returns an appropriate result.
 *
 * @return an array of extensions
 */
public static String[] getExtensions() {
	Map<String, List<String>> mimeInfo = gio_getMimeInfo();
	if (mimeInfo == null) return new String[0];
	/* Create a unique set of the file extensions. */
	List<String> extensions = new ArrayList<>(mimeInfo.keySet());
	/* Return the list of extensions. */
	return extensions.toArray(new String[extensions.size()]);
}

/**
 * Launches the operating system executable associated with the file or
 * URL (http:// or https://).  If the file is an executable then the
 * executable is launched.  Note that a <code>Display</code> must already
 * exist to guarantee that this method returns an appropriate result.
 *
 * @param fileName the file or program name or URL (http:// or https://)
 * @return <code>true</code> if the file is launched, otherwise <code>false</code>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when fileName is null</li>
 * </ul>
 */
public static boolean launch(String fileName) {
	return launch(fileName, null);
}

/**
 * Launches the operating system executable associated with the file or
 * URL (http:// or https://).  If the file is an executable then the
 * executable is launched. The program is launched with the specified
 * working directory only when the <code>workingDir</code> exists and
 * <code>fileName</code> is an executable.
 * Note that a <code>Display</code> must already exist to guarantee
 * that this method returns an appropriate result.
 *
 * @param fileName the file name or program name or URL (http:// or https://)
 * @param workingDir the name of the working directory or null
 * @return <code>true</code> if the file is launched, otherwise <code>false</code>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when fileName is null</li>
 * </ul>
 *
 * @since 3.6
 */
public static boolean launch (String fileName, String workingDir) {
	if (fileName == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (workingDir != null && isExecutable(fileName)) {
		try {
			Compatibility.exec (new String [] {fileName}, null, workingDir);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	if (gio_launch (fileName)) return true;
	int index = fileName.lastIndexOf ('.');
	if (index != -1) {
		String extension = fileName.substring (index);
		Program program = Program.findProgram (extension);
		if (program != null && program.execute (fileName)) return true;
	}
	String lowercaseName = fileName.toLowerCase ();
	if (lowercaseName.startsWith (PREFIX_HTTP) || lowercaseName.startsWith (PREFIX_HTTPS)) {
		Program program = Program.findProgram (".html"); //$NON-NLS-1$
		if (program == null) {
			program = Program.findProgram (".htm"); //$NON-NLS-1$
		}
		if (program != null && program.execute (fileName)) return true;
	}
	/* If the above launch attempts didn't launch the file, then try with exec().*/
	try {
		Compatibility.exec (new String [] {fileName}, null, workingDir);
		return true;
	} catch (IOException e) {
		return false;
	}
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
@Override
public boolean equals(Object other) {
	if (this == other) return true;
	if (!(other instanceof Program)) return false;
	Program program = (Program)other;
	return name.equals(program.name) && command.equals(program.command)
			&& gioExpectUri == program.gioExpectUri;
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
public boolean execute(String fileName) {
	if (fileName == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return gio_execute(fileName);
}

/**
 * Returns the receiver's name.  This is as short and
 * descriptive a name as possible for the program.  If
 * the program has no descriptive name, this string may
 * be the executable name, path or empty.
 *
 * @return the name of the program
 */
public String getName() {
	return name;
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
@Override
public int hashCode() {
	return name.hashCode() ^ command.hashCode();
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the program
 */
@Override
public String toString() {
	return "Program {" + name + "}";
}
}
