/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

public class JCoolBar extends JPanel {

  protected static final int FLOATING_MARGIN = 10;

  public JCoolBar() {
//    setComponentOrientation(java.awt.ComponentOrientation.ComponentOrientation.RIGHT_TO_LEFT);
    setLayout(new JCoolBarLayout());
  }

  protected boolean isLocked;

  public boolean isLocked() {
    return isLocked;
  }

  public void setLocked(boolean isLocked) {
    this.isLocked = isLocked;
    int componentCount = getComponentCount();
    for (int i = 0; i < componentCount; i++) {
      ((JCoolBarItem)getComponent(i)).setLocked(isLocked);
    }
  }

  public void setWrappedIndices(int[] wrappedIndices) {
    int componentCount = getComponentCount();
    boolean[] wrappedArray = new boolean[componentCount];
    for(int i=0; i<wrappedIndices.length; i++) {
      int wrappedIndex = wrappedIndices[i];
      if(wrappedIndex >= 0 && wrappedIndex < componentCount) {
        wrappedArray[wrappedIndex] = true;
      }
    }
    for (int i = 0; i < wrappedArray.length; i++) {
      ((JCoolBarItem)getComponent(i)).setWrapped(wrappedArray[i]);
    }
    revalidate();
    repaint();
  }

  public int getRowCount() {
    int rowCount = getItemCount()>0? 1: 0;
    for(int i=0; i<getItemCount(); i++) {
      if(getItem(i).isWrapped()) {
        rowCount++;
      }
    }
    return rowCount;
  }

  public int getItemCount() {
    return getComponentCount();
  }

  public JCoolBarItem getItem(int index) {
    return (JCoolBarItem)getComponent(index);
  }

  protected int getItemIndex(JCoolBarItem item) {
    int componentCount = getComponentCount();
    for(int i=0; i<componentCount; i++) {
      if(item == getComponent(i)) {
        return i;
      }
    }
    return -1;
  }

  protected Point mouseLocation;

  protected void processItemMouseEvent(MouseEvent mouseEvent) {
    switch(mouseEvent.getID()) {
      case MouseEvent.MOUSE_PRESSED: {
        this.mouseLocation = SwingUtilities.convertPoint(mouseEvent.getComponent(), mouseEvent.getPoint(), this);
        break;
      }
      case MouseEvent.MOUSE_DRAGGED: {
        JCoolBarItem item = (JCoolBarItem)mouseEvent.getComponent();
        Point mouseLocation = SwingUtilities.convertPoint(item, mouseEvent.getPoint(), this);
        Dimension size = getSize();
        int itemIndex = getItemIndex(item);
        if(isFloatable && (mouseLocation.x + FLOATING_MARGIN < 0 || mouseLocation.y + FLOATING_MARGIN < 0 || mouseLocation.x > size.width + FLOATING_MARGIN || mouseLocation.y > size.height + FLOATING_MARGIN)) {
          Point itemLocation = item.getLocation();
          SwingUtilities.convertPointToScreen(itemLocation, this);
          SwingUtilities.convertPointToScreen(mouseLocation, this);
          SwingUtilities.convertPointToScreen(this.mouseLocation, this);
          mouseLocation = new Point(itemLocation.x - this.mouseLocation.x + mouseLocation.x, itemLocation.y - this.mouseLocation.y + mouseLocation.y);
          setFloating(itemIndex, mouseLocation);
          this.mouseLocation = null;
          break;
        }
        int offsetX = mouseLocation.x - this.mouseLocation.x;
        if(mouseLocation.y < item.getY()) {
          if(getLineItems(itemIndex).length > 1) {
            moveUp(itemIndex);
          } else {
            mergeUp(itemIndex);
          }
        } else if(mouseLocation.y > item.getY() + item.getHeight()) {
          if(getLineItems(itemIndex).length > 1) {
            moveDown(itemIndex);
          } else {
            mergeDown(itemIndex);
          }
        }
        itemIndex = getItemIndex(item);
        boolean isLeftToRight = getComponentOrientation().isLeftToRight();
        if(offsetX < 0) {
          if(isLeftToRight) {
            moveLeading(itemIndex, -offsetX);
          } else {
            moveTrailing(itemIndex, -offsetX);
          }
        } else if(offsetX > 0) {
          if(isLeftToRight) {
            moveTrailing(itemIndex, offsetX);
          } else {
            moveLeading(itemIndex, offsetX);
          }
        }
        this.mouseLocation = mouseLocation;
        break;
      }
      case MouseEvent.MOUSE_RELEASED: {
        this.mouseLocation = null;
        JCoolBarItem item = (JCoolBarItem)mouseEvent.getComponent();
        item.setXSpacing(Math.max(0, item.getXSpacing()));
        break;
      }
    }
  }


  protected JCoolBarItem[] getLineItems(int itemIndex) {
    if(itemIndex < 0) {
      return new JCoolBarItem[0];
    }
    int count = getItemCount();
    if(itemIndex >= count) {
      return new JCoolBarItem[0];
    }
    JCoolBarItem item = getItem(itemIndex);
    if(itemIndex != 0 && !item.isWrapped()) {
      return getLineItems(itemIndex - 1);
    }
    Vector itemList = new Vector();
    for(int i=itemIndex; i<count; i++) {
      item = getItem(i);
      if(i != itemIndex && item.isWrapped()) {
        break;
      }
      itemList.add(item);
    }
    return (JCoolBarItem[])itemList.toArray(new JCoolBarItem[0]);
  }

  protected boolean isFloatable = false;

  public void setFloatable(boolean isFloatable) {
    this.isFloatable = isFloatable;
  }

  protected void setFloating(int itemIndex, Point mouseLocation) {
    if(isFloatable) {
      getItem(itemIndex).setFloating(true, mouseLocation);
    }
  }

  protected void moveUp(int itemIndex) {
    boolean isLeftToRight = getComponentOrientation().isLeftToRight();
    JCoolBarItem item = getItem(itemIndex);
    JCoolBarItem[] lineItems = getLineItems(itemIndex);
    JCoolBarItem lineItem0 = lineItems[0];
    if(lineItem0 == item) {
      lineItems[1].setWrapped(true);
    } else {
      int index = getItemIndex(lineItem0);
      remove(item);
      add(item, index);
      item.setWrapped(true);
      lineItem0.setWrapped(true);
    }
    JCoolBarItem lineItemN = lineItems[lineItems.length - 1];
    if(lineItemN != item) {
      JCoolBarItem nextItem = getItem(itemIndex + 1);
      nextItem.setXSpacing(Math.max(0, item.getXSpacing()));
    }
    int xSpacing = item.getXSpacing();
    int newXSpacing;
    if(isLeftToRight) {
      newXSpacing = item.getX();
    } else {
      newXSpacing = getWidth() - item.getX() - item.getWidth();
    }
    if(xSpacing < 0) {
      newXSpacing += xSpacing;
    }
    item.setXSpacing(newXSpacing);
    revalidate();
    repaint();
    setSize(new Dimension(getSize().width, getPreferredSize().height));
  }

  protected void mergeUp(int itemIndex) {
    boolean isLeftToRight = getComponentOrientation().isLeftToRight();
    JCoolBarItem[] lineItems = getLineItems(itemIndex-1);
    if(lineItems.length == 0) {
      return;
    }
    JCoolBarItem item = getItem(itemIndex);
    JCoolBarItem nextItem = null;
    int offset = 0;
    for(int i=0; i<lineItems.length; i++) {
      nextItem = lineItems[i];
      if(isLeftToRight) {
        if(nextItem.getX() >= item.getX()) {
          break;
        }
      } else {
        if(nextItem.getX() + nextItem.getWidth() <= item.getX() + item.getWidth()) {
          break;
        }
      }
      offset++;
      nextItem = null;
    }
    if(offset == lineItems.length) {
      item.setWrapped(false);
    } else {
      remove(item);
      add(item, itemIndex - lineItems.length + offset);
      if(offset == 0) {
        item.setWrapped(true);
        lineItems[0].setWrapped(false);
      } else {
        item.setWrapped(false);
      }
    }
    if(offset > 0) {
      JCoolBarItem previousLineItem = lineItems[offset - 1];
      if(isLeftToRight) {
        item.setXSpacing(item.getX() - previousLineItem.getX() - previousLineItem.getPreferredSize().width);
      } else {
        item.setXSpacing(previousLineItem.getX() + previousLineItem.getWidth() - previousLineItem.getPreferredSize().width - item.getX() - item.getWidth());
      }
    } else {
      int xSpacing = item.getXSpacing();
      int newXSpacing;
      if(isLeftToRight) {
        newXSpacing = item.getX();
      } else {
        newXSpacing = getWidth() - item.getX() - item.getWidth();
      }
      if(xSpacing < 0) {
        newXSpacing += xSpacing;
      }
      item.setXSpacing(newXSpacing);
    }
    if(offset < lineItems.length) {
      JCoolBarItem nextLineItem = lineItems[offset];
      if(isLeftToRight) {
        nextItem.setXSpacing(nextLineItem.getX() - item.getX() - item.getPreferredSize().width);
      } else {
        nextItem.setXSpacing(item.getX() + item.getWidth() - item.getPreferredSize().width - nextLineItem.getX() - nextLineItem.getWidth());
      }
    }
    revalidate();
    repaint();
    setSize(new Dimension(getSize().width, getPreferredSize().height));
  }

  protected void moveDown(int itemIndex) {
    boolean isLeftToRight = getComponentOrientation().isLeftToRight();
    JCoolBarItem item = getItem(itemIndex);
    JCoolBarItem[] lineItems = getLineItems(itemIndex);
    JCoolBarItem lineItem0 = lineItems[0];
    if(lineItem0 == item) {
      lineItems[1].setWrapped(true);
    }
    item.setWrapped(true);
    JCoolBarItem lineItemN = lineItems[lineItems.length - 1];
    if(lineItemN != item) {
      JCoolBarItem nextItem = getItem(itemIndex + 1);
      nextItem.setXSpacing(Math.max(0, item.getXSpacing()));
    }
    int index = getItemIndex(lineItemN);
    remove(item);
    add(item, index);
    int xSpacing = item.getXSpacing();
    int newXSpacing;
    if(isLeftToRight) {
      newXSpacing = item.getX();
    } else {
      newXSpacing = getWidth() - item.getX() - item.getWidth();
    }
    if(xSpacing < 0) {
      newXSpacing += xSpacing;
    }
    item.setXSpacing(newXSpacing);
    revalidate();
    repaint();
    setSize(new Dimension(getSize().width, getPreferredSize().height));
  }

  protected void mergeDown(int itemIndex) {
    boolean isLeftToRight = getComponentOrientation().isLeftToRight();
    JCoolBarItem[] lineItems = getLineItems(itemIndex+1);
    if(lineItems.length == 0) {
      return;
    }
    JCoolBarItem item = getItem(itemIndex);
    JCoolBarItem nextItem = null;
    int offset = 0;
    for(int i=0; i<lineItems.length; i++) {
      nextItem = lineItems[i];
      if(isLeftToRight) {
        if(nextItem.getX() >= item.getX()) {
          break;
        }
      } else {
        if(nextItem.getX() + nextItem.getWidth() <= item.getX() + item.getWidth()) {
          break;
        }
      }
      offset++;
      nextItem = null;
    }
    if(offset == 0) {
      item.setWrapped(true);
      lineItems[0].setWrapped(false);
    } else {
      remove(item);
      add(item, itemIndex + offset);
      item.setWrapped(false);
      JCoolBarItem previousLineItem = lineItems[offset - 1];
      if(isLeftToRight) {
        item.setXSpacing(item.getX() - previousLineItem.getX() - previousLineItem.getPreferredSize().width);
      } else {
        item.setXSpacing(previousLineItem.getX() + previousLineItem.getWidth() - previousLineItem.getPreferredSize().width - item.getX() - item.getWidth());
      }
    }
    if(offset < lineItems.length) {
      JCoolBarItem nextLineItem = lineItems[offset];
      if(isLeftToRight) {
        nextItem.setXSpacing(nextLineItem.getX() - item.getX() - item.getPreferredSize().width);
      } else {
        nextItem.setXSpacing(item.getX() + item.getWidth() - item.getPreferredSize().width - nextLineItem.getX() - nextLineItem.getWidth());
      }
    }
    revalidate();
    repaint();
    setSize(new Dimension(getSize().width, getPreferredSize().height));
  }

  protected void moveLeading(int itemIndex, int offsetX) {
    offsetX = -offsetX;
    JCoolBarItem item = getItem(itemIndex);
    int xSpacing = item.getXSpacing();
    int newXSpacing = xSpacing + offsetX;
    offsetX += Math.max(0, xSpacing);
    int shift = 0;
    int lineStartIndex = itemIndex - 1;
    if(!item.isWrapped()) {
      for(; offsetX < 0 && lineStartIndex>=0; lineStartIndex--) {
        JCoolBarItem previousItem = (JCoolBarItem)getComponent(lineStartIndex);
        int previousXSpacing = previousItem.getXSpacing();
        int diff = Math.min(-offsetX, previousXSpacing);
        offsetX += diff;
        shift += diff;
        previousItem.setXSpacing(previousXSpacing - diff);
        newXSpacing += diff;
        if(previousItem.isWrapped()) {
          lineStartIndex--;
          break;
        }
      }
    }
    lineStartIndex++;
    if(itemIndex + 1 < getComponentCount()) {
      JCoolBarItem nextItem = (JCoolBarItem)getComponent(itemIndex + 1);
      if(!nextItem.isWrapped()) {
        nextItem.setXSpacing(nextItem.getXSpacing() - newXSpacing + xSpacing + shift + Math.min(0, offsetX));
      }
    }
    if(newXSpacing < 0) {
      item.setXSpacing(newXSpacing);
      revalidate();
      int x = item.getX();
      boolean isLeftToRight = getComponentOrientation().isLeftToRight();
      for(int i = lineStartIndex; i<itemIndex; i++) {
        JCoolBarItem previousItem = (JCoolBarItem)getComponent(i);
        if(isLeftToRight) {
          int pX = previousItem.getX();
          if(pX >= x + newXSpacing) {
            remove(item);
            add(item, i);
            newXSpacing += x - pX;
            if(i == lineStartIndex) {
              item.setWrapped(true);
              previousItem.setWrapped(false);
            }
            break;
          }
        } else {
          int pX = previousItem.getX();
          if(pX + previousItem.getWidth() <= x + item.getWidth() - newXSpacing) {
            remove(item);
            add(item, i);
            newXSpacing += pX + previousItem.getWidth() - x - item.getWidth();
            if(i == lineStartIndex) {
              item.setWrapped(true);
              previousItem.setWrapped(false);
            }
            break;
          }
        }
      }
    }
    item.setXSpacing(newXSpacing);
    revalidate();
    repaint();
  }

  protected void moveTrailing(int itemIndex, int offsetX) {
    JCoolBarItem item = getItem(itemIndex);
    int xSpacing = item.getXSpacing();
    int newXSpacing = xSpacing + offsetX;
    int componentCount = getComponentCount();
    if(newXSpacing >= 0) {
      for(int i = itemIndex + 1; offsetX > 0 && i<componentCount; i++) {
        JCoolBarItem nextItem = (JCoolBarItem)getComponent(i);
        if(nextItem.isWrapped()) {
          break;
        }
        int nextXSpacing = nextItem.getXSpacing();
        int diff = Math.min(offsetX, nextXSpacing);
        offsetX -= diff;
        if(xSpacing < 0) {
          diff -= - xSpacing;
        }
        nextItem.setXSpacing(nextXSpacing - diff);
      }
    }
    item.setXSpacing(newXSpacing);
    revalidate();
    repaint();
  }
  
  protected static final Cursor GRIP_CURSOR = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);

  public Cursor getCursor() {
    if(!isLocked && isDragging()) {
      return GRIP_CURSOR;
    }
    return super.getCursor();
  }

  protected boolean isDragging() {
    return mouseLocation != null;
  }

  protected static final JToolBar TOOL_BAR = new JToolBar();

  static {
    TOOL_BAR.setFloatable(false);
  }

  public void updateUI() {
    TOOL_BAR.updateUI();
    super.updateUI();
  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int componentCount = getComponentCount();
    Dimension size = getSize();
    for(int i=0; i<componentCount; i++) {
      JCoolBarItem item = (JCoolBarItem)getComponent(i);
      if(i == 0 || item.isWrapped()) {
        Rectangle bounds = item.getBounds();
        g.translate(0, bounds.y);
        TOOL_BAR.setSize(size.width, bounds.height);
        TOOL_BAR.paint(g);
        g.translate(0, -bounds.y);
      }
    }
  }

  protected void addImpl(Component comp, Object constraints, int index) {
    JCoolBarItem coolBarItem = (JCoolBarItem)comp;
    coolBarItem.setLocked(isLocked);
    coolBarItem.setFloating(false, null);
    super.addImpl(comp, constraints, index);
  }

}
