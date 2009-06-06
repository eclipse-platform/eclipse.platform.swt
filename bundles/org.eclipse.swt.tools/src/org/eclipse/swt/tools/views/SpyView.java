/*******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.views;

import java.io.*;
import java.lang.reflect.*;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.tools.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.part.*;

public class SpyView extends ViewPart {
	private StyledText output;
	private Action spyAction;
	private Listener keyFilter;
	private Runnable timer;
	private Control lastControl;
	private Field field;
	
	static final int TIMEOUT = 100;

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		output = new StyledText(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY);
		
		keyFilter = new Listener() {
			public void handleEvent(Event e) {
				// If this accelerator changes, change the tooltip text
				if (e.keyCode == '.' && e.stateMask == (SWT.ALT | SWT.SHIFT | SWT.CONTROL)) {
					if (spyAction.isChecked()) {
						spyAction.setChecked(false);
					} else {
						spyAction.setChecked(true);
						spyAction.run();
					}
					e.type = SWT.None;
				};
			}
		};
		parent.getDisplay().addFilter(SWT.KeyDown, keyFilter);
		
		timer = new Runnable() {
			public void run() {
				if (output == null || output.isDisposed() || !spyAction.isChecked()) return;
				Display display = output.getDisplay();
				Control control = display.getCursorControl();
				if (control != lastControl) {
					StringBuffer text = new StringBuffer();
					if (control != null) {
						text.append(control+"@"+getOSHandle(control)+"\n");
						text.append("\tStyle: "+getStyle(control)+"\n");
						text.append("\tLayout Data: "+control.getLayoutData()+"\n");
						text.append("\tBounds: "+control.getBounds()+"\n");
						text.append("\n");
						if (control instanceof Composite) {
							text.append("\nChildren:\n");
							Control[] children = ((Composite)control).getChildren();
							for (int i = 0; i < children.length; i++) {
								text.append("\t"+children[i]+"\n");
							}
						}
						Composite parent = control.getParent();
						if (parent != null) {
							text.append("\nPeers:\n");
							Control[] peers = parent.getChildren();
							for (int i = 0; i < peers.length; i++) {
								text.append("\t");
								if (peers[i] == control) text.append("*");
								text.append(peers[i]+"@"+getOSHandle(peers[i]));
								text.append(" Layout Data: "+peers[i].getLayoutData());
								text.append(" Bounds: "+peers[i].getBounds());
								text.append("\n");
							}
							text.append("\nParent Tree:\n");
							Composite[] parents = new Composite[0];
							while (parent != null) {
								Composite[] newParents = new Composite[parents.length + 1];
								System.arraycopy(parents, 0, newParents, 0, parents.length);
								newParents[parents.length] = parent;
								parents = newParents;
								parent = parent.getParent();
							}
							for (int i = parents.length - 1; i >= 0; i--) {
								String prefix = "\t";
								for (int j = 0; j < parents.length - i - 1; j++) {
									prefix += "\t";
								}
								text.append(prefix + parents[i]+"@"+getOSHandle(parents[i])+"\n");
								text.append(prefix+"\t Style: "+getStyle(parents[i])+"\n");
								text.append(prefix+"\t Bounds: "+parents[i].getBounds()+"\n");
								text.append(prefix+"\t Layout: "+parents[i].getLayout()+"\n");
								text.append(prefix+"\t LayoutData: "+parents[i].getLayoutData()+"\n");
							}
						}
						Error error = (Error)control.getData("StackTrace");
						if (error != null) {
							text.append("\nCreation Stack Trace:\n");
							ByteArrayOutputStream stream = new ByteArrayOutputStream();
							PrintStream s = new PrintStream(stream);
							error.printStackTrace(s);
							text.append(stream.toString());
						}
					}
					output.setText(text.toString());
				}
				lastControl = control;
				display.timerExec(100, this);			
			}
		};
		
		makeActions();
		contributeToActionBars();
	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		if (output != null & !output.isDisposed()) output.setFocus();
	}
	
	private String getOSHandle(Control control) {
		if (field == null) {
			String[] fieldNames = {"handle", "view"};
			for (int i = 0; i < fieldNames.length; i++) {
				try {
					field = control.getClass().getField(fieldNames[i]);
					if (field != null) break;
				} catch (Throwable e) {}
			}
		}
		try {
			return field.get(control).toString();
		} catch (Throwable e) {}
		return "";
	}
	
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}
	
	public void dispose() {
		Display.getCurrent().removeFilter(SWT.KeyDown, keyFilter);
		super.dispose();
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(spyAction);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(spyAction);
	}
	
	private void makeActions() {
		spyAction = new Action() {
			public void run() {
				Display.getCurrent().timerExec(TIMEOUT, timer);
			}
		};
		spyAction.setText("Spy");
		spyAction.setToolTipText("Toggle Spy (CONTROL+ALT+SHIFT+.)");
		spyAction.setImageDescriptor(Activator.getImageDescriptor("icons/spy.gif"));
		spyAction.setChecked(false);
	}
	
	private String getStyle(Widget w) {
		//MODELESS = 0;
		//BAR = 1 << 1;
		//SEPARATOR = 1 << 1;
		//TOGGLE = 1 << 1;
		//MULTI = 1 << 1;
		//INDETERMINATE = 1 << 1;
		//DBCS = 1 << 1;
		//ALPHA = 1 << 2;
		//TOOL = 1 << 2; 
		//SINGLE = 1 << 2;
		//ARROW = 1 << 2;
		//DROP_DOWN = 1 << 2;
		//SHADOW_IN = 1 << 2;
		//POP_UP = 1 << 3;
		//PUSH = 1 << 3;
		//READ_ONLY = 1 << 3;
		//SHADOW_OUT = 1 << 3;
		//NO_TRIM = 1 << 3;
		//NATIVE = 1 << 3;
		//RESIZE = 1 << 4;
		//SHADOW_ETCHED_IN = 1 << 4;
		//RADIO = 1 << 4;
		//PHONETIC = 1 << 4;
		//ROMAN = 1 << 5;
		//CHECK = 1 << 5;
		//SHADOW_NONE = 1 << 5;
		//TITLE = 1 << 5;
		//DATE = 1 << 5;
		//CLOSE = 1 << 6;
		//MENU = CLOSE;
		//CASCADE = 1 << 6;
		//WRAP = 1 << 6;
		//SIMPLE = 1 << 6;
		//SHADOW_ETCHED_OUT = 1 << 6;
		//MIN = 1 << 7;
		//UP = 1 << 7;
		//TOP = UP;
		//TIME = 1 << 7;
		//HORIZONTAL = 1 << 8;
		//H_SCROLL = 1 << 8;
		//V_SCROLL = 1 << 9;
		//VERTICAL = 1 << 9;
		//MAX = 1 << 10;
		//DOWN               = 1 << 10;
		//BOTTOM             = DOWN;
		//CALENDAR = 1 << 10;
		//BORDER = 1 << 11;
		//CLIP_CHILDREN = 1 << 12; 
		//BALLOON = 1 << 12;
		//CLIP_SIBLINGS = 1 << 13;
		//ON_TOP = 1 << 14;
		//LEAD               = 1 << 14;
		//LEFT               = LEAD;
		//PRIMARY_MODAL = 1 << 15;
		//HIDE_SELECTION = 1 << 15;
		//SHORT = 1 << 15;
		//MEDIUM = 1 << 16;
		//FULL_SELECTION = 1 << 16;
		//SMOOTH = 1 << 16;
		//APPLICATION_MODAL = 1 << 16;
		//SYSTEM_MODAL = 1 << 17;
		//TRAIL              = 1 << 17;	
		//RIGHT              = TRAIL;
		//NO_BACKGROUND = 1 << 18;
		//NO_FOCUS = 1 << 19;
		//NO_REDRAW_RESIZE = 1 << 20;
		//NO_MERGE_PAINTS = 1 << 21;
		//NO_RADIO_GROUP = 1 << 22;
		//PASSWORD = 1 << 22;
		//FLAT = 1 << 23;
		//EMBEDDED = 1 << 24;
		//CENTER = 1 << 24;
		//LEFT_TO_RIGHT = 1 << 25;
		//RIGHT_TO_LEFT = 1 << 26;
		//MIRRORED = 1 << 27;
		//VIRTUAL = 1 << 28;
		//LONG = 1 << 28;
		//DOUBLE_BUFFERED = 1 << 29;
		
		int style = w.getStyle();
		String result = "";
		if (style == SWT.DEFAULT) {
			return "DEFAULT - bad!";
		}
		if ((style & 1 << 1) != 0) {
			if (w instanceof CTabFolder || w instanceof StyledText || w instanceof List || w instanceof Text || w instanceof Table || w instanceof Tree) {
				result += "MULTI | ";
			} else if (w instanceof Menu) {
				result += "BAR | ";
			} else if (w instanceof Label || w instanceof MenuItem || w instanceof ToolItem) {
				result += "SEPARATOR | ";
			} else if (w instanceof Button) {
				result += "TOGGLE | ";
			} else if (w instanceof ProgressBar) {
				result += "INDETERMINATE | ";
			} else {
				result += "BAR or SEPARATOR or TOGGLE or MULTI or INDETERMINATE or DBCS | ";	
			}
		}
		if ((style & 1 << 2) != 0) {
			if (w instanceof Menu || w instanceof ToolItem || w instanceof CoolItem || w instanceof Combo) {
				result += "DROP_DOWN | ";
			} else if (w instanceof Button) {
				result += "ARROW | ";
			} else if (w instanceof CTabFolder || w instanceof StyledText || w instanceof List || w instanceof Text || w instanceof Table || w instanceof Tree) {
				result += "SINGLE | ";
			} else if (w instanceof Label || w instanceof Group) {
				result += "SHADOW_IN | ";
			} else if (w instanceof Decorations) {
				result += "TOOL | ";
			} else {
				result += "ALPHA or TOOL or SINGLE or ARROW or DROP_DOWN or SHADOW_IN | ";
			}
		}
		if ((style & 1 << 3) != 0) {
			if (w instanceof Menu) {
				result += "POP_UP | ";
			} else if (w instanceof Button || w instanceof MenuItem || w instanceof ToolItem) {
				result += "PUSH | ";
			} else if (w instanceof Combo || w instanceof Text || w instanceof StyledText) {
				result += "READ_ONLY | ";
			} else if (w instanceof Label || w instanceof Group || w instanceof ToolBar) {
				result += "SHADOW_OUT | ";
			} else if (w instanceof Decorations) {
				result += "NO_TRIM | ";	
			} else {
				result += "POP_UP or PUSH or READ_ONLY or SHADOW_OUT or NO_TRIM or NATIVE | ";
			}
		}
		if ((style & 1 << 4) != 0) {
			if (w instanceof Button || w instanceof MenuItem || w instanceof ToolItem) {
				result += "RADIO | ";
			} else if (w instanceof Group) {
				result += "SHADOW_ETCHED_IN | ";
			} else if (w instanceof Decorations || w instanceof Tracker) {
				result += "RESIZE | ";
			} else {
				result += "RESIZE or SHADOW_ETCHED_IN or RADIO or PHONETIC | ";
			}
		}
		if ((style & 1 << 5) != 0) {
			if (w instanceof Button || w instanceof MenuItem || w instanceof ToolItem || w instanceof Table || w instanceof Tree) {
				result += "CHECK | ";
			} else if (w instanceof Label || w instanceof Group) {
				result += "SHADOW_NONE | ";
			} else if (w instanceof Decorations) {
				result += "TITLE | ";
			} else if (w instanceof DateTime) {
				result += "DATE | ";
			} else {
				result += "ROMAN or CHECK  or SHADOW_NONE or TITLE | ";
			}
		}
		if ((style & 1 << 6) != 0) {
			if (w instanceof MenuItem) {
				result += "CASCADE | ";
			} else if (w instanceof StyledText || w instanceof Label || w instanceof Text || w instanceof ToolBar) {
				result += "WRAP | ";
			} else if (w instanceof Combo) {
				result += "SIMPLE | ";
			} else if (w instanceof Group) {
				result += "SHADOW_ETCHED_OUT | ";
			} else if (w instanceof Decorations || w instanceof CTabFolder || w instanceof CTabItem) {
				result += "CLOSE | ";
			} else {
				result += "CLOSE or MENU or CASCADE or WRAP or SIMPLE or SHADOW_ETCHED_OUT | ";
			}
		}
		if ((style & 1 << 7) != 0) {
			if (w instanceof Decorations) {
				result += "MIN | ";
			} else if (w instanceof Button || w instanceof Tracker) {
				result += "UP | ";
			} else if (w instanceof CTabFolder) {
				result += "TOP | ";
			} else if (w instanceof DateTime) {
				result += "TIME | ";
			} else {
				result += "MIN or UP or TOP | ";
			}
		}
		if ((style & 1 << 8) != 0) {
			result += "HORIZONTAL | ";
		}
		if ((style & 1 << 9) != 0) {
			result += "VERTICAL | ";
		}
		if ((style & 1 << 10) != 0) {
			if (w instanceof Decorations) {
				result += "MAX | ";
			} else if (w instanceof Button || w instanceof Tracker) {
				result += "DOWN | ";
			} else if (w instanceof CTabFolder) {
				result += "BOTTOM | ";
			} else if (w instanceof DateTime) {
				result += "CALENDAR | ";
			} else {
				result += "MAX or DOWN or BOTTOM | ";
			}
		}
		if ((style & 1 << 11) != 0) {
			result += "BORDER | ";
		}
		if ((style & 1 << 12) != 0) {
			if (w instanceof ToolTip) {
				result += "BALLOON | ";
			} else {
				result += "CLIP_CHILDREN | ";
			}
		}
		if ((style & 1 << 13) != 0) {
			result += "CLIP_SIBLINGS | ";
		}
		if ((style & 1 << 14) != 0) {
			result += "ON_TOP or LEAD or LEFT | ";
		}
		if ((style & 1 << 15) != 0) {
			if (w instanceof Shell) {
				result += "PRIMARY_MODAL | ";
			} else if (w instanceof Table || w instanceof Tree) {
				result += "HIDE_SELECTION | ";
			} else if (w instanceof DateTime) {
				result += "SHORT | ";
			} else {
				result += "PRIMARY_MODAL or HIDE_SELECTION | ";
			}
		}
		if ((style & 1 << 16) != 0) {
			if (w instanceof StyledText || w instanceof Table || w instanceof Tree) {
				result += "FULL_SELECTION | ";
			} else if (w instanceof Shell) {
				result += "APPLICATION_MODAL | ";
			} else if (w instanceof ProgressBar) {
				result += "SMOOTH | ";
			} else if (w instanceof DateTime) {
				result += "MEDIUM | ";
			} else {
				result += "FULL_SELECTION or SMOOTH or APPLICATION_MODAL | ";
			}
		}
		if ((style & 1 << 17) != 0) {
			if (w instanceof Shell) {
				result += "SYSTEM_MODAL | ";
			} else if (w instanceof Button || w instanceof Label || w instanceof TableColumn || w instanceof Tracker || w instanceof ToolBar) {
				result += "TRAIL | ";
			} else {
				result += "SYSTEM_MODAL or TRAIL or RIGHT | ";
			}
		}
		if ((style & 1 << 18) != 0) {
			result += "NO_BACKGROUND | ";
		}
		if ((style & 1 << 19) != 0) {
			result += "NO_FOCUS | ";
		}
		if ((style & 1 << 20) != 0) {
			result += "NO_REDRAW_RESIZE | ";
		}
		if ((style & 1 << 21) != 0) {
			result += "NO_MERGE_PAINTS | ";
		}
		if ((style & 1 << 22) != 0) {
			if (w instanceof Text) {
				result += "PASSWORD | ";
			} else if (w instanceof Composite) {
				result += "NO_RADIO_GROUP | ";
			} else {
				result += "NO_RADIO_GROUP or PASSWORD | ";
			}
		}
		if ((style & 1 << 23) != 0) {
			result += "FLAT | ";
		}
		if ((style & 1 << 24) != 0) {
			if (w instanceof Button || w instanceof Label || w instanceof TableColumn) {
				result += "CENTER | ";
			} else {
				result += "EMBEDDED or CENTER | ";
			}
		}
		if ((style & 1 << 25) != 0) {
			result += "LEFT_TO_RIGHT | ";
		}
		if ((style & 1 << 26) != 0) {
			result += "RIGHT_TO_LEFT | ";
		}
		if ((style & 1 << 27) != 0) {
			result += "MIRRORED | ";
		}
		if ((style & 1 << 28) != 0) {
			if (w instanceof DateTime) {
				result += "LONG | ";
			} else {
				result += "VIRTUAL | ";
			}
		}
		if ((style & 1 << 29) != 0) {
			result += "DOUBLE_BUFFERED | ";
		}
		int lastOr = result.lastIndexOf("|");
		if (lastOr == result.length() - 2 ) result = result.substring(0, result.length() - 2);
		return result;
	}
}
