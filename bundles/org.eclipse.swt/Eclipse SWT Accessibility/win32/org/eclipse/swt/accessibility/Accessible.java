/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.swt.accessibility;

import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.ole.win32.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class provide a bridge between application
 * code and assistive technology clients. Many platforms provide
 * default accessible behavior for most widgets, and this class
 * allows that default behavior to be overridden. Applications
 * can get the default Accessible object for a control by sending
 * it <code>getAccessible</code>, and then add an accessible listener
 * to override simple items like the name and help string, or they
 * can add an accessible control listener to override complex items.
 * As a rule of thumb, an application would only want to use the
 * accessible control listener to implement accessibility for a
 * custom control.
 *
 * @see Control#getAccessible
 * @see AccessibleListener
 * @see AccessibleEvent
 * @see AccessibleControlListener
 * @see AccessibleControlEvent
 * @see <a href="http://www.eclipse.org/swt/snippets/#accessibility">Accessibility snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 2.0
 */
public class Accessible {
	static final int MAX_RELATION_TYPES = 15;
	static final int TABLE_MODEL_CHANGE_SIZE = 5;
	static final int TEXT_CHANGE_SIZE = 4;
	static final int SCROLL_RATE = 100;
	static final boolean DEBUG = false;
	static final String PROPERTY_USEIA2 = "org.eclipse.swt.accessibility.UseIA2"; //$NON-NLS-1$
	static boolean UseIA2 = true;
	static int UniqueID = -0x10;
	int refCount = 0, enumIndex = 0;
	Runnable timer;
	COMObject objIAccessible, objIEnumVARIANT, objIServiceProvider,
		objIAccessibleApplication, /*objIAccessibleComponent,*/ objIAccessibleEditableText, objIAccessibleHyperlink,
		objIAccessibleHypertext, /*objIAccessibleImage,*/ objIAccessibleTable2, objIAccessibleTableCell,
		objIAccessibleValue; /* objIAccessibleRelation is defined in Relation class */
	IAccessible iaccessible;
	List<AccessibleListener> accessibleListeners;
	List<AccessibleControlListener> accessibleControlListeners;
	List<AccessibleTextListener> accessibleTextListeners;
	List<AccessibleActionListener> accessibleActionListeners;
	List<AccessibleEditableTextListener> accessibleEditableTextListeners;
	List<AccessibleHyperlinkListener> accessibleHyperlinkListeners;
	List<AccessibleTableListener> accessibleTableListeners;
	List<AccessibleTableCellListener> accessibleTableCellListeners;
	List<AccessibleTextExtendedListener> accessibleTextExtendedListeners;
	List<AccessibleValueListener> accessibleValueListeners;
	List<AccessibleAttributeListener> accessibleAttributeListeners;
	Relation relations[] = new Relation[MAX_RELATION_TYPES];
	Object[] variants;
	Accessible parent;
	List<Accessible> children = new ArrayList<>();
	Control control;
	int uniqueID = -1;
	int [] tableChange; // type, rowStart, rowCount, columnStart, columnCount
	Object [] textDeleted; // type, start, end, text
	Object [] textInserted; // type, start, end, text
	ToolItem item;

	static {
		String property = System.getProperty (PROPERTY_USEIA2);
		if (property != null && property.equalsIgnoreCase ("false")) { //$NON-NLS-1$
			UseIA2 = false;
		}
	}

	/**
	 * Constructs a new instance of this class given its parent.
	 *
	 * @param parent the Accessible parent, which must not be null
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 * </ul>
	 *
	 * @see #dispose
	 * @see Control#getAccessible
	 *
	 * @since 3.6
	 */
	public Accessible(Accessible parent) {
		this.parent = checkNull(parent);
		this.control = parent.control;
		parent.children.add(this);
		AddRef();
	}

	/**
	 * @since 3.5
	 * @deprecated
	 */
	@Deprecated
	protected Accessible() {
	}

	Accessible(Control control) {
		this.control = control;
		long[] ppvObject = new long[1];
		/* CreateStdAccessibleObject([in] hwnd, [in] idObject, [in] riidInterface, [out] ppvObject).
		 * AddRef has already been called on ppvObject by the callee and must be released by the caller.
		 */
		int result = (int)COM.CreateStdAccessibleObject(control.handle, OS.OBJID_CLIENT, COM.IIDIAccessible, ppvObject);
		/* The object needs to be checked, because if the CreateStdAccessibleObject()
		 * symbol is not found, the return value is S_OK.
		 */
		if (ppvObject[0] == 0) return;
		if (result != COM.S_OK) OLE.error(OLE.ERROR_CANNOT_CREATE_OBJECT, result);
		iaccessible = new IAccessible(ppvObject[0]);
		createIAccessible();
		AddRef();
	}

	Accessible(Accessible parent, long iaccessible_address) {
		this(parent);
		iaccessible = new IAccessible(iaccessible_address);
	}

	static Accessible checkNull (Accessible parent) {
		if (parent == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		return parent;
	}

	void createIAccessible() {
		objIAccessible = new COMObject(new int[] {2,0,0,/*IA>>*/1,3,5,8,1,1,2,2,2,2,2,2,2,3,2,1,1,2,2,5,3,3,1,2,2,/*<<IA*/1,2,3,1,1,3,3,1,1,1,1,3,3,1,1,1,1,1}) {
			@Override
			public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
			@Override
			public long method1(long[] args) {return AddRef();}
			@Override
			public long method2(long[] args) {return Release();}
			// method3 GetTypeInfoCount - not implemented
			// method4 GetTypeInfo - not implemented
			// method5 GetIDsOfNames - not implemented
			// method6 Invoke - not implemented
			@Override
			public long method7(long[] args) {return get_accParent(args[0]);}
			@Override
			public long method8(long[] args) {return get_accChildCount(args[0]);}
			@Override
			public long method9(long[] args) {return get_accChild(args[0], args[1]);}
			@Override
			public long method10(long[] args) {return get_accName(args[0], args[1]);}
			@Override
			public long method11(long[] args) {return get_accValue(args[0], args[1]);}
			@Override
			public long method12(long[] args) {return get_accDescription(args[0], args[1]);}
			@Override
			public long method13(long[] args) {return get_accRole(args[0], args[1]);}
			@Override
			public long method14(long[] args) {return get_accState(args[0], args[1]);}
			@Override
			public long method15(long[] args) {return get_accHelp(args[0], args[1]);}
			@Override
			public long method16(long[] args) {return get_accHelpTopic(args[0], args[1], args[2]);}
			@Override
			public long method17(long[] args) {return get_accKeyboardShortcut(args[0], args[1]);}
			@Override
			public long method18(long[] args) {return get_accFocus(args[0]);}
			@Override
			public long method19(long[] args) {return get_accSelection(args[0]);}
			@Override
			public long method20(long[] args) {return get_accDefaultAction(args[0], args[1]);}
			@Override
			public long method21(long[] args) {return accSelect((int)args[0], args[1]);}
			@Override
			public long method22(long[] args) {return accLocation(args[0], args[1], args[2], args[3], args[4]);}
			@Override
			public long method23(long[] args) {return accNavigate((int)args[0], args[1], args[2]);}
			@Override
			public long method24(long[] args) {return accHitTest((int)args[0], (int)args[1], args[2]);}
			@Override
			public long method25(long[] args) {return accDoDefaultAction(args[0]);}
			@Override
			public long method26(long[] args) {return put_accName(args[0], args[1]);}
			@Override
			public long method27(long[] args) {return put_accValue(args[0], args[1]);}

			// IAccessible2 methods
			@Override
			public long method28(long[] args) {return get_nRelations(args[0]);}
			@Override
			public long method29(long[] args) {return get_relation((int)args[0], args[1]);}
			@Override
			public long method30(long[] args) {return get_relations((int)args[0], args[1], args[2]);}
			@Override
			public long method31(long[] args) {return get_role(args[0]);}
			@Override
			public long method32(long[] args) {return scrollTo((int)args[0]);}
			@Override
			public long method33(long[] args) {return scrollToPoint((int)args[0], (int)args[1], (int)args[2]);}
			@Override
			public long method34(long[] args) {return get_groupPosition(args[0], args[1], args[2]);}
			@Override
			public long method35(long[] args) {return get_states(args[0]);}
			@Override
			public long method36(long[] args) {return get_extendedRole(args[0]);}
			@Override
			public long method37(long[] args) {return get_localizedExtendedRole(args[0]);}
			@Override
			public long method38(long[] args) {return get_nExtendedStates(args[0]);}
			@Override
			public long method39(long[] args) {return get_extendedStates((int)args[0], args[1], args[2]);}
			@Override
			public long method40(long[] args) {return get_localizedExtendedStates((int)args[0], args[1], args[2]);}
			@Override
			public long method41(long[] args) {return get_uniqueID(args[0]);}
			@Override
			public long method42(long[] args) {return get_windowHandle(args[0]);}
			@Override
			public long method43(long[] args) {return get_indexInParent(args[0]);}
			@Override
			public long method44(long[] args) {return get_locale(args[0]);}
			@Override
			public long method45(long[] args) {return get_attributes(args[0]);}
		};
	}

	void createIAccessibleApplication() {
		objIAccessibleApplication = new COMObject(new int[] {2,0,0,1,1,1,1}) {
			@Override
			public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
			@Override
			public long method1(long[] args) {return AddRef();}
			@Override
			public long method2(long[] args) {return Release();}
			@Override
			public long method3(long[] args) {return get_appName(args[0]);}
			@Override
			public long method4(long[] args) {return get_appVersion(args[0]);}
			@Override
			public long method5(long[] args) {return get_toolkitName(args[0]);}
			@Override
			public long method6(long[] args) {return get_toolkitVersion(args[0]);}
		};
	}

	// This method is intentionally commented. We are not providing IAccessibleComponent at this time.
//	void createIAccessibleComponent() {
//		objIAccessibleComponent = new COMObject(new int[] {2,0,0,2,1,1}) {
//			public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
//			public long method1(long[] args) {return AddRef();}
//			public long method2(long[] args) {return Release();}
//			public long method3(long[] args) {return get_locationInParent(args[0], args[1]);}
//			public long method4(long[] args) {return get_foreground(args[0]);}
//			public long method5(long[] args) {return get_background(args[0]);}
//		};
//	}

	void createIAccessibleEditableText() {
		objIAccessibleEditableText = new COMObject(new int[] {2,0,0,2,2,2,2,1,3,3}) {
			@Override
			public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
			@Override
			public long method1(long[] args) {return AddRef();}
			@Override
			public long method2(long[] args) {return Release();}
			@Override
			public long method3(long[] args) {return copyText((int)args[0], (int)args[1]);}
			@Override
			public long method4(long[] args) {return deleteText((int)args[0], (int)args[1]);}
			@Override
			public long method5(long[] args) {return insertText((int)args[0], args[1]);}
			@Override
			public long method6(long[] args) {return cutText((int)args[0], (int)args[1]);}
			@Override
			public long method7(long[] args) {return pasteText((int)args[0]);}
			@Override
			public long method8(long[] args) {return replaceText((int)args[0], (int)args[1], args[2]);}
			@Override
			public long method9(long[] args) {return setAttributes((int)args[0], (int)args[1], args[2]);}
		};
	}

	void createIAccessibleHyperlink() {
		objIAccessibleHyperlink = new COMObject(new int[] {2,0,0,/*IAA>>*/1,1,2,4,2,2,/*<<IAA*/2,2,1,1,1}) {
			@Override
			public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
			@Override
			public long method1(long[] args) {return AddRef();}
			@Override
			public long method2(long[] args) {return Release();}
			// IAccessibleAction
			@Override
			public long method3(long[] args) {return get_nActions(args[0]);}
			@Override
			public long method4(long[] args) {return doAction((int)args[0]);}
			@Override
			public long method5(long[] args) {return get_description((int)args[0], args[1]);}
			@Override
			public long method6(long[] args) {return get_keyBinding((int)args[0], (int)args[1], args[2], args[3]);}
			@Override
			public long method7(long[] args) {return get_name((int)args[0], args[1]);}
			@Override
			public long method8(long[] args) {return get_localizedName((int)args[0], args[1]);}
			// IAccessibleHyperlink
			@Override
			public long method9(long[] args) {return get_anchor((int)args[0], args[1]);}
			@Override
			public long method10(long[] args) {return get_anchorTarget((int)args[0], args[1]);}
			@Override
			public long method11(long[] args) {return get_startIndex(args[0]);}
			@Override
			public long method12(long[] args) {return get_endIndex(args[0]);}
			@Override
			public long method13(long[] args) {return get_valid(args[0]);}
		};
	}

	void createIAccessibleHypertext() {
		objIAccessibleHypertext = new COMObject(new int[] {2,0,0,/*IAT>>*/2,4,1,6,1,4,3,3,5,5,5,1,1,3,1,3,5,1,1,/*<<IAT*/1,2,2}) {
			@Override
			public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
			@Override
			public long method1(long[] args) {return AddRef();}
			@Override
			public long method2(long[] args) {return Release();}
			// IAccessibleText
			@Override
			public long method3(long[] args) {return addSelection((int)args[0], (int)args[1]);}
			@Override
			public long method4(long[] args) {return get_attributes((int)args[0], args[1], args[2], args[3]);}
			@Override
			public long method5(long[] args) {return get_caretOffset(args[0]);}
			@Override
			public long method6(long[] args) {return get_characterExtents((int)args[0], (int)args[1], args[2], args[3], args[4], args[5]);}
			@Override
			public long method7(long[] args) {return get_nSelections(args[0]);}
			@Override
			public long method8(long[] args) {return get_offsetAtPoint((int)args[0], (int)args[1], (int)args[2], args[3]);}
			@Override
			public long method9(long[] args) {return get_selection((int)args[0], args[1], args[2]);}
			@Override
			public long method10(long[] args) {return get_text((int)args[0], (int)args[1], args[2]);}
			@Override
			public long method11(long[] args) {return get_textBeforeOffset((int)args[0], (int)args[1], args[2], args[3], args[4]);}
			@Override
			public long method12(long[] args) {return get_textAfterOffset((int)args[0], (int)args[1], args[2], args[3], args[4]);}
			@Override
			public long method13(long[] args) {return get_textAtOffset((int)args[0], (int)args[1], args[2], args[3], args[4]);}
			@Override
			public long method14(long[] args) {return removeSelection((int)args[0]);}
			@Override
			public long method15(long[] args) {return setCaretOffset((int)args[0]);}
			@Override
			public long method16(long[] args) {return setSelection((int)args[0], (int)args[1], (int)args[2]);}
			@Override
			public long method17(long[] args) {return get_nCharacters(args[0]);}
			@Override
			public long method18(long[] args) {return scrollSubstringTo((int)args[0], (int)args[1], (int)args[2]);}
			@Override
			public long method19(long[] args) {return scrollSubstringToPoint((int)args[0], (int)args[1], (int)args[2], (int)args[3], (int)args[4]);}
			@Override
			public long method20(long[] args) {return get_newText(args[0]);}
			@Override
			public long method21(long[] args) {return get_oldText(args[0]);}
			// IAccessibleHypertext
			@Override
			public long method22(long[] args) {return get_nHyperlinks(args[0]);}
			@Override
			public long method23(long[] args) {return get_hyperlink((int)args[0], args[1]);}
			@Override
			public long method24(long[] args) {return get_hyperlinkIndex((int)args[0], args[1]);}
		};
	}

	// This method is intentionally commented. We are not providing IAccessibleImage at this time.
//	void createIAccessibleImage() {
//		objIAccessibleImage = new COMObject(new int[] {2,0,0,1,3,2}) {
//			public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
//			public long method1(long[] args) {return AddRef();}
//			public long method2(long[] args) {return Release();}
//			public long method3(long[] args) {return get_description(args[0]);}
//			public long method4(long[] args) {return get_imagePosition((int)args[0], args[1], args[2]);}
//			public long method5(long[] args) {return get_imageSize(args[0], args[1]);}
//		};
//	}

	void createIAccessibleTable2() {
		objIAccessibleTable2 = new COMObject(new int[] {2,0,0,3,1,2,1,1,1,1,1,2,2,2,2,1,2,2,1,1,1,1,1}) {
			@Override
			public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
			@Override
			public long method1(long[] args) {return AddRef();}
			@Override
			public long method2(long[] args) {return Release();}
			@Override
			public long method3(long[] args) {return get_cellAt((int)args[0], (int)args[1], args[2]);}
			@Override
			public long method4(long[] args) {return get_caption(args[0]);}
			@Override
			public long method5(long[] args) {return get_columnDescription((int)args[0], args[1]);}
			@Override
			public long method6(long[] args) {return get_nColumns(args[0]);}
			@Override
			public long method7(long[] args) {return get_nRows(args[0]);}
			@Override
			public long method8(long[] args) {return get_nSelectedCells(args[0]);}
			@Override
			public long method9(long[] args) {return get_nSelectedColumns(args[0]);}
			@Override
			public long method10(long[] args) {return get_nSelectedRows(args[0]);}
			@Override
			public long method11(long[] args) {return get_rowDescription((int)args[0], args[1]);}
			@Override
			public long method12(long[] args) {return get_selectedCells(args[0], args[1]);}
			@Override
			public long method13(long[] args) {return get_selectedColumns(args[0], args[1]);}
			@Override
			public long method14(long[] args) {return get_selectedRows(args[0], args[1]);}
			@Override
			public long method15(long[] args) {return get_summary(args[0]);}
			@Override
			public long method16(long[] args) {return get_isColumnSelected((int)args[0], args[1]);}
			@Override
			public long method17(long[] args) {return get_isRowSelected((int)args[0], args[1]);}
			@Override
			public long method18(long[] args) {return selectRow((int)args[0]);}
			@Override
			public long method19(long[] args) {return selectColumn((int)args[0]);}
			@Override
			public long method20(long[] args) {return unselectRow((int)args[0]);}
			@Override
			public long method21(long[] args) {return unselectColumn((int)args[0]);}
			@Override
			public long method22(long[] args) {return get_modelChange(args[0]);}
		};
	}

	void createIAccessibleTableCell() {
		objIAccessibleTableCell = new COMObject(new int[] {2,0,0,1,2,1,1,2,1,1,5,1}) {
			@Override
			public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
			@Override
			public long method1(long[] args) {return AddRef();}
			@Override
			public long method2(long[] args) {return Release();}
			@Override
			public long method3(long[] args) {return get_columnExtent(args[0]);}
			@Override
			public long method4(long[] args) {return get_columnHeaderCells(args[0], args[1]);}
			@Override
			public long method5(long[] args) {return get_columnIndex(args[0]);}
			@Override
			public long method6(long[] args) {return get_rowExtent(args[0]);}
			@Override
			public long method7(long[] args) {return get_rowHeaderCells(args[0], args[1]);}
			@Override
			public long method8(long[] args) {return get_rowIndex(args[0]);}
			@Override
			public long method9(long[] args) {return get_isSelected(args[0]);}
			@Override
			public long method10(long[] args) {return get_rowColumnExtents(args[0], args[1], args[2], args[3], args[4]);}
			@Override
			public long method11(long[] args) {return get_table(args[0]);}
		};
	}

	void createIAccessibleValue() {
		objIAccessibleValue = new COMObject(new int[] {2,0,0,1,1,1,1}) {
			@Override
			public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
			@Override
			public long method1(long[] args) {return AddRef();}
			@Override
			public long method2(long[] args) {return Release();}
			@Override
			public long method3(long[] args) {return get_currentValue(args[0]);}
			@Override
			public long method4(long[] args) {return setCurrentValue(args[0]);}
			@Override
			public long method5(long[] args) {return get_maximumValue(args[0]);}
			@Override
			public long method6(long[] args) {return get_minimumValue(args[0]);}
		};
	}

	void createIEnumVARIANT() {
		objIEnumVARIANT = new COMObject(new int[] {2,0,0,3,1,0,1}) {
			@Override
			public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
			@Override
			public long method1(long[] args) {return AddRef();}
			@Override
			public long method2(long[] args) {return Release();}
			@Override
			public long method3(long[] args) {return Next((int)args[0], args[1], args[2]);}
			@Override
			public long method4(long[] args) {return Skip((int)args[0]);}
			@Override
			public long method5(long[] args) {return Reset();}
			@Override
			public long method6(long[] args) {return Clone(args[0]);}
		};
	}

	void createIServiceProvider() {
		objIServiceProvider = new COMObject(new int[] {2,0,0,3}) {
			@Override
			public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
			@Override
			public long method1(long[] args) {return AddRef();}
			@Override
			public long method2(long[] args) {return Release();}
			@Override
			public long method3(long[] args) {return QueryService(args[0], args[1], args[2]);}
		};
	}

	/**
	 * Invokes platform specific functionality to allocate a new accessible object.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
	 * API for <code>Accessible</code>. It is marked public only so that it
	 * can be shared within the packages provided by SWT. It is not
	 * available on all platforms, and should never be called from
	 * application code.
	 * </p>
	 *
	 * @param control the control to get the accessible object for
	 * @return the platform specific accessible object
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public static Accessible internal_new_Accessible(Control control) {
		return new Accessible(control);
	}

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when an accessible client asks for certain strings,
	 * such as name, description, help, or keyboard shortcut. The
	 * listener is notified by sending it one of the messages defined
	 * in the <code>AccessibleListener</code> interface.
	 *
	 * @param listener the listener that should be notified when the receiver
	 * is asked for a name, description, help, or keyboard shortcut string
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleListener
	 * @see #removeAccessibleListener
	 */
	public void addAccessibleListener(AccessibleListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleListeners == null) accessibleListeners = new ArrayList<>();
		accessibleListeners.add(listener);
	}

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when an accessible client asks for custom control
	 * specific information. The listener is notified by sending it
	 * one of the messages defined in the <code>AccessibleControlListener</code>
	 * interface.
	 *
	 * @param listener the listener that should be notified when the receiver
	 * is asked for custom control specific information
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleControlListener
	 * @see #removeAccessibleControlListener
	 */
	public void addAccessibleControlListener(AccessibleControlListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleControlListeners == null) accessibleControlListeners = new ArrayList<>();
		accessibleControlListeners.add(listener);
	}

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when an accessible client asks for custom text control
	 * specific information. The listener is notified by sending it
	 * one of the messages defined in the <code>AccessibleTextListener</code>
	 * and <code>AccessibleTextExtendedListener</code> interfaces.
	 *
	 * @param listener the listener that should be notified when the receiver
	 * is asked for custom text control specific information
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleTextListener
	 * @see AccessibleTextExtendedListener
	 * @see #removeAccessibleTextListener
	 *
	 * @since 3.0
	 */
	public void addAccessibleTextListener (AccessibleTextListener listener) {
		checkWidget ();
		if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		if (listener instanceof AccessibleTextExtendedListener) {
			if (accessibleTextExtendedListeners == null) accessibleTextExtendedListeners = new ArrayList<>();
			accessibleTextExtendedListeners.add ((AccessibleTextExtendedListener) listener);
		} else {
			if (accessibleTextListeners == null) accessibleTextListeners = new ArrayList<>();
			accessibleTextListeners.add (listener);
		}
	}

	/**
	 * Adds the listener to the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleActionListener</code> interface.
	 *
	 * @param listener the listener that should be notified when the receiver
	 * is asked for <code>AccessibleActionListener</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleActionListener
	 * @see #removeAccessibleActionListener
	 *
	 * @since 3.6
	 */
	public void addAccessibleActionListener(AccessibleActionListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleActionListeners == null) accessibleActionListeners = new ArrayList<>();
		accessibleActionListeners.add(listener);
	}

	/**
	 * Adds the listener to the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleEditableTextListener</code> interface.
	 *
	 * @param listener the listener that should be notified when the receiver
	 * is asked for <code>AccessibleEditableTextListener</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleEditableTextListener
	 * @see #removeAccessibleEditableTextListener
	 *
	 * @since 3.7
	 */
	public void addAccessibleEditableTextListener(AccessibleEditableTextListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleEditableTextListeners == null) accessibleEditableTextListeners = new ArrayList<>();
		accessibleEditableTextListeners.add(listener);
	}

	/**
	 * Adds the listener to the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleHyperlinkListener</code> interface.
	 *
	 * @param listener the listener that should be notified when the receiver
	 * is asked for <code>AccessibleHyperlinkListener</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleHyperlinkListener
	 * @see #removeAccessibleHyperlinkListener
	 *
	 * @since 3.6
	 */
	public void addAccessibleHyperlinkListener(AccessibleHyperlinkListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleHyperlinkListeners == null) accessibleHyperlinkListeners = new ArrayList<>();
		accessibleHyperlinkListeners.add(listener);
	}

	/**
	 * Adds the listener to the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleTableListener</code> interface.
	 *
	 * @param listener the listener that should be notified when the receiver
	 * is asked for <code>AccessibleTableListener</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleTableListener
	 * @see #removeAccessibleTableListener
	 *
	 * @since 3.6
	 */
	public void addAccessibleTableListener(AccessibleTableListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleTableListeners == null) accessibleTableListeners = new ArrayList<>();
		accessibleTableListeners.add(listener);
	}

	/**
	 * Adds the listener to the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleTableCellListener</code> interface.
	 *
	 * @param listener the listener that should be notified when the receiver
	 * is asked for <code>AccessibleTableCellListener</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleTableCellListener
	 * @see #removeAccessibleTableCellListener
	 *
	 * @since 3.6
	 */
	public void addAccessibleTableCellListener(AccessibleTableCellListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleTableCellListeners == null) accessibleTableCellListeners = new ArrayList<>();
		accessibleTableCellListeners.add(listener);
	}

	/**
	 * Adds the listener to the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleValueListener</code> interface.
	 *
	 * @param listener the listener that should be notified when the receiver
	 * is asked for <code>AccessibleValueListener</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleValueListener
	 * @see #removeAccessibleValueListener
	 *
	 * @since 3.6
	 */
	public void addAccessibleValueListener(AccessibleValueListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleValueListeners == null) accessibleValueListeners = new ArrayList<>();
		accessibleValueListeners.add(listener);
	}

	/**
	 * Adds the listener to the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleAttributeListener</code> interface.
	 *
	 * @param listener the listener that should be notified when the receiver
	 * is asked for <code>AccessibleAttributeListener</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleAttributeListener
	 * @see #removeAccessibleAttributeListener
	 *
	 * @since 3.6
	 */
	public void addAccessibleAttributeListener(AccessibleAttributeListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleAttributeListeners == null) accessibleAttributeListeners = new ArrayList<>();
		accessibleAttributeListeners.add(listener);
	}

	/**
	 * Adds a relation with the specified type and target
	 * to the receiver's set of relations.
	 *
	 * @param type an <code>ACC</code> constant beginning with RELATION_* indicating the type of relation
	 * @param target the accessible that is the target for this relation
	 * @exception IllegalArgumentException ERROR_NULL_ARGUMENT - if the Accessible target is null
	 * @since 3.6
	 */
	public void addRelation(int type, Accessible target) {
		checkWidget();
		if (target == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		if (relations[type] == null) {
			relations[type] = new Relation(this, type);
		}
		relations[type].addTarget(target);
	}

	/**
	 * Disposes of the operating system resources associated with
	 * the receiver, and removes the receiver from its parent's
	 * list of children.
	 * <p>
	 * This method should be called when an accessible that was created
	 * with the public constructor <code>Accessible(Accessible parent)</code>
	 * is no longer needed. You do not need to call this when the receiver's
	 * control is disposed, because all <code>Accessible</code> instances
	 * associated with a control are released when the control is disposed.
	 * It is also not necessary to call this for instances of <code>Accessible</code>
	 * that were retrieved with <code>Control.getAccessible()</code>.
	 * </p>
	 *
	 * @since 3.6
	 */
	public void dispose () {
		if (parent == null) return;
		Release();
		parent.children.remove(this);
		parent = null;
	}

	long getAddress() {
		/* The address of an Accessible is the address of its IAccessible COMObject. */
		if (objIAccessible == null) createIAccessible();
		return objIAccessible.getAddress();
	}

	/**
	 * Returns the control for this Accessible object.
	 *
	 * @return the receiver's control
	 * @since 3.0
	 */
	public Control getControl() {
		return control;
	}

	/**
	 * Invokes platform specific functionality to dispose an accessible object.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
	 * API for <code>Accessible</code>. It is marked public only so that it
	 * can be shared within the packages provided by SWT. It is not
	 * available on all platforms, and should never be called from
	 * application code.
	 * </p>
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public void internal_dispose_Accessible() {
		if (iaccessible != null) {
			iaccessible.Release();
		}
		iaccessible = null;
		Release();
		List<Accessible> list = new ArrayList<>(children);
		for (Accessible accChild : list) {
			accChild.dispose();
		}
	}

	/**
	 * Invokes platform specific functionality to handle a window message.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
	 * API for <code>Accessible</code>. It is marked public only so that it
	 * can be shared within the packages provided by SWT. It is not
	 * available on all platforms, and should never be called from
	 * application code.
	 * </p>
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public long internal_WM_GETOBJECT (long wParam, long lParam) {
		if (objIAccessible == null) return 0;
		if ((int)lParam == OS.OBJID_CLIENT) {
			/* LresultFromObject([in] riid, [in] wParam, [in] pAcc)
			 * The argument pAcc is owned by the caller so reference count does not
			 * need to be incremented.
			 */
			return COM.LresultFromObject(COM.IIDIAccessible, wParam, objIAccessible.getAddress());
		}
		return 0;
	}

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when an accessible client asks for certain strings,
	 * such as name, description, help, or keyboard shortcut.
	 *
	 * @param listener the listener that should no longer be notified when the receiver
	 * is asked for a name, description, help, or keyboard shortcut string
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleListener
	 * @see #addAccessibleListener
	 */
	public void removeAccessibleListener(AccessibleListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleListeners != null) {
			accessibleListeners.remove(listener);
			if (accessibleListeners.isEmpty()) accessibleListeners = null;
		}
	}

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when an accessible client asks for custom control
	 * specific information.
	 *
	 * @param listener the listener that should no longer be notified when the receiver
	 * is asked for custom control specific information
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleControlListener
	 * @see #addAccessibleControlListener
	 */
	public void removeAccessibleControlListener(AccessibleControlListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleControlListeners != null) {
			accessibleControlListeners.remove(listener);
			if (accessibleControlListeners.isEmpty()) accessibleControlListeners = null;
		}
	}

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when an accessible client asks for custom text control
	 * specific information.
	 *
	 * @param listener the listener that should no longer be notified when the receiver
	 * is asked for custom text control specific information
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleTextListener
	 * @see AccessibleTextExtendedListener
	 * @see #addAccessibleTextListener
	 *
	 * @since 3.0
	 */
	public void removeAccessibleTextListener (AccessibleTextListener listener) {
		checkWidget ();
		if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		if (listener instanceof AccessibleTextExtendedListener) {
			if (accessibleTextExtendedListeners != null) {
				accessibleTextExtendedListeners.remove (listener);
				if (accessibleTextExtendedListeners.isEmpty()) accessibleTextExtendedListeners = null;
			}
		} else {
			if (accessibleTextListeners != null) {
				accessibleTextListeners.remove (listener);
				if (accessibleTextListeners.isEmpty()) accessibleTextListeners = null;
			}
		}
	}

	/**
	 * Removes the listener from the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleActionListener</code> interface.
	 *
	 * @param listener the listener that should no longer be notified when the receiver
	 * is asked for <code>AccessibleActionListener</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleActionListener
	 * @see #addAccessibleActionListener
	 *
	 * @since 3.6
	 */
	public void removeAccessibleActionListener(AccessibleActionListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleActionListeners != null) {
			accessibleActionListeners.remove(listener);
			if (accessibleActionListeners.isEmpty()) accessibleActionListeners = null;
		}
	}

	/**
	 * Removes the listener from the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleEditableTextListener</code> interface.
	 *
	 * @param listener the listener that should no longer be notified when the receiver
	 * is asked for <code>AccessibleEditableTextListener</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleEditableTextListener
	 * @see #addAccessibleEditableTextListener
	 *
	 * @since 3.7
	 */
	public void removeAccessibleEditableTextListener(AccessibleEditableTextListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleEditableTextListeners != null) {
			accessibleEditableTextListeners.remove(listener);
			if (accessibleEditableTextListeners.isEmpty()) accessibleEditableTextListeners = null;
		}
	}

	/**
	 * Removes the listener from the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleHyperlinkListener</code> interface.
	 *
	 * @param listener the listener that should no longer be notified when the receiver
	 * is asked for <code>AccessibleHyperlinkListener</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleHyperlinkListener
	 * @see #addAccessibleHyperlinkListener
	 *
	 * @since 3.6
	 */
	public void removeAccessibleHyperlinkListener(AccessibleHyperlinkListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleHyperlinkListeners != null) {
			accessibleHyperlinkListeners.remove(listener);
			if (accessibleHyperlinkListeners.isEmpty()) accessibleHyperlinkListeners = null;
		}
	}

	/**
	 * Removes the listener from the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleTableListener</code> interface.
	 *
	 * @param listener the listener that should no longer be notified when the receiver
	 * is asked for <code>AccessibleTableListener</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleTableListener
	 * @see #addAccessibleTableListener
	 *
	 * @since 3.6
	 */
	public void removeAccessibleTableListener(AccessibleTableListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleTableListeners != null) {
			accessibleTableListeners.remove(listener);
			if (accessibleTableListeners.isEmpty()) accessibleTableListeners = null;
		}
	}

	/**
	 * Removes the listener from the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleTableCellListener</code> interface.
	 *
	 * @param listener the listener that should no longer be notified when the receiver
	 * is asked for <code>AccessibleTableCellListener</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleTableCellListener
	 * @see #addAccessibleTableCellListener
	 *
	 * @since 3.6
	 */
	public void removeAccessibleTableCellListener(AccessibleTableCellListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleTableCellListeners != null) {
			accessibleTableCellListeners.remove(listener);
			if (accessibleTableCellListeners.isEmpty()) accessibleTableCellListeners = null;
		}
	}

	/**
	 * Removes the listener from the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleValueListener</code> interface.
	 *
	 * @param listener the listener that should no longer be notified when the receiver
	 * is asked for <code>AccessibleValueListener</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleValueListener
	 * @see #addAccessibleValueListener
	 *
	 * @since 3.6
	 */
	public void removeAccessibleValueListener(AccessibleValueListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleValueListeners != null) {
			accessibleValueListeners.remove(listener);
			if (accessibleValueListeners.isEmpty()) accessibleValueListeners = null;
		}
	}

	/**
	 * Removes the listener from the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleAttributeListener</code> interface.
	 *
	 * @param listener the listener that should no longer be notified when the receiver
	 * is asked for <code>AccessibleAttributeListener</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleAttributeListener
	 * @see #addAccessibleAttributeListener
	 *
	 * @since 3.6
	 */
	public void removeAccessibleAttributeListener(AccessibleAttributeListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleAttributeListeners != null) {
			accessibleAttributeListeners.remove(listener);
			if (accessibleAttributeListeners.isEmpty()) accessibleAttributeListeners = null;
		}
	}

	/**
	 * Removes the relation with the specified type and target
	 * from the receiver's set of relations.
	 *
	 * @param type an <code>ACC</code> constant beginning with RELATION_* indicating the type of relation
	 * @param target the accessible that is the target for this relation
	 * @exception IllegalArgumentException ERROR_NULL_ARGUMENT - if the Accessible target is null
	 * @since 3.6
	 */
	public void removeRelation(int type, Accessible target) {
		checkWidget();
		if (target == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		Relation relation = relations[type];
		if (relation != null) {
			relation.removeTarget(target);
			if (!relation.hasTargets()) {
				relations[type].Release();
				relations[type] = null;
			}
		}
	}

	/**
	 * Sends a message with event-specific data to accessible clients
	 * indicating that something has changed within a custom control.
	 *
	 * @param event an <code>ACC</code> constant beginning with EVENT_* indicating the message to send
	 * @param eventData an object containing event-specific data, or null if there is no event-specific data
	 * (eventData is specified in the documentation for individual ACC.EVENT_* constants)
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see ACC#EVENT_ACTION_CHANGED
	 * @see ACC#EVENT_ATTRIBUTE_CHANGED
	 * @see ACC#EVENT_DESCRIPTION_CHANGED
	 * @see ACC#EVENT_DOCUMENT_LOAD_COMPLETE
	 * @see ACC#EVENT_DOCUMENT_LOAD_STOPPED
	 * @see ACC#EVENT_DOCUMENT_RELOAD
	 * @see ACC#EVENT_HYPERLINK_ACTIVATED
	 * @see ACC#EVENT_HYPERLINK_ANCHOR_COUNT_CHANGED
	 * @see ACC#EVENT_HYPERLINK_END_INDEX_CHANGED
	 * @see ACC#EVENT_HYPERLINK_SELECTED_LINK_CHANGED
	 * @see ACC#EVENT_HYPERLINK_START_INDEX_CHANGED
	 * @see ACC#EVENT_HYPERTEXT_LINK_COUNT_CHANGED
	 * @see ACC#EVENT_HYPERTEXT_LINK_SELECTED
	 * @see ACC#EVENT_LOCATION_CHANGED
	 * @see ACC#EVENT_NAME_CHANGED
	 * @see ACC#EVENT_PAGE_CHANGED
	 * @see ACC#EVENT_SECTION_CHANGED
	 * @see ACC#EVENT_SELECTION_CHANGED
	 * @see ACC#EVENT_STATE_CHANGED
	 * @see ACC#EVENT_TABLE_CAPTION_CHANGED
	 * @see ACC#EVENT_TABLE_CHANGED
	 * @see ACC#EVENT_TABLE_COLUMN_DESCRIPTION_CHANGED
	 * @see ACC#EVENT_TABLE_COLUMN_HEADER_CHANGED
	 * @see ACC#EVENT_TABLE_ROW_DESCRIPTION_CHANGED
	 * @see ACC#EVENT_TABLE_ROW_HEADER_CHANGED
	 * @see ACC#EVENT_TABLE_SUMMARY_CHANGED
	 * @see ACC#EVENT_TEXT_ATTRIBUTE_CHANGED
	 * @see ACC#EVENT_TEXT_CARET_MOVED
	 * @see ACC#EVENT_TEXT_CHANGED
	 * @see ACC#EVENT_TEXT_COLUMN_CHANGED
	 * @see ACC#EVENT_TEXT_SELECTION_CHANGED
	 * @see ACC#EVENT_VALUE_CHANGED
	 *
	 * @since 3.6
	 */
	public void sendEvent(int event, Object eventData) {
		checkWidget();
		if (!isATRunning ()) return;
		if (!UseIA2) return;
		if (DEBUG) print(this + ".NotifyWinEvent " + getEventString(event) + " hwnd=" + control.handle + " childID=" + eventChildID());
		switch (event) {
			case ACC.EVENT_TABLE_CHANGED: {
				if (!(eventData instanceof int[] && ((int[])eventData).length == TABLE_MODEL_CHANGE_SIZE)) break;
				tableChange = (int[])eventData;
				OS.NotifyWinEvent (COM.IA2_EVENT_TABLE_CHANGED, control.handle, OS.OBJID_CLIENT, eventChildID());
				break;
			}
			case ACC.EVENT_TEXT_CHANGED: {
				if (!(eventData instanceof Object[] && ((Object[])eventData).length == TEXT_CHANGE_SIZE)) break;
				Object[] data = (Object[])eventData;
				int type = ((Integer)data[0]).intValue();
				switch (type) {
					case ACC.DELETE:
						textDeleted = (Object[])eventData;
						OS.NotifyWinEvent (COM.IA2_EVENT_TEXT_REMOVED, control.handle, OS.OBJID_CLIENT, eventChildID());
						break;
					case ACC.INSERT:
						textInserted = (Object[])eventData;
						OS.NotifyWinEvent (COM.IA2_EVENT_TEXT_INSERTED, control.handle, OS.OBJID_CLIENT, eventChildID());
						break;
				}
				break;
			}
			case ACC.EVENT_HYPERTEXT_LINK_SELECTED: {
				if (!(eventData instanceof Integer)) break;
	//			int index = ((Integer)eventData).intValue();
				// TODO: IA2 currently does not use the index, however the plan is to use it in future
				OS.NotifyWinEvent (COM.IA2_EVENT_HYPERTEXT_LINK_SELECTED, control.handle, OS.OBJID_CLIENT, eventChildID());
				break;
			}
			case ACC.EVENT_VALUE_CHANGED:
				OS.NotifyWinEvent (COM.EVENT_OBJECT_VALUECHANGE, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_STATE_CHANGED:
				OS.NotifyWinEvent (COM.EVENT_OBJECT_STATECHANGE, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_SELECTION_CHANGED:
				OS.NotifyWinEvent (COM.EVENT_OBJECT_SELECTIONWITHIN, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_TEXT_SELECTION_CHANGED:
				OS.NotifyWinEvent (COM.EVENT_OBJECT_TEXTSELECTIONCHANGED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_LOCATION_CHANGED:
				OS.NotifyWinEvent (COM.EVENT_OBJECT_LOCATIONCHANGE, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_NAME_CHANGED:
				OS.NotifyWinEvent (COM.EVENT_OBJECT_NAMECHANGE, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_DESCRIPTION_CHANGED:
				OS.NotifyWinEvent (COM.EVENT_OBJECT_DESCRIPTIONCHANGE, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_DOCUMENT_LOAD_COMPLETE:
				OS.NotifyWinEvent (COM.IA2_EVENT_DOCUMENT_LOAD_COMPLETE, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_DOCUMENT_LOAD_STOPPED:
				OS.NotifyWinEvent (COM.IA2_EVENT_DOCUMENT_LOAD_STOPPED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_DOCUMENT_RELOAD:
				OS.NotifyWinEvent (COM.IA2_EVENT_DOCUMENT_RELOAD, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_PAGE_CHANGED:
				OS.NotifyWinEvent (COM.IA2_EVENT_PAGE_CHANGED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_SECTION_CHANGED:
				OS.NotifyWinEvent (COM.IA2_EVENT_SECTION_CHANGED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_ACTION_CHANGED:
				OS.NotifyWinEvent (COM.IA2_EVENT_ACTION_CHANGED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_HYPERLINK_START_INDEX_CHANGED:
				OS.NotifyWinEvent (COM.IA2_EVENT_HYPERLINK_START_INDEX_CHANGED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_HYPERLINK_END_INDEX_CHANGED:
				OS.NotifyWinEvent (COM.IA2_EVENT_HYPERLINK_END_INDEX_CHANGED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_HYPERLINK_ANCHOR_COUNT_CHANGED:
				OS.NotifyWinEvent (COM.IA2_EVENT_HYPERLINK_ANCHOR_COUNT_CHANGED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_HYPERLINK_SELECTED_LINK_CHANGED:
				OS.NotifyWinEvent (COM.IA2_EVENT_HYPERLINK_SELECTED_LINK_CHANGED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_HYPERLINK_ACTIVATED:
				OS.NotifyWinEvent (COM.IA2_EVENT_HYPERLINK_ACTIVATED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_HYPERTEXT_LINK_COUNT_CHANGED:
				OS.NotifyWinEvent (COM.IA2_EVENT_HYPERTEXT_LINK_COUNT_CHANGED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_ATTRIBUTE_CHANGED:
				OS.NotifyWinEvent (COM.IA2_EVENT_ATTRIBUTE_CHANGED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_TABLE_CAPTION_CHANGED:
				OS.NotifyWinEvent (COM.IA2_EVENT_TABLE_CAPTION_CHANGED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_TABLE_COLUMN_DESCRIPTION_CHANGED:
				OS.NotifyWinEvent (COM.IA2_EVENT_TABLE_COLUMN_DESCRIPTION_CHANGED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_TABLE_COLUMN_HEADER_CHANGED:
				OS.NotifyWinEvent (COM.IA2_EVENT_TABLE_COLUMN_HEADER_CHANGED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_TABLE_ROW_DESCRIPTION_CHANGED:
				OS.NotifyWinEvent (COM.IA2_EVENT_TABLE_ROW_DESCRIPTION_CHANGED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_TABLE_ROW_HEADER_CHANGED:
				OS.NotifyWinEvent (COM.IA2_EVENT_TABLE_ROW_HEADER_CHANGED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_TABLE_SUMMARY_CHANGED:
				OS.NotifyWinEvent (COM.IA2_EVENT_TABLE_SUMMARY_CHANGED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_TEXT_ATTRIBUTE_CHANGED:
				OS.NotifyWinEvent (COM.IA2_EVENT_TEXT_ATTRIBUTE_CHANGED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_TEXT_CARET_MOVED:
				OS.NotifyWinEvent (COM.IA2_EVENT_TEXT_CARET_MOVED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
			case ACC.EVENT_TEXT_COLUMN_CHANGED:
				OS.NotifyWinEvent (COM.IA2_EVENT_TEXT_COLUMN_CHANGED, control.handle, OS.OBJID_CLIENT, eventChildID()); break;
		}
	}

	/**
	 * Sends a message with event-specific data and a childID
	 * to accessible clients, indicating that something has changed
	 * within a custom control.
	 *
	 * NOTE: This API is intended for applications that are still using childIDs.
	 * Moving forward, applications should use accessible objects instead of childIDs.
	 *
	 * @param event an <code>ACC</code> constant beginning with EVENT_* indicating the message to send
	 * @param eventData an object containing event-specific data, or null if there is no event-specific data
	 * (eventData is specified in the documentation for individual ACC.EVENT_* constants)
	 * @param childID an identifier specifying a child of the control
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see ACC#EVENT_DESCRIPTION_CHANGED
	 * @see ACC#EVENT_LOCATION_CHANGED
	 * @see ACC#EVENT_NAME_CHANGED
	 * @see ACC#EVENT_SELECTION_CHANGED
	 * @see ACC#EVENT_STATE_CHANGED
	 * @see ACC#EVENT_TEXT_SELECTION_CHANGED
	 * @see ACC#EVENT_VALUE_CHANGED
	 *
	 * @since 3.8
	 */
	public void sendEvent(int event, Object eventData, int childID) {
		checkWidget();
		if (!isATRunning ()) return;
		if (!UseIA2) return;
		int osChildID = childID == ACC.CHILDID_SELF ? eventChildID() : childIDToOs(childID);
		if (DEBUG) print(this + ".NotifyWinEvent " + getEventString(event) + " hwnd=" + control.handle + " childID=" + osChildID);
		switch (event) {
			case ACC.EVENT_STATE_CHANGED:
				OS.NotifyWinEvent (COM.EVENT_OBJECT_STATECHANGE, control.handle, OS.OBJID_CLIENT, osChildID); break;
			case ACC.EVENT_NAME_CHANGED:
				OS.NotifyWinEvent (COM.EVENT_OBJECT_NAMECHANGE, control.handle, OS.OBJID_CLIENT, osChildID); break;
			case ACC.EVENT_VALUE_CHANGED:
				OS.NotifyWinEvent (COM.EVENT_OBJECT_VALUECHANGE, control.handle, OS.OBJID_CLIENT, osChildID); break;
			case ACC.EVENT_LOCATION_CHANGED:
				OS.NotifyWinEvent (COM.EVENT_OBJECT_LOCATIONCHANGE, control.handle, OS.OBJID_CLIENT, osChildID); break;
			case ACC.EVENT_SELECTION_CHANGED:
				OS.NotifyWinEvent (COM.EVENT_OBJECT_SELECTIONWITHIN, control.handle, OS.OBJID_CLIENT, osChildID); break;
			case ACC.EVENT_TEXT_SELECTION_CHANGED:
				OS.NotifyWinEvent (COM.EVENT_OBJECT_TEXTSELECTIONCHANGED, control.handle, OS.OBJID_CLIENT, osChildID); break;
			case ACC.EVENT_DESCRIPTION_CHANGED:
				OS.NotifyWinEvent (COM.EVENT_OBJECT_DESCRIPTIONCHANGE, control.handle, OS.OBJID_CLIENT, osChildID); break;
		}
	}


	/**
	 * Sends a message to accessible clients that the child selection
	 * within a custom container control has changed.
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @since 3.0
	 */
	public void selectionChanged () {
		checkWidget();
		if (!isATRunning ()) return;
		if (DEBUG) print(this + ".NotifyWinEvent EVENT_OBJECT_SELECTIONWITHIN hwnd=" + control.handle + " childID=" + eventChildID());
		OS.NotifyWinEvent (COM.EVENT_OBJECT_SELECTIONWITHIN, control.handle, OS.OBJID_CLIENT, eventChildID());
	}

	/**
	 * Sends a message to accessible clients indicating that the focus
	 * has changed within a custom control.
	 *
	 * @param childID an identifier specifying a child of the control
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 */
	public void setFocus(int childID) {
		checkWidget();
		if (!isATRunning ()) return;
		int osChildID = childID == ACC.CHILDID_SELF ? eventChildID() : childIDToOs(childID);
		if (DEBUG) print(this + ".NotifyWinEvent EVENT_OBJECT_FOCUS hwnd=" + control.handle + " childID=" + osChildID);
		OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, control.handle, OS.OBJID_CLIENT, osChildID);
	}

	/**
	 * Sends a message to accessible clients that the text
	 * caret has moved within a custom control.
	 *
	 * @param index the new caret index within the control
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @since 3.0
	 */
	public void textCaretMoved (int index) {
		checkWidget();
		if (timer == null) {
			timer = new Runnable() {
				@Override
				public void run() {
					if (!isATRunning ()) return;
					if (DEBUG) print(this + ".NotifyWinEvent EVENT_OBJECT_LOCATIONCHANGE hwnd=" + control.handle + " childID=" + eventChildID());
					OS.NotifyWinEvent (COM.EVENT_OBJECT_LOCATIONCHANGE, control.handle, OS.OBJID_CARET, eventChildID());
					if (!UseIA2) return;
					if (DEBUG) print(this + ".NotifyWinEvent IA2_EVENT_TEXT_CARET_MOVED hwnd=" + control.handle + " childID=" + eventChildID());
					OS.NotifyWinEvent (COM.IA2_EVENT_TEXT_CARET_MOVED, control.handle, OS.OBJID_CLIENT, eventChildID());
				}
			};
		}
		control.getDisplay ().timerExec(SCROLL_RATE, timer);
	}

	/**
	 * Sends a message to accessible clients that the text
	 * within a custom control has changed.
	 *
	 * @param type the type of change, one of <code>ACC.TEXT_INSERT</code>
	 * or <code>ACC.TEXT_DELETE</code>
	 * @param startIndex the text index within the control where the insertion or deletion begins
	 * @param length the non-negative length in characters of the insertion or deletion
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see ACC#TEXT_INSERT
	 * @see ACC#TEXT_DELETE
	 *
	 * @since 3.0
	 */
	public void textChanged (int type, int startIndex, int length) {
		checkWidget();
		if (!isATRunning ()) return;
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.start = startIndex;
		event.end = startIndex + length;
		event.count = 0;
		event.type = ACC.TEXT_BOUNDARY_ALL;
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.getText(event);
		}
		if (event.result != null) {
			Object[] eventData = new Object[] {
					Integer.valueOf(type),
					Integer.valueOf(startIndex),
					Integer.valueOf(startIndex + length),
					event.result};
			sendEvent(ACC.EVENT_TEXT_CHANGED, eventData);
			return;
		}
		if (DEBUG) print(this + ".NotifyWinEvent EVENT_OBJECT_VALUECHANGE hwnd=" + control.handle + " childID=" + eventChildID());
		OS.NotifyWinEvent (COM.EVENT_OBJECT_VALUECHANGE, control.handle, OS.OBJID_CLIENT, eventChildID());
	}

	/**
	 * Sends a message to accessible clients that the text
	 * selection has changed within a custom control.
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @since 3.0
	 */
	public void textSelectionChanged () {
		checkWidget();
		if (!isATRunning ()) return;
		if (DEBUG) print(this + ".NotifyWinEvent EVENT_OBJECT_TEXTSELECTIONCHANGED hwnd=" + control.handle + " childID=" + eventChildID());
		OS.NotifyWinEvent (COM.EVENT_OBJECT_TEXTSELECTIONCHANGED, control.handle, OS.OBJID_CLIENT, eventChildID());
	}

	/* QueryInterface([in] iid, [out] ppvObject)
	 * Ownership of ppvObject transfers from callee to caller so reference count on ppvObject
	 * must be incremented before returning.  Caller is responsible for releasing ppvObject.
	 */
	int QueryInterface(long iid, long ppvObject) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		OS.MoveMemory(ppvObject, new long[] { 0 }, C.PTR_SIZEOF);
		GUID guid = new GUID();
		COM.MoveMemory(guid, iid, GUID.sizeof);

		if (COM.IsEqualGUID(guid, COM.IIDIUnknown) || COM.IsEqualGUID(guid, COM.IIDIDispatch) || COM.IsEqualGUID(guid, COM.IIDIAccessible)) {
			if (objIAccessible == null) createIAccessible();
			OS.MoveMemory(ppvObject, new long[] { objIAccessible.getAddress() }, C.PTR_SIZEOF);
			AddRef();
			if (DEBUG) print(this + ".QueryInterface guid=" + guidString(guid) + " returning " + objIAccessible.getAddress() + hresult(COM.S_OK));
			return COM.S_OK;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIEnumVARIANT)) {
			if (objIEnumVARIANT == null) createIEnumVARIANT();
			OS.MoveMemory(ppvObject, new long[] { objIEnumVARIANT.getAddress() }, C.PTR_SIZEOF);
			AddRef();
			enumIndex = 0;
			if (DEBUG) print(this + ".QueryInterface guid=" + guidString(guid) + " returning " + objIEnumVARIANT.getAddress() + hresult(COM.S_OK));
			return COM.S_OK;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIServiceProvider)) {
			if (!UseIA2) return COM.E_NOINTERFACE;
			if (accessibleActionListenersSize() > 0 || accessibleAttributeListenersSize() > 0 ||
				accessibleHyperlinkListenersSize() > 0 || accessibleTableListenersSize() > 0 ||
				accessibleTableCellListenersSize() > 0 || accessibleTextExtendedListenersSize() > 0 ||
				accessibleValueListenersSize() > 0 || accessibleControlListenersSize() > 0 || getRelationCount() > 0
				|| (control instanceof Button && ((control.getStyle() & SWT.RADIO) != 0)) || (control instanceof Composite)) {
				if (objIServiceProvider == null) createIServiceProvider();
				OS.MoveMemory(ppvObject, new long[] { objIServiceProvider.getAddress() }, C.PTR_SIZEOF);
				AddRef();
				if (DEBUG) print(this + ".QueryInterface guid=" + guidString(guid) + " returning " + objIServiceProvider.getAddress() + hresult(COM.S_OK));
				return COM.S_OK;
			}
			if (DEBUG) if (interesting(guid)) print("QueryInterface guid=" + guidString(guid) + " returning" + hresult(COM.E_NOINTERFACE));
			return COM.E_NOINTERFACE;
		}

		int code = queryAccessible2Interfaces(guid, ppvObject);
		if (code != COM.S_FALSE) {
			if (DEBUG) print(this + ".QueryInterface guid=" + guidString(guid) + " returning" + hresult(code));
			return code;
		}

		if (iaccessible != null) {
			/* Forward any other GUIDs to the OS proxy. */
			long[] ppv = new long[1];
			code = iaccessible.QueryInterface(guid, ppv);
			OS.MoveMemory(ppvObject, ppv, C.PTR_SIZEOF);
			if (DEBUG) if (interesting(guid)) print("QueryInterface guid=" + guidString(guid) + " returning super" + hresult(code));
			return code;
		}

		if (DEBUG) if (interesting(guid)) print("QueryInterface guid=" + guidString(guid) + " returning" + hresult(COM.E_NOINTERFACE));
		return COM.E_NOINTERFACE;
	}

	int accessibleListenersSize() {
		return accessibleListeners == null ? 0 : accessibleListeners.size();
	}

	int accessibleControlListenersSize() {
		return accessibleControlListeners == null ? 0 : accessibleControlListeners.size();
	}

	int accessibleValueListenersSize() {
		return accessibleValueListeners == null ? 0 : accessibleValueListeners.size();
	}

	int accessibleTextExtendedListenersSize() {
		return accessibleTextExtendedListeners == null ? 0 : accessibleTextExtendedListeners.size();
	}

	int accessibleTextListenersSize() {
		return accessibleTextListeners == null ? 0 : accessibleTextListeners.size();
	}

	int accessibleTableCellListenersSize() {
		return accessibleTableCellListeners == null ? 0 : accessibleTableCellListeners.size();
	}

	int accessibleTableListenersSize() {
		return accessibleTableListeners == null ? 0 : accessibleTableListeners.size();
	}

	int accessibleHyperlinkListenersSize() {
		return accessibleHyperlinkListeners == null ? 0 : accessibleHyperlinkListeners.size();
	}

	int accessibleEditableTextListenersSize() {
		return accessibleEditableTextListeners == null ? 0 : accessibleEditableTextListeners.size();
	}

	int accessibleAttributeListenersSize() {
		return accessibleAttributeListeners == null ? 0 : accessibleAttributeListeners.size();
	}

	int accessibleActionListenersSize() {
		return accessibleActionListeners == null ? 0 : accessibleActionListeners.size();
	}

	int AddRef() {
		refCount++;
		return refCount;
	}

	int Release() {
		refCount--;

		if (refCount == 0) {
			if (objIAccessible != null)
				objIAccessible.dispose();
			objIAccessible = null;

			if (objIEnumVARIANT != null)
				objIEnumVARIANT.dispose();
			objIEnumVARIANT = null;

			if (objIServiceProvider != null)
				objIServiceProvider.dispose();
			objIServiceProvider = null;

			if (objIAccessibleApplication != null)
				objIAccessibleApplication.dispose();
			objIAccessibleApplication = null;

			// The following lines are intentionally commented. We are not providing IAccessibleComponent at this time.
//			if (objIAccessibleComponent != null)
//				objIAccessibleComponent.dispose();
//			objIAccessibleComponent = null;

			if (objIAccessibleEditableText != null)
				objIAccessibleEditableText.dispose();
			objIAccessibleEditableText = null;

			if (objIAccessibleHyperlink != null)
				objIAccessibleHyperlink.dispose();
			objIAccessibleHyperlink = null;

			if (objIAccessibleHypertext != null)
				objIAccessibleHypertext.dispose();
			objIAccessibleHypertext = null;

			// The following lines are intentionally commented. We are not providing IAccessibleImage at this time.
//			if (objIAccessibleImage != null)
//				objIAccessibleImage.dispose();
//			objIAccessibleImage = null;

			if (objIAccessibleTable2 != null)
				objIAccessibleTable2.dispose();
			objIAccessibleTable2 = null;

			if (objIAccessibleTableCell != null)
				objIAccessibleTableCell.dispose();
			objIAccessibleTableCell = null;

			if (objIAccessibleValue != null)
				objIAccessibleValue.dispose();
			objIAccessibleValue = null;

			for (Relation relation : relations) {
				if (relation != null) relation.Release();
			}
			// TODO: also remove all relations for which 'this' is a target??
		}
		return refCount;
	}

	/* QueryService([in] guidService, [in] riid, [out] ppvObject) */
	int QueryService(long guidService, long riid, long ppvObject) {
		OS.MoveMemory(ppvObject, new long[] { 0 }, C.PTR_SIZEOF);
		GUID service = new GUID();
		COM.MoveMemory(service, guidService, GUID.sizeof);
		GUID guid = new GUID();
		COM.MoveMemory(guid, riid, GUID.sizeof);

		if (COM.IsEqualGUID(service, COM.IIDIAccessible)) {
			if (COM.IsEqualGUID(guid, COM.IIDIUnknown) || COM.IsEqualGUID(guid, COM.IIDIDispatch) || COM.IsEqualGUID(guid, COM.IIDIAccessible)) {
				if (objIAccessible == null) createIAccessible();
				if (DEBUG) print(this + ".QueryService service=" + guidString(service) + " guid=" + guidString(guid) + " returning " + objIAccessible.getAddress() + hresult(COM.S_OK));
				OS.MoveMemory(ppvObject, new long[] { objIAccessible.getAddress() }, C.PTR_SIZEOF);
				AddRef();
				return COM.S_OK;
			}
			int code = queryAccessible2Interfaces(guid, ppvObject);
			if (code != COM.S_FALSE) {
				if (DEBUG) print(this + ".QueryService service=" + guidString(service) + " guid=" + guidString(guid) + " returning" + hresult(code));
				return code;
			}
		}

		if (COM.IsEqualGUID(service, COM.IIDIAccessible2)) {
			int code = queryAccessible2Interfaces(guid, ppvObject);
			if (code != COM.S_FALSE) {
				if (DEBUG) print(this + ".*QueryService service=" + guidString(service) + " guid=" + guidString(guid) + " returning" + hresult(code));
				return code;
			}
		}

		if (iaccessible != null) {
			/* Forward any other GUIDs to the OS proxy. */
			long [] ppv = new long [1];
			int code = iaccessible.QueryInterface(COM.IIDIServiceProvider, ppv);
			if (code == COM.S_OK) {
				IServiceProvider iserviceProvider = new IServiceProvider(ppv[0]);
				long [] ppvx = new long [1];
				code = iserviceProvider.QueryService(service, guid, ppvx);
				OS.MoveMemory(ppvObject, ppvx, C.PTR_SIZEOF);
				if (DEBUG) if (interesting(service) && interesting(guid)) print("QueryService service=" + guidString(service) + " guid=" + guidString(guid) + " returning super" + hresult(code));
				return code;
			}
		}

		if (DEBUG) if (interesting(service) && interesting(guid)) print("QueryService service=" + guidString(service) + " guid=" + guidString(guid) + " returning" + hresult(COM.E_NOINTERFACE));
		return COM.E_NOINTERFACE;
	}

	int queryAccessible2Interfaces(GUID guid, long ppvObject) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (COM.IsEqualGUID(guid, COM.IIDIAccessible2)) {
			if (accessibleActionListenersSize() > 0 || accessibleAttributeListenersSize() > 0 ||
					accessibleHyperlinkListenersSize() > 0 || accessibleTableListenersSize() > 0 ||
					accessibleTableCellListenersSize() > 0 || accessibleTextExtendedListenersSize() > 0 ||
					accessibleValueListenersSize() > 0 || accessibleControlListenersSize() > 0 || getRelationCount() > 0
					|| (control instanceof Button && ((control.getStyle() & SWT.RADIO) != 0)) || (control instanceof Composite)) {
				// NOTE: IAccessible2 vtable is shared with IAccessible
				if (objIAccessible == null) createIAccessible();
				OS.MoveMemory(ppvObject, new long[] { objIAccessible.getAddress() }, C.PTR_SIZEOF);
				AddRef();
				return COM.S_OK;
			}
			return COM.E_NOINTERFACE;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleAction)) {
			if (accessibleActionListenersSize() > 0) {
				// NOTE: IAccessibleAction vtable is shared with IAccessibleHyperlink
				if (objIAccessibleHyperlink == null) createIAccessibleHyperlink();
				OS.MoveMemory(ppvObject, new long[] { objIAccessibleHyperlink.getAddress() }, C.PTR_SIZEOF);
				AddRef();
				return COM.S_OK;
			}
			return COM.E_NOINTERFACE;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleApplication)) {
			if (objIAccessibleApplication == null) createIAccessibleApplication();
			OS.MoveMemory(ppvObject, new long[] { objIAccessibleApplication.getAddress() }, C.PTR_SIZEOF);
			AddRef();
			return COM.S_OK;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleComponent)) {
			// The following lines are intentionally commented. We are not supporting IAccessibleComponent at this time.
//			if (accessibleControlListenersSize() > 0) { // TO DO: can we reduce the scope of this somehow?
//				if (objIAccessibleComponent == null) createIAccessibleComponent();
//				COM.MoveMemory(ppvObject, new long[] { objIAccessibleComponent.getAddress() }, OS.PTR_SIZEOF);
//				AddRef();
//				return COM.S_OK;
//			}
			return COM.E_NOINTERFACE;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleEditableText)) {
			if (accessibleEditableTextListenersSize() > 0) {
				if (objIAccessibleEditableText == null) createIAccessibleEditableText();
				OS.MoveMemory(ppvObject, new long[] { objIAccessibleEditableText.getAddress() }, C.PTR_SIZEOF);
				AddRef();
				return COM.S_OK;
			}
			return COM.E_NOINTERFACE;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleHyperlink)) {
			if (accessibleHyperlinkListenersSize() > 0) {
				if (objIAccessibleHyperlink == null) createIAccessibleHyperlink();
				OS.MoveMemory(ppvObject, new long[] { objIAccessibleHyperlink.getAddress() }, C.PTR_SIZEOF);
				AddRef();
				return COM.S_OK;
			}
			return COM.E_NOINTERFACE;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleHypertext)) {
			if (accessibleTextExtendedListenersSize() > 0) {
				if (objIAccessibleHypertext == null) createIAccessibleHypertext();
				OS.MoveMemory(ppvObject, new long[] { objIAccessibleHypertext.getAddress() }, C.PTR_SIZEOF);
				AddRef();
				return COM.S_OK;
			}
			return COM.E_NOINTERFACE;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleImage)) {
			// The following lines are intentionally commented. We are not supporting IAccessibleImage at this time.
//			if (getRole() == ACC.ROLE_GRAPHIC && (accessibleAccessibleListenersSize() > 0 || accessibleControlListenersSize() > 0)) {
//				if (objIAccessibleImage == null) createIAccessibleImage();
//				COM.MoveMemory(ppvObject, new long[] { objIAccessibleImage.getAddress() }, OS.PTR_SIZEOF);
//				AddRef();
//				return COM.S_OK;
//			}
			return COM.E_NOINTERFACE;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleTable)) {
			// We are not supporting IAccessibleTable at this time.
			return COM.E_NOINTERFACE;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleTable2)) {
			if (accessibleTableListenersSize() > 0) {
				if (objIAccessibleTable2 == null) createIAccessibleTable2();
				OS.MoveMemory(ppvObject, new long[] { objIAccessibleTable2.getAddress() }, C.PTR_SIZEOF);
				AddRef();
				return COM.S_OK;
			}
			return COM.E_NOINTERFACE;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleTableCell)) {
			if (accessibleTableCellListenersSize() > 0) {
				if (objIAccessibleTableCell == null) createIAccessibleTableCell();
				OS.MoveMemory(ppvObject, new long[] { objIAccessibleTableCell.getAddress() }, C.PTR_SIZEOF);
				AddRef();
				return COM.S_OK;
			}
			return COM.E_NOINTERFACE;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleText)) {
			if (accessibleTextExtendedListenersSize() > 0 || accessibleAttributeListenersSize() > 0) {
				// NOTE: IAccessibleText vtable is shared with IAccessibleHypertext
				if (objIAccessibleHypertext == null) createIAccessibleHypertext();
				OS.MoveMemory(ppvObject, new long[] { objIAccessibleHypertext.getAddress() }, C.PTR_SIZEOF);
				AddRef();
				return COM.S_OK;
			}
			return COM.E_NOINTERFACE;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleValue)) {
			if (accessibleValueListenersSize() > 0) {
				if (objIAccessibleValue == null) createIAccessibleValue();
				OS.MoveMemory(ppvObject, new long[] { objIAccessibleValue.getAddress() }, C.PTR_SIZEOF);
				AddRef();
				return COM.S_OK;
			}
			return COM.E_NOINTERFACE;
		}

		return COM.S_FALSE;
	}

	/* IAccessible::accDoDefaultAction([in] varChild) */
	int accDoDefaultAction(long varChild) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessible::accDoDefaultAction");
		if (accessibleActionListenersSize() > 0) {
			VARIANT v = getVARIANT(varChild);
			if (v.vt != COM.VT_I4) return COM.E_INVALIDARG;
			if (v.lVal == COM.CHILDID_SELF) return doAction(0);
		}
		int code = COM.DISP_E_MEMBERNOTFOUND;
		if (iaccessible != null) {
			/* If there were no action listeners, forward to the proxy. */
			code = iaccessible.accDoDefaultAction(varChild);
			if (code == COM.E_INVALIDARG) code = COM.DISP_E_MEMBERNOTFOUND; // proxy doesn't know about app childID
		}
		return code;
	}

	/* IAccessible::accHitTest([in] xLeft, [in] yTop, [out] pvarChild) */
	int accHitTest(int xLeft, int yTop, long pvarChild) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		int osChild = ACC.CHILDID_NONE;
		long osChildObject = 0;
		if (iaccessible != null) {
			/* Get the default child at point (left, top) from the OS. */
			int code = iaccessible.accHitTest(xLeft, yTop, pvarChild);
			if (code == COM.S_OK) {
				VARIANT v = getVARIANT(pvarChild);
				if (v.vt == COM.VT_I4) osChild = v.lVal;
				else if (v.vt == COM.VT_DISPATCH) {
					osChildObject = v.lVal; // TODO: don't use struct. lVal is an int.
					if (DEBUG) print(this + ".IAccessible::accHitTest() super returned VT_DISPATCH");
				}
			}
			if (accessibleControlListenersSize() == 0) {
				if (DEBUG) print(this + ".IAccessible::accHitTest returning childID=" + osChild + " from super" + hresult(code));
				return code;
			}
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = osChild == ACC.CHILDID_NONE ? ACC.CHILDID_NONE : osToChildID(osChild);
		// TODO: event.accessible = Accessible for osChildObject;
		event.x = xLeft;
		event.y = yTop;
		for (int i = 0; i < accessibleControlListenersSize(); i++) {
			AccessibleControlListener listener = accessibleControlListeners.get(i);
			listener.getChildAtPoint(event);
		}
		Accessible accessible = event.accessible;
		if (accessible != null) {
			if (DEBUG) print(this + ".IAccessible::accHitTest returning " + accessible.getAddress() + hresult(COM.S_OK));
			accessible.AddRef();
			setPtrVARIANT(pvarChild, COM.VT_DISPATCH, accessible.getAddress());
			return COM.S_OK;
		}
		int childID = event.childID;
		if (childID == ACC.CHILDID_NONE) {
			if (osChildObject != 0) {
				if (DEBUG) print(this + ".IAccessible::accHitTest returning osChildObject " + osChildObject + " from super" + hresult(COM.S_OK));
				return COM.S_OK;
			}
			if (DEBUG) print(this + ".IAccessible::accHitTest returning VT_EMPTY" + hresult(COM.S_FALSE));
			setIntVARIANT(pvarChild, COM.VT_EMPTY, 0);
			return COM.S_FALSE;
		}
		if (DEBUG) print(this + ".IAccessible::accHitTest returning " + childIDToOs(childID) + hresult(COM.S_OK));
		setIntVARIANT(pvarChild, COM.VT_I4, childIDToOs(childID));
		return COM.S_OK;
	}

	/* IAccessible::accLocation([out] pxLeft, [out] pyTop, [out] pcxWidth, [out] pcyHeight, [in] varChild) */
	int accLocation(long pxLeft, long pyTop, long pcxWidth, long pcyHeight, long varChild) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		VARIANT v = getVARIANT(varChild);
		if (v.vt != COM.VT_I4) return COM.E_INVALIDARG;
		int osLeft = 0, osTop = 0, osWidth = 0, osHeight = 0;
		if (iaccessible != null) {
			/* Get the default location from the OS. */
			int code = iaccessible.accLocation(pxLeft, pyTop, pcxWidth, pcyHeight, varChild);
			if (code == COM.E_INVALIDARG) code = COM.DISP_E_MEMBERNOTFOUND; // proxy doesn't know about app childID
			if (accessibleControlListenersSize() == 0) {
				if (DEBUG) print(this + ".IAccessible::accLocation returning from super" + hresult(code));
				return code;
			}
			if (code == COM.S_OK) {
				int[] pLeft = new int[1], pTop = new int[1], pWidth = new int[1], pHeight = new int[1];
				OS.MoveMemory(pLeft, pxLeft, 4);
				OS.MoveMemory(pTop, pyTop, 4);
				OS.MoveMemory(pWidth, pcxWidth, 4);
				OS.MoveMemory(pHeight, pcyHeight, 4);
				osLeft = pLeft[0]; osTop = pTop[0]; osWidth = pWidth[0]; osHeight = pHeight[0];
			}
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = osToChildID(v.lVal);
		event.x = osLeft;
		event.y = osTop;
		event.width = osWidth;
		event.height = osHeight;
		for (int i = 0; i < accessibleControlListenersSize(); i++) {
			AccessibleControlListener listener = accessibleControlListeners.get(i);
			listener.getLocation(event);
		}
		if (DEBUG) print(this + ".IAccessible::accLocation(" + v.lVal + ") returning x=" + event.x + " y=" + event.y + "w=" + event.width + "h=" + event.height + hresult(COM.S_OK));
		OS.MoveMemory(pxLeft, new int[] { event.x }, 4);
		OS.MoveMemory(pyTop, new int[] { event.y }, 4);
		OS.MoveMemory(pcxWidth, new int[] { event.width }, 4);
		OS.MoveMemory(pcyHeight, new int[] { event.height }, 4);
		return COM.S_OK;
	}

	/* IAccessible::accNavigate([in] navDir, [in] varStart, [out] pvarEndUpAt) */
	int accNavigate(int navDir, long varStart, long pvarEndUpAt) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessible::accNavigate");
		/* MSAA: "The accNavigate method is deprecated and should not be used." */
		int code = COM.DISP_E_MEMBERNOTFOUND;
		if (iaccessible != null) {
			/* Since many of the native controls still handle accNavigate,
			 * we will continue to send this through to the proxy. */
			code = iaccessible.accNavigate(navDir, varStart, pvarEndUpAt);
			if (code == COM.E_INVALIDARG) code = COM.DISP_E_MEMBERNOTFOUND; // proxy doesn't know about app childID
		}
		return code;
	}

	// TODO: Consider supporting this in future.
	/* IAccessible::accSelect([in] flagsSelect, [in] varChild) */
	int accSelect(int flagsSelect, long varChild) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		int code = COM.DISP_E_MEMBERNOTFOUND;
		if (iaccessible != null) {
			/* Currently, we don't expose this as API. Forward to the proxy. */
			code = iaccessible.accSelect(flagsSelect, varChild);
			if (code == COM.E_INVALIDARG) code = COM.DISP_E_MEMBERNOTFOUND; // proxy doesn't know about app childID
		}
		if (DEBUG) print(this + ".IAccessible::accSelect(" + flagsSelect + ") returning" + hresult(code));
		return code;
	}

	/* IAccessible::get_accChild([in] varChild, [out] ppdispChild)
	 * Ownership of ppdispChild transfers from callee to caller so reference count on ppdispChild
	 * must be incremented before returning.  The caller is responsible for releasing ppdispChild.
	 */
	int get_accChild(long varChild, long ppdispChild) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		VARIANT v = getVARIANT(varChild);
		if (v.vt != COM.VT_I4) return COM.E_INVALIDARG;
		if (v.lVal == COM.CHILDID_SELF) {
			if (DEBUG) print(this + ".IAccessible::get_accChild(" + v.lVal + ") returning " + getAddress() + hresult(COM.S_OK));
			AddRef();
			OS.MoveMemory(ppdispChild, new long[] { getAddress() }, C.PTR_SIZEOF);
			return COM.S_OK;
		}
		final int childID = osToChildID(v.lVal);
		int code = COM.S_FALSE;
		Accessible osAccessible = null;
		if (iaccessible != null) {
			/* Get the default child from the OS. */
			code = iaccessible.get_accChild(varChild, ppdispChild);
			if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
			if (code == COM.S_OK && control instanceof ToolBar) {
				ToolBar toolBar = (ToolBar) control;
				final ToolItem item = toolBar.getItem(childID);
				if (item != null && (item.getStyle() & SWT.DROP_DOWN) != 0) {
					long[] addr = new long[1];
					OS.MoveMemory(addr, ppdispChild, C.PTR_SIZEOF);
					boolean found = false;
					for (Accessible accChild : children) {
						if (accChild.item == item) {
							/*
							 * MSAA uses a new accessible for the child
							 * so we dispose the old and use the new.
							 */
							accChild.dispose();
							accChild.item = null;
							found = true;
							break;
						}
					}
					osAccessible = new Accessible(this, addr[0]);
					osAccessible.item = item;
					if (!found) {
						item.addListener(SWT.Dispose, e -> {
							List<Accessible> list = new ArrayList<>(children);
							for (Accessible accChild : list) {
								if (accChild.item == item) {
									accChild.dispose();
								}
							}
						});
					}
					osAccessible.addAccessibleListener(new AccessibleAdapter() {
						@Override
						public void getName(AccessibleEvent e) {
							if (e.childID == ACC.CHILDID_SELF) {
								AccessibleEvent event = new AccessibleEvent(Accessible.this);
								event.childID = childID;
								for (int i = 0; i < accessibleListenersSize(); i++) {
									AccessibleListener listener = accessibleListeners.get(i);
									listener.getName(event);
								}
								e.result = event.result;
							}
						}
					});
				}
			}
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = childID;
		for (int i = 0; i < accessibleControlListenersSize(); i++) {
			AccessibleControlListener listener = accessibleControlListeners.get(i);
			listener.getChild(event);
		}
		Accessible accessible = event.accessible;
		if (accessible == null) accessible = osAccessible;
		if (accessible != null) {
			if (DEBUG) print(this + ".IAccessible::get_accChild(" + v.lVal + ") returning " + accessible.getAddress() + hresult(COM.S_OK));
			accessible.AddRef();
			OS.MoveMemory(ppdispChild, new long[] { accessible.getAddress() }, C.PTR_SIZEOF);
			return COM.S_OK;
		}
		if (DEBUG) print(this + ".IAccessible::get_accChild(" + v.lVal + ") returning from super" + hresult(code));
		return code;
	}

	/* IAccessible::get_accChildCount([out] pcountChildren) */
	int get_accChildCount(long pcountChildren) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		int osChildCount = 0;
		if (iaccessible != null) {
			/* Get the default child count from the OS. */
			int code = iaccessible.get_accChildCount(pcountChildren);
			if (code == COM.S_OK) {
				int[] pChildCount = new int[1];
				OS.MoveMemory(pChildCount, pcountChildren, 4);
				osChildCount = pChildCount[0];
			}
			if (accessibleControlListenersSize() == 0) {
				if (DEBUG) print(this + ".IAccessible::get_accChildCount() returning " + osChildCount + " from super" + hresult(code));
				return code;
			}
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = ACC.CHILDID_SELF;
		event.detail = osChildCount;
		for (int i = 0; i < accessibleControlListenersSize(); i++) {
			AccessibleControlListener listener = accessibleControlListeners.get(i);
			listener.getChildCount(event);
		}
		if (DEBUG) print(this + ".IAccessible::get_accChildCount() returning " + event.detail + hresult(COM.S_OK));
		OS.MoveMemory(pcountChildren, new int[] { event.detail }, 4);
		return COM.S_OK;
	}

	/* IAccessible::get_accDefaultAction([in] varChild, [out] pszDefaultAction) */
	int get_accDefaultAction(long varChild, long pszDefaultAction) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessible::get_accDefaultAction");
		VARIANT v = getVARIANT(varChild);
		if (v.vt != COM.VT_I4) return COM.E_INVALIDARG;
		int code = COM.DISP_E_MEMBERNOTFOUND;
		String osDefaultAction = null;
		if (iaccessible != null) {
			/* Get the default defaultAction from the OS. */
			code = iaccessible.get_accDefaultAction(varChild, pszDefaultAction);
			if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
			if (accessibleControlListenersSize() == 0) return code;
			if (code == COM.S_OK) {
				long[] pDefaultAction = new long[1];
				OS.MoveMemory(pDefaultAction, pszDefaultAction, C.PTR_SIZEOF);
				int size = COM.SysStringByteLen(pDefaultAction[0]);
				if (size > 0) {
					char[] buffer = new char[(size + 1) /2];
					OS.MoveMemory(buffer, pDefaultAction[0], size);
					osDefaultAction = new String(buffer);
				}
			}
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = osToChildID(v.lVal);
		event.result = osDefaultAction;
		for (int i = 0; i < accessibleControlListenersSize(); i++) {
			AccessibleControlListener listener = accessibleControlListeners.get(i);
			listener.getDefaultAction(event);
		}
		if ((event.result == null || event.result.length() == 0) && v.lVal == COM.CHILDID_SELF) {
			code = get_name(0, pszDefaultAction);
		}
		if (event.result == null) return code;
		if (event.result.length() == 0) return COM.S_FALSE;
		setString(pszDefaultAction, event.result);
		return COM.S_OK;
	}

	/* IAccessible::get_accDescription([in] varChild, [out] pszDescription) */
	int get_accDescription(long varChild, long pszDescription) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		/*
		 * MSAA: "The accDescription property is not supported in the transition to
		 * UI Automation. MSAA servers and applications should not use it."
		 *
		 * TODO: Description was exposed as SWT API. We will need to either deprecate this (?),
		 * or find a suitable replacement. Also, check description property on other platforms.
		 * If it is truly deprecated for MSAA, then perhaps it can be reused for IAccessibleImage.
		 * Note that the trick to expose tree columns (below) was not supported by screen readers,
		 * so it should be replaced.
		 */
		VARIANT v = getVARIANT(varChild);
		if (v.vt != COM.VT_I4) return COM.E_INVALIDARG;
		int code = COM.DISP_E_MEMBERNOTFOUND;
		String osDescription = null;
		if (iaccessible != null) {
			/* Get the default description from the OS. */
			code = iaccessible.get_accDescription(varChild, pszDescription);
			if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
			// TEMPORARY CODE - process tree even if there are no apps listening
			if (accessibleListenersSize() == 0 && !(control instanceof Tree)) {
				if (DEBUG) print(this + ".IAccessible::get_accDescription(" + v.lVal + ") returning super" + hresult(code));
				return code;
			}
			if (code == COM.S_OK) {
				long[] pDescription = new long[1];
				OS.MoveMemory(pDescription, pszDescription, C.PTR_SIZEOF);
				int size = COM.SysStringByteLen(pDescription[0]);
				if (size > 0) {
					char[] buffer = new char[(size + 1) /2];
					OS.MoveMemory(buffer, pDescription[0], size);
					osDescription = new String(buffer);
				}
			}
		}

		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = osToChildID(v.lVal);
		event.result = osDescription;

		// TEMPORARY CODE
		/* Currently our tree columns are emulated using custom draw,
		 * so we need to create the description using the tree column
		 * header text and tree item text. */
		if (v.lVal != COM.CHILDID_SELF) {
			if (control instanceof Tree) {
				Tree tree = (Tree) control;
				int columnCount = tree.getColumnCount ();
				if (columnCount > 1) {
					long hwnd = control.handle, hItem = 0;
					hItem = OS.SendMessage (hwnd, OS.TVM_MAPACCIDTOHTREEITEM, v.lVal, 0);
					Widget widget = tree.getDisplay ().findWidget (hwnd, hItem);
					event.result = "";
					if (widget instanceof TreeItem) {
						TreeItem item = (TreeItem) widget;
						for (int i = 1; i < columnCount; i++) {
							if (tree.isDisposed() || item.isDisposed()) {
								event.result = "";
								return COM.S_OK;
							}
							event.result += tree.getColumn(i).getText() + ": " + item.getText(i);
							if (i + 1 < columnCount) event.result += ", ";
						}
					}
				}
			}
		}
		for (int i = 0; i < accessibleListenersSize(); i++) {
			AccessibleListener listener = accessibleListeners.get(i);
			listener.getDescription(event);
		}
		if (DEBUG) print(this + ".IAccessible::get_accDescription(" + v.lVal + ") returning " + event.result + hresult(event.result == null ? code : event.result.length() == 0 ? COM.S_FALSE : COM.S_OK));
		if (event.result == null) return code;
		if (event.result.length() == 0) return COM.S_FALSE;
		setString(pszDescription, event.result);
		return COM.S_OK;
	}

	/* IAccessible::get_accFocus([out] pvarChild)
	 * Ownership of pvarChild transfers from callee to caller so reference count on pvarChild
	 * must be incremented before returning.  The caller is responsible for releasing pvarChild.
	 */
	int get_accFocus(long pvarChild) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		int osChild = ACC.CHILDID_NONE;
		if (iaccessible != null) {
			/* Get the default focus child from the OS. */
			int code = iaccessible.get_accFocus(pvarChild);
			if (code == COM.S_OK) {
				VARIANT v = getVARIANT(pvarChild);
				if (v.vt == COM.VT_I4) osChild = v.lVal;
				// TODO: need to check VT_DISPATCH (don't use struct)
				if (DEBUG) if (v.vt == COM.VT_DISPATCH) print("IAccessible::get_accFocus() super returned VT_DISPATCH");
			}
			if (accessibleControlListenersSize() == 0) {
				if (DEBUG) print(this + ".IAccessible::get_accFocus() returning childID=" + osChild + " from super" + hresult(code));
				return code;
			}
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = osChild == ACC.CHILDID_NONE ? ACC.CHILDID_NONE : osToChildID(osChild);
		for (int i = 0; i < accessibleControlListenersSize(); i++) {
			AccessibleControlListener listener = accessibleControlListeners.get(i);
			listener.getFocus(event);
		}
		Accessible accessible = event.accessible;
		if (accessible != null) {
			if (DEBUG) print(this + ".IAccessible::get_accFocus() returning accessible " + accessible.getAddress() + hresult(COM.S_OK));
			accessible.AddRef();
			setPtrVARIANT(pvarChild, COM.VT_DISPATCH, accessible.getAddress());
			return COM.S_OK;
		}
		int childID = event.childID;
		if (childID == ACC.CHILDID_NONE) {
			if (DEBUG) print(this + ".IAccessible::get_accFocus() returning VT_EMPTY" + hresult(COM.S_FALSE));
			setIntVARIANT(pvarChild, COM.VT_EMPTY, 0);
			return COM.S_FALSE;
		}
		if (childID == ACC.CHILDID_SELF) {
			if (DEBUG) print(this + ".IAccessible::get_accFocus() returning CHILDID_SELF " + hresult(COM.S_OK));
			AddRef();
			setIntVARIANT(pvarChild, COM.VT_I4, COM.CHILDID_SELF);
			return COM.S_OK;
		}
		if (DEBUG) print(this + ".IAccessible::get_accFocus() returning childID " + childIDToOs(childID) + hresult(COM.S_OK));
		setIntVARIANT(pvarChild, COM.VT_I4, childIDToOs(childID));
		return COM.S_OK;
	}

	/* IAccessible::get_accHelp([in] varChild, [out] pszHelp) */
	int get_accHelp(long varChild, long pszHelp) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessible::get_accHelp");
		VARIANT v = getVARIANT(varChild);
		if (v.vt != COM.VT_I4) return COM.E_INVALIDARG;
		int code = COM.DISP_E_MEMBERNOTFOUND;
		String osHelp = null;
		if (iaccessible != null) {
			/* Get the default help string from the OS. */
			code = iaccessible.get_accHelp(varChild, pszHelp);
			if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
			if (accessibleListenersSize() == 0) return code;
			if (code == COM.S_OK) {
				long[] pHelp = new long[1];
				OS.MoveMemory(pHelp, pszHelp, C.PTR_SIZEOF);
				int size = COM.SysStringByteLen(pHelp[0]);
				if (size > 0) {
					char[] buffer = new char[(size + 1) /2];
					OS.MoveMemory(buffer, pHelp[0], size);
					osHelp = new String(buffer);
				}
			}
		}

		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = osToChildID(v.lVal);
		event.result = osHelp;
		for (int i = 0; i < accessibleListenersSize(); i++) {
			AccessibleListener listener = accessibleListeners.get(i);
			listener.getHelp(event);
		}
		if (event.result == null) return code;
		if (event.result.length() == 0) return COM.S_FALSE;
		setString(pszHelp, event.result);
		return COM.S_OK;
	}

	/* IAccessible::get_accHelpTopic([out] pszHelpFile, [in] varChild, [out] pidTopic) */
	int get_accHelpTopic(long pszHelpFile, long varChild, long pidTopic) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessible::get_accHelpTopic");
		/* MSAA: "The accHelpTopic property is deprecated and should not be used." */
		int code = COM.DISP_E_MEMBERNOTFOUND;
		if (iaccessible != null) {
			/* Since it is possible that a native control might still handle get_accHelpTopic,
			 * we will continue to send this through to the proxy. */
			code = iaccessible.get_accHelpTopic(pszHelpFile, varChild, pidTopic);
			if (code == COM.E_INVALIDARG) code = COM.DISP_E_MEMBERNOTFOUND; // proxy doesn't know about app childID
		}
		return code;
	}

	/* IAccessible::get_accKeyboardShortcut([in] varChild, [out] pszKeyboardShortcut) */
	int get_accKeyboardShortcut(long varChild, long pszKeyboardShortcut) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessible::get_accKeyboardShortcut");
		VARIANT v = getVARIANT(varChild);
		if (v.vt != COM.VT_I4) return COM.E_INVALIDARG;
		int code = COM.DISP_E_MEMBERNOTFOUND;
		String osKeyboardShortcut = null;
		if (iaccessible != null) {
			/* Get the default keyboard shortcut from the OS. */
			code = iaccessible.get_accKeyboardShortcut(varChild, pszKeyboardShortcut);
			if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
			/* Process TabFolder even if there are no apps listening. */
			if (accessibleListenersSize() == 0 && !(control instanceof TabFolder)) return code;
			if (code == COM.S_OK) {
				long[] pKeyboardShortcut = new long[1];
				OS.MoveMemory(pKeyboardShortcut, pszKeyboardShortcut, C.PTR_SIZEOF);
				int size = COM.SysStringByteLen(pKeyboardShortcut[0]);
				if (size > 0) {
					char[] buffer = new char[(size + 1) /2];
					OS.MoveMemory(buffer, pKeyboardShortcut[0], size);
					osKeyboardShortcut = new String(buffer);
				}
			}
		}

		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = osToChildID(v.lVal);
		event.result = osKeyboardShortcut;
		/* SWT TabFolders use Ctrl+PageDown to switch pages (not Ctrl+Tab). */
		if (v.lVal == COM.CHILDID_SELF && control instanceof TabFolder) {
			event.result = SWT.getMessage ("SWT_SwitchPage_Shortcut"); //$NON-NLS-1$
		}
		for (int i = 0; i < accessibleListenersSize(); i++) {
			AccessibleListener listener = accessibleListeners.get(i);
			listener.getKeyboardShortcut(event);
		}
		if (event.result == null) return code;
		if (event.result.length() == 0) return COM.S_FALSE;
		setString(pszKeyboardShortcut, event.result);
		return COM.S_OK;
	}

	/* IAccessible::get_accName([in] varChild, [out] pszName) */
	int get_accName(long varChild, long pszName) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		VARIANT v = getVARIANT(varChild);
		if (v.vt != COM.VT_I4) return COM.E_INVALIDARG;
		int code = COM.S_FALSE;
		String osName = null;
		if (iaccessible != null) {
			/* Get the default name from the OS. */
			code = iaccessible.get_accName(varChild, pszName);
			if (code == COM.S_OK) {
				long[] pName = new long[1];
				OS.MoveMemory(pName, pszName, C.PTR_SIZEOF);
				int size = COM.SysStringByteLen(pName[0]);
				if (size > 0) {
					char[] buffer = new char[(size + 1) /2];
					OS.MoveMemory(buffer, pName[0], size);
					osName = new String(buffer);
				}
			}
			if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
			/* Process Text even if there are no apps listening. */
			if (accessibleListenersSize() == 0 && !(control instanceof Text)) {
				if (DEBUG) print(this + ".IAccessible::get_accName(" + v.lVal + ") returning name=" + osName + " from super" + hresult(code));
				return code;
			}
		}

		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = osToChildID(v.lVal);
		event.result = osName;
		/*
		* Bug in Windows:  A Text with SWT.SEARCH style uses EM_SETCUEBANNER
		* to set the message text. This text should be used as the control's
		* accessible name, however it is not. The fix is to return the message
		* text here as the accName (unless there is a preceding label).
		*/
		if (control instanceof Text && (control.getStyle() & SWT.SEARCH) != 0 && osName == null) {
			event.result = ((Text) control).getMessage();
		}
		for (int i = 0; i < accessibleListenersSize(); i++) {
			AccessibleListener listener = accessibleListeners.get(i);
			listener.getName(event);
		}
		if (DEBUG) print(this + ".IAccessible::get_accName(" + v.lVal + ") returning " + event.result + hresult(event.result == null ? code : event.result.length() == 0 ? COM.S_FALSE : COM.S_OK));
		if (event.result == null) return code;
		if (event.result.length() == 0) return COM.S_FALSE;
		setString(pszName, event.result);
		return COM.S_OK;
	}

	/* IAccessible::get_accParent([out] ppdispParent)
	 * Ownership of ppdispParent transfers from callee to caller so reference count on ppdispParent
	 * must be incremented before returning.  The caller is responsible for releasing ppdispParent.
	 */
	int get_accParent(long ppdispParent) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		int code = COM.DISP_E_MEMBERNOTFOUND;
		if (iaccessible != null) {
			/* Currently, we don't expose this as API. Forward to the proxy. */
			code = iaccessible.get_accParent(ppdispParent);
		}
		if (parent != null) {
			/* For lightweight accessibles, return the accessible's parent. */
			parent.AddRef();
			OS.MoveMemory(ppdispParent, new long[] { parent.getAddress() }, C.PTR_SIZEOF);
			code = COM.S_OK;
		}
		if (DEBUG) print(this + ".IAccessible::get_accParent() returning" + (parent != null ? " " + parent.getAddress() : " from super") + hresult(code));
		return code;
	}

	/* IAccessible::get_accRole([in] varChild, [out] pvarRole) */
	int get_accRole(long varChild, long pvarRole) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		VARIANT v = getVARIANT(varChild);
		if (v.vt != COM.VT_I4) return COM.E_INVALIDARG;
		int osRole = COM.ROLE_SYSTEM_CLIENT;
		if (iaccessible != null) {
			/* Get the default role from the OS. */
			int code = iaccessible.get_accRole(varChild, pvarRole);
			if (code == COM.S_OK) {
				VARIANT v2 = getVARIANT(pvarRole);
				if (v2.vt == COM.VT_I4) osRole = v2.lVal;
			}
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = osToChildID(v.lVal);
		event.detail = osToRole(osRole);
		// TEMPORARY CODE
		/* Currently our checkbox table and tree are emulated using state mask images,
		 * so we need to specify 'checkbox' role for the items. */
		if (control instanceof Tree || control instanceof Table) {
			if (v.lVal != COM.CHILDID_SELF && (control.getStyle() & SWT.CHECK) != 0) event.detail = ACC.ROLE_CHECKBUTTON;
		}
		for (int i = 0; i < accessibleControlListenersSize(); i++) {
			AccessibleControlListener listener = accessibleControlListeners.get(i);
			listener.getRole(event);
		}
		if (DEBUG) print(this + ".IAccessible::get_accRole(" + v.lVal + ") returning " + getRoleString(roleToOs(event.detail)) + hresult(COM.S_OK));
		setIntVARIANT(pvarRole, COM.VT_I4, roleToOs(event.detail));
		return COM.S_OK;
	}

	/* IAccessible::get_accSelection([out] pvarChildren)
	 * Ownership of pvarChildren transfers from callee to caller so reference count on pvarChildren
	 * must be incremented before returning.  The caller is responsible for releasing pvarChildren.
	 */
	int get_accSelection(long pvarChildren) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessible::get_accSelection");
		int osChild = ACC.CHILDID_NONE;
		long osChildObject = 0;
		if (iaccessible != null) {
			/* Get the default selection from the OS. */
			int code = iaccessible.get_accSelection(pvarChildren);
			if (accessibleControlListenersSize() == 0) return code;
			if (code == COM.S_OK) {
				VARIANT v = getVARIANT(pvarChildren);
				if (v.vt == COM.VT_I4) {
					osChild = osToChildID(v.lVal);
				} else if (v.vt == COM.VT_DISPATCH) {
					osChildObject = v.lVal; // TODO: don't use struct; lVal is an int
				} else if (v.vt == COM.VT_UNKNOWN) {
					osChild = ACC.CHILDID_MULTIPLE;
					// TODO: Should get IEnumVARIANT from punkVal field, and enumerate children...
				}
			}
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = osChild;
		for (int i = 0; i < accessibleControlListenersSize(); i++) {
			AccessibleControlListener listener = accessibleControlListeners.get(i);
			listener.getSelection(event);
		}
		Accessible accessible = event.accessible;
		if (accessible != null) {
			accessible.AddRef();
			setPtrVARIANT(pvarChildren, COM.VT_DISPATCH, accessible.getAddress());
			return COM.S_OK;
		}
		int childID = event.childID;
		if (childID == ACC.CHILDID_NONE) {
			if (osChildObject != 0) return COM.S_OK;
			setIntVARIANT(pvarChildren, COM.VT_EMPTY, 0);
			return COM.S_FALSE;
		}
		if (childID == ACC.CHILDID_MULTIPLE) {
			// TODO: return an enumeration for event.children (currently just returns enumeration from proxy)
			//AddRef();
			//setPtrVARIANT(pvarChildren, COM.VT_UNKNOWN, getAddress());
			return COM.S_OK;
		}
		if (childID == ACC.CHILDID_SELF) {
			AddRef();
			setPtrVARIANT(pvarChildren, COM.VT_DISPATCH, getAddress());
			return COM.S_OK;
		}
		setIntVARIANT(pvarChildren, COM.VT_I4, childIDToOs(childID));
		return COM.S_OK;
	}

	/* IAccessible::get_accState([in] varChild, [out] pvarState) */
	int get_accState(long varChild, long pvarState) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		VARIANT v = getVARIANT(varChild);
		if (v.vt != COM.VT_I4) return COM.E_INVALIDARG;
		int osState = 0;
		if (iaccessible != null) {
			/* Get the default state from the OS. */
			int code = iaccessible.get_accState(varChild, pvarState);
			if (code == COM.S_OK) {
				VARIANT v2 = getVARIANT(pvarState);
				if (v2.vt == COM.VT_I4) osState = v2.lVal;
			}
		}

		boolean grayed = false;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = osToChildID(v.lVal);
		event.detail = osToState(osState);
		// TEMPORARY CODE
		/* Currently our checkbox table and tree are emulated using state mask
		 * images, so we need to determine if the item state is 'checked'. */
		if (v.lVal != COM.CHILDID_SELF) {
			if (control instanceof Tree && (control.getStyle() & SWT.CHECK) != 0) {
				long hwnd = control.handle;
				TVITEM tvItem = new TVITEM ();
				tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
				tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
				tvItem.hItem = OS.SendMessage (hwnd, OS.TVM_MAPACCIDTOHTREEITEM, v.lVal, 0);
				long result = OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem);
				boolean checked = (result != 0) && (((tvItem.state >> 12) & 1) == 0);
				if (checked) event.detail |= ACC.STATE_CHECKED;
				grayed = tvItem.state >> 12 > 2;
			} else if (control instanceof Table && (control.getStyle() & SWT.CHECK) != 0) {
				Table table = (Table) control;
				int index = event.childID;
				if (0 <= index && index < table.getItemCount()) {
					TableItem item = table.getItem(index);
					if (item.getChecked()) event.detail |= ACC.STATE_CHECKED;
					if (item.getGrayed()) grayed = true;
				}
			}
		}
		for (int i = 0; i < accessibleControlListenersSize(); i++) {
			AccessibleControlListener listener = accessibleControlListeners.get(i);
			listener.getState(event);
		}
		int state = stateToOs(event.detail);
		if ((state & ACC.STATE_CHECKED) != 0 && grayed) {
			state &= ~ COM.STATE_SYSTEM_CHECKED;
			state |= COM.STATE_SYSTEM_MIXED;
		}
		if (DEBUG) print(this + ".IAccessible::get_accState(" + v.lVal + ") returning" + getStateString(state) + hresult(COM.S_OK));
		setIntVARIANT(pvarState, COM.VT_I4, state);
		return COM.S_OK;
	}

	/* IAccessible::get_accValue([in] varChild, [out] pszValue) */
	int get_accValue(long varChild, long pszValue) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		VARIANT v = getVARIANT(varChild);
		if (v.vt != COM.VT_I4) return COM.E_INVALIDARG;
		int code = COM.DISP_E_MEMBERNOTFOUND;
		String osValue = null;
		if (iaccessible != null) {
			/* Get the default value string from the OS. */
			code = iaccessible.get_accValue(varChild, pszValue);
			if (code == COM.S_OK) {
				long[] pValue = new long[1];
				OS.MoveMemory(pValue, pszValue, C.PTR_SIZEOF);
				int size = COM.SysStringByteLen(pValue[0]);
				if (size > 0) {
					char[] buffer = new char[(size + 1) /2];
					OS.MoveMemory(buffer, pValue[0], size);
					osValue = new String(buffer);
				}
			}
			if (code == COM.E_INVALIDARG) code = COM.DISP_E_MEMBERNOTFOUND; // proxy doesn't know about app childID
			/* Process Text even if there are no apps listening. */
			if (accessibleControlListenersSize() == 0 && !(control instanceof Text)) {
				if (DEBUG) print(this + ".IAccessible::get_accValue(" + v.lVal + ") returning value=" + osValue + " from super" + hresult(code));
				return code;
			}
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = osToChildID(v.lVal);
		event.result = osValue;
		/*
		* Bug in Windows:  A Text with SWT.SEARCH style uses EM_SETCUEBANNER
		* to set the message text. This text should be used as the control's
		* accessible value when the control does not have focus, however it
		* is not. The fix is to return the message text here as the accValue.
		*/
		if (control instanceof Text && (control.getStyle() & SWT.SEARCH) != 0 && !control.isFocusControl()) {
			event.result = ((Text) control).getMessage();
		}
		for (int i = 0; i < accessibleControlListenersSize(); i++) {
			AccessibleControlListener listener = accessibleControlListeners.get(i);
			listener.getValue(event);
		}
		if (DEBUG) print(this + ".IAccessible::get_accValue(" + v.lVal + ") returning " + event.result + hresult(event.result == null ? code : COM.S_OK));
		if (event.result == null) return code;
		// empty string is a valid value, so do not test for it
		setString(pszValue, event.result);
		return COM.S_OK;
	}

	/* put_accName([in] varChild, [in] szName) */
	int put_accName(long varChild, long szName) {
		/* MSAA: "The IAccessible::put_accName method is no longer supported. Servers should return E_NOTIMPL." */
		return COM.E_NOTIMPL;
	}

	/* put_accValue([in] varChild, [in] szValue) */
	int put_accValue(long varChild, long szValue) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		/* MSAA: this method is supported for some UI elements (usually edit controls). */
		VARIANT v = getVARIANT(varChild);
		if (v.vt != COM.VT_I4) return COM.E_INVALIDARG;
		int code = COM.DISP_E_MEMBERNOTFOUND;
		if (v.lVal == COM.CHILDID_SELF && accessibleEditableTextListenersSize() > 0) {
			/*
			 * If the object supports AccessibleEditableTextListener.replaceText,
			 * then give the object a chance to handle this event.
			 */
			AccessibleEditableTextEvent event = new AccessibleEditableTextEvent(this);
			event.start = 0;
			event.end = getCharacterCount();
			if (event.end >= 0) {
				int size = COM.SysStringByteLen(szValue);
				char [] buffer = new char [(size + 1) / 2];
				OS.MoveMemory (buffer, szValue, size);
				event.string = new String (buffer);
				for (int i = 0; i < accessibleEditableTextListenersSize(); i++) {
					AccessibleEditableTextListener listener = accessibleEditableTextListeners.get(i);
					listener.replaceText(event);
				}
				if (event.result != null && event.result.equals(ACC.OK)) code = COM.S_OK;
				if (DEBUG) print(this + ".IAccessible::put_accValue(" + v.lVal + ", \"" + event.string + "\") returning " + hresult(code));
			}
		}
		if (code != COM.S_OK && iaccessible != null) {
			/* If the object did not handle the event, then forward to the proxy. */
			code = iaccessible.put_accValue(varChild, szValue);
			if (code == COM.E_INVALIDARG) code = COM.DISP_E_MEMBERNOTFOUND; // proxy doesn't know about app childID
			if (DEBUG) print(this + ".IAccessible::put_accValue(" + v.lVal + ") returning " + hresult(code) + " from proxy");
		}
		return code;
	}

	/* IEnumVARIANT methods: Next, Skip, Reset, Clone */
	/* Retrieve the next celt items in the enumeration sequence.
	 * If there are fewer than the requested number of elements left
	 * in the sequence, retrieve the remaining elements.
	 * The number of elements actually retrieved is returned in pceltFetched
	 * (unless the caller passed in NULL for that parameter).
	 */

	/* IEnumVARIANT::Next([in] celt, [out] rgvar, [in, out] pceltFetched)
	 * Ownership of rgvar transfers from callee to caller so reference count on rgvar
	 * must be incremented before returning.  The caller is responsible for releasing rgvar.
	 */
	int Next(int celt, long rgvar, long pceltFetched) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IEnumVARIANT::Next");
		/* If there are no listeners, query the proxy for
		 * its IEnumVariant, and get the Next items from it.
		 */
		if (iaccessible != null && accessibleControlListenersSize() == 0) {
			long[] ppvObject = new long[1];
			int code = iaccessible.QueryInterface(COM.IIDIEnumVARIANT, ppvObject);
			if (code != COM.S_OK) return code;
			IEnumVARIANT ienumvariant = new IEnumVARIANT(ppvObject[0]);
			int[] celtFetched = new int[1];
			code = ienumvariant.Next(celt, rgvar, celtFetched);
			ienumvariant.Release();
			OS.MoveMemory(pceltFetched, celtFetched, 4);
			return code;
		}

		if (rgvar == 0) return COM.E_INVALIDARG;
		if (pceltFetched == 0 && celt != 1) return COM.E_INVALIDARG;
		if (enumIndex == 0) {
			AccessibleControlEvent event = new AccessibleControlEvent(this);
			event.childID = ACC.CHILDID_SELF;
			for (int i = 0; i < accessibleControlListenersSize(); i++) {
				AccessibleControlListener listener = accessibleControlListeners.get(i);
				listener.getChildren(event);
			}
			variants = event.children;
		}
		Object[] nextItems = null;
		if (variants != null && celt >= 1) {
			int endIndex = enumIndex + celt - 1;
			if (endIndex > (variants.length - 1)) endIndex = variants.length - 1;
			if (enumIndex <= endIndex) {
				nextItems = new Object[endIndex - enumIndex + 1];
				for (int i = 0; i < nextItems.length; i++) {
					Object child = variants[enumIndex];
					if (child instanceof Integer) {
						nextItems[i] = Integer.valueOf(childIDToOs(((Integer)child).intValue()));
					} else {
						nextItems[i] = child;
					}
					enumIndex++;
				}
			}
		}
		if (nextItems != null) {
			for (int i = 0; i < nextItems.length; i++) {
				Object nextItem = nextItems[i];
				if (nextItem instanceof Integer) {
					int item = ((Integer) nextItem).intValue();
					setIntVARIANT(rgvar + i * VARIANT.sizeof, COM.VT_I4, item);
				} else {
					Accessible accessible = (Accessible) nextItem;
					accessible.AddRef();
					setPtrVARIANT(rgvar + i * VARIANT.sizeof, COM.VT_DISPATCH, accessible.getAddress());
				}
			}
			if (pceltFetched != 0)
				OS.MoveMemory(pceltFetched, new int[] {nextItems.length}, 4);
			if (nextItems.length == celt) return COM.S_OK;
		} else {
			if (pceltFetched != 0)
				OS.MoveMemory(pceltFetched, new int[] {0}, 4);
		}
		return COM.S_FALSE;
	}

	/* IEnumVARIANT::Skip([in] celt) over the specified number of elements in the enumeration sequence. */
	int Skip(int celt) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IEnumVARIANT::Skip");
		/* If there are no listeners, query the proxy
		 * for its IEnumVariant, and tell it to Skip.
		 */
		if (iaccessible != null && accessibleControlListenersSize() == 0) {
			long[] ppvObject = new long[1];
			int code = iaccessible.QueryInterface(COM.IIDIEnumVARIANT, ppvObject);
			if (code != COM.S_OK) return code;
			IEnumVARIANT ienumvariant = new IEnumVARIANT(ppvObject[0]);
			code = ienumvariant.Skip(celt);
			ienumvariant.Release();
			return code;
		}

		if (celt < 1 ) return COM.E_INVALIDARG;
		enumIndex += celt;
		if (enumIndex > (variants.length - 1)) {
			enumIndex = variants.length - 1;
			return COM.S_FALSE;
		}
		return COM.S_OK;
	}

	/* IEnumVARIANT::Reset() the enumeration sequence to the beginning. */
	int Reset() {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IEnumVARIANT::Reset");
		/* If there are no listeners, query the proxy
		 * for its IEnumVariant, and tell it to Reset.
		 */
		if (iaccessible != null && accessibleControlListenersSize() == 0) {
			long[] ppvObject = new long[1];
			int code = (int)iaccessible.QueryInterface(COM.IIDIEnumVARIANT, ppvObject);
			if (code != COM.S_OK) return code;
			IEnumVARIANT ienumvariant = new IEnumVARIANT(ppvObject[0]);
			code = ienumvariant.Reset();
			ienumvariant.Release();
			return code;
		}

		enumIndex = 0;
		return COM.S_OK;
	}

	/* IEnumVARIANT::Clone([out] ppEnum)
	 * Ownership of ppEnum transfers from callee to caller so reference count on ppEnum
	 * must be incremented before returning.  The caller is responsible for releasing ppEnum.
	 */
	int Clone(long ppEnum) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IEnumVARIANT::Clone");
		/* If there are no listeners, query the proxy for
		 * its IEnumVariant, and get the Clone from it.
		 */
		if (iaccessible != null && accessibleControlListenersSize() == 0) {
			long[] ppvObject = new long[1];
			int code = iaccessible.QueryInterface(COM.IIDIEnumVARIANT, ppvObject);
			if (code != COM.S_OK) return code;
			IEnumVARIANT ienumvariant = new IEnumVARIANT(ppvObject[0]);
			long [] pEnum = new long [1];
			code = ienumvariant.Clone(pEnum);
			ienumvariant.Release();
			OS.MoveMemory(ppEnum, pEnum, C.PTR_SIZEOF);
			return code;
		}

		if (ppEnum == 0) return COM.E_INVALIDARG;
		OS.MoveMemory(ppEnum, new long[] { objIEnumVARIANT.getAddress() }, C.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}

	/* IAccessible2::get_nRelations([out] pNRelations) */
	int get_nRelations(long pNRelations) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		int count = getRelationCount();
		if (DEBUG) print(this + ".IAccessible2::get_nRelations returning " + count + hresult(COM.S_OK));
		OS.MoveMemory(pNRelations, new int [] { count }, 4);
		return COM.S_OK;
	}

	/* IAccessible2::get_relation([in] relationIndex, [out] ppRelation) */
	int get_relation(int relationIndex, long ppRelation) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		int i = -1;
		for (int type = 0; type < MAX_RELATION_TYPES; type++) {
			Relation relation = relations[type];
			if (relation != null) i++;
			if (i == relationIndex) {
				if (DEBUG) print(this + ".IAccessible2::get_relation(" + relationIndex + ") returning " + relation.getAddress() + hresult(COM.S_OK));
				relation.AddRef();
				OS.MoveMemory(ppRelation, new long[] { relation.getAddress() }, C.PTR_SIZEOF);
				return COM.S_OK;
			}
		}
		if (DEBUG) print(this + ".IAccessible2::get_relation(" + relationIndex + ") returning" + hresult(COM.E_INVALIDARG));
		return COM.E_INVALIDARG;
	}

	/* IAccessible2::get_relations([in] maxRelations, [out] ppRelations, [out] pNRelations) */
	int get_relations(int maxRelations, long ppRelations, long pNRelations) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		int count = 0;
		for (int type = 0; type < MAX_RELATION_TYPES; type++) {
			if (count == maxRelations) break;
			Relation relation = relations[type];
			if (relation != null) {
				relation.AddRef();
				OS.MoveMemory(ppRelations + count * C.PTR_SIZEOF, new long[] { relation.getAddress() }, C.PTR_SIZEOF);
				count++;
			}
		}
		if (DEBUG) print(this + ".IAccessible2::get_relations(" + maxRelations + ") returning " + count + hresult(COM.S_OK));
		OS.MoveMemory(pNRelations, new int [] { count }, 4);
		return COM.S_OK;
	}

	/* IAccessible2::get_role([out] pRole) */
	int get_role(long pRole) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		int role = getRole();
		if (role == 0) role = getDefaultRole();
		if (DEBUG) print(this + ".IAccessible2::get_role() returning " + getRoleString(role) + hresult(COM.S_OK));
		OS.MoveMemory(pRole, new int [] { role }, 4);
		return COM.S_OK;
	}

	/* IAccessible2::scrollTo([in] scrollType) */
	int scrollTo(int scrollType) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessible2::scrollTo");
		if (scrollType < ACC.SCROLL_TYPE_LEFT_EDGE || scrollType > ACC.SCROLL_TYPE_ANYWHERE) return COM.E_INVALIDARG;
		return COM.E_NOTIMPL;
	}

	/* IAccessible2::scrollToPoint([in] coordinateType, [in] x, [in] y) */
	int scrollToPoint(int coordinateType, int x, int y) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessible2::scrollToPoint");
		if (coordinateType != COM.IA2_COORDTYPE_SCREEN_RELATIVE) return COM.E_INVALIDARG;
		return COM.E_NOTIMPL;
	}

	/* IAccessible2::get_groupPosition([out] pGroupLevel, [out] pSimilarItemsInGroup, [out] pPositionInGroup) */
	int get_groupPosition(long pGroupLevel, long pSimilarItemsInGroup, long pPositionInGroup) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleAttributeEvent event = new AccessibleAttributeEvent(this);
		event.groupLevel = event.groupCount = event.groupIndex = -1;
		for (int i = 0; i < accessibleAttributeListenersSize(); i++) {
			AccessibleAttributeListener listener = accessibleAttributeListeners.get(i);
			listener.getAttributes(event);
		}
		int groupLevel = (event.groupLevel != -1) ? event.groupLevel : 0;
		int similarItemsInGroup = (event.groupCount != -1) ? event.groupCount : 0;
		int positionInGroup = (event.groupIndex != -1) ? event.groupIndex : 0;
		if (similarItemsInGroup == 0 && positionInGroup == 0) {
			/* Determine position and count for radio buttons. */
			if (control instanceof Button && ((control.getStyle() & SWT.RADIO) != 0)) {
				positionInGroup = 1;
				similarItemsInGroup = 1;
				for (Control child : control.getParent().getChildren()) {
					if (child instanceof Button && ((child.getStyle() & SWT.RADIO) != 0)) {
						if (child == control) positionInGroup = similarItemsInGroup;
						else similarItemsInGroup++;
					}
				}
			}
		}
		OS.MoveMemory(pGroupLevel, new int [] { groupLevel }, 4);
		OS.MoveMemory(pSimilarItemsInGroup, new int [] { similarItemsInGroup }, 4);
		OS.MoveMemory(pPositionInGroup, new int [] { positionInGroup }, 4);
		if (DEBUG) print(this + ".IAccessible2::get_groupPosition() returning level=" + groupLevel + ", count=" + similarItemsInGroup + ", index=" + positionInGroup + hresult(groupLevel == 0 && similarItemsInGroup == 0 && positionInGroup == 0 ? COM.S_FALSE : COM.S_OK));
		if (groupLevel == 0 && similarItemsInGroup == 0 && positionInGroup == 0) return COM.S_FALSE;
		return COM.S_OK;
	}

	/* IAccessible2::get_states([out] pStates) */
	int get_states(long pStates) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = ACC.CHILDID_SELF;
		for (int i = 0; i < accessibleControlListenersSize(); i++) {
			AccessibleControlListener listener = accessibleControlListeners.get(i);
			listener.getState(event);
		}
		int states = event.detail;
		int ia2States = 0;
		if ((states & ACC.STATE_ACTIVE) != 0) ia2States |= COM.IA2_STATE_ACTIVE;
		if ((states & ACC.STATE_SINGLELINE) != 0) ia2States |= COM.IA2_STATE_SINGLE_LINE;
		if ((states & ACC.STATE_MULTILINE) != 0) ia2States |= COM.IA2_STATE_MULTI_LINE;
		if ((states & ACC.STATE_REQUIRED) != 0) ia2States |= COM.IA2_STATE_REQUIRED;
		if ((states & ACC.STATE_INVALID_ENTRY) != 0) ia2States |= COM.IA2_STATE_INVALID_ENTRY;
		if ((states & ACC.STATE_SUPPORTS_AUTOCOMPLETION) != 0) ia2States |= COM.IA2_STATE_SUPPORTS_AUTOCOMPLETION;

		/* If the role is text and there are TextExtendedListeners, then set IA2_STATE_EDITABLE.
		 * Note that IA2_STATE_EDITABLE is not the opposite of STATE_READONLY.
		 * Instead, it means: "has a caret, supports IAccessibleText, and is a text editing environment".
		 */
		if (getRole() == ACC.ROLE_TEXT && accessibleTextExtendedListenersSize() > 0) {
			ia2States |= COM.IA2_STATE_EDITABLE;
		}
		if (DEBUG) print(this + ".IAccessible2::get_states returning" + getIA2StatesString(ia2States) + hresult(COM.S_OK));
		OS.MoveMemory(pStates, new int [] { ia2States }, 4);
		return COM.S_OK;
	}

	/* IAccessible2::get_extendedRole([out] pbstrExtendedRole) */
	int get_extendedRole(long pbstrExtendedRole) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		/* This feature is not supported. */
		setString(pbstrExtendedRole, null);
		return COM.S_FALSE;
	}

	/* IAccessible2::get_localizedExtendedRole([out] pbstrLocalizedExtendedRole) */
	int get_localizedExtendedRole(long pbstrLocalizedExtendedRole) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		/* This feature is not supported. */
		setString(pbstrLocalizedExtendedRole, null);
		return COM.S_FALSE;
	}

	/* IAccessible2::get_nExtendedStates([out] pNExtendedStates) */
	int get_nExtendedStates(long pNExtendedStates) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		/* This feature is not supported. */
		OS.MoveMemory(pNExtendedStates, new int [] { 0 }, 4);
		return COM.S_OK;
	}

	/* IAccessible2::get_extendedStates([in] maxExtendedStates, [out] ppbstrExtendedStates, [out] pNExtendedStates) */
	int get_extendedStates(int maxExtendedStates, long ppbstrExtendedStates, long pNExtendedStates) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		/* This feature is not supported. */
		setString(ppbstrExtendedStates, null);
		OS.MoveMemory(pNExtendedStates, new int [] { 0 }, 4);
		return COM.S_FALSE;
	}

	/* IAccessible2::get_localizedExtendedStates([in] maxLocalizedExtendedStates, [out] ppbstrLocalizedExtendedStates, [out] pNLocalizedExtendedStates) */
	int get_localizedExtendedStates(int maxLocalizedExtendedStates, long ppbstrLocalizedExtendedStates, long pNLocalizedExtendedStates) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		/* This feature is not supported. */
		setString(ppbstrLocalizedExtendedStates, null);
		OS.MoveMemory(pNLocalizedExtendedStates, new int [] { 0 }, 4);
		return COM.S_FALSE;
	}

	/* IAccessible2::get_uniqueID([out] pUniqueID) */
	int get_uniqueID(long pUniqueID) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (uniqueID == -1) uniqueID = UniqueID--;
		if (DEBUG) print(this + ".IAccessible2::get_uniqueID returning " + uniqueID + hresult(COM.S_OK));
		OS.MoveMemory(pUniqueID, new long [] { uniqueID }, 4);
		return COM.S_OK;
	}

	/* IAccessible2::get_windowHandle([out] pWindowHandle) */
	int get_windowHandle(long pWindowHandle) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessible2::get_windowHandle returning " + control.handle + hresult(COM.S_OK));
		OS.MoveMemory(pWindowHandle, new long [] { control.handle }, C.PTR_SIZEOF);
		return COM.S_OK;
	}

	/* IAccessible2::get_indexInParent([out] pIndexInParent) */
	int get_indexInParent(long pIndexInParent) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = ACC.CHILDID_CHILD_INDEX;
		event.detail = -1;
		for (int i = 0; i < accessibleControlListenersSize(); i++) {
			AccessibleControlListener listener = accessibleControlListeners.get(i);
			listener.getChild(event);
		}
		int indexInParent = event.detail;
		if (indexInParent == -1) {
//			/* The application did not implement CHILDID_CHILD_INDEX,
//			 * so determine the index by looping through the parent's
//			 * children looking for this Accessible. This may be slow,
//			 * so applications are strongly encouraged to implement
//			 * getChild for CHILDID_CHILD_INDEX.
//			 */
//			// TODO: finish this. See also get_groupPosition
			// this won't work because VARIANT.sizeof isn't big enough on 64-bit machines.
			// just create an  long [] ppdispParent - it's not a variant anyhow...
//			long ppdispParent = OS.GlobalAlloc (OS.GMEM_FIXED | OS.GMEM_ZEROINIT, VARIANT.sizeof);
//			int code = get_accParent(ppdispParent);
//			if (code == COM.S_OK) {
//				VARIANT v = getVARIANT(ppdispParent);
//				if (v.vt == COM.VT_DISPATCH) {
//					IAccessible accParent = new IAccessible(v.lVal);
//					long pcountChildren = OS.GlobalAlloc (OS.GMEM_FIXED | OS.GMEM_ZEROINIT, 4);
//					code = accParent.get_accChildCount(pcountChildren);
//					if (code == COM.S_OK) {
//						int [] childCount = new int[1];
//						OS.MoveMemory(childCount, pcountChildren, 4);
//						int[] pcObtained = new int[1];
//						long rgVarChildren = OS.GlobalAlloc (OS.GMEM_FIXED | OS.GMEM_ZEROINIT, VARIANT.sizeof * childCount[0]);
//						System.out.println("Asking for AccessibleChildren");
//						code = COM.AccessibleChildren(accParent.getAddress(), 0, childCount[0], rgVarChildren, pcObtained);
//						if (code == COM.S_OK) {
//							System.out.println("Got this far - now what?");
//						} else {
//							System.out.println("AccessibleChildren failed? code=" + code);
//						}
//						OS.GlobalFree(rgVarChildren);
//					} else {
//						System.out.println("get_accChildCount failed? code=" + code);
//					}
//					OS.GlobalFree (pcountChildren);
//				} else {
//					System.out.println("get_accParent did not return VT_DISPATCH? It returned: " + v.vt);
//				}
//				COM.VariantClear(ppdispParent);
//				OS.GlobalFree (ppdispParent);
//			} else {
//				System.out.println("get_accParent failed? code=" + code);
//			}
		}

		if (DEBUG) print(this + ".IAccessible2::get_indexInParent returning " + indexInParent + hresult(indexInParent == -1 ? COM.S_FALSE : COM.S_OK));
		OS.MoveMemory(pIndexInParent, new int [] { indexInParent }, 4);
		return indexInParent == -1 ? COM.S_FALSE : COM.S_OK;
	}

	/* IAccessible2::get_locale([out] pLocale) */
	int get_locale(long pLocale) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		/* Return the default locale for the JVM. */
		Locale locale = Locale.getDefault();

		char[] data = (locale.getLanguage()+"\0").toCharArray();
		long ptr = COM.SysAllocString(data);
		OS.MoveMemory(pLocale, new long[] {ptr}, C.PTR_SIZEOF);

		data = (locale.getCountry()+"\0").toCharArray();
		ptr = COM.SysAllocString(data);
		OS.MoveMemory(pLocale + C.PTR_SIZEOF, new long[] {ptr}, C.PTR_SIZEOF);

		data = (locale.getVariant()+"\0").toCharArray();
		ptr = COM.SysAllocString(data);
		OS.MoveMemory(pLocale + 2 * C.PTR_SIZEOF, new long[] {ptr}, C.PTR_SIZEOF);

		if (DEBUG) print(this + ".IAccessible2::get_locale() returning" + hresult(COM.S_OK));
		return COM.S_OK;
	}

	/* IAccessible2::get_attributes([out] pbstrAttributes) */
	int get_attributes(long pbstrAttributes) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleAttributeEvent event = new AccessibleAttributeEvent(this);
		for (int i = 0; i < accessibleAttributeListenersSize(); i++) {
			AccessibleAttributeListener listener = accessibleAttributeListeners.get(i);
			listener.getAttributes(event);
		}
		String attributes = "";
		attributes += "margin-left:" + event.leftMargin + ";";
		attributes += "margin-top:" + event.topMargin + ";";
		attributes += "margin-right:" + event.rightMargin + ";";
		attributes += "margin-bottom:" + event.bottomMargin + ";";
		if (event.tabStops != null) {
			for (int tabStop : event.tabStops) {
				attributes += "tab-stop:position=" + tabStop + ";";
			}
		}
		if (event.justify) attributes += "text-align:justify;";
		attributes += "text-align:" + (event.alignment == SWT.LEFT ? "left" : event.alignment == SWT.RIGHT ? "right" : "center") + ";";
		attributes += "text-indent:" + event.indent + ";";
		if (event.attributes != null) {
			for (int i = 0; i + 1 < event.attributes.length; i += 2) {
				attributes += event.attributes[i] + ":" + event.attributes[i+1] + ";";
			}
		}

		/* If the role is text, then specify the text model for JAWS. */
		if (getRole() == ACC.ROLE_TEXT) {
			attributes += "text-model:a1;";
		}
		if (DEBUG) print(this + ".IAccessible2::get_attributes() returning " + attributes + hresult(attributes.length() == 0 ? COM.S_FALSE : COM.S_OK));
		setString(pbstrAttributes, attributes);
		if (attributes.length() == 0) return COM.S_FALSE;
		return COM.S_OK;
	}

	/* IAccessibleAction::get_nActions([out] pNActions) */
	int get_nActions(long pNActions) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleActionEvent event = new AccessibleActionEvent(this);
		for (int i = 0; i < accessibleActionListenersSize(); i++) {
			AccessibleActionListener listener = accessibleActionListeners.get(i);
			listener.getActionCount(event);
		}
		if (DEBUG) print(this + ".IAccessibleAction::get_nActions() returning " + event.count + hresult(COM.S_OK));
		OS.MoveMemory(pNActions, new int [] { event.count }, 4);
		return COM.S_OK;
	}

	/* IAccessibleAction::doAction([in] actionIndex) */
	int doAction(int actionIndex) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleActionEvent event = new AccessibleActionEvent(this);
		event.index = actionIndex;
		for (int i = 0; i < accessibleActionListenersSize(); i++) {
			AccessibleActionListener listener = accessibleActionListeners.get(i);
			listener.doAction(event);
		}
		if (DEBUG) print(this + ".IAccessibleAction::doAction(" + actionIndex + ") returning" + hresult(event.result == null || !event.result.equals(ACC.OK) ? COM.E_INVALIDARG : COM.S_OK));
		if (event.result == null || !event.result.equals(ACC.OK)) return COM.E_INVALIDARG;
		return COM.S_OK;
	}

	/* IAccessibleAction::get_description([in] actionIndex, [out] pbstrDescription) */
	int get_description(int actionIndex, long pbstrDescription) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleActionEvent event = new AccessibleActionEvent(this);
		event.index = actionIndex;
		for (int i = 0; i < accessibleActionListenersSize(); i++) {
			AccessibleActionListener listener = accessibleActionListeners.get(i);
			listener.getDescription(event);
		}
		if (DEBUG) print(this + ".IAccessibleAction::get_description(" + actionIndex + ") returning " + event.result + hresult(event.result == null || event.result.length() == 0 ? COM.S_FALSE : COM.S_OK));
		setString(pbstrDescription, event.result);
		if (event.result == null || event.result.length() == 0) return COM.S_FALSE;
		return COM.S_OK;
	}

	/* IAccessibleAction::get_keyBinding([in] actionIndex, [in] nMaxBindings, [out] ppbstrKeyBindings, [out] pNBindings) */
	int get_keyBinding(int actionIndex, int nMaxBindings, long ppbstrKeyBindings, long pNBindings) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleActionEvent event = new AccessibleActionEvent(this);
		event.index = actionIndex;
		for (int i = 0; i < accessibleActionListenersSize(); i++) {
			AccessibleActionListener listener = accessibleActionListeners.get(i);
			listener.getKeyBinding(event);
		}
		String keyBindings = event.result;
		int length = 0;
		if (keyBindings != null) length = keyBindings.length();
		int i = 0, count = 0;
		while (i < length) {
			if (count == nMaxBindings) break;
			int j = keyBindings.indexOf(';', i);
			if (j == -1) j = length;
			String keyBinding = keyBindings.substring(i, j);
			if (keyBinding.length() > 0) {
				setString(ppbstrKeyBindings + count * C.PTR_SIZEOF, keyBinding);
				count++;
			}
			i = j + 1;
		}
		if (DEBUG) print(this + ".IAccessibleAction::get_keyBinding(index=" + actionIndex + " max=" + nMaxBindings + ") returning count=" + count + hresult(count == 0 ? COM.S_FALSE : COM.S_OK));
		OS.MoveMemory(pNBindings, new int [] { count }, 4);
		if (count == 0) {
			setString(ppbstrKeyBindings, null);
			return COM.S_FALSE;
		}
		return COM.S_OK;
	}

	/* IAccessibleAction::get_name([in] actionIndex, [out] pbstrName) */
	int get_name(int actionIndex, long pbstrName) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleActionEvent event = new AccessibleActionEvent(this);
		event.index = actionIndex;
		event.localized = false;
		for (int i = 0; i < accessibleActionListenersSize(); i++) {
			AccessibleActionListener listener = accessibleActionListeners.get(i);
			listener.getName(event);
		}
		if (DEBUG) print(this + ".IAccessibleAction::get_name(" + actionIndex + ") returning " + event.result + hresult(event.result == null || event.result.length() == 0 ? COM.S_FALSE : COM.S_OK));
		if (event.result == null || event.result.length() == 0) {
			setString(pbstrName, null);
			return COM.S_FALSE;
		}
		setString(pbstrName, event.result);
		return COM.S_OK;
	}

	/* IAccessibleAction::get_localizedName([in] actionIndex, [out] pbstrLocalizedName) */
	int get_localizedName(int actionIndex, long pbstrLocalizedName) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleActionEvent event = new AccessibleActionEvent(this);
		event.index = actionIndex;
		event.localized = true;
		for (int i = 0; i < accessibleActionListenersSize(); i++) {
			AccessibleActionListener listener = accessibleActionListeners.get(i);
			listener.getName(event);
		}
		if (DEBUG) print(this + ".IAccessibleAction::get_localizedName(" + actionIndex + ") returning " + event.result + hresult(event.result == null || event.result.length() == 0 ? COM.S_FALSE : COM.S_OK));
		if (event.result == null || event.result.length() == 0) {
			setString(pbstrLocalizedName, null);
			return COM.S_FALSE;
		}
		setString(pbstrLocalizedName, event.result);
		return COM.S_OK;
	}

	/* IAccessibleApplication::get_appName([out] pbstrName) */
	int get_appName(long pbstrName) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		String appName = Display.getAppName();
		if (DEBUG) print(this + ".IAccessibleApplication::get_appName() returning " + appName + hresult(appName == null || appName.length() == 0 ? COM.S_FALSE : COM.S_OK));
		if (appName == null || appName.length() == 0) {
			setString(pbstrName, null);
			return COM.S_FALSE;
		}
		setString(pbstrName, appName);
		return COM.S_OK;
	}

	/* IAccessibleApplication::get_appVersion([out] pbstrVersion) */
	int get_appVersion(long pbstrVersion) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		String appVersion = Display.getAppVersion();
		if (DEBUG) print(this + ".IAccessibleApplication::get_appVersion() returning" + appVersion + hresult(appVersion == null || appVersion.length() == 0 ? COM.S_FALSE : COM.S_OK));
		if (appVersion == null || appVersion.length() == 0) {
			setString(pbstrVersion, null);
			return COM.S_FALSE;
		}
		setString(pbstrVersion, appVersion);
		return COM.S_OK;
	}

	/* IAccessibleApplication::get_toolkitName([out] pbstrName) */
	int get_toolkitName(long pbstrName) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		String toolkitName = "SWT";
		if (DEBUG) print(this + ".IAccessibleApplication::get_toolkitName() returning" + toolkitName + hresult(COM.S_OK));
		setString(pbstrName, toolkitName);
		return COM.S_OK;
	}

	/* IAccessibleApplication::get_toolkitVersion([out] pbstrVersion) */
	int get_toolkitVersion(long pbstrVersion) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		String toolkitVersion = "" + SWT.getVersion(); //$NON-NLS-1$
		if (DEBUG) print(this + ".IAccessibleApplication::get_toolkitVersion() returning" + toolkitVersion + hresult(COM.S_OK));
		setString(pbstrVersion, toolkitVersion);
		return COM.S_OK;
	}

	// The following 3 method are intentionally commented. We are not providing IAccessibleComponent at this time.
//	/* IAccessibleComponent::get_locationInParent([out] pX, [out] pY) */
//	int get_locationInParent(long pX, long pY) {
//		if (DEBUG) print(this + ".IAccessibleComponent::get_locationInParent");
//		// TO DO: support transparently (hard for lightweight parents - screen vs. parent coords)
//		AccessibleControlEvent event = new AccessibleControlEvent(this);
//		for (int i = 0; i < accessibleControlListenersSize(); i++) {
//			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.get(i);
//			listener.getLocation (event);
//		}
//		COM.MoveMemory(pX, new int [] { event.x }, 4);
//		COM.MoveMemory(pY, new int [] { event.y }, 4);
//		return COM.S_OK;
//	}
//
//	/* IAccessibleComponent::get_foreground([out] pForeground) */
//	int get_foreground(long pForeground) {
//		Color color = control.getForeground();
//		if (DEBUG) print(this + ".IAccessibleComponent::get_foreground returning " + color.handle);
//		COM.MoveMemory(pForeground, new int [] { color.handle }, 4);
//		return COM.S_OK;
//	}
//
//	/* IAccessibleComponent::get_background([out] pBackground) */
//	int get_background(long pBackground) {
//		Color color = control.getBackground();
//		if (DEBUG) print(this + ".IAccessibleComponent::get_background returning " + color.handle);
//		COM.MoveMemory(pBackground, new int [] { color.handle }, 4);
//		return COM.S_OK;
//	}

	/* IAccessibleEditableText::copyText([in] startOffset, [in] endOffset) */
	int copyText(int startOffset, int endOffset) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleEditableText::copyText, start=" + startOffset + ", end=" + endOffset);
		AccessibleEditableTextEvent event = new AccessibleEditableTextEvent(this);
		event.start = startOffset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : startOffset;
		event.end = endOffset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : endOffset;
		for (int i = 0; i < accessibleEditableTextListenersSize(); i++) {
			AccessibleEditableTextListener listener = accessibleEditableTextListeners.get(i);
			listener.copyText(event);
		}
		if (event.result == null || !event.result.equals(ACC.OK)) return COM.E_INVALIDARG;
		return COM.S_OK;
	}

	/* IAccessibleEditableText::deleteText([in] startOffset, [in] endOffset) */
	int deleteText(int startOffset, int endOffset) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleEditableText::deleteText, start=" + startOffset + ", end=" + endOffset);
		AccessibleEditableTextEvent event = new AccessibleEditableTextEvent(this);
		event.start = startOffset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : startOffset;
		event.end = endOffset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : endOffset;
		event.string = "";
		for (int i = 0; i < accessibleEditableTextListenersSize(); i++) {
			AccessibleEditableTextListener listener = accessibleEditableTextListeners.get(i);
			listener.replaceText(event);
		}
		if (event.result == null || !event.result.equals(ACC.OK)) return COM.E_INVALIDARG;
		return COM.S_OK;
	}

	/* IAccessibleEditableText::insertText([in] offset, [in] pbstrText) */
	int insertText(int offset, long pbstrText) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleEditableText::insertText, offset=" + offset + ", pbstrText=" + pbstrText);
		AccessibleEditableTextEvent event = new AccessibleEditableTextEvent(this);
		event.start = offset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : offset;
		event.end = event.start;
		event.string = getString(pbstrText);
		for (int i = 0; i < accessibleEditableTextListenersSize(); i++) {
			AccessibleEditableTextListener listener = accessibleEditableTextListeners.get(i);
			listener.replaceText(event);
		}
		if (event.result == null || !event.result.equals(ACC.OK)) return COM.E_INVALIDARG;
		return COM.S_OK;
	}

	/* IAccessibleEditableText::cutText([in] startOffset, [in] endOffset) */
	int cutText(int startOffset, int endOffset) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleEditableText::cutText, start=" + startOffset + ", end=" + endOffset);
		AccessibleEditableTextEvent event = new AccessibleEditableTextEvent(this);
		event.start = startOffset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : startOffset;
		event.end = endOffset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : endOffset;
		for (int i = 0; i < accessibleEditableTextListenersSize(); i++) {
			AccessibleEditableTextListener listener = accessibleEditableTextListeners.get(i);
			listener.cutText(event);
		}
		if (event.result == null || !event.result.equals(ACC.OK)) return COM.E_INVALIDARG;
		return COM.S_OK;
	}

	/* IAccessibleEditableText::pasteText([in] offset) */
	int pasteText(int offset) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleEditableText::pasteText, offset=" + offset);
		AccessibleEditableTextEvent event = new AccessibleEditableTextEvent(this);
		event.start = offset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : offset;
		event.end = event.start;
		for (int i = 0; i < accessibleEditableTextListenersSize(); i++) {
			AccessibleEditableTextListener listener = accessibleEditableTextListeners.get(i);
			listener.pasteText(event);
		}
		if (event.result == null || !event.result.equals(ACC.OK)) return COM.E_INVALIDARG;
		return COM.S_OK;
	}

	/* IAccessibleEditableText::replaceText([in] startOffset, [in] endOffset, [in] pbstrText) */
	int replaceText(int startOffset, int endOffset, long pbstrText) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleEditableText::replaceText, start=" + startOffset + ", end=" + endOffset + ", pbstrText=" + pbstrText);
		AccessibleEditableTextEvent event = new AccessibleEditableTextEvent(this);
		event.start = startOffset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : startOffset;
		event.end = endOffset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : endOffset;
		event.string = getString(pbstrText);
		for (int i = 0; i < accessibleEditableTextListenersSize(); i++) {
			AccessibleEditableTextListener listener = accessibleEditableTextListeners.get(i);
			listener.replaceText(event);
		}
		if (event.result == null || !event.result.equals(ACC.OK)) return COM.E_INVALIDARG;
		return COM.S_OK;
	}

	/* IAccessibleEditableText::setAttributes([in] startOffset, [in] endOffset, [in] pbstrAttributes) */
	int setAttributes(int startOffset, int endOffset, long pbstrAttributes) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleEditableText::setAttributes, start=" + startOffset + ", end=" + endOffset + ", pbstrAttributes=" + pbstrAttributes);
		AccessibleTextAttributeEvent event = new AccessibleTextAttributeEvent(this);
		String string = getString(pbstrAttributes);
		if (string != null && string.length() > 0) {
			event.start = startOffset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : startOffset;
			event.end = endOffset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : endOffset;
			TextStyle style = new TextStyle();
			FontData fontData = null;
			int points = 10; // used for default rise
			String [] attributes = new String [0];
			int begin = 0;
			int end = string.indexOf(';');
			while (end != -1 && end < string.length()) {
				String keyValue = string.substring(begin, end).trim();
				int colonIndex = keyValue.indexOf(':');
				if (colonIndex != -1 && colonIndex + 1 < keyValue.length()) {
					String [] newAttributes = new String [attributes.length + 2];
					System.arraycopy (attributes, 0, newAttributes, 0, attributes.length);
					newAttributes[attributes.length] = keyValue.substring(0, colonIndex).trim();
					newAttributes[attributes.length + 1] = keyValue.substring(colonIndex + 1).trim();
					attributes = newAttributes;
				}
				begin = end + 1;
				end = string.indexOf(';', begin);
			}
			for (int i = 0; i+1 < attributes.length; i+=2) {
				String key = attributes[i];
				String value = attributes[i+1];
				if (key.equals("text-position")) {
					if (value.equals("super")) style.rise = points / 2;
					else if (value.equals("sub")) style.rise = - points / 2;
				} else if (key.equals("text-underline-type")) {
					style.underline = true;
					if (value.equals("double")) style.underlineStyle = SWT.UNDERLINE_DOUBLE;
					else if (value.equals("single")) {
						if (style.underlineStyle != SWT.UNDERLINE_SQUIGGLE && style.underlineStyle != SWT.UNDERLINE_ERROR) {
							style.underlineStyle = SWT.UNDERLINE_SINGLE;
						}
					}
				} else if (key.equals("text-underline-style") && value.equals("wave")) {
					style.underline = true;
					style.underlineStyle = SWT.UNDERLINE_SQUIGGLE;
				} else if (key.equals("invalid") && value.equals("true")) {
					style.underline = true;
					style.underlineStyle = SWT.UNDERLINE_ERROR;
				} else if (key.equals("text-line-through-type")) {
					if (value.equals("single")) style.strikeout = true;
				} else if (key.equals("font-family")) {
					if (fontData == null) fontData = new FontData ();
					fontData.setName(value);
				} else if (key.equals("font-size")) {
					try {
						String pts = value.endsWith("pt") ? value.substring(0, value.length() - 2) : value;
						points = Integer.parseInt(pts);
						if (fontData == null) fontData = new FontData ();
						fontData.setHeight(points);
						if (style.rise > 0) style.rise = points / 2;
						else if (style.rise < 0) style.rise = - points / 2;
					} catch (NumberFormatException ex) {}
				} else if (key.equals("font-style")) {
					if (value.equals("italic")) {
						if (fontData == null) fontData = new FontData ();
						fontData.setStyle(fontData.getStyle() | SWT.ITALIC);
					}
				} else if (key.equals("font-weight")) {
					if (value.equals("bold")) {
						if (fontData == null) fontData = new FontData ();
						fontData.setStyle(fontData.getStyle() | SWT.BOLD);
					} else {
						try {
							int weight = Integer.parseInt(value);
							if (fontData == null) fontData = new FontData ();
							if (weight > 400) fontData.setStyle(fontData.getStyle() | SWT.BOLD);
						} catch (NumberFormatException ex) {}
					}
				} else if (key.equals("color")) {
					style.foreground = colorFromString(value);
				} else if (key.equals("background-color")) {
					style.background = colorFromString(value);
				}
			}
			if (attributes.length > 0) {
				event.attributes = attributes;
				if (fontData != null) {
					style.font = new Font(control.getDisplay(), fontData);
				}
				if (!style.equals(new TextStyle())) event.textStyle = style;
			}
			for (int i = 0; i < accessibleEditableTextListenersSize(); i++) {
				AccessibleEditableTextListener listener = accessibleEditableTextListeners.get(i);
				listener.setTextAttributes(event);
			}
			if (style.font != null) {
				style.font.dispose();
			}
			if (style.foreground != null) {
				style.foreground.dispose();
			}
			if (style.background != null) {
				style.background.dispose();
			}
		}
		if (event.result == null || !event.result.equals(ACC.OK)) return COM.E_INVALIDARG;
		return COM.S_OK;
	}

	/* IAccessibleHyperlink::get_anchor([in] index, [out] pAnchor) */
	int get_anchor(int index, long pAnchor) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleHyperlink::get_anchor");
		AccessibleHyperlinkEvent event = new AccessibleHyperlinkEvent(this);
		event.index = index;
		for (int i = 0; i < accessibleHyperlinkListenersSize(); i++) {
			AccessibleHyperlinkListener listener = accessibleHyperlinkListeners.get(i);
			listener.getAnchor(event);
		}
		Accessible accessible = event.accessible;
		if (accessible != null) {
			accessible.AddRef();
			setPtrVARIANT(pAnchor, COM.VT_DISPATCH, accessible.getAddress());
			return COM.S_OK;
		}
		setStringVARIANT(pAnchor, event.result);
		if (event.result == null) return COM.S_FALSE;
		return COM.S_OK;
	}

	/* IAccessibleHyperlink::get_anchorTarget([in] index, [out] pAnchorTarget) */
	int get_anchorTarget(int index, long pAnchorTarget) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleHyperlink::get_anchorTarget");
		AccessibleHyperlinkEvent event = new AccessibleHyperlinkEvent(this);
		event.index = index;
		for (int i = 0; i < accessibleHyperlinkListenersSize(); i++) {
			AccessibleHyperlinkListener listener = accessibleHyperlinkListeners.get(i);
			listener.getAnchorTarget(event);
		}
		Accessible accessible = event.accessible;
		if (accessible != null) {
			accessible.AddRef();
			setPtrVARIANT(pAnchorTarget, COM.VT_DISPATCH, accessible.getAddress());
			return COM.S_OK;
		}
		setStringVARIANT(pAnchorTarget, event.result);
		if (event.result == null) return COM.S_FALSE;
		return COM.S_OK;
	}

	/* IAccessibleHyperlink::get_startIndex([out] pIndex) */
	int get_startIndex(long pIndex) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleHyperlink::get_startIndex");
		AccessibleHyperlinkEvent event = new AccessibleHyperlinkEvent(this);
		for (int i = 0; i < accessibleHyperlinkListenersSize(); i++) {
			AccessibleHyperlinkListener listener = accessibleHyperlinkListeners.get(i);
			listener.getStartIndex(event);
		}
		OS.MoveMemory(pIndex, new int [] { event.index }, 4);
		return COM.S_OK;
	}

	/* IAccessibleHyperlink::get_endIndex([out] pIndex) */
	int get_endIndex(long pIndex) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleHyperlink::get_endIndex");
		AccessibleHyperlinkEvent event = new AccessibleHyperlinkEvent(this);
		for (int i = 0; i < accessibleHyperlinkListenersSize(); i++) {
			AccessibleHyperlinkListener listener = accessibleHyperlinkListeners.get(i);
			listener.getEndIndex(event);
		}
		OS.MoveMemory(pIndex, new int [] { event.index }, 4);
		return COM.S_OK;
	}

	/* IAccessibleHyperlink::get_valid([out] pValid) */
	int get_valid(long pValid) {
		/* Deprecated. */
		return COM.E_NOTIMPL;
	}

	/* IAccessibleHypertext::get_nHyperlinks([out] pHyperlinkCount) */
	int get_nHyperlinks(long pHyperlinkCount) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleHypertext::get_nHyperlinks");
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.getHyperlinkCount(event);
		}
		OS.MoveMemory(pHyperlinkCount, new int [] { event.count }, 4);
		return COM.S_OK;
	}

	/* IAccessibleHypertext::get_hyperlink([in] index, [out] ppHyperlink) */
	int get_hyperlink(int index, long ppHyperlink) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleHypertext::get_hyperlink");
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.index = index;
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.getHyperlink(event);
		}
		Accessible accessible = event.accessible;
		if (accessible == null) {
			setIntVARIANT(ppHyperlink, COM.VT_EMPTY, 0);
			return COM.E_INVALIDARG;
		}
		accessible.AddRef();
		OS.MoveMemory(ppHyperlink, new long[] { accessible.getAddress() }, C.PTR_SIZEOF);
		return COM.S_OK;
	}

	/* IAccessibleHypertext::get_hyperlinkIndex([in] charIndex, [out] pHyperlinkIndex) */
	int get_hyperlinkIndex(int charIndex, long pHyperlinkIndex) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleHypertext::get_hyperlinkIndex");
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.offset = charIndex;
		event.index = -1;
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.getHyperlinkIndex(event);
		}
		OS.MoveMemory(pHyperlinkIndex, new int [] { event.index }, 4);
		if (event.index == -1) return COM.S_FALSE;
		return COM.S_OK;
	}

	// The following 3 method are intentionally commented. We are not providing IAccessibleImage at this time.
//	/* IAccessibleImage::get_description([out] pbstrDescription) */
//	int get_description(long pbstrDescription) {
//		if (DEBUG) print(this + ".IAccessibleImage::get_description");
//		// TO DO: Does it make sense to just reuse description?
//		AccessibleEvent event = new AccessibleEvent(this);
//		event.childID = ACC.CHILDID_SELF;
//		for (int i = 0; i < accessibleListenersSize(); i++) {
//			AccessibleListener listener = (AccessibleListener) accessibleListeners.get(i);
//			listener.getDescription(event);
//		}
//		setString(pbstrDescription, event.result);
//		if (event.result == null) return COM.S_FALSE;
//		return COM.S_OK;
//	}
//
//	/* IAccessibleImage::get_imagePosition([in] coordinateType, [out] pX, [out] pY) */
//	int get_imagePosition(int coordinateType, long pX, long pY) {
//		if (DEBUG) print(this + ".IAccessibleImage::get_imagePosition");
//		// TO DO: does it make sense to just reuse getLocation?
//		AccessibleControlEvent event = new AccessibleControlEvent(this);
//		event.childID = ACC.CHILDID_SELF;
//		for (int i = 0; i < accessibleControlListenersSize(); i++) {
//			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.get(i);
//			listener.getLocation(event);
//		}
//		COM.MoveMemory(pX, new int [] { event.x }, 4);
//		COM.MoveMemory(pY, new int [] { event.y }, 4);
//		return COM.S_OK;
//	}
//
//	/* IAccessibleImage::get_imageSize([out] pHeight, [out] pWidth) */
//	int get_imageSize(long pHeight, long pWidth) {
//		if (DEBUG) print(this + ".IAccessibleImage::get_imageSize");
//		// TO DO: does it make sense to just reuse getLocation?
//		AccessibleControlEvent event = new AccessibleControlEvent(this);
//		for (int i = 0; i < accessibleControlListenersSize(); i++) {
//			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.get(i);
//			listener.getLocation(event);
//		}
//		COM.MoveMemory(pHeight, new int [] { event.height }, 4);
//		COM.MoveMemory(pWidth, new int [] { event.width }, 4);
//		return COM.S_OK;
//	}

	/* IAccessibleTable2::get_cellAt([in] row, [in] column, [out] ppCell) */
	int get_cellAt(int row, int column, long ppCell) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		event.row = row;
		event.column = column;
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.getCell(event);
		}
		Accessible accessible = event.accessible;
		if (DEBUG) print(this + ".IAccessibleTable2::get_cellAt(row=" + row + ", column=" + column + ") returning " + accessible);
		if (accessible == null) return COM.E_INVALIDARG;
		accessible.AddRef();
		OS.MoveMemory(ppCell, new long[] { accessible.getAddress() }, C.PTR_SIZEOF);
		return COM.S_OK;
	}

	/* IAccessibleTable2::get_caption([out] ppAccessible) */
	int get_caption(long ppAccessible) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.getCaption(event);
		}
		Accessible accessible = event.accessible;
		if (DEBUG) print(this + ".IAccessibleTable2::get_caption() returning " + accessible);
		if (accessible == null) {
			OS.MoveMemory(ppAccessible, new long[] { 0 }, C.PTR_SIZEOF);
			return COM.S_FALSE;
		}
		accessible.AddRef();
		OS.MoveMemory(ppAccessible, new long[] { accessible.getAddress() }, C.PTR_SIZEOF);
		return COM.S_OK;
	}

	/* IAccessibleTable2::get_columnDescription([in] column, [out] pbstrDescription) */
	int get_columnDescription(int column, long pbstrDescription) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		event.column = column;
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.getColumnDescription(event);
		}
		if (DEBUG) print(this + ".IAccessibleTable2::get_columnDescription(column=" + column + ") returning " + event.result);
		setString(pbstrDescription, event.result);
		if (event.result == null) return COM.S_FALSE;
		return COM.S_OK;
	}

	/* IAccessibleTable2::get_nColumns([out] pColumnCount) */
	int get_nColumns(long pColumnCount) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.getColumnCount(event);
		}
		if (DEBUG) print(this + ".IAccessibleTable2::get_nColumns() returning " + event.count);
		OS.MoveMemory(pColumnCount, new int [] { event.count }, 4);
		return COM.S_OK;
	}

	/* IAccessibleTable2::get_nRows([out] pRowCount) */
	int get_nRows(long pRowCount) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.getRowCount(event);
		}
		if (DEBUG) print(this + ".IAccessibleTable2::get_nRows() returning " + event.count);
		OS.MoveMemory(pRowCount, new int [] { event.count }, 4);
		return COM.S_OK;
	}

	/* IAccessibleTable2::get_nSelectedCells([out] pCellCount) */
	int get_nSelectedCells(long pCellCount) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.getSelectedCellCount(event);
		}
		if (DEBUG) print(this + ".IAccessibleTable2::get_nSelectedCells() returning " + event.count);
		OS.MoveMemory(pCellCount, new int [] { event.count }, 4);
		return COM.S_OK;
	}

	/* IAccessibleTable2::get_nSelectedColumns([out] pColumnCount) */
	int get_nSelectedColumns(long pColumnCount) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.getSelectedColumnCount(event);
		}
		if (DEBUG) print(this + ".IAccessibleTable2::get_nSelectedColumns() returning " + event.count);
		OS.MoveMemory(pColumnCount, new int [] { event.count }, 4);
		return COM.S_OK;
	}

	/* IAccessibleTable2::get_nSelectedRows([out] pRowCount) */
	int get_nSelectedRows(long pRowCount) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.getSelectedRowCount(event);
		}
		if (DEBUG) print(this + ".IAccessibleTable2::get_nSelectedRows() returning " + event.count);
		OS.MoveMemory(pRowCount, new int [] { event.count }, 4);
		return COM.S_OK;
	}

	/* IAccessibleTable2::get_rowDescription([in] row, [out] pbstrDescription) */
	int get_rowDescription(int row, long pbstrDescription) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		event.row = row;
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.getRowDescription(event);
		}
		if (DEBUG) print(this + ".IAccessibleTable2::get_rowDescription(row=" + row + ") returning " + event.result);
		setString(pbstrDescription, event.result);
		if (event.result == null) return COM.S_FALSE;
		return COM.S_OK;
	}

	/* IAccessibleTable2::get_selectedCells([out] ppCells, [out] pNSelectedCells) */
	int get_selectedCells(long ppCells, long pNSelectedCells) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.getSelectedCells(event);
		}
		if (DEBUG) print(this + ".IAccessibleTable2::get_selectedCells() returning " + (event.accessibles == null ? "null" : "accessibles[" + event.accessibles.length + "]"));
		if (event.accessibles == null || event.accessibles.length == 0) {
			OS.MoveMemory(ppCells, new long[] { 0 }, C.PTR_SIZEOF);
			OS.MoveMemory(pNSelectedCells, new int [] { 0 }, 4);
			return COM.S_FALSE;
		}
		int length = event.accessibles.length;
		long pv = OS.CoTaskMemAlloc(length * C.PTR_SIZEOF);
		int count = 0;
		for (int i = 0; i < length; i++) {
			Accessible accessible = event.accessibles[i];
			if (accessible != null) {
				accessible.AddRef();
				OS.MoveMemory(pv + i * C.PTR_SIZEOF, new long[] { accessible.getAddress() }, C.PTR_SIZEOF);
				count++;
			}
		}
		OS.MoveMemory(ppCells, new long [] { pv }, C.PTR_SIZEOF);
		OS.MoveMemory(pNSelectedCells, new int [] { count }, 4);
		return COM.S_OK;
	}

	/* IAccessibleTable2::get_selectedColumns([out] ppSelectedColumns, [out] pNColumns) */
	int get_selectedColumns(long ppSelectedColumns, long pNColumns) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.getSelectedColumns(event);
		}
		int count = event.selected == null ? 0 : event.selected.length;
		if (DEBUG) print(this + ".IAccessibleTable2::get_selectedColumns() returning " + (count == 0 ? "null" : "selected[" + count + "]"));
		if (count == 0) {
			OS.MoveMemory(ppSelectedColumns, new long[] { 0 }, C.PTR_SIZEOF);
			OS.MoveMemory(pNColumns, new int [] { 0 }, 4);
			return COM.S_FALSE;
		}
		long pv = OS.CoTaskMemAlloc(count * 4);
		OS.MoveMemory(pv, event.selected, count * 4);
		OS.MoveMemory(ppSelectedColumns, new long [] { pv }, C.PTR_SIZEOF);
		OS.MoveMemory(pNColumns, new int [] { count }, 4);
		return COM.S_OK;
	}

	/* IAccessibleTable2::get_selectedRows([out] ppSelectedRows, [out] pNRows) */
	int get_selectedRows(long ppSelectedRows, long pNRows) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.getSelectedRows(event);
		}
		int count = event.selected == null ? 0 : event.selected.length;
		if (DEBUG) print(this + ".IAccessibleTable2::get_selectedRows() returning " + (count == 0 ? "null" : "selected[" + count + "]"));
		if (count == 0) {
			OS.MoveMemory(ppSelectedRows, new long[] { 0 }, C.PTR_SIZEOF);
			OS.MoveMemory(pNRows, new int [] { 0 }, 4);
			return COM.S_FALSE;
		}
		long pv = OS.CoTaskMemAlloc(count * 4);
		OS.MoveMemory(pv, event.selected, count * 4);
		OS.MoveMemory(ppSelectedRows, new long [] { pv }, C.PTR_SIZEOF);
		OS.MoveMemory(pNRows, new int [] { count }, 4);
		return COM.S_OK;
	}

	/* IAccessibleTable2::get_summary([out] ppAccessible) */
	int get_summary(long ppAccessible) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.getSummary(event);
		}
		Accessible accessible = event.accessible;
		if (DEBUG) print(this + ".IAccessibleTable2::get_summary() returning " + accessible);
		if (accessible == null) {
			OS.MoveMemory(ppAccessible, new long[] { 0 }, C.PTR_SIZEOF);
			return COM.S_FALSE;
		}
		accessible.AddRef();
		OS.MoveMemory(ppAccessible, new long[] { accessible.getAddress() }, C.PTR_SIZEOF);
		return COM.S_OK;
	}

	/* IAccessibleTable2::get_isColumnSelected([in] column, [out] pIsSelected) */
	int get_isColumnSelected(int column, long pIsSelected) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		event.column = column;
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.isColumnSelected(event);
		}
		if (DEBUG) print(this + ".IAccessibleTable2::get_isColumnSelected() returning " + event.isSelected);
		OS.MoveMemory(pIsSelected, new int [] {event.isSelected ? 1 : 0}, 4);
		return COM.S_OK;
	}

	/* IAccessibleTable2::get_isRowSelected([in] row, [out] pIsSelected) */
	int get_isRowSelected(int row, long pIsSelected) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		event.row = row;
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.isRowSelected(event);
		}
		if (DEBUG) print(this + ".IAccessibleTable2::get_isRowSelected() returning " + event.isSelected);
		OS.MoveMemory(pIsSelected, new int [] {event.isSelected ? 1 : 0}, 4);
		return COM.S_OK;
	}

	/* IAccessibleTable2::selectRow([in] row) */
	int selectRow(int row) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		event.row = row;
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.setSelectedRow(event);
		}
		if (DEBUG) print(this + ".IAccessibleTable2::selectRow() returning " + (event.result == null ? "E_INVALIDARG" : event.result));
		if (event.result == null || !event.result.equals(ACC.OK)) return COM.E_INVALIDARG;
		return COM.S_OK;
	}

	/* IAccessibleTable2::selectColumn([in] column) */
	int selectColumn(int column) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		event.column = column;
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.setSelectedColumn(event);
		}
		if (DEBUG) print(this + ".IAccessibleTable2::selectColumn() returning " + (event.result == null ? "E_INVALIDARG" : event.result));
		if (event.result == null || !event.result.equals(ACC.OK)) return COM.E_INVALIDARG;
		return COM.S_OK;
	}

	/* IAccessibleTable2::unselectRow([in] row) */
	int unselectRow(int row) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		event.row = row;
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.deselectRow(event);
		}
		if (DEBUG) print(this + ".IAccessibleTable2::unselectRow() returning " + (event.result == null ? "E_INVALIDARG" : event.result));
		if (event.result == null || !event.result.equals(ACC.OK)) return COM.E_INVALIDARG;
		return COM.S_OK;
	}

	/* IAccessibleTable2::unselectColumn([in] column) */
	int unselectColumn(int column) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		event.column = column;
		for (int i = 0; i < accessibleTableListenersSize(); i++) {
			AccessibleTableListener listener = accessibleTableListeners.get(i);
			listener.deselectColumn(event);
		}
		if (DEBUG) print(this + ".IAccessibleTable2::unselectColumn() returning " + (event.result == null ? "E_INVALIDARG" : event.result));
		if (event.result == null || !event.result.equals(ACC.OK)) return COM.E_INVALIDARG;
		return COM.S_OK;
	}

	/* IAccessibleTable2::get_modelChange([out] pModelChange) */
	int get_modelChange(long pModelChange) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleTable2::get_modelChange() returning " + (tableChange == null ? "null" : "tableChange=" + tableChange[0] + ", " + tableChange[1] + ", " + tableChange[2] + ", " + tableChange[3]));
		if (tableChange == null) {
			OS.MoveMemory(pModelChange, new long [] { 0 }, C.PTR_SIZEOF);
			return COM.S_FALSE;
		}
		OS.MoveMemory(pModelChange, tableChange, tableChange.length * 4);
		return COM.S_OK;
	}

	/* IAccessibleTableCell::get_columnExtent([out] pNColumnsSpanned) */
	int get_columnExtent(long pNColumnsSpanned) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableCellEvent event = new AccessibleTableCellEvent(this);
		for (int i = 0; i < accessibleTableCellListenersSize(); i++) {
			AccessibleTableCellListener listener = accessibleTableCellListeners.get(i);
			listener.getColumnSpan(event);
		}
		if (DEBUG) print(this + ".IAccessibleTableCell::get_columnExtent() returning " + event.count);
		OS.MoveMemory(pNColumnsSpanned, new int [] { event.count }, 4);
		return COM.S_OK;
	}

	/* IAccessibleTableCell::get_columnHeaderCells([out] ppCellAccessibles, [out] pNColumnHeaderCells) */
	int get_columnHeaderCells(long ppCellAccessibles, long pNColumnHeaderCells) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableCellEvent event = new AccessibleTableCellEvent(this);
		for (int i = 0; i < accessibleTableCellListenersSize(); i++) {
			AccessibleTableCellListener listener = accessibleTableCellListeners.get(i);
			listener.getColumnHeaders(event);
		}
		if (DEBUG) print(this + ".IAccessibleTableCell::get_columnHeaderCells() returning " + (event.accessibles == null ? "null" : "accessibles[" + event.accessibles.length + "]"));
		if (event.accessibles == null || event.accessibles.length == 0) {
			OS.MoveMemory(ppCellAccessibles, new long[] { 0 }, C.PTR_SIZEOF);
			OS.MoveMemory(pNColumnHeaderCells, new int [] { 0 }, 4);
			return COM.S_FALSE;
		}
		int length = event.accessibles.length;
		long pv = OS.CoTaskMemAlloc(length * C.PTR_SIZEOF);
		int count = 0;
		for (int i = 0; i < length; i++) {
			Accessible accessible = event.accessibles[i];
			if (accessible != null) {
				accessible.AddRef();
				OS.MoveMemory(pv + i * C.PTR_SIZEOF, new long[] { accessible.getAddress() }, C.PTR_SIZEOF);
				count++;
			}
		}
		OS.MoveMemory(ppCellAccessibles, new long [] { pv }, C.PTR_SIZEOF);
		OS.MoveMemory(pNColumnHeaderCells, new int [] { count }, 4);
		return COM.S_OK;
	}

	/* IAccessibleTableCell::get_columnIndex([out] pColumnIndex) */
	int get_columnIndex(long pColumnIndex) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableCellEvent event = new AccessibleTableCellEvent(this);
		for (int i = 0; i < accessibleTableCellListenersSize(); i++) {
			AccessibleTableCellListener listener = accessibleTableCellListeners.get(i);
			listener.getColumnIndex(event);
		}
		if (DEBUG) print(this + ".IAccessibleTableCell::get_columnIndex() returning " + event.index);
		OS.MoveMemory(pColumnIndex, new int [] { event.index }, 4);
		return COM.S_OK;
	}

	/* IAccessibleTableCell::get_rowExtent([out] pNRowsSpanned) */
	int get_rowExtent(long pNRowsSpanned) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableCellEvent event = new AccessibleTableCellEvent(this);
		for (int i = 0; i < accessibleTableCellListenersSize(); i++) {
			AccessibleTableCellListener listener = accessibleTableCellListeners.get(i);
			listener.getRowSpan(event);
		}
		if (DEBUG) print(this + ".IAccessibleTableCell::get_rowExtent() returning " + event.count);
		OS.MoveMemory(pNRowsSpanned, new int [] { event.count }, 4);
		return COM.S_OK;
	}

	/* IAccessibleTableCell::get_rowHeaderCells([out] ppCellAccessibles, [out] pNRowHeaderCells) */
	int get_rowHeaderCells(long ppCellAccessibles, long pNRowHeaderCells) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableCellEvent event = new AccessibleTableCellEvent(this);
		for (int i = 0; i < accessibleTableCellListenersSize(); i++) {
			AccessibleTableCellListener listener = accessibleTableCellListeners.get(i);
			listener.getRowHeaders(event);
		}
		if (DEBUG) print(this + ".IAccessibleTableCell::get_rowHeaderCells() returning " + (event.accessibles == null ? "null" : "accessibles[" + event.accessibles.length + "]"));
		if (event.accessibles == null || event.accessibles.length == 0) {
			OS.MoveMemory(ppCellAccessibles, new long[] { 0 }, C.PTR_SIZEOF);
			OS.MoveMemory(pNRowHeaderCells, new int [] { 0 }, 4);
			return COM.S_FALSE;
		}
		int length = event.accessibles.length;
		long pv = OS.CoTaskMemAlloc(length * C.PTR_SIZEOF);
		int count = 0;
		for (int i = 0; i < length; i++) {
			Accessible accessible = event.accessibles[i];
			if (accessible != null) {
				accessible.AddRef();
				OS.MoveMemory(pv + i * C.PTR_SIZEOF, new long[] { accessible.getAddress() }, C.PTR_SIZEOF);
				count++;
			}
		}
		OS.MoveMemory(ppCellAccessibles, new long [] { pv }, C.PTR_SIZEOF);
		OS.MoveMemory(pNRowHeaderCells, new int [] { count }, 4);
		return COM.S_OK;
	}

	/* IAccessibleTableCell::get_rowIndex([out] pRowIndex) */
	int get_rowIndex(long pRowIndex) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableCellEvent event = new AccessibleTableCellEvent(this);
		for (int i = 0; i < accessibleTableCellListenersSize(); i++) {
			AccessibleTableCellListener listener = accessibleTableCellListeners.get(i);
			listener.getRowIndex(event);
		}
		if (DEBUG) print(this + ".IAccessibleTableCell::get_rowIndex() returning " + event.index);
		OS.MoveMemory(pRowIndex, new int [] { event.index }, 4);
		return COM.S_OK;
	}

	/* IAccessibleTableCell::get_isSelected([out] pIsSelected) */
	int get_isSelected(long pIsSelected) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableCellEvent event = new AccessibleTableCellEvent(this);
		for (int i = 0; i < accessibleTableCellListenersSize(); i++) {
			AccessibleTableCellListener listener = accessibleTableCellListeners.get(i);
			listener.isSelected(event);
		}
		if (DEBUG) print(this + ".IAccessibleTableCell::get_isSelected() returning " + event.isSelected);
		OS.MoveMemory(pIsSelected, new int [] {event.isSelected ? 1 : 0}, 4);
		return COM.S_OK;
	}

	/* IAccessibleTableCell::get_rowColumnExtents([out] pRow, [out] pColumn, [out] pRowExtents, [out] pColumnExtents, [out] pIsSelected) */
	int get_rowColumnExtents(long pRow, long pColumn, long pRowExtents, long pColumnExtents, long pIsSelected) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleTableCell::get_rowColumnExtents");
		// TODO: should we implement this? It is just a convenience function.
		return COM.DISP_E_MEMBERNOTFOUND;
//		AccessibleTableCellEvent event = new AccessibleTableCellEvent(this);
//		for (int i = 0; i < accessibleTableCellListenersSize(); i++) {
//			AccessibleTableCellListener listener = (AccessibleTableCellListener) accessibleTableCellListeners.get(i);
//			listener.getRowColumnExtents(event);
//		}
//		COM.MoveMemory(pRow, new int [] { event.row }, 4);
//		COM.MoveMemory(pColumn, new int [] { event.column }, 4);
//		COM.MoveMemory(pRowExtents, new int [] { event.rowExtents }, 4);
//		COM.MoveMemory(pColumnExtents, new int [] { event.columnExtents }, 4);
//		return COM.S_OK;
	}

	/* IAccessibleTableCell::get_table([out] ppTable) */
	int get_table(long ppTable) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTableCellEvent event = new AccessibleTableCellEvent(this);
		for (int i = 0; i < accessibleTableCellListenersSize(); i++) {
			AccessibleTableCellListener listener = accessibleTableCellListeners.get(i);
			listener.getTable(event);
		}
		Accessible accessible = event.accessible;
		if (DEBUG) print(this + ".IAccessibleTableCell::get_table() returning " + accessible);
		if (accessible == null) {
			// TODO: This is not supposed to return S_FALSE. We need to lookup the table role parent and return that.
			OS.MoveMemory(ppTable, new long[] { 0 }, C.PTR_SIZEOF);
			return COM.S_FALSE;
		}
		accessible.AddRef();
		OS.MoveMemory(ppTable, new long[] { accessible.getAddress() }, C.PTR_SIZEOF);
		return COM.S_OK;
	}

	/* IAccessibleText::addSelection([in] startOffset, [in] endOffset) */
	int addSelection(int startOffset, int endOffset) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleText::addSelection(" + startOffset + ", " + endOffset + ")");
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.start = startOffset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : startOffset;
		event.end = endOffset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : endOffset;
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.addSelection(event);
		}
		if (event.result == null || !event.result.equals(ACC.OK)) return COM.E_INVALIDARG;
		return COM.S_OK;
	}

	/* IAccessibleText::get_attributes([in] offset, [out] pStartOffset, [out] pEndOffset, [out] pbstrTextAttributes) */
	int get_attributes(int offset, long pStartOffset, long pEndOffset, long pbstrTextAttributes) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTextAttributeEvent event = new AccessibleTextAttributeEvent(this);
		event.offset = offset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : offset;
		for (int i = 0; i < accessibleAttributeListenersSize(); i++) {
			AccessibleAttributeListener listener = accessibleAttributeListeners.get(i);
			listener.getTextAttributes(event);
		}
		String textAttributes = "";
		TextStyle style = event.textStyle;
		if (style != null) {
			if (style.rise != 0) {
				textAttributes += "text-position:";
				if (style.rise > 0) textAttributes += "super";
				else textAttributes += "sub";
			}
			if (style.underline) {
				textAttributes += "text-underline-type:";
				switch (style.underlineStyle) {
					case SWT.UNDERLINE_SINGLE: textAttributes += "single;"; break;
					case SWT.UNDERLINE_DOUBLE: textAttributes += "double;"; break;
					case SWT.UNDERLINE_SQUIGGLE: textAttributes += "single;text-underline-style:wave;"; break;
					case SWT.UNDERLINE_ERROR: textAttributes += "single;text-underline-style:wave;invalid:true;"; break;
					default: textAttributes += "none;"; break;
				}
				// style.underlineColor is not currently part of the IA2 spec. If provided, it would be "text-underline-color:rgb(n,n,n);"
			}
			if (style.strikeout) {
				textAttributes += "text-line-through-type:single;";
				// style.strikeoutColor is not currently part of the IA2 spec. If provided, it would be "text-line-through-color:rgb(n,n,n);"
			}
			Font font = style.font;
			if (font != null && !font.isDisposed()) {
				FontData fontData = font.getFontData()[0];
				textAttributes += "font-family:" + fontData.getName() + ";";
				textAttributes += "font-size:" + fontData.getHeight() + "pt;";
				textAttributes += "font-style:" + (fontData.data.lfItalic != 0 ? "italic" : "normal") + ";";
				textAttributes += "font-weight:" + fontData.data.lfWeight + ";";
			}
			Color color = style.foreground;
			if (color != null && !color.isDisposed()) {
				textAttributes += "color:rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ");";
			}
			color = style.background;
			if (color != null && !color.isDisposed()) {
				textAttributes += "background-color:rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ");";
			}
		}
		if (event.attributes != null) {
			for (int i = 0; i + 1 < event.attributes.length; i += 2) {
				textAttributes += event.attributes[i] + ":" + event.attributes[i+1] + ";";
			}
		}
		if (DEBUG) print(this + ".IAccessibleText::get_attributes(" + offset + ") returning start = " + event.start + ", end = " + event.end + ", attributes = " + textAttributes);
		OS.MoveMemory(pStartOffset, new int [] { event.start }, 4);
		OS.MoveMemory(pEndOffset, new int [] { event.end }, 4);
		setString(pbstrTextAttributes, textAttributes);
		if (textAttributes.length() == 0) return COM.S_FALSE;
		return COM.S_OK;
	}

	/* IAccessibleText::get_caretOffset([out] pOffset) */
	int get_caretOffset(long pOffset) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		int offset = getCaretOffset();
		if (DEBUG) print(this + ".IAccessibleText::get_caretOffset returning " + offset + hresult(offset == -1 ? COM.S_FALSE : COM.S_OK));
		OS.MoveMemory(pOffset, new int [] { offset }, 4);
		if (offset == -1) return COM.S_FALSE;
		return COM.S_OK;
	}

	/* IAccessibleText::get_characterExtents([in] offset, [in] coordType, [out] pX, [out] pY, [out] pWidth, [out] pHeight) */
	int get_characterExtents(int offset, int coordType, long pX, long pY, long pWidth, long pHeight) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		int length = getCharacterCount();
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.start = offset == COM.IA2_TEXT_OFFSET_LENGTH ? length : offset < 0 ? 0 : offset;
		event.end = offset == COM.IA2_TEXT_OFFSET_LENGTH || offset >= length ? length : offset + 1;
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.getTextBounds(event);
		}
		/* Note: event.rectangles is not used here, because IAccessibleText::get_characterExtents is just for one character. */
		if (DEBUG) print(this + ".IAccessibleText::get_characterExtents(" + offset + ") returning " + event.x + ", " + event.y + ", " + event.width + ", " + event.height);
		OS.MoveMemory(pX, new int [] { event.x }, 4);
		OS.MoveMemory(pY, new int [] { event.y }, 4);
		OS.MoveMemory(pWidth, new int [] { event.width }, 4);
		OS.MoveMemory(pHeight, new int [] { event.height }, 4);
		if (event.width == 0 && event.height == 0) return COM.E_INVALIDARG;
		return COM.S_OK;
	}

	/* IAccessibleText::get_nSelections([out] pNSelections) */
	int get_nSelections(long pNSelections) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.count = -1;
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.getSelectionCount(event);
		}
		if (event.count == -1) {
			event.childID = ACC.CHILDID_SELF;
			event.offset = -1;
			event.length = 0;
			for (int i = 0; i < accessibleTextListenersSize(); i++) {
				AccessibleTextListener listener = accessibleTextListeners.get(i);
				listener.getSelectionRange (event);
			}
			event.count = event.offset != -1 && event.length > 0 ? 1 : 0;
		}
		if (DEBUG) print(this + ".IAccessibleText::get_nSelections returning " + event.count);
		OS.MoveMemory(pNSelections, new int [] { event.count }, 4);
		return COM.S_OK;
	}

	/* IAccessibleText::get_offsetAtPoint([in] x, [in] y, [in] coordType, [out] pOffset) */
	int get_offsetAtPoint(int x, int y, int coordType, long pOffset) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.x = x;
		event.y = y;
		event.offset = -1;
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.getOffsetAtPoint(event);
		}
		if (DEBUG) print(this + ".IAccessibleText::get_offsetAtPoint(" + x + ", " + y + ") returning " + event.offset + hresult(event.offset == -1 ? COM.S_FALSE : COM.S_OK));
		/*
		 * Note that the current IA2 spec says to return 0 when there's nothing to return,
		 * but since 0 is a valid return value, the spec is going to be updated to return -1.
		 */
		OS.MoveMemory(pOffset, new int [] { event.offset }, 4);
		if (event.offset == -1) return COM.S_FALSE;
		return COM.S_OK;
	}

	/* IAccessibleText::get_selection([in] selectionIndex, [out] pStartOffset, [out] pEndOffset) */
	int get_selection(int selectionIndex, long pStartOffset, long pEndOffset) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.index = selectionIndex;
		event.start = -1;
		event.end = -1;
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.getSelection(event);
		}
		if (event.start == -1 && selectionIndex == 0) {
			event.childID = ACC.CHILDID_SELF;
			event.offset = -1;
			event.length = 0;
			for (int i = 0; i < accessibleTextListenersSize(); i++) {
				AccessibleTextListener listener = accessibleTextListeners.get(i);
				listener.getSelectionRange (event);
			}
			event.start = event.offset;
			event.end = event.offset + event.length;
		}
		if (DEBUG) print(this + ".IAccessibleText::get_selection(" + selectionIndex + ") returning " + event.start + ", " + event.end);
		OS.MoveMemory(pStartOffset, new int [] { event.start }, 4);
		OS.MoveMemory(pEndOffset, new int [] { event.end }, 4);
		/*
		 * Note that the current IA2 spec says to return 0,0 when there's nothing to return,
		 * but since 0 is a valid return value, the spec is going to be updated to return -1,-1.
		 */
		if (event.start == -1) return COM.S_FALSE;
		return COM.S_OK;
	}

	/* IAccessibleText::get_text([in] startOffset, [in] endOffset, [out] pbstrText) */
	int get_text(int startOffset, int endOffset, long pbstrText) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.start = startOffset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : startOffset;
		event.end = endOffset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : endOffset;
		if (event.start > event.end) {
			/* IA2 spec says that indices can be exchanged. */
			int temp = event.start;
			event.start = event.end;
			event.end = temp;
		}
		event.count = 0;
		event.type = ACC.TEXT_BOUNDARY_ALL;
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.getText(event);
		}
		if (event.result == null) {
			AccessibleControlEvent e = new AccessibleControlEvent(this);
			e.childID = ACC.CHILDID_SELF;
			for (int i = 0; i < accessibleControlListenersSize(); i++) {
				AccessibleControlListener listener = accessibleControlListeners.get(i);
				listener.getRole(e);
				listener.getValue(e);
			}
			// TODO: Consider passing the value through for other roles as well (i.e. combo, etc). Keep in sync with get_nCharacters.
			if (e.detail == ACC.ROLE_TEXT) {
				event.result = e.result;
			}
		}
		if (DEBUG) print(this + ".IAccessibleText::get_text(" + startOffset + ", " + endOffset + ") returning " + event.result + hresult(event.result == null ? COM.E_INVALIDARG : COM.S_OK));
		setString(pbstrText, event.result);
		if (event.result == null) return COM.E_INVALIDARG;
		return COM.S_OK;
	}

	/* IAccessibleText::get_textBeforeOffset([in] offset, [in] boundaryType, [out] pStartOffset, [out] pEndOffset, [out] pbstrText) */
	int get_textBeforeOffset(int offset, int boundaryType, long pStartOffset, long pEndOffset, long pbstrText) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		int charCount = getCharacterCount();
		event.start = offset == COM.IA2_TEXT_OFFSET_LENGTH ? charCount : offset == COM.IA2_TEXT_OFFSET_CARET ? getCaretOffset() : offset;
		event.end = event.start;
		event.count = -1;
		switch (boundaryType) {
			case COM.IA2_TEXT_BOUNDARY_CHAR: event.type = ACC.TEXT_BOUNDARY_CHAR; break;
			case COM.IA2_TEXT_BOUNDARY_WORD: event.type = ACC.TEXT_BOUNDARY_WORD; break;
			case COM.IA2_TEXT_BOUNDARY_SENTENCE: event.type = ACC.TEXT_BOUNDARY_SENTENCE; break;
			case COM.IA2_TEXT_BOUNDARY_PARAGRAPH: event.type = ACC.TEXT_BOUNDARY_PARAGRAPH; break;
			case COM.IA2_TEXT_BOUNDARY_LINE: event.type = ACC.TEXT_BOUNDARY_LINE; break;
			default: return COM.E_INVALIDARG;
		}
		int eventStart = event.start;
		int eventEnd = event.end;
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.getText(event);
		}
		if (event.end < charCount) {
			switch (boundaryType) {
				case COM.IA2_TEXT_BOUNDARY_WORD:
				case COM.IA2_TEXT_BOUNDARY_SENTENCE:
				case COM.IA2_TEXT_BOUNDARY_PARAGRAPH:
				case COM.IA2_TEXT_BOUNDARY_LINE:
					int start = event.start;
					event.start = eventStart;
					event.end = eventEnd;
					event.count = 0;
					for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
						AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
						listener.getText(event);
					}
					event.end = event.start;
					event.start = start;
					event.type = ACC.TEXT_BOUNDARY_ALL;
					event.count = 0;
					for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
						AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
						listener.getText(event);
					}
			}
		}
		if (DEBUG) print(this + ".IAccessibleText::get_textBeforeOffset(" + offset + ") returning start=" + event.start + ", end=" + event.end + " " + event.result + hresult(event.result == null ? COM.S_FALSE : COM.S_OK));
		OS.MoveMemory(pStartOffset, new int [] { event.start }, 4);
		OS.MoveMemory(pEndOffset, new int [] { event.end }, 4);
		setString(pbstrText, event.result);
		if (event.result == null) return COM.S_FALSE;
		return COM.S_OK;
	}

	/* IAccessibleText::get_textAfterOffset([in] offset, [in] boundaryType, [out] pStartOffset, [out] pEndOffset, [out] pbstrText) */
	int get_textAfterOffset(int offset, int boundaryType, long pStartOffset, long pEndOffset, long pbstrText) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		int charCount = getCharacterCount();
		event.start = offset == COM.IA2_TEXT_OFFSET_LENGTH ? charCount : offset == COM.IA2_TEXT_OFFSET_CARET ? getCaretOffset() : offset;
		event.end = event.start;
		event.count = 1;
		switch (boundaryType) {
			case COM.IA2_TEXT_BOUNDARY_CHAR: event.type = ACC.TEXT_BOUNDARY_CHAR; break;
			case COM.IA2_TEXT_BOUNDARY_WORD: event.type = ACC.TEXT_BOUNDARY_WORD; break;
			case COM.IA2_TEXT_BOUNDARY_SENTENCE: event.type = ACC.TEXT_BOUNDARY_SENTENCE; break;
			case COM.IA2_TEXT_BOUNDARY_PARAGRAPH: event.type = ACC.TEXT_BOUNDARY_PARAGRAPH; break;
			case COM.IA2_TEXT_BOUNDARY_LINE: event.type = ACC.TEXT_BOUNDARY_LINE; break;
			default: return COM.E_INVALIDARG;
		}
		int eventStart = event.start;
		int eventEnd = event.end;
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.getText(event);
		}
		if (event.end < charCount) {
			switch (boundaryType) {
				case COM.IA2_TEXT_BOUNDARY_WORD:
				case COM.IA2_TEXT_BOUNDARY_SENTENCE:
				case COM.IA2_TEXT_BOUNDARY_PARAGRAPH:
				case COM.IA2_TEXT_BOUNDARY_LINE:
					int start = event.start;
					event.start = eventStart;
					event.end = eventEnd;
					event.count = 2;
					for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
						AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
						listener.getText(event);
					}
					event.end = event.start;
					event.start = start;
					event.type = ACC.TEXT_BOUNDARY_ALL;
					event.count = 0;
					for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
						AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
						listener.getText(event);
					}
			}
		}
		if (DEBUG) print(this + ".IAccessibleText::get_textAfterOffset(" + offset + ") returning start=" + event.start + ", end=" + event.end + " " + event.result + hresult(event.result == null ? COM.S_FALSE : COM.S_OK));
		OS.MoveMemory(pStartOffset, new int [] { event.start }, 4);
		OS.MoveMemory(pEndOffset, new int [] { event.end }, 4);
		setString(pbstrText, event.result);
		if (event.result == null) return COM.S_FALSE;
		return COM.S_OK;
	}

	/* IAccessibleText::get_textAtOffset([in] offset, [in] boundaryType, [out] pStartOffset, [out] pEndOffset, [out] pbstrText) */
	int get_textAtOffset(int offset, int boundaryType, long pStartOffset, long pEndOffset, long pbstrText) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		int charCount = getCharacterCount();
		event.start = offset == COM.IA2_TEXT_OFFSET_LENGTH ? charCount : offset == COM.IA2_TEXT_OFFSET_CARET ? getCaretOffset() : offset;
		event.end = event.start;
		event.count = 0;
		switch (boundaryType) {
			case COM.IA2_TEXT_BOUNDARY_CHAR: event.type = ACC.TEXT_BOUNDARY_CHAR; break;
			case COM.IA2_TEXT_BOUNDARY_WORD: event.type = ACC.TEXT_BOUNDARY_WORD; break;
			case COM.IA2_TEXT_BOUNDARY_SENTENCE: event.type = ACC.TEXT_BOUNDARY_SENTENCE; break;
			case COM.IA2_TEXT_BOUNDARY_PARAGRAPH: event.type = ACC.TEXT_BOUNDARY_PARAGRAPH; break;
			case COM.IA2_TEXT_BOUNDARY_LINE: event.type = ACC.TEXT_BOUNDARY_LINE; break;
			case COM.IA2_TEXT_BOUNDARY_ALL: {
				event.type = ACC.TEXT_BOUNDARY_ALL;
				event.start = 0;
				event.end = charCount;
				event.count = 0;
				break;
			}
			default: return COM.E_INVALIDARG;
		}
		int eventStart = event.start;
		int eventEnd = event.end;
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.getText(event);
		}
		if (event.end < charCount) {
			switch (boundaryType) {
				case COM.IA2_TEXT_BOUNDARY_WORD:
				case COM.IA2_TEXT_BOUNDARY_SENTENCE:
				case COM.IA2_TEXT_BOUNDARY_PARAGRAPH:
				case COM.IA2_TEXT_BOUNDARY_LINE:
					int start = event.start;
					event.start = eventStart;
					event.end = eventEnd;
					event.count = 1;
					for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
						AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
						listener.getText(event);
					}
					event.end = event.start;
					event.start = start;
					event.type = ACC.TEXT_BOUNDARY_ALL;
					event.count = 0;
					for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
						AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
						listener.getText(event);
					}
			}
		}
		if (DEBUG) print(this + ".IAccessibleText::get_textAtOffset(" + offset + ") returning start=" + event.start + ", end=" + event.end + " " + event.result + hresult(event.result == null ? COM.S_FALSE : COM.S_OK));
		OS.MoveMemory(pStartOffset, new int [] { event.start }, 4);
		OS.MoveMemory(pEndOffset, new int [] { event.end }, 4);
		setString(pbstrText, event.result);
		if (event.result == null) return COM.S_FALSE;
		return COM.S_OK;
	}

	/* IAccessibleText::removeSelection([in] selectionIndex) */
	int removeSelection(int selectionIndex) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.index = selectionIndex;
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.removeSelection(event);
		}
		if (DEBUG) print(this + ".IAccessibleText::removeSelection(" + selectionIndex + ") returning" + hresult(event.result == null || !event.result.equals(ACC.OK) ? COM.E_INVALIDARG : COM.S_OK));
		if (event.result == null || !event.result.equals(ACC.OK)) return COM.E_INVALIDARG;
		return COM.S_OK;
	}

	/* IAccessibleText::setCaretOffset([in] offset) */
	int setCaretOffset(int offset) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.offset = offset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : offset;
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.setCaretOffset(event);
		}
		if (DEBUG) print(this + ".IAccessibleText::setCaretOffset(" + offset + ") returning" + hresult(event.result == null || !event.result.equals(ACC.OK) ? COM.E_INVALIDARG : COM.S_OK));
		if (event.result == null || !event.result.equals(ACC.OK)) return COM.E_INVALIDARG; // TODO: @retval E_FAIL if the caret cannot be set ?
		return COM.S_OK;
	}

	/* IAccessibleText::setSelection([in] selectionIndex, [in] startOffset, [in] endOffset) */
	int setSelection(int selectionIndex, int startOffset, int endOffset) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.index = selectionIndex;
		event.start = startOffset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : startOffset;
		event.end = endOffset == COM.IA2_TEXT_OFFSET_LENGTH ? getCharacterCount() : endOffset;
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.setSelection(event);
		}
		if (DEBUG) print(this + ".IAccessibleText::setSelection(index=" + selectionIndex + ", start=" + event.start + ", end=" + event.end + ") returning " + (event.result.equals(ACC.OK) ? "OK" : "INVALIDARG"));
		if (event.result == null || !event.result.equals(ACC.OK)) return COM.E_INVALIDARG;
		return COM.S_OK;
	}

	/* IAccessibleText::get_nCharacters([out] pNCharacters) */
	int get_nCharacters(long pNCharacters) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		int count = getCharacterCount();
		OS.MoveMemory(pNCharacters, new int [] { count }, 4);
		if (DEBUG) print(this + ".IAccessibleText::get_nCharacters returning " + count);
		return COM.S_OK;
	}

	/* IAccessibleText::scrollSubstringTo([in] startIndex, [in] endIndex, [in] scrollType) */
	int scrollSubstringTo(int startIndex, int endIndex, int scrollType) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleText::scrollSubstringTo");
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.start = startIndex;
		event.end = endIndex;
		switch (scrollType) {
			case COM.IA2_SCROLL_TYPE_TOP_LEFT: event.type = ACC.SCROLL_TYPE_TOP_LEFT; break;
			case COM.IA2_SCROLL_TYPE_BOTTOM_RIGHT: event.type = ACC.SCROLL_TYPE_BOTTOM_RIGHT; break;
			case COM.IA2_SCROLL_TYPE_TOP_EDGE: event.type = ACC.SCROLL_TYPE_TOP_EDGE; break;
			case COM.IA2_SCROLL_TYPE_BOTTOM_EDGE: event.type = ACC.SCROLL_TYPE_BOTTOM_EDGE; break;
			case COM.IA2_SCROLL_TYPE_LEFT_EDGE: event.type = ACC.SCROLL_TYPE_LEFT_EDGE; break;
			case COM.IA2_SCROLL_TYPE_RIGHT_EDGE: event.type = ACC.SCROLL_TYPE_RIGHT_EDGE; break;
			case COM.IA2_SCROLL_TYPE_ANYWHERE: event.type = ACC.SCROLL_TYPE_ANYWHERE; break;
		}
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.scrollText(event);
		}
		if (event.result == null || !event.result.equals(ACC.OK)) return COM.E_INVALIDARG;
		return COM.S_OK;
	}

	/* IAccessibleText::scrollSubstringToPoint([in] startIndex, [in] endIndex, [in] coordinateType, [in] x, [in] y) */
	int scrollSubstringToPoint(int startIndex, int endIndex, int coordinateType, int x, int y) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleText::scrollSubstringToPoint");
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.start = startIndex;
		event.end = endIndex;
		event.type = ACC.SCROLL_TYPE_POINT;
		event.x = x;
		event.y = y;
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.scrollText(event);
		}
		if (event.result == null || !event.result.equals(ACC.OK)) return COM.E_INVALIDARG; // TODO: @retval S_FALSE if the object is already at the specified location.
		return COM.S_OK;
	}

	/* IAccessibleText::get_newText([out] pNewText) */
	int get_newText(long pNewText) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleText::get_newText");
		String text = null;
		int start = 0;
		int end = 0;
		if (textInserted != null) {
			text = (String) textInserted[3];
			start = ((Integer)textInserted[1]).intValue();
			end = ((Integer)textInserted[2]).intValue();
		}
		setString(pNewText, text);
		OS.MoveMemory(pNewText + C.PTR_SIZEOF, new int [] {start}, 4);
		OS.MoveMemory(pNewText + C.PTR_SIZEOF + 4, new int [] {end}, 4);
		if (textInserted == null) return COM.S_FALSE;
		return COM.S_OK;
	}

	/* IAccessibleText::get_oldText([out] pOldText) */
	int get_oldText(long pOldText) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleText::get_oldText");
		String text = null;
		int start = 0;
		int end = 0;
		if (textDeleted != null) {
			text = (String) textDeleted[3];
			start = ((Integer)textDeleted[1]).intValue();
			end = ((Integer)textDeleted[2]).intValue();
		}
		setString(pOldText, text);
		OS.MoveMemory(pOldText + C.PTR_SIZEOF, new int [] {start}, 4);
		OS.MoveMemory(pOldText + C.PTR_SIZEOF + 4, new int [] {end}, 4);
		if (textDeleted == null) return COM.S_FALSE;
		return COM.S_OK;
	}

	/* IAccessibleValue::get_currentValue([out] pCurrentValue) */
	int get_currentValue(long pCurrentValue) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleValueEvent event = new AccessibleValueEvent(this);
		for (int i = 0; i < accessibleValueListenersSize(); i++) {
			AccessibleValueListener listener = accessibleValueListeners.get(i);
			listener.getCurrentValue(event);
		}
		if (DEBUG) print(this + ".IAccessibleValue::get_currentValue returning " + event.value + hresult(event.value == null ? COM.S_FALSE : COM.S_OK));
		setNumberVARIANT(pCurrentValue, event.value);
		return COM.S_OK;
	}

	/* IAccessibleValue::setCurrentValue([in] value) */
	int setCurrentValue(long value) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		if (DEBUG) print(this + ".IAccessibleValue::setCurrentValue");
		AccessibleValueEvent event = new AccessibleValueEvent(this);
		event.value = getNumberVARIANT(value);
		for (int i = 0; i < accessibleValueListenersSize(); i++) {
			AccessibleValueListener listener = accessibleValueListeners.get(i);
			listener.setCurrentValue(event);
		}
		//if (event.value == null) return COM.S_FALSE;
		return COM.S_OK;
	}

	/* IAccessibleValue::get_maximumValue([out] pMaximumValue) */
	int get_maximumValue(long pMaximumValue) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleValueEvent event = new AccessibleValueEvent(this);
		for (int i = 0; i < accessibleValueListenersSize(); i++) {
			AccessibleValueListener listener = accessibleValueListeners.get(i);
			listener.getMaximumValue(event);
		}
		if (DEBUG) print(this + ".IAccessibleValue::get_maximumValue returning " + event.value + hresult(event.value == null ? COM.S_FALSE : COM.S_OK));
		setNumberVARIANT(pMaximumValue, event.value);
		return COM.S_OK;
	}

	/* IAccessibleValue::get_minimumValue([out] pMinimumValue) */
	int get_minimumValue(long pMinimumValue) {
		if (control != null && control.isDisposed()) return COM.CO_E_OBJNOTCONNECTED;
		AccessibleValueEvent event = new AccessibleValueEvent(this);
		for (int i = 0; i < accessibleValueListenersSize(); i++) {
			AccessibleValueListener listener = accessibleValueListeners.get(i);
			listener.getMinimumValue(event);
		}
		if (DEBUG) print(this + ".IAccessibleValue::get_minimumValue returning " + event.value + hresult(event.value == null ? COM.S_FALSE : COM.S_OK));
		setNumberVARIANT(pMinimumValue, event.value);
		return COM.S_OK;
	}

	int eventChildID() {
		if (parent == null) return COM.CHILDID_SELF;
		if (uniqueID == -1) uniqueID = UniqueID--;
		return uniqueID;
	}

	void checkUniqueID(int childID) {
		/* If the application is using child ids, check whether there's a corresponding
		 * accessible, and if so, use the child id as that accessible's unique id. */
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = childID;
		for (int l = 0; l < accessibleControlListenersSize(); l++) {
			AccessibleControlListener listener = accessibleControlListeners.get(l);
			listener.getChild(event);
		}
		Accessible accessible = event.accessible;
		if (accessible != null && accessible.uniqueID == -1) {
			accessible.uniqueID = childID;
		}
	}

	int childIDToOs(int childID) {
		if (childID == ACC.CHILDID_SELF) return COM.CHILDID_SELF;
		/* ChildIDs are 1-based indices. */
		int osChildID = childID + 1;
		if (control instanceof Tree) {
			osChildID = (int)OS.SendMessage (control.handle, OS.TVM_MAPHTREEITEMTOACCID, childID, 0);
		}
		checkUniqueID(osChildID);
		return osChildID;
	}

	int osToChildID(int osChildID) {
		if (osChildID == COM.CHILDID_SELF) return ACC.CHILDID_SELF;
		/*
		* Feature of Windows:
		* Before Windows XP, tree item ids were 1-based indices.
		* Windows XP and later use the tree item handle for the
		* accessible child ID. For backward compatibility, we still
		* take 1-based childIDs for tree items prior to Windows XP.
		* All other childIDs are 1-based indices.
		*/
		if (!(control instanceof Tree)) return osChildID - 1;
		return (int)OS.SendMessage (control.handle, OS.TVM_MAPACCIDTOHTREEITEM, osChildID, 0);
	}

	int stateToOs(int state) {
		int osState = 0;
		if ((state & ACC.STATE_SELECTED) != 0) osState |= COM.STATE_SYSTEM_SELECTED;
		if ((state & ACC.STATE_SELECTABLE) != 0) osState |= COM.STATE_SYSTEM_SELECTABLE;
		if ((state & ACC.STATE_MULTISELECTABLE) != 0) osState |= COM.STATE_SYSTEM_MULTISELECTABLE;
		if ((state & ACC.STATE_FOCUSED) != 0) osState |= COM.STATE_SYSTEM_FOCUSED;
		if ((state & ACC.STATE_FOCUSABLE) != 0) osState |= COM.STATE_SYSTEM_FOCUSABLE;
		if ((state & ACC.STATE_PRESSED) != 0) osState |= COM.STATE_SYSTEM_PRESSED;
		if ((state & ACC.STATE_CHECKED) != 0) osState |= COM.STATE_SYSTEM_CHECKED;
		if ((state & ACC.STATE_EXPANDED) != 0) osState |= COM.STATE_SYSTEM_EXPANDED;
		if ((state & ACC.STATE_COLLAPSED) != 0) osState |= COM.STATE_SYSTEM_COLLAPSED;
		if ((state & ACC.STATE_HOTTRACKED) != 0) osState |= COM.STATE_SYSTEM_HOTTRACKED;
		if ((state & ACC.STATE_BUSY) != 0) osState |= COM.STATE_SYSTEM_BUSY;
		if ((state & ACC.STATE_READONLY) != 0) osState |= COM.STATE_SYSTEM_READONLY;
		if ((state & ACC.STATE_INVISIBLE) != 0) osState |= COM.STATE_SYSTEM_INVISIBLE;
		if ((state & ACC.STATE_OFFSCREEN) != 0) osState |= COM.STATE_SYSTEM_OFFSCREEN;
		if ((state & ACC.STATE_SIZEABLE) != 0) osState |= COM.STATE_SYSTEM_SIZEABLE;
		if ((state & ACC.STATE_LINKED) != 0) osState |= COM.STATE_SYSTEM_LINKED;
		if ((state & ACC.STATE_DISABLED) != 0) osState |= COM.STATE_SYSTEM_UNAVAILABLE;
		return osState;
	}

	int osToState(int osState) {
		int state = ACC.STATE_NORMAL;
		if ((osState & COM.STATE_SYSTEM_SELECTED) != 0) state |= ACC.STATE_SELECTED;
		if ((osState & COM.STATE_SYSTEM_SELECTABLE) != 0) state |= ACC.STATE_SELECTABLE;
		if ((osState & COM.STATE_SYSTEM_MULTISELECTABLE) != 0) state |= ACC.STATE_MULTISELECTABLE;
		if ((osState & COM.STATE_SYSTEM_FOCUSED) != 0) state |= ACC.STATE_FOCUSED;
		if ((osState & COM.STATE_SYSTEM_FOCUSABLE) != 0) state |= ACC.STATE_FOCUSABLE;
		if ((osState & COM.STATE_SYSTEM_PRESSED) != 0) state |= ACC.STATE_PRESSED;
		if ((osState & COM.STATE_SYSTEM_CHECKED) != 0) state |= ACC.STATE_CHECKED;
		if ((osState & COM.STATE_SYSTEM_EXPANDED) != 0) state |= ACC.STATE_EXPANDED;
		if ((osState & COM.STATE_SYSTEM_COLLAPSED) != 0) state |= ACC.STATE_COLLAPSED;
		if ((osState & COM.STATE_SYSTEM_HOTTRACKED) != 0) state |= ACC.STATE_HOTTRACKED;
		if ((osState & COM.STATE_SYSTEM_BUSY) != 0) state |= ACC.STATE_BUSY;
		if ((osState & COM.STATE_SYSTEM_READONLY) != 0) state |= ACC.STATE_READONLY;
		if ((osState & COM.STATE_SYSTEM_INVISIBLE) != 0) state |= ACC.STATE_INVISIBLE;
		if ((osState & COM.STATE_SYSTEM_OFFSCREEN) != 0) state |= ACC.STATE_OFFSCREEN;
		if ((osState & COM.STATE_SYSTEM_SIZEABLE) != 0) state |= ACC.STATE_SIZEABLE;
		if ((osState & COM.STATE_SYSTEM_LINKED) != 0) state |= ACC.STATE_LINKED;
		if ((osState & COM.STATE_SYSTEM_UNAVAILABLE) != 0) state |= ACC.STATE_DISABLED;
		return state;
	}

	int roleToOs(int role) {
		switch (role) {
			case ACC.ROLE_CLIENT_AREA: return COM.ROLE_SYSTEM_CLIENT;
			case ACC.ROLE_WINDOW: return COM.ROLE_SYSTEM_WINDOW;
			case ACC.ROLE_MENUBAR: return COM.ROLE_SYSTEM_MENUBAR;
			case ACC.ROLE_MENU: return COM.ROLE_SYSTEM_MENUPOPUP;
			case ACC.ROLE_MENUITEM: return COM.ROLE_SYSTEM_MENUITEM;
			case ACC.ROLE_SEPARATOR: return COM.ROLE_SYSTEM_SEPARATOR;
			case ACC.ROLE_TOOLTIP: return COM.ROLE_SYSTEM_TOOLTIP;
			case ACC.ROLE_SCROLLBAR: return COM.ROLE_SYSTEM_SCROLLBAR;
			case ACC.ROLE_DIALOG: return COM.ROLE_SYSTEM_DIALOG;
			case ACC.ROLE_LABEL: return COM.ROLE_SYSTEM_STATICTEXT;
			case ACC.ROLE_PUSHBUTTON: return COM.ROLE_SYSTEM_PUSHBUTTON;
			case ACC.ROLE_CHECKBUTTON: return COM.ROLE_SYSTEM_CHECKBUTTON;
			case ACC.ROLE_RADIOBUTTON: return COM.ROLE_SYSTEM_RADIOBUTTON;
			case ACC.ROLE_SPLITBUTTON: return COM.ROLE_SYSTEM_SPLITBUTTON;
			case ACC.ROLE_COMBOBOX: return COM.ROLE_SYSTEM_COMBOBOX;
			case ACC.ROLE_TEXT: return COM.ROLE_SYSTEM_TEXT;
			case ACC.ROLE_TOOLBAR: return COM.ROLE_SYSTEM_TOOLBAR;
			case ACC.ROLE_LIST: return COM.ROLE_SYSTEM_LIST;
			case ACC.ROLE_LISTITEM: return COM.ROLE_SYSTEM_LISTITEM;
			case ACC.ROLE_TABLE: return COM.ROLE_SYSTEM_TABLE;
			case ACC.ROLE_TABLECELL: return COM.ROLE_SYSTEM_CELL;
			case ACC.ROLE_TABLECOLUMNHEADER: return COM.ROLE_SYSTEM_COLUMNHEADER;
			case ACC.ROLE_TABLEROWHEADER: return COM.ROLE_SYSTEM_ROWHEADER;
			case ACC.ROLE_TREE: return COM.ROLE_SYSTEM_OUTLINE;
			case ACC.ROLE_TREEITEM: return COM.ROLE_SYSTEM_OUTLINEITEM;
			case ACC.ROLE_TABFOLDER: return COM.ROLE_SYSTEM_PAGETABLIST;
			case ACC.ROLE_TABITEM: return COM.ROLE_SYSTEM_PAGETAB;
			case ACC.ROLE_PROGRESSBAR: return COM.ROLE_SYSTEM_PROGRESSBAR;
			case ACC.ROLE_SLIDER: return COM.ROLE_SYSTEM_SLIDER;
			case ACC.ROLE_LINK: return COM.ROLE_SYSTEM_LINK;
			case ACC.ROLE_ALERT: return COM.ROLE_SYSTEM_ALERT;
			case ACC.ROLE_ANIMATION: return COM.ROLE_SYSTEM_ANIMATION;
			case ACC.ROLE_COLUMN: return COM.ROLE_SYSTEM_COLUMN;
			case ACC.ROLE_DOCUMENT: return COM.ROLE_SYSTEM_DOCUMENT;
			case ACC.ROLE_GRAPHIC: return COM.ROLE_SYSTEM_GRAPHIC;
			case ACC.ROLE_GROUP: return COM.ROLE_SYSTEM_GROUPING;
			case ACC.ROLE_ROW: return COM.ROLE_SYSTEM_ROW;
			case ACC.ROLE_SPINBUTTON: return COM.ROLE_SYSTEM_SPINBUTTON;
			case ACC.ROLE_STATUSBAR: return COM.ROLE_SYSTEM_STATUSBAR;
			case ACC.ROLE_CLOCK: return COM.ROLE_SYSTEM_CLOCK;
			case ACC.ROLE_CALENDAR: return COM.ROLE_SYSTEM_DROPLIST;

			/* The rest are IA2 roles, so return the closest match. */
			case ACC.ROLE_CANVAS: return COM.ROLE_SYSTEM_CLIENT;
			case ACC.ROLE_CHECKMENUITEM: return COM.ROLE_SYSTEM_MENUITEM;
			case ACC.ROLE_RADIOMENUITEM: return COM.ROLE_SYSTEM_MENUITEM;
			case ACC.ROLE_DATETIME: return COM.ROLE_SYSTEM_DROPLIST;
			case ACC.ROLE_FOOTER: return COM.ROLE_SYSTEM_CLIENT;
			case ACC.ROLE_FORM: return COM.ROLE_SYSTEM_CLIENT;
			case ACC.ROLE_HEADER: return COM.ROLE_SYSTEM_CLIENT;
			case ACC.ROLE_HEADING: return COM.ROLE_SYSTEM_CLIENT;
			case ACC.ROLE_PAGE: return COM.ROLE_SYSTEM_CLIENT;
			case ACC.ROLE_PARAGRAPH: return COM.ROLE_SYSTEM_CLIENT;
			case ACC.ROLE_SECTION: return COM.ROLE_SYSTEM_CLIENT;
		}
		return COM.ROLE_SYSTEM_CLIENT;
	}

	int osToRole(int osRole) {
		switch (osRole) {
			case COM.ROLE_SYSTEM_CLIENT: return ACC.ROLE_CLIENT_AREA;
			case COM.ROLE_SYSTEM_WINDOW: return ACC.ROLE_WINDOW;
			case COM.ROLE_SYSTEM_MENUBAR: return ACC.ROLE_MENUBAR;
			case COM.ROLE_SYSTEM_MENUPOPUP: return ACC.ROLE_MENU;
			case COM.ROLE_SYSTEM_MENUITEM: return ACC.ROLE_MENUITEM;
			case COM.ROLE_SYSTEM_SEPARATOR: return ACC.ROLE_SEPARATOR;
			case COM.ROLE_SYSTEM_TOOLTIP: return ACC.ROLE_TOOLTIP;
			case COM.ROLE_SYSTEM_SCROLLBAR: return ACC.ROLE_SCROLLBAR;
			case COM.ROLE_SYSTEM_DIALOG: return ACC.ROLE_DIALOG;
			case COM.ROLE_SYSTEM_STATICTEXT: return ACC.ROLE_LABEL;
			case COM.ROLE_SYSTEM_PUSHBUTTON: return ACC.ROLE_PUSHBUTTON;
			case COM.ROLE_SYSTEM_CHECKBUTTON: return ACC.ROLE_CHECKBUTTON;
			case COM.ROLE_SYSTEM_RADIOBUTTON: return ACC.ROLE_RADIOBUTTON;
			case COM.ROLE_SYSTEM_SPLITBUTTON: return ACC.ROLE_SPLITBUTTON;
			case COM.ROLE_SYSTEM_COMBOBOX: return ACC.ROLE_COMBOBOX;
			case COM.ROLE_SYSTEM_TEXT: return ACC.ROLE_TEXT;
			case COM.ROLE_SYSTEM_TOOLBAR: return ACC.ROLE_TOOLBAR;
			case COM.ROLE_SYSTEM_LIST: return ACC.ROLE_LIST;
			case COM.ROLE_SYSTEM_LISTITEM: return ACC.ROLE_LISTITEM;
			case COM.ROLE_SYSTEM_TABLE: return ACC.ROLE_TABLE;
			case COM.ROLE_SYSTEM_CELL: return ACC.ROLE_TABLECELL;
			case COM.ROLE_SYSTEM_COLUMNHEADER: return ACC.ROLE_TABLECOLUMNHEADER;
			case COM.ROLE_SYSTEM_ROWHEADER: return ACC.ROLE_TABLEROWHEADER;
			case COM.ROLE_SYSTEM_OUTLINE: return ACC.ROLE_TREE;
			case COM.ROLE_SYSTEM_OUTLINEITEM: return ACC.ROLE_TREEITEM;
			case COM.ROLE_SYSTEM_PAGETABLIST: return ACC.ROLE_TABFOLDER;
			case COM.ROLE_SYSTEM_PAGETAB: return ACC.ROLE_TABITEM;
			case COM.ROLE_SYSTEM_PROGRESSBAR: return ACC.ROLE_PROGRESSBAR;
			case COM.ROLE_SYSTEM_SLIDER: return ACC.ROLE_SLIDER;
			case COM.ROLE_SYSTEM_LINK: return ACC.ROLE_LINK;
			case COM.ROLE_SYSTEM_ALERT: return ACC.ROLE_ALERT;
			case COM.ROLE_SYSTEM_ANIMATION: return ACC.ROLE_ANIMATION;
			case COM.ROLE_SYSTEM_COLUMN: return ACC.ROLE_COLUMN;
			case COM.ROLE_SYSTEM_DOCUMENT: return ACC.ROLE_DOCUMENT;
			case COM.ROLE_SYSTEM_GRAPHIC: return ACC.ROLE_GRAPHIC;
			case COM.ROLE_SYSTEM_GROUPING: return ACC.ROLE_GROUP;
			case COM.ROLE_SYSTEM_ROW: return ACC.ROLE_ROW;
			case COM.ROLE_SYSTEM_SPINBUTTON: return ACC.ROLE_SPINBUTTON;
			case COM.ROLE_SYSTEM_STATUSBAR: return ACC.ROLE_STATUSBAR;
			case COM.ROLE_SYSTEM_CLOCK: return ACC.ROLE_CLOCK;
			case COM.ROLE_SYSTEM_DROPLIST: return ACC.ROLE_CALENDAR;
		}
		return ACC.ROLE_CLIENT_AREA;
	}

	/*
	 * Return a Color given a string of the form "rgb(n,n,n)".
	 */
	Color colorFromString(String rgbString) {
		try {
			int open = rgbString.indexOf('(');
			int comma1 = rgbString.indexOf(',');
			int comma2 = rgbString.indexOf(',', comma1 + 1);
			int close = rgbString.indexOf(')');
			int r = Integer.parseInt(rgbString.substring(open + 1, comma1));
			int g = Integer.parseInt(rgbString.substring(comma1 + 1, comma2));
			int b = Integer.parseInt(rgbString.substring(comma2 + 1, close));
			return new Color(control.getDisplay(), r, g, b);
		} catch (NumberFormatException ex) {}
		return null;
	}

	int getCaretOffset() {
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.offset = -1;
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextListener listener = accessibleTextExtendedListeners.get(i);
			listener.getCaretOffset (event);
		}
		if (event.offset == -1) {
			for (int i = 0; i < accessibleTextListenersSize(); i++) {
				event.childID = ACC.CHILDID_SELF;
				AccessibleTextListener listener = accessibleTextListeners.get(i);
				listener.getCaretOffset (event);
			}
		}
		return event.offset;
	}

	int getCharacterCount() {
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.count = -1;
		for (int i = 0; i < accessibleTextExtendedListenersSize(); i++) {
			AccessibleTextExtendedListener listener = accessibleTextExtendedListeners.get(i);
			listener.getCharacterCount(event);
		}
		if (event.count == -1) {
			AccessibleControlEvent e = new AccessibleControlEvent(this);
			e.childID = ACC.CHILDID_SELF;
			for (int i = 0; i < accessibleControlListenersSize(); i++) {
				AccessibleControlListener listener = accessibleControlListeners.get(i);
				listener.getRole(e);
				listener.getValue(e);
			}
			// TODO: Consider passing the value through for other roles as well (i.e. combo, etc). Keep in sync with get_text.
			event.count = e.detail == ACC.ROLE_TEXT && e.result != null ? e.result.length() : 0;
		}
		return event.count;
	}

	int getRelationCount() {
		int count = 0;
		for (int type = 0; type < MAX_RELATION_TYPES; type++) {
			if (relations[type] != null) count++;
		}
		return count;
	}

	int getRole() {
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = ACC.CHILDID_SELF;
		for (int i = 0; i < accessibleControlListenersSize(); i++) {
			AccessibleControlListener listener = accessibleControlListeners.get(i);
			listener.getRole(event);
		}
		return event.detail;
	}

	int getDefaultRole() {
		int role;
		role = COM.ROLE_SYSTEM_CLIENT;
		if (iaccessible != null) {
			/* Get the default role from the OS. */
			long varChild = OS.GlobalAlloc (OS.GMEM_FIXED | OS.GMEM_ZEROINIT, VARIANT.sizeof);
			setIntVARIANT(varChild, COM.VT_I4, COM.CHILDID_SELF);
			long pvarRole = OS.GlobalAlloc (OS.GMEM_FIXED | OS.GMEM_ZEROINIT, VARIANT.sizeof);
			int code = iaccessible.get_accRole(varChild, pvarRole);
			if (code == COM.S_OK) {
				VARIANT v = getVARIANT(pvarRole);
				if (v.vt == COM.VT_I4) role = v.lVal;
			}
			OS.GlobalFree(varChild);
			OS.GlobalFree(pvarRole);
		}
		return role;
	}

	String getString(long psz) {
		long [] ptr = new long [1];
		OS.MoveMemory (ptr, psz, C.PTR_SIZEOF);
		int size = COM.SysStringByteLen(ptr [0]);
		if (size == 0) return "";
		char [] buffer = new char [(size + 1) / 2];
		OS.MoveMemory (buffer, ptr [0], size);
		return new String (buffer);
	}

	VARIANT getVARIANT(long variant) {
		VARIANT v = new VARIANT();
		COM.MoveMemory(v, variant, VARIANT.sizeof);
		return v;
	}

	Number getNumberVARIANT(long variant) {
		VARIANT v = new VARIANT();
		COM.MoveMemory(v, variant, VARIANT.sizeof);
		if (v.vt == COM.VT_I8) return Long.valueOf(v.lVal); // TODO: Fix this - v.lVal is an int - don't use struct
		return Integer.valueOf(v.lVal);
	}

	void setIntVARIANT(long variant, short vt, int lVal) {
		if (vt == COM.VT_I4 || vt == COM.VT_EMPTY) {
			OS.MoveMemory(variant, new short[] { vt }, 2);
			OS.MoveMemory(variant + 8, new int[] { lVal }, 4);
		}
	}

	void setPtrVARIANT(long variant, short vt, long lVal) {
		if (vt == COM.VT_DISPATCH || vt == COM.VT_UNKNOWN) {
			OS.MoveMemory(variant, new short[] { vt }, 2);
			OS.MoveMemory(variant + 8, new long [] { lVal }, C.PTR_SIZEOF);
		}
	}

	void setNumberVARIANT(long variant, Number number) {
		if (number == null) {
			OS.MoveMemory(variant, new short[] { COM.VT_EMPTY }, 2);
			OS.MoveMemory(variant + 8, new int[] { 0 }, 4);
		} else if (number instanceof Double) {
			OS.MoveMemory(variant, new short[] { COM.VT_R8 }, 2);
			OS.MoveMemory(variant + 8, new double[] { number.doubleValue() }, 8);
		} else if (number instanceof Float) {
			OS.MoveMemory(variant, new short[] { COM.VT_R4 }, 2);
			OS.MoveMemory(variant + 8, new float[] { number.floatValue() }, 4);
		} else if (number instanceof Long) {
			OS.MoveMemory(variant, new short[] { COM.VT_I8 }, 2);
			OS.MoveMemory(variant + 8, new long[] { number.longValue() }, 8);
		} else {
			OS.MoveMemory(variant, new short[] { COM.VT_I4 }, 2);
			OS.MoveMemory(variant + 8, new int[] { number.intValue() }, 4);
		}
	}

	void setString(long psz, String string) {
		long ptr = 0;
		if (string != null) {
			char[] data = (string + "\0").toCharArray();
			ptr = COM.SysAllocString(data);
		}
		OS.MoveMemory(psz, new long [] { ptr }, C.PTR_SIZEOF);
	}

	void setStringVARIANT(long variant, String string) {
		long ptr = 0;
		if (string != null) {
			char[] data = (string + "\0").toCharArray();
			ptr = COM.SysAllocString(data);
		}
		OS.MoveMemory(variant, new short[] { ptr == 0 ? COM.VT_EMPTY : COM.VT_BSTR }, 2);
		OS.MoveMemory(variant + 8, new long [] { ptr }, C.PTR_SIZEOF);
	}

	/* checkWidget was copied from Widget, and rewritten to work in this package */
	void checkWidget () {
		if (!isValidThread ()) SWT.error (SWT.ERROR_THREAD_INVALID_ACCESS);
		if (control.isDisposed ()) SWT.error (SWT.ERROR_WIDGET_DISPOSED);
	}

	boolean isATRunning () {
		/*
		 * Currently there is no accurate way to check if AT is running from 'refCount'.
		 * JAWS screen reader cannot be detected using 'refCount' approach,
		 * because 'refCount' continues to be 1 even when JAWS is running.
		 */
		// if (refCount <= 1) return false;
		return true;
	}

	/* isValidThread was copied from Widget, and rewritten to work in this package */
	boolean isValidThread () {
		return control.getDisplay ().getThread () == Thread.currentThread ();
	}

	// START DEBUG CODE
	static void print (String str) {
		if (DEBUG) System.out.println (str);
	}
	String getRoleString(int role) {
		if (DEBUG) switch (role) {
			case COM.ROLE_SYSTEM_CLIENT: return "ROLE_SYSTEM_CLIENT";
			case COM.ROLE_SYSTEM_WINDOW: return "ROLE_SYSTEM_WINDOW";
			case COM.ROLE_SYSTEM_MENUBAR: return "ROLE_SYSTEM_MENUBAR";
			case COM.ROLE_SYSTEM_MENUPOPUP: return "ROLE_SYSTEM_MENUPOPUP";
			case COM.ROLE_SYSTEM_MENUITEM: return "ROLE_SYSTEM_MENUITEM";
			case COM.ROLE_SYSTEM_SEPARATOR: return "ROLE_SYSTEM_SEPARATOR";
			case COM.ROLE_SYSTEM_TOOLTIP: return "ROLE_SYSTEM_TOOLTIP";
			case COM.ROLE_SYSTEM_SCROLLBAR: return "ROLE_SYSTEM_SCROLLBAR";
			case COM.ROLE_SYSTEM_DIALOG: return "ROLE_SYSTEM_DIALOG";
			case COM.ROLE_SYSTEM_STATICTEXT: return "ROLE_SYSTEM_STATICTEXT";
			case COM.ROLE_SYSTEM_PUSHBUTTON: return "ROLE_SYSTEM_PUSHBUTTON";
			case COM.ROLE_SYSTEM_CHECKBUTTON: return "ROLE_SYSTEM_CHECKBUTTON";
			case COM.ROLE_SYSTEM_RADIOBUTTON: return "ROLE_SYSTEM_RADIOBUTTON";
			case COM.ROLE_SYSTEM_SPLITBUTTON: return "ROLE_SYSTEM_SPLITBUTTON";
			case COM.ROLE_SYSTEM_COMBOBOX: return "ROLE_SYSTEM_COMBOBOX";
			case COM.ROLE_SYSTEM_TEXT: return "ROLE_SYSTEM_TEXT";
			case COM.ROLE_SYSTEM_TOOLBAR: return "ROLE_SYSTEM_TOOLBAR";
			case COM.ROLE_SYSTEM_LIST: return "ROLE_SYSTEM_LIST";
			case COM.ROLE_SYSTEM_LISTITEM: return "ROLE_SYSTEM_LISTITEM";
			case COM.ROLE_SYSTEM_TABLE: return "ROLE_SYSTEM_TABLE";
			case COM.ROLE_SYSTEM_CELL: return "ROLE_SYSTEM_CELL";
			case COM.ROLE_SYSTEM_COLUMNHEADER: return "ROLE_SYSTEM_COLUMNHEADER";
			case COM.ROLE_SYSTEM_ROWHEADER: return "ROLE_SYSTEM_ROWHEADER";
			case COM.ROLE_SYSTEM_OUTLINE: return "ROLE_SYSTEM_OUTLINE";
			case COM.ROLE_SYSTEM_OUTLINEITEM: return "ROLE_SYSTEM_OUTLINEITEM";
			case COM.ROLE_SYSTEM_PAGETABLIST: return "ROLE_SYSTEM_PAGETABLIST";
			case COM.ROLE_SYSTEM_PAGETAB: return "ROLE_SYSTEM_PAGETAB";
			case COM.ROLE_SYSTEM_PROGRESSBAR: return "ROLE_SYSTEM_PROGRESSBAR";
			case COM.ROLE_SYSTEM_SLIDER: return "ROLE_SYSTEM_SLIDER";
			case COM.ROLE_SYSTEM_LINK: return "ROLE_SYSTEM_LINK";
			case COM.ROLE_SYSTEM_ALERT: return "ROLE_SYSTEM_ALERT";
			case COM.ROLE_SYSTEM_ANIMATION: return "ROLE_SYSTEM_ANIMATION";
			case COM.ROLE_SYSTEM_COLUMN: return "ROLE_SYSTEM_COLUMN";
			case COM.ROLE_SYSTEM_DOCUMENT: return "ROLE_SYSTEM_DOCUMENT";
			case COM.ROLE_SYSTEM_GRAPHIC: return "ROLE_SYSTEM_GRAPHIC";
			case COM.ROLE_SYSTEM_GROUPING: return "ROLE_SYSTEM_GROUPING";
			case COM.ROLE_SYSTEM_ROW: return "ROLE_SYSTEM_ROW";
			case COM.ROLE_SYSTEM_SPINBUTTON: return "ROLE_SYSTEM_SPINBUTTON";
			case COM.ROLE_SYSTEM_STATUSBAR: return "ROLE_SYSTEM_STATUSBAR";
			case COM.ROLE_SYSTEM_CLOCK: return "ROLE_SYSTEM_CLOCK";
			case COM.ROLE_SYSTEM_DROPLIST: return "ROLE_SYSTEM_DROPLIST";
			// IA2 roles
			case ACC.ROLE_CANVAS: return "IA2_ROLE_CANVAS";
			case ACC.ROLE_CHECKMENUITEM: return "IA2_ROLE_CHECKMENUITEM";
			case ACC.ROLE_RADIOMENUITEM: return "IA2_ROLE_RADIOMENUITEM";
			case ACC.ROLE_DATETIME: return "IA2_ROLE_DATETIME";
			case ACC.ROLE_FOOTER: return "IA2_ROLE_FOOTER";
			case ACC.ROLE_FORM: return "IA2_ROLE_FORM";
			case ACC.ROLE_HEADER: return "IA2_ROLE_HEADER";
			case ACC.ROLE_HEADING: return "IA2_ROLE_HEADING";
			case ACC.ROLE_PAGE: return "IA2_ROLE_PAGE";
			case ACC.ROLE_PARAGRAPH: return "IA2_ROLE_PARAGRAPH";
			case ACC.ROLE_SECTION: return "IA2_ROLE_SECTION";
		}
		return "Unknown role (" + role + ")";
	}
	String getStateString(int state) {
		if (state == 0) return " no state bits set";
		StringBuilder stateString = new StringBuilder();
		if (DEBUG) {
		if ((state & COM.STATE_SYSTEM_SELECTED) != 0) stateString.append(" STATE_SYSTEM_SELECTED");
		if ((state & COM.STATE_SYSTEM_SELECTABLE) != 0) stateString.append(" STATE_SYSTEM_SELECTABLE");
		if ((state & COM.STATE_SYSTEM_MULTISELECTABLE) != 0) stateString.append(" STATE_SYSTEM_MULTISELECTABLE");
		if ((state & COM.STATE_SYSTEM_FOCUSED) != 0) stateString.append(" STATE_SYSTEM_FOCUSED");
		if ((state & COM.STATE_SYSTEM_FOCUSABLE) != 0) stateString.append(" STATE_SYSTEM_FOCUSABLE");
		if ((state & COM.STATE_SYSTEM_PRESSED) != 0) stateString.append(" STATE_SYSTEM_PRESSED");
		if ((state & COM.STATE_SYSTEM_CHECKED) != 0) stateString.append(" STATE_SYSTEM_CHECKED");
		if ((state & COM.STATE_SYSTEM_EXPANDED) != 0) stateString.append(" STATE_SYSTEM_EXPANDED");
		if ((state & COM.STATE_SYSTEM_COLLAPSED) != 0) stateString.append(" STATE_SYSTEM_COLLAPSED");
		if ((state & COM.STATE_SYSTEM_HOTTRACKED) != 0) stateString.append(" STATE_SYSTEM_HOTTRACKED");
		if ((state & COM.STATE_SYSTEM_BUSY) != 0) stateString.append(" STATE_SYSTEM_BUSY");
		if ((state & COM.STATE_SYSTEM_READONLY) != 0) stateString.append(" STATE_SYSTEM_READONLY");
		if ((state & COM.STATE_SYSTEM_INVISIBLE) != 0) stateString.append(" STATE_SYSTEM_INVISIBLE");
		if ((state & COM.STATE_SYSTEM_OFFSCREEN) != 0) stateString.append(" STATE_SYSTEM_OFFSCREEN");
		if ((state & COM.STATE_SYSTEM_SIZEABLE) != 0) stateString.append(" STATE_SYSTEM_SIZEABLE");
		if ((state & COM.STATE_SYSTEM_LINKED) != 0) stateString.append(" STATE_SYSTEM_LINKED");
		if ((state & COM.STATE_SYSTEM_UNAVAILABLE) != 0) stateString.append(" STATE_SYSTEM_UNAVAILABLE");
		if (stateString.length() == 0) stateString.append(" Unknown state[s] (" + Integer.toHexString(state) + ")");
		}
		return stateString.toString();
	}
	String getIA2StatesString(int ia2States) {
		if (ia2States == 0) return " no state bits set";
		StringBuilder stateString = new StringBuilder();
		if (DEBUG) {
		if ((ia2States & COM.IA2_STATE_ACTIVE) != 0) stateString.append(" IA2_STATE_ACTIVE");
		if ((ia2States & COM.IA2_STATE_EDITABLE) != 0) stateString.append(" IA2_STATE_EDITABLE");
		if ((ia2States & COM.IA2_STATE_SINGLE_LINE) != 0) stateString.append(" IA2_STATE_SINGLE_LINE");
		if ((ia2States & COM.IA2_STATE_MULTI_LINE) != 0) stateString.append(" IA2_STATE_MULTI_LINE");
		if ((ia2States & COM.IA2_STATE_REQUIRED) != 0) stateString.append(" IA2_STATE_REQUIRED");
		if ((ia2States & COM.IA2_STATE_INVALID_ENTRY) != 0) stateString.append(" IA2_STATE_INVALID_ENTRY");
		if ((ia2States & COM.IA2_STATE_SUPPORTS_AUTOCOMPLETION) != 0) stateString.append(" IA2_STATE_SUPPORTS_AUTOCOMPLETION");
		if (stateString.length() == 0) stateString.append(" Unknown IA2 state[s] (" + ia2States + ")");
		}
		return stateString.toString();
	}
	String getEventString(int event) {
		if (DEBUG) switch (event) {
			case ACC.EVENT_TABLE_CHANGED: return "IA2_EVENT_TABLE_CHANGED";
			case ACC.EVENT_TEXT_CHANGED: return "IA2_EVENT_TEXT_REMOVED or IA2_EVENT_TEXT_INSERTED";
			case ACC.EVENT_HYPERTEXT_LINK_SELECTED: return "IA2_EVENT_HYPERTEXT_LINK_SELECTED";
			case ACC.EVENT_VALUE_CHANGED: return "EVENT_OBJECT_VALUECHANGE";
			case ACC.EVENT_STATE_CHANGED: return "EVENT_OBJECT_STATECHANGE";
			case ACC.EVENT_SELECTION_CHANGED: return "EVENT_OBJECT_SELECTIONWITHIN";
			case ACC.EVENT_TEXT_SELECTION_CHANGED: return "EVENT_OBJECT_TEXTSELECTIONCHANGED";
			case ACC.EVENT_LOCATION_CHANGED: return "EVENT_OBJECT_LOCATIONCHANGE";
			case ACC.EVENT_NAME_CHANGED: return "EVENT_OBJECT_NAMECHANGE";
			case ACC.EVENT_DESCRIPTION_CHANGED: return "EVENT_OBJECT_DESCRIPTIONCHANGE";
			case ACC.EVENT_DOCUMENT_LOAD_COMPLETE: return "IA2_EVENT_DOCUMENT_LOAD_COMPLETE";
			case ACC.EVENT_DOCUMENT_LOAD_STOPPED: return "IA2_EVENT_DOCUMENT_LOAD_STOPPED";
			case ACC.EVENT_DOCUMENT_RELOAD: return "IA2_EVENT_DOCUMENT_RELOAD";
			case ACC.EVENT_PAGE_CHANGED: return "IA2_EVENT_PAGE_CHANGED";
			case ACC.EVENT_SECTION_CHANGED: return "IA2_EVENT_SECTION_CHANGED";
			case ACC.EVENT_ACTION_CHANGED: return "IA2_EVENT_ACTION_CHANGED";
			case ACC.EVENT_HYPERLINK_START_INDEX_CHANGED: return "IA2_EVENT_HYPERLINK_START_INDEX_CHANGED";
			case ACC.EVENT_HYPERLINK_END_INDEX_CHANGED: return "IA2_EVENT_HYPERLINK_END_INDEX_CHANGED";
			case ACC.EVENT_HYPERLINK_ANCHOR_COUNT_CHANGED: return "IA2_EVENT_HYPERLINK_ANCHOR_COUNT_CHANGED";
			case ACC.EVENT_HYPERLINK_SELECTED_LINK_CHANGED: return "IA2_EVENT_HYPERLINK_SELECTED_LINK_CHANGED";
			case ACC.EVENT_HYPERLINK_ACTIVATED: return "IA2_EVENT_HYPERLINK_ACTIVATED";
			case ACC.EVENT_HYPERTEXT_LINK_COUNT_CHANGED: return "IA2_EVENT_HYPERTEXT_LINK_COUNT_CHANGED";
			case ACC.EVENT_ATTRIBUTE_CHANGED: return "IA2_EVENT_ATTRIBUTE_CHANGED";
			case ACC.EVENT_TABLE_CAPTION_CHANGED: return "IA2_EVENT_TABLE_CAPTION_CHANGED";
			case ACC.EVENT_TABLE_COLUMN_DESCRIPTION_CHANGED: return "IA2_EVENT_TABLE_COLUMN_DESCRIPTION_CHANGED";
			case ACC.EVENT_TABLE_COLUMN_HEADER_CHANGED: return "IA2_EVENT_TABLE_COLUMN_HEADER_CHANGED";
			case ACC.EVENT_TABLE_ROW_DESCRIPTION_CHANGED: return "IA2_EVENT_TABLE_ROW_DESCRIPTION_CHANGED";
			case ACC.EVENT_TABLE_ROW_HEADER_CHANGED: return "IA2_EVENT_TABLE_ROW_HEADER_CHANGED";
			case ACC.EVENT_TABLE_SUMMARY_CHANGED: return "IA2_EVENT_TABLE_SUMMARY_CHANGED";
			case ACC.EVENT_TEXT_ATTRIBUTE_CHANGED: return "IA2_EVENT_TEXT_ATTRIBUTE_CHANGED";
			case ACC.EVENT_TEXT_CARET_MOVED: return "IA2_EVENT_TEXT_CARET_MOVED";
			case ACC.EVENT_TEXT_COLUMN_CHANGED: return "IA2_EVENT_TEXT_COLUMN_CHANGED";
		}
		return "Unknown event (" + event + ")";
	}
	private String hresult(int code) {
		if (DEBUG) switch (code) {
			case COM.S_OK: return " S_OK";
			case COM.S_FALSE: return " S_FALSE";
			case COM.E_ACCESSDENIED: return " E_ACCESSDENIED";
			case COM.E_FAIL: return " E_FAIL";
			case COM.E_INVALIDARG: return " E_INVALIDARG";
			case COM.E_NOINTERFACE: return " E_NOINTERFACE";
			case COM.E_NOTIMPL: return " E_NOTIMPL";
			case COM.E_NOTSUPPORTED: return " E_NOTSUPPORTED";
			case COM.E_OUTOFMEMORY: return " E_OUTOFMEMORY";
			case OS.E_POINTER: return " E_POINTER";
			case COM.DISP_E_EXCEPTION: return " DISP_E_EXCEPTION";
			case COM.DISP_E_MEMBERNOTFOUND: return " DISP_E_MEMBERNOTFOUND";
			case COM.DISP_E_UNKNOWNINTERFACE: return " DISP_E_UNKNOWNINTERFACE";
			case COM.DISP_E_UNKNOWNNAME: return " DISP_E_UNKNOWNNAME";
		}
		return " HRESULT=" + code;
	}
	boolean interesting(GUID guid) {
		if (DEBUG) {
		if (COM.IsEqualGUID(guid, COM.IIDIUnknown)) return true;
		if (COM.IsEqualGUID(guid, COM.IIDIAccessible)) return true;
		if (COM.IsEqualGUID(guid, COM.IIDIEnumVARIANT)) return true;
		if (COM.IsEqualGUID(guid, COM.IIDIServiceProvider)) return true;
		if (COM.IsEqualGUID(guid, COM.IIDIAccessible2)) return true;
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleRelation)) return true;
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleAction)) return true;
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleComponent)) return true;
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleValue)) return true;
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleText)) return true;
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleEditableText)) return true;
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleHyperlink)) return true;
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleHypertext)) return true;
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleTable)) return true;
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleTable2)) return true;
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleTableCell)) return true;
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleImage)) return true;
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleApplication)) return true;
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleContext)) return true;
		}
		return false;
	}
	String guidString(GUID guid) {
		if (DEBUG) {
		final GUID IIDIAccessibleHandler = IIDFromString("{03022430-ABC4-11D0-BDE2-00AA001A1953}"); //$NON-NLS-1$
		final GUID IIDIAccessor = IIDFromString("{0C733A8C-2A1C-11CE-ADE5-00AA0044773D}"); //$NON-NLS-1$
		final GUID IIDIAdviseSink2 = IIDFromString("{00000125-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIBindCtx = IIDFromString("{0000000E-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDICreateErrorInfo = IIDFromString("{22F03340-547D-101B-8E65-08002B2BD119}"); //$NON-NLS-1$
		final GUID IIDICreateTypeInfo = IIDFromString("{00020405-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDICreateTypeLib = IIDFromString("{00020406-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIDataAdviseHolder = IIDFromString("{00000110-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIEnumConnectionPoints = IIDFromString("{B196B285-BAB4-101A-B69C-00AA00341D07}"); //$NON-NLS-1$
		final GUID IIDIEnumConnections = IIDFromString("{B196B287-BAB4-101A-B69C-00AA00341D07}"); //$NON-NLS-1$
		final GUID IIDIEnumMoniker = IIDFromString("{00000102-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIEnumOLEVERB = IIDFromString("{00000104-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIEnumSTATDATA = IIDFromString("{00000105-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIEnumSTATSTG = IIDFromString("{0000000D-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIEnumString = IIDFromString("{00000101-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIEnumUnknown = IIDFromString("{00000100-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIErrorInfo = IIDFromString("{1CF2B120-547D-101B-8E65-08002B2BD119}"); //$NON-NLS-1$
		final GUID IIDIErrorLog = IIDFromString("{3127CA40-446E-11CE-8135-00AA004BB851}"); //$NON-NLS-1$
		final GUID IIDIExternalConnection = IIDFromString("{00000019-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIFontDisp = IIDFromString("{BEF6E003-A874-101A-8BBA-00AA00300CAB}"); //$NON-NLS-1$
		final GUID IIDILockBytes = IIDFromString("{0000000A-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIMalloc = IIDFromString("{00000002-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIMallocSpy = IIDFromString("{0000001D-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIMarshal = IIDFromString("{00000003-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIMessageFilter = IIDFromString("{00000016-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIMoniker = IIDFromString("{0000000F-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIOleAdviseHolder = IIDFromString("{00000111-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIOleCache = IIDFromString("{0000011E-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIOleCache2 = IIDFromString("{00000128-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIOleCacheControl = IIDFromString("{00000129-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIOleItemContainer = IIDFromString("{0000011C-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIParseDisplayName = IIDFromString("{0000011A-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIPerPropertyBrowsing = IIDFromString("{376BD3AA-3845-101B-84ED-08002B2EC713}"); //$NON-NLS-1$
		final GUID IIDIPersistMemory = IIDFromString("{BD1AE5E0-A6AE-11CE-BD37-504200C10000}"); //$NON-NLS-1$
		final GUID IIDIPersistPropertyBag = IIDFromString("{37D84F60-42CB-11CE-8135-00AA004BB851}"); //$NON-NLS-1$
		final GUID IIDIPicture = IIDFromString("{7BF80980-BF32-101A-8BBB-00AA00300CAB}"); //$NON-NLS-1$
		final GUID IIDIPictureDisp = IIDFromString("{7BF80981-BF32-101A-8BBB-00AA00300CAB}"); //$NON-NLS-1$
		final GUID IIDIPropertyBag = IIDFromString("{55272A00-42CB-11CE-8135-00AA004BB851}"); //$NON-NLS-1$
		final GUID IIDIPropertyPage = IIDFromString("{B196B28D-BAB4-101A-B69C-00AA00341D07}"); //$NON-NLS-1$
		final GUID IIDIPropertyPage2 = IIDFromString("{01E44665-24AC-101B-84ED-08002B2EC713}"); //$NON-NLS-1$
		final GUID IIDIPropertyPageSite = IIDFromString("{B196B28C-BAB4-101A-B69C-00AA00341D07}"); //$NON-NLS-1$
		final GUID IIDIPSFactoryBuffer = IIDFromString("{D5F569D0-593B-101A-B569-08002B2DBF7A}"); //$NON-NLS-1$
		final GUID IIDIRootStorage = IIDFromString("{00000012-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIROTData = IIDFromString("{F29F6BC0-5021-11CE-AA15-00006901293F}"); //$NON-NLS-1$
		final GUID IIDIRpcChannelBuffer = IIDFromString("{D5F56B60-593B-101A-B569-08002B2DBF7A}"); //$NON-NLS-1$
		final GUID IIDIRpcProxyBuffer = IIDFromString("{D5F56A34-593B-101A-B569-08002B2DBF7A}"); //$NON-NLS-1$
		final GUID IIDIRpcStubBuffer = IIDFromString("{D5F56AFC-593B-101A-B569-08002B2DBF7A}"); //$NON-NLS-1$
		final GUID IIDIRunnableObject = IIDFromString("{00000126-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIRunningObjectTable = IIDFromString("{00000010-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDISimpleFrameSite = IIDFromString("{742B0E01-14E6-101B-914E-00AA00300CAB}"); //$NON-NLS-1$
		final GUID IIDIStdMarshalInfo = IIDFromString("{00000018-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDISupportErrorInfo = IIDFromString("{DF0B3D60-548F-101B-8E65-08002B2BD119}"); //$NON-NLS-1$
		final GUID IIDITypeComp = IIDFromString("{00020403-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDITypeLib = IIDFromString("{00020402-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIViewObject = IIDFromString("{0000010D-0000-0000-C000-000000000046}"); //$NON-NLS-1$
		final GUID IIDIdentityUnmarshal = IIDFromString("{0000001b-0000-0000-c000-000000000046}"); //$NON-NLS-1$
		final GUID IIDInternalMSMarshaller = IIDFromString("{4c1e39e1-e3e3-4296-aa86-ec938d896e92}"); //$NON-NLS-1$
		final GUID IIDIAccIdentity = IIDFromString("{7852B78D-1CFD-41C1-A615-9C0C85960B5F}"); //$NON-NLS-1$
		final GUID IIDIAccPropServer = IIDFromString("{76C0DBBB-15E0-4E7B-B61B-20EEEA2001E0}"); //$NON-NLS-1$
		final GUID IIDIAccPropServices = IIDFromString("{6E26E776-04F0-495D-80E4-3330352E3169}"); //$NON-NLS-1$
		if (COM.IsEqualGUID(guid, COM.IID_IDropTargetHelper)) return "IID_IDropTargetHelper";
		if (COM.IsEqualGUID(guid, COM.IIDJavaBeansBridge)) return "IIDJavaBeansBridge";
		if (COM.IsEqualGUID(guid, COM.IIDShockwaveActiveXControl)) return "IIDShockwaveActiveXControl";
		if (COM.IsEqualGUID(guid, COM.IIDIAccessible)) return "IIDIAccessible";
		if (COM.IsEqualGUID(guid, IIDIAccessibleHandler)) return "IIDIAccessibleHandler";
		if (COM.IsEqualGUID(guid, IIDIAccessor)) return "IIDIAccessor";
		if (COM.IsEqualGUID(guid, COM.IIDIAdviseSink)) return "IIDIAdviseSink";
		if (COM.IsEqualGUID(guid, IIDIAdviseSink2)) return "IIDIAdviseSink2";
		if (COM.IsEqualGUID(guid, IIDIBindCtx)) return "IIDIBindCtx";
		if (COM.IsEqualGUID(guid, COM.IIDIClassFactory)) return "IIDIClassFactory";
		if (COM.IsEqualGUID(guid, COM.IIDIClassFactory2)) return "IIDIClassFactory2";
		if (COM.IsEqualGUID(guid, COM.IIDIConnectionPointContainer)) return "IIDIConnectionPointContainer";
		if (COM.IsEqualGUID(guid, IIDICreateErrorInfo)) return "IIDICreateErrorInfo";
		if (COM.IsEqualGUID(guid, IIDICreateTypeInfo)) return "IIDICreateTypeInfo";
		if (COM.IsEqualGUID(guid, IIDICreateTypeLib)) return "IIDICreateTypeLib";
		if (COM.IsEqualGUID(guid, IIDIDataAdviseHolder)) return "IIDIDataAdviseHolder";
		if (COM.IsEqualGUID(guid, COM.IIDIDataObject)) return "IIDIDataObject";
		if (COM.IsEqualGUID(guid, COM.IIDIDispatch)) return "IIDIDispatch";
		if (COM.IsEqualGUID(guid, COM.IIDIDispatchEx)) return "IIDIDispatchEx";
		if (COM.IsEqualGUID(guid, COM.IIDIDocHostUIHandler)) return "IIDIDocHostUIHandler";
		if (COM.IsEqualGUID(guid, COM.IIDIDocHostShowUI)) return "IIDIDocHostShowUI";
		if (COM.IsEqualGUID(guid, COM.IIDIDropSource)) return "IIDIDropSource";
		if (COM.IsEqualGUID(guid, COM.IIDIDropTarget)) return "IIDIDropTarget";
		if (COM.IsEqualGUID(guid, IIDIEnumConnectionPoints)) return "IIDIEnumConnectionPoints";
		if (COM.IsEqualGUID(guid, IIDIEnumConnections)) return "IIDIEnumConnections";
		if (COM.IsEqualGUID(guid, COM.IIDIEnumFORMATETC)) return "IIDIEnumFORMATETC";
		if (COM.IsEqualGUID(guid, IIDIEnumMoniker)) return "IIDIEnumMoniker";
		if (COM.IsEqualGUID(guid, IIDIEnumOLEVERB)) return "IIDIEnumOLEVERB";
		if (COM.IsEqualGUID(guid, IIDIEnumSTATDATA)) return "IIDIEnumSTATDATA";
		if (COM.IsEqualGUID(guid, IIDIEnumSTATSTG)) return "IIDIEnumSTATSTG";
		if (COM.IsEqualGUID(guid, IIDIEnumString)) return "IIDIEnumString";
		if (COM.IsEqualGUID(guid, IIDIEnumUnknown)) return "IIDIEnumUnknown";
		if (COM.IsEqualGUID(guid, COM.IIDIEnumVARIANT)) return "IIDIEnumVARIANT";
		if (COM.IsEqualGUID(guid, IIDIErrorInfo)) return "IIDIErrorInfo";
		if (COM.IsEqualGUID(guid, IIDIErrorLog)) return "IIDIErrorLog";
		if (COM.IsEqualGUID(guid, IIDIExternalConnection)) return "IIDIExternalConnection";
		if (COM.IsEqualGUID(guid, IIDIFontDisp)) return "IIDIFontDisp";
	//	if (COM.IsEqualGUID(guid, COM.IIDIHTMLDocumentEvents2)) return "IIDIHTMLDocumentEvents2";
		if (COM.IsEqualGUID(guid, COM.IIDIInternetSecurityManager)) return "IIDIInternetSecurityManager";
		if (COM.IsEqualGUID(guid, COM.IIDIAuthenticate)) return "IIDIAuthenticate";
		if (COM.IsEqualGUID(guid, COM.IIDIJScriptTypeInfo)) return "IIDIJScriptTypeInfo";
		if (COM.IsEqualGUID(guid, IIDILockBytes)) return "IIDILockBytes";
		if (COM.IsEqualGUID(guid, IIDIMalloc)) return "IIDIMalloc";
		if (COM.IsEqualGUID(guid, IIDIMallocSpy)) return "IIDIMallocSpy";
		if (COM.IsEqualGUID(guid, IIDIMarshal)) return "IIDIMarshal";
		if (COM.IsEqualGUID(guid, IIDIMessageFilter)) return "IIDIMessageFilter";
		if (COM.IsEqualGUID(guid, IIDIMoniker)) return "IIDIMoniker";
		if (COM.IsEqualGUID(guid, IIDIOleAdviseHolder)) return "IIDIOleAdviseHolder";
		if (COM.IsEqualGUID(guid, IIDIOleCache)) return "IIDIOleCache";
		if (COM.IsEqualGUID(guid, IIDIOleCache2)) return "IIDIOleCache2";
		if (COM.IsEqualGUID(guid, IIDIOleCacheControl)) return "IIDIOleCacheControl";
		if (COM.IsEqualGUID(guid, COM.IIDIOleClientSite)) return "IIDIOleClientSite";
		if (COM.IsEqualGUID(guid, COM.IIDIOleCommandTarget)) return "IIDIOleCommandTarget";
		if (COM.IsEqualGUID(guid, COM.IIDIOleControl)) return "IIDIOleControl";
		if (COM.IsEqualGUID(guid, COM.IIDIOleControlSite)) return "IIDIOleControlSite";
		if (COM.IsEqualGUID(guid, COM.IIDIOleDocument)) return "IIDIOleDocument";
		if (COM.IsEqualGUID(guid, COM.IIDIOleDocumentSite)) return "IIDIOleDocumentSite";
		if (COM.IsEqualGUID(guid, COM.IIDIOleInPlaceFrame)) return "IIDIOleInPlaceFrame";
		if (COM.IsEqualGUID(guid, COM.IIDIOleInPlaceObject)) return "IIDIOleInPlaceObject";
		if (COM.IsEqualGUID(guid, COM.IIDIOleInPlaceSite)) return "IIDIOleInPlaceSite";
		if (COM.IsEqualGUID(guid, IIDIOleItemContainer)) return "IIDIOleItemContainer";
		if (COM.IsEqualGUID(guid, COM.IIDIOleLink)) return "IIDIOleLink";
		if (COM.IsEqualGUID(guid, COM.IIDIOleObject)) return "IIDIOleObject";
		if (COM.IsEqualGUID(guid, IIDIParseDisplayName)) return "IIDIParseDisplayName";
		if (COM.IsEqualGUID(guid, IIDIPerPropertyBrowsing)) return "IIDIPerPropertyBrowsing";
		if (COM.IsEqualGUID(guid, COM.IIDIPersist)) return "IIDIPersist";
		if (COM.IsEqualGUID(guid, COM.IIDIPersistFile)) return "IIDIPersistFile";
		if (COM.IsEqualGUID(guid, IIDIPersistMemory)) return "IIDIPersistMemory";
		if (COM.IsEqualGUID(guid, IIDIPersistPropertyBag)) return "IIDIPersistPropertyBag";
		if (COM.IsEqualGUID(guid, COM.IIDIPersistStorage)) return "IIDIPersistStorage";
		if (COM.IsEqualGUID(guid, COM.IIDIPersistStreamInit)) return "IIDIPersistStreamInit";
		if (COM.IsEqualGUID(guid, IIDIPicture)) return "IIDIPicture";
		if (COM.IsEqualGUID(guid, IIDIPictureDisp)) return "IIDIPictureDisp";
		if (COM.IsEqualGUID(guid, IIDIPropertyBag)) return "IIDIPropertyBag";
		if (COM.IsEqualGUID(guid, COM.IIDIPropertyNotifySink)) return "IIDIPropertyNotifySink";
		if (COM.IsEqualGUID(guid, IIDIPropertyPage)) return "IIDIPropertyPage";
		if (COM.IsEqualGUID(guid, IIDIPropertyPage2)) return "IIDIPropertyPage2";
		if (COM.IsEqualGUID(guid, IIDIPropertyPageSite)) return "IIDIPropertyPageSite";
		if (COM.IsEqualGUID(guid, COM.IIDIProvideClassInfo)) return "IIDIProvideClassInfo";
		if (COM.IsEqualGUID(guid, COM.IIDIProvideClassInfo2)) return "IIDIProvideClassInfo2";
		if (COM.IsEqualGUID(guid, IIDIPSFactoryBuffer)) return "IIDIPSFactoryBuffer";
		if (COM.IsEqualGUID(guid, IIDIRootStorage)) return "IIDIRootStorage";
		if (COM.IsEqualGUID(guid, IIDIROTData)) return "IIDIROTData";
		if (COM.IsEqualGUID(guid, IIDIRpcChannelBuffer)) return "IIDIRpcChannelBuffer";
		if (COM.IsEqualGUID(guid, IIDIRpcProxyBuffer)) return "IIDIRpcProxyBuffer";
		if (COM.IsEqualGUID(guid, IIDIRpcStubBuffer)) return "IIDIRpcStubBuffer";
		if (COM.IsEqualGUID(guid, IIDIRunnableObject)) return "IIDIRunnableObject";
		if (COM.IsEqualGUID(guid, IIDIRunningObjectTable)) return "IIDIRunningObjectTable";
		if (COM.IsEqualGUID(guid, IIDISimpleFrameSite)) return "IIDISimpleFrameSite";
		if (COM.IsEqualGUID(guid, COM.IIDIServiceProvider)) return "IIDIServiceProvider";
		if (COM.IsEqualGUID(guid, COM.IIDISpecifyPropertyPages)) return "IIDISpecifyPropertyPages";
		if (COM.IsEqualGUID(guid, IIDIStdMarshalInfo)) return "IIDIStdMarshalInfo";
		if (COM.IsEqualGUID(guid, IIDISupportErrorInfo)) return "IIDISupportErrorInfo";
		if (COM.IsEqualGUID(guid, IIDITypeComp)) return "IIDITypeComp";
		if (COM.IsEqualGUID(guid, IIDITypeLib)) return "IIDITypeLib";
		if (COM.IsEqualGUID(guid, COM.IIDIUnknown)) return "IIDIUnknown";
		if (COM.IsEqualGUID(guid, IIDIViewObject)) return "IIDIViewObject";
		if (COM.IsEqualGUID(guid, COM.IIDIViewObject2)) return "IIDIViewObject2";
		if (COM.IsEqualGUID(guid, COM.CGID_DocHostCommandHandler)) return "CGID_DocHostCommandHandler";
		if (COM.IsEqualGUID(guid, COM.CGID_Explorer)) return "CGID_Explorer";
		if (COM.IsEqualGUID(guid, COM.IIDIAccessible2)) return "IIDIAccessible2";
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleRelation)) return "IIDIAccessibleRelation";
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleAction)) return "IIDIAccessibleAction";
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleComponent)) return "IIDIAccessibleComponent";
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleValue)) return "IIDIAccessibleValue";
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleText)) return "IIDIAccessibleText";
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleEditableText)) return "IIDIAccessibleEditableText";
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleHyperlink)) return "IIDIAccessibleHyperlink";
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleHypertext)) return "IIDIAccessibleHypertext";
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleTable)) return "IIDIAccessibleTable";
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleTable2)) return "IIDIAccessibleTable2";
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleTableCell)) return "IIDIAccessibleTableCell";
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleImage)) return "IIDIAccessibleImage";
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleApplication)) return "IIDIAccessibleApplication";
		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleContext)) return "IIDIAccessibleContext";
		if (COM.IsEqualGUID(guid, IIDIdentityUnmarshal)) return "IIDIdentityUnmarshal";
		if (COM.IsEqualGUID(guid, IIDInternalMSMarshaller)) return "IIDInternalMSMarshaller";
		if (COM.IsEqualGUID(guid, IIDIAccIdentity)) return "IIDIAccIdentity";
		if (COM.IsEqualGUID(guid, IIDIAccPropServer)) return "IIDIAccPropServer";
		if (COM.IsEqualGUID(guid, IIDIAccPropServices)) return "IIDIAccPropServices";
		}
		return guid.toString();
	}
	static GUID IIDFromString(String lpsz) {
		if (DEBUG) {
		int length = lpsz.length();
		char[] buffer = new char[length + 1];
		lpsz.getChars(0, length, buffer, 0);
		GUID lpiid = new GUID();
		if (COM.IIDFromString(buffer, lpiid) == COM.S_OK) return lpiid;
		}
		return null;
	}
	@Override
	public String toString () {
		String toString = super.toString();
		if (DEBUG) {
			int role = getRole();
			if (role == 0) role = getDefaultRole();
			return toString.substring(toString.lastIndexOf('.') + 1) + "(" + getRoleString(role) + ")";
		}
		return toString;
	}
	// END DEBUG CODE
}
