package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.internal.win32.*;

public class COM extends OS {

	static {
		/* Initialize OLE */
		// OleInitialize Initializes the COM library on the current apartment 
		// and identifies the concurrency model as single-thread apartment (STA).
		// To use any other threading model, you must call CoInitializeEx
		OS.OleInitialize(0);
	}
	
	/* Constants */

	// GUIDs for Home Page Browser
	public static final GUID IIDIEditorSiteTime = IIDFromString("{6BD2AEFE-7876-45e6-A6E7-3BFCDF6540AA}");
	public static final GUID IIDIEditorSiteProperty = IIDFromString("{D381A1F4-2326-4f3c-AFB9-B7537DB9E238}");
	public static final GUID IIDIEditorBaseProperty = IIDFromString("{61E55B0B-2647-47c4-8C89-E736EF15D636}");
	public static final GUID IIDIEditorSite = IIDFromString("{CDD88AB9-B01D-426E-B0F0-30973E9A074B}");
	public static final GUID IIDIEditorService = IIDFromString("{BEE283FE-7B42-4FF3-8232-0F07D43ABCF1}");
	public static final GUID IIDIEditorManager = IIDFromString("{EFDE08C4-BE87-4B1A-BF84-15FC30207180}");
	
		
	public static final GUID IIDIAdviseSink = IIDFromString("{0000010F-0000-0000-C000-000000000046}");
	//public static final GUID IIDIAdviseSink2 = IIDFromString("{00000125-0000-0000-C000-000000000046}");
	//public static final GUID IIDIBindCtx = IIDFromString("{0000000E-0000-0000-C000-000000000046}");
	//public static final GUID IIDIClassFactory = IIDFromString("{00000001-0000-0000-C000-000000000046}");
	public static final GUID IIDIClassFactory2 = IIDFromString("{B196B28F-BAB4-101A-B69C-00AA00341D07}");
	public static final GUID IIDIConnectionPoint = IIDFromString("{B196B286-BAB4-101A-B69C-00AA00341D07}");
	public static final GUID IIDIConnectionPointContainer = IIDFromString("{B196B284-BAB4-101A-B69C-00AA00341D07}");
	//public static final GUID IIDICreateErrorInfo = IIDFromString("{22F03340-547D-101B-8E65-08002B2BD119}");
	//public static final GUID IIDICreateTypeInfo = IIDFromString("{00020405-0000-0000-C000-000000000046}");
	//public static final GUID IIDICreateTypeLib = IIDFromString("{00020406-0000-0000-C000-000000000046}");
	//public static final GUID IIDIDataAdviseHolder = IIDFromString("{00000110-0000-0000-C000-000000000046}");
	public static final GUID IIDIDataObject = IIDFromString("{0000010E-0000-0000-C000-000000000046}");
	public static final GUID IIDIDispatch = IIDFromString("{00020400-0000-0000-C000-000000000046}");
	public static final GUID IIDIDropSource = IIDFromString("{00000121-0000-0000-C000-000000000046}");
	public static final GUID IIDIDropTarget = IIDFromString("{00000122-0000-0000-C000-000000000046}");
	//public static final GUID IIDIEnumConnectionPoints = IIDFromString("{B196B285-BAB4-101A-B69C-00AA00341D07}");
	//public static final GUID IIDIEnumConnections = IIDFromString("{B196B287-BAB4-101A-B69C-00AA00341D07}");
	public static final GUID IIDIEnumFORMATETC = IIDFromString("{00000103-0000-0000-C000-000000000046}");
	//public static final GUID IIDIEnumMoniker = IIDFromString("{00000102-0000-0000-C000-000000000046}");
	//public static final GUID IIDIEnumOLEVERB = IIDFromString("{00000104-0000-0000-C000-000000000046}");
	//public static final GUID IIDIEnumSTATDATA = IIDFromString("{00000105-0000-0000-C000-000000000046}");
	//public static final GUID IIDIEnumSTATSTG = IIDFromString("{0000000D-0000-0000-C000-000000000046}");
	//public static final GUID IIDIEnumString = IIDFromString("{00000101-0000-0000-C000-000000000046}");
	//public static final GUID IIDIEnumUnknown = IIDFromString("{00000100-0000-0000-C000-000000000046}");
	//public static final GUID IIDIEnumVARIANT = IIDFromString("{00020404-0000-0000-C000-000000000046}");
	//public static final GUID IIDIErrorInfo = IIDFromString("{1CF2B120-547D-101B-8E65-08002B2BD119}");
	//public static final GUID IIDIErrorLog = IIDFromString("{3127CA40-446E-11CE-8135-00AA004BB851}");
	//public static final GUID IIDIExternalConnection = IIDFromString("{00000019-0000-0000-C000-000000000046}");
	public static final GUID IIDIFont = IIDFromString("{BEF6E002-A874-101A-8BBA-00AA00300CAB}");
	//public static final GUID IIDIFontDisp = IIDFromString("{BEF6E003-A874-101A-8BBA-00AA00300CAB}");
	//public static final GUID IIDILockBytes = IIDFromString("{0000000A-0000-0000-C000-000000000046}");
	//public static final GUID IIDIMalloc = IIDFromString("{00000002-0000-0000-C000-000000000046}");
	//public static final GUID IIDIMallocSpy = IIDFromString("{0000001D-0000-0000-C000-000000000046}");
	//public static final GUID IIDIMarshal = IIDFromString("{00000003-0000-0000-C000-000000000046}");
	//public static final GUID IIDIMessageFilter = IIDFromString("{00000016-0000-0000-C000-000000000046}");
	//public static final GUID IIDIMoniker = IIDFromString("{0000000F-0000-0000-C000-000000000046}");
	//public static final GUID IIDIOleAdviseHolder = IIDFromString("{00000111-0000-0000-C000-000000000046}");
	//public static final GUID IIDIOleCache = IIDFromString("{0000011E-0000-0000-C000-000000000046}");
	//public static final GUID IIDIOleCache2 = IIDFromString("{00000128-0000-0000-C000-000000000046}");
	//public static final GUID IIDIOleCacheControl = IIDFromString("{00000129-0000-0000-C000-000000000046}");
	public static final GUID IIDIOleClientSite = IIDFromString("{00000118-0000-0000-C000-000000000046}");
	public static final GUID IIDIOleCommandTarget = IIDFromString("{B722BCCB-4E68-101B-A2BC-00AA00404770}");
	public static final GUID IIDIOleContainer = IIDFromString("{0000011B-0000-0000-C000-000000000046}");
	public static final GUID IIDIOleControl = IIDFromString("{B196B288-BAB4-101A-B69C-00AA00341D07}");
	public static final GUID IIDIOleControlSite = IIDFromString("{B196B289-BAB4-101A-B69C-00AA00341D07}");
	public static final GUID IIDIOleInPlaceActiveObject = IIDFromString("{00000117-0000-0000-C000-000000000046}");
	public static final GUID IIDIOleInPlaceFrame = IIDFromString("{00000116-0000-0000-C000-000000000046}");
	public static final GUID IIDIOleInPlaceObject = IIDFromString("{00000113-0000-0000-C000-000000000046}");
	public static final GUID IIDIOleInPlaceSite = IIDFromString("{00000119-0000-0000-C000-000000000046}");
	public static final GUID IIDIOleInPlaceUIWindow = IIDFromString("{00000115-0000-0000-C000-000000000046}");
	//public static final GUID IIDIOleItemContainer = IIDFromString("{0000011C-0000-0000-C000-000000000046}");
	public static final GUID IIDIOleLink = IIDFromString("{0000011D-0000-0000-C000-000000000046}");
	public static final GUID IIDIOleObject = IIDFromString("{00000112-0000-0000-C000-000000000046}");
	public static final GUID IIDIOleWindow = IIDFromString("{00000114-0000-0000-C000-000000000046}");
	//public static final GUID IIDIParseDisplayName = IIDFromString("{0000011A-0000-0000-C000-000000000046}");
	//public static final GUID IIDIPerPropertyBrowsing = IIDFromString("{376BD3AA-3845-101B-84ED-08002B2EC713}");
	public static final GUID IIDIPersist = IIDFromString("{0000010C-0000-0000-C000-000000000046}");
	public static final GUID IIDIPersistFile = IIDFromString("{0000010B-0000-0000-C000-000000000046}");
	//public static final GUID IIDIPersistMemory = IIDFromString("{BD1AE5E0-A6AE-11CE-BD37-504200C10000}");
	//public static final GUID IIDIPersistPropertyBag = IIDFromString("{37D84F60-42CB-11CE-8135-00AA004BB851}");
	public static final GUID IIDIPersistStorage = IIDFromString("{0000010A-0000-0000-C000-000000000046}");
	public static final GUID IIDIPersistStream = IIDFromString("{00000109-0000-0000-C000-000000000046}");
	//public static final GUID IIDIPersistStreamInit = IIDFromString("{7FD52380-4E07-101B-AE2D-08002B2EC713}");
	//public static final GUID IIDIPicture = IIDFromString("{7BF80980-BF32-101A-8BBB-00AA00300CAB}");
	//public static final GUID IIDIPictureDisp = IIDFromString("{7BF80981-BF32-101A-8BBB-00AA00300CAB}");
	//public static final GUID IIDIPropertyBag = IIDFromString("{55272A00-42CB-11CE-8135-00AA004BB851}");
	public static final GUID IIDIPropertyNotifySink = IIDFromString("{9BFBBC02-EFF1-101A-84ED-00AA00341D07}");
	//public static final GUID IIDIPropertyPage = IIDFromString("{B196B28D-BAB4-101A-B69C-00AA00341D07}");
	//public static final GUID IIDIPropertyPage2 = IIDFromString("{01E44665-24AC-101B-84ED-08002B2EC713}");
	//public static final GUID IIDIPropertyPageSite = IIDFromString("{B196B28C-BAB4-101A-B69C-00AA00341D07}");
	public static final GUID IIDIProvideClassInfo = IIDFromString("{B196B283-BAB4-101A-B69C-00AA00341D07}");
	public static final GUID IIDIProvideClassInfo2 = IIDFromString("{A6BC3AC0-DBAA-11CE-9DE3-00AA004BB851}");
	//public static final GUID IIDIPSFactoryBuffer = IIDFromString("{D5F569D0-593B-101A-B569-08002B2DBF7A}");
	//public static final GUID IIDIRootStorage = IIDFromString("{00000012-0000-0000-C000-000000000046}");
	//public static final GUID IIDIROTData = IIDFromString("{F29F6BC0-5021-11CE-AA15-00006901293F}");
	//public static final GUID IIDIRpcChannelBuffer = IIDFromString("{D5F56B60-593B-101A-B569-08002B2DBF7A}");
	//public static final GUID IIDIRpcProxyBuffer = IIDFromString("{D5F56A34-593B-101A-B569-08002B2DBF7A}");
	//public static final GUID IIDIRpcStubBuffer = IIDFromString("{D5F56AFC-593B-101A-B569-08002B2DBF7A}");
	//public static final GUID IIDIRunnableObject = IIDFromString("{00000126-0000-0000-C000-000000000046}");
	//public static final GUID IIDIRunningObjectTable = IIDFromString("{00000010-0000-0000-C000-000000000046}");
	//public static final GUID IIDISimpleFrameSite = IIDFromString("{742B0E01-14E6-101B-914E-00AA00300CAB}");
	public static final GUID IIDISpecifyPropertyPages = IIDFromString("{B196B28B-BAB4-101A-B69C-00AA00341D07}");
	//public static final GUID IIDIStdMarshalInfo = IIDFromString("{00000018-0000-0000-C000-000000000046}");
	public static final GUID IIDIStorage = IIDFromString("{0000000B-0000-0000-C000-000000000046}");
	public static final GUID IIDIStream = IIDFromString("{0000000C-0000-0000-C000-000000000046}");
	//public static final GUID IIDISupportErrorInfo = IIDFromString("{DF0B3D60-548F-101B-8E65-08002B2BD119}");
	//public static final GUID IIDITypeComp = IIDFromString("{00020403-0000-0000-C000-000000000046}");
	//public static final GUID IIDITypeLib = IIDFromString("{00020402-0000-0000-C000-000000000046}");
	public static final GUID IIDIUnknown = IIDFromString("{00000000-0000-0000-C000-000000000046}");
	//public static final GUID IIDIViewObject = IIDFromString("{0000010D-0000-0000-C000-000000000046}");
	public static final GUID IIDIViewObject2 = IIDFromString("{00000127-0000-0000-C000-000000000046}");
	
	//public static final int ADVF_DATAONSTOP = 64;
	//public static final int ADVF_NODATA = 1;
	//public static final int ADVF_ONLYONCE = 2;
	//public static final int ADVF_PRIMEFIRST = 4;
	//public static final int ADVFCACHE_FORCEBUILTIN = 16;
	//public static final int ADVFCACHE_NOHANDLER = 8;
	//public static final int ADVFCACHE_ONSAVE = 32;
	public static final int CF_TEXT         = 1;
	public static final int CF_BITMAP       = 2;
	public static final int CF_METAFILEPICT = 3;
	public static final int CF_SYLK         = 4;
	public static final int CF_DIF          = 5;
	public static final int CF_TIFF         = 6;
	public static final int CF_OEMTEXT      = 7;
	public static final int CF_DIB          = 8;
	public static final int CF_PALETTE      = 9;
	public static final int CF_PENDATA      = 10;
	public static final int CF_RIFF         = 11;
	public static final int CF_WAVE         = 12;
	public static final int CF_UNICODETEXT  = 13;
	public static final int CF_ENHMETAFILE  = 14;
	public static final int CF_HDROP        = 15;
	public static final int CF_LOCALE       = 16;
	public static final int CF_MAX          = 17;
	public static final int CLSCTX_INPROC_HANDLER = 2;
	public static final int CLSCTX_INPROC_SERVER = 1;
	public static final int CLSCTX_LOCAL_SERVER = 4;
	public static final int CLSCTX_REMOTE_SERVER = 16;
	public static final int CO_E_CLASSSTRING = -2147221005;
	//public static final int COINIT_APARTMENTTHREADED = 2;  Apartment model
	//public static final int COINIT_DISABLE_OLE1DDE = 4;  Don't use DDE for Ole1 support.
	//public static final int COINIT_MULTITHREADED = 0;  OLE calls objects on any thread.
	//public static final int COINIT_SPEED_OVER_MEMORY = 8;  Trade memory for speed.
	public static final int DATADIR_GET = 1;
	public static final int DATADIR_SET = 2;
	public static final int DISP_E_EXCEPTION = 0x80020009;
	public static final int DISP_E_MEMBERNOTFOUND = -2147352573;
	public static final int DISP_E_UNKNOWNINTERFACE = 0x80020001;
	//public static final int DISPID_AMBIENT_APPEARANCE = -716;
	//public static final int DISPID_AMBIENT_AUTOCLIP = -715;
	public static final int DISPID_AMBIENT_BACKCOLOR = -701;
	//public static final int DISPID_AMBIENT_CHARSET = -727;
	//public static final int DISPID_AMBIENT_CODEPAGE = -725;
	//public static final int DISPID_AMBIENT_DISPLAYASDEFAULT = -713;
	//public static final int DISPID_AMBIENT_DISPLAYNAME = -702;
	public static final int DISPID_AMBIENT_FONT = -703;
	public static final int DISPID_AMBIENT_FORECOLOR = -704;
	public static final int DISPID_AMBIENT_LOCALEID = -705;
	public static final int DISPID_AMBIENT_MESSAGEREFLECT = -706;
	public static final int DISPID_AMBIENT_OFFLINEIFNOTCONNECTED = -5501;
	//public static final int DISPID_AMBIENT_PALETTE = -726;
	//public static final int DISPID_AMBIENT_RIGHTTOLEFT = -732;
	//public static final int DISPID_AMBIENT_SCALEUNITS = -707;
	public static final int DISPID_AMBIENT_SHOWGRABHANDLES = -711;
	public static final int DISPID_AMBIENT_SHOWHATCHING = -712;
	public static final int DISPID_AMBIENT_SILENT = -5502;
	public static final int DISPID_AMBIENT_SUPPORTSMNEMONICS = -714;
	//public static final int DISPID_AMBIENT_TEXTALIGN = -708;
	//public static final int DISPID_AMBIENT_TOPTOBOTTOM = -733;
	//public static final int DISPID_AMBIENT_TRANSFERPRIORITY = -728;
	public static final int DISPID_AMBIENT_UIDEAD = -710;
	public static final int DISPID_AMBIENT_USERMODE = -709;
	public static final int DISPID_BACKCOLOR = -501;
	public static final int DISPID_FONT = -512;
	public static final int DISPID_FONT_BOLD    = 3;
	public static final int DISPID_FONT_CHARSET = 8; 
	public static final int DISPID_FONT_ITALIC  = 4; 
	public static final int DISPID_FONT_NAME    = 0;
	public static final int DISPID_FONT_SIZE    = 2;
	public static final int DISPID_FONT_STRIKE  = 6;
	public static final int DISPID_FONT_UNDER   = 5;
	public static final int DISPID_FONT_WEIGHT  = 7;
	public static final int DISPID_FORECOLOR = -513;
	//public static final int DISPID_READYSTATE = -525;
	//public static final int DISPID_READYSTATECHANGE = -609;
	public static final int DRAGDROP_S_DROP   = 0x00040100; //Successful drop took place
	public static final int DRAGDROP_S_CANCEL = 0x00040101; //  Drag-drop operation canceled
	public static final int DRAGDROP_S_USEDEFAULTCURSORS = 0x00040102; // Use the default cursor
	public static final int DROPEFFECT_NONE = 0; // Drop target cannot accept the data. 
	public static final int DROPEFFECT_COPY = 1; // Drop results in a copy. The original data is untouched by 
	                                             // the drag source. 
	public static final int DROPEFFECT_MOVE = 2; // Drag source should remove the data. 
	public static final int DROPEFFECT_LINK = 4; // Drag source should create a link to the original data. 
	public static final int DROPEFFECT_SCROLL = 0x80000000; // Scrolling is about to start or is currently 
	                                                        // occurring in the target. This value is used in 
	                                                        // addition to the other values. 
	public static final int DV_E_FORMATETC = -2147221404;
	public static final int DV_E_STGMEDIUM = -2147221402;
	public static final int DV_E_TYMED = -2147221399;
	public static final int DVASPECT_CONTENT = 1;
	//public static final int DVASPECT_DOCPRINT = 8;
	//public static final int DVASPECT_ICON = 4;
	//public static final int DVASPECT_THUMBNAIL = 2;
	public static final int E_FAIL = -2147467259;  //Unspecified failure.
	public static final int E_INVALIDARG = -2147024809;
	public static final int E_NOINTERFACE = -2147467262;  //QueryInterface did not recognize the requested interface.
	public static final int E_NOTIMPL = -2147467263;  //Member function contains no implementation.
	//public static final int E_NOTLICENSED = -2147221230;
	//public static final int E_OUTOFMEMORY = -2147024882;  //Function failed to allocate necessary memory.
	//public static final int E_POINTER = -2147467261;
	public static final int GMEM_FIXED = 0;  //Global Memory Constants
	//public static final int GMEM_MOVABLE = 2;
	//public static final int GMEM_NODISCARD = 32;
	public static final int GMEM_ZEROINIT = 64;
	public static final int GUIDKIND_DEFAULT_SOURCE_DISP_IID = 1;
	public static final int IMPLTYPEFLAG_FDEFAULT = 1;
	//public static final int IMPLTYPEFLAG_FDEFAULTVTABLE = 2048;
	public static final int IMPLTYPEFLAG_FRESTRICTED = 4;
	public static final int IMPLTYPEFLAG_FSOURCE = 2;
	public static final int LOCALE_SYSTEM_DEFAULT = 1024;  //Locale Constants
	public static final int LOCALE_USER_DEFAULT = 2048;
	//public static final int MEMCTX_TASK = 1;  //dwMemContext values for COM's task memory allocation service
	//public static final int OLEACTIVATEAUTO = 3;  //Object is activated based on the object's default method of activation 
	//public static final int OLEACTIVATEDOUBLECLICK = 2;  //Object is activated when the OLE container control is double-clicked 
	//public static final int OLEACTIVATEGETFOCUS = 1;  //Object is activated when the OLE container control gets the focus 
	//public static final int OLEACTIVATEMANUAL = 0;  //OLE object isn't automatically activated 
	//public static final int OLEAUTOMATIC = 0;  //Object is updated each time the linked data changes 
	//public static final int OLECHANGED = 0;  //Object's data has changed 
	public static final int OLECLOSE_NOSAVE = 1;
	//public static final int OLECLOSE_PROMPTSAVE = 2;
	public static final int OLECLOSE_SAVEIFDIRTY = 0;	
	//public static final int OLECLOSED = 2;  //Application file containing the linked object's data has been closed 
	//public static final int OLECONTF_EMBEDDINGS = 1;  //The OLECONTF enumeration indicates the kind of objects to be enumerated by the returned IEnumUnknown interface
	//public static final int OLECONTF_LINKS = 2;
	//public static final int OLECONTF_ONLYIFRUNNING = 16;
	//public static final int OLECONTF_ONLYUSER = 8;
	//public static final int OLECONTF_OTHERS = 4;
	//public static final int OLEDEACTIVATEMANUAL = 1;  //The OLE object can only be deactivated programatically via the #doVerb: method."
	//public static final int OLEDEACTIVATEONLOSEFOCUS = 0;  //The OLE object is deactivated whenever focus is given to another widget in the receiver's shell. 
	//public static final int OLEDECBORDER = 1;  //a border is displayed around the receiver.
	//public static final int OLEDECBORDERANDNIBS = 3;  //border and resize nibs are displayed.
	//public static final int OLEDECNIBS = 2;  //resize nibs are displayed around the reciever.
	//public static final int OLEDECNONE = 0;  //no special trimmings are displayed around the receiver.
	//public static final int OLEDISPLAYCONTENT = 0;  //Object's data is displayed in the OLE container control 
	//public static final int OLEDISPLAYICON = 1;  //Object's icon is displayed in the OLE container control 
	//public static final int OLEEITHER = 2;  //OLE container control can contain either a linked or an embedded object 
	public static final int OLEEMBEDDED = 1;  //OLE container control contains an embedded object 
	//public static final int OLEFROZEN = 1;  //Object is updated whenever the user saves the linked document from within the application in which it was created 
	public static final int OLEIVERB_DISCARDUNDOSTATE = -6;  //close the OLE object and discard the undo state
	//public static final int OLEIVERB_HIDE = -3;  //hide the OLE object
	public static final int OLEIVERB_INPLACEACTIVATE = -5;  //open the OLE for editing in-place	
	//public static final int OLEIVERB_OPEN = -2;  //open the OLE object for editing in a separate window
	public static final int OLEIVERB_PRIMARY = 0;  //opens the OLE object for editing
	//public static final int OLEIVERB_PROPERTIES = -7;  //request the OLE object properties dialog
	//public static final int OLEIVERB_SHOW = -1;  //show the OLE object
	//public static final int OLEIVERB_UIACTIVATE = -4;  //activate the UI for the OLE object 
	public static final int OLELINKED = 0;  //OLE container control contains a linked object 
	//public static final int OLEMANUAL = 2;  //Object is updated only when the Action property is set to 6 (Update) 
	//public static final int OLEMISC_ACTIVATEWHENVISIBLE = 256;
	//public static final int OLEMISC_ACTSLIKEBUTTON = 4096;
	//public static final int OLEMISC_ACTSLIKELABEL = 8192;
	//public static final int OLEMISC_ALIGNABLE = 32768;
	//public static final int OLEMISC_ALWAYSRUN = 2048;
	//public static final int OLEMISC_CANLINKBYOLE1 = 32;
	//public static final int OLEMISC_CANTLINKINSIDE = 16;
	//public static final int OLEMISC_IGNOREACTIVATEWHENVISIBLE = 524288;
	//public static final int OLEMISC_IMEMODE = 262144;
	//public static final int OLEMISC_INSERTNOTREPLACE = 4;
	//public static final int OLEMISC_INSIDEOUT = 128;
	//public static final int OLEMISC_INVISIBLEATRUNTIME = 1024;
	//public static final int OLEMISC_ISLINKOBJECT = 64;
	//public static final int OLEMISC_NOUIACTIVATE = 16384;
	//public static final int OLEMISC_ONLYICONIC = 2;
	//public static final int OLEMISC_RECOMPOSEONRESIZE = 1;
	//public static final int OLEMISC_RENDERINGISDEVICEINDEPENDENT = 512;
	//public static final int OLEMISC_SETCLIENTSITEFIRST = 131072;
	//public static final int OLEMISC_SIMPLEFRAME = 65536;
	//public static final int OLEMISC_STATIC = 8;
	//public static final int OLEMISC_SUPPORTSMULTILEVELUNDO = 2097152;
	//public static final int OLEMISC_WANTSTOMENUMERGE = 1048576;
	//public static final int OLENONE = 3;  //OLE container control doesn't contain an object 
	//public static final int OLERENAMED = 3;  //Application file containing the linked object's data has been renamed 
	//public static final int OLERENDER_ASIS = 3;  //Ole Create rendering formats
	public static final int OLERENDER_DRAW = 1;
	//public static final int OLERENDER_FORMAT = 2;
	//public static final int OLERENDER_NONE = 0;
	//public static final int OLESAVED = 1;  //Object's data has been saved by the application that created the object 
	//public static final int OLESIZEAUTOSIZE = 2;  //OLE container control is automatically resized to display the entire object 
	//public static final int OLESIZECLIP = 0;  //Object's image is clipped by the OLE container control's borders 
	//public static final int OLESIZESTRETCH = 1;  //Object's image is sized to fill the OLE container control 
	//public static final int OLESIZEZOOM = 3;  //Object's image is stretched but in proportion 
	//public static final int OLEWHICHMK_CONTAINER = 1;
	//public static final int OLEWHICHMK_OBJFULL = 3;
	//public static final int OLEWHICHMK_OBJREL = 2;
	public static final int S_FALSE = 1;  //Used for functions that semantically return a Boolean FALSE result to indicate that the function succeeded.
	public static final int S_OK = 0;  //Function succeeded.
	public static final int STG_E_FILENOTFOUND = 0x80030002;
	public static final int STG_S_CONVERTED = 0x00030200;
	//public static final int STGC_CONSOLIDATE = 8;
	//public static final int STGC_DANGEROUSLYCOMMITMERELYTODISKCACHE = 4;
	public static final int STGC_DEFAULT = 0;
	//public static final int STGC_ONLYIFCURRENT = 2;
	//public static final int STGC_OVERWRITE = 1;
	public static final int STGM_CONVERT = 0x00020000;
	public static final int STGM_CREATE = 0x00001000;
	public static final int STGM_DELETEONRELEASE = 0x04000000;
	public static final int STGM_DIRECT = 0x00000000;
	public static final int STGM_DIRECT_SWMR = 0x00400000;
	public static final int STGM_FAILIFTHERE = 0x00000000;
	public static final int STGM_NOSCRATCH = 0x00100000;
	public static final int STGM_NOSNAPSHOT = 0x00200000;
	public static final int STGM_PRIORITY = 0x00040000;
	public static final int STGM_READ = 0x00000000;
	public static final int STGM_READWRITE = 0x00000002;
	public static final int STGM_SHARE_DENY_NONE = 0x00000040;
	public static final int STGM_SHARE_DENY_READ = 0x00000030;
	public static final int STGM_SHARE_DENY_WRITE = 0x00000020;
	public static final int STGM_SHARE_EXCLUSIVE = 0x00000010;
	public static final int STGM_SIMPLE = 0x08000000;
	public static final int STGM_TRANSACTED = 0x00010000;
	public static final int STGM_WRITE = 0x00000001;
	public static final int STGTY_STORAGE      = 1;
	public static final int STGTY_STREAM       = 2;
	public static final int STGTY_LOCKBYTES    = 3;
	public static final int STGTY_PROPERTY     = 4;
	//public static final int TYMED_ENHMF = 64;  //Values for tymed
	//public static final int TYMED_FILE = 2;
	//public static final int TYMED_GDI = 16;
	public static final int TYMED_HGLOBAL = 1;
	//public static final int TYMED_ISTORAGE = 8;
	//public static final int TYMED_ISTREAM = 4;
	//public static final int TYMED_MFPICT = 32;
	//public static final int TYMED_NULL = 0;

	public static final short DISPATCH_METHOD = 0x1;  //Dispatch Constants
	public static final short DISPATCH_PROPERTYGET = 0x2;
	public static final short DISPATCH_PROPERTYPUT = 0x4;
	public static final short DISPATCH_PROPERTYPUTREF = 0x8;
	//public static final short DISPID_CONSTRUCTOR = -6;
	//public static final short DISPID_DESTRUCTOR = -7;
	//public static final short DISPID_EVALUATE = -5;
	//public static final short DISPID_NEWENUM = -4;
	public static final short DISPID_PROPERTYPUT = -3;
	//public static final short DISPID_UNKNOWN = -1;
	//public static final short DISPID_VALUE = 0;
	
	// Variant types
	public static final short VT_BOOL = 11;     // Boolean; True=-1, False=0.
	public static final short VT_BSTR = 8;      // Binary String.
	public static final short VT_BYREF = 16384; // By reference - must be combined with one of the othre VT values
	public static final short VT_CY = 6;        // Currency.
	public static final short VT_DATE = 7;      // Date.
	public static final short VT_DISPATCH = 9;  // IDispatch
	public static final short VT_EMPTY = 0;     // Not specified.
	public static final short VT_ERROR = 10;    // Scodes.
	public static final short VT_I2 = 2;        // 2-byte signed int.
	public static final short VT_I4 = 3;        // 4-byte signed int.
	public static final short VT_NULL = 1;      // Null.
	public static final short VT_R4 = 4;        // 4-byte real.
	public static final short VT_R8 = 5;        // 8-byte real.
	public static final short VT_UI1 = 17;      // Unsigned char.
	public static final short VT_UI4 = 19;      // Unsigned int.
	public static final short VT_UNKNOWN = 13;  // IUnknown FAR*.
	public static final short VT_VARIANT = 12;  // VARIANT FAR*.

public static final native int CLSIDFromProgID(char[] lpszProgID, GUID pclsid);
public static final native int CLSIDFromString(char[] lpsz, GUID pclsid);
public static final native int CoCreateInstance(
	GUID rclsid,
	int pUnkOuter, 
	int dwClsContext, 
	GUID riid, 
	int[] ppv
); 

public static final native void CoFreeUnusedLibraries();
public static final native int CoGetClassObject(GUID rclsid, int dwClsContext, int pServerInfo, GUID riid, int[] ppv);
public static final native int CoLockObjectExternal(
  int pUnk,  //Pointer to object to be locked or unlocked
  boolean fLock,       //TRUE = lock, FALSE = unlock  
  boolean fLastUnlockReleases  //TRUE = release all pointers to object
);
public static final native int CoTaskMemAlloc(int cb);  //Size in bytes of memory block to be allocated
public static final native void CoTaskMemFree(int pv);
public static final native int DoDragDrop(  
	int pDataObject,  //Pointer to the data object
	int pDropSource,  //Pointer to the source
	int dwOKEffect,   //Effects allowed by the source
	int[] pdwEffect   //Pointer to effects on the source
);

public static final native void DragFinish(int hDrop);

public static final native int DragQueryFile(
	int hDrop,
	int iFile,
	byte[] lpszFile,
	int cch
);

public static final native int GetClassFile(
	char[] szFileName, //Pointer to filename for which you are requesting a CLSID
	GUID clsid         //Pointer to location for returning the CLSID
);
public static final native int GetClipboardFormatName(
	int format, 
	byte[] lpszFormatName, 
	int cchMaxCount);
public static final native int IIDFromString(char[] lpsz, GUID lpiid);
private static GUID IIDFromString(String lpsz) {
	// create a null terminated array of char
	char[] buffer = (lpsz +"\0").toCharArray();

	// invoke system method
	GUID lpiid = new GUID();
	if (COM.IIDFromString(buffer, lpiid) == COM.S_OK)
		return lpiid;
	return null;
}
public static final native boolean IsEqualGUID(GUID rguid1, GUID rguid2);
public static final native void MoveMemory(byte[] Destination, DROPFILES Source, int Length);
public static final native void MoveMemory(double[] Destination, int SourcePtr, int Length);
public static final native void MoveMemory(float[] Destination, int SourcePtr, int Length);
public static final native void MoveMemory(short[] Destination, int SourcePtr, int Length);
public static final native void MoveMemory(int DestinationPtr, double[] Source, int Length);
public static final native void MoveMemory(int DestinationPtr, float[] Source, int Length);
public static final native void MoveMemory(int DestinationPtr, short[] Source, int Length);
public static final native void MoveMemory(int Destination, FORMATETC Source, int Length);
public static final native void MoveMemory(int DestinationPtr, GUID Source, int Length);
public static final native void MoveMemory(int DestinationPtr, OLEINPLACEFRAMEINFO Source, int Length);
public static final native void MoveMemory(int Destination, STATSTG Source, int Length);
public static final native void MoveMemory(int Destination, STGMEDIUM Source, int Length);
public static final native void MoveMemory(STGMEDIUM Destination, int SourcePtr, int Length);
public static final native void MoveMemory(DISPPARAMS Destination, int SourcePtr, int Length);
public static final native void MoveMemory(FORMATETC Destination, int Source, int Length);
public static final native void MoveMemory(GUID Destination, int SourcePtr, int Length);
public static final native void MoveMemory(STATSTG Destination, int Source, int Length);
public static final native void MoveMemory(TYPEATTR Destination, int SourcePtr, int Length);
public static final native void MoveMemory(RECT Destination, int Source, int Length);
public static final native void MoveMemory(FUNCDESC1 Destination, int Source, int Length);
public static final native void MoveMemory(VARDESC1 Destination, int Source, int Length);
public static final native void MoveMemory(FUNCDESC2 Destination, int Source, int Length);
public static final native void MoveMemory(VARDESC2 Destination, int Source, int Length);

public static final native int OleCreate(GUID rclsid, GUID riid, int renderopt, FORMATETC pFormatEtc, int pClientSite, int pStg, int[] ppvObject);
public static final native int OleCreateFromFile(
	GUID rclsid,          //Reserved. Must be CLSID_NULL
	char[] lpszFileName,  //Pointer to full path of file used to create object
	GUID riid,            //Reference to the identifier of the interface to be used to 
	                      //    communicate with new object
	int renderopt,        //Value from OLERENDER
	FORMATETC pFormatEtc, //Pointer to the FORMATETC structure
	int pClientSite,      //Pointer to an interface
	int pStg,             //Pointer tothe interface to be used as object storage
	int[] ppvObj);        //Address of output variable that receives the interface pointer 
						  //    requested in riid  
public static final native int OleCreatePropertyFrame(int hwndOwner,int x, int y, char[] lpszCaption, int cObjects, int[] lplpUnk, int cPages, int lpPageClsID, int lcid, int dwReserved, int lpvReserved);
public static final native int OleDraw(int pUnk,       //Pointer to the view object to be drawn
						   int dwAspect,   //How the object is to be represented
						   int hdcDraw,    //Device context on which to draw
						   int lprcBounds);//Pointer to the rectangle in which the object is drawn
public static final native int OleFlushClipboard();
public static final native int OleGetClipboard(int[] ppDataObject);
public static final native int OleIsCurrentClipboard(int pDataObject);
public static final native boolean OleIsRunning(int pObject);
public static final native int OleLoad(
	int pStg,        //Pointer to the storage object from which to load
	GUID riid,       //Reference to the identifier of the interface
	int pClientSite, //Pointer to the client site for the object
	int[] ppvObj     //Address of output variable that receives the interface pointer requested in riid
);
public static final native int OleRun(int pUnknown);
public static final native int OleSave(int pPS, int pStg,boolean fSameAsLoad);
public static final native int OleSetClipboard(int pDataObject);
public static final native int OleSetContainedObject(int pUnk, boolean fContained);
public static final native int OleSetMenuDescriptor(int holemenu, int hwndFrame, int hwndActiveObject, int lpFrame, int lpActiveObj);
public static final native int OleTranslateColor(int clr, int hpal, int[] pcolorref);
public static final native int ProgIDFromCLSID(
	GUID clsid,  //CLSID for which the ProgID is requested
	int[] lplpszProgID //Address of output variable that receives a pointer to the requested ProgID string
);
public static final native int RegisterClipboardFormat(byte[] lpszFormat); 
public static final native int RegisterDragDrop(
	int hwnd,       //Handle to a window that can accept drops
	int pDropTarget //Pointer to object that is to be target of drop
);
public static final native  void ReleaseStgMedium(
  int pmedium  //Pointer to storage medium to be freed
); 
public static final native int RevokeDragDrop(
	int hwnd  //Handle to a window that can accept drops
);
public static final native int StgCreateDocfile(char[] pwcsName, int grfMode, int reserved, int[] ppstgOpen);
public static final native int StgIsStorageFile(
	char[] pwcsName  //Points to a path of the file to check
);
public static final native int StgOpenStorage(
	char[] pwcsName,   //Points to the path of the file containing storage object
	int pstgPriority,  //Points to a previous opening of a root storage object
	int grfMode,       //Specifies the access mode for the object
	int snbExclude,    //Points to an SNB structure specifying elements to be excluded
	int reserved,      //Reserved; must be zero
	int[] ppstgOpen    //Address of output variable that receives the IStorage interface pointer
);
public static final native int SysAllocString(char [] sz);
public static final native void SysFreeString(int bstr);
public static final native int SysStringByteLen(int bstr);
public static final native int VariantChangeType(int pvargDest, int pvarSrc, short wFlags, short vt);
public static final native int VariantClear(int pvarg);
public static final native void VariantInit(int pvarg);
public static final native int VtblCall(int fnNumber, int ppVtbl);
public static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0);
public static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, char[] arg1);
public static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, int arg1);
public static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, int arg1, int arg2, int arg3, int[] arg4);
public static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, int arg1, int arg2, int arg3, int arg4, int[] arg5);
public static final native int VtblCall(int fnNumber, int ppVtbl, int[] arg0);
public static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int[] arg1);
public static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1);
public static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, int[] arg2);
public static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, int arg2);
public static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, int arg2, int[] arg3);
public static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, DVTARGETDEVICE arg2, SIZE arg3);
public static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, GUID arg2, int arg3, int[] arg4);
public static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, FORMATETC arg1, int[] arg2);
public static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, GUID arg1);
public static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, GUID arg1, int arg2, int arg3);
public static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, GUID arg1, int arg2, int arg3, DISPPARAMS arg4, int arg5, EXCEPINFO arg6, int[] arg7);
public static final native int VtblCall(int ppVtbl,   int fnNumber, int arg0, STATSTG arg1, int[] arg2);
public static final native int VtblCall(int fnNumber, int ppVtbl, MSG arg0);
public static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, MSG arg1, int arg2, int arg3, int arg4, RECT arg5);
public static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, SIZE arg1);
public static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, boolean arg1);
public static final native int VtblCall(int fnNumber, int ppVtbl, CAUUID arg0);
public static final native int VtblCall(int ppVtbl,   int fnNumber, CONTROLINFO arg0);
public static final native int VtblCall(int fnNumber, int ppVtbl, FORMATETC arg0);
public static final native int VtblCall(int fnNumber, int ppVtbl, FORMATETC arg0, STGMEDIUM arg1);
public static final native int VtblCall(int fnNumber, int ppVtbl, FORMATETC arg0, STGMEDIUM arg1, boolean arg2);
public static final native int VtblCall(int ppVtbl,   int fnNumber, GUID arg0);
public static final native int VtblCall(int ppVtbl,   int fnNumber, GUID arg0, int[] arg1);
public static final native int VtblCall(int fnNumber, int ppVtbl, GUID arg0, int arg1, int arg2, int arg3, int[] arg4);
public static final native int VtblCall(int fnNumber, int ppVtbl, GUID arg0, int arg1, int arg2, int arg3, int arg4);
public static final native int VtblCall(int fnNumber, int ppVtbl, GUID arg0, int arg1, OLECMD arg2, OLECMDTEXT arg3);
public static final native int VtblCall(int fnNumber, int ppVtbl, LICINFO arg0);
public static final native int VtblCall(int fnNumber, int ppVtbl, RECT arg0, int arg1, boolean arg2);
public static final native int VtblCall(int fnNumber, int ppVtbl, RECT arg0, RECT arg1);
public static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int[] arg1, int[] arg2, int[] arg3, int[] arg4);
public static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int[] arg1, int arg2, int[] arg3);

public static final native int WriteClassStg(int pStg, GUID rclsid);
}
