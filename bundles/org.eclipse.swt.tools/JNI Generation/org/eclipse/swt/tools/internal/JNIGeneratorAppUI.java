/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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
import java.lang.reflect.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

public class JNIGeneratorAppUI {

	Display display;
	Shell shell;
	
	Composite actionsPanel;
	Combo mainClassCb, outputDirCb;
	Table classesLt, membersLt, paramsLt;
	ProgressBar progressBar;
	Label progressLabel;
	FileDialog fileDialog;
	
	TableEditor paramTextEditor, memberTextEditor, classTextEditor;
	FlagsEditor paramListEditor, memberListEditor, classListEditor;
	Text paramEditorTx, memberEditorTx, classEditorTx;
	List paramEditorLt, memberEditorLt, classEditorLt;
	
	static class FlagsEditor {
		Table parent;
		int column = -1;
		TableItem item;
		
		public FlagsEditor(Table parent) {
			this.parent = parent;
		}
		
		public int getColumn() {
			return column;
		}
		
		public TableItem getItem() {
			return item;
		}
		
		public void setColumn(int column) {
			this.column = column;
		}
		
		public void setItem(TableItem item) {
			this.item = item;
		}
	}
	
	JNIGeneratorApp app;

	static final int CLASS_NAME_COLUMN = 0;
	static final int CLASS_FLAGS_COLUMN = 1;
	static final int CLASS_EXCLUDE_COLUMN = 2;
	
	static final int FIELD_NAME_COLUMN = 0;
	static final int FIELD_FLAGS_COLUMN = 1;
	static final int FIELD_CAST_COLUMN = 2;
	static final int FIELD_ACCESSOR_COLUMN = 3;
	static final int FIELD_EXCLUDE_COLUMN = 4;
	
	static final int METHOD_NAME_COLUMN = 0;
	static final int METHOD_FLAGS_COLUMN = 1;
	static final int METHOD_ACCESSOR_COLUMN = 2;
	static final int METHOD_EXCLUDE_COLUMN = 3;
	
	static final int PARAM_INDEX_COLUMN = 0;
	static final int PARAM_TYPE_COLUMN = 1;
	static final int PARAM_FLAGS_COLUMN = 2;
	static final int PARAM_CAST_COLUMN = 3;
	
public JNIGeneratorAppUI() {
	this (new JNIGeneratorApp());
}

public JNIGeneratorAppUI(JNIGeneratorApp app) {
	this.app = app;
}

void cleanup() {
	display.dispose();
}

void generateStructsHeader () {
	StructsGenerator gen = new StructsGenerator(true);
	gen.setMainClass(app.getMainClass());
	gen.setMetaData(app.getMetaData());
	gen.setClasses(getSelectedClasses());
	gen.generate();
}

void generateStructs () {
	StructsGenerator gen = new StructsGenerator(false);
	gen.setMainClass(app.getMainClass());
	gen.setMetaData(app.getMetaData());
	gen.setClasses(getSelectedClasses());
	gen.generate();
}

void generateSizeof () {
	SizeofGenerator gen = new SizeofGenerator();
	gen.setMainClass(app.getMainClass());
	gen.setMetaData(app.getMetaData());
	gen.setClasses(getSelectedClasses());
	gen.generate();
}

void generateMetaData () {
	MetaDataGenerator gen = new MetaDataGenerator();
	gen.setMainClass(app.getMainClass());
	gen.setMetaData(app.getMetaData());
	JNIMethod[] methods = getSelectedMethods();
	if (methods.length != 0) {
		gen.generate(methods);
	} else {
		gen.setClasses(getSelectedClasses());
		gen.generate();
	}
}

void generateNatives () {
	NativesGenerator gen = new NativesGenerator();
	gen.setMainClass(app.getMainClass());
	gen.setMetaData(app.getMetaData());
	JNIMethod[] methods = getSelectedMethods();
	if (methods.length != 0) {
		gen.generate(methods);
	} else {
		gen.setClasses(getSelectedClasses());
		gen.generate();
	}
}

void generateAll() {
	if (!updateOutputDir()) return;
	Cursor cursor = display.getSystemCursor(SWT.CURSOR_WAIT);
	shell.setCursor(cursor);
	shell.setEnabled(false);
	Control[] children = actionsPanel.getChildren();
	for (int i = 0; i < children.length; i++) {
		Control child = children[i];
		if (child instanceof Button) child.setEnabled(false);				
	}
	boolean showProgress = true;
	final boolean finalShowProgress = showProgress; /* avoid dead code warning below */
	if (showProgress) {
		progressLabel.setText("");
		progressBar.setSelection(0);
		progressLabel.setVisible(true);
		progressBar.setVisible(true);
	}
	final boolean[] done = new boolean[1];
	new Thread() {
		public void run() {
			try {
				app.generate(!finalShowProgress ? null : new ProgressMonitor() {
					int total, step, maximum = 100;
					public void setTotal(final int total) {
						this.total = total;
						display.syncExec(new Runnable() {
							public void run() {
								progressBar.setMaximum(maximum);
							}
						});
					}
					public void step() {
						int oldValue = step * maximum / total;
						step++;
						final int newValue = step * maximum / total;
						if (oldValue == newValue) return;
						display.syncExec(new Runnable() {
							public void run() {
								progressBar.setSelection(newValue);
							}
						});					
					}
					public void setMessage(final String message) {
						display.syncExec(new Runnable() {
							public void run() {
								progressLabel.setText(message);
								progressLabel.update();
							}
						});
					}
				});
			} finally {
				done[0] = true;
				display.wake();
			}
		}
	}.start();
	while (!done[0]) {
		if (!display.readAndDispatch()) display.sleep();
	}
	for (int i = 0; i < children.length; i++) {
		Control child = children[i];
		if (child instanceof Button) child.setEnabled(true);				
	}
	if (showProgress) {
		progressBar.setVisible(false);
		progressLabel.setVisible(false);
	}
	shell.setEnabled(true);
	shell.setCursor(null);
}

void generateConstants () {
	ConstantsGenerator gen = new ConstantsGenerator();
	gen.setMainClass(app.getMainClass());
	gen.setMetaData(app.getMetaData());
	JNIField[] fields = getSelectedFields();
	if (fields.length != 0) {
		gen.generate(fields);
	} else {
		gen.setClasses(getSelectedClasses());
		gen.generate();
	}
}

JNIClass[] getSelectedClasses() {
	TableItem[] items = classesLt.getSelection();
	JNIClass[] classes = new JNIClass[items.length];
	for (int i = 0; i < items.length; i++) {
		TableItem item = items[i];
		classes[i] = (JNIClass)item.getData();
	}
	return classes;
}

JNIMethod[] getSelectedMethods() {
	TableItem[] selection = membersLt.getSelection();
	JNIMethod[] methods = new JNIMethod[selection.length];
	int count = 0;
	for (int i = 0; i < selection.length; i++) {
		TableItem item = selection [i];
		Object data = item.getData();
		if (data instanceof JNIMethod) {
			methods[count++] = (JNIMethod)data;
		}
	}
	if (count != methods.length) {
		JNIMethod[] result = new JNIMethod[count];
		System.arraycopy(methods, 0, result, 0, count);
		methods = result;
	}
	return methods;
}

JNIField[] getSelectedFields() {
	TableItem[] selection = membersLt.getSelection();
	JNIField[] fields = new JNIField[selection.length];
	int count = 0;
	for (int i = 0; i < selection.length; i++) {
		TableItem item = selection [i];
		Object data = item.getData();
		if (data instanceof JNIField) {
			fields[count++] = (JNIField)data;
		}
	}
	if (count != fields.length) {
		JNIField[] result = new JNIField[count];
		System.arraycopy(fields, 0, result, 0, count);
		fields = result;
	}
	return fields;
}

public void open () {
	display = new Display();
	shell = new Shell(display);
	shell.setText("JNI Generator");

	GridData data;
	GridLayout shellLayout = new GridLayout();
	shellLayout.numColumns = 2;
	shell.setLayout(shellLayout);
	
	Composite panel = new Composite(shell, SWT.NONE);
	data = new GridData(GridData.FILL_BOTH);
	panel.setLayoutData(data);
	
	GridLayout panelLayout = new GridLayout();
	panelLayout.numColumns = 1;
	panel.setLayout(panelLayout);
	
	Listener updateMainClassListener =  new Listener() {
		public void handleEvent(Event e) {
			updateMainClass();
			if (!updateOutputDir()) return;
			updateClasses();
			updateMembers();
			updateParameters();
		}
	};	
	createMainClassPanel(panel, updateMainClassListener);
	createClassesPanel(panel);
	createMembersPanel(panel);
	createParametersPanel(panel);
	createActionButtons(shell);

	Point preferredSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	shell.setSize(shell.getSize().x, preferredSize.y);
	
	updateMainClass();
	updateClasses();
	updateMembers();
	updateParameters();
}

void createMainClassPanel(Composite panel, Listener updateListener) {
	Label mainClassLb = new Label(panel, SWT.NONE);
	mainClassLb.setText("&Main Class:");

	GridData data;
	mainClassCb = new Combo(panel, SWT.DROP_DOWN);
	String mainClass = app.getMainClassName();
	mainClassCb.setText(mainClass == null ? "" : mainClass);
	data = new GridData(GridData.FILL_HORIZONTAL);
	mainClassCb.setLayoutData(data);
	mainClassCb.addListener(SWT.Selection, updateListener);
	mainClassCb.addListener(SWT.DefaultSelection, updateListener);

	Label outputDirLb = new Label(panel, SWT.NONE);
	outputDirLb.setText("&Output Dir:");
	
	outputDirCb = new Combo(panel, SWT.DROP_DOWN);
	String outputDir = app.getOutputDir();
	outputDirCb.setText(outputDir == null ? "" : outputDir);
	data = new GridData(GridData.FILL_HORIZONTAL);
	outputDirCb.setLayoutData(data);
	outputDirCb.addListener(SWT.Selection, updateListener);
	outputDirCb.addListener(SWT.DefaultSelection, updateListener);

	String mainClasses = app.getMetaData().getMetaData("swt_main_classes", null);
	if (mainClasses != null) {
		String[] list = JNIGenerator.split(mainClasses, ",");
		for (int i = 0; i < list.length; i += 2) {
			String className = list[i].trim();
			try {
				Class.forName(className, false, getClass().getClassLoader());
				mainClassCb.add(className);
				outputDirCb.add(list[i + 1].trim());
			} catch (Exception e) {}
		}
	}
}

void createClassesPanel(Composite panel) {
	Label classesLb = new Label(panel, SWT.NONE);
	classesLb.setText("&Classes:");

	GridData data;
	classesLt = new Table(panel, SWT.CHECK | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
	data = new GridData(GridData.FILL_BOTH);
	data.heightHint = classesLt.getItemHeight() * 6;
	classesLt.setLayoutData(data);
	classesLt.setHeaderVisible(true);
	classesLt.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event e) {
			if (e.detail == SWT.CHECK) {
				updateGenerate((TableItem)e.item);
			} else {
				updateMembers();
				updateParameters();
			}
		}
	});
	TableColumn column;
	column = new TableColumn(classesLt, SWT.NONE, CLASS_NAME_COLUMN);
	column.setText("Class");
	column = new TableColumn(classesLt, SWT.NONE, CLASS_FLAGS_COLUMN);
	column.setText("Flags");
	/*
	column = new TableColumn(classesLt, SWT.NONE, CLASS_EXCLUDE_COLUMN);
	column.setText("Exclude");
	*/
	
	classTextEditor = new TableEditor(classesLt);
	classTextEditor.grabHorizontal = true;
	classEditorTx = new Text(classesLt, SWT.SINGLE);
	classTextEditor.setEditor(classEditorTx);
	Listener classTextListener = new Listener() {
		public void handleEvent(Event e) {
			if (e.type == SWT.Traverse) {
				switch (e.detail) {
					case SWT.TRAVERSE_ESCAPE:
						classTextEditor.setItem(null);
						break;
					default:
						return;
				}
			}
			classEditorTx.setVisible(false);
			TableItem item = classTextEditor.getItem();
			if (item == null) return;
			int column = classTextEditor.getColumn();
			JNIClass clazz = (JNIClass)item.getData();
			if (column == CLASS_EXCLUDE_COLUMN) {
				String text = classEditorTx.getText();
				clazz.setExclude(text);
				item.setText(column, clazz.getExclude());
				classesLt.getColumn(column).pack();
			}
		}
	};
	classEditorTx.addListener(SWT.DefaultSelection, classTextListener);
	classEditorTx.addListener(SWT.FocusOut, classTextListener);
	classEditorTx.addListener(SWT.Traverse, classTextListener);
	
	final Shell floater = new Shell(shell, SWT.NO_TRIM);
	floater.setLayout(new FillLayout());
	classListEditor = new FlagsEditor(classesLt);
	classEditorLt = new List(floater, SWT.MULTI | SWT.BORDER);
	classEditorLt.setItems(JNIClass.FLAGS);
	floater.pack();
	floater.addListener(SWT.Close, new Listener() {
		public void handleEvent(Event e) {
			classListEditor.setItem(null);
			e.doit = false;
			floater.setVisible(false);
		}
	});
	Listener classesListListener = new Listener() {
		public void handleEvent(Event e) {
			if (e.type == SWT.Traverse) {
				switch (e.detail) {
					case SWT.TRAVERSE_RETURN:
						break;
					default:
						return;
				}
			}
			floater.setVisible(false);
			TableItem item = classListEditor.getItem();
			if (item == null) return;
			int column = classListEditor.getColumn();
			JNIClass clazz = (JNIClass)item.getData();
			if (column == CLASS_FLAGS_COLUMN) {
				String[] flags = classEditorLt.getSelection();
				clazz.setFlags(flags);
				item.setText(column, getFlagsString(clazz.getFlags()));
				item.setChecked(clazz.getGenerate());
				classesLt.getColumn(column).pack();
			}
		}
	};
	classEditorLt.addListener(SWT.DefaultSelection, classesListListener);
	classEditorLt.addListener(SWT.FocusOut, classesListListener);
	classEditorLt.addListener(SWT.Traverse, classesListListener);

	classesLt.addListener(SWT.MouseDown, new Listener() {
		public void handleEvent(final Event e) {
			e.display.asyncExec (new Runnable () {
				public void run () {
					if (classesLt.isDisposed ()) return;
					if (e.button != 1) return;
					Point pt = new Point(e.x, e.y);
					TableItem item = classesLt.getItem(pt);
					if (item == null) return;
					int column = -1;
					for (int i = 0; i < classesLt.getColumnCount(); i++) {
						if (item.getBounds(i).contains(pt)) {
							column = i;
							break;
						}				
					}
					if (column == -1) return;
					JNIClass data = (JNIClass)item.getData();
					if (column == CLASS_EXCLUDE_COLUMN) {
						classTextEditor.setColumn(column);
						classTextEditor.setItem(item);
						classEditorTx.setText(data.getExclude());
						classEditorTx.selectAll();
						classEditorTx.setVisible(true);
						classEditorTx.setFocus();
					} else if (column == CLASS_FLAGS_COLUMN) {
						classListEditor.setColumn(column);
						classListEditor.setItem(item);
						classEditorLt.setSelection(data.getFlags());
						floater.setLocation(classesLt.toDisplay(e.x, e.y));
						floater.setVisible(true);
						classEditorLt.setFocus();
					}
				}
			});
		}
	});
}

void createMembersPanel(Composite panel) {
	GridData data;
	Composite comp = new Composite(panel, SWT.NONE);
	data = new GridData(GridData.FILL_HORIZONTAL);
	comp.setLayoutData(data);	
	GridLayout layout = new GridLayout(2, false);
	layout.marginWidth = layout.marginHeight = 0;
	comp.setLayout(layout);
	Label membersLb = new Label(comp, SWT.NONE);
	membersLb.setText("Mem&bers [regex]:");
	final Text searchText = new Text(comp, SWT.SINGLE | SWT.SEARCH);
	searchText.setText(".*");
	data = new GridData(GridData.FILL_HORIZONTAL);
	searchText.setLayoutData(data);
	searchText.addListener(SWT.DefaultSelection, new Listener() {
		boolean match (int index, String pattern) {
			TableItem item = membersLt.getItem(index);
			String text = item.getText();
			try {
				if (text.matches(pattern)) {
					membersLt.setSelection(index);
					return true;
				}
			} catch (Exception ex) {}
			return false;
		}
		public void handleEvent(Event e) {
			String pattern = searchText.getText();
			int selection = membersLt.getSelectionIndex();
			int count = membersLt.getItemCount();
			selection++;
			for (int i = selection; i < count; i++) {
				if (match (i, pattern)) return;
			}
			for (int i = 0; i < selection; i++) {
				if (match (i, pattern)) return;
			}
		}
	});

	membersLt = new Table(panel, SWT.CHECK | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
	data = new GridData(GridData.FILL_BOTH);
	data.heightHint = membersLt.getItemHeight() * 6;
	membersLt.setLayoutData(data);
	membersLt.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event e) {
			if (e.detail == SWT.CHECK) {
				updateGenerate((TableItem)e.item);
			} else {
				updateParameters();
			}
		}
	});
	
	memberTextEditor = new TableEditor(membersLt);
	memberTextEditor.grabHorizontal = true;
	memberEditorTx = new Text(membersLt, SWT.SINGLE);
	memberTextEditor.setEditor(memberEditorTx);
	Listener memberTextListener = new Listener() {
		public void handleEvent(Event e) {
			if (e.type == SWT.Traverse) {
				switch (e.detail) {
					case SWT.TRAVERSE_ESCAPE:
						memberTextEditor.setItem(null);
						break;
					default:
						return;
				}
			}
			memberEditorTx.setVisible(false);
			TableItem item = memberTextEditor.getItem();
			if (item == null) return;
			int column = memberTextEditor.getColumn();
			JNIItem memberData = (JNIItem)item.getData();
			String text = memberEditorTx.getText();
			if (memberData instanceof JNIField) {
				JNIField field = (JNIField)memberData;
				switch (column) {
					case FIELD_CAST_COLUMN: {
						field.setCast(text);
						item.setText(column, field.getCast());
						break;
					}
					case FIELD_ACCESSOR_COLUMN: {
						field.setAccessor(text.equals(field.getName()) ? "" : text);
						item.setText(column, field.getAccessor());
						break;
					}
					case FIELD_EXCLUDE_COLUMN: {
						field.setExclude(text);
						item.setText(column, field.getExclude());
						break;
					}
				}
				membersLt.getColumn(column).pack();
			} else if (memberData instanceof JNIMethod) {
				JNIMethod method = (JNIMethod)memberData;
				switch (column) {
					case METHOD_ACCESSOR_COLUMN: {
						method.setAccessor(text.equals(method.getName()) ? "" : text);
						item.setText(column, method.getAccessor());
						break;
					}
					case METHOD_EXCLUDE_COLUMN: {
						method.setExclude(text);
						item.setText(column, method.getExclude());
						break;
					}
				}
				membersLt.getColumn(column).pack();
			}
		}
	};
	memberEditorTx.addListener(SWT.DefaultSelection, memberTextListener);
	memberEditorTx.addListener(SWT.FocusOut, memberTextListener);
	memberEditorTx.addListener(SWT.Traverse, memberTextListener);
	
	final Shell floater = new Shell(shell, SWT.NO_TRIM);
	floater.setLayout(new FillLayout());
	memberListEditor = new FlagsEditor(membersLt);
	memberEditorLt = new List(floater, SWT.MULTI | SWT.BORDER);
	floater.addListener(SWT.Close, new Listener() {
		public void handleEvent(Event e) {
			memberListEditor.setItem(null);
			e.doit = false;
			floater.setVisible(false);
		}
	});
	Listener memberListListener = new Listener() {
		public void handleEvent(Event e) {
			if (e.type == SWT.Traverse) {
				switch (e.detail) {
					case SWT.TRAVERSE_RETURN:
						break;
					default:
						return;
				}
			}
			floater.setVisible(false);
			TableItem item = memberListEditor.getItem();
			if (item == null) return;
			int column = memberListEditor.getColumn();
			JNIItem data = (JNIItem)item.getData();
			String[] flags = memberEditorLt.getSelection();
			data.setFlags(flags);
			item.setText(column, getFlagsString(data.getFlags()));
			item.setChecked(data.getGenerate());
			membersLt.getColumn(column).pack();
		}
	};
	memberEditorLt.addListener(SWT.DefaultSelection, memberListListener);
	memberEditorLt.addListener(SWT.FocusOut, memberListListener);
	memberEditorLt.addListener(SWT.Traverse, memberListListener);
	
	membersLt.addListener(SWT.MouseDown, new Listener() {
		public void handleEvent(final Event e) {
			e.display.asyncExec (new Runnable () {
				public void run () {
					if (membersLt.isDisposed ()) return;
					if (e.button != 1) return;
					Point pt = new Point(e.x, e.y);
					TableItem item = membersLt.getItem(pt);
					if (item == null) return;
					int column = -1;
					for (int i = 0; i < membersLt.getColumnCount(); i++) {
						if (item.getBounds(i).contains(pt)) {
							column = i;
							break;
						}				
					}
					if (column == -1) return;
					Object itemData = item.getData();
					if (itemData instanceof JNIField) {
						JNIField field = (JNIField)itemData;
						if (column == FIELD_CAST_COLUMN || column == FIELD_ACCESSOR_COLUMN || column == FIELD_EXCLUDE_COLUMN) {
							memberTextEditor.setColumn(column);
							memberTextEditor.setItem(item);
							String text = "";
							switch (column) {
								case FIELD_CAST_COLUMN: text = field.getCast(); break;
								case FIELD_ACCESSOR_COLUMN: {
									text = field.getAccessor(); 
									if (text.length() == 0) {
										text = field.getName();
										int index = text.lastIndexOf('_');
										if (index != -1) {
											char[] chars = text.toCharArray();
											chars[index] = '.';
											text = new String(chars);
										}
									}
									break;
								}
								case FIELD_EXCLUDE_COLUMN: text = field.getExclude(); break;
							}
							memberEditorTx.setText(text);
							memberEditorTx.selectAll();
							memberEditorTx.setVisible(true);
							memberEditorTx.setFocus();
						} else if (column == FIELD_FLAGS_COLUMN) {
							memberListEditor.setColumn(column);
							memberListEditor.setItem(item);
							memberEditorLt.setItems(JNIField.FLAGS);
							memberEditorLt.setSelection(field.getFlags());
							floater.setLocation(membersLt.toDisplay(e.x, e.y));
							floater.pack();
							floater.setVisible(true);
							memberEditorLt.setFocus();
						}
					} else if (itemData instanceof JNIMethod) {
						JNIMethod method = (JNIMethod)itemData;
						if (column == METHOD_EXCLUDE_COLUMN || column == METHOD_ACCESSOR_COLUMN) {
							memberTextEditor.setColumn(column);
							memberTextEditor.setItem(item);
							String text = "";
							switch (column) {
								case METHOD_ACCESSOR_COLUMN: {
									text = method.getAccessor();
									if (text.length() == 0) text = method.getName();
									break;
								}
								case METHOD_EXCLUDE_COLUMN: text = method.getExclude(); break;
							}
							memberEditorTx.setText(text);
							memberEditorTx.selectAll();
							memberEditorTx.setVisible(true);
							memberEditorTx.setFocus();
						} else if (column == METHOD_FLAGS_COLUMN) {
							memberListEditor.setColumn(column);
							memberListEditor.setItem(item);
							memberEditorLt.setItems(JNIMethod.FLAGS);
							memberEditorLt.setSelection(method.getFlags());
							floater.setLocation(membersLt.toDisplay(e.x, e.y));
							floater.pack();
							floater.setVisible(true);
							memberEditorLt.setFocus();
						}
					}
				}
			});
		}
	});
}

void createParametersPanel(Composite panel) {
	Label paramsLb = new Label(panel, SWT.NONE);
	paramsLb.setText("&Parameters:");
	
	GridData data;
	paramsLt = new Table(panel, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
	data = new GridData(GridData.FILL_BOTH);
	int itemHeight = paramsLt.getItemHeight();
	data.heightHint = itemHeight * 6;
	paramsLt.setLayoutData(data);
	paramsLt.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event e) {
			if (e.detail == SWT.CHECK) {
				updateGenerate((TableItem)e.item);
			}
		}
	});

	TableColumn column;
	column = new TableColumn(paramsLt, SWT.NONE, PARAM_INDEX_COLUMN);
	column = new TableColumn(paramsLt, SWT.NONE, PARAM_TYPE_COLUMN);
	column.setText("Type");
	column = new TableColumn(paramsLt, SWT.NONE, PARAM_FLAGS_COLUMN);
	column.setText("Flags");
	column = new TableColumn(paramsLt, SWT.NONE, PARAM_CAST_COLUMN);
	column.setText("Cast");
	
	paramTextEditor = new TableEditor(paramsLt);
	paramTextEditor.grabHorizontal = true;
	paramEditorTx = new Text(paramsLt, SWT.SINGLE);
	paramTextEditor.setEditor(paramEditorTx);
	Listener paramTextListener = new Listener() {
		public void handleEvent(Event e) {
			if (e.type == SWT.Traverse) {
				switch (e.detail) {
					case SWT.TRAVERSE_ESCAPE:
						paramTextEditor.setItem(null);
						break;
					default:
						return;
				}
			}
			paramEditorTx.setVisible(false);
			TableItem item = paramTextEditor.getItem();
			if (item == null) return;
			int column = paramTextEditor.getColumn();
			JNIParameter param = (JNIParameter)item.getData();
			if (column == PARAM_CAST_COLUMN) {
				String text = paramEditorTx.getText();
				param.setCast(text);
				item.setText(column, param.getCast());
				paramsLt.getColumn(column).pack();
			}
		}
	};
	paramEditorTx.addListener(SWT.DefaultSelection, paramTextListener);
	paramEditorTx.addListener(SWT.FocusOut, paramTextListener);
	paramEditorTx.addListener(SWT.Traverse, paramTextListener);
	
	final Shell floater = new Shell(shell, SWT.NO_TRIM);
	floater.setLayout(new FillLayout());
	paramListEditor = new FlagsEditor(paramsLt);
	paramEditorLt = new List(floater, SWT.MULTI | SWT.BORDER);
	paramEditorLt.setItems(JNIParameter.FLAGS);
	floater.pack();
	floater.addListener(SWT.Close, new Listener() {
		public void handleEvent(Event e) {
			paramListEditor.setItem(null);
			e.doit = false;
			floater.setVisible(false);
		}
	});
	Listener paramListListener = new Listener() {
		public void handleEvent(Event e) {
			if (e.type == SWT.Traverse) {
				switch (e.detail) {
					case SWT.TRAVERSE_RETURN:
						break;
					default:
						return;
				}
			}
			floater.setVisible(false);
			TableItem item = paramListEditor.getItem();
			if (item == null) return;
			int column = paramListEditor.getColumn();
			JNIParameter param = (JNIParameter)item.getData();
			if (column == PARAM_FLAGS_COLUMN) {
				String[] flags = paramEditorLt.getSelection();
				param.setFlags(flags);
				item.setText(column, getFlagsString(param.getFlags()));
				paramsLt.getColumn(column).pack();
			}
		}
	};
	paramEditorLt.addListener(SWT.DefaultSelection, paramListListener);
	paramEditorLt.addListener(SWT.FocusOut, paramListListener);
	paramEditorLt.addListener(SWT.Traverse, paramListListener);

	paramsLt.addListener(SWT.MouseDown, new Listener() {
		public void handleEvent(final Event e) {
			e.display.asyncExec (new Runnable () {
				public void run () {
					if (paramsLt.isDisposed ()) return;
					if (e.button != 1) return;
					Point pt = new Point(e.x, e.y);
					TableItem item = paramsLt.getItem(pt);
					if (item == null) return;
					int column = -1;
					for (int i = 0; i < paramsLt.getColumnCount(); i++) {
						if (item.getBounds(i).contains(pt)) {
							column = i;
							break;
						}				
					}
					if (column == -1) return;
					JNIParameter param = (JNIParameter)item.getData();
					if (column == PARAM_CAST_COLUMN) {
						paramTextEditor.setColumn(column);
						paramTextEditor.setItem(item);
						paramEditorTx.setText(param.getCast());
						paramEditorTx.selectAll();
						paramEditorTx.setVisible(true);
						paramEditorTx.setFocus();
					} else if (column == PARAM_FLAGS_COLUMN) {
						paramListEditor.setColumn(column);
						paramListEditor.setItem(item);
						paramEditorLt.setSelection(param.getFlags());
						floater.setLocation(paramsLt.toDisplay(e.x, e.y));
						floater.setVisible(true);
						paramEditorLt.setFocus();
					}
				}
			});
		}
	});
}

Button createActionButton(Composite parent, String text, Listener listener) {
	Button action = new Button(parent, SWT.PUSH);
	action.setText(text);
	GridData data = new GridData(GridData.FILL_HORIZONTAL);
	action.setLayoutData(data);
	action.addListener(SWT.Selection, listener);
	return action;
}

void createActionButtons(Composite parent) {		
	actionsPanel = new Composite(parent, SWT.NONE);

	GridData data = new GridData(GridData.FILL_VERTICAL);
	actionsPanel.setLayoutData(data);
		
	GridLayout actionsLayout = new GridLayout();
	actionsLayout.numColumns = 1;
	actionsPanel.setLayout(actionsLayout);
	
	createActionButton(actionsPanel, "Generate &All", new Listener() {
		public void handleEvent(Event e) {
			generateAll();
		}
	});
	
	Label separator = new Label(actionsPanel, SWT.SEPARATOR | SWT.HORIZONTAL);
	data = new GridData(GridData.FILL_HORIZONTAL);
	separator.setLayoutData(data);
	separator = new Label(actionsPanel, SWT.SEPARATOR | SWT.HORIZONTAL);
	data = new GridData(GridData.FILL_HORIZONTAL);
	separator.setLayoutData(data);
	
	createActionButton(actionsPanel, "Generate Structs &Header", new Listener() {
		public void handleEvent(Event e) {
			generateStructsHeader();
		}
	});
	createActionButton(actionsPanel, "Generate &Structs", new Listener() {
		public void handleEvent(Event e) {
			generateStructs();
		}
	});
	createActionButton(actionsPanel, "Generate &Natives", new Listener() {
		public void handleEvent(Event e) {
			generateNatives();
		}
	});
	createActionButton(actionsPanel, "Generate Meta &Data", new Listener() {
		public void handleEvent(Event e) {
			generateMetaData();
		}
	});
	createActionButton(actionsPanel, "Generate Cons&tants", new Listener() {
		public void handleEvent(Event e) {
			generateConstants();
		}
	});	
	createActionButton(actionsPanel, "Generate Si&zeof", new Listener() {
		public void handleEvent(Event e) {
			generateSizeof();
		}
	});

	Composite filler = new Composite(actionsPanel, SWT.NONE);
	filler.setLayoutData(new GridData(GridData.FILL_BOTH));
	
	progressLabel = new Label(actionsPanel, SWT.NONE);
	data = new GridData(GridData.FILL_HORIZONTAL);
	progressLabel.setLayoutData(data);
	progressLabel.setVisible(false);
	
	progressBar = new ProgressBar(actionsPanel, SWT.NONE);
	data = new GridData(GridData.FILL_HORIZONTAL);
	progressBar.setLayoutData(data);
	progressBar.setVisible(false);
}

public void run() {
	shell.open();
	MessageBox box = new MessageBox(shell, SWT.YES | SWT.NO);
	box.setText("Warning");
	box.setMessage("This tool is obsolete as of Eclipse 3.5 M2.\nThe meta data has been embedded in java source files.\nThere is a new plugin tool that replaces this tool.\nSee http://www.eclipse.org/swt/jnigen.php.\n\n Continue?");
	int result = box.open();
	if (result == SWT.NO) {
		shell.dispose();
	}
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep ();
	}
	cleanup();
}

String getPackageString(String className) {
	int dot = app.getMainClassName().lastIndexOf('.');
	if (dot == -1) return "";
	return app.getMainClassName().substring(0, dot);
}

String getClassString(JNIType type) {
	String name = type.getTypeSignature3(false);
	int index = name.lastIndexOf('.');
	if (index == -1) return name;
	return name.substring(index + 1, name.length());
}

String getFlagsString(String[] flags) {
	if (flags.length == 0) return "";
	StringBuffer buffer = new StringBuffer();
	for (int j = 0; j < flags.length; j++) {
		String flag = flags[j];
		if (buffer.length() != 0) buffer.append(", ");
		buffer.append(flag);
	}
	return buffer.toString();
}

String getMethodString(JNIMethod method) {
	String pkgName = getPackageString(method.getDeclaringClass().getName());
	StringBuffer buffer = new StringBuffer();
	buffer.append(method.getName());
	buffer.append("(");
	JNIParameter[] params = method.getParameters();
	for (int i = 0; i < params.length; i++) {
		JNIParameter param = params[i];
		if (i != 0) buffer.append(",");
		String string = param.getType().getTypeSignature3(false);
		if (string.startsWith(pkgName)) string = string.substring(pkgName.length() + 1);
		buffer.append(string);
	}
	buffer.append(")");
	return buffer.toString();
}

String getFieldString(JNIField field) {
	return field.getName();
}

void updateClasses() {
	classesLt.removeAll();
	JNIClass[] classes = app.getClasses();
	int mainIndex = 0;
	for (int i = 0; i < classes.length; i++) {
		JNIClass clazz = classes[i];
		if (clazz.equals(app.getMainClass())) mainIndex = i;
		TableItem item = new TableItem(classesLt, SWT.NONE);
		item.setData(clazz);
		item.setText(CLASS_NAME_COLUMN, clazz.getSimpleName());
		item.setText(CLASS_FLAGS_COLUMN, getFlagsString(clazz.getFlags()));
		item.setChecked(clazz.getGenerate());
	}
	TableColumn[] columns = classesLt.getColumns();
	for (int i = 0; i < columns.length; i++) {
		TableColumn column = columns[i];
		column.pack();
	}
	classesLt.setSelection(mainIndex);
}

void updateMembers() {
	membersLt.removeAll();
	membersLt.setHeaderVisible(false);
	TableColumn[] columns = membersLt.getColumns();
	for (int i = 0; i < columns.length; i++) {
		TableColumn column = columns[i];
		column.dispose();
	}
	int[] indices = classesLt.getSelectionIndices();
	if (indices.length != 1) return;
	TableItem classItem = classesLt.getItem(indices[0]);
	JNIClass clazz = (JNIClass)classItem.getData();
	boolean hasNatives = false;
	JNIMethod[] methods = clazz.getDeclaredMethods();
	for (int i = 0; i < methods.length; i++) {
		JNIMethod method = methods[i];
		int mods = method.getModifiers();
		if (hasNatives =((mods & Modifier.NATIVE) != 0)) break;
	}
	membersLt.setRedraw(false);
	if (hasNatives) {
		TableColumn column;
		column = new TableColumn(membersLt, SWT.NONE, METHOD_NAME_COLUMN);
		column.setText("Method");
		column = new TableColumn(membersLt, SWT.NONE, METHOD_FLAGS_COLUMN);
		column.setText("Flags");
		column = new TableColumn(membersLt, SWT.NONE, METHOD_ACCESSOR_COLUMN);
		column.setText("Accessor");
		/*
		column = new TableColumn(membersLt, SWT.NONE, METHOD_EXCLUDE_COLUMN);
		column.setText("Exclude");
		*/
		JNIGenerator.sort(methods);
		for (int i = 0; i < methods.length; i++) {
			JNIMethod method = methods[i];
			if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
			TableItem item = new TableItem(membersLt, SWT.NONE);
			item.setData(method);
			item.setText(METHOD_NAME_COLUMN, getMethodString(method));
			item.setChecked(method.getGenerate());
			item.setText(METHOD_FLAGS_COLUMN, getFlagsString(method.getFlags()));
			item.setText(METHOD_ACCESSOR_COLUMN, method.getAccessor());
			/*
			item.setText(METHOD_EXCLUDE_COLUMN, methodData.getExclude());
			*/
		}
	} else {
		TableColumn column;
		column = new TableColumn(membersLt, SWT.NONE, FIELD_NAME_COLUMN);
		column.setText("Field");
		column = new TableColumn(membersLt, SWT.NONE, FIELD_FLAGS_COLUMN);
		column.setText("Flags");
		column = new TableColumn(membersLt, SWT.NONE, FIELD_CAST_COLUMN);
		column.setText("Cast");
		column = new TableColumn(membersLt, SWT.NONE, FIELD_ACCESSOR_COLUMN);
		column.setText("Accessor");
		/*
		column = new TableColumn(membersLt, SWT.NONE, FIELD_EXCLUDE_COLUMN);
		column.setText("Exclude");
		*/
		JNIField[] fields = clazz.getDeclaredFields();	
		for (int i = 0; i < fields.length; i++) {
			JNIField field = fields[i];
			int mods = field.getModifiers(); 
			if (((mods & Modifier.PUBLIC) == 0) ||
				((mods & Modifier.FINAL) != 0) ||
				((mods & Modifier.STATIC) != 0)) continue;
			TableItem item = new TableItem(membersLt, SWT.NONE);
			item.setData(field);
			item.setText(FIELD_NAME_COLUMN, getFieldString(field));
			item.setChecked(field.getGenerate());
			item.setText(FIELD_CAST_COLUMN, field.getCast());
			item.setText(FIELD_FLAGS_COLUMN, getFlagsString(field.getFlags()));
			item.setText(FIELD_ACCESSOR_COLUMN, field.getAccessor());
			/*
			item.setText(FIELD_EXCLUDE_COLUMN, fieldData.getExclude());
			*/
		}
	}
	columns = membersLt.getColumns();
	for (int i = 0; i < columns.length; i++) {
		TableColumn column = columns[i];
		column.pack();
	}
	membersLt.setHeaderVisible(true);
	membersLt.setRedraw(true);
}

void updateParameters() {
	paramsLt.removeAll();
	int[] indices = membersLt.getSelectionIndices();
	if (indices.length != 1) {
		paramsLt.setHeaderVisible(false);
		return;
	}
	TableItem memberItem = membersLt.getItem(indices[0]);
	Object data = memberItem.getData();
	if (!(data instanceof JNIMethod)) return;
	paramsLt.setRedraw(false);
	JNIMethod method = (JNIMethod)data;
	JNIParameter[] params = method.getParameters();
	for (int i = 0; i < params.length; i++) {
		JNIParameter param = params[i];
		TableItem item = new TableItem(paramsLt, SWT.NONE);
		item.setData(param);
		item.setText(PARAM_INDEX_COLUMN, String.valueOf(i));
		item.setText(PARAM_TYPE_COLUMN, getClassString(param.getType()));
		item.setText(PARAM_CAST_COLUMN, param.getCast());
		item.setText(PARAM_FLAGS_COLUMN, getFlagsString(param.getFlags()));
	}
	TableColumn[] columns = paramsLt.getColumns();
	for (int i = 0; i < columns.length; i++) {
		TableColumn column = columns[i];
		column.pack();
	}
	paramsLt.setRedraw(true);
	paramsLt.setHeaderVisible(true);
}

void updateGenerate(TableItem item) {
	JNIItem itemData = (JNIItem)item.getData();
	itemData.setGenerate(item.getChecked());
//	MetaData metaData = app.getMetaData();
//	if (itemData instanceof JNIClass) {
//		ClassData data = (ClassData)itemData;
//		metaData.setMetaData(data.getClazz(), data);
//	} else if (itemData instanceof FieldData) {
//		FieldData data = (FieldData)itemData;
//		item.setText(FIELD_FLAGS_COLUMN, getFlagsString(data.getFlags()));
//		metaData.setMetaData(data.getField(), data);
//	} else if (itemData instanceof MethodData) {
//		MethodData data = (MethodData)itemData;
//		item.setText(METHOD_FLAGS_COLUMN, getFlagsString(data.getFlags()));
//		metaData.setMetaData(data.getMethod(), data);
//	} else if (itemData instanceof ParameterData) {
//		ParameterData data = (ParameterData)itemData;
//		item.setText(PARAM_FLAGS_COLUMN, getFlagsString(data.getFlags()));
//		metaData.setMetaData(data.getMethod(), data.getParameter(), data);
//	}
}

boolean updateOutputDir() {
	String outputDirStr = outputDirCb.getText();
	File file = new File(outputDirStr);
	if (!file.exists()) {
		MessageBox dialog = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
		dialog.setMessage("Output directory does not exist.");
		dialog.open();
		return false;
	}
	if (!file.isDirectory()) {
		MessageBox dialog = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
		dialog.setMessage("Output directory is not a directory.");
		dialog.open();
		return false;
	}
	if (outputDirStr.length() > 0) {
		if (!outputDirStr.equals(app.getOutputDir())) {
			app.setOutputDir(outputDirStr);
		}
		if (outputDirCb.indexOf(outputDirStr) == -1) {
			outputDirCb.add(outputDirStr);
		}
	}
	return true;
}

void updateMainClass() {
	String mainClassStr = mainClassCb.getText();
	if (mainClassStr.length() > 0) {
		if (!mainClassStr.equals(app.getMainClassName())) {
			app.setMainClassName(mainClassStr);
		}
		if (mainClassCb.indexOf(mainClassStr) == -1) {
			mainClassCb.add(mainClassStr);
		}
		if (app.getOutputDir() != null) {
			int index = outputDirCb.indexOf(app.getOutputDir());
			if (index != -1) outputDirCb.select(index);
		}
	}
}

public static void main(String[] args) {
	JNIGeneratorApp gen = new JNIGeneratorApp ();
	if (args.length > 0) {
		gen.setMainClassName(args[0]);
		if (args.length > 1) gen.setOutputDir(args[1]);
	} else {
		gen.setMainClassName(JNIGeneratorApp.getDefaultMainClass());
	}
	JNIGeneratorAppUI ui = new JNIGeneratorAppUI(gen);
	ui.open();
	ui.run();
}

}
