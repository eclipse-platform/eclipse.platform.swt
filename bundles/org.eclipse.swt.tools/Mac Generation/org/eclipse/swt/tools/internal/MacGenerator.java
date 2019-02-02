/*******************************************************************************
 * Copyright (c) 2008, 2019 IBM Corporation and others.
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
 *     Thomas Wolf <thomas.wolf@paranor.ch> [534923] Annotate sends "fixedargs=2"
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.io.*;
import java.util.*;
import java.util.Map.*;
import java.util.stream.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.xml.sax.*;
@SuppressWarnings("unchecked")
public class MacGenerator {
	String[] xmls;
	Document[] documents;
	String outputDir, outputLibDir, extrasDir, mainClassName, selectorEnumName;
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

static void list(File path, ArrayList<String> list) {
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
	HashMap<String, Node> extras = new HashMap<>();
	buildLookup(extraDocument, extras);

	/* Merge attributes on existing elements building a lookup table for document */
	HashMap<String, Node> lookup = new HashMap<>();
	merge(document, extras, lookup);

	/*
	 * Merge new elements. Extras at this point contains only elements that were
	 * not found in the document.
	 */
	ArrayList<Node> sortedNodes = Collections.list(Collections.enumeration(extras.values()));
	Collections.sort(sortedNodes, (arg0, arg1) -> {
		int compare = getLevel(arg0) - getLevel(arg1);
		if (compare == 0) {
			return arg0.getNodeName().compareTo(arg1.getNodeName());
		}
		return compare;
	});
	String delimiter = System.getProperty("line.separator");
	for (Node node : sortedNodes) {
		String name = node.getNodeName();
		if ("arg".equals(name) || "retval".equals(name)) {
			if (!sortedNodes.contains(node.getParentNode())) continue;
		}
		Node parent = lookup.get(getKey(node.getParentNode()));
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
			Node attr = attributes.item(j);
			element.setAttribute(attr.getNodeName(), attr.getNodeValue());
		}
		lookup.put(getKey(element), element);
	}
}

public void generate(ProgressMonitor progress) {
	if (progress != null) {
		progress.setTotal(BUILD_C_SOURCE ? 5 : 4);
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
	generateSelectorEnum();
	if (progress != null) {
		progress.step();
		progress.setMessage("selector enum...");
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
	StringBuilder buffer = new StringBuilder();
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

void generateFields(ArrayList<Node> fields) {
	for (Node field : fields) {
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
			String clazz = getDeclaredType(fieldAttributes, field);
			out (clazz);
			out ("()");
		}
		out(";");
		outln();
	}
}

private String getDeclaredType(NamedNodeMap map, Node location) {
	Node declaredType = map.getNamedItem("declared_type");
	String value = declaredType != null ? declaredType.getNodeValue() : null;
	if(value == null) {
		System.err.printf("Unable to detect declared_type. Check bridge file! It might have been removed, inheritance changed, etc. It could also be an issue with gen_bridge_metadata. Location: %s %n", toDebugLocation(location));
		return "nodeclaredtype";
	}

	// strip any _Nullable and _Nonnull annotations
	value = value.replace("_Nullable", "").replace("_Nonnull", "").replace("_Null_unspecified", "");
	
	// strip greater-than (>) sign
	value = value.replace(">", "");

	// also strip __kindof keyword
	value = value.replace("__kindof", "");

	// anther generics thing is "ObjectType", however, those names can be arbitrary
	// let's to a catch-all for common names here and handle other cases individually via mapping in extras file
	value = value.replace("ObjectType", "id").replace("KeyType", "id");

	// also invalid: 'struct FSRef*' should be 'FSRef*'
	value = value.replace("struct ", "");

	// also remove any white spaces
	value = value.chars().filter((c)->!Character.isWhitespace(c)).mapToObj(c -> String.valueOf((char)c)).collect(Collectors.joining());

	return value;
}

void generateMethods(String className, ArrayList<Node> methods) {
	for (Node method : methods) {
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

			// convert "instancetype" to class name
			if(type.equals("instancetype")) {
				type = returnType = className;
				type64 = returnType64 = className;
			}

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

TreeMap<String, Object[]> getGeneratedClasses() {
	TreeMap<String, Object[]> classes = new TreeMap<>();
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null) continue;
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if ("class".equals(node.getNodeName()) && getGen(node)) {
				ArrayList<Node> methods;
				String name = node.getAttributes().getNamedItem("name").getNodeValue();
				Object[] clazz = classes.get(name);
				if (clazz == null) {
					methods = new ArrayList<>();
					classes.put(name, new Object[]{node, methods});
				} else {
					methods = (ArrayList<Node>)clazz[1];
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

TreeMap<String, Object[]> getGeneratedStructs() {
	TreeMap<String, Object[]> structs = new TreeMap<>();
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null) continue;
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if ("struct".equals(node.getNodeName()) && getGen(node)) {
				ArrayList<Node> fields;
				String name = node.getAttributes().getNamedItem("name").getNodeValue();
				Object[] clazz = structs.get(name);
				if (clazz == null) {
					fields = new ArrayList<>();
					structs.put(name, new Object[]{node, fields});
				} else {
					fields = (ArrayList<Node>)clazz[1];
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

void copyClassMethodsDown(final Map<String, Object[]> classes) {
	ArrayList<Object[]> sortedClasses = Collections.list(Collections.enumeration(classes.values()));
	Collections.sort(sortedClasses, new Comparator<Object>() {
		int getHierarchyLevel(Node node) {
			String superclass = getSuperclassName(node);
			int level = 0;
			while (!superclass.equals("id") && !superclass.equals("NSObject")) {
				level++;
				superclass = getSuperclassName((Node)classes.get(superclass)[0]);
			}
			return level;
		}
		@Override
		public int compare(Object arg0, Object arg1) {
			return getHierarchyLevel((Node)((Object[])arg0)[0]) - getHierarchyLevel((Node)((Object[])arg1)[0]);
		}
	});
	for (Object[] clazz : sortedClasses) {
		Node node = (Node)clazz[0];
		ArrayList<Node> methods = (ArrayList<Node>)clazz[1];
		Object[] superclass = classes.get(getSuperclassName(node));
		if (superclass != null) {
			for (Iterator<Node> iterator2 = ((ArrayList<Node>)superclass[1]).iterator(); iterator2.hasNext();) {
				Node method = iterator2.next();
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
	} 
	Node name = attributes.getNamedItem("name");
	if (name.getNodeValue().equals("NSObject")) {
		return "id";
	}
	return "NSObject";
}

void generateClasses() {
	MetaData metaData = new MetaData(mainClassName);
	TreeMap<String, Object[]> classes = getGeneratedClasses();
	copyClassMethodsDown(classes);

	for (Entry<String, Object[]> clazzes: classes.entrySet()) {
		CharArrayWriter out = new CharArrayWriter();
		this.out = new PrintWriter(out);

		out(fixDelimiter(metaData.getCopyright()));

		String className = clazzes.getKey();
		Object[] clazz = clazzes.getValue();
		Node node = (Node)clazz[0];
		ArrayList<Node> methods = (ArrayList<Node>)clazz[1];
		out("package ");
		String packageName = getPackageName();
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
	TreeMap<String, Object[]> structs = getGeneratedStructs();

	for (Entry<String, Object[]> structEntry: structs.entrySet()) {
		CharArrayWriter out = new CharArrayWriter();
		this.out = new PrintWriter(out);

		out(fixDelimiter(metaData.getCopyright()));

		String className = structEntry.getKey();
		Object[] clazz = structEntry.getValue();
		ArrayList<Node> methods = (ArrayList<Node>)clazz[1];
		out("package ");
		String packageName = getPackageName();
		out(packageName);
		out(";");
		outln();
		outln();
		out("public class ");
		out(className);
		out(" {");
		outln();
		generateFields(methods);
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
	try (FileInputStream is = new FileInputStream(fileName);
		InputStreamReader input = new InputStreamReader(new BufferedInputStream(is))){
		StringBuilder str = new StringBuilder();
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

void generateSelectorEnum() {
	CharArrayWriter out = new CharArrayWriter();
	this.out = new PrintWriter(out);

	String header = "", footer = "";
	String fileName = outputDir + selectorEnumName.replace('.', '/') + ".java";
	try (FileInputStream is = new FileInputStream(fileName);
		InputStreamReader input = new InputStreamReader(new BufferedInputStream(is))){
		StringBuilder str = new StringBuilder();
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
		input.close();
	} catch (IOException e) {
	}

	out(header);
	outln();
	outln();

	generateSelectorsEnumLiteral();
	
	out(";"); outln();
	
	String mainClassShortName = mainClassName.substring(mainClassName.lastIndexOf('.')+1);
	out("	final String name;"); outln();
	out("	final long value;"); outln();
	outln();
	out("	private Selector(String name) {"); outln();
	out("		this.name= name;"); outln();
	out("		this.value = "+mainClassShortName+".sel_registerName(name);"); outln();
	out("		"+mainClassShortName+".registerSelector(value,this);"); outln();
	out("	}"); outln();
	outln();
	out("	public static Selector valueOf(long value) {"); outln();
	out("		return "+mainClassShortName+".getSelector(value);"); outln();
	out("	}"); outln();
	
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
				String packageName = getPackageName();
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
		ArrayList<String> array = new ArrayList<>();
		if (USE_SYSTEM_BRIDGE_FILES) {
			list(new File("/System/Library/Frameworks"), array);
			list(new File("/System/Library/Frameworks/CoreServices.framework/Frameworks"), array);
			list(new File("/System/Library/Frameworks/ApplicationServices.framework/Frameworks"), array);
		} else {
			String packageName = getPackageName();
			File folder = new File(extrasDir != null ? extrasDir : outputDir + packageName.replace('.', '/'));
			File[] files = folder.listFiles((FilenameFilter) (dir, name) -> name.endsWith("Full.bridgesupport"));
			if(files == null) {
				files = new File[0];
			}
			for (int i = 0; i < files.length; i++) {
				array.add(files[i].getAbsolutePath());
			}
		}
		Collections.sort(array, (o1, o2) -> new File(o1).getName().compareTo(new File(o2).getName()));
		xmls = array.toArray(new String[array.size()]);
	}
	return xmls;
}

void saveExtraAttributes(String xmlPath, Document document) {
	try {
		String packageName = getPackageName();
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

public void setSelectorEnum(String selectorEnumName) {
	this.selectorEnumName = selectorEnumName;
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
	StringBuilder buffer = new StringBuilder();
	while (node != null) {
		if (buffer.length() > 0) buffer.append("_");
		String name = node.getNodeName();
		StringBuilder key = new StringBuilder(name);
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

void merge(Node node, HashMap<String, Node> extras, HashMap<String, Node> docLookup) {
	NodeList list = node.getChildNodes();
	for (int i = 0; i < list.getLength(); i++) {
		Node childNode = list.item(i);
		if (childNode.getNodeType() == Node.ELEMENT_NODE) {
			String key = getKey(childNode);
			if (docLookup != null && docLookup.get(key) == null) {
				docLookup.put(key, childNode);
			}
			Node extra = extras.remove(key);
			if (extra != null) {
				NamedNodeMap attributes = extra.getAttributes();
				for (int j = 0, length = attributes.getLength(); j < length; j++) {
					Node attr = attributes.item(j);
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
					if (getDeclaredType(attributes, node).equals("NSString*")) {
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
					} else {
						System.err.printf("No value for enum. Check bridge file! It might have been removed, renamed, etc. Location: %s %n", toDebugLocation(node));
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

void buildLookup(Node node, HashMap<String, Node> table) {
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

boolean isUnique(Node method, ArrayList<Node> methods) {
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
	for (Node other : methods) {
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
	TreeSet<String> set = new TreeSet<>();
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
	out ("private static java.util.Map<Long,Selector> SELECTORS;"); outln();
	out ("public static void registerSelector (Long value, Selector selector) {"); outln();
	out ("	if (SELECTORS == null) {"); outln();
	out ("		SELECTORS = new java.util.HashMap<>();"); outln();
	out ("	}"); outln();
	out ("	SELECTORS.put(value, selector);"); outln();
	out ("}"); outln();
	out ("public static Selector getSelector (long value) {"); outln();
	out ("	return SELECTORS.get(value);"); outln();
	out ("}"); outln();
	for (String sel : set) {
		String selConst = getSelConst(sel);
		out("public static final int /*long*/ ");
		out(selConst);
		out(" = ");
		out("Selector."+selConst+".value;");
		outln();
	}
}

void generateSelectorsEnumLiteral() {
	TreeSet<String> set = new TreeSet<>();
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
	for (String sel: set) {
		String selConst = getSelConst(sel);
		out("	, "+selConst+"(\""+sel+"\")");
		outln();
	}
}

void generateStructNatives() {
	TreeSet<String> set = new TreeSet<>();
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
	for (String struct : set) {
		out("public static final native int ");
		out(struct);
		out("_sizeof();");
		outln();
	}
	outln();
	out("/** Memmove natives */");
	outln();
	outln();
	for (String struct : set) {
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
	StringBuilder buffer = new StringBuilder();
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
	return getDeclaredType(attributes, node);
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
	TreeMap<String, Node> set = new TreeMap<>();
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
	for (Entry<String, Node> entry: set.entrySet()) {
		String key = entry.getKey();
		Node method = entry.getValue();
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
	TreeMap<String, Node> set = new TreeMap<>();
	TreeMap<String, Node> set64 = new TreeMap<>();
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
	TreeMap<String, Node> tagsSet = new TreeMap<>();
	for (Iterator<String> iterator = set.keySet().iterator(); iterator.hasNext();) {
		String key = iterator.next();
		Node method = set.get(key);
		String tagCode = buildSend(method, false, true, superCall);
		if (set64.get(tagCode) != null) {
			tagsSet.put(key, method);
			iterator.remove();
			set64.remove(tagCode);
		}
	}
	TreeMap<String, Node> all = new TreeMap<>();
	for (String key : tagsSet.keySet()) {
		Node method = tagsSet.get(key);
		all.put(buildSend(method, true, false, superCall), method);
	}
	for (String key : set.keySet()) {
		all.put(key, set.get(key));
	}
	for (String key : set64.keySet()) {
		all.put(key, set64.get(key));
	}
	for (String key : all.keySet()) {
		Node method = all.get(key);
		NodeList params = method.getChildNodes();
		ArrayList<String> tags = new ArrayList<>();
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
		out(" @method flags=cast,fixedargs=2");
		if (tags.size() > 0) outln();
		for (String tag : tags) {
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
	TreeSet<String> set = new TreeSet<>();
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
	for (String cls : set) {
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
	TreeSet<String> set = new TreeSet<>();
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
	for (String cls : set) {
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

String getPackageName() {
	int dot = mainClassName.lastIndexOf('.');
	if (dot == -1) return "";
	return mainClassName.substring(0, dot);
}

String getClassName() {
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
	if (type == null) {
		System.err.printf("Unable to detect type. Check bridge file! It might have been removed, inheritance changed, etc. Location: %s %n", toDebugLocation(node));
		return "notype";
	}
	String code = type.getNodeValue();
	return getType(code, attributes, false, node);
}

private String toDebugLocation(Node location) {
	StringBuilder result = new StringBuilder();
	while(location != null) {
		if(result.length() > 0) {
			result.insert(0, " > ");
		}
		result.insert(0, getNodeInfo(location));
		location = location.getParentNode();
	}
	return result.toString();
}

private String getNodeInfo(Node location) {
	String name = location.getNodeName();
	if (name != null) {
		NamedNodeMap attributes = location.getAttributes();
		if (attributes != null) {
			StringBuilder info = new StringBuilder();
			info.append(name).append("[");
			for (int i = 0; i < attributes.getLength(); i++) {
				Node attribute = attributes.item(i);
				if (i > 0) {
					info.append(", ");
				}
				info.append(attribute.getNodeName()).append("=").append(attribute.getNodeValue());
			}
			return info.append("]").toString();
		}
		return name;
	}
	return location.toString();
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
	return getType(code, attributes, true, node);
}

String getType(String code, NamedNodeMap attributes, boolean is64, Node location) {
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
		return getDeclaredType(attributes, location);
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
	return getJavaType(code, attributes, false, node);
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
	return getJavaType(code, attributes, true, node);
}

String getJavaType(String code, NamedNodeMap attributes, boolean is64, Node location) {
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
		String type = getDeclaredType(attributes, location);
		int index = type.indexOf('*');
		if (index != -1) type = type.substring(0, index);
		index = type.indexOf('<');
		if (index != -1) type = type.substring(0, index);
		return type.trim();
	}
	if (code.startsWith("{")) {
		return getDeclaredType(attributes, location).trim();
	}
    if (code.startsWith("@?")) return is64 ? "long" : "int";
	return "BAD " + code;
}

static String[] split(String str, String separator) {
	StringTokenizer tk = new StringTokenizer(str, separator);
	ArrayList<String> result = new ArrayList<>();
	while (tk.hasMoreTokens()) {
		result.add(tk.nextToken());
	}
	return result.toArray(new String[result.size()]);
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
								String cast = swtCast != null ? swtCast.getNodeValue(): getDeclaredType(paramAttributes, param);
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
		gen.setSelectorEnum("org.eclipse.swt.internal.cocoa.Selector");
		gen.generate(null);
	} catch (Throwable e) {
		e.printStackTrace();
	}
}
}
