/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;
import java.io.*;
import java.util.*;

/* Produces the java classes mapping to XPCOM Mozilla objects */
public class MozillaGenerator {
	FileReader r = null;
	FileWriter w = null;
	int maxLines = 1000;
	int cntLines = 0;
	int n = 0;
	String[] b = null;
	String body = null;
	int nMethods = 0;
	String uuidName;
	String uuidValue;
	String className;
	String parentName;
	String[] constantNames;
	String[] constantValues;
	String[] methodNames;
	String[][] argTypes;
	String[][] argNames;
	String bodyOrder;
	Hashtable vtbls;

	static boolean DEBUG = false;

	// Contains the characters found before a method name
	// Useful to extract the method name. e.g.
	//  NS_IMETHOD QueryInterface(const nsIID & uuid, void * *result) = 0;
	//  NS_IMETHOD_(nsrefcnt) AddRef(void) = 0;
	// method name follows: QueryInterface, AddRef etc.
	static String[] BEFORE_METHOD_NAME = {
		"  NS_IMETHOD ",
		"  NS_IMETHOD_(nsrefcnt) ",
		"  NS_IMETHOD_(void *) ",
		"  NS_IMETHOD_(void) ",
		"  NS_IMETHOD_(nsresult) ",
		"  NS_SCRIPTABLE NS_IMETHOD ",
		"  NS_SCRIPTABLE NS_IMETHOD_(nsrefcnt) ",
		"  NS_SCRIPTABLE NS_IMETHOD_(void *) ",
		"  NS_SCRIPTABLE NS_IMETHOD_(void) ",
		"  NS_SCRIPTABLE NS_IMETHOD_(nsresult) ",
	};
	static String NO_SUPER_CLASS = "SWT_NO_SUPER_CLASS";
	
	static String[][] TYPES_C2JAVA = { 
		{ "PRBool *", "int[]" },
		{ "nsIID &", "nsID" },
		{ "nsCID &", "nsID" },
		{ "nsCID * *", "int /*long*/" }, // nsID[] not supported by jnigen
		{ "* *", "int /*long*/[]" },
		{ "**", "int /*long*/[]" },
		{ "* &", "int /*long*/[]" },
		{ "PRUint32 *", "int[]" }, 
		{ "PRInt32 *", "int[]" },
		{ "PRInt64 *", "long[]" },
		{ "PRUnichar *", "char[]" },
		{ "char *", "byte[]" },
		{ "float *", "float[]" },
		{ "PRUint16 *", "short[]" },
		{ "nativeWindow *", "int /*long*/[]" },
		{ "nsWriteSegmentFun", "int /*long*/" },
		{ "nativeWindow", "int /*long*/" },

		{ "*", "int /*long*/" }, // c type containing one or more * (and any other character, and did not match previous patterns) is a simple pointer
		{ "&", "int /*long*/" }, 

		{ "PRUint32", "int" },
		{ "PRInt32", "int" }, 
		{ "PRInt64", "long" }, 
		{ "nsresult", "int" },
		{ "PRBool", "int" },
		{ "float", "float" },
		{ "PRUint16", "short" },
		{ "size_t", "int" },
		};

	static String GECKO = "/bluebird/teamswt/swt-builddir/mozilla/1.4/linux_gtk2/mozilla/dist/include/";
	static String TARGET_FOLDER = "/bluebird/teamswt/chrisx/amd64/workspace/org.eclipse.swt/Eclipse SWT Mozilla/common/org/eclipse/swt/internal/mozilla/";
	static String[] XPCOM_HEADERS = {
		"profile/nsIProfile.h",
		"widget/nsIAppShell.h",
		"widget/nsIBaseWindow.h",
		"xpcom/nsIComponentManager.h",
		"xpcom/nsIComponentRegistrar.h",
		"webbrwsr/nsIContextMenuListener.h", 
		"docshell/nsIDocShell.h",
		"dom/nsIDOMEvent.h",
		"dom/nsIDOMMouseEvent.h",
		"dom/nsIDOMUIEvent.h",
		"dom/nsIDOMWindow.h",
		"uriloader/nsIDownload.h", 
		"webbrwsr/nsIEmbeddingSiteWindow.h",
		"xpcom/nsIFactory.h", 
		"xpcom/nsIFile.h",
		"helperAppDlg/nsIHelperAppLauncherDialog.h",
		"exthandler/nsIExternalHelperAppService.h", // contains nsIHelperAppLauncher
		"xpcom/nsIInputStream.h", 
		"xpcom/nsIInterfaceRequestor.h",
		"necko/nsIIOService.h", 
		"xpcom/nsILocalFile.h", 
		"xpcom/nsIMemory.h",
		"progressDlg/nsIProgressDialog.h",
		"windowwatcher/nsIPromptService.h", 
		"xpcom/nsIServiceManager.h",
		"xpcom/nsISupports.h", 
		"webbrwsr/nsITooltipListener.h", 
		"necko/nsIURI.h",
		"uriloader/nsIURIContentListener.h", 
		"xpcom/nsIWeakReference.h",
		"webbrwsr/nsIWebBrowser.h", 
		"webbrwsr/nsIWebBrowserChrome.h",
		"webbrwsr/nsIWebBrowserChromeFocus.h",
		"webbrwsr/nsIWebBrowserFocus.h", 
		"docshell/nsIWebNavigation.h",
		"uriloader/nsIWebProgress.h", 
		"uriloader/nsIWebProgressListener.h",
		"embed_base/nsIWindowCreator.h", 
		"windowwatcher/nsIWindowWatcher.h" };

	public static void main(String[] args) {
		MozillaGenerator x = new MozillaGenerator();
		for (int i = 0; i < XPCOM_HEADERS.length; i++)
			x.parse(GECKO + XPCOM_HEADERS[i], TARGET_FOLDER);
		x.outputVtblCall();
		System.out.println("done");
	}

	public MozillaGenerator() {
		vtbls = new Hashtable();
	}

	/** Write callbacks */
	public void write(String data) {
		if (DEBUG) {
			System.out.print(data);
			return;
		}
		try {
			w.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeLine() {
		if (DEBUG) {
			System.out.println();
			return;
		}
		write("\r\n");
	}

	public void writeLine(String data) {
		if (DEBUG) {
			System.out.println(data);
			return;
		}
		write(data + "\r\n");
	}

	public void writeCopyrights() {
		writeLine(COPYRIGHTS);
	}

	public void writePackageDeclaration() {
		writeLine(PACKAGE_DECLARATION);
	}

	public void writeClassDeclaration(String className, String parentName) {
		String line = "public class " + className;
		if (!parentName.equals(NO_SUPER_CLASS)) line += " extends " + parentName;
		line += " {";
		writeLine(line);
	}

	public void writeLastMethodId(String parentName, int nMethods) {
		String line = "\tstatic final int LAST_METHOD_ID = ";
		if (!parentName.equals(NO_SUPER_CLASS)) line += parentName + ".LAST_METHOD_ID + " + nMethods + ";";
		else line += "" + (nMethods - 1)+ ";"; // zero indexed
		writeLine(line);
	}

	public void writeIID(String uuidName, String uuidValue) {
		writeLine("\tpublic static final String " + uuidName + " =");
		writeLine("\t\t\"" + uuidValue + "\";");
		writeLine();
		String iid = uuidName.substring(0, uuidName.indexOf("_STR"));
		writeLine("\tpublic static final nsID " + iid + " =");
		writeLine("\t\tnew nsID(" + uuidName + ");");
	}

	public void writeAddressField() {
		writeLine("\tint /*long*/ address;");
	}
	public void writeConstructor(String className, String parentName) {
		writeLine("\tpublic " + className + "(int /*long*/ address) {");
		if (!parentName.equals(NO_SUPER_CLASS)) {
			writeLine("\t\tsuper(address);");
		} else {
			writeLine("\t\tthis.address = address;");
		}
		writeLine("\t}");
	}

	public void writeAddressGetter() {
		writeLine("\tpublic int /*long*/ getAddress() {");
		writeLine("\t\treturn this.address;");
		writeLine("\t}");
	}
	
	public void writeConstant(String name, String value) {
		writeLine("\tpublic static final int " + name + " = " + value + ";");
	}

	public void writeMethod(String name, String parentName, int methodIndex, String[] argTypes, String[] argNames) {
		write("\tpublic int " + name + "(");
		for (int i = 0; i < argTypes.length; i++) {
			write(argTypes[i] + " " + argNames[i]);
			if (i < argTypes.length - 1)
				write(", ");
		}
		write(") {");
		writeLine();
		String line = "\t\treturn XPCOM.VtblCall(";
		if (!parentName.equals(NO_SUPER_CLASS)) line += parentName + ".LAST_METHOD_ID + " + (methodIndex + 1) + ", getAddress()";
		else line += methodIndex + ", getAddress()"; // zero indexed
		write(line);
		if (argTypes.length > 0)
			write(", ");
		for (int i = 0; i < argTypes.length; i++) {
			write(argNames[i]);
			if (i < argTypes.length - 1)
				write(", ");
		}
		writeLine(");");
		writeLine("\t}");
	}

	public void writeClassEnd() {
		write("}");
	}

	public void logVtblCall(String[] argTypes) {
		String vtbl = "static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl";
		if (argTypes.length > 0)
			vtbl += ", ";
		for (int i = 0; i < argTypes.length; i++) {
			vtbl += argTypes[i] + " arg" + i;
			if (i < argTypes.length - 1)
				vtbl += ", ";
		}
		vtbl += ");";
		Integer key = new Integer(argTypes.length);
		Vector list = (Vector) vtbls.get(key);
		if (list == null) {
			list = new Vector();
			vtbls.put(key, list);
		}
		boolean duplicate = false;
		Enumeration e = list.elements();
		while (e.hasMoreElements()) {
			String s = (String) e.nextElement();
			if (vtbl.equals(s)) {
				duplicate = true;
				break;
			}
		}
		if (!duplicate)
			list.add(vtbl);
	}

	public void outputVtblCall() {
		Enumeration e = vtbls.keys();
		int n = 0;
		while (e.hasMoreElements()) {
			e.nextElement();
			n++;
		}
		Integer[] keys = new Integer[n];
		e = vtbls.keys();
		n = 0;
		while (e.hasMoreElements()) {
			keys[n] = (Integer) e.nextElement();
			n++;
		}
		Arrays.sort(keys);
		for (int i = 0; i < keys.length; i++) {
			Vector list = (Vector) vtbls.get(keys[i]);
			Object[] elts = (Object[]) list.toArray();
			Arrays.sort(elts);
			for (int j = 0; j < elts.length; j++) {
				System.out.println(elts[j]);
			}
		}

	}

	/** Parsing invoking write callbacks */

	/*
	 * Convert a C header file into a Java source file matching SWT Mozilla
	 binding.
	 */
	public void parse(String src, String destPath) {
		if (DEBUG) writeLine("*** PARSING <"+src+"> to folder "+destPath);
		b = new String[maxLines];
		cntLines = 0;
		try {
			r = new FileReader(src);
			BufferedReader br = new BufferedReader(r);
			while ((b[cntLines] = br.readLine()) != null) {
				cntLines++;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		n = 0;
		boolean lookForClasses = true;
		while (lookForClasses) {	
			/* parsing */
			lookForClasses = parse();
	
			String destFile = destPath + className + ".java";
			try {
				w = new FileWriter(destFile);
				if (DEBUG) writeLine("** CREATED JAVA FILE <"+destFile+">");
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
	
			/* writing */
			writeCopyrights();
			writePackageDeclaration();
			writeLine();
			writeClassDeclaration(className, parentName);
			writeLine();
			writeLastMethodId(parentName, nMethods);
			writeLine();
			writeIID(uuidName, uuidValue);
			writeLine();
			if (parentName.equals(NO_SUPER_CLASS)) {
				writeAddressField();
				writeLine();
			}
			writeConstructor(className, parentName);
			writeLine();
			
			if (parentName.equals(NO_SUPER_CLASS)) {
				writeAddressGetter();
				writeLine();
			}
	
			int constantIndex = 0, methodIndex = 0;
			for (int i = 0; i < bodyOrder.length(); i++) {
				if (bodyOrder.charAt(i) == 'C') {
					writeConstant(constantNames[constantIndex],
							constantValues[constantIndex]);
					if (i < bodyOrder.length() - 1) writeLine();
					constantIndex++;
				} else if (bodyOrder.charAt(i) == 'M') {
					writeMethod(methodNames[methodIndex], parentName, methodIndex,
							argTypes[methodIndex], argNames[methodIndex]);
					if (i < bodyOrder.length() - 1) writeLine();
					methodIndex++;
				}
			}
	
			writeClassEnd();
	
			try {
				w.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getPackages() {
		return "package org.eclipse.swt.internal.mozilla;";
	}

	public boolean parse() {
		if (!jumpToUuidDeclaration()) return false;
		uuidName = getUuidName(b[n]);
		if (DEBUG)
			System.out.println("UUID name: <" + uuidName + ">");
		uuidValue = getUuidValue(b[n]);
		if (DEBUG)
			System.out.println("UUID value: <" + uuidValue + ">");
		jumpToInterfaceDeclaration();
		className = getClassName(b[n]);
		if (DEBUG)
			System.out.println("Interface name: <" + className + ">");
		parentName = getParentName(b[n]);
		if (DEBUG)
			System.out.println("parentName: <" + parentName + ">");
		parseBody();
		return true;
	}

	boolean jumpToUuidDeclaration() {
		// jump to line matching: "#define NS_IWEBBROWSERCHROME_IID_STR "ba434c60-9d52-11d3-afb0-00a024ffc08c""
		while (!(b[n].startsWith("#define ") && b[n].indexOf("_IID_STR \"") != -1)) {
			n++;
			if (n >= cntLines) return false;
		}
		return true;
	}

	//assume a declaration matching: "#define NS_IWEBBROWSERCHROME_IID_STR "ba434c60-9d52-11d3-afb0-00a024ffc08c""
	//returns NS_IWEBBROWSERCHROME_IID_STR
	String getUuidName(String declaration) {
		return declaration.substring(declaration.indexOf("#define ")
				+ "#define ".length(), declaration.indexOf(" \""));
	}

	//assume a declaration matching: "#define NS_IWEBBROWSERCHROME_IID_STR "ba434c60-9d52-11d3-afb0-00a024ffc08c""
	//returns ba434c60-9d52-11d3-afb0-00a024ffc08c
	String getUuidValue(String declaration) {
		return declaration.substring(declaration.indexOf("_IID_STR \"")
				+ "_IID_STR \"".length(), declaration.lastIndexOf('"'));
	}

	void jumpToInterfaceDeclaration() {
		// jump to line matching: "class NS_NO_VTABLE nsIWebBrowserChrome : public nsISupports {"
		while (!(b[n].startsWith("class NS_NO_VTABLE "))) {
			n++;
		}
	}

	// Assume a declaration matching: "class NS_NO_VTABLE nsIWebBrowserChrome : public nsISupports {"
	// or "class NS_NO_VTABLE NS_SCRIPTABLE nsIWebBrowserChrome : public nsISupports {" returns nsIWebBrowserChrome.
	// Special case for nsISupports that has no super class: class NS_NO_VTABLE nsISupports {
	String getClassName(String declaration) {
		int endIndex = declaration.indexOf(" :");
		// nsISupports special case (no super class)
		if (endIndex == -1) endIndex = declaration.indexOf(" {");
		String searchString = "class NS_NO_VTABLE NS_SCRIPTABLE";
		int startIndex = declaration.indexOf(searchString);
		if (startIndex == -1) {
			searchString = "class NS_NO_VTABLE ";
			startIndex = declaration.indexOf(searchString);
		}
		return declaration.substring(startIndex + searchString.length(), endIndex);
	}

	// assume a declaration matching: "class NS_NO_VTABLE nsIWebBrowserChrome : public nsISupports {"
	// returns nsISupports
	// special case for nsISupports that has no super class: class NS_NO_VTABLE nsISupports {
	String getParentName(String declaration) {
		if (declaration.indexOf(" :") == -1) return NO_SUPER_CLASS;
		return declaration.substring(declaration.indexOf(": public ")
				+ ": public ".length(), declaration.indexOf(" {"));
	}

	// parse methods and constants declarations starting at the current index
	// out:
	// .String body - contains the corresponding java content
	// .n - set to the end of the interface body declaration ( line with the enclosing "};" )
	// .nMethods    - set to the number of methods parsed
	void parseBody() {
		body = "";
		bodyOrder = "";
		int nConstants = 0;
		nMethods = 0;

		int tmp_n = n;
		while (true) {
			int type = jumpToNextConstantOrMethod();
			if (type == CONSTANT)
				nConstants++;
			if (type == METHOD)
				nMethods++;
			if (type == END_BODY)
				break;
			n++;
		}
		n = tmp_n;
		constantNames = new String[nConstants];
		constantValues = new String[nConstants];
		methodNames = new String[nMethods];
		argTypes = new String[nMethods][];
		argNames = new String[nMethods][];
		int constantIndex = 0, methodIndex = 0;
		while (true) {
			int type = jumpToNextConstantOrMethod();
			if (type == CONSTANT) {
				parseConstant(b[n], constantIndex);
				bodyOrder += "C";
				constantIndex++;
			}
			if (type == METHOD) {
				parseMethod(b[n], methodIndex);
				logVtblCall(argTypes[methodIndex]);
				bodyOrder += "M";
				methodIndex++;
			}
			if (type == END_BODY)
				return;
			n++;
		}
	}

	static int CONSTANT = 0;

	static int METHOD = 1;

	static int END_BODY = 2;

	boolean isEndOfInterfaceBody() {
		return b[n].startsWith("};");
	}

	int jumpToNextConstantOrMethod() {
		while (!isEndOfInterfaceBody()) {
			if (b[n].startsWith("  enum { ")) {
				return CONSTANT;
			}
			if (methodNameStartIndexOf(b[n]) != -1) {
				return METHOD;
			}
			n++;
		}
		return END_BODY;
	}

	void parseConstant(String constant, int constantIndex) {
		String constantName = constant.substring(constant.indexOf(" enum { ")
				+ " enum { ".length(), constant.indexOf(" ="));
		if (DEBUG)
			writeLine("constantName <" + constantName + ">");
		constantNames[constantIndex] = constantName;
		
		// most constants values have a trailing U
		// enum { APP_TYPE_UNKNOWN = 0U };
		int endIndex = constant.indexOf("U };");
		// a few others don't
		// enum { ENUMERATE_FORWARDS = 0 };		
		if (endIndex == -1) endIndex = constant.indexOf(" };");
		String constantValue = constant.substring(constant.indexOf(" = ")
				+ " = ".length(), endIndex);
		if (DEBUG)
			writeLine("constantValue <" + constantValue + ">");
		constantValues[constantIndex] = constantValue;
	}

	// NS_IMETHOD SetStatus(PRUint32 statusType, const PRUnichar *status) = 0;
	// identify:
	// method name: <SetStatus>
	// Nbr of arguments: 2
	// Type of argument 0: PRUint32
	// Name of argument 0: statusType
	// Type of argument 1: const PRUnichar *
	// Name of argument 1: status
	void parseMethod(String line, int methodIndex) {
		int start = methodNameStartIndexOf(line);
		int end = methodNameEndIndexOf(line);
		String methodName = line.substring(start, end);
		if (DEBUG)
			writeLine("method name: <" + methodName + ">");
		methodNames[methodIndex] = methodName;
		int argStart = end+"(".length();
		int argEnd = line.indexOf(")", argStart);
		parseArgs(line.substring(argStart, argEnd), methodIndex);
	}

	// Given a line, returns the start of the method name or -1
	// if the line does not contain a method declaration.
	int methodNameStartIndexOf(String line) {
		for (int i = 0; i < BEFORE_METHOD_NAME.length; i++) {
			int index = line.indexOf(BEFORE_METHOD_NAME[i]);
			if (index != -1) return index + BEFORE_METHOD_NAME[i].length();
		}
		return -1;
	}
	int methodNameEndIndexOf(String line) {
		int startIndex = methodNameStartIndexOf(line);
		return line.indexOf("(", startIndex);
	}
	void parseArgs(String args, int methodIndex) {
		int nArgs = -1;
		// methods with no args look like: () or (void)
		String[] noArgs = new String[] { "", "void" };
		for (int i = 0; i < noArgs.length; i++) {
			if (args.equals(noArgs[i])) {
				nArgs = 0;
				break;
			}
		}
		if (nArgs == -1)
			nArgs = count(args, ", ") + 1;
		String[] argTypes = new String[nArgs];
		this.argTypes[methodIndex] = argTypes;
		String[] argNames = new String[nArgs];
		this.argNames[methodIndex] = argNames;
		int typeStart = 0;

		// name is separated from its type by either of the following (sorted by decreasing size to find the most complete pattern */
		String[] typeNameSep = new String[] { " * *", " **", " * & ", " * ", " *",
				" & ", " " };
		for (int i = 0; i < nArgs; i++) {
			/* get the type */
			int nextTypeStart = i < nArgs - 1 ? args.indexOf(", ", typeStart)
					+ ", ".length() : args.length();
			int typeNameSepIndex = 0;
			int separatorIndex = 0;
			for (; typeNameSepIndex < typeNameSep.length; typeNameSepIndex++) {
				separatorIndex = args.indexOf(typeNameSep[typeNameSepIndex],
						typeStart);
				if (separatorIndex != -1 && separatorIndex < nextTypeStart)
					break;
			}
			String separator = typeNameSep[typeNameSepIndex];
			argTypes[i] = getC2JavaType(args.substring(typeStart,
					separatorIndex + separator.length()));
			if (DEBUG)
				writeLine("arg type" + i + ": <" + argTypes[i] + ">");
			/* get the name */
			int nameStart = separatorIndex + separator.length();
			int nameEnd = i < nArgs - 1 ? args.indexOf(", ", nameStart) : args
					.length();
			argNames[i] = args.substring(nameStart, nameEnd);
			if (DEBUG)
				writeLine("arg name" + i + ": <" + argNames[i] + ">");

			typeStart = nextTypeStart;
		}
	}

	String getC2JavaType(String cType) {
		for (int i = 0; i < TYPES_C2JAVA.length; i++) {
			if (cType.indexOf(TYPES_C2JAVA[i][0]) != -1)
				return TYPES_C2JAVA[i][1];
		}
		return "!ERROR UNKNOWN C TYPE <" + cType + ">!";
	}

	// how many times part can be found in s
	static int count(String s, String part) {
		int index = -1, cnt = 0;
		while ((index = s.indexOf(part, index + 1)) != -1)
			cnt++;
		return cnt;
	}

	static String COPYRIGHTS =
		"/* ***** BEGIN LICENSE BLOCK *****\r\n"
		+ " * Version: MPL 1.1\r\n"
		+ " *\r\n"
		+ " * The contents of this file are subject to the Mozilla Public License Version\r\n"
		+ " * 1.1 (the \"License\"); you may not use this file except in compliance with\r\n"
		+ " * the License. You may obtain a copy of the License at\r\n"
		+ " * http://www.mozilla.org/MPL/\r\n"
		+ " *\r\n"
		+ " * Software distributed under the License is distributed on an \"AS IS\" basis,\r\n"
		+ " * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License\r\n"
		+ " * for the specific language governing rights and limitations under the\r\n"
		+ " * License.\r\n"
		+ " *\r\n"
		+ " * The Original Code is Mozilla Communicator client code, released March 31, 1998.\r\n"
		+ " *\r\n"
		+ " * The Initial Developer of the Original Code is\r\n"
		+ " * Netscape Communications Corporation.\r\n"
		+ " * Portions created by Netscape are Copyright (C) 1998-1999\r\n"
		+ " * Netscape Communications Corporation.  All Rights Reserved.\r\n"
		+ " *\r\n" 
		+ " * Contributor(s):\r\n" 
		+ " *\r\n" 
		+ " * IBM\r\n"
		+ " * -  Binding to permit interfacing between Mozilla and SWT\r\n"
		+ " * -  Copyright (C) 2003, 2009 IBM Corp.  All Rights Reserved.\r\n"
		+ " *\r\n" + " * ***** END LICENSE BLOCK ***** */";

	static String PACKAGE_DECLARATION = "package org.eclipse.swt.internal.mozilla;";

}
