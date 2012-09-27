/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
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

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

public class MacGenerator {
	String[] xmls;
	Document[] documents;
	String outputDir, outputLibDir, extrasDir, mainClassName;
	String delimiter = System.getProperty("line.separator");
	boolean generate64Code;
	PrintWriter out;
	
	public static boolean BUILD_C_SOURCE = true;
	public static boolean GENERATE_ALLOC = true;
	public static boolean GENERATE_STRUCTS = false;
	public static boolean USE_SYSTEM_BRIDGE_FILES = false;

	static final char[] INT_LONG = "int /*long*/".toCharArray();
	static final char[] INT_LONG_ARRAY = "int[] /*long[]*/".toCharArray();
	static final char[] FLOAT_DOUBLE = "float /*double*/".toCharArray();
	static final char[] FLOAT_DOUBLE_ARRAY = "float[] /*double[]*/".toCharArray();
	static final char[] LONG_INT = "long /*int*/".toCharArray();
	static final char[] LONG_INT_ARRAY = "long[] /*int[]*/".toCharArray();
	static final char[] DOUBLE_FLOAT = "double /*float*/".toCharArray();
	static final char[] DOUBLE_FLOAT_ARRAY = "double[] /*float[]*/".toCharArray();

public MacGenerator() {
}

static void list(File path, ArrayList list) {
	if (path == null) return;
	File[] frameworks = path.listFiles();
	if (frameworks == null) return;
	for (int i = 0; i < frameworks.length; i++) {
		File file = frameworks[i];
		String name = file.getName();
		int index = name.lastIndexOf(".");
		if (index != -1) {
			String xml = file.getAbsolutePath() + "/Resources/BridgeSupport/" + name.substring(0, index) + "Full.bridgesupport";
			if (new File(xml).exists()) {
				list.add(xml);
			}
		}
	}
}

static int indexOf(final char[] toBeFound, final char[] array, final int start) {
	final int arrayLength = array.length;
	final int toBeFoundLength = toBeFound.length;
	if (toBeFoundLength > arrayLength || start < 0) return -1;
	if (toBeFoundLength == 0) return 0;
	if (toBeFoundLength == arrayLength) {
		for (int i = start; i < arrayLength; i++) {
			if (array[i] != toBeFound[i]) return -1;
		}
		return 0;
	}
	arrayLoop: for (int i = start, max = arrayLength - toBeFoundLength + 1; i < max; i++) {
		if (array[i] == toBeFound[0]) {
			for (int j = 1; j < toBeFoundLength; j++) {
				if (array[i + j] != toBeFound[j]) continue arrayLoop;
			}
			return i;
		}
	}
	return -1;
}

static boolean replace(char[] source, char[] src, char[] dest) {
	boolean changed = false;
	int start = 0;
	while (start < source.length) {
		int index = indexOf(src, source, start);
		if (index == -1) break;
		changed |= true;
		System.arraycopy(dest, 0, source, index, dest.length);
		start = index + 1;
	}
	return changed;
}

void output(String fileName, char[] source) {
	try {
		if (source.length > 0) {
			if (generate64Code) {
				replace(source, INT_LONG, LONG_INT);
				replace(source, INT_LONG_ARRAY, LONG_INT_ARRAY);
				replace(source, FLOAT_DOUBLE, DOUBLE_FLOAT);
				replace(source, FLOAT_DOUBLE_ARRAY, DOUBLE_FLOAT_ARRAY);
			} else {
				replace(source, LONG_INT, INT_LONG);
				replace(source, LONG_INT_ARRAY, INT_LONG_ARRAY);
				replace(source, DOUBLE_FLOAT, FLOAT_DOUBLE);
				replace(source, DOUBLE_FLOAT_ARRAY, FLOAT_DOUBLE_ARRAY);
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PrintStream stream = new PrintStream(out);
			stream.print(source);
			stream.flush();
			JNIGenerator.output(out.toByteArray(), fileName);
		}
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

int getLevel(Node node) {
	int level = 0;
	while (node != null) {
		level++;
		node = node.getParentNode();
	}
	return level;
}

void merge(Document document, Document extraDocument) {
	if (extraDocument == null) return;
	
	/* Build a lookup table for extraDocument */
	HashMap extras = new HashMap();
	buildLookup(extraDocument, extras);

	/* Merge attributes on existing elements building a lookup table for document */
	HashMap lookup = new HashMap();
	merge(document, extras, lookup);
	
	/* 
	 * Merge new elements. Extras at this point contains only elements that were
	 * not found in the document.
	 */
	ArrayList sortedNodes = Collections.list(Collections.enumeration(extras.values()));
	Collections.sort(sortedNodes, new Comparator() {
		public int compare(Object arg0, Object arg1) {
			int compare = getLevel((Node)arg0) - getLevel((Node)arg1);
			if (compare == 0) {
				return ((Node)arg0).getNodeName().compareTo(((Node)arg1).getNodeName());
			}
			return compare;
		}
	});
	String delimiter = System.getProperty("line.separator");
	for (Iterator iterator = sortedNodes.iterator(); iterator.hasNext();) {
		Node node = (Node) iterator.next();
		String name = node.getNodeName();
		if ("arg".equals(name) || "retval".equals(name)) {
			if (!sortedNodes.contains(node.getParentNode())) continue;
		}
		Node parent = (Node)lookup.get(getKey(node.getParentNode()));
		Element element = document.createElement(node.getNodeName());
		String text = parent.getChildNodes().getLength() == 0 ? delimiter : "";
		for (int i = 0, level = getLevel(parent) - 1; i < level; i++) {
			text += "  ";
		}
		parent.appendChild(document.createTextNode(text));
		parent.appendChild(element);
		parent.appendChild(document.createTextNode(delimiter));
		NamedNodeMap attributes = node.getAttributes();
		for (int j = 0, length = attributes.getLength(); j < length; j++) {
			Node attr = (Node)attributes.item(j);
			element.setAttribute(attr.getNodeName(), attr.getNodeValue());
		}
		lookup.put(getKey(element), element);
	}
}

public void generate(ProgressMonitor progress) {
	if (progress != null) {
		progress.setTotal(BUILD_C_SOURCE ? 4 : 3);
		progress.setMessage("extra attributes...");
	}
	generateExtraAttributes();
	if (progress != null) {
		progress.step();
		progress.setMessage(mainClassName);
	}
	generateMainClass();
	if (progress != null) {
		progress.step();
		progress.setMessage("classes...");
	}
	generateClasses();
	if (GENERATE_STRUCTS) {
		if (progress != null) {
			progress.step();
			progress.setMessage("structs...");
		}
		generateStructs();
	}
	if (BUILD_C_SOURCE) {
		if (progress != null) {
			progress.step();
			progress.setMessage("C source...");
		}
		generateCSource();
	}
	if (progress != null) {
		progress.step();
		progress.setMessage("Done.");
	}
}

void generateCSource() {
	JNIGeneratorApp app = new JNIGeneratorApp();
	String outputLibDir = this.outputLibDir != null ? this.outputLibDir : outputDir + "/library";
	app.setMainClassName(mainClassName, outputLibDir, outputDir);
	app.generate();
}

String fixDelimiter(String str) {
	if (delimiter.equals("\n")) return str;
	int index = 0, length = str.length();
	StringBuffer buffer = new StringBuffer();
	while (index != -1) {
		int start = index;
		index = str.indexOf('\n', start);
		if (index == -1) {
			buffer.append(str.substring(start, length));
		} else {
			buffer.append(str.substring(start, index));
			buffer.append(delimiter);
			index++;
		}
	}
	return buffer.toString();
}

String getParamName(Node param, int i) {
	NamedNodeMap paramAttributes = param.getAttributes();
	Node swtName = paramAttributes.getNamedItem("swt_param_name");
	String paramName = "";
	if (swtName != null) {
		paramName = swtName.getNodeValue();
	} else {
		 Node node = paramAttributes.getNamedItem("name");
		 if (node != null) paramName = node.getNodeValue();
	}
	if (paramName.length() == 0) {
		Node node = paramAttributes.getNamedItem("index");
		String index = "0";
		if (node != null) {
			index = node.getNodeValue();
		} else {
			index = String.valueOf(i);
		}
		paramName = "arg" + index;
	}
	if (paramName.equals("boolean")) paramName = "b";
	return paramName;
}

void generateFields(String structName, ArrayList fields) {
	for (Iterator iterator = fields.iterator(); iterator.hasNext();) {
		Node field = (Node)iterator.next();
		NamedNodeMap fieldAttributes = field.getAttributes();
		String fieldName = fieldAttributes.getNamedItem("name").getNodeValue();
		String type = getJavaType(field), type64 = getJavaType64(field);
		out("\t");
		out("public ");
		out(type);
		if (!type.equals(type64)) {
			out(" /*");
			out(type64);
			out("*/");
		}
		out(" ");
		out(fieldName);
		if (isStruct(field)) {
			out(" = new ");
			String clazz = fieldAttributes.getNamedItem("declared_type").getNodeValue();
			out (clazz);
			out ("()");
		}
		out(";");
		outln();
	}
}

void generateMethods(String className, ArrayList methods) {
	for (Iterator iterator = methods.iterator(); iterator.hasNext();) {
		Node method = (Node)iterator.next();
		NamedNodeMap mthAttributes = method.getAttributes();
		String sel = mthAttributes.getNamedItem("selector").getNodeValue();
		if ("NSObject".equals(className)) {
		    if ("alloc".equals(sel) || "dealloc".equals(sel)) continue;
		}
		out("public ");
		boolean isStatic = isStatic(method); 
		if (isStatic) out("static ");
		Node returnNode = getReturnNode(method.getChildNodes());
		if (getType(returnNode).equals("void")) returnNode = null;
		String returnType = "", returnType64 = "";
		if (returnNode != null) {
			String type = returnType = getJavaType(returnNode), type64 = returnType64 = getJavaType64(returnNode);
			out(type);
			if (!type.equals(type64)) {
				out(" /*");
				out(type64);
				out("*/");
			}
			out(" ");
		} else {
			out("void ");
		}
		String methodName = sel;
		if (isUnique(method, methods)) {
			int index = methodName.indexOf(":");
			if (index != -1) methodName = methodName.substring(0, index);
		} else {
			//TODO improve this selector
			methodName = methodName.replaceAll(":", "_");
			if (isStatic) methodName = "static_" + methodName;
		}
		out(methodName);
		out("(");
		NodeList params = method.getChildNodes();
		boolean first = true;
		int argIndex = 0;
		for (int k = 0; k < params.getLength(); k++) {
			Node param = params.item(k);
			if ("arg".equals(param.getNodeName())) {
				if (!first) out(", ");
				String type = getJavaType(param), type64 = getJavaType64(param);
				out( type);
				if (!type.equals(type64)) {
					out(" /*");
					out(type64);
					out("*/");
				}
				first = false;
				out(" ");
				out(getParamName(param, argIndex++));
			}
		}
		out(") {");
		outln();
		if (returnNode != null && isStruct(returnNode)) {
			out("\t");
			out(returnType);
			out(" result = new ");
			out(returnType);
			out("();");
			outln();
			out("\tOS.objc_msgSend_stret(result, ");
		} else if (returnNode != null && isBoolean(returnNode)) {
			out("\treturn ");
			out("OS.objc_msgSend_bool(");
		} else if (returnNode != null && isFloatingPoint(returnNode)) {
			out("\treturn ");
			String type = getType(returnNode), type64 = getType64(returnNode);
			if (type.equals(type64) && type.equals("float")) {
				out("OS.objc_msgSend_floatret(");
			} else {
				if (returnType.equals("float")) out("(float /*double*/)");
				out("OS.objc_msgSend_fpret(");
			}
		} else if (returnNode != null && isObject(returnNode)) {
			out("\tint /*long*/ result = OS.objc_msgSend(");
		} else {
			if (returnNode != null) {
				out("\treturn ");
				if ((returnType.equals("int") && returnType64.equals("int")) || !returnType.equals("int")) {
					out("(");
					out(returnType);
					out(")");
				}
				if (returnType.equals("int") && returnType64.equals("int")) {
					out("/*64*/");
				}
			} else {
				out("\t");
			}
			out("OS.objc_msgSend(");
		}
		if (isStatic) {
			out("OS.class_");
			out(className);
		} else {
			out("this.id");
		}
		out(", OS.");
		out(getSelConst(sel));
		first = false;
		argIndex = 0;
		for (int k = 0; k < params.getLength(); k++) {
			Node param = params.item(k);
			if ("arg".equals(param.getNodeName())) {
				if (!first) out(", ");
				first = false;
				String paramName = getParamName(param, argIndex++);
				if (isObject(param)) {
					out(paramName);
					out(" != null ? ");
					out(paramName);
					out(".id : 0");
				} else {
					out(paramName);
				}
			}
		}
		out(")");
		out(";");
		outln();
		if (returnNode != null && isObject(returnNode)) {
			if (!isStatic && returnType.equals(className)) {
				out("\treturn result == this.id ? this : (result != 0 ? new ");
				out(returnType);
				out("(result) : null);");
			} else {
				out("\treturn result != 0 ? new ");
				NamedNodeMap attributes = returnNode.getAttributes();
				Node swt_alloc = attributes.getNamedItem("swt_alloc");
				if (swt_alloc != null && swt_alloc.getNodeValue().equals("true")) {
					out(className);
				} else {
					out(returnType);
				}
				out("(result) : null;");
			}
			outln();
		} else if (returnNode != null && isStruct(returnNode)) {
			out("\treturn result;");
			outln();
		}
		out("}");
		outln();
		outln();
	}
}

void generateExtraFields(String className) {
	/* sizeof field */
	out("\t");
	out("public static int sizeof = OS." + className + "_sizeof();");
	outln();
	if ("CGSize".equals(className)) {
		outln();
		out("\tpublic String toString () {");
		outln();
		out("\t\treturn \"CGSize {\" + width + \" \" + height + \"}\";");
		outln();
		out("\t}");
		outln();
	}
	if ("CGRect".equals(className)) {
		outln();
		out("\tpublic String toString () {");
		outln();
		out("\t\treturn \"CGRect {\" + origin.x + \" \" + origin.y + \" \" + size.width + \" \" + size.height + \"}\";");
		outln();
		out("\t}");
		outln();
	}
	if ("CGPoint".equals(className)) {
		outln();
		out("\tpublic String toString () {");
		outln();
		out("\t\treturn \"CGPoint {\" + x + \" \" + y + \"}\";");
		outln();
		out("\t}");
		outln();
	}
}

void generateExtraMethods(String className) {
	/* Empty constructor */
	out("public ");
	out(className);
	out("() {");
	outln();
	out("\tsuper();");
	outln();
	out("}");
	outln();
	outln();
	/* pointer constructor */
	out("public ");
	out(className);
	out("(int /*long*/ id) {");
	outln();
	out("\tsuper(id);");
	outln();
	out("}");
	outln();
	outln();
	/* object constructor */
	out("public ");
	out(className);
	out("(id id) {");
	outln();
	out("\tsuper(id);");
	outln();
	out("}");
	outln();
	outln();
	/* NSObject helpers */
	if (className.equals("NSObject")) {
		if (GENERATE_ALLOC) {
			out("public NSObject alloc() {");
			outln();
			out("\tthis.id = OS.objc_msgSend(objc_getClass(), OS.sel_alloc);");
			outln();
			out("\treturn this;");
			outln();
			out("}");
			outln();
			outln();
		}
	}
	/* NSString helpers */
	if (className.equals("NSString")) {
		/* Get java string */
		out("public String getString() {");
		outln();
		out("\tchar[] buffer = new char[(int)/*64*/length()];");
		outln();
		out("\tgetCharacters(buffer);");
		outln();
		out("\treturn new String(buffer);");
		outln();
		out("}");
		outln();
		outln();
		/* create NSString */
		out("public NSString initWithString(String str) {");
		outln();
		out("\tchar[] buffer = new char[str.length()];");
		outln();
		out("\tstr.getChars(0, buffer.length, buffer, 0);");
		outln();
		out("\treturn initWithCharacters(buffer, buffer.length);");
		outln();
		out("}");
		outln();
		outln();
		out("public static NSString stringWith(String str) {");
		outln();
		out("\tchar[] buffer = new char[str.length()];");
		outln();
		out("\tstr.getChars(0, buffer.length, buffer, 0);");
		outln();
		out("\treturn stringWithCharacters(buffer, buffer.length);");
		outln();
		out("}");
		outln();
		outln();
	}	
}

TreeMap getGeneratedClasses() {
	TreeMap classes = new TreeMap();
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null) continue;
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if ("class".equals(node.getNodeName()) && getGen(node)) {
				ArrayList methods;
				String name = node.getAttributes().getNamedItem("name").getNodeValue();
				Object[] clazz = (Object[])classes.get(name);
				if (clazz == null) {
					methods = new ArrayList();
					classes.put(name, new Object[]{node, methods});
				} else {
					methods = (ArrayList)clazz[1];
				}
				NodeList methodList = node.getChildNodes();
				for (int j = 0; j < methodList.getLength(); j++) {
					Node method = methodList.item(j);
					if ("method".equals(method.getNodeName()) && getGen(method)) {
						methods.add(method);
					}					
				}
			}
		}
	}
	return classes;
}

TreeMap getGeneratedStructs() {
	TreeMap structs = new TreeMap();
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null) continue;
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if ("struct".equals(node.getNodeName()) && getGen(node)) {
				ArrayList fields;
				String name = node.getAttributes().getNamedItem("name").getNodeValue();
				Object[] clazz = (Object[])structs.get(name);
				if (clazz == null) {
					fields = new ArrayList();
					structs.put(name, new Object[]{node, fields});
				} else {
					fields = (ArrayList)clazz[1];
				}
				NodeList fieldList = node.getChildNodes();
				for (int j = 0; j < fieldList.getLength(); j++) {
					Node field = fieldList.item(j);
					if ("field".equals(field.getNodeName()) && getGen(field)) {
						fields.add(field);
					}
				}
			}
		}
	}
	return structs;
}

void copyClassMethodsDown(final Map classes) {
	ArrayList sortedClasses = Collections.list(Collections.enumeration(classes.values()));
	Collections.sort(sortedClasses, new Comparator() {
		int getHierarchyLevel(Node node) {
			String superclass = getSuperclassName(node);
			int level = 0;
			while (!superclass.equals("id") && !superclass.equals("NSObject")) {
				level++;
				superclass = getSuperclassName((Node)((Object[])classes.get(superclass))[0]);
			}
			return level;			
		}
		public int compare(Object arg0, Object arg1) {
			return getHierarchyLevel((Node)((Object[])arg0)[0]) - getHierarchyLevel((Node)((Object[])arg1)[0]);  
		}
	});
	for (Iterator iterator = sortedClasses.iterator(); iterator.hasNext();) {
		Object[] clazz = (Object[]) iterator.next();
		Node node = (Node)clazz[0];
		ArrayList methods = (ArrayList)clazz[1];
		Object[] superclass = (Object[])classes.get(getSuperclassName(node));
		if (superclass != null) {
			for (Iterator iterator2 = ((ArrayList)superclass[1]).iterator(); iterator2.hasNext();) {
				Node method = (Node) iterator2.next();
				if (isStatic(method)) {
					methods.add(method);
				}
			}
		}
	}
}

String getSuperclassName (Node node) {
	NamedNodeMap attributes = node.getAttributes();
	Node superclass = attributes.getNamedItem("swt_superclass");
	if (superclass != null) {
		return superclass.getNodeValue();
	} else {
		Node name = attributes.getNamedItem("name");
		if (name.getNodeValue().equals("NSObject")) {
			return "id";
		} else {
			return "NSObject";
		}
	}
}

void generateClasses() {
	MetaData metaData = new MetaData(mainClassName);	
	TreeMap classes = getGeneratedClasses();
	copyClassMethodsDown(classes);
	
	Set classNames = classes.keySet();
	for (Iterator iterator = classNames.iterator(); iterator.hasNext();) {
		CharArrayWriter out = new CharArrayWriter();
		this.out = new PrintWriter(out);

		out(fixDelimiter(metaData.getCopyright()));

		String className = (String) iterator.next();
		Object[] clazz = (Object[])classes.get(className);
		Node node = (Node)clazz[0];
		ArrayList methods = (ArrayList)clazz[1];
		out("package ");
		String packageName = getPackageName(mainClassName);
		out(packageName);
		out(";");
		outln();
		outln();
		out("public class ");
		out(className);
		out(" extends ");
		out(getSuperclassName(node));
		out(" {");
		outln();
		outln();		
		generateExtraMethods(className);
		generateMethods(className, methods);		
		out("}");
		outln();
		
		String fileName = outputDir + packageName.replace('.', '/') + "/" + className + ".java";
		this.out.flush();
		output(fileName, out.toCharArray());
		this.out = null;
	}
}

void generateStructs() {
	MetaData metaData = new MetaData(mainClassName);	
	TreeMap structs = getGeneratedStructs();

	Set structNames = structs.keySet();
	for (Iterator iterator = structNames.iterator(); iterator.hasNext();) {
		CharArrayWriter out = new CharArrayWriter();
		this.out = new PrintWriter(out);

		out(fixDelimiter(metaData.getCopyright()));

		String className = (String) iterator.next();
		Object[] clazz = (Object[])structs.get(className);
		ArrayList methods = (ArrayList)clazz[1];
		out("package ");
		String packageName = getPackageName(mainClassName);
		out(packageName);
		out(";");
		outln();
		outln();
		out("public class ");
		out(className);
		out(" {");
		outln();		
		generateFields(className, methods);		
		generateExtraFields(className);
		out("}");
		outln();
		
		String fileName = outputDir + packageName.replace('.', '/') + "/" + className + ".java";
		this.out.flush();
		output(fileName, out.toCharArray());
		this.out = null;
	}
}

void generateExtraAttributes() {
	Document[] documents = getDocuments();
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null || !getGen(document.getDocumentElement())) continue;
		saveExtraAttributes(xmls[x], document);
	}
}

void generateMainClass() {
	CharArrayWriter out = new CharArrayWriter();
	this.out = new PrintWriter(out);

	String header = "", footer = "";
	String fileName = outputDir + mainClassName.replace('.', '/') + ".java";
	FileInputStream is = null;
	try {
		InputStreamReader input = new InputStreamReader(new BufferedInputStream(is = new FileInputStream(fileName)));
		StringBuffer str = new StringBuffer();
		char[] buffer = new char[4096];
		int read;
		while ((read = input.read(buffer)) != -1) {
			str.append(buffer, 0, read);
		}
		String section = "/** This section is auto generated */";
		int start = str.indexOf(section) + section.length();
		int end = str.indexOf(section, start);
		header = str.substring(0, start);
		footer = end == -1 ? "\n}" : str.substring(end);
		generate64Code = str.indexOf("long /*int*/") != -1;
		input.close();
	} catch (IOException e) {
	} finally {
		try {
			if (is != null) is.close();
		} catch (IOException e) {}
	}
	
	out(header);
	outln();
	outln();

	out("/** Custom callbacks */");
	outln();
	generateCustomCallbacks();
	outln();	
	out("/** Classes */");
	outln();
	generateClassesConst();
	outln();
	out("/** Protocols */");
	outln();
	generateProtocolsConst();
	outln();
	out("/** Selectors */");
	outln();
	generateSelectorsConst();
	outln();
	out("/** Constants */");
	outln();
	generateEnums();
	outln();
	out("/** Globals */");
	outln();
	generateConstants();
	outln();
	out("/** Functions */");
	outln();
	outln();
	generateFunctions();
	outln();
	out("/** Super Sends */");
	outln();
	generateSends(true);
	outln();
	out("/** Sends */");
	outln();
	generateSends(false);
	outln();
	generateStructNatives();
	
	outln();
	out(footer);
	this.out.flush();
	output(fileName, out.toCharArray());
	this.out = null;
}

public Document[] getDocuments() {
	if (documents == null) {
		String[] xmls = getXmls();
		documents = new Document[xmls.length];
		for (int i = 0; i < xmls.length; i++) {
			String xmlPath = xmls[i];
			Document document = documents[i] = getDocument(xmlPath);
			if (document == null) continue;
			if (mainClassName != null && outputDir != null) {
				String packageName = getPackageName(mainClassName);
				String folder = extrasDir != null ? extrasDir : outputDir + packageName.replace('.', '/');
				String extrasPath = folder + "/" + getFileName(xmlPath) + ".extras";
				merge(document, getDocument(extrasPath));
			}
		}
	}
	return documents;
}

public String[] getXmls() {
	if (xmls == null || xmls.length == 0) {
		ArrayList array = new ArrayList();
		if (USE_SYSTEM_BRIDGE_FILES) {
			list(new File("/System/Library/Frameworks"), array);
			list(new File("/System/Library/Frameworks/CoreServices.framework/Frameworks"), array);
			list(new File("/System/Library/Frameworks/ApplicationServices.framework/Frameworks"), array);
		} else {
			String packageName = getPackageName(mainClassName);
			File folder = new File(extrasDir != null ? extrasDir : outputDir + packageName.replace('.', '/'));
			File[] files = folder.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith("Full.bridgesupport");
				}
			});
			for (int i = 0; i < files.length; i++) {
				array.add(files[i].getAbsolutePath());
			}
		}
		Collections.sort(array, new Comparator() {
			public int compare(Object o1, Object o2) {
				return new File((String)o1).getName().compareTo(new File((String)o2).getName());
			}
		});
		xmls = (String[])array.toArray(new String[array.size()]);
	}
	return xmls;
}

void saveExtraAttributes(String xmlPath, Document document) {
	try {
		String packageName = getPackageName(mainClassName);
		String folder = extrasDir != null ? extrasDir : outputDir + packageName.replace('.', '/');
		String fileName = folder + "/" + getFileName(xmlPath) + ".extras";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DOMWriter writer = new DOMWriter(new PrintStream(out));
		String[] names = getIDAttributeNames();
		String[] filter = new String[names.length + 2];
		filter[0] = "class_method";
		filter[1] = "swt_.*";
		System.arraycopy(names, 0, filter, 2, names.length);
		writer.setIDAttributes(names);
		writer.setAttributeFilter(filter);
		writer.setNodeFilter("swt_");
		writer.print(document);
		if (out.size() > 0) JNIGenerator.output(out.toByteArray(), fileName);
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

public String getOutputDir() {
	return outputDir;
}

public void setOutputDir(String dir) {
	if (dir != null) {
		if (!dir.endsWith("\\") && !dir.endsWith("/") ) {
			dir += "/";
		}
	}
	this.outputDir = dir;
}

public void setOutputLibDir(String dir) {
	if (dir != null) {
		if (!dir.endsWith("\\") && !dir.endsWith("/") ) {
			dir += "/";
		}
	}
	this.outputLibDir = dir;
}

public void setExtrasDir(String dir) {
	if (dir != null) {
		if (!dir.endsWith("\\") && !dir.endsWith("/") ) {
			dir += "/";
		}
	}
	this.extrasDir = dir;
}

public void setXmls(String[] xmls) {
	this.xmls = xmls;
	this.documents = null;
}

public void setMainClass(String mainClassName) {
	this.mainClassName = mainClassName;
}

Document getDocument(String xmlPath) {
	try {
		InputStream is = null;
		if (xmlPath.indexOf(File.separatorChar) == -1) is = getClass().getResourceAsStream(xmlPath);
		if (is == null) is = new BufferedInputStream(new FileInputStream(xmlPath));
		if (is != null) return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(is));
	} catch (Exception e) {
//		e.printStackTrace();
	}
	return null;
}

public String[] getExtraAttributeNames(Node node) {
	String name = node.getNodeName();
	if (name.equals("method")) {
		NamedNodeMap attribs = node.getAttributes();
		if (attribs != null && attribs.getNamedItem("variadic") != null) {
			return new String[]{"swt_gen_super_msgSend", "swt_gen_custom_callback", "swt_variadic_count","swt_variadic_java_types"};
		}
		return new String[]{"swt_gen_super_msgSend", "swt_gen_custom_callback"};
	} else if (name.equals("function")) {
		NamedNodeMap attribs = node.getAttributes();
		if (attribs != null && attribs.getNamedItem("variadic") != null) {
			return new String[]{"swt_variadic_count","swt_variadic_java_types"};
		}
	} else if (name.equals("class")) {
		return new String[]{"swt_superclass"};
	} else if (name.equals("retval")) {
		return new String[]{"swt_java_type", "swt_java_type64", "swt_alloc"};
	} else if (name.equals("arg")) {
		return new String[]{"swt_java_type", "swt_java_type64", "swt_param_name", "swt_param_cast"};
	}
	return new String[0];
}

public String getFileName(String xmlPath) {
	File file = new File(xmlPath);
	return file.getName();
}

int indexOfNode(Node node) {
	Node parent = node.getParentNode();
	int count = 0;
	Node temp = parent.getFirstChild();
	while (temp != node) {
		count++;
		temp = temp.getNextSibling();
	}
	return count;
}

String getKey (Node node) {
	StringBuffer buffer = new StringBuffer();
	while (node != null) {
		if (buffer.length() > 0) buffer.append("_");
		String name = node.getNodeName();
		StringBuffer key = new StringBuffer(name);
		if ("arg".equals(name)) {
			key.append("-");
			key.append(indexOfNode(node));
		} else {
			Node nameAttrib = getIDAttribute(node);
			if (nameAttrib != null) {
				key.append("-");
				key.append(nameAttrib.getNodeValue());
			}
		}
		NamedNodeMap attributes = node.getAttributes();
		if (attributes != null) {
			boolean isStatic = attributes.getNamedItem("class_method") != null;
			if (isStatic) key.append("-static");
		}
		buffer.append(key.reverse());
		node = node.getParentNode();
	}
	buffer.reverse();
	return buffer.toString();
}

public Node getIDAttribute(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	if (attributes == null) return null;
	String[] names = getIDAttributeNames();
	for (int i = 0; i < names.length; i++) {
		Node nameAttrib = attributes.getNamedItem(names[i]);
		if (nameAttrib != null) return nameAttrib;
	}
	return null;
}

public String[] getIDAttributeNames() {
	return new String[]{
		"name",
		"selector",
		"path",
	};
}

void merge(Node node, HashMap extras, HashMap docLookup) {
	NodeList list = node.getChildNodes();
	for (int i = 0; i < list.getLength(); i++) {
		Node childNode = list.item(i);
		if (childNode.getNodeType() == Node.ELEMENT_NODE) {
			String key = getKey(childNode);
			if (docLookup != null && docLookup.get(key) == null) {
				docLookup.put(key, childNode);
			}
			Node extra = (Node)extras.remove(key);
			if (extra != null) {
				NamedNodeMap attributes = extra.getAttributes();
				for (int j = 0, length = attributes.getLength(); j < length; j++) {
					Node attr = (Node)attributes.item(j);
					String name = attr.getNodeName();
					if (name.startsWith("swt_")) {
						((Element)childNode).setAttribute(name, attr.getNodeValue());
					}
				}
			}
		}
		merge(childNode, extras, docLookup);
	}
}

	
void out(String str) {
	PrintWriter out = this.out;
	out.print(str);
}

void outln() {
	PrintWriter out = this.out;
	out.println();
}

void generateConstants() {
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null) continue;
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if ("constant".equals(node.getNodeName())) {
				if (getGen(node)) {
					NamedNodeMap attributes = node.getAttributes();
					String constName = attributes.getNamedItem("name").getNodeValue();
					out("/** @method flags=const */");
					outln();
					out("public static final native ");
					String type = getType(node), type64 = getType64(node);
					out(type);
					if (!type.equals(type64)) {
						out(" /*");
						out(type64);
						out("*/");
					}
					out(" ");
					out(constName);
					out("();");
					outln();
					if (attributes.getNamedItem("declared_type").getNodeValue().equals("NSString*")) {
						out("public static final NSString ");
						out(constName);
						out(" = new NSString(");
						out(constName);
						out("());");
						outln();
					}
				}
			}
		}		
	}
}

void generateEnums() {
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null) continue;
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if ("enum".equals(node.getNodeName())) {
				if (getGen(node)) {
					NamedNodeMap attributes = node.getAttributes();
					Node valueNode = attributes.getNamedItem("value");
					if (valueNode != null) {
						String value = valueNode.getNodeValue();
						out("public static final ");
						boolean isLong = false;
						if (value.indexOf('.') != -1) {
							out("double ");
						} else {
							if (value.equals("4294967295")) {
								out("int ");
								value = "-1";
							} else {
								try {
									Integer.parseInt(value);
									out("int ");
								} catch (NumberFormatException e) {
									isLong = true;
									out("long ");
								}
							}
						}
						out(attributes.getNamedItem("name").getNodeValue());
						out(" = ");
						out(value);
						if (isLong && !value.endsWith("L")) out("L");
						out(";");
						outln();
					}
				}
			}
		}
	}
}

boolean getGen(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	if (attributes == null) return false;
	Node gen = attributes.getNamedItem("swt_gen");
	return gen != null && !gen.getNodeValue().equals("false");
}

boolean getGenSuper(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	if (attributes == null) return false;
	Node gen = attributes.getNamedItem("swt_gen_super_msgSend");
	return gen != null && !gen.getNodeValue().equals("false");
}

boolean getGenCallback(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	if (attributes == null) return false;
	Node gen = attributes.getNamedItem("swt_gen_custom_callback");
	return gen != null && !gen.getNodeValue().equals("false");
}

boolean isStatic(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	Node isStatic = attributes.getNamedItem("class_method");
	return isStatic != null && isStatic.getNodeValue().equals("true");
}

boolean isStruct(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	Node type = attributes.getNamedItem("type");
	if (type == null) return false;
	String code = type.getNodeValue();
	return code.startsWith("{");
}

boolean isFloatingPoint(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	Node type = attributes.getNamedItem("type");
	if (type == null) return false;
	String code = type.getNodeValue();
	return code.equals("f") || code.equals("d");
}

boolean isObject(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	Node type = attributes.getNamedItem("type");
	if (type == null) return false;
	String code = type.getNodeValue();
	return code.equals("@");
}

boolean isBoolean(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	Node type = attributes.getNamedItem("type");
	if (type == null) return false;
	String code = type.getNodeValue();
	return code.equals("B");
}

void buildLookup(Node node, HashMap table) {
	NodeList list = node.getChildNodes();
	for (int i = 0; i < list.getLength(); i++) {
		Node childNode = list.item(i);
		if (childNode.getNodeType() == Node.ELEMENT_NODE) {
			String key = getKey(childNode);
			if (table.get(key) == null) table.put(key, childNode);
			buildLookup(childNode, table);
		}
	}
}

boolean isUnique(Node method, ArrayList methods) {
	String methodName = method.getAttributes().getNamedItem("selector").getNodeValue();
	String signature = "";
	NodeList params = method.getChildNodes();
	for (int k = 0; k < params.getLength(); k++) {
		Node param = params.item(k);
		if ("arg".equals(param.getNodeName())) {
			signature += getJavaType(param);
		}
	}
	int index = methodName.indexOf(":");
	if (index != -1) methodName = methodName.substring(0, index);
	for (Iterator iterator = methods.iterator(); iterator.hasNext();) {
		Node other = (Node) iterator.next();
		NamedNodeMap attributes = other.getAttributes();
		Node otherSel = null;
		if (attributes != null) otherSel = attributes.getNamedItem("selector");
		if (other != method && otherSel != null) {
			String otherName = otherSel.getNodeValue();
			index = otherName.indexOf(":");
			if (index != -1) otherName = otherName.substring(0, index);
			if (methodName.equals(otherName)) {
				NodeList otherParams = other.getChildNodes();
				String otherSignature = "";
				for (int k = 0; k < otherParams.getLength(); k++) {
					Node param = otherParams.item(k);
					if ("arg".equals(param.getNodeName())) {
						otherSignature += getJavaType(param);
					}
				}
				if (signature.equals(otherSignature)) {
					return false;
				}
			}
		}
	}
	return true;
}

void generateSelectorsConst() {
	TreeSet set = new TreeSet();
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null) continue;
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if ("class".equals(node.getNodeName()) || "informal_protocol".equals(node.getNodeName())) {
				if (getGen(node)) {
					NodeList methods = node.getChildNodes();
					for (int j = 0; j < methods.getLength(); j++) {
						Node method = methods.item(j);
						if (getGen(method)) {
							NamedNodeMap mthAttributes = method.getAttributes();
							String sel = mthAttributes.getNamedItem("selector").getNodeValue();
							set.add(sel);
						}
					}
				}
			}
		}
	}
	if (set.size() > 0) {
		set.add("alloc");
		set.add("dealloc");
	}
	for (Iterator iterator = set.iterator(); iterator.hasNext();) {
		String sel = (String) iterator.next();
		String selConst = getSelConst(sel);
		out("public static final int /*long*/ ");
		out(selConst);
		out(" = ");
		out("sel_registerName(\"");
		out(sel);
		out("\");");
		outln();
	}
}

void generateStructNatives() {
	TreeSet set = new TreeSet();
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null) continue;
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if ("struct".equals(node.getNodeName()) && getGen(node)) {
				set.add(getIDAttribute(node).getNodeValue());
			}
		}
	}
	out("/** Sizeof natives */");
	outln();
	for (Iterator iterator = set.iterator(); iterator.hasNext();) {
		String struct = (String) iterator.next();
		out("public static final native int ");
		out(struct);
		out("_sizeof();");
		outln();
	}
	outln();
	out("/** Memmove natives */");
	outln();
	outln();
	for (Iterator iterator = set.iterator(); iterator.hasNext();) {
		String struct = (String) iterator.next();
		out("/**");
		outln();
		out(" * @param dest cast=(void *),flags=no_in critical");
		outln();
		out(" * @param src cast=(void *),flags=critical");
//		out(" * @param src cast=(void *),flags=no_out critical");
		outln();
		out(" */");
		outln();
		out("public static final native void memmove(");
		out("int /*long*/ dest, ");
		out(struct);
		out(" src, int /*long*/ size);");
		outln();
		out("/**");
		outln();
		out(" * @param dest cast=(void *),flags=no_in critical");
		outln();
		out(" * @param src cast=(void *),flags=critical");
//		out(" * @param src cast=(void *),flags=no_out critical");
		outln();
		out(" */");
		outln();
		out("public static final native void memmove(");
		out(struct);
		out(" dest, int /*long*/ src, int /*long*/ size);");
		outln();
	}
}

String buildSend(Node method, boolean tags, boolean only64, boolean superCall) {
	Node returnNode = getReturnNode(method.getChildNodes());
	StringBuffer buffer = new StringBuffer();
	buffer.append("public static final native "); 
	if (returnNode != null && isStruct(returnNode)) {
		buffer.append("void ");
		buffer.append(superCall ? "objc_msgSendSuper_stret" : "objc_msgSend_stret");
		buffer.append("(");
		buffer.append(getJavaType(returnNode));
		buffer.append(" result, ");
	} else if (returnNode != null && isFloatingPoint(returnNode)) {
		String type = getType(returnNode), type64 = getType64(returnNode);
		if (type.equals(type64) && type.equals("float")) {
			buffer.append("float ");
			buffer.append(superCall ? "objc_msgSendSuper_floatret" : "objc_msgSend_floatret");
		} else {
			buffer.append("double ");
			buffer.append(superCall ? "objc_msgSendSuper_fpret" : "objc_msgSend_fpret");
		}
		buffer.append("(");
	} else if (returnNode != null && isBoolean(returnNode)) {
		buffer.append("boolean ");
		buffer.append(superCall ? "objc_msgSendSuper_bool" : "objc_msgSend_bool");
		buffer.append("(");
	} else {
		if (only64) {
			buffer.append("long");
		} else {		
			if (tags) {
				buffer.append("int /*long*/");
			} else {
				buffer.append("int");
			}
		}
		buffer.append(" ");
		buffer.append(superCall ? "objc_msgSendSuper" : "objc_msgSend");
		buffer.append("(");
	}
	if (superCall) {
		if (only64) {
			buffer.append("objc_super superId, long sel");
		} else {
			if (tags) {
				buffer.append("objc_super superId, int /*long*/ sel");
			} else {
				buffer.append("objc_super superId, int sel");
			}
		}
	} else {
		if (only64) {
			buffer.append("long id, long sel");
		} else {
			if (tags) {
				buffer.append("int /*long*/ id, int /*long*/ sel");
			} else {
				buffer.append("int id, int sel");
			}
		}
	}
	NodeList params = method.getChildNodes();
	boolean first = false;
	int count = 0;
	for (int k = 0; k < params.getLength(); k++) {
		Node param = params.item(k);
		if ("arg".equals(param.getNodeName())) {
			if (!first) buffer.append(", ");
			if (isStruct(param)) {
				buffer.append(getJavaType(param));
			} else {
				String type = getType(param), type64 = getType64(param);
				buffer.append(only64 ? type64 : type);
				if (!only64 && tags && !type.equals(type64)) {
					buffer.append(" /*");
					buffer.append(type64);
					buffer.append("*/");
				}
			}
			first = false;
			buffer.append(" arg");
			buffer.append(String.valueOf(count++));
		}
	}
	buffer.append(");");
	return buffer.toString();
}

String getCType (Node node) {
	NamedNodeMap attributes = node.getAttributes();
	return attributes.getNamedItem("declared_type").getNodeValue();
}

Node findNSObjectMethod(Node method) {
	NamedNodeMap methodAttributes = method.getAttributes();
	String selector = methodAttributes.getNamedItem("selector").getNodeValue();
	NodeList list = method.getParentNode().getParentNode().getChildNodes();
	for (int i = 0; i < list.getLength(); i++) {
		Node cls = list.item(i);
		if ("class".equals(cls.getNodeName())) {
			NamedNodeMap classAttributes = cls.getAttributes();
			if ("NSObject".equals(classAttributes.getNamedItem("name").getNodeValue())) {
				NodeList methods = cls.getChildNodes();
				for (int j = 0; j < methods.getLength(); j++) {
					Node mth = methods.item(j);
					if ("method".equals(mth.getNodeName())) {
						NamedNodeMap mthAttributes = mth.getAttributes();
						if (selector.equals(mthAttributes.getNamedItem("selector").getNodeValue())) {
							return mth;
						}
					}
				}
			}
		}
	}
	return null;
}

void generateCustomCallbacks() {
	TreeMap set = new TreeMap();
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null) continue;
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (("class".equals(node.getNodeName()) || "informal_protocol".equals(node.getNodeName())) && getGen(node)) {
				NodeList methods = node.getChildNodes();
				for (int j = 0; j < methods.getLength(); j++) {
					Node method = methods.item(j);
					if ("method".equals(method.getNodeName()) && getGen(method) && getGenCallback(method)) {
						NamedNodeMap mthAttributes = method.getAttributes();
						String sel = mthAttributes.getNamedItem("selector").getNodeValue();
						set.put(sel, method);
					}
				}
			}
		}
	}
	for (Iterator iterator = set.keySet().iterator(); iterator.hasNext();) {
		String key = (String)iterator.next();
		Node method = (Node)set.get(key);
		if ("informal_protocol".equals(method.getParentNode().getNodeName())) {
			method = findNSObjectMethod(method);
			if (method == null) continue;
		}		
		String nativeMth = key.replaceAll(":", "_");
		out("/** @method callback_types=");
		Node returnNode = getReturnNode(method.getChildNodes());
		out(returnNode == null ? "void" : getCType(returnNode));
		out(";id;SEL;");
		NodeList params = method.getChildNodes();
		for (int k = 0; k < params.getLength(); k++) {
			Node param = params.item(k);
			if ("arg".equals(param.getNodeName())) {
				out(getCType(param));
				out(";");
			}
		}
		out(",callback_flags=");
		out(returnNode != null && isStruct(returnNode) ? "struct" : "none");
		out(";none;none;");
		for (int k = 0; k < params.getLength(); k++) {
			Node param = params.item(k);
			if ("arg".equals(param.getNodeName())) {
				out (isStruct(param) ? "struct" : "none");
				out(";");
			}
		}
		out(" */");
		outln();
		out("public static final native int /*long*/ CALLBACK_");
		out(nativeMth);
		out("(int /*long*/ func);");
		outln();
	}
}

void generateSends(boolean superCall) {
	TreeMap set = new TreeMap();
	TreeMap set64 = new TreeMap();
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null) continue;
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if ("class".equals(node.getNodeName()) && getGen(node)) {
				NodeList methods = node.getChildNodes();
				for (int j = 0; j < methods.getLength(); j++) {
					Node method = methods.item(j);
					if ("method".equals(method.getNodeName()) && getGen(method) && (!superCall || getGenSuper(method))) {
						String code = buildSend(method, false, false, superCall);
						String code64 = buildSend(method, false, true, superCall);
						if (set.get(code) == null) {
							set.put(code, method);
						}
						if (set64.get(code64) == null) {
							set64.put(code64, method);
						}
					}
				}
			}
		}
	}
	outln();
	TreeMap tagsSet = new TreeMap();
	for (Iterator iterator = set.keySet().iterator(); iterator.hasNext();) {
		String key = (String)iterator.next();
		Node method = (Node)set.get(key);
		String tagCode = buildSend(method, false, true, superCall);
		if (set64.get(tagCode) != null) {
			tagsSet.put(key, method);
			iterator.remove();
			set64.remove(tagCode);
		}
	}
	TreeMap all = new TreeMap();
	for (Iterator iterator = tagsSet.keySet().iterator(); iterator.hasNext();) {
		String key = (String) iterator.next();
		Node method = (Node)tagsSet.get(key);
		all.put(buildSend(method, true, false, superCall), method);
	}
	for (Iterator iterator = set.keySet().iterator(); iterator.hasNext();) {
		String key = (String) iterator.next();
		all.put(key, set.get(key));
	}
	for (Iterator iterator = set64.keySet().iterator(); iterator.hasNext();) {
		String key = (String) iterator.next();
		all.put(key, set64.get(key));
	}
	for (Iterator iterator = all.keySet().iterator(); iterator.hasNext();) {
		String key = (String)iterator.next();
		Node method = (Node)all.get(key);
		NodeList params = method.getChildNodes();
		ArrayList tags = new ArrayList();
		int count = 0;
		for (int k = 0; k < params.getLength(); k++) {
			Node param = params.item(k);
			if ("arg".equals(param.getNodeName())) {
				if (isStruct(param)) {
					tags.add(" * @param arg" + count + " flags=struct");
				}
				count++;
			}
		}
		out("/**");
		if (tags.size() > 0) {
			outln();
			out(" *");
		}
		out(" @method flags=cast");
		if (tags.size() > 0) outln();
		for (Iterator iterator2 = tags.iterator(); iterator2.hasNext();) {
			String tag = (String) iterator2.next();
			out(tag);
			outln();
		}
		out(" */");
		outln();
		out(key.toString());
		outln();
	}
}

String getSelConst(String sel) {
	return "sel_" + sel.replaceAll(":", "_");
}

void generateClassesConst() {
	TreeSet set = new TreeSet();
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null) continue;
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if ("class".equals(node.getNodeName())) {
				if (getGen(node)) {
					NamedNodeMap attributes = node.getAttributes();
					String name = attributes.getNamedItem("name").getNodeValue();
					set.add(name);
				}
			}
		}
	}
	for (Iterator iterator = set.iterator(); iterator.hasNext();) {
		String cls = (String) iterator.next();
		String clsConst = "class_" + cls;
		out("public static final int /*long*/ ");
		out(clsConst);
		out(" = ");
		out("objc_getClass(\"");
		out(cls);
		out("\");");
		outln();
	}
}

void generateProtocolsConst() {
	TreeSet set = new TreeSet();
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null) continue;
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if ("informal_protocol".equals(node.getNodeName())) {
				if (getGen(node)) {
					NamedNodeMap attributes = node.getAttributes();
					String name = attributes.getNamedItem("name").getNodeValue();
					set.add(name);
				}
			}
		}
	}
	for (Iterator iterator = set.iterator(); iterator.hasNext();) {
		String cls = (String) iterator.next();
		String clsConst = "protocol_" + cls;
		out("public static final int /*long*/ ");
		out(clsConst);
		out(" = ");
		out("objc_getProtocol(\"");
		out(cls);
		out("\");");
		outln();
	}
}

String getPackageName(String className) {
	int dot = mainClassName.lastIndexOf('.');
	if (dot == -1) return "";
	return mainClassName.substring(0, dot);
}

String getClassName(String className) {
	int dot = mainClassName.lastIndexOf('.');
	if (dot == -1) return mainClassName;
	return mainClassName.substring(dot + 1);
}

Node getReturnNode(NodeList list) {
	for (int j = 0; j < list.getLength(); j++) {
		Node node = list.item(j);
		if ("retval".equals(node.getNodeName())) {
			return node;
		}
	}
	return null;
}

String getType(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	Node javaType = attributes.getNamedItem("swt_java_type");
	if (javaType != null) return javaType.getNodeValue();
	Node type = attributes.getNamedItem("type");
	if (type == null) return "notype";
	String code = type.getNodeValue();
	return getType(code, attributes, false);
}

String getType64(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	Node javaType = attributes.getNamedItem("swt_java_type");
	if (javaType != null) {
		Node javaType64 = attributes.getNamedItem("swt_java_type64");
		return javaType64 != null ? javaType64.getNodeValue() : javaType.getNodeValue();
	}
	Node attrib = attributes.getNamedItem("type");
	if (attrib == null) return "notype";
	String code = attrib.getNodeValue();
	Node attrib64 = attributes.getNamedItem("type64");
	if (attrib64 != null) code = attrib64.getNodeValue();
	return getType(code, attributes, true);
}

String getType(String code, NamedNodeMap attributes, boolean is64) {
	if (code.equals("c")) return "byte";
	if (code.equals("i")) return "int";
	if (code.equals("s")) return "short";
	if (code.equals("l")) return "int";
	if (code.equals("q")) return "long";
	if (code.equals("C")) return "byte";
	if (code.equals("I")) return "int";
	if (code.equals("S")) return "short";
	if (code.equals("L")) return "int";
	if (code.equals("Q")) return "long";
	if (code.equals("f")) return "float";
	if (code.equals("d")) return "double";
	if (code.equals("B")) return "boolean";
	if (code.equals("v")) return "void";
	if (code.equals("*")) return is64 ? "long" : "int";
	if (code.equals("@")) return is64 ? "long" : "int";
	if (code.equals("#")) return is64 ? "long" : "int";
	if (code.equals(":")) return is64 ? "long" : "int";
	if (code.startsWith("^")) return is64 ? "long" : "int";
	if (code.startsWith("{")) {		
		return attributes.getNamedItem("declared_type").getNodeValue();
	}
    if (code.startsWith("@?")) {
        return is64 ? "long" : "int";
    }
	return "BAD " + code;
}

String getJNIType(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	String code = attributes.getNamedItem("type").getNodeValue();
	if (code.equals("c")) return "B";
	if (code.equals("i")) return "I";
	if (code.equals("s")) return "S";
	if (code.equals("l")) return "I";
	if (code.equals("q")) return "J";
	if (code.equals("C")) return "B";
	if (code.equals("I")) return "I";
	if (code.equals("S")) return "S";
	if (code.equals("L")) return "I";
	if (code.equals("Q")) return "J";
	if (code.equals("f")) return "F";
	if (code.equals("d")) return "D";
	if (code.equals("B")) return "Z";
	if (code.equals("v")) return "V";
	if (code.equals("*")) return "I";
	if (code.equals("@")) return "I";
	if (code.equals("#")) return "I";
	if (code.equals(":")) return "I";
	if (code.startsWith("^")) return "I";
    if (code.startsWith("@?")) return "I";
	if (code.startsWith("[")) return "BAD " + code;
	if (code.startsWith("{")) {		
		return "BAD " + code;
	}
	if (code.startsWith("(")) return "BAD " + code;
	return "BAD " + code;
}

String getJavaType(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	Node javaType = attributes.getNamedItem("swt_java_type");
	if (javaType != null) return javaType.getNodeValue().trim();
	Node type = attributes.getNamedItem("type");
	if (type == null) return "notype";
	String code = type.getNodeValue();
	return getJavaType(code, attributes, false);
}

String getJavaType64(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	Node javaType = attributes.getNamedItem("swt_java_type");
	if (javaType != null) {
		Node javaType64 = attributes.getNamedItem("swt_java_type64");
		return javaType64 != null ? javaType64.getNodeValue() : javaType.getNodeValue();
	}
	Node attrib = attributes.getNamedItem("type");
	if (attrib == null) return "notype";
	String code = attrib.getNodeValue();
	Node attrib64 = attributes.getNamedItem("type64");
	if (attrib64 != null) code = attrib64.getNodeValue();
	return getJavaType(code, attributes, true);
}
	
String getJavaType(String code, NamedNodeMap attributes, boolean is64) {
	if (code.equals("c")) return "byte";
	if (code.equals("i")) return "int";
	if (code.equals("s")) return "short";
	if (code.equals("l")) return "int";
	if (code.equals("q")) return "long";
	if (code.equals("C")) return "byte";
	if (code.equals("I")) return "int";
	if (code.equals("S")) return "short";
	if (code.equals("L")) return "int";
	if (code.equals("Q")) return "long";
	if (code.equals("f")) return "float";
	if (code.equals("d")) return "double";
	if (code.equals("B")) return "boolean";
	if (code.equals("v")) return "void";
	if (code.equals("*")) return is64 ? "long" : "int";
	if (code.equals("#")) return is64 ? "long" : "int";
	if (code.equals(":")) return is64 ? "long" : "int";
	if (code.startsWith("^")) return is64 ? "long" : "int";
	if (code.equals("@")) {
		String type = attributes.getNamedItem("declared_type").getNodeValue();
		int index = type.indexOf('*');
		if (index != -1) type = type.substring(0, index);
		index = type.indexOf('<');
		if (index != -1) type = type.substring(0, index);
		return type.trim();
	}
	if (code.startsWith("{")) {		
		return attributes.getNamedItem("declared_type").getNodeValue().trim();
	}
    if (code.startsWith("@?")) return is64 ? "long" : "int";
	return "BAD " + code;
}

static String[] split(String str, String separator) {
	StringTokenizer tk = new StringTokenizer(str, separator);
	ArrayList result = new ArrayList();
	while (tk.hasMoreElements()) {
		result.add(tk.nextElement());
	}
	return (String[])result.toArray(new String[result.size()]);
}

void generateFunctions() {
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null) continue;
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if ("function".equals(node.getNodeName())) {
				if (getGen(node)) {
					NamedNodeMap attributes = node.getAttributes();
					String name = attributes.getNamedItem("name").getNodeValue();
					NodeList params = node.getChildNodes();
					int count = 0;
					for (int j = 0; j < params.getLength(); j++) {
						Node param = params.item(j);
						if ("arg".equals(param.getNodeName())) {
							count++;
						}
					}
					if (count > 0) {
						out("/**");
						outln();
					}
					int argIndex = 0;
					for (int j = 0; j < params.getLength(); j++) {
						Node param = params.item(j);
						if ("arg".equals(param.getNodeName())) {
							out(" * @param ");
							out(getParamName(param, argIndex++));
							if (isStruct(param)) {
								out(" flags=struct");
							} else {
								out(" cast=");
								NamedNodeMap paramAttributes = param.getAttributes();
								Node swtCast = paramAttributes.getNamedItem("swt_param_cast");
								Node declaredType = swtCast != null ? swtCast : paramAttributes.getNamedItem("declared_type");
								String cast = declaredType.getNodeValue();
								if (!cast.startsWith("(")) out("(");
								out(cast);
								if (!cast.endsWith(")")) out(")");
							}
							outln();
						}
					}
					if (count > 0) {
						out(" */");
						outln();
					}
					out("public static final native ");
					Node returnNode = getReturnNode(node.getChildNodes());
					if (returnNode != null) {
						String type = getType(returnNode), type64 = getType64(returnNode);
						out(type);
						if (!type.equals(type64)) {
							out(" /*");
							out(type64);
							out("*/");
						}
						out(" ");
					} else {
						out("void ");
					}
					out(name);
					out("(");
					params = node.getChildNodes();
					boolean first = true;
					argIndex = 0;
					for (int j = 0; j < params.getLength(); j++) {
						Node param = params.item(j);
						if ("arg".equals(param.getNodeName())) {
							if (!first) out(", ");
							first = false;
							String type = getType(param), type64 = getType64(param);
							out(type);
							if (!type.equals(type64)) {
								out(" /*");
								out(type64);
								out("*/");
							}
							out(" ");
							out(getParamName(param, argIndex++));
						}
					}
					generateVariadics(node);
					out(");");
					outln();
				}
			}
		}
	}
}

void generateVariadics(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	Node variadicCount = attributes.getNamedItem("swt_variadic_count");
	if (variadicCount != null) {
		Node variadicTypes = attributes.getNamedItem("swt_variadic_java_types");
		String[] types = null;
		if (variadicTypes != null) {
			types = split(variadicTypes.getNodeValue(), ",");
		}
		int varCount = 0;
		try {
			varCount = Integer.parseInt(variadicCount.getNodeValue());
		} catch (NumberFormatException e) {}
		for (int j = 0; j < varCount; j++) {
			out(", ");
			if (types != null && types.length > j && !types[j].equals("*")) {
				out(types[j]);
			} else if (types != null && types[types.length - 1].equals("*")) {
				out(types[types.length - 2]);
			} else {
				out("int /*long*/");
			}
			out(" varArg");
			out("" + j);
		}
	}
}

public static void main(String[] args) {
	try {
		MacGenerator gen = new MacGenerator();
		gen.setXmls(args);
		gen.setOutputDir("../org.eclipse.swt/Eclipse SWT PI/cocoa/");
		gen.setMainClass("org.eclipse.swt.internal.cocoa.OS");
		gen.generate(null);
	} catch (Throwable e) {
		e.printStackTrace();
	}
}
}
