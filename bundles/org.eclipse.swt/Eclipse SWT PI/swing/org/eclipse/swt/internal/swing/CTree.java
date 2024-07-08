/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.PaintEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;

class CTreeImplementation extends JScrollPane implements CTree {

  protected Tree handle;
  protected JTreeTable treeTable;
  protected DefaultMutableTreeTableNode rootNode;
  protected boolean isCheckType;

  public Container getSwingComponent() {
    return treeTable;
  }

  public Control getSWTHandle() {
    return handle;
  }

  protected class CheckBoxCellRenderer extends JPanel {
    protected JStateCheckBox checkBox = new JStateCheckBox();
    protected Component component;
    public CheckBoxCellRenderer(Component component) {
      super(new BorderLayout(0, 0));
      this.component = component;
      setOpaque(false);
      checkBox.setOpaque(false);
      add(checkBox, BorderLayout.WEST);
      add(component, BorderLayout.CENTER);
      addNotify();
    }
    public JStateCheckBox getStateCheckBox() {
      return checkBox;
    }
    public Component getComponent() {
      return component;
    }
  }

  protected UserAttributeHandler userAttributeHandler;
  
  public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }
  
  public CTreeImplementation(Tree tree, int style) {
    handle = tree;
    setViewport(new JViewport() {
      public boolean isOpaque() {
        return backgroundImageIcon == null && super.isOpaque();
      }
      protected void paintComponent(Graphics g) {
        Utils.paintTiledImage(this, g, backgroundImageIcon);
        super.paintComponent(g);
      }
      public Color getBackground() {
        return CTreeImplementation.this != null && userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
      }
    });
    rootNode = new DefaultMutableTreeTableNode() {
      public void insert(MutableTreeNode newChild, int childIndex) {
        super.insert(newChild, childIndex);
        getModel().nodesWereInserted(this, new int[] {childIndex});
        treeTable.expandPath(new TreePath(rootNode.getPath()));
      }
    };
    treeTable = new JTreeTable(new DefaultTreeModel(rootNode)) {
      protected JTableHeader createDefaultTableHeader() {
        return new JTableHeader(getColumnModel()) {
          public String getToolTipText(MouseEvent e) {
            int index = columnModel.getColumnIndexAtX(e.getX());
            if(index < 0) {
              return null;
            }
            return ((CTreeColumn)columnModel.getColumn(index)).getToolTipText();
          }
          public void paint(Graphics g) {
            super.paint(g);
            if(handle.getSortDirection() != SWT.NONE) {
              org.eclipse.swt.widgets.TreeColumn sortColumn = handle.getSortColumn();
              if(sortColumn != null) {
                Rectangle bounds = getCellRect(-1, CTreeImplementation.this.treeTable.convertColumnIndexToView(handle.indexOf(sortColumn)), false);
                TableColumn draggedColumn = getDraggedColumn();
                Shape clip = g.getClip();
                if(draggedColumn != null) {
                  int draggedDistance = getDraggedDistance();
                  if(((CTreeColumn)draggedColumn).getTreeColumn() == sortColumn) {
                    bounds.x += draggedDistance;
                  } else {
                    Rectangle dragBounds = getCellRect(-1, CTreeImplementation.this.treeTable.convertColumnIndexToView(handle.indexOf(((CTreeColumn)draggedColumn).getTreeColumn())), true);
                    dragBounds.x += draggedDistance;
                    bounds.height = getHeight();
                    if(bounds.x < dragBounds.x) {
                      g.clipRect(0, 0, dragBounds.x, bounds.height);
                    } else {
                      int x = dragBounds.x + dragBounds.width;
                      g.clipRect(x, 0, getWidth() - x, bounds.height);
                    }
                  }
                }
                paintSortArrow(g, bounds);
                g.setClip(clip);
              }
            }
          }
          protected void paintSortArrow(Graphics g, Rectangle bounds) {
//            Color color = new Color(11, 80, 48);             
            Color color = getBackground().darker();             
            int priority = 0;
            int height = Math.round(getHeight() / 1.5f);
            int x = bounds.x + bounds.width;
            int y = 1;
            boolean descending = handle.getSortDirection() == SWT.DOWN;
            // In a compound sort, make each succesive triangle 20% 
            // smaller than the previous one. 
            int dx = (int)(height/2*Math.pow(0.8, priority));
            if(bounds.width < dx * 3) {
              return;
            }
            x -= dx * 2;
            if(dx % 2 != 0) {
              dx++;
            }
            int dy = descending ? dx / 2 : -dx / 2;
            // Align icon (roughly) with font baseline. 
            y = y + 4*height/6 + (descending ? -dy : 0);
            int shift = descending ? 1 : -1;
            g.translate(x, y);

            g.setColor(color);
            g.fillPolygon(new int[] {0, dx / 2, dx, 0}, new int[] {0, dy, 0, 0}, 4);

            // Right diagonal. 
            g.setColor(color.darker());
            g.drawLine(dx / 2, dy, 0, 0);
            g.drawLine(dx / 2, dy + shift, 0, shift);
            
            // Left diagonal. 
            g.setColor(color.brighter());
            g.drawLine(dx / 2, dy, dx, 0);
            g.drawLine(dx / 2, dy + shift, dx, shift);
            
            // Horizontal line. 
            if (descending) {
                g.setColor(color.darker().darker());
            } else {
//                g.setColor(color.brighter().brighter());
                g.setColor(color.brighter());
            }
            g.drawLine(dx, 0, 0, 0);

            g.setColor(color);
            g.translate(-x, -y);
          }
        };
      }
      public boolean getScrollableTracksViewportWidth() {
        return handle.isDisposed()? false: handle.getColumnCount() == 0 && getPreferredSize().width < getParent().getWidth();
      }
      public boolean getScrollableTracksViewportHeight() {
        return getPreferredSize().height < getParent().getHeight();
      }
      public Dimension getPreferredScrollableViewportSize() {
        if(handle.getColumnCount() != 0) {
          return getPreferredSize();
        }
        // TODO: use some caching mecanism?
        int columnCount = treeTable.getColumnModel().getColumnCount();
        int width = 0;
        for(int i=0; i<columnCount; i++) {
          width += getPreferredColumnWidth(i);
        }
//        if(isGridVisible()) {
          width += columnCount - 1;
//        }
        // TODO: check why we need to add the columnCount again.
        width += columnCount;
        return new Dimension(width, getPreferredSize().height);
      }
      protected boolean processMouseOnTreeRenderer(int row, MouseEvent e, Dimension cellSize) {
        if(isCheckType) {
          TreePath treePath = treeTable.getPathForRow(row);
          DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode)treePath.getLastPathComponent();
          CheckBoxCellRenderer checkBoxCellRenderer = (CheckBoxCellRenderer)treeTable.getCellRenderer().getTreeTableCellRendererComponent(treeTable, node.getUserObject(), treeTable.isRowSelected(row), treeTable.isExpanded(treePath), node.isLeaf(), row, 0, false);
          checkBoxCellRenderer.setSize(cellSize);
          checkBoxCellRenderer.doLayout();
          Point point = e.getPoint();
          Component component = checkBoxCellRenderer.getComponentAt(point);
          JStateCheckBox stateCheckBox = checkBoxCellRenderer.getStateCheckBox();
          // TODO: find a way to rely on the Look&Feel for mouse events.
          if(component == stateCheckBox) {
            switch(e.getID()) {
            case MouseEvent.MOUSE_PRESSED:
              CTreeItem.TreeItemObject treeItemObject = (CTreeItem.TreeItemObject)node.getUserObject(0);
              boolean ischecked = !treeItemObject.isChecked();
              treeItemObject.getTreeItem().setChecked(ischecked);
              ((DefaultTreeModel)treeTable.getModel()).nodeChanged(node);
              handle.processEvent(new ItemEvent(stateCheckBox, ItemEvent.ITEM_STATE_CHANGED, node, ischecked? ItemEvent.SELECTED: ItemEvent.DESELECTED));
            case MouseEvent.MOUSE_DRAGGED:
            case MouseEvent.MOUSE_RELEASED:
            case MouseEvent.MOUSE_CLICKED:
              return false;
            }
          }
        }
        switch(e.getID()) {
        case MouseEvent.MOUSE_PRESSED:
          switch(e.getButton()) {
          case MouseEvent.BUTTON1: {
            // We have to assume that the mouse button 1 is the one and only to trigger mouse selection events.
            TreeSelectionModel selectionModel = getSelectionModel();
            if(selectionModel.getSelectionCount() == 1) {
              if(selectionModel.isRowSelected(row)) {
                // This is to satisfy the fact that re-clicking on the sole-selected node should re-trigger selection
                selectionModel.clearSelection();
              }
            }
            break;
          }
          case MouseEvent.BUTTON3: {
            // We have to assume that popup menus are triggered with the mouse button 3.
            TreeSelectionModel selectionModel = getSelectionModel();
            if(!selectionModel.isRowSelected(row)) {
              selectionModel.setSelectionPath(getPathForRow(row));
            }
            break;
          }
          }
          break;
        }
        return super.processMouseOnTreeRenderer(row, e, cellSize);
      }
      public Color getBackground() {
        return CTreeImplementation.this != null && userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
      }
      public Color getForeground() {
        return CTreeImplementation.this != null && userAttributeHandler != null && userAttributeHandler.foreground != null? userAttributeHandler.foreground: super.getForeground();
      }
      public Font getFont() {
        return CTreeImplementation.this != null && userAttributeHandler != null && userAttributeHandler.font != null? userAttributeHandler.font: super.getFont();
      }
      public Cursor getCursor() {
        if(Utils.globalCursor != null) {
          return Utils.globalCursor;
        }
        return CTreeImplementation.this != null && userAttributeHandler != null && userAttributeHandler.cursor != null? userAttributeHandler.cursor: super.getCursor();
      }
      protected Graphics graphics;
      public Graphics getGraphics() {
        Graphics g;
        if(graphics != null) {
          g = graphics.create();
        } else {
          g = super.getGraphics();
        }
        return g;
      }
      public boolean isOpaque() {
        return backgroundImageIcon == null && super.isOpaque();
      }
      protected void paintComponent (Graphics g) {
        graphics = g;
        putClientProperty(Utils.SWTSwingGraphics2DClientProperty, g);
        super.paintComponent(g);
        handle.processEvent(new PaintEvent(this, PaintEvent.PAINT, null));
        putClientProperty(Utils.SWTSwingGraphics2DClientProperty, null);
        graphics = null;
      }
    };
    userAttributeHandler = new UserAttributeHandler(treeTable);
    treeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    treeTable.setCellRenderer(new DefaultTreeTableCellRenderer() {
      protected boolean isInitialized;
      protected boolean isDefaultOpaque;
      protected Color defaultForeground;
      protected Color defaultBackground;
      protected Font defaultFont;
      protected boolean isSelectionOpaque;
      protected Color selectionForeground;
      protected Color selectionBackground;
      protected Font selectionFont;
      public Component getTreeTableCellRendererComponent(JTreeTable treeTable, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, int column, boolean hasFocus) {
        if(value instanceof CTreeItem.TreeItemObject) {
          treeItemObject = (CTreeItem.TreeItemObject)value;
          CellPaintEvent event = new CellPaintEvent(treeTable, CellPaintEvent.ERASE_TYPE);
          event.row = row;
          event.column = column;
          event.treeItem = treeItemObject.getTreeItem();
          event.ignoreDrawSelection = !isSelected;
          event.ignoreDrawFocused = !hasFocus;
          handle.processEvent(event);
          ignoreDrawForeground = event.ignoreDrawForeground;
          ignoreDrawBackground = event.ignoreDrawBackground;
          ignoreDrawSelection = event.ignoreDrawSelection;
          ignoreDrawFocused = event.ignoreDrawFocused;
          isSelected = !event.ignoreDrawSelection;
          hasFocus = !event.ignoreDrawFocused;
          this.row = row;
          this.column = column;
        } else {
          treeItemObject = null;
        }
        if(!isInitialized) {
          Component c = super.getTreeTableCellRendererComponent(treeTable, "", true, expanded, leaf, row, column, hasFocus);
          if(c instanceof JComponent) {
            isSelectionOpaque = ((JComponent)c).isOpaque();
          }
          selectionForeground = c.getForeground();
          selectionBackground = c.getBackground();
          selectionFont = c.getFont();
        }
        if(!isInitialized) {
          Component c = super.getTreeTableCellRendererComponent(treeTable, "", false, expanded, leaf, row, column, hasFocus);
          if(c instanceof JComponent) {
            isDefaultOpaque = ((JComponent)c).isOpaque();
          }
          defaultForeground = c.getForeground();
          defaultBackground = c.getBackground();
          defaultFont = c.getFont();
          isInitialized = true;
        }
        Component c = super.getTreeTableCellRendererComponent(treeTable, value, isSelected, expanded, leaf, row, column, hasFocus);
        if(value != null) {
          Color userForeground = userAttributeHandler.foreground;
          c.setForeground(isSelected? selectionForeground: userForeground != null? userForeground: defaultForeground);
          Color userBackground = userAttributeHandler.background;
          c.setBackground(isSelected? selectionBackground: userBackground != null? userBackground: defaultBackground);
          Font userFont = userAttributeHandler.font;
          c.setFont(isSelected? selectionFont: userFont != null? userFont: defaultFont);
          if(c instanceof JComponent) {
            ((JComponent)c).setOpaque(isSelected? isSelectionOpaque: isDefaultOpaque && treeTable.isOpaque());
          }
          CTreeItem.TreeItemObject treeItemObject = (CTreeItem.TreeItemObject)value;
          if(treeItemObject != null) {
            if(c instanceof JLabel) {
              TableColumn tableColumn = treeTable.getColumnModel().getColumn(column);
              JLabel label = (JLabel)c;
              if(tableColumn instanceof CTreeColumn) {
                CTreeColumn treeColumn = (CTreeColumn)tableColumn;
                label.setHorizontalAlignment(treeColumn.getAlignment());
              }
              label.setIcon(treeItemObject.getIcon());
            }
            CTreeItem cTreeItem = treeItemObject.getTreeItem();
            Color foreground = treeItemObject.getForeground();
            if(foreground != null) {
              c.setForeground(foreground);
            } else {
              foreground = cTreeItem.getForeground();
              if(foreground != null) {
                c.setForeground(foreground);
              }
            }
            if(!isSelected) {
              Color background = treeItemObject.getBackground();
              if(background != null) {
                if(c instanceof JComponent) {
                  ((JComponent)c).setOpaque(true);
                }
                c.setBackground(background);
              } else {
                background = cTreeItem.getBackground();
                if(background != null) {
                  if(c instanceof JComponent) {
                    ((JComponent)c).setOpaque(true);
                  }
                  c.setBackground(background);
                }
              }
            }
            Font font = treeItemObject.getFont();
            if(font != null) {
              c.setFont(font);
            } else {
              font = cTreeItem.getFont();
              if(font != null) {
                c.setFont(font);
              }
            }
          }
        }
        if(!isCheckType || column != 0) {
          return c;
        }
        CheckBoxCellRenderer checkBoxCellRenderer = new CheckBoxCellRenderer(c);
        if(treeItemObject != null) {
          checkBoxCellRenderer.getStateCheckBox().setSelected(treeItemObject.isChecked());
        }
        return checkBoxCellRenderer;
      }
      protected CTreeItem.TreeItemObject treeItemObject;
      protected int row;
      protected int column;
      protected boolean ignoreDrawForeground;
      protected boolean ignoreDrawBackground;
      protected boolean ignoreDrawSelection;
      protected boolean ignoreDrawFocused;
      protected void paintComponent(CellPainter c, Graphics g) {
        if(ignoreDrawForeground) {
          if(c instanceof JLabel) {
            ((JLabel)c).setText(null);
          }
        }
        if(ignoreDrawBackground) {
          setOpaque(false);
        }
//        graphics = g;
        super.paintComponent(c, g);
        if(treeItemObject != null) {
          CellPaintEvent event = new CellPaintEvent(treeTable, CellPaintEvent.PAINT_TYPE);
          event.row = row;
          event.column = column;
          event.treeItem = treeItemObject.getTreeItem();
          event.ignoreDrawForeground = this.ignoreDrawForeground;
          event.ignoreDrawBackground = this.ignoreDrawBackground;
          event.ignoreDrawSelection = this.ignoreDrawSelection;
          event.ignoreDrawFocused = this.ignoreDrawFocused;
          handle.processEvent(event);
        }
//        graphics = null;
      }
      protected InnerTreeCellRenderer createInnerTreeCellRenderer() {
        return new InnerTreeCellRenderer() {
          protected boolean isCreated = true;
          public Color getBackgroundNonSelectionColor() {
            return getBackground();
//            return !isCreated || !treeTable.isOpaque()? null: super.getBackgroundNonSelectionColor();
          }
          public Color getBackground() {
            if(isOpaque()) {
              return super.getBackground();
            }
            if(isCreated) {
              if(!treeTable.isOpaque()) {
                return null;
              }
              Color background = userAttributeHandler.background;
              return background != null? background: treeTable.getBackground();
            }
            return super.getBackground();
          }
        };
      }
    });
    final JTableHeader tableHeader = treeTable.getTableHeader();
    class HeaderMouseListener extends MouseAdapter implements MouseMotionListener {
      public void mouseClicked(MouseEvent e) {
        TableColumnModel columnModel = tableHeader.getColumnModel();
        int columnIndex = columnModel.getColumnIndexAtX(e.getX());
        if(columnIndex != -1) {
          CTreeColumn column = (CTreeColumn)columnModel.getColumn(columnIndex);
          column.getTreeColumn().processEvent(e);
        }
      }
      public void mouseEntered(MouseEvent e) {
        setHeaderOrderingState(e);
      }
      public void mouseMoved(MouseEvent e) {
        setHeaderOrderingState(e);
      }
      public void mouseDragged(MouseEvent e) {
      }
      protected void setHeaderOrderingState(MouseEvent e) {
        TableColumnModel columnModel = tableHeader.getColumnModel();
        int columnIndex = columnModel.getColumnIndexAtX(e.getX());
        if(columnIndex != -1) {
          CTreeColumn column = (CTreeColumn)columnModel.getColumn(columnIndex);
          tableHeader.setReorderingAllowed(column.getTreeColumn().getMoveable());
        }
      }
    }
    tableHeader.getColumnModel().addColumnModelListener(new TableColumnModelListener() {
      public void columnAdded(TableColumnModelEvent e) {
      }
      public void columnMarginChanged(ChangeEvent e) {
      }
      public void columnMoved(TableColumnModelEvent e) {
        if(isAdjustingColumnOrder) {
          return;
        }
        int toIndex = e.getToIndex();
        int fromIndex = e.getFromIndex();
        if(fromIndex != toIndex) {
          TableColumnModel columnModel = getColumnModel();
          CTreeColumn cTreeColumn = (CTreeColumn)columnModel.getColumn(toIndex);
          cTreeColumn.getTreeColumn().processEvent(e);
          cTreeColumn = (CTreeColumn)columnModel.getColumn(fromIndex);
          cTreeColumn.getTreeColumn().processEvent(e);
        }
      }
      public void columnRemoved(TableColumnModelEvent e) {
      }
      public void columnSelectionChanged(ListSelectionEvent e) {
      }
    });
    HeaderMouseListener headerMouseListener = new HeaderMouseListener();
    tableHeader.addMouseListener(headerMouseListener);
    tableHeader.addMouseMotionListener(headerMouseListener);
    final TableCellRenderer headerRenderer = tableHeader.getDefaultRenderer();
    tableHeader.setDefaultRenderer(new TableCellRenderer() {
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = headerRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(c instanceof JLabel) {
          TableColumn tableColumn = treeTable.getColumnModel().getColumn(column);
          if(tableColumn instanceof CTreeColumn) {
            JLabel label = (JLabel)c;
            CTreeColumn treeColumn = (CTreeColumn)tableColumn;
            label.setHorizontalAlignment(treeColumn.getAlignment());
            label.setIcon(treeColumn.getIcon());
          }
        }
        return c;
      }
    });
    // TODO: add a first bogus column? (1)
    javax.swing.table.TableColumnModel columnModel = treeTable.getColumnModel();
    javax.swing.table.TableColumn tableColumn = new javax.swing.table.TableColumn(0);
    columnModel.addColumn(tableColumn);

//    treeTable.expandedPath(new TreePath(rootNode.getPath()));
    treeTable.getInnerTree().setRootVisible(false);
    treeTable.getInnerTree().setShowsRootHandles(true);
    setFocusable(false);
    getViewport().setView(treeTable);
    setGridVisible(false);
    setColumnHeader(createViewport());
    setHeaderVisible(false);
    init(style);
    treeTable.configureEnclosingScrollPane();
  }

  protected void init(int style) {
    isCheckType = (style & SWT.CHECK) != 0;
    if((style & SWT.BORDER) != 0) {
      setBorder(LookAndFeelUtils.getStandardBorder());
    } else {
      setBorder(BorderFactory.createEmptyBorder());
    }
//    if((style & SWT.H_SCROLL) == 0) {
//      setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
//    }
//    if((style & SWT.V_SCROLL) == 0) {
//      setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
//    }
//    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
//      setBorder(null);
//    }
    if((style & SWT.MULTI) == 0) {
      treeTable.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }
    if((style & SWT.FULL_SELECTION) != 0) {
      treeTable.setFullLineSelection(true);
    }
    treeTable.addTreeExpansionListener(new TreeExpansionListener() {
      public void treeCollapsed(TreeExpansionEvent e) {
        handle.processEvent(e);
      }
      public void treeExpanded(TreeExpansionEvent e) {
        handle.processEvent(e);
      }
    });
    treeTable.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        handle.processEvent(e);
        treeTable.repaint();
      }
    });
    // TODO: Map other events for table header click etc.
    JTable innerTable = treeTable.getInnerTable();
    Utils.installMouseListener(innerTable, handle);
    Utils.installKeyListener(innerTable, handle);
    Utils.installFocusListener(innerTable, handle);
    Utils.installComponentListener(this, handle);
  }

  public Container getClientArea() {
    return treeTable.getInnerTable();
  }

  public void clearSelection() {
    treeTable.clearSelection();
  }

  public void selectAll() {
    treeTable.selectAll();
  }

  public TreeSelectionModel getSelectionModel() {
    return treeTable.getSelectionModel();
  }

  public void setGridVisible(boolean isGridVisible) {
    treeTable.setGridVisible(isGridVisible);
  }

  public boolean isGridVisible() {
    return treeTable.isGridVisible();
  }

  public JTableHeader getTableHeader() {
    return treeTable.getTableHeader();
  }

  public void expandPath(TreePath treePath) {
    treeTable.expandPath(treePath);
  }

  public void collapsePath(TreePath treePath) {
    treeTable.collapsePath(treePath);
  }

  public boolean isExpanded(TreePath path) {
    return treeTable.isExpanded(path);
  }

  public DefaultMutableTreeTableNode getRoot() {
    return rootNode;
  }

  public DefaultTreeModel getModel() {
    return (DefaultTreeModel)treeTable.getModel();
  }

  public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
    Rectangle cellRect = treeTable.getCellRect(row, column, includeSpacing);
    if(column == 0) {
      int dx = treeTable.getInnerTree().getRowBounds(row).x;
      cellRect.x += dx;
      cellRect.width -= dx;
    }
    if(isCheckType && column == 0) {
      TreePath treePath = treeTable.getPathForRow(row);
      DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode)treePath.getLastPathComponent();
      Object value = node.getUserObject();
      Component c = treeTable.getCellRenderer().getTreeTableCellRendererComponent(treeTable, value, treeTable.isRowSelected(row), treeTable.isExpanded(treePath), node.isLeaf(), row, column, false);
      c.setBounds(cellRect);
      if(c instanceof CheckBoxCellRenderer) {
        CheckBoxCellRenderer checkBoxCellRenderer = (CheckBoxCellRenderer)c;
        c = checkBoxCellRenderer.getComponent();
        int dx = c.getBounds().x;
        cellRect.x += dx;
        cellRect.width -= dx;
      }
    }
    return cellRect;
  }

  public int getRowForPath(TreePath path) {
    return treeTable.getRowForPath(path);
  }

  public TableColumnModel getColumnModel() {
    return treeTable.getColumnModel();
  }

  public TreePath getPathForLocation(int x, int y) {
    Insets insets = getInsets();
    x -= insets.left;
    y -= insets.top;
    return treeTable.getPathForLocation(x, y);
  }

  public int getPreferredColumnWidth(int columnIndex) {
    return treeTable.getPreferredColumnWidth(columnIndex);
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
      treeTable.setOpaque(true);
      break;
    case BACKGROUND_INHERITANCE:
      setOpaque(false);
      getViewport().setOpaque(false);
      treeTable.setOpaque(false);
      break;
    }
  }

  public int getRowHeight() {
    return treeTable.getRowHeight();
  }

  public void setRowHeight(int rowHeight) {
    treeTable.setRowHeight(rowHeight);
  }

  public void ensureRowVisible(int index) {
    if(index < 0 || index >= treeTable.getRowCount()) {
      return;
    }
    Rectangle bounds = getCellRect(index, 0, true);
    bounds.width = treeTable.getWidth();
    bounds.height = treeTable.getRowHeight(index);
    treeTable.scrollRectToVisible(bounds);
  }

  public void ensureColumnVisible(int index) {
    if(index < 0 || index >= treeTable.getColumnCount()) {
      return;
    }
    Rectangle bounds = new Rectangle();
    TableColumnModel columnModel = getColumnModel();
    for(int i=0; i<index; i++) {
      bounds.x += columnModel.getColumn(i).getPreferredWidth();
    }
    bounds.width = columnModel.getColumn(index).getPreferredWidth();
    bounds.height = treeTable.getHeight();
    treeTable.scrollRectToVisible(bounds);
  }
  
  public int rowAtPoint(Point point) {
    point = SwingUtilities.convertPoint(this, point.x, point.y, treeTable);
    return treeTable.rowAtPoint(point);
  }

  public int getTopIndex() {
    return rowAtPoint(new Point(0, 0));
  }
  
  public void setTopIndex(int index) {
    ensureRowVisible(index);
    if(index != 0) {
      ensureRowVisible(index);
    }
  }

  public TreePath getPathForRow(int index) {
    return treeTable.getPathForRow(index);
  }

  public void setHeaderVisible(boolean isHeaderVisible) {
    getColumnHeader().setVisible(isHeaderVisible);
    treeTable.getTableHeader().setVisible(isHeaderVisible);
  }

  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    treeTable.setEnabled(enabled);
  }
  
  public boolean isFocusable() {
    return treeTable.isFocusable();
  }

  public void requestFocus() {
    treeTable.requestFocus();
  }
  
  protected boolean isAdjustingColumnOrder;
  
  public void setColumnOrder(int[] order) {
    isAdjustingColumnOrder = true;
    JTable table = treeTable.getInnerTable();
    for(int i=0; i<order.length; i++) {
      table.moveColumn(table.convertColumnIndexToView(order[i]), i);
    }
    isAdjustingColumnOrder = false;
    for(int i=0; i<order.length; i++) {
      CTreeColumn cTreeColumn = (CTreeColumn)getColumnModel().getColumn(i);
      cTreeColumn.getTreeColumn().processEvent(new TableColumnModelEvent(table.getColumnModel(), i, order[i]));
    }
  }
  
  public int[] getColumnOrder() {
    JTable table = treeTable.getInnerTable();
    int[] order = new int[table.getColumnCount()];
    for(int i=0; i<order.length; i++) {
      order[i] = table.convertColumnIndexToModel(i);
    }
    return order;
  }
  
  public Rectangle getImageBounds(int row, int column) {
    TreePath treePath = treeTable.getPathForRow(row);
    DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode)treePath.getLastPathComponent();
    Object value = node.getUserObject();
    Component c = treeTable.getCellRenderer().getTreeTableCellRendererComponent(treeTable, value, treeTable.isRowSelected(row), treeTable.isExpanded(treePath), node.isLeaf(), row, column, false);
    Rectangle cellRect = treeTable.getCellRect(row, column, false);
    if(column == 0) {
      int dx = treeTable.getInnerTree().getRowBounds(row).x;
      cellRect.x += dx;
      cellRect.width -= dx;
    }
    c.setBounds(cellRect);
    Rectangle bounds = new Rectangle();
    if(c instanceof CheckBoxCellRenderer) {
      CheckBoxCellRenderer checkBoxCellRenderer = (CheckBoxCellRenderer)c;
      c = checkBoxCellRenderer.getComponent();
      bounds.x += c.getBounds().x;
    }
    if(c instanceof JLabel) {
      Rectangle iconR = new Rectangle();
      JLabel label = (JLabel)c;
      SwingUtilities.layoutCompoundLabel(label.getFontMetrics(label.getFont()), label.getText(), label.getIcon(), label.getVerticalAlignment(), label.getHorizontalAlignment(), label.getVerticalTextPosition(), label.getHorizontalTextPosition(), new Rectangle(), iconR, new Rectangle(), label.getIconTextGap());
      bounds.x += iconR.x;
      bounds.y += iconR.y;
      bounds.width = iconR.width;
      bounds.height = iconR.height;
    }
    if(column == 0) {
      bounds.x += treeTable.getInnerTree().getRowBounds(row).x;
    }
    return bounds;
  }

}

public interface CTree extends CComposite {

  public static class CellPaintEvent extends EventObject {
    
    public static final int ERASE_TYPE = 1;
    public static final int PAINT_TYPE = 2;
    public static final int MEASURE_TYPE = 3;
    protected int type;
    public int row;
    public int column;
    public CTreeItem treeItem;
    public boolean ignoreDrawForeground;
    public boolean ignoreDrawBackground;
    public boolean ignoreDrawSelection;
    public boolean ignoreDrawFocused;
    public int rowHeight;

    CellPaintEvent(Object source, int type) {
      super(source);
      this.type = type;
    }

    public int getType() {
      return type;
    }
    
  }
  
  public static class Factory {
    private Factory() {}

    public static CTree newInstance(Tree tree, int style) {
      return new CTreeImplementation(tree, style);
    }

  }

  public void clearSelection();

  public void selectAll();

  public TreeSelectionModel getSelectionModel();

  public void setGridVisible(boolean isGridVisible);

  public boolean isGridVisible();

  public JTableHeader getTableHeader();

  public void expandPath(TreePath treePath);

  public void collapsePath(TreePath treePath);

  public boolean isExpanded(TreePath path);

  public DefaultMutableTreeTableNode getRoot();

  public DefaultTreeModel getModel();

  public Rectangle getCellRect(int row, int column, boolean includeSpacing);

  public int getRowForPath(TreePath path);

  public TableColumnModel getColumnModel();

  public TreePath getPathForLocation(int x, int y);

  public int getPreferredColumnWidth(int columnIndex);

  public int getRowHeight();

  public void setRowHeight(int rowHeight);

  public void ensureRowVisible(int index);

  public void ensureColumnVisible(int index);

  public int getTopIndex();

  public void setTopIndex(int index);

  public TreePath getPathForRow(int index);

  public void setHeaderVisible(boolean isHeaderVisible);
  
  public void setColumnOrder(int[] order);
  
  public int[] getColumnOrder();
  
  public Rectangle getImageBounds(int row, int column);

}
