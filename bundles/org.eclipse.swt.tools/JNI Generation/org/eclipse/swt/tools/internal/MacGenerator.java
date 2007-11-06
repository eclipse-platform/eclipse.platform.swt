package org.eclipse.swt.tools.internal;


import java.util.HashSet;
import java.util.Iterator;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.w3c.dom.*;

public class MacGenerator {
	
public void out(String str) {
	System.out.print(str);
}

public void outln() {
	System.out.println();
}


public void generateConstants(NodeList list) {
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

public void generateConstantsMetaData(NodeList list) {
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

public void generateEnums(NodeList list) {
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

public void generateClasses(NodeList list) {
	for (int i = 0; i < list.getLength(); i++) {
		Node node = list.item(i);
		if ("class".equals(node.getLocalName())) {
			NamedNodeMap attributes = node.getAttributes();
			String name = attributes.getNamedItem("name").getNodeValue();
			if (name.equals("NSButton")) {
				out("public class ");
				out(name);
				if (!name.equals("NSObject")) out(" extends NSObject {");
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
						out("}");
						outln();
					}					
				}				
				out("}");
				outln();				
			}
		}
	}
}

public void generateSelectorsConst(NodeList list) {
	HashSet set = new HashSet();
	for (int i = 0; i < list.getLength(); i++) {
		Node node = list.item(i);
		if ("class".equals(node.getLocalName())) {
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
	for (Iterator iterator = set.iterator(); iterator.hasNext();) {
		String sel = (String) iterator.next();
		String selConst = "sel_" + sel.replaceAll(":", "_1");
		out("public static final int ");
		out(selConst);
		out(" = ");
		out("sel_registerName(\"");
		out(sel);
		out("\");");
		outln();
	}
}

public void generateClassesConst(NodeList list) {
	HashSet set = new HashSet();
	for (int i = 0; i < list.getLength(); i++) {
		Node node = list.item(i);
		if ("class".equals(node.getLocalName())) {
			NamedNodeMap attributes = node.getAttributes();
			set.add(attributes.getNamedItem("name").getNodeValue());
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

public void generateFunctions(NodeList list) {
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

public static void main(String[] args) throws Exception {
	DOMParser parser = new DOMParser();
	parser.parse(args[0]);
	Document document = parser.getDocument();
	NodeList list = document.getDocumentElement().getChildNodes();
	MacGenerator gen = new MacGenerator();
	gen.generateClasses(list);
}
}
