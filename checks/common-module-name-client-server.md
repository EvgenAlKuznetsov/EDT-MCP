# common-module-name-client-server

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `common-module-name-client-server` |
| **Title** | Client-server common module should end with ClientServer suffix |
| **Description** | Check client-server common module name has "ClientServer" suffix |
| **Severity** | `CRITICAL` |
| **Type** | `WARNING` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |
| **1C Standard** | [469](https://its.1c.ru/db/v8std/content/469/hdoc) |

---

## 🎯 What This Check Does

This check validates that common modules configured to run on **both client AND server** have the appropriate naming suffix:
- Russian: `КлиентСервер`
- English: `ClientServer`

### Why This Is Important

- **Universal code visibility**: Module runs on both client and server
- **Shared utilities**: Contains code that works in any context
- **Standards compliance**: Follows 1C naming conventions (Standard 469)
- **Developer awareness**: Indicates code must work without context-specific features

---

## ❌ Error Example

### Error Message

```
Client-server common module should end with "{suffix}" suffix
```

**Russian:**
```
Client-server common module should end with "{suffix}" suffix
```

### Noncompliant XML Configuration

```xml
<!-- ❌ Noncompliant: Client-server module without ClientServer suffix -->
<mdclass:CommonModule xmlns:mdclass="http://g5.1c.ru/v8/dt/metadata/mdclass">
  <name>StringUtilities</name>                      <!-- ❌ Missing "ClientServer" suffix -->
  <server>true</server>
  <clientManagedApplication>true</clientManagedApplication>  <!-- Both client and server enabled -->
</mdclass:CommonModule>

<mdclass:CommonModule xmlns:mdclass="http://g5.1c.ru/v8/dt/metadata/mdclass">
  <name>CommonFunctions</name>                      <!-- ❌ Missing "КлиентСервер" suffix -->
  <server>true</server>
  <clientManagedApplication>true</clientManagedApplication>
</mdclass:CommonModule>
```

### Noncompliant Code Example

```
Configuration/
└── CommonModules/
    └── StringUtilities/  ❌ Missing "ClientServer" suffix
        └── Module.bsl
```

**Module Properties:**
- Client (managed application): ✓
- Server: ✓

---

## ✅ Compliant Solution

### Correct XML Configuration

```xml
<!-- ✅ Correct: Client-server module with ClientServer suffix -->
<mdclass:CommonModule xmlns:mdclass="http://g5.1c.ru/v8/dt/metadata/mdclass">
  <name>StringUtilitiesClientServer</name>         <!-- ✅ Has "ClientServer" suffix -->
  <server>true</server>
  <clientManagedApplication>true</clientManagedApplication>
</mdclass:CommonModule>

<!-- ✅ Correct: Russian naming with КлиентСервер suffix -->
<mdclass:CommonModule xmlns:mdclass="http://g5.1c.ru/v8/dt/metadata/mdclass">
  <name>СтроковыеФункцииКлиентСервер</name>        <!-- ✅ Has "КлиентСервер" suffix -->
  <server>true</server>
  <clientManagedApplication>true</clientManagedApplication>
</mdclass:CommonModule>
```

### Correct Module Naming

```
Configuration/
└── CommonModules/
    └── StringUtilitiesClientServer/  ✅ Has "ClientServer" suffix
        └── Module.bsl
```

Or in Russian:

```
Configuration/
└── CommonModules/
    └── СтроковыеФункцииКлиентСервер/  ✅ Has "КлиентСервер" suffix
        └── Module.bsl
```

### Module Settings

| Property | Value |
|----------|-------|
| Name | `StringUtilitiesClientServer` |
| Client (managed application) | ✓ |
| Server | ✓ |
| Server call | ✗ |
| External connection | Optional |

---

## 📖 ClientServer Module Characteristics

### What Makes a ClientServer Module

A module is considered "ClientServer" when:
- `Client (managed application)` = True
- `Server` = True
- `Server call` = False (not a server call module)

### Suffix Options

| Language | Suffix | Example |
|----------|--------|---------|
| English | `ClientServer` | `CommonUtilitiesClientServer` |
| Russian | `КлиентСервер` | `ОбщиеФункцииКлиентСервер` |

---

## 📋 Typical ClientServer Module Content

### What Belongs in ClientServer Modules

```bsl
// ✅ String manipulation - works everywhere
Function TrimText(Text) Export
    Return TrimAll(Text);
EndFunction

// ✅ Math calculations - works everywhere
Function RoundAmount(Amount, Precision = 2) Export
    Return Round(Amount, Precision);
EndFunction

// ✅ Date formatting - works everywhere
Function FormatDateShort(Date) Export
    Return Format(Date, "DLF=D");
EndFunction

// ✅ Type checking - works everywhere
Function IsBlankString(Value) Export
    Return Not ValueIsFilled(Value);
EndFunction
```

### What Does NOT Belong

```bsl
// ❌ Server-only: database access
Function GetDocumentData(Ref) Export
    Return Ref.GetObject();  // Doesn't work on client!
EndFunction

// ❌ Client-only: UI operations
Procedure ShowNotification(Text) Export
    ShowUserNotification(Text);  // Doesn't work on server!
EndProcedure
```

---

## 🔧 How to Fix

### Step 1: Verify module properties

Check that module has:
- Client = True
- Server = True
- Server call = False

### Step 2: Rename the module

Add the `ClientServer` suffix:

**Before:** `StringUtilities`  
**After:** `StringUtilitiesClientServer`

### Step 3: Update all references

```bsl
// Before
Result = StringUtilities.TrimText(Value);

// After
Result = StringUtilitiesClientServer.TrimText(Value);
```

---

## 🔍 Technical Details

### Check Implementation Class

```
com.e1c.v8codestyle.md.commonmodule.check.CommonModuleNameClientServer
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.md/src/com/e1c/v8codestyle/md/commonmodule/check/
```

---

## 📚 References

- [1C:Enterprise Development Standards - Standard 469](https://its.1c.ru/db/v8std/content/469/hdoc)
- [Common Module Types](https://1c-dn.com/library/common_modules/)
