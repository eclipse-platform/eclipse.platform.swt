package org.eclipse.swt.examples.paint;

import org.eclipse.swt.graphics.*;

public class Tool {
	public int id;
	public String name;
	public String group;
	public int type;
	public Object action;
	public Image image = null;
	public Object data;
	
	public Tool(int id, String name, String group, int type) {
		super();
		this.id = id;
		this.name = name;
		this.group = group;
		this.type = type;
	}

	public Tool(int id, String name, String group, int type, Object data) {
		this(id, name, group, type);
		this.data = data;
	}
}
