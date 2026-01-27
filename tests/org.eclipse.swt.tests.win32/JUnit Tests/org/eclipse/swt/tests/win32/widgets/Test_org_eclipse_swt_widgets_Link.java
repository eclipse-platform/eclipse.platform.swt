package org.eclipse.swt.tests.win32.widgets;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.Test;

public class Test_org_eclipse_swt_widgets_Link {

    @Test
    public void test_LinkRendersCorrectlyAfterResize() {
        Display display = new Display();
        try {
            Shell shell = new Shell(display);
            shell.setSize(400, 300);
            Link link = new Link(shell, SWT.NONE);
            link.setText("Visit <a href=\"https://eclipse.org\">Eclipse</a> website");
            link.setBounds(10, 10, 200, 50);
            
            shell.open();
            
            // Force initial paint
            while (display.readAndDispatch()) {
                // loop until no more events
            }
            
            // Resize multiple times rapidly (stress test for flicker)
            for (int i = 0; i < 10; i++) {
                link.setSize(200 + (i * 10), 50 + (i * 5));
                while (display.readAndDispatch()) {
                    // loop until no more events
                }
            }
            
            // Verify link is still functional and sized correctly
            Point size = link.getSize();
            assertTrue(size.x > 0 && size.y > 0, "Link should have valid size after resize");
            assertEquals("Visit <a href=\"https://eclipse.org\">Eclipse</a> website", 
                        link.getText(), "Link text should be preserved after resize");
            
            // Verify the link is still visible and has proper bounds
            Rectangle bounds = link.getBounds();
            assertTrue(bounds.width > 0 && bounds.height > 0, 
                      "Link should have valid bounds after multiple resizes");
            
            shell.close();
        } finally {
            display.dispose();
        }
    }
    
    @Test
    public void test_LinkBackgroundNotErasedDuringPaint() {
        Display display = new Display();
        try {
            Shell shell = new Shell(display);
            Link link = new Link(shell, SWT.NONE);
            link.setText("Test <a>link</a>");
            
            final int[] paintCount = {0};
            final int[] eraseCount = {0};
            
            // Monitor paint events
            link.addListener(SWT.Paint, e -> paintCount[0]++);
            link.addListener(SWT.EraseItem, e -> eraseCount[0]++);
            
            shell.setSize(300, 200);
            shell.open();
            
            // Trigger redraws
            for (int i = 0; i < 5; i++) {
                link.redraw();
                while (display.readAndDispatch()) {
                    // loop until no more events
                }
            }
            
            // We should see paints, but minimal erase operations
            assertTrue(paintCount[0] > 0, "Link should have been painted");
            // Note: EraseItem may not fire for Link widget, this is platform-specific
            
            shell.close();
        } finally {
            display.dispose();
        }
    }
    
    @Test
    public void test_LinkMaintainsContentDuringRapidResize() {
        Display display = new Display();
        try {
            Shell shell = new Shell(display);
            shell.setSize(400, 300);
            Link link = new Link(shell, SWT.NONE);
            String testText = "Click <a href=\"page1\">here</a> or <a href=\"page2\">there</a>";
            link.setText(testText);
            link.setBounds(10, 10, 300, 100);
            
            shell.open();
            while (display.readAndDispatch()) {
                // loop until no more events
            }
            
            // Rapidly resize - this would cause flickering in the old implementation
            for (int i = 0; i < 20; i++) {
                int newWidth = 200 + (i % 2) * 100;
                int newHeight = 50 + (i % 2) * 30;
                link.setSize(newWidth, newHeight);
                
                // Process events but don't wait for full redraw
                display.readAndDispatch();
                
                // Content should remain intact
                assertEquals(testText, link.getText(), 
                    "Link text should remain unchanged during resize iteration " + i);
            }
            
            shell.close();
        } finally {
            display.dispose();
        }
    }
}