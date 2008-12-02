package org.eclipse.swt.internal.mozilla;


public class nsIWritableVariant extends nsIVariant {

	static final int LAST_METHOD_ID = nsIVariant.LAST_METHOD_ID + 31;

	public static final String NS_IWRITABLEVARIANT_IID_STR =
		"5586a590-8c82-11d5-90f3-0010a4e73d9a";

	public static final nsID NS_IWRITABLEVARIANT_IID =
		new nsID(NS_IWRITABLEVARIANT_IID_STR);

	public nsIWritableVariant(int /*long*/ address) {
		super(address);
	}

	public int GetWritable(int[] aWritable) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 1, getAddress(), aWritable);
	}

	public int SetWritable(int aWritable) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 2, getAddress(), aWritable);
	}

//	public int SetAsInt8(!ERROR UNKNOWN C TYPE <PRUint8 >! aValue) {
//		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 3, getAddress(), aValue);
//	}
//
//	public int SetAsInt16(!ERROR UNKNOWN C TYPE <PRInt16 >! aValue) {
//		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 4, getAddress(), aValue);
//	}

	public int SetAsInt32(int aValue) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 5, getAddress(), aValue);
	}

	public int SetAsInt64(long aValue) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 6, getAddress(), aValue);
	}

//	public int SetAsUint8(!ERROR UNKNOWN C TYPE <PRUint8 >! aValue) {
//		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 7, getAddress(), aValue);
//	}

	public int SetAsUint16(short aValue) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 8, getAddress(), aValue);
	}

	public int SetAsUint32(int aValue) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 9, getAddress(), aValue);
	}

//	public int SetAsUint64(!ERROR UNKNOWN C TYPE <PRUint64 >! aValue) {
//		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 10, getAddress(), aValue);
//	}

	public int SetAsFloat(float aValue) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 11, getAddress(), aValue);
	}

	public int SetAsDouble(double aValue) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 12, getAddress(), aValue);
	}

	public int SetAsBool(int aValue) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 13, getAddress(), aValue);
	}

//	public int SetAsChar(!ERROR UNKNOWN C TYPE <char >! aValue) {
//		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 14, getAddress(), aValue);
//	}
//
//	public int SetAsWChar(!ERROR UNKNOWN C TYPE <PRUnichar >! aValue) {
//		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 15, getAddress(), aValue);
//	}

	public int SetAsID(int /*long*/ aValue) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 16, getAddress(), aValue);
	}

	public int SetAsAString(int /*long*/ aValue) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 17, getAddress(), aValue);
	}

	public int SetAsDOMString(int /*long*/ aValue) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 18, getAddress(), aValue);
	}

	public int SetAsACString(int /*long*/ aValue) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 19, getAddress(), aValue);
	}

	public int SetAsAUTF8String(int /*long*/ aValue) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 20, getAddress(), aValue);
	}

	public int SetAsString(byte[] aValue) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 21, getAddress(), aValue);
	}

	public int SetAsWString(char[] aValue) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 22, getAddress(), aValue);
	}

	public int SetAsISupports(int /*long*/ aValue) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 23, getAddress(), aValue);
	}

	public int SetAsInterface(nsID iid, int /*long*/ iface) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 24, getAddress(), iid, iface);
	}

	public int SetAsArray(short type, int /*long*/ iid, int count, int /*long*/ ptr) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 25, getAddress(), type, iid, count, ptr);
	}

	public int SetAsStringWithSize(int size, byte[] str) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 26, getAddress(), size, str);
	}

	public int SetAsWStringWithSize(int size, char[] str) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 27, getAddress(), size, str);
	}

	public int SetAsVoid() {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 28, getAddress());
	}

	public int SetAsEmpty() {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 29, getAddress());
	}

	public int SetAsEmptyArray() {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 30, getAddress());
	}

	public int SetFromVariant(int /*long*/ aValue) {
		return XPCOM.VtblCall(nsIVariant.LAST_METHOD_ID + 31, getAddress(), aValue);
	}
}
