package org.eclipse.swt.examples.explorer;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.io.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

class Images {
	// Bitmap Images
	public static Image Copy;
	public static Image Cut;
	public static Image Delete;	
	public static Image Details;
	public static Image Disconnect;
	public static Image Drive;	
	public static Image File;
	public static Image Folder;
	public static Image FolderOpen;
	public static Image Large;	
	public static Image List;
	public static Image MapNetwork;
	public static Image Paste;
	public static Image Properties;	
	public static Image Small;		
	public static Image Undo;	
	public static Image UpDirectory;
	
	// Icon Images
	public static Image ShellIcon;
	
	// Cursors
	public static Cursor CursorWait;

	static Image  [] AllIcons;
	static Image  [] AllBitmaps;
	static Cursor [] AllCursors;

public static void freeAll () {
	if (AllIcons == null) return;
	for (int i=0; i<AllIcons.length; i++) AllIcons [i].dispose ();
	for (int i=0; i<AllBitmaps.length; i++) AllBitmaps [i].dispose ();
	for (int i=0; i<AllCursors.length; i++) AllCursors [i].dispose ();
	AllIcons = null;  AllBitmaps = null;  AllCursors = null;
}

static Image createBitmapImage(Display display, String fileName) {
	ImageData source = new ImageData(Images.class.getResourceAsStream(fileName+".bmp"));
	ImageData mask = new ImageData(Images.class.getResourceAsStream(fileName+"_mask.bmp"));
	return new Image (display, source, mask);
}
public static void loadAll (Display display) {

	// Bitmap Images
	Copy = createBitmapImage (display, "copy");
	Cut = createBitmapImage (display, "cut");
	Delete = createBitmapImage (display, "delete");	
	Details = createBitmapImage (display, "details");
	Disconnect = createBitmapImage (display, "disconnect");
	Drive = createBitmapImage (display, "drive");	
	File = createBitmapImage (display, "file");
	Folder = createBitmapImage (display, "folder");
	FolderOpen = createBitmapImage (display, "folderopen");
	Large = createBitmapImage (display, "largeicons");	
	List = createBitmapImage (display, "list");
	MapNetwork = createBitmapImage (display, "mapnetwork");
	Paste = createBitmapImage (display, "paste");
	Properties = createBitmapImage (display, "properties");	
	Small = createBitmapImage (display, "smallicons");		
	Undo = createBitmapImage (display, "undo");	
	UpDirectory = createBitmapImage (display, "up");
	
	// Icon Images
	ShellIcon = new Image(display, Images.class.getResourceAsStream("oti.ico"));
	
	// Cursors
	CursorWait = new Cursor (display, SWT.CURSOR_WAIT);
		
	AllBitmaps = new Image [] {
		Drive,
		Folder,
		FolderOpen,
		UpDirectory,
		MapNetwork,
		Disconnect,
		Cut,
		Copy,
		Paste,
		Undo,
		Delete,
		Properties,
		Large,
		Small,
		List,
		Details,
		File,
	};

	AllIcons = new Image [] {
		ShellIcon
		};
		
	AllCursors = new Cursor [] {
		CursorWait,
	};
}
}
