package org.eclipse.swt.internal.cocoa;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.*;

public class MacGenerator {
	String[] classes;
	String xml;
	NodeList list;
	
public MacGenerator(String xml) throws Exception {
	this.xml = xml;
	DOMParser parser = new DOMParser();
	parser.parse(xml);
	Document document = parser.getDocument();
	list = document.getDocumentElement().getChildNodes();
}
	
public void out(String str) {
	System.out.print(str);
}

public void outln() {
	System.out.println();
}

public void generateConstants() {
	for (int i = 0; i < list.getLength(); i++) {
		Node node = list.item(i);
		if ("constant".equals(node.getLocalName())) {
			NamedNodeMap attributes = node.getAttributes();
			out("public static final native int ");
			out(attributes.getNamedItem("name").getNodeValue());
			out("();");
			outln();
		}
	}
}

public void generateConstantsMetaData() {
	for (int i = 0; i < list.getLength(); i++) {
		Node node = list.item(i);
		if ("constant".equals(node.getLocalName())) {
			NamedNodeMap attributes = node.getAttributes();
			out("OS_");
			out(attributes.getNamedItem("name").getNodeValue());
			out("=flags=const");
			outln();
		}
	}
}

public void generateEnums() {
	for (int i = 0; i < list.getLength(); i++) {
		Node node = list.item(i);
		if ("enum".equals(node.getLocalName())) {
			NamedNodeMap attributes = node.getAttributes();
			out("public static final int ");
			out(attributes.getNamedItem("name").getNodeValue());
			out(" = ");
			out(attributes.getNamedItem("value").getNodeValue());
			out(";");
			outln();
		}
	}
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

boolean getGenerateClass(String className) {
	if (classes != null) {
		for (int i = 0; i < classes.length; i++) {
			if (className.equals(classes[i])) return true;
		}
		return false;
	}
	return true;
}

public void generateClasses() {
	for (int i = 0; i < list.getLength(); i++) {
		Node node = list.item(i);
		if ("class".equals(node.getLocalName())) {
			NamedNodeMap attributes = node.getAttributes();
			String name = attributes.getNamedItem("name").getNodeValue();
			if (getGenerateClass(name)) {
				out("public class ");
				out(name);
				if (name.equals("NSObject")) {
					out(" extends id {");
				} else {
					out(" extends NSObject {");
				}
				outln();
				outln();
				out("public ");
				out(name);
				out("() {");
				outln();
				out("}");
				outln();
				outln();
				out("public ");
				out(name);
				out("(int id) {");
				outln();
				out("\tsuper(id);");
				outln();
				out("}");
				outln();
				outln();
				NodeList methods = node.getChildNodes();
				for (int j = 0; j < methods.getLength(); j++) {
					Node method = methods.item(j);
					if ("method".equals(method.getLocalName())) {
						String sel = method.getAttributes().getNamedItem("selector").getNodeValue();
						out("public ");
						boolean isStatic = method.getAttributes().getNamedItem("class_method") != null; 
						if (isStatic) out("static ");
						Node returnNode = getReturnNode(method.getChildNodes());
						if (returnNode != null) {
							out(getJavaType(returnNode));
							out(" ");
						} else {
							out("void ");
						}
						String methodName = sel;
						int index = methodName.indexOf(":");
						if (index != -1) methodName = methodName.substring(0, index);
						out(methodName);
						out("(");
						NodeList params = method.getChildNodes();
						boolean first = true;
						for (int k = 0; k < params.getLength(); k++) {
							Node param = params.item(k);
							if ("arg".equals(param.getLocalName())) {
								NamedNodeMap paramAttributes = param.getAttributes();
								if (!first) out(", ");
								out(getJavaType(param));
								first = false;
								out(" ");
								out(paramAttributes.getNamedItem("name").getNodeValue());
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
							if (returnNode != null) out("\treturn ");
							out("OS.objc_msgSend(");
						}
						if (isStatic) {
							out("OS.class_");
							out(name);
						} else {
							out("this.id");
						}
						out(", OS.");
						out(getSelConst(sel));
						first = false;
						for (int k = 0; k < params.getLength(); k++) {
							Node param = params.item(k);
							if ("arg".equals(param.getLocalName())) {
								NamedNodeMap paramAttributes = param.getAttributes();
								if (!first) out(", ");
								first = false;
								if (isObject(param)) {
									String pName = paramAttributes.getNamedItem("name").getNodeValue();
									out(pName);
									out(" != null ? ");
									out(pName);
									out(".id : 0");
								} else {
									out(paramAttributes.getNamedItem("name").getNodeValue());
								}
							}
						}
						out(")");
						if (isBoolean(returnNode)) {
							out(" != 0");
						}
						out(";");
						outln();
						if (returnNode != null && isObject(returnNode)) {
							out("\treturn result == this.id ? this : (result != 0 ? new ");
							out(getJavaType(returnNode));
							out("(result) : null);");
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
				out("}");
				outln();				
			}
		}
	}
}

public void generateSelectorsConst() {
	HashSet set = new HashSet();
	for (int i = 0; i < list.getLength(); i++) {
		Node node = list.item(i);
		if ("class".equals(node.getLocalName())) {
			NamedNodeMap attributes = node.getAttributes();
			String name = attributes.getNamedItem("name").getNodeValue();
			if (getGenerateClass(name)) {
				NodeList methods = node.getChildNodes();
				for (int j = 0; j < methods.getLength(); j++) {
					Node method = methods.item(j);
					if ("method".equals(method.getLocalName())) {
						String sel = method.getAttributes().getNamedItem("selector").getNodeValue();
						set.add(sel);
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

public void generateSends() {
	HashSet set = new HashSet();
	for (int i = 0; i < list.getLength(); i++) {
		Node node = list.item(i);
		if ("class".equals(node.getLocalName())) {
			NamedNodeMap attributes = node.getAttributes();
			String name = attributes.getNamedItem("name").getNodeValue();
			if (getGenerateClass(name)) {
				NodeList methods = node.getChildNodes();
				for (int j = 0; j < methods.getLength(); j++) {
					Node method = methods.item(j);
					if ("method".equals(method.getLocalName())) {
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
						buffer.append("int id, int sel, ");
						NodeList params = method.getChildNodes();
						boolean first = true;
						int count = 0;
						for (int k = 0; k < params.getLength(); k++) {
							Node param = params.item(k);
							if ("arg".equals(param.getLocalName())) {
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
	for (Iterator iterator = set.iterator(); iterator.hasNext();) {
		out(iterator.next().toString());
		outln();
	}
}

public void generateSendsMetaData() {
	HashMap set = new HashMap();
	for (int i = 0; i < list.getLength(); i++) {
		Node node = list.item(i);
		if ("class".equals(node.getLocalName())) {
			NamedNodeMap attributes = node.getAttributes();
			String name = attributes.getNamedItem("name").getNodeValue();
			if (getGenerateClass(name)) {
				NodeList methods = node.getChildNodes();
				for (int j = 0; j < methods.getLength(); j++) {
					Node method = methods.item(j);
					if ("method".equals(method.getLocalName())) {
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
							if ("arg".equals(param.getLocalName())) {
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
			}
		}
	}
	for (Iterator iterator = set.keySet().iterator(); iterator.hasNext();) {
		String key = iterator.next().toString();
		out(key);
		out("=");
		outln();
		out(key);
		out("_0=cast=(id)");
		outln();
		out(key);
		out("_1=cast=(SEL)");
		outln();
		Node method = (Node)set.get(key);
		NodeList params = method.getChildNodes();
		int count = 2;
		for (int k = 0; k < params.getLength(); k++) {
			Node param = params.item(k);
			if ("arg".equals(param.getLocalName())) {
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

public void generateClassesConst() {
	HashSet set = new HashSet();
	for (int i = 0; i < list.getLength(); i++) {
		Node node = list.item(i);
		if ("class".equals(node.getLocalName())) {
			NamedNodeMap attributes = node.getAttributes();
			String name = attributes.getNamedItem("name").getNodeValue();
			if (getGenerateClass(name)) {
				set.add(name);
			}
		}
	}
	for (Iterator iterator = set.iterator(); iterator.hasNext();) {
		String cls = (String) iterator.next();
		String clsConst = "class_" + cls;
		out("public static final int ");
		out(clsConst);
		out(" = ");
		out("objc_getClass(\"");
		out(cls);
		out("\");");
		outln();
	}
}

Node getReturnNode(NodeList list) {
	for (int j = 0; j < list.getLength(); j++) {
		Node node = list.item(j);
		if ("retval".equals(node.getLocalName())) {
			return node;
		}
	}
	return null;
}

String getType(Node node) {
	NamedNodeMap attributes = node.getAttributes();
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

public void generateFunctions() {
	for (int i = 0; i < list.getLength(); i++) {
		Node node = list.item(i);
		if ("function".equals(node.getLocalName())) {
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
				if ("arg".equals(param.getLocalName())) {
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

public void generateOS() {
	out("/** Classes */");
	outln();
	generateClassesConst();
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
}

public void generateMetadata() {
	generateConstantsMetaData();
	generateSendsMetaData();
}

public void setClasses(String[] classes) {
	this.classes = classes;
}

public static void main(String[] args) throws Exception {
	MacGenerator gen = new MacGenerator(args[0]);
//	gen.setClasses(new String[]{
//		"NSView",
//	});
	gen.generateOS();
}
}
