/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabItem;

class CTabItemImplementation extends JPanel implements CTabItem {

  protected TabItem handle;

  public CTabItemImplementation(TabItem tabItem, int style) {
    super(new BorderLayout(0, 0));
    this.handle = tabItem;
    init(style);
  }

  protected void init(int style) {
    setOpaque(false);
  }
  
  protected void paintComponent(Graphics g) {
    Utils.paintTiledImage(this, g, ((CTabFolderImplementation)handle.getParent().handle).backgroundImageIcon);
    super.paintComponent(g);
  }
  
  public Component getContent() {
    if(getComponentCount() > 0) {
      return getComponent(0);
    }
    return null;
  }

  public Dimension getPreferredSize() {
    if(getComponentCount() > 0) {
      Control swtHandle = ((CControl)getComponent(0)).getSWTHandle();
      if(!swtHandle.isDisposed()) {
        Point size = swtHandle.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        return new Dimension(size.x, size.y);
      }
    }
    return super.getPreferredSize();
  }
  
}

public interface CTabItem {

  public static class Factory {
    private Factory() {}

    public static CTabItem newInstance(TabItem tabItem, int style) {
      return new CTabItemImplementation(tabItem, style);
    }
  }
  
  public Component getContent();

}
