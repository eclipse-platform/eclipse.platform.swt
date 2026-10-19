/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;
/** Commented out to prevent jFace dependency */

//package jface;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.eclipse.jface.dialogs.Dialog;
//import org.eclipse.jface.dialogs.IDialogConstants;
//import org.eclipse.jface.viewers.ArrayContentProvider;
//import org.eclipse.jface.viewers.CellEditor;
//import org.eclipse.jface.viewers.ComboBoxCellEditor;
//import org.eclipse.jface.viewers.ICellModifier;
//import org.eclipse.jface.viewers.LabelProvider;
//import org.eclipse.jface.viewers.TableViewer;
//import org.eclipse.jface.window.Window;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.RowLayout;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Shell;
////
//
///**
// * A window that launches a dialog containing a table with an editable cell.
// * It takes 2 seconds for the cell editor to appear.  If the dialog is closed
// * during that time, an SWTException occurs.
// *
// * The dialog and window are both needed so that the SWT event loop continues
// * to run after the dialog is closed.  The {@link ComboBoxCellEditor} also
// * needs to be using the DROP_DOWN_ON_MOUSE_ACTIVATION style so that the
// * drop-down list is made visible as soon as the cell editor is activated.
// *
// * @author Eleanor Joslin (ejj@corefiling.com)
// */
//public class Bug288357_ComboBoxCellEditorExceptionError extends Window {
//
//  /**
//   * Dialog containing a table with an editable cell.
//   * It takes 2 seconds before the combo box in the cell editor pops up.
//   * Closing the dialog during that time triggers an error.
//   */
//  private static class PopupDialog extends Dialog {
//
//    private static final int SLEEP_INTERVAL_MILLIS = 2000;
//
//    private static final List<String> CHOICES = Arrays.asList("Apples", "Pears", "Plums", "Cherries");
//
//    private final StringHolder _element = new StringHolder();
//
//    private TableViewer _table;
//
//    private final ComboBoxCellEditor _cellEditor = new ComboBoxCellEditor();
//
//    private final ICellModifier _cellModifier = new ICellModifier() {
//
//      public void modify(final Object element, final String property, final Object value) {
//        _element.setString(CHOICES.get((Integer) value));
//        _table.refresh();
//      }
//
//      public Object getValue(final Object element, final String property) {
//        return CHOICES.indexOf(_element.getString());
//      }
//
//      public boolean canModify(final Object element, final String property) {
//        try {
//          Thread.sleep(SLEEP_INTERVAL_MILLIS);
//        }
//        catch (InterruptedException e) {
//        }
//        _cellEditor.setItems(CHOICES.toArray(new String[CHOICES.size()]));
//        return true;
//      }
//    };
//
//    public PopupDialog(final Shell parentShell) {
//      super(parentShell);
//    }
//
//    @Override
//    protected Control createDialogArea(final Composite parent) {
//      _table = new TableViewer(parent);
//      _table.setColumnProperties(new String[] {"Only one column"});
//      _table.setCellEditors(new CellEditor[] {_cellEditor });
//      _table.setCellModifier(_cellModifier);
//      _table.setContentProvider(new ArrayContentProvider());
//      _table.setLabelProvider(new LabelProvider());
//      _table.setInput(new Object[] {_element});
//
//      _table.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//      _cellEditor.setStyle(SWT.READ_ONLY);
//      _cellEditor.setActivationStyle(ComboBoxCellEditor.DROP_DOWN_ON_MOUSE_ACTIVATION);
//      _cellEditor.create(_table.getTable());
//
//      return _table.getControl();
//    }
//
//    @Override
//    protected void createButtonsForButtonBar(final Composite parent) {
//      createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
//    }
//  }
//
//  /**
//   * Element to show in the table.
//   */
//  private static class StringHolder {
//
//    private String _string = "Apples";
//
//    public void setString(final String string) {
//      _string = string;
//    }
//
//    public String getString() {
//      return _string;
//    }
//
//    @Override
//    public String toString() {
//      return _string;
//    }
//  }
//
//  public Bug288357_ComboBoxCellEditorExceptionError() {
//    super(new Shell());
//    setBlockOnOpen(true);
//  }
//
//  @Override
//  protected Control createContents(final Composite parent) {
//    Composite composite = new Composite(parent, SWT.NONE);
//    composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//    composite.setLayout(new RowLayout());
//
//    Button button = new Button(composite, SWT.PUSH);
//    button.setText("Open Dialog");
//    button.addSelectionListener(new SelectionAdapter() {
//      @Override
//      public void widgetSelected(final SelectionEvent e) {
//        new PopupDialog(getShell()).open();
//      }
//    });
//
//    return composite;
//  }
//
//  public static void main(final String[] args) {
//    new Bug288357_ComboBoxCellEditorExceptionError().open();
//  }
//}
