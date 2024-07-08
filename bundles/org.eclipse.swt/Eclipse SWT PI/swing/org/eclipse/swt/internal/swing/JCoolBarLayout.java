/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class JCoolBarLayout implements LayoutManager {

  public void addLayoutComponent(String name, Component comp) {}

  public void removeLayoutComponent(Component comp) {}

  public void layoutContainer(Container parent) {
    boolean isLeftToRight = parent.getComponentOrientation().isLeftToRight(); 
    synchronized (parent.getTreeLock()) {
      Component[] components = parent.getComponents();
      if(components.length == 0) {
        return;
      }
      Dimension size = parent.getSize();
      int i=0;
      int lineY = 0;
      while(i != components.length) {
        int lineHeight = 0;
        int j=i;
        for(; j<components.length; j++) {
          JCoolBarItem coolBarItem = (JCoolBarItem)components[j];
          if(j != i && coolBarItem.isWrapped()) {
            break;
          }
          // TODO: Check why we have to add "+ 1"
          lineHeight = Math.max(lineHeight, coolBarItem.getItemPreferredSize().height + 1);
        }
        int lineX = 0;
        boolean isFirst = true;
        for(; i<components.length; i++) {
          JCoolBarItem coolBarItem = (JCoolBarItem)components[i];
          if(!isFirst && coolBarItem.isWrapped()) {
            break;
          }
          isFirst = false;
          int x = lineX + Math.max(0, coolBarItem.getXSpacing());
          int nextXSpacing = i < j-1? Math.max(0, ((JCoolBarItem)components[i+1]).getXSpacing()): 0;
          int nextPositionX = i < j-1? Math.max(0, ((JCoolBarItem)components[i+1]).getXSpacing()) + x + coolBarItem.getMinimumSize().width: size.width;
          if(isLeftToRight) {
            coolBarItem.setBounds(x, lineY, nextPositionX - x, lineHeight);
          } else {
            coolBarItem.setBounds(size.width - nextPositionX, lineY, nextPositionX - x, lineHeight);
          }
          lineX = nextPositionX - nextXSpacing;
        }
        lineY += lineHeight;
//            coolBarItem.setBounds(r)
      }
    }
  }

  public Dimension minimumLayoutSize(Container parent) {
    return new Dimension(0, 0);
  }

  public Dimension preferredLayoutSize(Container parent) {
    Component[] components = parent.getComponents();
    if(components.length == 0) {
      return new Dimension(0, 0);
    }
    boolean isNewLine = true;
    int maxWidth = 0;
    int maxHeight = 0;
    int lineHeight = 0;
    int width = 0;
    for(int i=0; i<components.length; i++) {
      JCoolBarItem coolBarItem = (JCoolBarItem)components[i];
      if(!isNewLine && coolBarItem.isWrapped()) {
        isNewLine = true;
        maxHeight += lineHeight;
        maxWidth = Math.max(maxWidth, width);
        width = 0;
      }
      isNewLine = false;
      Dimension preferredSize = coolBarItem.getItemPreferredSize();
      width = width + Math.max(0, coolBarItem.getXSpacing()) + preferredSize.width;
      // TODO: Check why we have to add "+ 1"
      lineHeight = Math.max(lineHeight, preferredSize.height + 1);
    }
    return new Dimension(Math.max(maxWidth, width), maxHeight + lineHeight);
  }

}
