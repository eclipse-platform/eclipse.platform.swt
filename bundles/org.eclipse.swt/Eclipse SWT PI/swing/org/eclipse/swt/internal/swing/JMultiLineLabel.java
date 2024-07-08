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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

public class JMultiLineLabel extends JPanel implements SwingConstants {

  protected static class InnerLabel extends JLabel {

    protected String nonHtmlText;
    
    public InnerLabel(String text, int alignment, String nonHtmlText) {
      super(text, alignment);
      this.nonHtmlText = nonHtmlText;
    }
    
    public String getNonHtmlText() {
      return nonHtmlText;
    }
    
    public Dimension getPreferredSize() {
      Dimension preferredSize = super.getPreferredSize();
      if(getIcon() == null && getText().length() == 0) {
        preferredSize.height += getFontMetrics(getFont()).getHeight();
      }
      return preferredSize;
    }

    public Dimension getMaximumSize() {
      return new Dimension(Integer.MAX_VALUE, super.getMaximumSize().height);
    }

    public void reshape(int x, int y, int w, int h) {
      super.reshape(x, y, w, h);
      View globalView = (View)getClientProperty(BasicHTML.propertyKey);
      if(globalView != null) {
        Border border = getBorder();
        if(border != null) {
          Insets insets = border.getBorderInsets(this);
          w -= insets.left + insets.right;
          h -= insets.top + insets.bottom;
        }
        globalView.setSize(w, h);
      }
    }
    
    public Font getFont() {
      Container parent = getParent();
      return parent == null? super.getFont(): parent.getFont();
    }
    
    public Color getBackground() {
      Container parent = getParent();
      return parent == null? super.getBackground(): parent.getBackground();
    }
    
    public Color getForeground() {
      Container parent = getParent();
      return parent == null? super.getForeground(): parent.getForeground();
    }
    
    public boolean isEnabled() {
      Container parent = getParent();
      return parent == null? super.isEnabled(): parent.isEnabled();
    }
    
  }

  public JMultiLineLabel() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    LookAndFeelUtils.applyLabelStyle(this);
    setOpaque(false);
    createContent();
  }

  protected String text = "";
  protected int mnemonicIndex = -1;

  public int getPreferredWidth() {
    int width = 0;
    for(int i=getComponentCount()-1; i>=0; i--) {
      InnerLabel innerLabel = (InnerLabel)getComponent(i);
      String text = innerLabel.getText();
      String nonHtmlText = innerLabel.getNonHtmlText();
      innerLabel.setText(nonHtmlText);
      width = Math.max(width, innerLabel.getPreferredSize().width);
      innerLabel.setText(text);
    }
    return width;
  }
  
  public int getMnemonicIndex() {
    return mnemonicIndex;
  }
  
  public void setText(String text, int mnemonicIndex) {
    if(text == null) {
      text = "";
    }
    if(this.text.equals(text) && this.mnemonicIndex == mnemonicIndex) {
      return;
    }
    this.text = text;
    this.mnemonicIndex = mnemonicIndex;
    createContent();
  }

  protected void createContent() {
    removeAll();
    String[] labels = text.split("\n");
    int count = 0;
    for(int i=0; i<labels.length; i++) {
      String nonHtmlLabel = labels[i];
      String label = nonHtmlLabel;
      boolean isWrapping = isWrapping();
      if(isWrapping) {
        label = "<html>" + Utils.escapeSwingXML(label) + "</html>";
      }
      InnerLabel innerLabel = new InnerLabel(label, alignment, nonHtmlLabel);
      innerLabel.setHorizontalAlignment(alignment);
      if(!isWrapping) {
        innerLabel.putClientProperty("html", null);
      }
      innerLabel.setText(label);
      int newCount = count + label.length() + 1;
      // TODO: check with HTML style what to do with mnemonics
      if(count < mnemonicIndex && newCount > mnemonicIndex) {
        innerLabel.setDisplayedMnemonicIndex(mnemonicIndex - count);
      }
      count = newCount;
      add(innerLabel);
    }
    adjustStyles();
    revalidate();
    repaint();
  }

  protected boolean isWrapping;

  public void setWrapping(boolean isWrapping) {
    if(this.isWrapping == isWrapping) {
      return;
    }
    this.isWrapping = isWrapping;
    createContent();
  }

  public boolean isWrapping() {
    return isWrapping;
  }

  protected int alignment = JLabel.LEFT;

  public void setHorizontalAlignment(int alignment) {
    this.alignment = alignment;
    for(int i=getComponentCount()-1; i>=0; i--) {
      ((InnerLabel)getComponent(i)).setHorizontalAlignment(alignment);
    }
  }

  public void setIcon(Icon icon) {
    if(getComponentCount() > 0) {
      ((InnerLabel)getComponent(0)).setIcon(icon);
    }
  }

//  public void reshape(int x, int y, int w, int h) {
//    super.reshape(x, y, w, h);
//    for(int i=getComponentCount()-1; i>=0; i--) {
//      getComponent(i).invalidate();
//    }
//    validate();
//  }
  
  public void reshape(int x, int y, int w, int h) {
    super.reshape(x, y, w, h);
    for(int i=getComponentCount()-1; i>=0; i--) {
      getComponent(i).invalidate();
    }
    validate();
  }

  public Dimension getPreferredSize() {
    Dimension d = new Dimension(0, 0);
    for(int i=getComponentCount()-1; i>=0; i--) {
      Dimension preferredSize = getComponent(i).getPreferredSize();
      d.height += preferredSize.height;
      d.width = Math.max(d.width, preferredSize.width);
    }
    Insets insets = getInsets();
    d.width += insets.left + insets.right;
    d.height += insets.top + insets.bottom;
    return d;
  }
  
  public void setFont(Font font) {
    super.setFont(font);
    adjustStyles();
  }
  
  public void adjustStyles() {
    if(!isWrapping()) {
      return;
    }
    for(int i=getComponentCount()-1; i>=0; i--) {
      InnerLabel innerLabel = (InnerLabel)getComponent(i);
      innerLabel.updateUI();
      innerLabel.reshape(getX(), getY(), getWidth(), getHeight());
    }
  }

}
