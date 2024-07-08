/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;

class CSpinnerImplementation extends JSpinner implements CSpinner {

  protected Spinner handle;

  protected SpinnerNumberModel model;

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
  
  public CSpinnerImplementation(Spinner spinner, int style) {
    this.handle = spinner;
    userAttributeHandler = new UserAttributeHandler(this);
    model = new SpinnerNumberModel(0, minimum, maximum, 1.0);
    setModel(model);
    model.setMinimum(new Comparable() {
      public int compareTo(Object o) {
        return minimum - Math.round(((Number)o).floatValue() * (int)Math.pow(10, digitCount));
      }
    });
    model.setMaximum(new Comparable() {
      public int compareTo(Object o) {
        return maximum - Math.round(((Number)o).floatValue() * (int)Math.pow(10, digitCount));
      }
    });
    init(style);
  }

  protected KeyEvent keyEvent = null;

  protected void init(int style) {
    JFormattedTextField textField = ((DefaultEditor)getEditor()).getTextField();
    if((style & SWT.READ_ONLY) != 0) {
      textField.setEditable(false);
    }
//    if((style & SWT.BORDER) == 0) {
//      setBorder(null);
//      textField.setBorder(null);
//    }
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
    textField.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        keyEvent = e;
      }
      public void keyReleased(KeyEvent e) {
        keyEvent = null;
      }
    });
    addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        handle.processEvent(e);
      }
    });
    AbstractDocument document = (AbstractDocument)textField.getDocument();
    document.addDocumentListener(new DocumentListener() {
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
    document.setDocumentFilter(new DocumentFilter() {
//    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
//    }
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
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

  public int getStepSize() {
    return Math.round(model.getStepSize().floatValue() * (int)Math.pow(10, digitCount));
  }

  public void setStepSize(int stepSize) {
    model.setStepSize(new Float((float)stepSize / (int)Math.pow(10, digitCount)));
  }

  protected int minimum = 0;
  protected int maximum = 100;
  
  public int getMinimum() {
    return minimum;
  }

  public int getMaximum() {
    return maximum;
  }
  
  public void setMinimum(int minimum) {
    this.minimum = minimum;
  }

  public void setMaximum(int maximum) {
    this.maximum = maximum;
  }
  
  public void setSelectedValue(int value) {
    model.setValue(new Float((float)value / (int)Math.pow(10, digitCount)));
  }

  public int getSelectedValue() {
    return Math.round(((Number)model.getValue()).floatValue() * (int)Math.pow(10, digitCount));
  }

  public void copy() {
    ((DefaultEditor)getEditor()).getTextField().copy();
  }

  public void cut() {
    ((DefaultEditor)getEditor()).getTextField().cut();
  }

  public void paste() {
    ((DefaultEditor)getEditor()).getTextField().paste();
  }

  protected int digitCount;

  public void setDigitCount(int digitCount) {
    if(digitCount == this.digitCount) {
      return;
    }
    this.digitCount = digitCount;
  }

  public int getDigitCount() {
    return digitCount;
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
    case NO_BACKGROUND_INHERITANCE: setOpaque(true); break;
    case PREFERRED_BACKGROUND_INHERITANCE:
    case BACKGROUND_INHERITANCE: setOpaque(false); break;
    }
  }

}

/**
 * The spinner equivalent on the Swing side.
 * @version 1.0 2006.03.12
 * @author Christopher Deckers (chrriis@nextencia.net)
 */
public interface CSpinner extends CControl {

  public static class Factory {
    private Factory() {}

    public static CSpinner newInstance(Spinner spinner, int style) {
      return new CSpinnerImplementation(spinner, style);
    }

  }

  public int getStepSize();

  public void setStepSize(int stepSize);

  public int getMinimum();

  public int getMaximum();
  
  public void setMinimum(int minimum);

  public void setMaximum(int maximum);

  public void setSelectedValue(int value);

  public int getSelectedValue();

  public void copy();

  public void cut();

  public void paste();

  public void setDigitCount(int digitCount);

  public int getDigitCount();

}
