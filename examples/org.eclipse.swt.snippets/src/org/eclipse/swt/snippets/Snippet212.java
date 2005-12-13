package org.eclipse.swt.snippets;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.custom.*;

/*
 * SWT StyledText snippet: embed images in a StyledText.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.2
 */
public class Snippet212 {
	
	static StyledText styledText;
	static String text = 
		"This snippet shows how to embed images in a StyledText. Here is one: \uFFFC, and here is another: \uFFFC. Use the add button" +
		" to add an image from your filesystem to the StyledText at the current caret offset.";
	static Image[] images;
	static int[] offsets;

	static void addImage(Image image, int offset) {
		StyleRange style = new StyleRange ();
		style.start = offset;
		style.length = 1;
		Rectangle rect = image.getBounds();
		style.metrics = new GlyphMetrics(rect.height, 0, rect.width);
		styledText.setStyleRange(style);		
	}
	
	public static void main(String [] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		styledText = new StyledText(shell, SWT.WRAP | SWT.BORDER);
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		styledText.setText(text);
		images = new Image[] {
			display.getSystemImage(SWT.ICON_QUESTION),
			display.getSystemImage(SWT.ICON_INFORMATION),
		};
		offsets = new int[2];
		int lastOffset = 0;
		for (int i = 0; i < offsets.length; i++) {
			int offset = text.indexOf("\uFFFC", lastOffset);
			offsets[i] = offset;
			addImage(images[i], offset);
			lastOffset = offset + 1;
		}
		
		// use a verify listener to keep the offsets up to data
		styledText.addVerifyListener(new VerifyListener()  {
			public void verifyText(VerifyEvent e) {
				int start = e.start;
				int replaceCharCount = e.end - e.start;
				int newCharCount = e.text.length();
				for (int i = 0; i < offsets.length; i++) {
					int offset = offsets[i];
					if (start <= offset && offset < start + replaceCharCount) {
						// this image is being deleted from the text
						if (images[i] != null && !images[i].isDisposed()) {
							images[i].dispose();
							images[i] = null;
						}
						offset = -1;
					}
					if (offset != -1 && offset >= start) offset += newCharCount - replaceCharCount;
					offsets[i] = offset;
				}
			}
		});
		styledText.addPaintObjectListener(new PaintObjectListener() {
			public void paintObject(PaintObjectEvent event) {
				GC gc = event.gc;
				StyleRange style = event.style;
				int start = style.start;
				for (int i = 0; i < offsets.length; i++) {
					int offset = offsets[i];
					if (start == offset) {
						Image image = images[i];
						int x = event.x;
						int y = event.y + event.ascent - style.metrics.ascent;						
						gc.drawImage(image, x, y);
					}
				}
			}
		});
		
		Button button = new Button (shell, SWT.PUSH);
		button.setText("Add Image");
		button.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				FileDialog dialog = new FileDialog(shell);
				String filename = dialog.open();
				if (filename != null) {
					try {
						Image image = new Image(display, filename);
						int offset = styledText.getCaretOffset();
						styledText.replaceTextRange(offset, 0, "\uFFFC");
						int index = 0;
						while (index < offsets.length) {
							if (offsets[index] == -1 && images[index] == null) break;
							index++;
						}
						if (index == offsets.length) {
							int[] tmpOffsets = new int[index + 1];
							System.arraycopy(offsets, 0, tmpOffsets, 0, offsets.length);
							offsets = tmpOffsets;
							Image[] tmpImages = new Image[index + 1];
							System.arraycopy(images, 0, tmpImages, 0, images.length);
							images = tmpImages;
						}
						offsets[index] = offset;
						images[index] = image;
						addImage(image, offset);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}				
			}
		});
		shell.setSize(400, 400);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		for (int i = 0; i < images.length; i++) {
			Image image = images[i];
			if (image != null && !image.isDisposed()) {
				image.dispose();
			}
		}
		display.dispose();
	}
}
