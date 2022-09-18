/*******************************************************************************
 * Copyright (c) 2022 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/

#import <Cocoa/Cocoa.h>
#import <Carbon/Carbon.h>
#include <string>

@interface AppDelegate : NSObject<NSApplicationDelegate>
{
}
@end

@implementation AppDelegate : NSObject
-(BOOL) applicationShouldTerminateAfterLastWindowClosed:(NSApplication*)app
{
    return YES;
}
@end

//-------------
// Snippet's additional code
//-------------
void describeFlag(std::string* a_result, int* a_flags, int a_flag, const char* a_name)
{
	if (*a_flags & a_flag)
	{
		*a_result += a_name;
		*a_flags &= ~a_flag;
	}
}

std::string composeModsString(int mods) {
	std::string result;

	mods &= NSEventModifierFlagDeviceIndependentFlagsMask;
	describeFlag(&result, &mods, NSEventModifierFlagCapsLock,   "Caps+");
	describeFlag(&result, &mods, NSEventModifierFlagShift,      "Shift+");
	describeFlag(&result, &mods, NSEventModifierFlagControl,    "Ctrl+");
	describeFlag(&result, &mods, NSEventModifierFlagOption,     "Alt+");
	describeFlag(&result, &mods, NSEventModifierFlagCommand,    "Win+");     // Cmd = Win
	describeFlag(&result, &mods, NSEventModifierFlagNumericPad, "Num+");
	describeFlag(&result, &mods, NSEventModifierFlagHelp,       "Help+");
	describeFlag(&result, &mods, NSEventModifierFlagFunction,   "Func+");

	if (0 != mods) {
		char buffer[64];
		sprintf(buffer, "0x%02X+", mods);
		result += buffer;
	}

	return result;
}

// Note: takes ownership and releases a_inputSource
NSString* calcCharsNoMods(NSEvent* a_event, TISInputSourceRef a_inputSource)
{
	CFDataRef keyLayoutData = (CFDataRef)TISGetInputSourceProperty(a_inputSource, kTISPropertyUnicodeKeyLayoutData);
	CFRelease(a_inputSource);
	if (!keyLayoutData)
		return @"<error>";

	const UCKeyboardLayout* keyboardLayout = (const UCKeyboardLayout*)CFDataGetBytePtr(keyLayoutData);
	if (!keyboardLayout)
		return @"<error>";

	int keyboardType = CGEventGetIntegerValueField([a_event CGEvent], kCGKeyboardEventKeyboardType);
	unsigned short keyCode = [a_event keyCode];
	int keyAction = (NSEventTypeKeyDown == [a_event type]) ? kUCKeyActionDown : kUCKeyActionUp;

	UniChar unicodeChars[256];
	UniCharCount numChars = 0;
	UInt32 deadKeyState = 0;
	UCKeyTranslate(
		keyboardLayout,
		keyCode,
		keyAction,
		0,                  // modifierKeyState
		keyboardType,
		0,                  // keyTranslateOptions
		&deadKeyState,
		sizeof(unicodeChars) / sizeof(unicodeChars[0]),
		&numChars,
		unicodeChars
	);

	return [NSString stringWithCharacters:unicodeChars length:numChars];
}

NSString* getEventKeyUnicodes(NSEvent* a_event)
{
	UniChar unicodeChars[256];
	ByteCount outSize = 0;
	GetEventParameter((EventRef)[a_event eventRef], kEventParamKeyUnicodes, typeUnicodeText, nil, sizeof(unicodeChars), &outSize, unicodeChars);
	UniCharCount numChars = outSize / sizeof(UniChar);
	return [NSString stringWithCharacters:unicodeChars length:numChars];
}

// This function is not documented in headers, but is exported in Carbon libraries.
extern "C" TISInputSourceRef TSMDefaultAsciiCapableKeyboardLayoutCopy(void);

void reportEvent(const char* a_caption, NSEvent* a_event)
{
	int keyCode = [a_event keyCode];
	int mods    = [a_event modifierFlags];
	NSString* charsNormal1 = [a_event characters];
	NSString* charsNormal2 = getEventKeyUnicodes(a_event);
	// This approach is recommended by Apple.
	// However, it has the following problems:
	// [1] Does not remove Shift modifier.
	//     So Shift+1 returns '!' instead of desired '1'.
	// [2] Does not transform non-latin layouts into latin.
	//     So in for example Russian, 'A' returns 'ф' instead of desired 'a'.
	// [3] Does not transform digits keys in layouts where they are repurposed.
	//     So in for example Czech, '2' returns 'ě' instead of desired '2'.
	NSString* charsNoMods1 = [a_event charactersIgnoringModifiers];
	// Solves [1], but is only available on macOS 10.15+
	NSString* charsNoMods2 = [a_event charactersByApplyingModifiers:0];
	// Implements 'charactersByApplyingModifiers:' for earlier macOS
	NSString* charsNoMods3 = calcCharsNoMods(a_event, TISCopyCurrentKeyboardInputSource());
	// Solves [1][2]
	NSString* charsNoMods4 = calcCharsNoMods(a_event, TISCopyCurrentASCIICapableKeyboardInputSource());
	// Solves [1][2][3], but uses default keyboard layout instead of current layout.
	// This is a problem when current layout is also latin, such as Dvorak.
	NSString* charsNoMods5 = calcCharsNoMods(a_event, TSMDefaultAsciiCapableKeyboardLayoutCopy());

	printf(
		"%s keyCode=%02X chars={1:%04X '%s' 2:%04X '%s'} charsNoMods={1:%04X '%s' 2:%04X '%s' 3:%04X '%s' 4:%04X '%s' 5:%04X '%s'} mods=%08X (%s)\n",
		a_caption,
		keyCode,
		[charsNormal1 characterAtIndex:0], [charsNormal1 UTF8String],
		[charsNormal2 characterAtIndex:0], [charsNormal2 UTF8String],
		[charsNoMods1 characterAtIndex:0], [charsNoMods1 UTF8String],
		[charsNoMods2 characterAtIndex:0], [charsNoMods2 UTF8String],
		[charsNoMods3 characterAtIndex:0], [charsNoMods3 UTF8String],
		[charsNoMods4 characterAtIndex:0], [charsNoMods4 UTF8String],
		[charsNoMods5 characterAtIndex:0], [charsNoMods5 UTF8String],
		mods, composeModsString(mods).c_str()
	);
}

//-------------
// Snippet's main window
//-------------
@interface MainWindow : NSWindow
{
}
@end

@implementation MainWindow
- (id)initWithContentRect:(NSRect)a_contentRect styleMask:(NSUInteger)a_styleMask backing:(NSBackingStoreType)a_backing defer:(BOOL)a_defer
{
	self = [super initWithContentRect:a_contentRect styleMask:a_styleMask backing:a_backing defer:a_defer];

	NSView* contentView = [self contentView];
	[contentView setAutoresizesSubviews:YES];

	NSStackView* stackView = [NSStackView new];
	[contentView addSubview:stackView];
	[stackView setTranslatesAutoresizingMaskIntoConstraints:NO];
	[stackView setOrientation:NSUserInterfaceLayoutOrientationVertical];
	[[stackView.heightAnchor constraintEqualToAnchor:contentView.heightAnchor] setActive:YES];
	[[stackView.widthAnchor constraintEqualToAnchor:contentView.widthAnchor] setActive:YES];

	NSTextField* txtHint = [[NSTextField alloc] init];
	[stackView addArrangedSubview:txtHint];
	[txtHint setEditable:NO];
	[txtHint setBordered:NO];
    [txtHint setStringValue:
       @"1) This is a snippet made for debugging purposes macOS\n"
        "2) Press keys\n"
        "3) Investigate output in the console"
    ];

	return self;
}

- (void)keyDown:(NSEvent*)a_event
{
	reportEvent("keyDown: ", a_event);
	[super keyDown:a_event];
}

- (void)keyUp:(NSEvent*)a_event
{
	reportEvent("keyUp:   ", a_event);
	[super keyUp:a_event];
}
@end

// Boilerplate
// -----------
int main ()
{
	NSApplication* app = [NSApplication sharedApplication];
	// Application should close when last window is closed
	AppDelegate* appDelegate = [[AppDelegate alloc] init];
	[app setDelegate:appDelegate];
	// Programs without application bundles and Info.plist files don't get a menubar and can't be brought to the front without this
	[app setActivationPolicy:NSApplicationActivationPolicyRegular];

	NSRect windowRect = NSMakeRect(0, 0, 100, 100);
	id window = [[MainWindow alloc]
		initWithContentRect: windowRect
		styleMask: NSWindowStyleMaskTitled | NSWindowStyleMaskClosable | NSWindowStyleMaskResizable | NSWindowStyleMaskMiniaturizable
		backing: NSBackingStoreBuffered
		defer:NO
		];
	[window setTitle:[[NSProcessInfo processInfo] processName]];
	[window makeKeyAndOrderFront:nil];

	[app run];
	return 0;
}
