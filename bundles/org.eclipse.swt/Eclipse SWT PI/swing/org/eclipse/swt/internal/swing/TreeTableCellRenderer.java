/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Component;

public interface TreeTableCellRenderer {

  public Component getTreeTableCellRendererComponent(JTreeTable treeTable, Object value, boolean selected, boolean expanded, boolean leaf, int row, int column, boolean hasFocus);

}
