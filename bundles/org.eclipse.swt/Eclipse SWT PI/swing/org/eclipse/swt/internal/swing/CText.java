/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

class CTextMulti extends JScrollPane implements CText {

  protected Text handle;
  protected JTextArea textArea;

  public Container getSwingComponent() {
    return textArea;
  }

  public Control getSWTHandle() {
    return handle;
  }

  protected UserAttributeHandler userAttributeHandler;
  
  public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }
  
  public CTextMulti(Text text, int style) {
    this.handle = text;
//    textArea = new JTextArea(4, 7);
    textArea = new JTextArea() {
//      public Cursor getCursor() {
//        if(!isCursorSet()) {
//          return super.getCursor();
//        }
//        for(Component parent = this; (parent = parent.getParent()) != null; ) {
//          if(parent.isCursorSet()) {
//            Cursor cursor = parent.getCursor();
//            if(!(parent instanceof Window) || cursor != Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)) {
//              return cursor;
//            }
//          }
//        }
//        return super.getCursor();
//      }
      public Color getBackground() {
        return CTextMulti.this != null && userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
      }
      public Color getForeground() {
        return CTextMulti.this != null && userAttributeHandler != null && userAttributeHandler.foreground != null? userAttributeHandler.foreground: super.getForeground();
      }
      public Font getFont() {
        return CTextMulti.this != null && userAttributeHandler != null && userAttributeHandler.font != null? userAttributeHandler.font: super.getFont();
      }
      public Cursor getCursor() {
        if(CTextMulti.this == null || userAttributeHandler == null) {
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
    };
    userAttributeHandler = new UserAttributeHandler(textArea);
    setFocusable(false);
    JViewport viewport = new JViewport() {
      public boolean isOpaque() {
        return backgroundImageIcon == null && super.isOpaque();
      }
      protected void paintComponent(Graphics g) {
        Utils.paintTiledImage(this, g, backgroundImageIcon);
        super.paintComponent(g);
      }
      public Color getBackground() {
        return CTextMulti.this != null && userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
      }
    };
    setViewport(viewport);
    viewport.setView(textArea);
    init(style);
  }

  public boolean isFocusable() {
    return textArea.isFocusable();
  }
  
  public void requestFocus() {
    textArea.requestFocus();
  }
  
  protected KeyEvent keyEvent = null;

  protected void init(int style) {
    // A multi line text field should have the same characteristics than a single-line one.
    LookAndFeelUtils.applyTextFieldStyle(textArea);
    if((style & SWT.BORDER) == 0) {
      setBorder(null);
    }
    textArea.setBorder(null);
    if((style & SWT.WRAP) != 0) {
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);
    }
    textArea.setEditable((style & SWT.READ_ONLY) == 0);
    if((style & SWT.H_SCROLL) == 0) {
      setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
    }
    if((style & SWT.V_SCROLL) == 0) {
      setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
    }
    Utils.installMouseListener(textArea, handle);
    Utils.installKeyListener(textArea, handle);
    Utils.installFocusListener(textArea, handle);
    Utils.installComponentListener(this, handle);
    textArea.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        keyEvent = e;
      }
      public void keyReleased(KeyEvent e) {
        keyEvent = null;
      }
    });
    textArea.getDocument().addDocumentListener(new DocumentListener() {
    	public void changedUpdate(DocumentEvent e) {
    		handle.processEvent(e);
    	}
    	public void insertUpdate(DocumentEvent e) {
    		handle.processEvent(e);
    	}
    	public void removeUpdate(DocumentEvent e) {
    		handle.processEvent(e);
    	}
    });
    // TODO: find out the expected behaviour for default selection event
//    textArea.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        handle.processEvent(e);
//      }
//    });
    ((AbstractDocument)textArea.getDocument()).setDocumentFilter(new DocumentFilter() {
//      public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
//      }
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if(getText().length() - length + text.length() > getTextLimit()) {
          return;
        }
        TextFilterEvent filterEvent = new TextFilterEvent(this, text, offset, length, keyEvent);
        handle.processEvent(filterEvent);
        String s = filterEvent.getText();
        if(s != null) {
          super.replace(fb, offset, length, s, attrs);
        }
      }
      public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        TextFilterEvent filterEvent = new TextFilterEvent(this, "", offset, length, keyEvent);
        handle.processEvent(filterEvent);
        String s = filterEvent.getText();
        if(s != null) {
          super.replace(fb, offset, length, s, null);
        }
      }
    });
  }

  public Container getClientArea() {
    return textArea;
  }

  public String getText() {
    return textArea.getText();
  }

  public String getText(int offs, int len) throws BadLocationException {
    return textArea.getText(offs, len);
  }

  public void setText(String text) {
    textArea.setText(text);
  }

  public void setSelectionStart(int start) {
    textArea.setSelectionStart(start);
  }

  public void setSelectionEnd(int end) {
    textArea.setSelectionEnd(end);
  }

  public int getSelectionStart() {
    return textArea.getSelectionStart();
  }

  public int getSelectionEnd() {
    return textArea.getSelectionEnd();
  }

  public void selectAll() {
    textArea.selectAll();
  }

  public void setEditable(boolean isEditable) {
    textArea.setEditable(isEditable);
  }

  public void setEchoChar(char echoChar) {
  }

  public char getEchoChar() {
    return '\0';
  }

  public void copy() {
    textArea.copy();
  }

  public void cut() {
    textArea.cut();
  }

  public void paste() {
    textArea.paste();
  }

  public void setTabSize(int tabSize) {
    textArea.setTabSize(tabSize);
  }

  public void replaceSelection(String content) {
    textArea.replaceSelection(content);
  }

  public boolean isEditable() {
    return textArea.isEditable();
  }

  public int getCaretPosition() {
    return textArea.getCaretPosition();
  }

  public int getLineCount() {
    return textArea.getLineCount();
  }

  public Point getCaretLocation() {
    int caretPosition = textArea.getCaretPosition();
    try {
      int line = textArea.getLineOfOffset(caretPosition);
      int width = textArea.getFontMetrics(textArea.getFont()).stringWidth(textArea.getText().substring(textArea.getLineStartOffset(line), caretPosition));
      return new Point(width, line * getRowHeight());
    } catch(BadLocationException e) {
    }
    return null;
  }

  public void showSelection() {
    try {
      Rectangle rec1 = textArea.modelToView(getSelectionStart());
      Rectangle rec2 = textArea.modelToView(getSelectionEnd());
      if(rec1.y < rec2.y) {
        Dimension size = textArea.getSize();
        rec1.x = 0;
        rec1.width = size.width;
        rec2.x = 0;
        rec2.width = size.width;
      }
      rec1.add(rec2);
      scrollRectToVisible(rec1);
    } catch(Exception e) {
    }
  }

  public int getCaretLineNumber() {
    try {
      return textArea.getLineOfOffset(textArea.getCaretPosition());
    } catch(Exception e) {
      return -1;
    }
  }

  public int getRowHeight() {
    return textArea.getFontMetrics(textArea.getFont()).getHeight();
  }

  public void setComponentOrientation(ComponentOrientation o) {
    super.setComponentOrientation(o);
    textArea.setComponentOrientation(o);
  }

  public Point getViewPosition() {
    return getViewport().getViewPosition();
  }

  public void setViewPosition(Point p) {
    getViewport().setViewPosition(p);
  }

  public void setTextLimit(int limit) {
    textLimit = limit;
    String text = getText();
    if(text.length() > limit) {
      setText(text.substring(0, limit));
    }
  }

  protected int textLimit = Text.LIMIT;

  public int getTextLimit() {
    return textLimit;
  }

  protected ImageIcon backgroundImageIcon;

  public void setBackgroundImage(Image backgroundImage) {
    this.backgroundImageIcon = backgroundImage == null? null: new ImageIcon(backgroundImage);
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
    switch(backgroundInheritanceType) {
    case PREFERRED_BACKGROUND_INHERITANCE:
    case NO_BACKGROUND_INHERITANCE:
      setOpaque(true);
      getViewport().setOpaque(true);
      textArea.setOpaque(true);
      break;
    case BACKGROUND_INHERITANCE:
      setOpaque(false);
      getViewport().setOpaque(false);
      textArea.setOpaque(false);
      break;
    }
  }

  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    textArea.setEnabled(enabled);
  }
  
}

class CTextField extends JPasswordField implements CText {

  protected Text handle;

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
  
  public CTextField(Text text, int style) {
    this.handle = text;
    userAttributeHandler = new UserAttributeHandler(this);
    setEchoChar('\0');
    init(style);
  }
  
  public boolean isOpaque() {
    return backgroundImageIcon == null && super.isOpaque();
  }
  
  protected void paintComponent(Graphics g) {
    Utils.paintTiledImage(this, g, backgroundImageIcon);
    super.paintComponent(g);
  }
  
//  public Cursor getCursor() {
//    if(!isCursorSet()) {
//      return super.getCursor();
//    }
//    for(Component parent = this; (parent = parent.getParent()) != null; ) {
//      if(parent.isCursorSet()) {
//        Cursor cursor = parent.getCursor();
//        if(!(parent instanceof Window) || cursor != Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)) {
//          return cursor;
//        }
//      }
//    }
//    return super.getCursor();
//  }
  
  protected KeyEvent keyEvent = null;
  
  protected void init(int style) {
    if((style & SWT.BORDER) == 0) {
      setBorder(null);
    }
    if((style & SWT.CENTER) != 0) {
      setHorizontalAlignment(CENTER);
    } else if((style & SWT.RIGHT) != 0) {
      setHorizontalAlignment(RIGHT);
    }
    setEditable((style & SWT.READ_ONLY) == 0);
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
    addKeyListener(new KeyListener() {
      public void keyPressed(KeyEvent e) {
        keyEvent = e;
      }
      public void keyReleased(KeyEvent e) {
        keyEvent = null;
      }
      public void keyTyped(KeyEvent e) {
      }
    });
    getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        handle.processEvent(e);
      }
      public void insertUpdate(DocumentEvent e) {
        handle.processEvent(e);
      }
      public void removeUpdate(DocumentEvent e) {
        handle.processEvent(e);
      }
    });
    ((AbstractDocument)getDocument()).setDocumentFilter(new DocumentFilter() {
//      public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
//      }
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if(getText().length() - length + text.length() > getTextLimit()) {
          return;
        }
        TextFilterEvent filterEvent = new TextFilterEvent(this, text, offset, length, keyEvent);
        handle.processEvent(filterEvent);
        String s = filterEvent.getText();
        if(s != null) {
          super.replace(fb, offset, length, s, attrs);
        }
      }
      public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        TextFilterEvent filterEvent = new TextFilterEvent(this, "", offset, length, keyEvent);
        handle.processEvent(filterEvent);
        String s = filterEvent.getText();
        if(s != null) {
          super.replace(fb, offset, length, s, null);
        }
      }
    });
  }

  public Container getClientArea() {
    return this;
  }

  public void setText(String text) {
    super.setText(text.replaceAll("[\r\n]", ""));
  }

  public void setEchoChar(char c) {
    super.setEchoChar(c);
    if(c == '\0') {
      LookAndFeelUtils.applyTextFieldStyle(this);
    } else {
      LookAndFeelUtils.applyPasswordFieldStyle(this);
    }
  }
  
  public void setTabSize(int tabSize) {
  }

  public int getLineCount() {
    return 1;
  }

  public Point getCaretLocation() {
    return new Point(getCaretPosition(), 0);
  }

  public void showSelection() {
    try {
      Rectangle rec1 = modelToView(getSelectionStart());
      Rectangle rec2 = modelToView(getSelectionEnd());
      rec1.add(rec2);
      scrollRectToVisible(rec1);
    } catch(Exception e) {
    }
  }

  public int getCaretLineNumber() {
    return 0;
  }

  public int getRowHeight() {
    return getFontMetrics(getFont()).getHeight();
  }

  public Point getViewPosition() {
    try {
      Rectangle rec1 = modelToView(0);
      return new Point(rec1.x, rec1.y);
    } catch(Exception e) {
    }
    return new Point(0, 0);
  }

  public void setViewPosition(Point p) {
    try {
      Rectangle rec1 = modelToView(p.x);
      scrollRectToVisible(rec1);
    } catch(Exception e) {
    }
  }

  public void setTextLimit(int limit) {
    textLimit = limit;
    String text = getText();
    if(text.length() > limit) {
      setText(text.substring(0, limit));
    }
  }

  protected int textLimit = Text.LIMIT;

  public int getTextLimit() {
    return textLimit;
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
    if(userAttributeHandler == null) {
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
  
  protected ImageIcon backgroundImageIcon;

  public void setBackgroundImage(Image backgroundImage) {
    this.backgroundImageIcon = backgroundImage == null? null: new ImageIcon(backgroundImage);
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
    switch(backgroundInheritanceType) {
    case PREFERRED_BACKGROUND_INHERITANCE:
    case NO_BACKGROUND_INHERITANCE:
      setOpaque(true);
      break;
    case BACKGROUND_INHERITANCE:
      setOpaque(false);
      break;
    }
  }

  public JScrollBar getHorizontalScrollBar() {
    // No support for scrolling in SIMPLE style.
    return null;
  }

  public JScrollBar getVerticalScrollBar() {
    // No support for scrolling in SIMPLE style.
    return null;
  }

}

/**
 * The text equivalent on the Swing side.
 * @version 1.0 2005.08.30
 * @author Christopher Deckers (chrriis@nextencia.net)
 */
public interface CText extends CScrollable {

  public static class Factory {
    private Factory() {}

    public static CText newInstance(Text text, int style) {
      if((style & SWT.MULTI) != 0) {
        return new CTextMulti(text, style);
      }
      return new CTextField(text, style);
    }

  }

  public String getText();

  public String getText(int offs, int len) throws BadLocationException;

  public void setText(String text);

  public void setSelectionStart(int start);

  public void setSelectionEnd(int end);

  public int getSelectionStart();

  public int getSelectionEnd();

  public void selectAll();

  public void setEditable(boolean isEditable);

  public void setEchoChar(char echoChar);

  public char getEchoChar();

  public void copy();

  public void cut();

  public void paste();

  public void setTabSize(int tabSize);

  public void replaceSelection(String content);

  public boolean isEditable();

  public int getCaretPosition();

  public int getLineCount();

  public Point getCaretLocation();

  public void showSelection();

  public int getCaretLineNumber();

  public int getRowHeight();

  public void setComponentOrientation(ComponentOrientation o);

  public Point getViewPosition();

  public void setViewPosition(Point p);

  public void setTextLimit(int limit);

  public int getTextLimit();

}
