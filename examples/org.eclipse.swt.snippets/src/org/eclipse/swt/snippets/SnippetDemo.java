package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SnippetDemo {
    public static void main(String[] args) {
        // Create display and shell
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Snippet316 - GridLayout with Button");
        shell.setSize(300, 200);

        // Set a GridLayout with 1 column
        shell.setLayout(new GridLayout(1, false));

        // Create a button with text "abcd"
        Button button = new Button(shell, SWT.PUSH);
        button.setText("abcd");

        // Open the shell
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}
