package org.eclipse.swt.tests.cocoa.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug569088_macOS_RadioButtonNotRendered {

  public static void main(String[] args) {
    final Display display = new Display();

    final Shell shell = new Shell(display);
    shell.setSize(300, 300);
    shell.setLayout(new GridLayout(2, true));

    final Button button1 = new Button(shell, SWT.RADIO | SWT.WRAP);
    button1.setText("Button text 1 is loooooooong");
    button1.setForeground(display.getSystemColor(SWT.COLOR_RED));
    button1.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
    button1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

    final Button button2 = new Button(shell, SWT.CHECK | SWT.WRAP);
    button2.setText("Button text 2  is loooooooong");
    button2.setForeground(display.getSystemColor(SWT.COLOR_RED));
    button2.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
    button2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

    shell.open();
    System.err.println(button1.getBounds()); // Rectangle {5, 5, 142, 0}
    System.err.println(button2.getBounds()); // Rectangle {152, 10, 142, 0}
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();
  }

}
