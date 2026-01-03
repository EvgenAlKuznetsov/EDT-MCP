# common-module-name-global

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `common-module-name-global` |
| **Title** | Global server common module should end with Global suffix |
| **Description** | Check global server common module name has "Global" suffix |
| **Severity** | `CRITICAL` |
| **Type** | `WARNING` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |
| **1C Standard** | [469](https://its.1c.ru/db/v8std/content/469/hdoc) |

---

## 🎯 What This Check Does

This check validates that common modules with **Global** attribute enabled (server context) have the appropriate naming suffix:
- Russian: `Глобальный`
- English: `Global`

### Why This Is Important

- **Namespace clarity**: Global modules add methods to global namespace
- **Name collision prevention**: Developers aware of global scope
- **Standards compliance**: Follows 1C naming conventions (Standard 469)
- **Code organization**: Clear distinction from regular modules

---

## ❌ Error Example

### Error Message

```
Global server common module name should end with "{suffix}" suffix
```

**Russian:**
```
Global server common module should end with "{suffix}" suffix
```

### Noncompliant XML Configuration

```xml
<!-- ❌ Noncompliant: Global module without Global suffix -->
<mdclass:CommonModule xmlns:mdclass="http://g5.1c.ru/v8/dt/metadata/mdclass">
  <name>ServerUtilities</name>                      <!-- ❌ Missing "Global" suffix -->
  <server>true</server>
  <global>true</global>                             <!-- Global module enabled -->
  <clientManagedApplication>false</clientManagedApplication>
</mdclass:CommonModule>

<mdclass:CommonModule xmlns:mdclass="http://g5.1c.ru/v8/dt/metadata/mdclass">
  <name>CommonFunctions</name>                      <!-- ❌ Missing "Глобальный" suffix -->
  <server>true</server>
  <global>true</global>
</mdclass:CommonModule>
```

### Noncompliant Code Example

```
Configuration/
└── CommonModules/
    └── ServerUtilities/  ❌ Missing "Global" suffix
        └── Module.bsl
```

**Module Properties:**
- Global: ✓
- Server: ✓
- Client: ✗

---

## ✅ Compliant Solution

### Correct XML Configuration

```xml
<!-- ✅ Correct: Global module with Global suffix -->
<mdclass:CommonModule xmlns:mdclass="http://g5.1c.ru/v8/dt/metadata/mdclass">
  <name>ServerUtilitiesGlobal</name>               <!-- ✅ Has "Global" suffix -->
  <server>true</server>
  <global>true</global>
  <clientManagedApplication>false</clientManagedApplication>
</mdclass:CommonModule>

<!-- ✅ Correct: Russian naming with Глобальный suffix -->
<mdclass:CommonModule xmlns:mdclass="http://g5.1c.ru/v8/dt/metadata/mdclass">
  <name>СерверныеФункцииГлобальный</name>          <!-- ✅ Has "Глобальный" suffix -->
  <server>true</server>
  <global>true</global>
</mdclass:CommonModule>
```

### Correct Module Naming

```
Configuration/
└── CommonModules/
    └── ServerUtilitiesGlobal/  ✅ Has "Global" suffix
        └── Module.bsl
```

Or in Russian:

```
Configuration/
└── CommonModules/
    └── СерверныеФункцииГлобальный/  ✅ Has "Глобальный" suffix
        └── Module.bsl
```

### Module Settings

| Property | Value |
|----------|-------|
| Name | `ServerUtilitiesGlobal` |
| Global | ✓ |
| Server | ✓ |
| Client (managed application) | ✗ |

---

## 📖 Global Module Characteristics

### What Makes a Global Module

A module is considered "Global" when:
- `Global` = True
- `Server` = True
- `Client` = False (this check is for server-only global modules)

### Effects of Global Attribute

```bsl
// In a Global module:
Procedure DoSomething() Export
    // ...
EndProcedure

// Can be called WITHOUT module name prefix:
DoSomething();  // Works! Method is in global namespace

// Can also be called with prefix:
ServerUtilitiesGlobal.DoSomething();  // Also works
```

### Suffix Options

| Language | Suffix | Example |
|----------|--------|---------|
| English | `Global` | `ServerHelpersGlobal` |
| Russian | `Глобальный` | `СерверныеПомощникиГлобальный` |

---

## ⚠️ Global Module Considerations

### Advantages

- **Convenience**: No need to type module name prefix
- **Legacy compatibility**: Works like 1C v7.7 global modules

### Disadvantages

- **Name collisions**: Method names must be unique globally
- **Code readability**: Not clear which module provides the method
- **Maintenance**: Harder to track dependencies

### Best Practices

```bsl
// ✅ Recommended: Explicit module reference (even for global modules)
Result = ServerUtilitiesGlobal.ProcessData(Value);

// ⚠️ Works but less clear: Using global namespace
Result = ProcessData(Value);  // Which module is this from?
```

---

## 🔧 How to Fix

### Step 1: Verify module properties

Check that module has:
- Global = True
- Server = True
- Client = False

### Step 2: Rename the module

Add the `Global` suffix:

**Before:** `ServerUtilities`  
**After:** `ServerUtilitiesGlobal`

### Step 3: Update all references

```bsl
// Before (if used with prefix)
ServerUtilities.ProcessData(Value);

// After
ServerUtilitiesGlobal.ProcessData(Value);
```

---

## 🔍 Technical Details

### Check Implementation Class

```
com.e1c.v8codestyle.md.commonmodule.check.CommonModuleNameGlobal
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.md/src/com/e1c/v8codestyle/md/commonmodule/check/
```

---

## 📚 References

- [1C:Enterprise Development Standards - Standard 469](https://its.1c.ru/db/v8std/content/469/hdoc)
- [Global Common Modules](https://1c-dn.com/library/common_modules/)
