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
	String outputDir, mainClassName;
	String delimiter = System.getProperty("line.separator");
	
	PrintStream out;
	
public MacGenerator(String[] xmls) {
	this.xmls = xmls;
	documents = new Document[xmls.length];
	for (int i = 0; i < xmls.length; i++) {
//		long start = System.currentTimeMillis();
		Document document = documents[i] = getDocument(xmls[i]);
		if (document == null) continue;
		HashMap extras = loadExtraAttributes(xmls[i]);
		merge(document, document, extras);
//		for (Iterator iterator = extras.values().iterator(); iterator.hasNext();) {
//			Node node = (Node) iterator.next();
//			if (getGen(node)) {
//				Node attribName = getIDAttribute(node);
//				System.out.println(node.getNodeName() + " " + attribName.getNodeValue());
//			}
//		}
//		long end = System.currentTimeMillis();
//		System.out.println("load=" + (end - start));
	}
}

public void generateAll() {
	generateExtraAttributes();
	generateMainClass();
	generateClasses();
//	generateMetadata();
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

void generateMethods(String className, TreeMap methods) {
	Set methodSelectors = methods.keySet();
	for (Iterator iterator = methodSelectors.iterator(); iterator.hasNext();) {
		String selector = (String) iterator.next();
		Node method = (Node)methods.get(selector);
		NamedNodeMap mthAttributes = method.getAttributes();
		String sel = mthAttributes.getNamedItem("selector").getNodeValue();
		out("public ");
		boolean isStatic = mthAttributes.getNamedItem("class_method") != null; 
		if (isStatic) out("static ");
		Node returnNode = getReturnNode(method.getChildNodes());
		if (getType(returnNode).equals("void")) returnNode = null;
		String returnType = "";
		if (returnNode != null) {
			out(returnType = getJavaType(returnNode));
			out(" ");
		} else {
			out("void ");
		}
		String methodName = sel;
		if (isUnique(method, methods)) {
			int index = methodName.indexOf(":");
			if (index != -1) methodName = methodName.substring(0, index);
		} else {
			methodName = methodName.replaceAll(":", "_");
			if (isStatic) methodName = "static_" + methodName;
		}
		out(methodName);
		out("(");
		NodeList params = method.getChildNodes();
		boolean first = true;
		for (int k = 0; k < params.getLength(); k++) {
			Node param = params.item(k);
			if ("arg".equals(param.getNodeName())) {
				NamedNodeMap paramAttributes = param.getAttributes();
				if (!first) out(", ");
				out(getJavaType(param));
				first = false;
				out(" ");
				String paramName = paramAttributes.getNamedItem("name").getNodeValue();
				if (paramName.length() == 0) paramName = "arg" + paramAttributes.getNamedItem("index").getNodeValue();
				if (paramName.equals("boolean")) paramName = "b";
				out(paramName);
			}
		}
		out(") {");
		outln();
		if (returnNode != null && isStruct(returnNode)) {
			String type = getJavaType(returnNode);
			out("\t");
			out(type);
			out(" result = new ");
			out(type);
			out("();");
			outln();
			out("\tOS.objc_msgSend_stret(result, ");
		} else if (returnNode != null && isFloatingPoint(returnNode)) {
			String type = getJavaType(returnNode);
			out("\treturn ");
			if (type.equals("float")) out("(float)");
			out("OS.objc_msgSend_fpret(");
		} else if (returnNode != null && isObject(returnNode)) {
			out("\tint result = OS.objc_msgSend(");
		} else {
			if (returnNode != null) {
				out("\treturn ");
				String type = getJavaType(returnNode);
				if (!(type.equals("int") || type.equals("boolean"))) {
					out("(");
					out(type);
					out(")");
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
		for (int k = 0; k < params.getLength(); k++) {
			Node param = params.item(k);
			if ("arg".equals(param.getNodeName())) {
				NamedNodeMap paramAttributes = param.getAttributes();
				if (!first) out(", ");
				first = false;
				String paramName = paramAttributes.getNamedItem("name").getNodeValue();
				if (paramName.length() == 0) paramName = "arg" + paramAttributes.getNamedItem("index").getNodeValue();
				if (paramName.equals("boolean")) paramName = "b";
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
		if (returnNode != null && isBoolean(returnNode)) {
			out(" != 0");
		}
		out(";");
		outln();
		if (returnNode != null && isObject(returnNode)) {
			if (!isStatic && returnType.equals(className)) {
				out("\treturn result == this.id ? this : (result != 0 ? new ");
				out(returnType);
				out("(result) : null);");
			} else {
				out("\treturn result != 0 ? new ");
				out(returnType);
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

void generateClasses() {
	HashMap classes = new HashMap();
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null) continue;
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if ("class".equals(node.getNodeName()) && getGen(node)) {
				Map methods;
				NamedNodeMap attributes = node.getAttributes();
				String name = attributes.getNamedItem("name").getNodeValue();
				Object[] clazz = (Object[])classes.get(name);
				if (clazz == null) {
					methods = new TreeMap();
					classes.put(name, new Object[]{node, methods});
				} else {
					methods = (TreeMap)clazz[1];
				}
				NodeList methodList = node.getChildNodes();
				for (int j = 0; j < methodList.getLength(); j++) {
					Node method = methodList.item(j);
					if ("method".equals(method.getNodeName()) && getGen(method)) {
						NamedNodeMap mthAttributes = method.getAttributes();
						String selector = mthAttributes.getNamedItem("selector").getNodeValue();
						Node other = (Node)methods.get(selector);
						if (other == null) {
							methods.put(selector, method);
						} else {
							boolean isStatic = mthAttributes.getNamedItem("class_method") != null;
							boolean otherIsStatic = other.getAttributes().getNamedItem("class_method") != null;
							if (isStatic != otherIsStatic) {
								if (isStatic) {
									methods.put("static_" + selector, method);
									methods.put(selector, other);
								} else {
									methods.put("static_" + selector, other);
									methods.put(selector, method);
								}
							}
						}
					}					
				}
			}
		}
	}
	MetaData metaData = new MetaData(mainClassName);	
	Set classNames = classes.keySet();
	for (Iterator iterator = classNames.iterator(); iterator.hasNext();) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		this.out = new PrintStream(out);

		String className = (String) iterator.next();
		Object[] clazz = (Object[])classes.get(className);
		Node node = (Node)clazz[0];
		TreeMap methods = (TreeMap)clazz[1];
		NamedNodeMap attributes = node.getAttributes();
		String data = metaData.getMetaData("swt_copyright", null);
		if (data != null && data.length() != 0) {
			out(fixDelimiter(data));
		}
		out("package ");
		String packageName = getPackageName(mainClassName);
		out(packageName);
		out(";");
		outln();
		outln();
		out("public class ");
		out(className);
		if (className.equals("NSObject")) {
			out(" extends id {");
		} else {
			Node superclass = attributes.getNamedItem("swt_superclass");
			out(" extends ");
			if (superclass != null) {
				out(superclass.getNodeValue());
			} else {
				out("NSObject");
			}
			out(" {");
		}
		outln();
		outln();
		out("public ");
		out(className);
		out("() {");
		outln();
		out("\tsuper();");
		outln();
		out("}");
		outln();
		outln();
		out("public ");
		out(className);
		out("(int /*long*/ id) {");
		outln();
		out("\tsuper(id);");
		outln();
		out("}");
		outln();
		out("public ");
		out(className);
		out("(id id) {");
		outln();
		out("\tsuper(id);");
		outln();
		out("}");
		outln();
		if (className.equals("NSString")) {
			out("public String getString() {");
			outln();
			out("\treturn null;");
			outln();
			out("}");
			outln();
			out("public static NSString stringWith(String str) {");
			outln();
			out("\treturn null;");
			outln();
			out("}");
			outln();
		}
		outln();
		
		generateMethods(className, methods);
		
		out("}");
		outln();
		
		String fileName = outputDir + packageName.replace('.', '/') + "/" + className + ".java";
		try {
			out.flush();
			if (out.size() > 0) output(out.toByteArray(), fileName);
		} catch (Exception e) {
			System.out.println("Problem");
			e.printStackTrace(System.out);
		}
		out = null;
	}
}

void generateExtraAttributes() {
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null) continue;
		saveExtraAttributes(xmls[x], document);
	}
}

void generateMainClass() {
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	this.out = new PrintStream(out);

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
		footer = str.substring(end);
	} catch (IOException e) {
	} finally {
		try {
			if (is != null) is.close();
		} catch (IOException e) {}
	}
	
	out(header);
	outln();
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
	generateFunctions();
	outln();
	out("/** Sends */");
	outln();
	generateSends();
	
	outln();
	out(footer);
	try {
		out.flush();
		if (out.size() > 0) output(out.toByteArray(), fileName);
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

void generateMetadata() {
	generateConstantsMetaData();
	generateSendsMetaData();
}

public Document[] getDocuments() {
	return documents;
}

public String[] getXmls() {
	return xmls;
}

public boolean getEditable(Node node, String attribName) {
	String name = node.getNodeName();
	if (name.equals("method")) {
		if (attribName.equals("swt_vararg_max")) return true;
		if (attribName.equals("swt_not_static")) return true;
	} else if (name.equals("class")) {
		if (attribName.equals("swt_superclass")) return true;
	} else if (name.equals("retval") || name.equals("arg")) {
		if (attribName.equals("swt_java_type")) return true;
	}
	return false;
}

private HashMap loadExtraAttributes(String xmlPath) {
	HashMap table = new HashMap();
	String path = getFileName(xmlPath) + ".extras";
	File file = new File(getExtraAttributesDir());
	if (file.exists()) path = new File(file, path).getAbsolutePath();
	Document doc = getDocument(path);
	if (doc != null) buildExtrasLookup(doc, table);
	return table;
}

private void saveExtraAttributes(String xmlPath, Document document) {
	try {
		String fileName = getExtraAttributesDir() + getFileName(xmlPath) + ".extras";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DOMWriter writer = new DOMWriter(new PrintStream(out), false);
		String[] names = getIDAttributeNames();
		String[] filter = new String[names.length + 1];
		filter[0] = "swt_.*";
		System.arraycopy(names, 0, filter, 1, names.length);
		writer.setAttributeFilter(filter);
		writer.print(document);
		if (out.size() > 0) output(out.toByteArray(), fileName);
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

public void setOutputDir(String dir) {
	this.outputDir = dir;
}

public void setMainClass(String mainClassName) {
	this.mainClassName = mainClassName;
}

private Document getDocument(String xmlPath) {
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

public String[] getExtraAttributeNames() {
	return new String[]{
		"swt_gen",
		"swt_superclass",
		"swt_java_type",
		"swt_vararg_max",
		"swt_not_static",
	};
}

public String getFileName(String xmlPath) {
	String fileName = xmlPath;
	int index = fileName.lastIndexOf(File.separatorChar);
	if (index != -1) fileName = fileName.substring(index + 1);
	return fileName;
}

private String getKey (Node node) {
	StringBuffer buffer = new StringBuffer();
	while (node != null) {
		if (buffer.length() > 0) buffer.append("_");
		String name = node.getNodeName();
		StringBuffer key = new StringBuffer(name);
		Node nameAttrib = getIDAttribute(node);
		if (nameAttrib != null) {
			key.append("-");
			key.append(nameAttrib.getNodeValue());
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

void merge(Document document, Node node, HashMap extras) {
	if (extras == null) return;
	NodeList list = node.getChildNodes();
	for (int i = 0; i < list.getLength(); i++) {
		Node childNode = list.item(i);
		if (childNode.getNodeType() == Node.ELEMENT_NODE) {
			Node extra = (Node)extras.remove(getKey(childNode));
			if (extra != null) {
				NamedNodeMap attributes = extra.getAttributes();
				for (int j = 0, length = attributes.getLength(); j < length; j++) {
					Attr attr = (Attr)attributes.item(j);
					String name = attr.getNodeName();
					if (name.startsWith("swt_")) {
						Attr newAttr = document.createAttribute(name);
						newAttr.setNodeValue(attr.getNodeValue());
						((Element)childNode).setAttributeNode(newAttr);
					}
				}
			}
		}
		merge(document, childNode, extras);
	}
}

	
private void out(String str) {
	PrintStream out = this.out;
	if (out == null) out = System.out;
	out.print(str);
}

private void outln() {
	PrintStream out = this.out;
	if (out == null) out = System.out;
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
					out("public static final native int ");
					out(constName);
					out("();");
					outln();
					if (attributes.getNamedItem("declared_type").getNodeValue().equals("NSString*")) {
						out("public static final NSString ");
						out(constName);
						out("= new NSString(");
						out(constName);
						out("());");
						outln();
					}
				}
			}
		}		
	}
}

void generateConstantsMetaData() {
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null) continue;
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if ("constant".equals(node.getNodeName())) {
				NamedNodeMap attributes = node.getAttributes();
				out("OS_");
				out(attributes.getNamedItem("name").getNodeValue());
				out("=flags=const");
				outln();
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
							try {
								Integer.parseInt(value);
								out("int ");
							} catch (NumberFormatException e) {
								isLong = true;
								out("long ");
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

boolean isStruct(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	String code = attributes.getNamedItem("type").getNodeValue();
	return code.startsWith("{");
}

boolean isFloatingPoint(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	String code = attributes.getNamedItem("type").getNodeValue();
	return code.equals("f") || code.equals("d");
}

boolean isObject(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	String code = attributes.getNamedItem("type").getNodeValue();
	return code.equals("@");
}

boolean isBoolean(Node node) {
	NamedNodeMap attributes = node.getAttributes();
	String code = attributes.getNamedItem("type").getNodeValue();
	return code.equals("B");
}

String getExtraAttributesDir() {
	return "./Mac Generation/org/eclipse/swt/tools/internal/";
}

void buildExtrasLookup(Node node, HashMap table) {
	NodeList list = node.getChildNodes();
	for (int i = 0; i < list.getLength(); i++) {
		Node childNode = list.item(i);
		String key = getKey(childNode);
		table.put(key, childNode);
		buildExtrasLookup(childNode, table);
	}
}

boolean isUnique(Node method, TreeMap methods) {
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
	for (Iterator iterator = methods.values().iterator(); iterator.hasNext();) {
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
	HashSet set = new HashSet();
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
	for (Iterator iterator = set.iterator(); iterator.hasNext();) {
		String sel = (String) iterator.next();
		String selConst = getSelConst(sel);
		out("public static final int ");
		out(selConst);
		out(" = ");
		out("sel_registerName(\"");
		out(sel);
		out("\");");
		outln();
	}
}

void generateSends() {
	HashSet set = new HashSet();
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null) continue;
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if ("class".equals(node.getNodeName())) {
				if (getGen(node)) {
					NodeList methods = node.getChildNodes();
					for (int j = 0; j < methods.getLength(); j++) {
						Node method = methods.item(j);
						if ("method".equals(method.getNodeName())) {
							if (getGen(method)) {
								Node returnNode = getReturnNode(method.getChildNodes());
								StringBuffer buffer = new StringBuffer();
								buffer.append("public static final native "); 
								if (returnNode != null && isStruct(returnNode)) {
									buffer.append("void objc_msgSend_stret(");
									buffer.append(getJavaType(returnNode));
									buffer.append(" result, ");
								} else if (returnNode != null && isFloatingPoint(returnNode)) {
									buffer.append("double objc_msgSend_fpret(");
								} else {
									buffer.append("int objc_msgSend(");
								}
								buffer.append("int id, int sel");
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
											buffer.append(getType(param));
										}
										first = false;
										buffer.append(" arg");
										buffer.append(String.valueOf(count++));
									}
								}
								buffer.append(");");
								set.add(buffer.toString());
							}
						}
					}
				}
			}
		}
	}
	for (Iterator iterator = set.iterator(); iterator.hasNext();) {
		out(iterator.next().toString());
		outln();
	}
}

void generateSendsMetaData() {
	HashMap set = new HashMap();
	for (int x = 0; x < xmls.length; x++) {
		Document document = documents[x];
		if (document == null) continue;
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if ("class".equals(node.getNodeName())) {
//				NamedNodeMap attributes = node.getAttributes();
//				String name = attributes.getNamedItem("name").getNodeValue();
//				if (getGenerateClass(name)) {
					NodeList methods = node.getChildNodes();
					for (int j = 0; j < methods.getLength(); j++) {
						Node method = methods.item(j);
						if ("method".equals(method.getNodeName())) {
							Node returnNode = getReturnNode(method.getChildNodes());
							StringBuffer buffer = new StringBuffer();
							if (returnNode != null && isStruct(returnNode)) {
								buffer.append("OS_objc_1msgSend_1stret__");
								buffer.append("Lorg_eclipse_swt_internal_cocoa_");
								buffer.append(getJavaType(returnNode));
								buffer.append("_2");
							} else if (returnNode != null && isFloatingPoint(returnNode)) {
								buffer.append("OS_objc_1msgSend_1fpret__");
							} else {
								buffer.append("OS_objc_1msgSend__");
							}
							buffer.append("II");
							NodeList params = method.getChildNodes();
							for (int k = 0; k < params.getLength(); k++) {
								Node param = params.item(k);
								if ("arg".equals(param.getNodeName())) {
									if (isStruct(param)) {
										buffer.append("Lorg_eclipse_swt_internal_cocoa_");
										buffer.append(getJavaType(param));
										buffer.append("_2");
									} else {
										buffer.append(getJNIType(param));
									}
								}
							}
							String key = buffer.toString();
							if (set.get(key) == null) set.put(key, method);
						}
					}
//				}
			}
		}
	}
	for (Iterator iterator = set.keySet().iterator(); iterator.hasNext();) {
		String key = iterator.next().toString();
		out(key);
		out("=flags=cast");
		outln();
		int count = 2;
		if (key.indexOf("stret") != -1) {
			count = 3;
			out(key);
			out("_0=");
			outln();
			out(key);
			out("_1=cast=(id)");
			outln();
			out(key);
			out("_2=cast=(SEL)");
			outln();
		} else {
			out(key);
			out("_0=cast=(id)");
			outln();
			out(key);
			out("_1=cast=(SEL)");
			outln();
		}
		Node method = (Node)set.get(key);
		NodeList params = method.getChildNodes();
		for (int k = 0; k < params.getLength(); k++) {
			Node param = params.item(k);
			if ("arg".equals(param.getNodeName())) {
				out(key);
				out("_");
				out(String.valueOf(count));
				out("=");
				if (isStruct(param)) {
					out("flags=struct");
				}
				outln();
				count++;
			}
		}
		outln();
	}
}


String getSelConst(String sel) {
	return "sel_" + sel.replaceAll(":", "_1");
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
	String code = attributes.getNamedItem("type").getNodeValue();
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
	if (code.equals("*")) return "int";
	if (code.equals("@")) return "int";
	if (code.equals("#")) return "int";
	if (code.equals(":")) return "int";
	if (code.startsWith("^")) return "int";
	if (code.startsWith("[")) return "BAD " + code;
	if (code.startsWith("{")) {		
		return attributes.getNamedItem("declared_type").getNodeValue();
	}
	if (code.startsWith("(")) return "BAD " + code;
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
	if (javaType != null) return javaType.getNodeValue();
	String code = attributes.getNamedItem("type").getNodeValue();
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
	if (code.equals("*")) return "int";
	if (code.equals("@")) {
		String type = attributes.getNamedItem("declared_type").getNodeValue();
		int index = type.indexOf('*');
		if (index != -1) type = type.substring(0, index);
		index = type.indexOf('<');
		if (index != -1) type = type.substring(0, index);
		return type;
	}
	if (code.equals("#")) return "int";
	if (code.equals(":")) return "int";
	if (code.startsWith("^")) return "int";
	if (code.startsWith("[")) return "BAD " + code;
	if (code.startsWith("{")) {		
		return attributes.getNamedItem("declared_type").getNodeValue();
	}
	if (code.startsWith("(")) return "BAD " + code;
	return "BAD " + code;
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
					out("public static final native ");
					Node returnNode = getReturnNode(node.getChildNodes());
					if (returnNode != null) {
						out(getType(returnNode));
						out(" ");
					} else {
						out("void ");
					}
					out(name);
					out("(");
					NodeList params = node.getChildNodes();
					boolean first = true;
					for (int j = 0; j < params.getLength(); j++) {
						Node param = params.item(j);
						if ("arg".equals(param.getNodeName())) {
							NamedNodeMap paramAttributes = param.getAttributes();
							if (!first) out(", ");
							out(getType(param));
							first = false;
							out(" ");
							out(paramAttributes.getNamedItem("name").getNodeValue());
						}
					}
					out(");");
					outln();
				}
			}
		}
	}
}

boolean compare(InputStream is1, InputStream is2) throws IOException {
	while (true) {
		int c1 = is1.read();
		int c2 = is2.read();
		if (c1 != c2) return false;
		if (c1 == -1) break;
	}
	return true;
}

void output(byte[] bytes, String fileName) throws IOException {
	FileInputStream is = null;
	try {
		is = new FileInputStream(fileName);
		if (compare(new ByteArrayInputStream(bytes), new BufferedInputStream(is))) return;
	} catch (FileNotFoundException e) {
	} finally {
		try {
			if (is != null) is.close();
		} catch (IOException e) {}
	}
	FileOutputStream out = new FileOutputStream(fileName);
	out.write(bytes);
	out.close();
}

public static void main(String[] args) {
	try {
		MacGenerator gen = new MacGenerator(args);
		gen.setOutputDir("../org.eclipse.swt/Eclipse SWT PI/cocoa/");
		gen.setMainClass("org.eclipse.swt.internal.cocoa.OS");
		gen.generateAll();
	} catch (Throwable e) {
		e.printStackTrace();
	}
}
}
