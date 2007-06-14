package org.eclipse.swt.internal.ole.win32;

import org.eclipse.swt.internal.win32.OS;

public class ObjIAccessible extends COMObject {

	public ObjIAccessible(int[] argCounts) {
		super(new int[] {2,0,0,1,3,5,8,1,1,2,2,2,2,2,2,2,3,2,1,1,2,2,5,3,3,1,2,2});
		//int /*long*/ ppVtable = objIAccessible.ppVtable;
		int /*long*/[] pVtable = new int /*long*/[1];
		COM.MoveMemory(pVtable, ppVtable, OS.PTR_SIZEOF);
		int /*long*/[] funcs = new int /*long*/[28];
		COM.MoveMemory(funcs, pVtable[0], OS.PTR_SIZEOF * funcs.length);
		funcs[9] = COM.get_accChild_CALLBACK(funcs[9]);
		funcs[10] = COM.get_accName_CALLBACK(funcs[10]);
		funcs[11] = COM.get_accValue_CALLBACK(funcs[11]);
		funcs[12] = COM.get_accDescription_CALLBACK(funcs[12]);
		funcs[13] = COM.get_accRole_CALLBACK(funcs[13]);
		funcs[14] = COM.get_accState_CALLBACK(funcs[14]);
		funcs[15] = COM.get_accHelp_CALLBACK(funcs[15]);
		funcs[16] = COM.get_accHelpTopic_CALLBACK(funcs[16]);
		funcs[17] = COM.get_accKeyboardShortcut_CALLBACK(funcs[17]);
		funcs[20] = COM.get_accDefaultAction_CALLBACK(funcs[20]);
		funcs[21] = COM.accSelect_CALLBACK(funcs[21]);
		funcs[22] = COM.accLocation_CALLBACK(funcs[22]);
		funcs[23] = COM.accNavigate_CALLBACK(funcs[23]);
		funcs[25] = COM.accDoDefaultAction_CALLBACK(funcs[25]);
		funcs[26] = COM.put_accName_CALLBACK(funcs[26]);
		funcs[27] = COM.put_accValue_CALLBACK(funcs[27]);
		COM.MoveMemory(pVtable[0], funcs, OS.PTR_SIZEOF * funcs.length);
	}

int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
int /*long*/ method1(int /*long*/[] args) {return AddRef();}
int /*long*/ method2(int /*long*/[] args) {return Release();}
// method3 GetTypeInfoCount - not implemented
// method4 GetTypeInfo - not implemented
// method5 GetIDsOfNames - not implemented
// method6 Invoke - not implemented
int /*long*/ method7(int /*long*/[] args) {return get_accParent(args[0]);}
int /*long*/ method8(int /*long*/[] args) {return get_accChildCount(args[0]);}
int /*long*/ method9(int /*long*/[] args) {return get_accChild(args[0], args[1]);}
int /*long*/ method10(int /*long*/[] args) {return get_accName(args[0], args[1]);}
int /*long*/ method11(int /*long*/[] args) {return get_accValue(args[0], args[1]);}
int /*long*/ method12(int /*long*/[] args) {return get_accDescription(args[0], args[1]);}
int /*long*/ method13(int /*long*/[] args) {return get_accRole(args[0], args[1]);}
int /*long*/ method14(int /*long*/[] args) {return get_accState(args[0], args[1]);}
int /*long*/ method15(int /*long*/[] args) {return get_accHelp(args[0], args[1]);}
int /*long*/ method16(int /*long*/[] args) {return get_accHelpTopic(args[0], args[1], args[2]);}
int /*long*/ method17(int /*long*/[] args) {return get_accKeyboardShortcut(args[0], args[1]);}
int /*long*/ method18(int /*long*/[] args) {return get_accFocus(args[0]);}
int /*long*/ method19(int /*long*/[] args) {return get_accSelection(args[0]);}
int /*long*/ method20(int /*long*/[] args) {return get_accDefaultAction(args[0], args[1]);}
int /*long*/ method21(int /*long*/[] args) {return accSelect((int)/*64*/args[0], args[1]);}
int /*long*/ method22(int /*long*/[] args) {return accLocation(args[0], args[1], args[2], args[3], args[4]);}
int /*long*/ method23(int /*long*/[] args) {return accNavigate((int)/*64*/args[0], args[1], args[2]);}
int /*long*/ method24(int /*long*/[] args) {return accHitTest((int)/*64*/args[0], (int)/*64*/args[1], args[2]);}
int /*long*/ method25(int /*long*/[] args) {return accDoDefaultAction(args[0]);}
int /*long*/ method26(int /*long*/[] args) {return put_accName(args[0], args[1]);}
int /*long*/ method27(int /*long*/[] args) {return put_accValue(args[0], args[1]);}
}
