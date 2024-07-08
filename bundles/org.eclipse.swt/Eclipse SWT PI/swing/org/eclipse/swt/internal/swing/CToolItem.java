/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar.Separator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolItem;

class CToolItemPush extends JButton implements CToolItem {

  protected ToolItem handle;

  public CToolItemPush(ToolItem toolItem, int style) {
    handle = toolItem;
    init(style);
  }

  protected void init(int style) {
    setFocusable(false);
    setHorizontalTextPosition(CENTER);
    setVerticalTextPosition(BOTTOM);
    setMargin(new Insets(0, 1, 0, 1));
    Utils.installMouseListener(this, handle.getParent());
    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public void setWidth(int width) {
  }

}

class CToolItemCheck extends JToggleButton implements CToolItem {

  protected ToolItem handle;

  public CToolItemCheck(ToolItem toolItem, int style) {
    handle = toolItem;
    init(style);
  }

  protected void init(int style) {
    setFocusable(false);
//    setHorizontalTextPosition(CENTER);
//    setVerticalTextPosition(BOTTOM);
    setMargin(new Insets(0, 1, 0, 1));
    Utils.installMouseListener(this, handle.getParent());
    addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public void setWidth(int width) {
  }

}

class CToolItemRadio extends JToggleButton implements CToolItem {

  protected ToolItem handle;

  public CToolItemRadio(ToolItem toolItem, int style) {
    handle = toolItem;
    init(style);
  }

  protected void init(int style) {
    setFocusable(false);
    setHorizontalTextPosition(CENTER);
    setVerticalTextPosition(BOTTOM);
    setMargin(new Insets(0, 1, 0, 1));
    Utils.installMouseListener(this, handle.getParent());
    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public void setWidth(int width) {
  }

}

class CToolItemDropDown extends CComboButton implements CToolItem {

  protected ToolItem handle;

  public CToolItemDropDown(ToolItem toolItem, int style) {
    handle = toolItem;
    init(style);
  }

  protected void init(int style) {
    setFocusable(false);
    setHorizontalTextPosition(CENTER);
    setVerticalTextPosition(BOTTOM);
    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public boolean isSelected() {
    return false;
  }

  public void setSelected(boolean isSelected) {
  }

  public void setWidth(int width) {
  }

}

class CToolItemSeparator extends Separator implements CToolItem {

  protected ToolItem handle;

  public CToolItemSeparator(ToolItem toolItem, int style) {
    handle = toolItem;
    init(style);
  }

  protected void init(int style) {
    setLayout(null);
    Utils.installMouseListener(this, handle.getParent());
  }

  public Dimension getMaximumSize() {
    Dimension size = super.getMaximumSize();
    if(getComponentCount() == 1) {
      return new Dimension(size.width, getComponent(0).getHeight());
    }
    return size;
  }

  public Dimension getMinimumSize() {
    Dimension size = super.getMinimumSize();
    if(getComponentCount() == 1) {
      return new Dimension(size.width, getComponent(0).getHeight());
    }
    return size;
  }

  public Dimension getPreferredSize() {
    Dimension size = super.getPreferredSize();
    if(getComponentCount() == 1) {
      return new Dimension(size.width, getComponent(0).getHeight());
    }
    return size;
  }

//  public Dimension getMinimumSize() {
//    if(getComponentCount() == 1) {
//      return getComponent(0).getSize();
//    }
//    return super.getMinimumSize();
//  }

//  public Dimension getPreferredSize() {
//    if(getComponentCount() == 1) {
//      return getComponent(0).getSize();
//    }
//    return super.getPreferredSize();
//  }

  public boolean isSelected() {
    return false;
  }

  public void setSelected(boolean isSelected) {
  }

  public void setIcon(Icon icon) {
  }

  public void setDisabledIcon(Icon disabledIcon) {
  }

  public void setRolloverIcon(Icon rolloverIcon) {
  }

  public void setText(String text) {
  }

  public void setMnemonic(char mnemonic) {
  }

  public void setWidth(int width) {
    setSeparatorSize(new Dimension(width, getSize().height));
  }

}

public interface CToolItem {

  public static class Factory {
    private Factory() {}

    public static CToolItem newInstance(ToolItem toolItem, int style) {
      if((style & SWT.PUSH) != 0) {
        return new CToolItemPush(toolItem, style);
      }
      if((style & SWT.CHECK) != 0) {
        return new CToolItemCheck(toolItem, style);
      }
      if((style & SWT.RADIO) != 0) {
        return new CToolItemRadio(toolItem, style);
      }
      if((style & SWT.DROP_DOWN) != 0) {
        return new CToolItemDropDown(toolItem, style);
      }
      if((style & SWT.SEPARATOR) != 0) {
        return new CToolItemSeparator(toolItem, style);
      }
      return null;
    }

  }

  public boolean isSelected();

  public void setSelected(boolean isSelected);

  public String getToolTipText();

  public void setToolTipText(String toolTipText);

  public void setIcon(Icon icon);

  public void setDisabledIcon(Icon disabledIcon);

  public void setRolloverIcon(Icon rolloverIcon);

  public void setText(String text);

  public void setMnemonic(char mnemonic);

  public void setWidth(int width);

}
