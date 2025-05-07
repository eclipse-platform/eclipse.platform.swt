package org.eclipse.swt.widgets;

public abstract class TableRenderer extends ControlRenderer {

	protected final Table table;

	protected TableRenderer(Table table) {
		super(table);
		this.table = table;
	}
}
