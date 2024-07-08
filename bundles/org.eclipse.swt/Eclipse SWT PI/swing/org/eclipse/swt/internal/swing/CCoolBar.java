/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Container;
import java.awt.Image;

import javax.swing.JScrollBar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;

class CCoolBarImplementation extends JCoolBar implements CCoolBar {

  protected CoolBar handle;

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
  
  public CCoolBarImplementation(CoolBar coolBar, int style) {
    handle = coolBar;
    userAttributeHandler = new UserAttributeHandler(this);
    init(style);
  }

  protected void init(int style) {
    if((style & SWT.BORDER) != 0) {
      setBorder(LookAndFeelUtils.getStandardBorder());
    }
  }

  public Container getClientArea() {
    return this;
  }

  public JScrollBar getVerticalScrollBar() {
    return null;
  }

  public JScrollBar getHorizontalScrollBar() {
    return null;
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

public interface CCoolBar extends CComposite {

  public static class Factory {
    private Factory() {}

    public static CCoolBar newInstance(CoolBar coolBar, int style) {
      return new CCoolBarImplementation(coolBar, style);
    }

  }

  public int getItemCount();

  public boolean isLocked();

  public void setLocked(boolean isLocked);

  public void setWrappedIndices(int[] indices);

}
