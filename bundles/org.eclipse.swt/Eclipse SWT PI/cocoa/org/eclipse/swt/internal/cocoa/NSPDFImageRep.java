package org.eclipse.swt.internal.cocoa;

public class NSPDFImageRep extends NSImageRep {

public NSPDFImageRep() {
	super();
}

public NSPDFImageRep(int id) {
	super(id);
}

public NSData PDFRepresentation() {
	int result = OS.objc_msgSend(this.id, OS.sel_PDFRepresentation);
	return result != 0 ? new NSData(result) : null;
}

public NSRect bounds() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_bounds);
	return result;
}

public int currentPage() {
	return OS.objc_msgSend(this.id, OS.sel_currentPage);
}

public static id imageRepWithData(NSData pdfData) {
	int result = OS.objc_msgSend(OS.class_NSPDFImageRep, OS.sel_imageRepWithData_1, pdfData != null ? pdfData.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithData(NSData pdfData) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithData_1, pdfData != null ? pdfData.id : 0);
	return result != 0 ? new id(result) : null;
}

public int pageCount() {
	return OS.objc_msgSend(this.id, OS.sel_pageCount);
}

public void setCurrentPage(int page) {
	OS.objc_msgSend(this.id, OS.sel_setCurrentPage_1, page);
}

}
