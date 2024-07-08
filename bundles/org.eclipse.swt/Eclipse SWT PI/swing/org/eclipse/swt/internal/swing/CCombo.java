/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;

class CComboSimple extends JPanel implements CCombo {
  
  protected Combo handle;
  protected JTextField textField;
  protected JScrollPane scrollPane;
  protected JList list;

  public void requestFocus() {
    textField.requestFocus();
  }
  
  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

  protected UserAttributeHandler userAttributeHandler;
  
  public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }
  
  public CComboSimple(Combo combo, int style) {
    super(new BorderLayout(0, 0));
    textField = new JTextField(7) {
      public Color getBackground() {
        return CComboSimple.this != null && userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
      }
      public Color getForeground() {
        return CComboSimple.this != null && userAttributeHandler != null && userAttributeHandler.foreground != null? userAttributeHandler.foreground: super.getForeground();
      }
      public Font getFont() {
        return CComboSimple.this != null && userAttributeHandler != null && userAttributeHandler.font != null? userAttributeHandler.font: super.getFont();
      }
      public Cursor getCursor() {
        if(CComboSimple.this == null || userAttributeHandler == null) {
          return super.getCursor();
        }
        if(Utils.globalCursor != null) {
          return Utils.globalCursor;
        }
        for(Control parent = handle; parent != null && parent.handle != null; parent = parent.getParent()) {
          Cursor cursor = ((CControl)parent.handle).getUserAttributeHandler().cursor;
          if(cursor != null) {
            return cursor;
          }
        }
        return super.getCursor();
      }
      public boolean isOpaque() {
        return backgroundImageIcon == null && super.isOpaque();
      }
      protected void paintComponent(Graphics g) {
        Utils.paintTiledImage(this, g, backgroundImageIcon);
        super.paintComponent(g);
      }
    };
    userAttributeHandler = new UserAttributeHandler(textField);
    add(textField, BorderLayout.NORTH);
    scrollPane = new JScrollPane();
    list = new JList(new DefaultListModel()) {
      public Dimension getPreferredScrollableViewportSize() {
        Dimension preferredSize = getPreferredSize();
        preferredSize.width += scrollPane.getVerticalScrollBar().getPreferredSize().width;
        preferredSize.height += scrollPane.getHorizontalScrollBar().getPreferredSize().height;
        return preferredSize;
      }
      public Color getBackground() {
        return CComboSimple.this != null && userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
      }
      public Color getForeground() {
        return CComboSimple.this != null && userAttributeHandler != null && userAttributeHandler.foreground != null? userAttributeHandler.foreground: super.getForeground();
      }
      public Font getFont() {
        return CComboSimple.this != null && userAttributeHandler != null && userAttributeHandler.font != null? userAttributeHandler.font: super.getFont();
      }
//      public Cursor getCursor() {
//        return CComboSimple.this != null && userAttributeHandler != null && userAttributeHandler.cursor != null? userAttributeHandler.cursor: super.getCursor();
//      }
      public boolean isOpaque() {
        return backgroundImageIcon == null && super.isOpaque();
      }
    };
    list.setCellRenderer(new DefaultListCellRenderer() {
      public boolean isOpaque() {
        return list.isOpaque() && super.isOpaque();
      }
    });
    JViewport viewport = new JViewport() {
      public boolean isOpaque() {
        return backgroundImageIcon == null && super.isOpaque();
      }
      protected void paintComponent(Graphics g) {
        Utils.paintTiledImage(this, g, backgroundImageIcon);
        super.paintComponent(g);
      }
      public Color getBackground() {
        return CComboSimple.this != null && userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
      }
    };
    scrollPane.setViewport(viewport);
    viewport.setView(list);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    add(scrollPane, BorderLayout.CENTER);
    this.handle = combo;
    init(style);
  }

  protected void init(int style) {
    Utils.installMouseListener(list, handle);
    Utils.installKeyListener(list, handle);
    Utils.installFocusListener(list, handle);
    Utils.installComponentListener(list, handle);
    list.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        Object item = list.getSelectedValue();
        if(item != null) {
          textField.setText(String.valueOf(item));
        }
      }
    });
    textField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handle.processEvent(e);
      }
    });
    ((AbstractDocument)textField.getDocument()).setDocumentFilter(new DocumentFilter() {
//      public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
//      }
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if(getEditorText().length() - length + text.length() > getEditorTextLimit()) {
          return;
        }
        super.replace(fb, offset, length, text, attrs);
      }
//      public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
//      }
    });
//    list.addItemListener(new ItemListener() {
//      public void itemStateChanged(ItemEvent e) {
//        handle.processEvent(e);
//      }
//    });
  }
  
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    textField.setEnabled(enabled);
    list.setEnabled(enabled);
  }
  
  public JScrollBar getHorizontalScrollBar() {
    // TODO: implement
    return null;
  }

  public JScrollBar getVerticalScrollBar() {
    // TODO: implement
    return null;
  }

  public Container getClientArea() {
    return this;
  }

  protected ImageIcon backgroundImageIcon;

  public void setBackgroundImage(Image backgroundImage) {
    this.backgroundImageIcon = backgroundImage == null? null: new ImageIcon(backgroundImage);
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
    switch(backgroundInheritanceType) {
    case PREFERRED_BACKGROUND_INHERITANCE:
    case NO_BACKGROUND_INHERITANCE: {
      setOpaque(true);
      textField.setOpaque(true);
      list.setOpaque(true);
      scrollPane.setOpaque(true);
      scrollPane.getViewport().setOpaque(true);
      break;
    }
    case BACKGROUND_INHERITANCE: {
      setOpaque(false);
      textField.setOpaque(false);
      list.setOpaque(false);
      scrollPane.setOpaque(false);
      scrollPane.getViewport().setOpaque(false);
      break;
    }
    }
  }

  public void addItem(Object o) {
    ((DefaultListModel)list.getModel()).addElement(o);
  }

  public Object getItemAt(int index) {
    return list.getModel().getElementAt(index);
  }

  public void removeItemAt(int index) {
    ((DefaultListModel)list.getModel()).remove(index);
  }

  public void removeAllItems() {
    ((DefaultListModel)list.getModel()).removeAllElements();
  }

  public void insertElementAt(Object o, int index) {
    ((DefaultListModel)list.getModel()).add(index, o);
  }

  public int getItemCount() {
    return list.getModel().getSize();
  }

  public int getSelectedIndex() {
    return list.getSelectedIndex();
  }

  public void setSelectedIndex(int index) {
    Object item = list.getModel().getElementAt(index);
    if(item != null) {
      list.setSelectedIndex(index);
    }
  }

  public int getMaximumRowCount() {
    return list.getVisibleRowCount();
  }

  public void setMaximumRowCount(int count) {
    // Do nothing?
  }

  public String getEditorText() {
    return textField.getText();
  }

  public void setEditorText(String text) {
    textField.setText(text);
  }

  public void copyEditor() {
    textField.copy();
  }

  public void cutEditor() {
    textField.cut();
  }

  public void pasteEditor() {
    textField.paste();
  }

  public void setEditorCaretPosition(int index) {
    textField.setCaretPosition(index);
  }

  public int getEditorSelectionStart() {
    return textField.getSelectionStart();
  }

  public void setEditorSelectionStart(int selectionStart) {
    textField.setSelectionStart(selectionStart);
  }

  public int getEditorSelectionEnd() {
    return textField.getSelectionEnd();
  }

  public void setEditorSelectionEnd(int selectionEnd) {
    textField.setSelectionEnd(selectionEnd);
  }

  public void clearEditorSelection() {
    textField.setSelectionStart(textField.getSelectionEnd());
  }

  public void setEditorTextLimit(int limit) {
    textLimit = limit;
    String text = getEditorText();
    if(text.length() > limit) {
      setEditorText(text.substring(0, limit));
    }
  }

  protected int textLimit = Combo.LIMIT;

  public int getEditorTextLimit() {
    return textLimit;
  }

  public Dimension getEditorSize() {
    return textField.getSize();
  }
  
  public boolean isPopupVisible() {
    return false;
  }

}

class CComboImplementation extends JComboBox implements CCombo {

  protected Combo handle;

  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

  protected UserAttributeHandler userAttributeHandler;
  
  public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }
  
  public CComboImplementation(Combo combo, int style) {
    this.handle = combo;
    userAttributeHandler = new UserAttributeHandler(this);
    setLightWeightPopupEnabled(Utils.isLightweightPopups());
    init(style);
  }

  protected boolean isDefaultButtonHackActive;
  
  public boolean isPopupVisible() {
    boolean isPopupVisible = super.isPopupVisible();
    if(!isPopupVisible) {
      return isDefaultButtonHackActive;
    }
    return isPopupVisible;
  }

  protected void init(int style) {
    setEditable((style & SWT.READ_ONLY) == 0);
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
    JTextField textField = (JTextField)getEditor().getEditorComponent();
    // We put a listener before and after all existing listeners to place and remove the hack
    // The hack is there because the combo notifies the default button of the rootpane when its popup is not visible 
    ActionListener[] actionListeners = textField.getActionListeners();
    for(int i=actionListeners.length-1; i>=0; i--) {
      textField.removeActionListener(actionListeners[i]);
    }
    textField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        isDefaultButtonHackActive = false;
      }
    });
    for(int i=0; i<actionListeners.length; i++) {
      textField.addActionListener(actionListeners[i]);
    }
    textField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        isDefaultButtonHackActive = true;
        handle.processEvent(e);
      }
    });
    ((AbstractDocument)((JTextComponent)getEditor().getEditorComponent()).getDocument()).setDocumentFilter(new DocumentFilter() {
//      public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
//      }
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if(getEditorText().length() - length + text.length() > getEditorTextLimit()) {
          return;
        }
        super.replace(fb, offset, length, text, attrs);
      }
//      public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
//      }
    });
    addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public JScrollBar getHorizontalScrollBar() {
    // TODO: implement
    return null;
  }

  public JScrollBar getVerticalScrollBar() {
    // TODO: implement
    return null;
  }

  public Container getClientArea() {
    return this;
  }

  public void insertElementAt(Object anObject, int index) {
    ((DefaultComboBoxModel)getModel()).insertElementAt(anObject, index);
  }

  public String getEditorText() {
    Object selectedItem = getSelectedItem();
    return selectedItem == null? "": String.valueOf(selectedItem);
//    return ((JTextComponent)getEditor().getEditorComponent()).getText();
  }

  public void setEditorText(String text) {
    setSelectedItem(text);
//    ((JTextComponent)getEditor().getEditorComponent()).setText(text);
  }

  public void copyEditor() {
    ((JTextComponent)getEditor().getEditorComponent()).copy();
  }

  public void cutEditor() {
    ((JTextComponent)getEditor().getEditorComponent()).cut();
  }

  public void pasteEditor() {
    ((JTextComponent)getEditor().getEditorComponent()).paste();
  }

  public void setEditorCaretPosition(int index) {
    ((JTextComponent)getEditor().getEditorComponent()).setCaretPosition(index);
  }

  public int getEditorSelectionStart() {
    return ((JTextComponent)getEditor().getEditorComponent()).getSelectionStart();
  }

  public void setEditorSelectionStart(int selectionStart) {
    ((JTextComponent)getEditor().getEditorComponent()).setSelectionStart(selectionStart);
  }

  public int getEditorSelectionEnd() {
    return ((JTextComponent)getEditor().getEditorComponent()).getSelectionEnd();
  }

  public void setEditorSelectionEnd(int selectionEnd) {
    ((JTextComponent)getEditor().getEditorComponent()).setSelectionEnd(selectionEnd);
  }

  public void clearEditorSelection() {
    JTextComponent textComponent = (JTextComponent)getEditor().getEditorComponent();
    textComponent.setSelectionStart(textComponent.getSelectionEnd());
  }

  public void setEditorTextLimit(int limit) {
    textLimit = limit;
    String text = getEditorText();
    if(text.length() > limit) {
      setEditorText(text.substring(0, limit));
    }
  }

  protected int textLimit = Combo.LIMIT;

  public int getEditorTextLimit() {
    return textLimit;
  }

  public Dimension getEditorSize() {
    return ((JTextComponent)getEditor().getEditorComponent()).getSize();
  }

  public void reshape(int x, int y, int w, int h) {
    super.reshape(x, y, w, getPreferredSize().height);
  }

  public Color getBackground() {
    return userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
  }
  public Color getForeground() {
    return userAttributeHandler != null && userAttributeHandler.foreground != null? userAttributeHandler.foreground: super.getForeground();
  }
  public Font getFont() {
    return userAttributeHandler != null && userAttributeHandler.font != null? userAttributeHandler.font: super.getFont();
  }
  public Cursor getCursor() {
    if(Utils.globalCursor != null) {
      return Utils.globalCursor;
    }
    return userAttributeHandler != null && userAttributeHandler.cursor != null? userAttributeHandler.cursor: super.getCursor();
  }
  
  public void setBackgroundImage(Image backgroundImage) {
    // TODO: implement
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
    switch(backgroundInheritanceType) {
    case PREFERRED_BACKGROUND_INHERITANCE:
    case NO_BACKGROUND_INHERITANCE: {
      setOpaque(true);
      ((JComponent)getEditor().getEditorComponent()).setOpaque(true);
      break;
    }
    case BACKGROUND_INHERITANCE: {
      setOpaque(false);
      ((JComponent)getEditor().getEditorComponent()).setOpaque(false);
      break;
    }
    }
  }

}

public interface CCombo extends CComposite {

  public static class Factory {
    private Factory() {}

    public static CCombo newInstance(Combo combo, int style) {
      if ((style & SWT.SIMPLE) != 0) {
        return new CComboSimple(combo, style);
      }
      return new CComboImplementation(combo, style);
    }

  }

  public void addItem(Object anObject);

  public Object getItemAt(int index);

  public void removeItemAt(int anIndex);

  public void removeAllItems();

  public void insertElementAt(Object anObject, int index);

  public int getItemCount();

  public int getSelectedIndex();

  public void setSelectedIndex(int index);

  public int getMaximumRowCount();

  public void setMaximumRowCount(int count);

  public void setComponentOrientation(ComponentOrientation o);

  public String getEditorText();

  public void setEditorText(String text);

  public void copyEditor();

  public void cutEditor();

  public void pasteEditor();

  public void setEditorCaretPosition(int index);

  public int getEditorSelectionStart();

  public void setEditorSelectionStart(int selectionStart);

  public int getEditorSelectionEnd();

  public void setEditorSelectionEnd(int selectionEnd);

  public void clearEditorSelection();

  public void setEditorTextLimit(int limit);

  public int getEditorTextLimit();

  public Dimension getEditorSize();
  
  public boolean isPopupVisible();

}
