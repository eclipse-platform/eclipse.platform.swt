package org.eclipse.swt.examples.explorer;import org.eclipse.swt.graphics.*;


/* package */ class FileDisplayInfo {
	public String nameString;
	public String typeString;
	public String sizeString;
	public String dateString;
	public Image iconImage;
	public boolean iconCached;
	
	public FileDisplayInfo(String name, String type, String size, String date,
		Image icon, boolean iconCached) {
		this.nameString = name;
		this.typeString = type;
		this.sizeString = size;
		this.dateString = date;
		this.iconImage = icon;
		this.iconCached = iconCached;
	}
}
