/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import javax.swing.tree.DefaultMutableTreeNode;

public class DefaultMutableTreeTableNode extends DefaultMutableTreeNode implements TreeTableNode {

  protected Object[] userObjects;

  public DefaultMutableTreeTableNode() {
    this(null);
  }

  public DefaultMutableTreeTableNode(Object[] userObjects) {
    this(userObjects, true);
  }

  public DefaultMutableTreeTableNode(Object[] userObjects, boolean allowsChildren) {
    super(null, allowsChildren);
    setUserObjects(userObjects);
  }


  public void setUserObjects(Object[] objects) {
    if(objects == null || objects.length == 0) {
      setUserObject(null);
      userObjects = null;
      return;
    }
    setUserObject(objects[0]);
    if(objects.length == 1) {
      userObjects = null;
    } else {
      userObjects = new Object[objects.length - 1];
      System.arraycopy(objects, 1, userObjects, 0, userObjects.length);
    }
  }

  public Object[] getUserObjects() {
    if(userObjects == null) {
      return new Object[] {getUserObject()};
    }
    Object[] objects = new Object[userObjects.length + 1];
    System.arraycopy(userObjects, 0, objects, 1, userObjects.length);
    objects[0] = getUserObject();
    return objects;
  }

  public Object getUserObject(int index) {
    if(index == 0) {
      return getUserObject();
    }
    index--;
    if(userObjects == null || userObjects.length <= index) {
      return null;
    }
    return userObjects[index];
  }

  public void setUserObject(int index, Object object) {
    if(index == 0) {
      setUserObject(object);
    } else {
      index--;
      if(userObjects == null) {
        userObjects = new Object[index + 1];
        userObjects[index] = object;
      } else {
        if(userObjects.length <= index) {
          Object[] newUserObjects = new Object[index + 1];
          System.arraycopy(userObjects, 0, newUserObjects, 0, userObjects.length);
          userObjects = newUserObjects;
        }
        userObjects[index] = object;
      }
    }
  }

}
