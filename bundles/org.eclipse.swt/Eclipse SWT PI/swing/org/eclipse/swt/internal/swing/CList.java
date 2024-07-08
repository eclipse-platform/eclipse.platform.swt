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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;

class CListImplementation extends JScrollPane implements CList {

  protected List handle;
  protected JList list;

  public Container getSwingComponent() {
    return list;
  }

  public Control getSWTHandle() {
    return handle;
  }

  protected UserAttributeHandler userAttributeHandler;
  
  public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }
  
  public CListImplementation(List list, int style) {
    this.handle = list;
    this.list = new JList(new DefaultListModel()) {
      public Color getBackground() {
        return CListImplementation.this != null && userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
      }
      public Color getForeground() {
        return CListImplementation.this != null && userAttributeHandler != null && userAttributeHandler.foreground != null? userAttributeHandler.foreground: super.getForeground();
      }
      public Font getFont() {
        return CListImplementation.this != null && userAttributeHandler != null && userAttributeHandler.font != null? userAttributeHandler.font: super.getFont();
      }
      public Cursor getCursor() {
        if(Utils.globalCursor != null) {
          return Utils.globalCursor;
        }
        return CListImplementation.this != null && userAttributeHandler != null && userAttributeHandler.cursor != null? userAttributeHandler.cursor: super.getCursor();
      }
      public boolean isOpaque() {
        return backgroundImageIcon == null && super.isOpaque();
      }
    };
    this.list.setCellRenderer(new DefaultListCellRenderer() {
      public boolean isOpaque() {
        return CListImplementation.this.list.isOpaque() && super.isOpaque();
      }
    });
    userAttributeHandler = new UserAttributeHandler(this.list);
    JViewport viewport = new JViewport() {
      public boolean isOpaque() {
        return backgroundImageIcon == null && super.isOpaque();
      }
      protected void paintComponent(Graphics g) {
        Utils.paintTiledImage(this, g, backgroundImageIcon);
        super.paintComponent(g);
      }
      public Color getBackground() {
        return CListImplementation.this != null && userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
      }
    };
    setViewport(viewport);
    viewport.setView(this.list);
    init(style);
  }

  protected void init(int style) {
    setFont(list.getFont());
    if((style & SWT.BORDER) == 0) {
      setBorder(null);
      list.setBorder(null);
    }
    if((style & SWT.H_SCROLL) == 0) {
      setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
    }
    if((style & SWT.V_SCROLL) == 0) {
      setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
    }
    list.setSelectionMode((style & SWT.MULTI) != 0? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION: ListSelectionModel.SINGLE_SELECTION);
    Utils.installMouseListener(list, handle);
    Utils.installKeyListener(list, handle);
    Utils.installFocusListener(list, handle);
    Utils.installComponentListener(this, handle);
    list.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 2) {
          handle.processEvent(new ActionEvent(list, ActionEvent.ACTION_PERFORMED, null));
        }
      }
    });
    list.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if(!isAdjustingSelection) {
          handle.processEvent(e);
        }
      }
    });
  }

  public boolean isFocusable() {
    return list.isFocusable();
  }
  
  protected boolean isAdjustingSelection;
  
  public void requestFocus() {
    list.requestFocus();
    if(list.getModel().getSize() > 0 && list.getSelectedIndex() < 0) {
      isAdjustingSelection = true;
      ListSelectionModel selectionModel = list.getSelectionModel();
      if(selectionModel.getLeadSelectionIndex() < 0) {
        selectionModel.setAnchorSelectionIndex(0);
        selectionModel.setLeadSelectionIndex(0);
        selectionModel.clearSelection();
      }
      isAdjustingSelection = false;
    }
  }

  public void setFont(Font font) {
    super.setFont(font);
    if(list != null) {
      list.setFont(font);
    }
  }

  public Container getClientArea() {
    return list;
  }

  public Dimension getPreferredSize() {
    int itemCount = getItemCount();
    int height = super.getPreferredSize().height;
    if(itemCount > 0) {
      Rectangle bounds = list.getCellBounds(itemCount - 1, itemCount - 1);
      height = bounds.y + 2 * bounds.height + SwingUtilities.convertPoint(list, 0, 0, this).y + 1;
    }
    return new Dimension(super.getPreferredSize().width, height);
  }

  public void addElement(Object obj) {
    ((DefaultListModel)list.getModel()).addElement(obj);
  }

  public void insertElementAt(Object obj, int index) {
    ((DefaultListModel)list.getModel()).insertElementAt(obj, index);
  }

  public void removeElementAt(int index) {
    ((DefaultListModel)list.getModel()).removeElementAt(index);
  }

  public void removeRange(int fromIndex, int toIndex) {
    ((DefaultListModel)list.getModel()).removeRange(fromIndex, toIndex);
  }

  public void removeAllElements() {
    ((DefaultListModel)list.getModel()).removeAllElements();
  }

  public Object getElementAt(int index) {
    return list.getModel().getElementAt(index);
  }

  public void setElementAt(Object obj, int index) {
    ((DefaultListModel)list.getModel()).setElementAt(obj, index);
  }

  public void setElements(Object[] objects) {
    removeAllElements();
    DefaultListModel model = ((DefaultListModel)list.getModel());
    for(int i=0; i<objects.length; i++) {
      model.addElement(objects[i]);
    }
  }

  public int indexOf(Object obj, int index) {
    return ((DefaultListModel)list.getModel()).indexOf(obj, index);
  }

  public int getItemCount() {
    return list.getModel().getSize();
  }

  public int getMinSelectionIndex() {
    return list.getSelectionModel().getMinSelectionIndex();
  }

  public int getMaxSelectionIndex() {
    return list.getSelectionModel().getMaxSelectionIndex();
  }

  public int[] getSelectionIndices() {
    int min = getMinSelectionIndex();
    if(min == -1) {
      return new int[0];
    }
    int max = getMaxSelectionIndex();
    ArrayList list = new ArrayList(max - min + 1);
    for(int i=min; i<=max; i++) {
      if(isSelectedIndex(i)) {
        list.add(new Integer(i));
      }
    }
    int[] selectionIndices = new int[list.size()];
    for(int i=0; i<list.size(); i++) {
      selectionIndices[i] = ((Integer)list.get(i)).intValue();
    }
    return selectionIndices;
  }

  public boolean isSelectedIndex(int index) {
    return list.getSelectionModel().isSelectedIndex(index);
  }

  public void setSelectedElements(Object[] elements) {
    DefaultListModel listModel = ((DefaultListModel)list.getModel());
    for(int i=0; i<elements.length; i++) {
      int index = listModel.indexOf(elements[i]);
      if(index >= 0) {
        list.setSelectedIndex(index);
      }
    }
  }

  public void addSelectionInterval(int index0, int index1) {
    list.getSelectionModel().addSelectionInterval(index0, index1);
  }

  public void setSelectionInterval(int index0, int index1) {
    list.getSelectionModel().setSelectionInterval(index0, index1);
  }

  public void removeSelectionInterval(int index0, int index1) {
    list.getSelectionModel().removeSelectionInterval(index0, index1);
  }

  public void showSelection() {
    int min = list.getMinSelectionIndex();
    if(min == -1) {
      return;
    }
    int max = list.getMaxSelectionIndex();
    scrollRectToVisible(list.getCellBounds(min, max));
  }

  public int getFirstVisibleIndex() {
    return list.getFirstVisibleIndex();
  }

  public void setFirstVisibleIndex(int index) {
    if(index < 0) {
      return;
    }
    if(index >= getItemCount()) {
      return;
    }
    scrollRectToVisible(new Rectangle(0, 0, getWidth(), 0));
    scrollRectToVisible(list.getCellBounds(index, index));
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
      getViewport().setOpaque(true);
      list.setOpaque(true);
      break;
    }
    case BACKGROUND_INHERITANCE: {
      setOpaque(false);
      getViewport().setOpaque(false);
      list.setOpaque(false);
      break;
    }
    }
  }

  public Rectangle getCellBounds(int index) {
    return list.getCellBounds(index, index);
  }

  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    list.setEnabled(enabled);
  }
  
  public int getFocusIndex() {
    return list.getSelectionModel().getLeadSelectionIndex();
  }
  
}

public interface CList extends CScrollable {

  public static class Factory {
    private Factory() {}

    public static CList newInstance(List list, int style) {
      return new CListImplementation(list, style);
    }

  }

  public void addElement(Object obj);

  public void insertElementAt(Object obj, int index);

  public void removeElementAt(int index);

  public void removeRange(int fromIndex, int toIndex);

  public void removeAllElements();

  public Object getElementAt(int index);

  public void setElementAt(Object obj, int index);

  public void setElements(Object[] objects);

  public int indexOf(Object obj, int index);

  public int getItemCount();

  public int getMinSelectionIndex();

  public int getMaxSelectionIndex();

  public int[] getSelectionIndices();

  public boolean isSelectedIndex(int index);

  public void setSelectedElements(Object[] elements);

  public void addSelectionInterval(int index0, int index1);

  public void setSelectionInterval(int index0, int index1);

  public void removeSelectionInterval(int index0, int index1);

  public void showSelection();

  public int getFirstVisibleIndex();

  public void setFirstVisibleIndex(int index);

  public Rectangle getCellBounds(int index);
  
  public int getFocusIndex();

}
