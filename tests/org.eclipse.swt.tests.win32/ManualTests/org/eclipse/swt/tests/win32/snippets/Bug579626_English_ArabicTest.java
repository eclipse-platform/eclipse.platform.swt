package org.eclipse.swt.tests.win32.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

class Bug579626_English_ArabicTest {

    private static final String MIXED_TEXT = "English جارِ التحميل";

    public static void main(String[] args) {
        final Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new GridLayout());

        System.out.println(MIXED_TEXT);
        // This is rendered correctly
        Text text = new Text(shell, SWT.BORDER);
        text.setText(MIXED_TEXT);

        // This is not rendered correctly
        StyledText styled = new StyledText(shell, SWT.BORDER | SWT.MULTI);
        styled.setText(MIXED_TEXT);

        shell.pack();
        shell.open();

        while(!shell.isDisposed()) {
            if(!display.readAndDispatch()) display.sleep();
        }
    }
}