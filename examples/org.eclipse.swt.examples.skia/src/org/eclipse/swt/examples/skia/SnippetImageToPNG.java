package org.eclipse.swt.examples.skia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

public class SnippetImageToPNG {

	final static boolean DELETE_OUTPUT_DIR = true;

	record ImageInfo(String name, Image image) {
	}

	record FormatInfo(String format, int type) {
	}


	private final static List<FormatInfo> formats = List.of(
//			new FormatInfo(".gif", SWT.IMAGE_GIF), // SWT does not support saving GIFs for these images
//			new FormatInfo(".ico", SWT.IMAGE_ICO), // SWT does not support saving ICOs for these images
			new FormatInfo(".bmp", SWT.IMAGE_BMP),
			new FormatInfo(".jpef", SWT.IMAGE_JPEG),
			new FormatInfo(".png", SWT.IMAGE_PNG)
	);


	private static final String IMAGES_PATH = "images";
	private final static ArrayList<ImageInfo> swtImages = new ArrayList<>();

	public static void main(String[] args) {
		Display display = new Display();

		// Load images
		swtImages.add(new ImageInfo("question", Display.getDefault().getSystemImage(SWT.ICON_QUESTION)));
		swtImages.add(new ImageInfo("error", Display.getDefault().getSystemImage(SWT.ICON_ERROR)));
		swtImages.add(new ImageInfo("info", Display.getDefault().getSystemImage(SWT.ICON_INFORMATION)));

		List<File> imageFiles = new ArrayList<>();
		// Use working directory reliably
		File imagesDir = new File(System.getProperty("user.dir"), IMAGES_PATH);
		if (imagesDir.exists() && imagesDir.isDirectory()) {
			for (File f : imagesDir.listFiles()) {
				String name = f.getName().toLowerCase();
				if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif")
						|| name.endsWith(".bmp") || name.endsWith(".ico")) {
					try {
						swtImages.add(new ImageInfo(name, new Image(display, f.getAbsolutePath())));
						imageFiles.add(f);
					} catch (Exception e) {
						// skip invalid image
					}
				}
			}
		}

		File f = new File("./out");

		if (f.exists()) {
			for (File oldFile : f.listFiles()) {
				oldFile.delete();
			}
		}
		f.mkdirs();

		for (FormatInfo format : formats) {
			System.out.println(format.format + " (" + format.type + "):");
			int i = 0;
			for (ImageInfo img : swtImages) {

//				printHasAlpha(img, "original image " + i);

				ImageLoader loader = new ImageLoader();
				loader.data = new ImageData[] { img.image.getImageData() };
				String outPath = "./out/out" + i + format.format;
				loader.save(outPath, format.type);

				if (format.type == SWT.IMAGE_PNG) {
					printHasAlpha(outPath);
				}

				i++;
			}

		}

		if (DELETE_OUTPUT_DIR) {
			for (File oldFile : f.listFiles()) {
				oldFile.delete();
			}
			f.delete();

		}
	}

//	private static void printHasAlpha(ImageInfo img, String outPath) {
//		// Transparenzprüfung
//		ImageData pngData = img.image.getImageData();
//		boolean hasAlpha = pngData.alphaData != null || pngData.alpha != -1;
//		System.out.println("Path: " + img.name + " hasAlpha=" + hasAlpha + " alphaData="
//				+ (pngData.alphaData != null ? pngData.alphaData.length : "null") + " alpha=" + pngData.alpha);
//
//	}

	private static void printHasAlpha(String outPath) {
		// Transparenzprüfung
		ImageData pngData = new ImageLoader().load(outPath)[0];
		boolean hasAlpha = pngData.alphaData != null || pngData.alpha != -1;
		System.out.println("Path: " + outPath + " hasAlpha=" + hasAlpha + " alphaData="
				+ (pngData.alphaData != null ? pngData.alphaData.length : "null") + " alpha=" + pngData.alpha);
	}

}
