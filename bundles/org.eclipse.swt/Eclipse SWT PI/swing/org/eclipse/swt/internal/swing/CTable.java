/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
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
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.swing.CTableItem.TableItemObject;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;

class CTableImplementation extends JScrollPane implements CTable {

  protected Table handle;
  protected JTable table;
  protected boolean isCheckType;

  public Container getSwingComponent() {
    return table;
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

  public class CTableModel extends AbstractTableModel {
    protected Table table;
    protected CTableModel(Table table) {
      this.table = table;
    }
    public int getRowCount() {
//      if(table.isDisposed()) return 0;
//      return table.getItemCount();
      return rowCount;
    }
    public int getColumnCount() {
      return table.getColumnCount();
    }
    public Object getValueAt(int rowIndex, int columnIndex) {
      return table.getItem(rowIndex).handle.getTableItemObject(columnIndex);
    }
  }

  protected UserAttributeHandler userAttributeHandler;
  
  public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }
  
  public CTableImplementation(Table table, int style) {
    handle = table;
    JViewport viewport = new JViewport() {
      public boolean isOpaque() {
        return backgroundImageIcon == null && super.isOpaque();
      }
      protected void paintComponent(Graphics g) {
        Utils.paintTiledImage(this, g, backgroundImageIcon);
        super.paintComponent(g);
      }
      public Color getBackground() {
        return CTableImplementation.this != null && userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
      }
    };
    setViewport(viewport);
    this.table = new JTable(new CTableModel(table)) {
      {
        CTableImplementation.this.setBackground(super.getBackground());
      }
      public boolean isCellSelected(int row, int column) {
        return super.isCellSelected(row, column) || isRowSelected(row);
      }
      public boolean getScrollableTracksViewportWidth() {
        if(handle.isDisposed()) {
          return false;
        }
        return handle.getColumnCount() == 0 && getPreferredSize().width < getParent().getWidth();
      }
      public boolean getScrollableTracksViewportHeight() {
        if(handle.isDisposed()) {
          return false;
        }
        return getPreferredSize().height < getParent().getHeight();
      }
      public Dimension getPreferredScrollableViewportSize() {
        if(handle.getColumnCount() != 0) {
          return getPreferredSize();
        }
        // TODO: use some caching mecanism?
        int columnCount = getColumnCount();
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
      final JTable table = this;
      protected TableCellRenderer renderer = new DefaultTableCellRenderer() {
        protected boolean isInitialized;
        protected boolean isDefaultOpaque;
        protected boolean isSelectionOpaque;
        protected Color defaultForeground;
        protected Color defaultBackground;
        protected Font defaultFont;
        protected Color selectionForeground;
        protected Color selectionBackground;
        protected Font selectionFont;
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
          if(value instanceof CTableItem.TableItemObject) {
            tableItemObject = (CTableItem.TableItemObject)value;
            CellPaintEvent event = new CellPaintEvent(table, CellPaintEvent.ERASE_TYPE);
            event.row = row;
            event.column = column;
            event.tableItem = tableItemObject.getTableItem();
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
            tableItemObject = null;
          }
          if(!isInitialized) {
            Component c = super.getTableCellRendererComponent(CTableImplementation.this.table, "", true, false, 0, 0);
            if(c instanceof JComponent) {
              isSelectionOpaque = ((JComponent)c).isOpaque();
            }
            selectionForeground = c.isForegroundSet()? c.getForeground(): null;
            selectionBackground = c.isBackgroundSet()? c.getBackground(): null;
            selectionFont = c.getFont();
            c.setForeground(null);
            c.setBackground(null);
          }
          if(!isInitialized) {
            Component c = super.getTableCellRendererComponent(CTableImplementation.this.table, "", false, false, 0, 0);
            if(c instanceof JComponent) {
              isDefaultOpaque = ((JComponent)c).isOpaque();
            }
            defaultForeground = c.isForegroundSet()? c.getForeground(): null;
            defaultBackground = c.isBackgroundSet()? c.getBackground(): null;
            defaultFont = c.getFont();
            isInitialized = true;
          }
          Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//          if(!isInitialized) {
//            defaultForeground = c.isForegroundSet()? c.getForeground(): null;
//            defaultBackground = c.isBackgroundSet()? c.getBackground(): null;
//            defaultFont = c.getFont();
//            isInitialized = true;
//          }
          if(value != null) {
            CTableItem.TableItemObject tableItemObject = (CTableItem.TableItemObject)value;
            Color userForeground = userAttributeHandler.foreground;
            c.setForeground(isSelected? selectionForeground: userForeground != null? userForeground: defaultForeground);
            Color userBackground = userAttributeHandler.background;
            c.setBackground(isSelected? selectionBackground: userBackground != null? userBackground: defaultBackground);
            Font userFont = userAttributeHandler.font;
            c.setFont(isSelected? selectionFont: userFont != null? userFont: defaultFont);
            if(c instanceof JComponent) {
              ((JComponent)c).setOpaque(isSelected? isSelectionOpaque: isDefaultOpaque && table.isOpaque());
            }
            if(tableItemObject != null) {
              if(c instanceof JLabel) {
                TableColumn tableColumn = table.getColumnModel().getColumn(table.convertColumnIndexToView(column));
                JLabel label = (JLabel)c;
                if(tableColumn instanceof CTableColumn) {
                  CTableColumn cTableColumn = (CTableColumn)tableColumn;
                  label.setHorizontalAlignment(cTableColumn.getAlignment());
                }
                label.setIcon(tableItemObject.getIcon());
              }
              CTableItem cTableItem = tableItemObject.getTableItem();
              Color foreground = tableItemObject.getForeground();
              if(foreground != null) {
                c.setForeground(foreground);
              } else {
                foreground = cTableItem.getForeground();
                if(foreground != null) {
                  c.setForeground(foreground);
                }
              }
              if(!isSelected) {
                Color background = tableItemObject.getBackground();
                if(background != null) {
                  if(c instanceof JComponent) {
                    ((JComponent)c).setOpaque(true);
                  }
                  c.setBackground(background);
                } else {
                  background = cTableItem.getBackground();
                  if(background != null) {
                    if(c instanceof JComponent) {
                      ((JComponent)c).setOpaque(true);
                    }
                    c.setBackground(background);
                  }
                }
              }
              Font font = tableItemObject.getFont();
              if(font != null) {
                c.setFont(font);
              } else {
                font = cTableItem.getFont();
                if(font != null) {
                  c.setFont(font);
                }
              }
            }
          }
          if(!isCheckType || column != table.convertColumnIndexToView(0)) {
            return c;
          }
          CheckBoxCellRenderer checkBoxCellRenderer = new CheckBoxCellRenderer(c);
          if(tableItemObject != null) {
            checkBoxCellRenderer.getStateCheckBox().setSelected(tableItemObject.isChecked());
          }
          return checkBoxCellRenderer;
        }
        protected CTableItem.TableItemObject tableItemObject;
        protected int row;
        protected int column;
        protected boolean ignoreDrawForeground;
        protected boolean ignoreDrawBackground;
        protected boolean ignoreDrawSelection;
        protected boolean ignoreDrawFocused;
        protected void paintComponent (Graphics g) {
          if(ignoreDrawForeground) {
            setText(null);
          }
          if(ignoreDrawBackground) {
            setOpaque(false);
          }
//          graphics = g;
          super.paintComponent(g);
          if(tableItemObject != null) {
            CellPaintEvent event = new CellPaintEvent(table, CellPaintEvent.PAINT_TYPE);
            event.row = row;
            event.column = column;
            event.tableItem = tableItemObject.getTableItem();
            event.ignoreDrawForeground = this.ignoreDrawForeground;
            event.ignoreDrawBackground = this.ignoreDrawBackground;
            event.ignoreDrawSelection = this.ignoreDrawSelection;
            event.ignoreDrawFocused = this.ignoreDrawFocused;
            handle.processEvent(event);
          }
//          graphics = null;
        }
      };
      public TableCellRenderer getCellRenderer(int row, int column) {
        return renderer;
      }
      protected JTableHeader createDefaultTableHeader() {
        return new JTableHeader(columnModel) {
          public String getToolTipText(MouseEvent e) {
            int index = columnModel.getColumnIndexAtX(e.getX());
            if(index < 0) {
              return null;
            }
            return ((CTableColumn)columnModel.getColumn(index)).getToolTipText();
          }
          public void paint(Graphics g) {
            super.paint(g);
            if(handle.getSortDirection() != SWT.NONE) {
              org.eclipse.swt.widgets.TableColumn sortColumn = handle.getSortColumn();
              if(sortColumn != null) {
                Rectangle bounds = getCellRect(-1, CTableImplementation.this.table.convertColumnIndexToView(handle.indexOf(sortColumn)), false);
                TableColumn draggedColumn = getDraggedColumn();
                Shape clip = g.getClip();
                if(draggedColumn != null) {
                  int draggedDistance = getDraggedDistance();
                  if(((CTableColumn)draggedColumn).getTableColumn() == sortColumn) {
                    bounds.x += draggedDistance;
                  } else {
                    Rectangle dragBounds = getCellRect(-1, CTableImplementation.this.table.convertColumnIndexToView(handle.indexOf(((CTableColumn)draggedColumn).getTableColumn())), true);
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
      public Color getBackground() {
        return CTableImplementation.this != null && userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
      }
      public Color getForeground() {
        return CTableImplementation.this != null && userAttributeHandler != null && userAttributeHandler.foreground != null? userAttributeHandler.foreground: super.getForeground();
      }
      public Font getFont() {
        return CTableImplementation.this != null && userAttributeHandler != null && userAttributeHandler.font != null? userAttributeHandler.font: super.getFont();
      }
      public Cursor getCursor() {
        if(Utils.globalCursor != null) {
          return Utils.globalCursor;
        }
        return CTableImplementation.this != null && userAttributeHandler != null && userAttributeHandler.cursor != null? userAttributeHandler.cursor: super.getCursor();
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
      protected void processEvent(AWTEvent e) {
        if(Utils.redispatchEvent(getSWTHandle(), e)) {
          return;
        }
        if(e instanceof MouseEvent) {
          MouseEvent me = (MouseEvent)e;
          if(isCheckType) {
            Point location = me.getPoint();
            int column = table.columnAtPoint(location);
            int row = table.rowAtPoint(location);
            if(row != -1 && column != -1) {
              Component tableCellRendererComponent = table.getCellRenderer(row, column).getTableCellRendererComponent(this, table.getValueAt(row, column), table.isCellSelected(row, column), false, row, column);
              if(tableCellRendererComponent instanceof CheckBoxCellRenderer) {
                CheckBoxCellRenderer checkBoxCellRenderer = (CheckBoxCellRenderer)tableCellRendererComponent;
                Rectangle cellBounds = table.getCellRect(row, column, false);
                checkBoxCellRenderer.setSize(cellBounds.width, cellBounds.height);
                checkBoxCellRenderer.doLayout();
                Component component = checkBoxCellRenderer.getComponentAt(location.x - cellBounds.x, location.y - cellBounds.y);
                JStateCheckBox stateCheckBox = checkBoxCellRenderer.getStateCheckBox();
                // TODO: find a way to rely on the Look&Feel for mouse events.
                if(component == stateCheckBox) {
                  switch(me.getID()) {
                  case MouseEvent.MOUSE_PRESSED:
                    CTableItem cTableItem = handle.getItem(row).handle;
                    TableItemObject tableItemObject = cTableItem.getTableItemObject(0);
                    boolean ischecked = !tableItemObject.isChecked();
                    tableItemObject.getTableItem().setChecked(ischecked);
                    table.repaint();
                    handle.processEvent(new ItemEvent(stateCheckBox, ItemEvent.ITEM_STATE_CHANGED, cTableItem, ischecked? ItemEvent.SELECTED: ItemEvent.DESELECTED));
                    return;
                  case MouseEvent.MOUSE_DRAGGED:
                  case MouseEvent.MOUSE_RELEASED:
                  case MouseEvent.MOUSE_CLICKED:
                    return;
                  }
                }
              }
            }
          }
          switch(me.getID()) {
          case MouseEvent.MOUSE_PRESSED:
            switch(me.getButton()) {
            case MouseEvent.BUTTON3: {
              // We have to assume that popup menus are triggered with the mouse button 3.
              int row = rowAtPoint(me.getPoint());
              if(row != -1) {
                ListSelectionModel selectionModel = getSelectionModel();
                if(!selectionModel.isSelectedIndex(row)) {
                  selectionModel.setSelectionInterval(row, row);
                }
              }
              break;
            }
            }
            break;
          }
        }
        super.processEvent(e);
      }
//      protected Set validItemSet = new HashSet();
//      public int getRowHeight(int row) {
//        int rowHeight = super.getRowHeight(row);
//        int columnCount = getColumnCount();
//        if(columnCount == 0 || handle.isDisposed()) {
//          return rowHeight;
//        }
//        if(adjustItemHeight) {
//          adjustItemHeight = false;
//          validItemSet.clear();
//        }
//        if(validItemSet.contains(new Integer(row))) {
//          return rowHeight;
//        }
//        validItemSet.add(new Integer(row));
//        int maxHeight = 0;
//        for(int column=0; column<columnCount; column++) {
//          Object value = handle.getItem(row).handle.getTableItemObject(column);
//          if(value instanceof CTableItem.TableItemObject) {
//            CellPaintEvent event = new CellPaintEvent(this, CellPaintEvent.MEASURE_TYPE);
//            event.row = row;
//            event.column = column;
//            event.tableItem = ((CTableItem.TableItemObject)value).getTableItem();
//            event.rowHeight = rowHeight;
//            handle.processEvent(event);
//            maxHeight = Math.max(event.rowHeight, maxHeight);
//          }
//        }
//        setRowHeight(row, maxHeight);
//        return maxHeight;
//      }
    };
    userAttributeHandler = new UserAttributeHandler(this.table);
    this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    final JTableHeader tableHeader = this.table.getTableHeader();
    class HeaderMouseListener extends MouseAdapter implements MouseMotionListener {
      public void mouseClicked(MouseEvent e) {
        TableColumnModel columnModel = tableHeader.getColumnModel();
        int columnIndex = columnModel.getColumnIndexAtX(e.getX());
        if(columnIndex != -1) {
          CTableColumn column = (CTableColumn)columnModel.getColumn(columnIndex);
          column.getTableColumn().processEvent(e);
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
          TableColumn column = columnModel.getColumn(columnIndex);
          if(column instanceof CTableColumn) {
            tableHeader.setReorderingAllowed(((CTableColumn)column).getTableColumn().getMoveable());
          }
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
          CTableColumn cTableColumn = (CTableColumn)columnModel.getColumn(toIndex);
          cTableColumn.getTableColumn().processEvent(e);
          cTableColumn = (CTableColumn)columnModel.getColumn(fromIndex);
          cTableColumn.getTableColumn().processEvent(e);
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
        if(value == null || "".equals(value)) {
          value = " ";
        }
        Component c = headerRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(c instanceof JLabel) {
          TableColumn tableColumn = CTableImplementation.this.table.getColumnModel().getColumn(column);
          if(tableColumn instanceof CTableColumn) {
            JLabel label = (JLabel)c;
            CTableColumn cTableColumn = (CTableColumn)tableColumn;
            label.setHorizontalAlignment(cTableColumn.getAlignment());
            label.setIcon(cTableColumn.getIcon());
          }
        }
        return c;
      }
    });
    // TODO: add a first bogus column? (1)
    javax.swing.table.TableColumnModel columnModel = this.table.getColumnModel();
    javax.swing.table.TableColumn tableColumn = new javax.swing.table.TableColumn(0);
    columnModel.addColumn(tableColumn);
    setFocusable(false);
    viewport.setView(this.table);
    viewport.setBackground(this.table.getBackground());
    setColumnHeader(createViewport());
    setHeaderVisible(false);
    init(style);
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
//    table.setAutoCreateRowSorter(true);
    if((style & SWT.FULL_SELECTION) == 0) {
      // Not perfect because it does not prevent the selection of the first cell by clicking anywhere on the row.
      table.setCellSelectionEnabled(true);
      table.getColumnModel().setSelectionModel(new DefaultListSelectionModel() {
        public void addSelectionInterval(int index0, int index1) {
          index0 = table.convertColumnIndexToView(0);
          index1 = table.convertColumnIndexToView(0);
          super.addSelectionInterval(index0, index1);
        }
        public void setAnchorSelectionIndex(int anchorIndex) {
          anchorIndex = table.convertColumnIndexToView(0);
          super.setAnchorSelectionIndex(anchorIndex);
        }
        public void setLeadSelectionIndex(int leadIndex) {
          leadIndex = table.convertColumnIndexToView(0);
          super.setLeadSelectionIndex(leadIndex);
        }
        public void moveLeadSelectionIndex(int leadIndex) {
          leadIndex = table.convertColumnIndexToView(0);
          super.moveLeadSelectionIndex(leadIndex);
        }
        public void setSelectionInterval(int index0, int index1) {
          index0 = table.convertColumnIndexToView(0);
          index1 = table.convertColumnIndexToView(0);
          super.setSelectionInterval(index0, index1);
        }
      });
    }
    setGridVisible(false);
    if((style & SWT.MULTI) == 0) {
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    // TODO: Map other events for table header click etc.
    Utils.installMouseListener(table, handle);
    Utils.installKeyListener(table, handle);
    Utils.installFocusListener(table, handle);
    Utils.installComponentListener(this, handle);
    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public Container getClientArea() {
    return table;
  }

  public JTableHeader getTableHeader() {
    return table.getTableHeader();
  }

  protected boolean isGridVisible;

  public void setGridVisible(boolean isGridVisible) {
    this.isGridVisible = isGridVisible;
    table.setIntercellSpacing(isGridVisible? new Dimension(1, 1): new Dimension(0, 0));
    table.setShowHorizontalLines(isGridVisible);
    table.setShowVerticalLines(isGridVisible);
    repaint();
  }

  public boolean isGridVisible() {
    return isGridVisible;
  }

  public DefaultListSelectionModel getSelectionModel() {
    return (DefaultListSelectionModel)table.getSelectionModel();
  }

  public AbstractTableModel getModel() {
    return (AbstractTableModel)table.getModel();
  }

  public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
    Rectangle cellRect = table.getCellRect(row, column, includeSpacing);
    if(isCheckType && column == 0) {
      Component c = getCellRenderer(row, column).getTableCellRendererComponent(table, getModel().getValueAt(row, column), false, false, row, column);
      c.setBounds(cellRect);
      c.validate();
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

  public TableColumnModel getColumnModel() {
    return table.getColumnModel();
  }

//  protected boolean adjustItemHeight;
  protected int rowCount;

  public void addItem(int index) {
    rowCount++;
//    adjustItemHeight = true;
    getModel().fireTableRowsInserted(index, index);
  }

  public void removeItem(int index) {
//    adjustItemHeight = true;
    rowCount--;
    getModel().fireTableRowsDeleted(index, index);
  }
  
  public TableCellRenderer getCellRenderer(int row, int column) {
    return table.getCellRenderer(row, column);
  }

  public int getPreferredColumnWidth(int columnIndex) {
    int count = handle.getItemCount();
    int newWidth = Math.max(table.getColumnModel().getColumn(columnIndex).getMinWidth(), 10);
    if((handle.getStyle() & SWT.VIRTUAL) != 0) {
      // TODO: is there a way to know the preferred size? The method below generates an exception (cf Snippet144)
      return newWidth;
    }
    // TODO: is there a better way than this hack?
    TableModel model = table.getModel();
    for(int i=0; i<count; i++) {
      javax.swing.table.TableCellRenderer renderer = table.getCellRenderer(i, columnIndex);
      java.awt.Component component = renderer.getTableCellRendererComponent(table, model.getValueAt(i, columnIndex), false, false, i, columnIndex);
      newWidth = Math.max(newWidth, (int)component.getPreferredSize().getWidth());
    }
    JTableHeader tableHeader = getTableHeader();
    TableColumn column = tableHeader.getColumnModel().getColumn(columnIndex);
    java.awt.Component component = tableHeader.getDefaultRenderer().getTableCellRendererComponent(table, column.getHeaderValue(), false, false, -1, columnIndex);
    newWidth = Math.max(newWidth, (int)component.getPreferredSize().getWidth());
    return newWidth;
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
      table.setOpaque(true);
      break;
    case BACKGROUND_INHERITANCE:
      setOpaque(false);
      getViewport().setOpaque(false);
      table.setOpaque(false);
      break;
    }
  }

  public void ensureRowVisible(int index) {
    if(index < 0 || index >= table.getRowCount()) {
      return;
    }
    Rectangle bounds = getCellRect(index, 0, true);
    bounds.width = table.getWidth();
    bounds.height = table.getRowHeight(index);
    table.scrollRectToVisible(bounds);
  }

  public void ensureColumnVisible(int index) {
    if(index < 0 || index >= table.getColumnCount()) {
      return;
    }
    Rectangle bounds = new Rectangle();
    TableColumnModel columnModel = getColumnModel();
    for(int i=0; i<index; i++) {
      bounds.x += columnModel.getColumn(i).getPreferredWidth();
    }
    bounds.width = columnModel.getColumn(index).getPreferredWidth();
    bounds.height = table.getHeight();
    table.scrollRectToVisible(bounds);
  }
  
  public void setHeaderVisible(boolean isHeaderVisible) {
    getColumnHeader().setVisible(isHeaderVisible);
    table.getTableHeader().setVisible(isHeaderVisible);
  }

  public int getRowHeight() {
    return table.getRowHeight();
  }

  public void setRowHeight(int rowHeight) {
    table.setRowHeight(rowHeight);
  }

  public int rowAtPoint(Point point) {
    point = SwingUtilities.convertPoint(this, point.x, point.y, table);
    return table.rowAtPoint(point);
  }

  public void setTopIndex(int index) {
    int rowCount = table.getRowCount();
    if(rowCount == 0) {
      return;
    }
    ensureRowVisible(rowCount - 1);
    if(index != 0) {
      ensureRowVisible(index);
    }
  }

  public int getTopIndex() {
    return rowAtPoint(new Point(0, 0));
  }
  
  public void moveColumn(int column, int targetColumn) {
    table.moveColumn(column, targetColumn);
  }

  public void setFont(Font font) {
    super.setFont(font);
    if(table != null) {
      table.setFont(font);
    }
  }

  public void setForeground(Color foreground) {
    super.setForeground(foreground);
    if(table != null) {
      table.setForeground(foreground);
    }
  }

  public boolean isFocusable() {
    return table.isFocusable();
  }

  public void requestFocus() {
    table.requestFocus();
  }

  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    table.setEnabled(enabled);
  }
  
  protected boolean isAdjustingColumnOrder;
  
  public void setColumnOrder(int[] order) {
    isAdjustingColumnOrder = true;
    for(int i=0; i<order.length; i++) {
      table.moveColumn(table.convertColumnIndexToView(order[i]), i);
    }
    isAdjustingColumnOrder = false;
    for(int i=0; i<order.length; i++) {
      CTableColumn cTableColumn = (CTableColumn)getColumnModel().getColumn(i);
      cTableColumn.getTableColumn().processEvent(new TableColumnModelEvent(table.getColumnModel(), i, order[i]));
    }
  }
  
  public int[] getColumnOrder() {
    int[] order = new int[table.getColumnCount()];
    for(int i=0; i<order.length; i++) {
      order[i] = table.convertColumnIndexToModel(i);
    }
    return order;
  }
  
  public Rectangle getImageBounds(int row, int column) {
    Component c = getCellRenderer(row, column).getTableCellRendererComponent(table, getModel().getValueAt(row, column), false, false, row, column);
    Rectangle cellRect = table.getCellRect(row, column, false);
    c.setBounds(cellRect);
    c.validate();
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
    bounds.x += cellRect.x;
    bounds.y += cellRect.y;
    return bounds;
  }

}

public interface CTable extends CComposite {

  public static class CellPaintEvent extends EventObject {
  
    public static final int ERASE_TYPE = 1;
    public static final int PAINT_TYPE = 2;
    public static final int MEASURE_TYPE = 3;
    protected int type;
    public int row;
    public int column;
    public CTableItem tableItem;
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

    public static CTable newInstance(Table table, int style) {
      return new CTableImplementation(table, style);
    }

  }

  public JTableHeader getTableHeader();

  public void setGridVisible(boolean isGridVisible);

  public boolean isGridVisible();

  public DefaultListSelectionModel getSelectionModel();

  public AbstractTableModel getModel();

  public Rectangle getCellRect(int row, int column, boolean includeSpacing);

  public TableColumnModel getColumnModel();

  public void addItem(int index);

  public void removeItem(int index);

  public TableCellRenderer getCellRenderer(int row, int column);

  public int getPreferredColumnWidth(int columnIndex);

  public void ensureRowVisible(int index);

  public void ensureColumnVisible(int index);

  public void setHeaderVisible(boolean isColumnHeaderVisible);

  public int getRowHeight();

  public void setRowHeight(int rowHeight);

  public int rowAtPoint(Point point);

  public void setTopIndex(int index);

  public int getTopIndex();
  
  public void moveColumn(int column, int targetColumn);

  public void setColumnOrder(int[] order);
  
  public int[] getColumnOrder();
  
  public Rectangle getImageBounds(int row, int column);
  
}
