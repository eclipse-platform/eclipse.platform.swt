/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import javax.swing.Icon;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeColumn;

class CTreeColumnImplementation extends TableColumn implements CTreeColumn {

  protected DefaultMutableTreeTableNode mutableTreeTableNode;

  protected TreeColumn handle;

  public CTreeColumnImplementation(TreeColumn treeColumn, int style) {
    handle = treeColumn;
    setPreferredWidth(0);
    setMinWidth(0);
    init(style);
  }

  protected void init(int style) {
    if ((style & SWT.LEFT) == SWT.LEFT) setAlignment(SwingConstants.LEFT);
    if ((style & SWT.CENTER) == SWT.CENTER) setAlignment(SwingConstants.CENTER);
    if ((style & SWT.RIGHT) == SWT.RIGHT) setAlignment(SwingConstants.RIGHT);
  }

  protected Icon icon;

  public void setIcon(Icon icon) {
    this.icon = icon;
  }

  public Icon getIcon() {
    return icon;
  }

  protected int alignment;

  public void setAlignment(int alignment) {
    this.alignment = alignment;
  }

  public int getAlignment() {
    return alignment;
  }
  
  protected String toolTipText;

  public void setToolTipText(String toolTipText) {
    this.toolTipText = toolTipText;
  }
  
  public String getToolTipText() {
    return toolTipText;
  }
  
  public TreeColumn getTreeColumn() {
    return handle;
  }

}

public interface CTreeColumn {

  public static class Factory {
    private Factory() {}

    public static CTreeColumn newInstance(TreeColumn treeColumn, int style) {
      return new CTreeColumnImplementation(treeColumn, style);
    }

  }

  public void setIcon(Icon icon);

  public Icon getIcon();

  public void setAlignment(int alignment);

  public int getAlignment();
  
  public void setToolTipText(String toolTipText);
  
  public String getToolTipText();
  
  public TreeColumn getTreeColumn();

}
