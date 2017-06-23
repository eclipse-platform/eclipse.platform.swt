package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/*
 * Title: Bug 287083 - [Widgets] KeyListener on Table doesn't receive KeyEvents
 * How to run: launch snippet and type into the Table
 * Bug description: KeyEvents are only fired for first key pressed and ESC key.
 * Expected results: KeyEvents should fire properly for all keys pressed.
 * GTK Version(s): GTK2.x, GTK3.x
 */
public class Bug287038_TableKeyEvent {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        final Table table = new Table(shell, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        for (int i = 0; i < 2; i++) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setWidth(100);
        }
        String[] itemsToAdd = new String[] {
                "Cheese", "Potatoes", "Rocks", "Bacon", "Tree", "Forest", "Chocolate", "Broccoli", "Turnip",
                "Squash", "Carrot"
        };
        int i = 0;
        for (String s : itemsToAdd) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(new String[] {
                    s, "" + i++
            });
        }
        table.addKeyListener(new KeyListener() {
            @Override
			public void keyPressed(KeyEvent e) {
                System.out.println(e.character);
            }

            @Override
			public void keyReleased(KeyEvent e) {
                System.out.println(e.character);
            }
        });
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
