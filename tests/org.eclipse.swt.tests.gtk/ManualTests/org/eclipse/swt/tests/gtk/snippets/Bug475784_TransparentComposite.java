/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.PathData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Bug475784_TransparentComposite extends Shell
{
  public static int RADIUS_OF_ROUND_EDGES = 30;

  /**
   * Launch the application.
   * @param args
   */
  public static void main(String args[])
  {
    try
    {
      Display display = Display.getDefault();
      Bug475784_TransparentComposite shell = new Bug475784_TransparentComposite(display);
      shell.open();
      shell.layout();
      while (!shell.isDisposed())
      {
        if (!display.readAndDispatch())
        {
          display.sleep();
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Create the shell.
   * @param display
   */
  public Bug475784_TransparentComposite(final Display display)
  {
    super(display, SWT.SHELL_TRIM);
    setLayout(new GridLayout(1, false));
    final Composite composite = new Composite(this, SWT.NONE);
    composite.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
    composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    composite.setLayout(new GridLayout(1, false));
    //overlay composite
    final Composite overlay = new Composite(composite, SWT.NO_BACKGROUND | SWT.NO_FOCUS);
    overlay.setLayout(new FillLayout());
    overlay.setVisible(false);
    //label
    Label textLabel = new Label(composite, SWT.NONE);
    textLabel.setText("Hello World!\nHello World!");
    Button btnCheckButton = new Button(composite, SWT.CHECK);
    btnCheckButton.setText("Check Button");
    Button btnRadioButton = new Button(composite, SWT.RADIO);
    btnRadioButton.setText("Radio Button");
    //Composite for drawings
    Composite compDraws = new Composite(composite, SWT.NONE);
    compDraws.addPaintListener(e -> {
        e.gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
        e.gc.fillArc(0, 0, 50, 50, 0, 360);
      });
    //exclude overlay composite
    GridData gData = new GridData();
    gData.exclude = true;
    overlay.setLayoutData(gData);
    //set region
    Region reg = new Region();
    getRegionForRoundRectangle(reg, 0, 0, 200, 200, RADIUS_OF_ROUND_EDGES);
    overlay.setRegion(reg);
    reg.dispose();
    //toggle overlay button
    final Button btnNewButton = new Button(composite, SWT.NONE);
    btnNewButton.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1));
    btnNewButton.setText("Toggle overlay");
    GridData gdDataButton = new GridData(GridData.VERTICAL_ALIGN_END);
    gdDataButton.horizontalAlignment = SWT.LEFT;
    gdDataButton.verticalAlignment = SWT.BOTTOM;
    gdDataButton.grabExcessVerticalSpace = true;
    btnNewButton.setLayoutData(gdDataButton);
    btnNewButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        int width = composite.getSize().x;
        int height = composite.getSize().y - btnNewButton.getSize().y - 10;
        overlay.setSize(width, height);
        overlay.setVisible(!overlay.isVisible());
      }
    });
    overlay.moveAbove(null);
    overlay.addPaintListener(e -> {
        e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
        e.gc.setAlpha(150);
        e.gc.fillRectangle(new Rectangle(0, 0, overlay.getSize().x, overlay.getSize().y));
      });
    createContents();
  }

  /**
   * Create contents of the shell.
   */
  protected void createContents()
  {
    setText("SWT Application");
    setSize(450, 300);
  }

  @Override
  protected void checkSubclass()
  {
    // Disable the check that prevents subclassing of SWT components
  }

  public static void getCircleRegion(Region region, int r, int offsetX, int offsetY)
  {
    int[] polygon = new int[8 * r + 4];
    // x^2 + y^2 = r^2
    for (int i = 0; i < 2 * r + 1; i++)
    {
      int x = i - r;
      int y = (int) Math.sqrt(r * r - x * x);
      polygon[2 * i] = offsetX + x;
      polygon[2 * i + 1] = offsetY + y;
      polygon[8 * r - 2 * i - 2] = offsetX + x;
      polygon[8 * r - 2 * i - 1] = offsetY - y;
    }
    region.add(polygon);
  }

  public static void drawRoundFrame(GC gc, int width, int height)
  {
    Rectangle tmpClippingRect = gc.getClipping();
    Region reg = new Region();
    gc.getClipping(reg);
    Region roundReg = new Region();
    getRegionForRoundRectangle(roundReg, 1, 1, width, height, RADIUS_OF_ROUND_EDGES - 1);
    reg.subtract(roundReg);
    gc.setClipping(reg);
    gc.fillRectangle(0, 0, width + 1, height + 1);
    reg.dispose();
    roundReg.dispose();
    gc.setClipping(tmpClippingRect);
  }

  public static void loadPath(Region region, float[] points, byte[] types)
  {
    int start = 0, end = 0;
    for (int i = 0; i < types.length; i++)
    {
      switch (types[i])
      {
        case SWT.PATH_MOVE_TO:
        {
          if (start != end)
          {
            int n = 0;
            int[] temp = new int[end - start];
            for (int k = start; k < end; k++)
            {
              temp[n++] = Math.round(points[k]);
            }
            region.add(temp);
          }
          start = end;
          end += 2;
          break;
        }
        case SWT.PATH_LINE_TO:
        {
          end += 2;
          break;
        }
        case SWT.PATH_CLOSE:
        {
          if (start != end)
          {
            int n = 0;
            int[] temp = new int[end - start];
            for (int k = start; k < end; k++)
            {
              temp[n++] = Math.round(points[k]);
            }
            region.add(temp);
          }
          start = end;
          break;
        }
      }
    }
  }

  public static Path getPathForRoundRectangle(int x, int y, int width, int height, int r)
  {
    Path path = new Path(Display.getDefault());
    path.addArc(x, y, 2 * r, 2 * r, 0, 360);
    path.addArc(width - 2 * r, y, 2 * r, 2 * r, 0, 360);
    path.addArc(x, height - 2 * r, 2 * r, 2 * r, 0, 360);
    path.addArc(width - 2 * r, height - 2 * r, 2 * r, 2 * r, 0, 360);
    path.addRectangle(x, r - y, width - x, height - 2 * r);
    path.addRectangle(r - x, y, width - 2 * r, height - y);
    Path path2 = new Path(Display.getDefault(), path, 0.0001f);
    path.dispose();
    return path2;
  }

  public static void drawRoundRectangleByPath(GC gc, int x, int y, int width, int height)
  {
    Path path = getPathForRoundRectangle(x, y, width, height, RADIUS_OF_ROUND_EDGES - 1);
    gc.drawPath(path);
    path.dispose();
  }

  public static void getRegionForRoundRectangle(Region reg, int x, int y, int width, int height, int r)
  {
    Path tmpPath = getPathForRoundRectangle(x, y, width, height, r);
    PathData data = tmpPath.getPathData();
    tmpPath.dispose();
    loadPath(reg, data.points, data.types);
  }
}