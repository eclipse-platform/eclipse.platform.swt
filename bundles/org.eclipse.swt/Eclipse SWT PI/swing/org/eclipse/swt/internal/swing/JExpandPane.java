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
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

class JExpandPaneLayout implements LayoutManager {
  protected int spacing;
  public void setSpacing(int spacing) {
    this.spacing = spacing;
  }
  public int getSpacing() {
    return spacing;
  }
  public void layoutContainer(Container parent) {
    int width = Math.max(0, parent.getWidth() - 2 * spacing);
    int componentCount = parent.getComponentCount();
    int y = spacing;
    for(int i=0; i<componentCount; i++) {
      Component component = parent.getComponent(i);
      if(component.isVisible()) {
        Dimension size = component.getPreferredSize();
        component.setBounds(spacing, y, width, size.height);
        y += size.height + spacing;
      }
    }
  }
  public Dimension minimumLayoutSize(Container parent) {
    return preferredLayoutSize(parent);
  }
  public Dimension preferredLayoutSize(Container parent) {
    int height = spacing;
    int width = 0;
    int componentCount = parent.getComponentCount();
    for(int i=0; i<componentCount; i++) {
      Component component = parent.getComponent(i);
      if(component.isVisible()) {
        Dimension size = component.getPreferredSize();
        width = Math.max(width, size.width);
        height += size.height + spacing;
      }
    }
    return new Dimension(2 * spacing + width, height);
  }
  public void addLayoutComponent(String name, Component comp) {
  }
  public void removeLayoutComponent(Component comp) {
  }
}

public class JExpandPane extends JPanel {

  public static class JExpandPaneItem extends JPanel {

    protected Component component;
    protected JToolBar titleBar;
    protected JSeparator separator;
    protected JLabel titleLabel;
    protected JLabel expansionLabel;
    protected JComponent contentPane;

    protected static final Icon EXPANDED_ICON = UIManager.getIcon("Tree.expandedIcon");
    protected static final Icon COLLAPSED_ICON = UIManager.getIcon("Tree.collapsedIcon");

    protected MouseListener expansionMouseListener = new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        if(!new Rectangle(new Point(0, 0), e.getComponent().getSize()).contains(e.getPoint())) {
          return;
        }
        setExpanded(!isExpanded());
      }
    };

    public boolean isOpaque() {
      return getParent().isOpaque();
    }
    
    public JExpandPaneItem(String title, Icon icon, Component component) {
      super(new BorderLayout(0, 0));
      this.component = component;
      setBorder(LookAndFeelUtils.getStandardBorder());
      titleBar = new JToolBar();
      titleBar.setFloatable(false);
      titleBar.setLayout(new BorderLayout(0, 0));
      expansionLabel = new JLabel(COLLAPSED_ICON);
      expansionLabel.setBorder(new EmptyBorder(2, 5, 2, 5));
      titleBar.add(expansionLabel, getComponentOrientation().isLeftToRight()? BorderLayout.WEST: BorderLayout.EAST);
      titleLabel = new JLabel(title, icon, JLabel.LEADING);
      titleBar.add(titleLabel, BorderLayout.CENTER);
      titleBar.addMouseListener(expansionMouseListener);
      titleBar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      add(titleBar, BorderLayout.NORTH);
      separator = new JSeparator();
      add(separator, BorderLayout.CENTER);
      contentPane = new JPanel(new BorderLayout(0, 0));
      contentPane.setOpaque(false);
      contentPane.add(component, BorderLayout.CENTER);
      separator.setVisible(false);
      contentPane.setVisible(false);
      add(contentPane, BorderLayout.SOUTH);
    }

    public Dimension getMaximumSize() {
      return new Dimension(super.getMaximumSize().width, getPreferredSize().height);
    }

    public Component getComponent() {
      return component;
    }

    public void setIcon(Icon icon) {
      titleLabel.setIcon(icon);
    }

    public Icon getIcon() {
      return titleLabel.getIcon();
    }

    public void setExpanded(boolean isExpanded) {
      expansionLabel.setIcon(isExpanded? EXPANDED_ICON: COLLAPSED_ICON);
      separator.setVisible(isExpanded);
      contentPane.setVisible(isExpanded);
      contentPane.revalidate();
      repaint();
    }

    public boolean isExpanded() {
      return contentPane.isVisible();
    }

    public Dimension getTitleBarSize() {
      return titleBar.getSize();
    }

    public String getText() {
      return titleLabel.getText();
    }

    public void setText(String text) {
      titleLabel.setText(text);
    }

  }

  protected JExpandPaneLayout expandPaneLayout;

  public JExpandPane() {
    expandPaneLayout = new JExpandPaneLayout();
    setBackground(UIManager.getColor("desktop"));
    setLayout(expandPaneLayout);
  }

  public void addExpandPaneItem(String title, Icon icon, Component component) {
    add(new JExpandPaneItem(title, icon, component), getComponentCount());
  }

  public void insertExpandPaneItem(String title, Icon icon, Component component, int index) {
    add(new JExpandPaneItem(title, icon, component), index);
  }
  
  public void removeExpandPaneItem(Component component) {
    int componentCount = getComponentCount();
    for(int i=0; i<componentCount; i++) {
      if(((JExpandPaneItem)getComponent(i)).getComponent() == component) {
        remove(i);
        revalidate();
        repaint();
        break;
      }
    }
  }

  public void setComponentExpanded(Component component, boolean isExpanded) {
    int componentCount = getComponentCount();
    for(int i=0; i<componentCount; i++) {
      JExpandPaneItem expandedItem = (JExpandPaneItem)getComponent(i);
      if(expandedItem.getComponent() == component) {
        expandedItem.setExpanded(isExpanded);
        break;
      }
    }
  }

  public void setIndexExpanded(int index, boolean isExpanded) {
    if(index < 0 || index >= getComponentCount()) {
      return;
    }
    ((JExpandPaneItem)getComponent(index)).setExpanded(isExpanded);
  }

  public void addChangeListener(ChangeListener l) {
    listenerList.add(ChangeListener.class, l);
  }
  
  public void removeChangeListener(ChangeListener l) {
    listenerList.remove(ChangeListener.class, l);
  }
      
  public ChangeListener[] getChangeListeners() {
    return (ChangeListener[])listenerList.getListeners(ChangeListener.class);
  }

  public JExpandPaneItem getExpandItem(int index) {
    return (JExpandPaneItem)getComponent(index);
  }

  public JExpandPaneItem getExpandItem(Component component) {
    int componentCount = getComponentCount();
    for(int i=0; i<componentCount; i++) {
      JExpandPaneItem expandedItem = (JExpandPaneItem)getComponent(i);
      if(expandedItem.getComponent() == component) {
        return expandedItem;
      }
    }
    return null;
  }

  public void setSpacing(int spacing) {
    expandPaneLayout.setSpacing(spacing);
  }

  public int getSpacing() {
    return expandPaneLayout.getSpacing();
  }

}
