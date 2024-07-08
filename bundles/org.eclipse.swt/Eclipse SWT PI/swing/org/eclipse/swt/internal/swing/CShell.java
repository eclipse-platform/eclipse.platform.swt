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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.PaintEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

class CShellFrame extends JFrame implements CShell {

  protected Container contentPane;
  protected JScrollPane scrollPane;

  protected Shell handle;

  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

  protected UserAttributeHandler userAttributeHandler;
  
  public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }
  
  protected boolean areActivationEventsBlocked;
  
  public void setActivationEventsBlocked(boolean areActivationEventsBlocked) {
    this.areActivationEventsBlocked = areActivationEventsBlocked;
  }
  
  protected void processEvent(AWTEvent e) {
    if(areActivationEventsBlocked) {
      switch(e.getID()) {
        case WindowEvent.WINDOW_ACTIVATED:
        case WindowEvent.WINDOW_DEACTIVATED:
          return;
      }
    }
    super.processEvent(e);
  }
  
  public CShellFrame(Shell shell, int style) {
    this.handle = shell;
    if(Compatibility.IS_JAVA_5_OR_GREATER) {
      setLocationByPlatform(true);
    }
    init(style);
  }
  
  protected void init(int style) {
    resetFocusableState();
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    java.awt.Rectangle bounds = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    setSize(bounds.width * 3 / 4, bounds.height * 3 / 4);
    boolean isResizable = (style & SWT.RESIZE) != 0;
    if(!isResizable) {
      setResizable(false);
    }
    if(Compatibility.IS_JAVA_5_OR_GREATER) {
      setAlwaysOnTop((style & SWT.ON_TOP) != 0);
    }
    boolean isDecorated = (style & SWT.TITLE) != 0;
    if(isDecorated) {
      if(JFrame.isDefaultLookAndFeelDecorated()) {
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
      } else {
        setUndecorated(!isDecorated);
      }
    } else {
      JRootPane rootPane = getRootPane();
      if(JFrame.isDefaultLookAndFeelDecorated()) {
        rootPane.setWindowDecorationStyle(JRootPane.NONE);
      }
      setUndecorated(!isDecorated);
      if(isResizable || (style & SWT.BORDER) != 0) {
        rootPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY), BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.LIGHT_GRAY, Color.GRAY)));
        if(isResizable) {
          ComponentBorderResizer.handle(this, rootPane);
        }
      } else if(!isDecorated && (style & SWT.NO_TRIM) == 0) {
        rootPane.setBorder(LookAndFeelUtils.getDefaultWindowBorder());
      }
    }
    // BORDER, CLOSE, MIN, MAX, NO_TRIM, RESIZE, TITLE, APPLICATION_MODAL, MODELESS, PRIMARY_MODAL, SYSTEM_MODAL
    contentPane = getContentPane();
    JPanel panel = new JPanel(null) {
      public Color getBackground() {
        return CShellFrame.this != null && userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
      }
      public Color getForeground() {
        return CShellFrame.this != null && userAttributeHandler != null && userAttributeHandler.foreground != null? userAttributeHandler.foreground: super.getForeground();
      }
      public Font getFont() {
        return CShellFrame.this != null && userAttributeHandler != null && userAttributeHandler.font != null? userAttributeHandler.font: super.getFont();
      }
      public Cursor getCursor() {
        if(Utils.globalCursor != null) {
          return Utils.globalCursor;
        }
        return CShellFrame.this != null && userAttributeHandler != null && userAttributeHandler.cursor != null? userAttributeHandler.cursor: super.getCursor();
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
      public boolean isOptimizedDrawingEnabled() {
        return getComponentCount() < 2 || Utils.isFlatLayout(handle);
      }
      protected void paintComponent (Graphics g) {
        graphics = g;
        putClientProperty(Utils.SWTSwingGraphics2DClientProperty, g);
        super.paintComponent(g);
        Utils.paintTiledImage(this, g, backgroundImageIcon);
        handle.processEvent(new PaintEvent(this, PaintEvent.PAINT, null));
        putClientProperty(Utils.SWTSwingGraphics2DClientProperty, null);
        graphics = null;
      }
      protected void addImpl(Component comp, Object constraints, int index) {
        isPaintActive = true;
        super.addImpl(comp, constraints, index);
      }
    };
    userAttributeHandler = new UserAttributeHandler(panel);
    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
      JScrollPane scrollPane = new UnmanagedScrollPane((style & SWT.V_SCROLL) != 0? JScrollPane.VERTICAL_SCROLLBAR_ALWAYS: JScrollPane.VERTICAL_SCROLLBAR_NEVER, (style & SWT.H_SCROLL) != 0? JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      this.scrollPane = scrollPane;
      scrollPane.setBorder(null);
      contentPane.add(scrollPane, BorderLayout.CENTER);
      scrollPane.getViewport().setView(panel);
    } else {
      contentPane.add(panel, BorderLayout.CENTER);
    }
    contentPane = panel;
    contentPane.setFocusable(true);
//    setFocusable(false);
    Utils.installMouseListener(contentPane, handle);
    Utils.installKeyListener(contentPane, handle);
//    Utils.installFocusListener(contentPane, handle);
    addComponentListener(new ComponentAdapter() {
      public void componentHidden(ComponentEvent e) {
        handle.processEvent(e);
      }
      public void componentShown(ComponentEvent e) {
        handle.processEvent(e);
      }
      public void componentMoved(ComponentEvent e) {
        handle.processEvent(e);
      }
    });
    contentPane.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        handle.processEvent(e);
      }
    });
    addWindowListener(new WindowAdapter() {
      public void windowOpened(WindowEvent e) {
//        transferFocusDownCycle();
      }
      public void windowClosing(WindowEvent e) {
        if(!isModallyBlocked()) {
          handle.processEvent(e);
        }
      };
      public void windowActivated(WindowEvent e) {
        handle.processEvent(e);
      }
      public void windowDeactivated(WindowEvent e) {
        handle.processEvent(e);
      }
      public void windowClosed(WindowEvent e) {
        handle.processEvent(e);
      }
      public void windowIconified(WindowEvent e) {
        handle.processEvent(e);
      }
      public void windowDeiconified(WindowEvent e) {
        handle.processEvent(e);
      }
    });
  }

  protected boolean isPaintActive = true;
  
  public void paint(Graphics g) {
    if(isPaintActive) {
      super.paint(g);
    }
  }
  
  protected void resetFocusableState() {
    setFocusableWindowState((handle.getStyle() & SWT.TITLE) != 0);
  }
  
  protected static void adjustLocation(Window window, Window parent) {
    if(parent == null) {
      return;
    }
    GraphicsConfiguration nGC = parent.getGraphicsConfiguration();
    if(nGC == null) {
      return;
    }
    Point p = window.getLocation();
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] gs = ge.getScreenDevices();
    for(int j=0; j<gs.length; j++) { 
      GraphicsDevice gd = gs[j];
      GraphicsConfiguration gc = gd.getDefaultConfiguration();
      Rectangle bounds = gc.getBounds();
      if(bounds.contains(p)) {
        Rectangle nBounds = nGC.getBounds();
        window.setLocation(nBounds.x + p.x - bounds.x, nBounds.y + p.y - bounds.y);
        break;
      }
    }
  }
  
  public void show() {
    if(!isVisible()) {
      isPaintActive = true;
      boolean isEventDispatchThread = SwingUtilities.isEventDispatchThread();
      if(isEventDispatchThread) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            isPaintActive = true;
          }
        });
      }
      final Window owner = getOwner();
      if((owner instanceof CShell && !getFocusableWindowState())) {
        setActivationEventsBlocked(true);
        ((CShell)owner).setActivationEventsBlocked(true);
      }
      super.show();
      if(Compatibility.IS_JAVA_5_OR_GREATER && isLocationByPlatform()) {
        adjustLocation(this, owner);
      }
      if(!getFocusableWindowState() && (handle.getStyle() & SWT.NO_FOCUS) == 0) {
        new Thread() {
          public void run() {
            try {
              sleep(500);
            } catch(Exception e) {}
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                if(!getFocusableWindowState()) {
                  setFocusableWindowState(true);
                }
              }
            });
          }
        }.start();
      }
      getModalityHandler().setEnabled(true);
      // The following is to block paint events that occur when a shell is opened to allow direct GC drawing. Check if it is dangerous to do that...
      if(isEventDispatchThread) {
        paint(getGraphics());
        Utils.paintComponentImmediately(this);
        isPaintActive = false;
      }
    }
  }

  public void hide() {
    if(isVisible()) {
      getModalityHandler().setEnabled(false);
      super.hide();
      resetFocusableState();
    }
  }
  
  public void forceActive() {
    toFront();
    requestFocus();
  }

  public Container getClientArea() {
    return contentPane;
  }

  public String getToolTipText() {
    return ((JComponent)contentPane).getToolTipText();
  }

  public void setToolTipText(String string) {
    ((JComponent)contentPane).setToolTipText(string);
  }

  public JScrollBar getVerticalScrollBar() {
    return scrollPane == null? null: scrollPane.getVerticalScrollBar();
  }

  public JScrollBar getHorizontalScrollBar() {
    return scrollPane == null? null: scrollPane.getHorizontalScrollBar();
  }

  public void setDefaultButton(CButton button) {
    if(button instanceof JButton) {
      getRootPane().setDefaultButton((JButton)button);
    }
  }

  protected ImageIcon backgroundImageIcon;

  public void setBackgroundImage(Image backgroundImage) {
    this.backgroundImageIcon = backgroundImage == null? null: new ImageIcon(backgroundImage);
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
  }

  protected ModalityHandler modalityHandler = new ModalityHandler(this);
  
  public ModalityHandler getModalityHandler() {
    return modalityHandler;
  }

  public boolean isFocusable() {
    return getFocusableWindowState() && super.isFocusable();
  }
  
  public boolean getFocusableWindowState() {
    return super.getFocusableWindowState() && !isModallyBlocked();
  }
  
  public void setFocusableWindowState(boolean focusableWindowState) {
    if(focusableWindowState) {
      Window owner = getOwner();
      if(owner instanceof CShell) {
        setActivationEventsBlocked(false);
        ((CShell)owner).setActivationEventsBlocked(false);
      }
    }
    super.setFocusableWindowState(focusableWindowState);
  }
  
  public void setIconImages(List imageList) {
    if(Compatibility.IS_JAVA_6_OR_GREATER) {
      super.setIconImages(imageList);
    } else if(!imageList.isEmpty()) {
      super.setIconImage((Image)imageList.get(0));
    }
  }

  protected boolean isModallyBlocked;

  public void setModallyBlocked(boolean isModallyBlocked) {
    this.isModallyBlocked = isModallyBlocked;
    super.setEnabled(!isModallyBlocked && isEnabled);
  }
  
  public boolean isModallyBlocked() {
    return isModallyBlocked;
  }
  
  protected boolean isEnabled = true;
  
  public void setEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
    super.setEnabled(isEnabled);
  }
  
  public boolean isEnabled() {
    return isEnabled;
  }
  
  public Cursor getCursor() {
    return Utils.globalCursor != null? Utils.globalCursor: super.getCursor();
  }

}

class CShellDialog extends JDialog implements CShell {

  protected Container contentPane;
  protected JScrollPane scrollPane;
  protected Shell handle;

  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

  protected UserAttributeHandler userAttributeHandler;
  
  public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }
  
  protected boolean areActivationEventsBlocked;
  
  public void setActivationEventsBlocked(boolean areActivationEventsBlocked) {
    this.areActivationEventsBlocked = areActivationEventsBlocked;
  }
  
  protected void processEvent(AWTEvent e) {
    if(areActivationEventsBlocked) {
      switch(e.getID()) {
        case WindowEvent.WINDOW_ACTIVATED:
        case WindowEvent.WINDOW_DEACTIVATED:
          return;
      }
    }
    super.processEvent(e);
  }
  
  public CShellDialog(Shell shell, CShellDialog parent, int style) {
    super(parent);
    if(Compatibility.IS_JAVA_5_OR_GREATER) {
      setLocationByPlatform(true);
    }
    this.handle = shell;
    init(style);
  }

  public CShellDialog(Shell shell, CShellFrame parent, int style) {
    super(parent);
    if(Compatibility.IS_JAVA_5_OR_GREATER) {
      setLocationByPlatform(true);
    }
    this.handle = shell;
    init(style);
  }

  protected void init(int style) {
    resetFocusableState();
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    java.awt.Rectangle bounds = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    setSize(bounds.width * 3 / 4, bounds.height * 3 / 4);
    boolean isResizable = (style & SWT.RESIZE) != 0;
    if(!isResizable) {
      setResizable(false);
    }
    if(Compatibility.IS_JAVA_5_OR_GREATER) {
      setAlwaysOnTop((style & SWT.ON_TOP) != 0);
    }
    boolean isDecorated = (style & SWT.TITLE) != 0;
    if(isDecorated) {
      if(JDialog.isDefaultLookAndFeelDecorated()) {
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
      } else {
        setUndecorated(!isDecorated);
      }
    } else {
      JRootPane rootPane = getRootPane();
      if(JDialog.isDefaultLookAndFeelDecorated()) {
        rootPane.setWindowDecorationStyle(JRootPane.NONE);
      }
      setUndecorated(!isDecorated);
      if(isResizable || (style & SWT.BORDER) != 0) {
        rootPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY), BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.LIGHT_GRAY, Color.GRAY)));
        if(isResizable) {
          ComponentBorderResizer.handle(this, rootPane);
        }
      } else if(!isDecorated && (style & SWT.NO_TRIM) == 0) {
        rootPane.setBorder(LookAndFeelUtils.getDefaultWindowBorder());
      }
    }
    contentPane = getContentPane();
    JPanel panel = new JPanel(null) {
      public Color getBackground() {
        return CShellDialog.this != null && userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
      }
      public Color getForeground() {
        return CShellDialog.this != null && userAttributeHandler != null && userAttributeHandler.foreground != null? userAttributeHandler.foreground: super.getForeground();
      }
      public Font getFont() {
        return CShellDialog.this != null && userAttributeHandler != null && userAttributeHandler.font != null? userAttributeHandler.font: super.getFont();
      }
      public Cursor getCursor() {
        if(Utils.globalCursor != null) {
          return Utils.globalCursor;
        }
        return CShellDialog.this != null && userAttributeHandler != null && userAttributeHandler.cursor != null? userAttributeHandler.cursor: super.getCursor();
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
      public boolean isOptimizedDrawingEnabled() {
        return getComponentCount() < 2 || Utils.isFlatLayout(handle);
      }
      protected void paintComponent (Graphics g) {
        graphics = g;
        putClientProperty(Utils.SWTSwingGraphics2DClientProperty, g);
        super.paintComponent(g);
        Utils.paintTiledImage(this, g, backgroundImageIcon);
        handle.processEvent(new PaintEvent(this, PaintEvent.PAINT, null));
        putClientProperty(Utils.SWTSwingGraphics2DClientProperty, null);
        graphics = null;
      }
      protected void addImpl(Component comp, Object constraints, int index) {
        isPaintActive = true;
        super.addImpl(comp, constraints, index);
      }
    };
    userAttributeHandler = new UserAttributeHandler(panel);
    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
      JScrollPane scrollPane = new UnmanagedScrollPane((style & SWT.V_SCROLL) != 0? JScrollPane.VERTICAL_SCROLLBAR_ALWAYS: JScrollPane.VERTICAL_SCROLLBAR_NEVER, (style & SWT.H_SCROLL) != 0? JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      this.scrollPane = scrollPane;
      scrollPane.setBorder(null);
      contentPane.add(scrollPane, BorderLayout.CENTER);
      scrollPane.getViewport().setView(panel);
    } else {
      contentPane.add(panel, BorderLayout.CENTER);
    }
    contentPane = panel;
    contentPane.setFocusable(true);
//    setFocusable(false);
    Utils.installMouseListener(contentPane, handle);
    Utils.installKeyListener(contentPane, handle);
    getRootPane().registerKeyboardAction(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handle.processEvent(e);
      }
    }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
//    Utils.installFocusListener(contentPane, handle);
    addComponentListener(new ComponentAdapter() {
      public void componentHidden(ComponentEvent e) {
        handle.processEvent(e);
      }
      public void componentShown(ComponentEvent e) {
        handle.processEvent(e);
      }
      public void componentMoved(ComponentEvent e) {
        handle.processEvent(e);
      }
    });
    contentPane.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        handle.processEvent(e);
      }
    });
    addWindowListener(new WindowAdapter() {
      public void windowOpened(WindowEvent e) {
//        transferFocusDownCycle();
      }
      public void windowClosing(WindowEvent e) {
        if(!isModallyBlocked()) {
          handle.processEvent(e);
        }
      };
      public void windowActivated(WindowEvent e) {
        handle.processEvent(e);
      }
      public void windowDeactivated(WindowEvent e) {
        handle.processEvent(e);
      }
      public void windowClosed(WindowEvent e) {
        handle.processEvent(e);
      }
      public void windowIconified(WindowEvent e) {
        handle.processEvent(e);
      }
      public void windowDeiconified(WindowEvent e) {
        handle.processEvent(e);
      }
    });
  }

  protected boolean isPaintActive = true;
  
  public void paint(Graphics g) {
    if(isPaintActive) {
      super.paint(g);
    }
  }
  
  protected void resetFocusableState() {
    setFocusableWindowState((handle.getStyle() & SWT.TITLE) != 0);
  }
  
  protected static void adjustLocation(Window window, Window parent) {
    if(parent == null) {
      return;
    }
    GraphicsConfiguration nGC = parent.getGraphicsConfiguration();
    if(nGC == null) {
      return;
    }
    Point p = window.getLocation();
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] gs = ge.getScreenDevices();
    for(int j=0; j<gs.length; j++) { 
      GraphicsDevice gd = gs[j];
      GraphicsConfiguration gc = gd.getDefaultConfiguration();
      Rectangle bounds = gc.getBounds();
      if(bounds.contains(p)) {
        Rectangle nBounds = nGC.getBounds();
        window.setLocation(nBounds.x + p.x - bounds.x, nBounds.y + p.y - bounds.y);
        break;
      }
    }
  }
  
  public void show() {
    if(!isVisible()) {
      isPaintActive = true;
      boolean isEventDispatchThread = SwingUtilities.isEventDispatchThread();
      if(isEventDispatchThread) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            isPaintActive = true;
          }
        });
      }
      final Window owner = getOwner();
      if((owner instanceof CShell && !getFocusableWindowState())) {
        setActivationEventsBlocked(true);
        ((CShell)owner).setActivationEventsBlocked(true);
      }
      super.show();
      if(Compatibility.IS_JAVA_5_OR_GREATER && isLocationByPlatform()) {
        adjustLocation(this, owner);
      }
      if(!getFocusableWindowState() && (handle.getStyle() & SWT.NO_FOCUS) == 0) {
        new Thread() {
          public void run() {
            try {
              sleep(500);
            } catch(Exception e) {}
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                if(!getFocusableWindowState()) {
                  setFocusableWindowState(true);
                }
              }
            });
          }
        }.start();
      }
      getModalityHandler().setEnabled(true);
      // The following is to block paint events that occur when a shell is opened to allow direct GC drawing. Check if it is dangerous to do that...
      if(isEventDispatchThread) {
        paint(getGraphics());
        Utils.paintComponentImmediately(this);
        isPaintActive = false;
      }
    }
  }
  
  public void hide() {
    if(isVisible()) {
      getModalityHandler().setEnabled(false);
      super.hide();
      resetFocusableState();
    }
  }
  
  public void forceActive() {
    toFront();
    requestFocus();
  }

  public Container getClientArea() {
    return contentPane;
  }

  public String getToolTipText() {
    return ((JComponent)contentPane).getToolTipText();
  }

  public void setToolTipText(String string) {
    ((JComponent)contentPane).setToolTipText(string);
  }

  public JScrollBar getVerticalScrollBar() {
    return scrollPane == null? null: scrollPane.getVerticalScrollBar();
  }

  public JScrollBar getHorizontalScrollBar() {
    return scrollPane == null? null: scrollPane.getHorizontalScrollBar();
  }

  public int getExtendedState() {
    return 0;
  }

  public void setExtendedState(int state) {
    // TODO: implement fake maximize/minimize?
  }

  public void setDefaultButton(CButton button) {
    if(button instanceof JButton) {
      getRootPane().setDefaultButton((JButton)button);
    }
  }

  protected ImageIcon backgroundImageIcon;

  public void setBackgroundImage(Image backgroundImage) {
    this.backgroundImageIcon = backgroundImage == null? null: new ImageIcon(backgroundImage);
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
  }

  protected ModalityHandler modalityHandler = new ModalityHandler(this);
  
  public ModalityHandler getModalityHandler() {
    return modalityHandler;
  }
  
  public boolean isFocusable() {
    return getFocusableWindowState() && super.isFocusable();
  }
  
  public boolean getFocusableWindowState() {
    return super.getFocusableWindowState() && !isModallyBlocked();
  }
  
  public void setFocusableWindowState(boolean focusableWindowState) {
    if(focusableWindowState) {
      Window owner = getOwner();
      if(owner instanceof CShell) {
        setActivationEventsBlocked(false);
        ((CShell)owner).setActivationEventsBlocked(false);
      }
    }
    super.setFocusableWindowState(focusableWindowState);
  }
  
  public void setIconImage(Image image) {
    if(Compatibility.IS_JAVA_6_OR_GREATER) {
      super.setIconImage(image);
    }
  }
  
  public void setIconImages(List imageList) {
    if(Compatibility.IS_JAVA_6_OR_GREATER) {
      super.setIconImages(imageList);
    }
  }

  protected boolean isModallyBlocked;

  public void setModallyBlocked(boolean isModallyBlocked) {
    this.isModallyBlocked = isModallyBlocked;
    super.setEnabled(!isModallyBlocked && isEnabled);
  }
  
  public boolean isModallyBlocked() {
    return isModallyBlocked;
  }
  
  protected boolean isEnabled = true;
  
  public void setEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
    super.setEnabled(isEnabled);
  }
  
  public boolean isEnabled() {
    return isEnabled;
  }

  public Cursor getCursor() {
    return Utils.globalCursor != null? Utils.globalCursor: super.getCursor();
  }

}

class CShellPanel extends JPanel implements CShell {
  protected Shell handle;
  public Container getSwingComponent() {
    return this;
  }
  public Control getSWTHandle() {
    return handle;
  }
  public Container getClientArea() {
    return this;
  }
  
  protected UserAttributeHandler userAttributeHandler;
  
  public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }
  
  public void setActivationEventsBlocked(boolean areActivationEventsBlocked) {}
  
  public CShellPanel(Shell shell, int style) {
    handle = shell;
    userAttributeHandler = new UserAttributeHandler(this);
    init(style);
  }
  protected void init(int style) {
//    addHierarchyListener(new HierarchyListener() {
//      protected Window window;
//      public void hierarchyChanged(HierarchyEvent e) {
//        Window window = SwingUtilities.getWindowAncestor(CShellPanel.this);
//        boolean isEnabled = window != null;
//        if(!getModalityHandler().setEnabled(isEnabled)) {
//          return;
//        }
//        System.err.println(isEnabled);
//        if(isEnabled) {
//          if(window.getFocusableWindowState()) {
//            window.setFocusableWindowState(false);
//            this.window = window;
//          }
//        } else if(this.window != null) {
//          this.window.setFocusableWindowState(false);
//        }
//        
//      }
//    });
  }
  public void forceActive() {
  }
  public String getTitle() {
    return null;
  }
  public void setTitle(String title) {
  }
  public void toFront() {
  }
  public int getExtendedState() {
    return 0;
  }
  public void setExtendedState(int state) {
  }
  public void setIconImage(Image image) {
  }
  public void setIconImages(List imageList) {
  }
  public void setJMenuBar(JMenuBar menuBar) {
  }
  public void setDefaultButton(CButton button) {
  }
  protected ModalityHandler modalityHandler = new ModalityHandler(this);
  public ModalityHandler getModalityHandler() {
    return modalityHandler;
  }
  public JScrollBar getHorizontalScrollBar() {
    return null;
  }
  public JScrollBar getVerticalScrollBar() {
    return null;
  }
  public void setBackgroundImage(Image backgroundImage) {
  }
  public void setBackgroundInheritance(int backgroundInheritanceType) {
  }
  protected boolean isModallyBlocked;
  public void setModallyBlocked(boolean isModallyBlocked) {
    this.isModallyBlocked = isModallyBlocked;
    super.setEnabled(!isModallyBlocked && isEnabled);
  }
  public boolean isModallyBlocked() {
    return isModallyBlocked;
  }
  protected boolean isEnabled = true;
  public void setEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
    super.setEnabled(isEnabled);
  }
  public boolean isEnabled() {
    return isEnabled;
  }
}

/**
 * The shell equivalent on the Swing side.
 * @version 1.0 2005.03.13
 * @author Christopher Deckers (chrriis@nextencia.net)
 */
public interface CShell extends CScrollable {

  public static interface CEmbeddedShell {}
  
  public static class ModalityHandler {
    
    protected List blockerList = new ArrayList(0);
    protected static List applicationBlockerList = new ArrayList(0);

    protected static final CShell[] NO_BLOCKERS = new CShell[0];
    protected static Set windowSet = new HashSet();
    
    public static void initialize() {
      Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
      defaultToolkit.addAWTEventListener(new AWTEventListener() {
        public void eventDispatched(AWTEvent event) {
          if(event.getSource() instanceof Window) {
            switch(event.getID()) {
              case ComponentEvent.COMPONENT_SHOWN:
                windowSet.add(event.getSource());
                break;
              case ComponentEvent.COMPONENT_HIDDEN:
                windowSet.remove(event.getSource());
                break;
            }
          }
        }
      }, ComponentEvent.COMPONENT_EVENT_MASK);
      defaultToolkit.addAWTEventListener(new AWTEventListener() {
        public void eventDispatched(AWTEvent event) {
          InputEvent ie = (InputEvent)event;
          Component component = ie.getComponent();
          if(component != null) {
            Window window = SwingUtilities.getWindowAncestor(component);
            if(window instanceof CShell) {
              CShell[] blockers = ((CShell)window).getModalityHandler().getBlockers();
              if(blockers.length != 0) {
                if(ie.getID() == MouseEvent.MOUSE_PRESSED) {
                  blockers[0].getModalityHandler().advertiseBlocker();
                }
                ie.consume();
                return;
              }
            }
          }
        }
      }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
    }
    
    protected boolean isBlocked() {
      if(blockerList.isEmpty() && applicationBlockerList.isEmpty()) {
        return false;
      }
      for(int i=blockerList.size()-1; i>= 0; i--) {
        if(blockerList.get(i) == cShell) {
          return true;
        }
      }
      if(!applicationBlockerList.isEmpty()) {
        for(int i=applicationBlockerList.size()-1; i>= 0; i--) {
          if(applicationBlockerList.get(i) == cShell) {
            return false;
          }
        }
        return true;
      }
      return false;
    }
    
    protected CShell[] getBlockers() {
      if(blockerList.isEmpty() && applicationBlockerList.isEmpty()) {
        return NO_BLOCKERS;
      }
      if(blockerList.size() + applicationBlockerList.size() == 1) {
        CShell blocker = blockerList.isEmpty()? (CShell)applicationBlockerList.get(0): (CShell)blockerList.get(0);
        if(blocker == cShell) {
          return NO_BLOCKERS;
        }
        return new CShell[] {blocker};
      }
      List shellList = new ArrayList();
      for(int i=blockerList.size()-1; i>= 0; i--) {
        Object blocker = blockerList.get(i);
        if(blocker != cShell) {
          shellList.add(blocker);
        }
      }
      if(!applicationBlockerList.contains(cShell)) {
        for(int i=applicationBlockerList.size()-1; i>= 0; i--) {
          Object blocker = applicationBlockerList.get(i);
          shellList.add(blocker);
        }
      }
      return (CShell[])shellList.toArray(new CShell[0]);
    }
    
    protected Shell parent;
    protected CShell cShell;
    
    protected ModalityHandler(CShell cShell) {
      this.cShell = cShell;
    }
    
    protected boolean isEnabled;
    
    protected void adjustBlockedShells() {
      for(Iterator it=windowSet.iterator(); it.hasNext(); ) {
        Window window = (Window)it.next();
        if(window instanceof CShell) {
          CShell cShell = (CShell)window;
          cShell.setModallyBlocked(cShell.getModalityHandler().isBlocked());
        }
      }
    }
    
    protected void setEnabled(boolean isEnabled) {
      if(this.isEnabled == isEnabled) {
        return;
      }
      Control handle = cShell.getSWTHandle();
      if(isEnabled) {
        if(!handle.isDisposed()) {
          parent = (Shell)cShell.getSWTHandle().getParent();
          this.isEnabled = isEnabled;
          int style = handle.getStyle();
          if((style & SWT.APPLICATION_MODAL) != 0 || (style & SWT.SYSTEM_MODAL) != 0) {
            applicationBlockerList.add(cShell);
          } else if((style & SWT.PRIMARY_MODAL) != 0) {
            Composite parent = handle.getParent();
            if(parent != null) {
              ((CShell)parent.handle).getModalityHandler().blockerList.add(cShell);
            }
          }
        }
      } else {
        applicationBlockerList.remove(cShell);
        if(parent != null) {
          ((CShell)parent.handle).getModalityHandler().blockerList.remove(cShell);
        }
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            // Empty event
          }
        });
      }
      adjustBlockedShells();
    }
    
    protected void advertiseBlocker() {
//      Toolkit.getDefaultToolkit().beep();
      cShell.toFront();
//      SwingUtilities.invokeLater(new Runnable() {
//        public void run() {
//          final JDialog dialog = new JDialog((Window)cShell);
//          dialog.setLocation(Integer.MIN_VALUE, Integer.MIN_VALUE);
//          dialog.setVisible(true);
//          SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//              dialog.setVisible(false);
//              cShell.toFront();
//              SwingUtilities.invokeLater(new Runnable() {
//                public void run() {
//                  dialog.setVisible(true);
//                  SwingUtilities.invokeLater(new Runnable() {
//                    public void run() {
//                      dialog.dispose();
//                      cShell.toFront();
//                    }
//                  });
//                }
//              });
//            }
//          });
//        }
//      });
    }
  }
  
  public static int MAXIMIZED_BOTH = JFrame.MAXIMIZED_BOTH;
  public static int ICONIFIED = JFrame.ICONIFIED;
  public static int NORMAL = JFrame.NORMAL;

  public static class Factory {
    private Factory() {}

    static {
      Toolkit.getDefaultToolkit().setDynamicLayout(true);
    }

    public static CShell newInstance(Shell shell, CShell parent, int style) {
      if(shell instanceof CEmbeddedShell) {
        return new CShellPanel(shell, style);
      }
      if(parent instanceof CShellFrame) {
        return new CShellDialog(shell, (CShellFrame)parent, style);
      }
      if(parent instanceof CShellDialog) {
        return new CShellDialog(shell, (CShellDialog)parent, style);
      }
      return new CShellFrame(shell, style);
    }

  }

  public void forceActive();

  public String getTitle();

  public void setTitle(String title);

  public void toFront();

  public int getExtendedState();

  public void setExtendedState(int state);

  public void setIconImage(Image image);

  public void setIconImages(List imageList);

  public void setJMenuBar(JMenuBar menuBar);

  public void setDefaultButton(CButton button);

  public ModalityHandler getModalityHandler();
  
  public void setModallyBlocked(boolean isModallyBlocked);
  
  public boolean isModallyBlocked();
  
  public void setActivationEventsBlocked(boolean areActivationEventsBlocked);
  
}