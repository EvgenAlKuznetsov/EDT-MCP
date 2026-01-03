# deprecated-procedure-outside-deprecated-region

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `deprecated-procedure-outside-deprecated-region` |
| **Title** | Deprecated procedure (function) is outside deprecated region |
| **Description** | Deprecated procedure (function) should be placed in the Deprecated region of the Public region |
| **Severity** | `MINOR` |
| **Type** | `CODE_STYLE` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |
| **1C Standard** | [644](https://its.1c.ru/db/v8std/content/644/hdoc) |

---

## 🎯 What This Check Does

This check validates that **deprecated methods** marked with the `&Deprecated` annotation are placed in the **Deprecated region** within the **Public region** of a common module.

### Why This Is Important

- **Code organization**: Deprecated methods are grouped together
- **Visibility**: Developers easily find what's deprecated
- **Maintenance**: Clear plan for future removal
- **Standards compliance**: Follows Standard 644

---

## ❌ Error Example

### Error Message

```
The deprecated procedure (function) "{name}" should be placed in the Deprecated region of the Public region in a common module area
```

**Russian:**
```
Устаревшая процедура (функция) "{name}" должна быть размещена в области Устаревшие в области ПрограммныйИнтерфейс общего модуля
```

### Noncompliant Code Example

```bsl
#Region Public

// ❌ Deprecated method outside Deprecated region
&Deprecated("Use GetClientDataV2 instead")
Function GetClientData(ClientRef) Export
    Return ClientRef.GetObject();
EndFunction

// Current methods
Function GetClientDataV2(ClientRef) Export
    // New implementation
EndFunction

#EndRegion
```

---

## ✅ Compliant Solution

### Correct Code

```bsl
#Region Public

// Current methods
Function GetClientDataV2(ClientRef) Export
    // New implementation
EndFunction

#Region Deprecated

// ✅ Deprecated method in Deprecated region
&Deprecated("Use GetClientDataV2 instead")
Function GetClientData(ClientRef) Export
    Return ClientRef.GetObject();
EndFunction

#EndRegion

#EndRegion
```

---

## 📖 Module Structure for Deprecated Methods

### Standard Region Hierarchy

```bsl
#Region Public

    // Active public methods
    Function ActiveMethod1() Export
    EndFunction
    
    Function ActiveMethod2() Export
    EndFunction
    
    #Region Deprecated
    
        // All deprecated public methods go here
        &Deprecated("Use ActiveMethod1")
        Function OldMethod1() Export
        EndFunction
        
        &Deprecated("Use ActiveMethod2")
        Function OldMethod2() Export
        EndFunction
    
    #EndRegion

#EndRegion

#Region Internal

    // Internal methods

#EndRegion

#Region Private

    // Private methods

#EndRegion
```

---

## 📋 Deprecation Annotation Syntax

### Standard Format

```bsl
&Deprecated("Reason and alternative")
Function OldFunctionName(Parameters) Export
    // Implementation
EndFunction
```

### Examples

```bsl
// Simple deprecation
&Deprecated("Use NewFunction instead")
Function OldFunction() Export
EndFunction

// Deprecation with version info
&Deprecated("Deprecated since v2.0. Use NewMethod instead")
Procedure OldProcedure() Export
EndProcedure

// Deprecation with removal plan
&Deprecated("Will be removed in v3.0. Migrate to ModernAPI.DoSomething()")
Function LegacyFunction() Export
EndFunction
```

---

## 🔧 How to Fix

### Step 1: Identify deprecated methods

Find all methods with `&Deprecated` annotation:

```bsl
&Deprecated("...")
Function SomeMethod() Export
```

### Step 2: Create Deprecated region if missing

Inside the `#Region Public`, add:

```bsl
#Region Public

    // ... active methods ...
    
    #Region Deprecated
    
    #EndRegion

#EndRegion
```

### Step 3: Move deprecated methods

Move all deprecated methods into the Deprecated region:

```bsl
#Region Deprecated

&Deprecated("Use NewMethod instead")
Function OldMethod() Export
    // ...
EndFunction

#EndRegion
```

---

## 📋 Applies To

This check applies to:

| Module Type | Checked |
|-------------|---------|
| Common Module | ✓ Yes |
| Manager Module | ✓ Yes |
| Object Module | ✓ Yes |
| RecordSet Module | ✓ Yes |
| Value Manager Module | ✓ Yes |
| Form Module | ✗ No |
| Command Module | ✗ No |

---

## 🔍 Technical Details

### What Is Checked

1. Finds methods with `&Deprecated` annotation
2. Checks if method is `Export`
3. Verifies method is inside `#Region Deprecated`
4. Verifies Deprecated region is inside `#Region Public`

### Check Implementation Class

```
com.e1c.v8codestyle.bsl.check.DeprecatedProcedureOutsideDeprecatedRegionCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.bsl/src/com/e1c/v8codestyle/bsl/check/
```

---

## 📚 References

- [1C:Enterprise Development Standards - Standard 644](https://its.1c.ru/db/v8std/content/644/hdoc)
- [Module Structure Best Practices](https://1c-dn.com/library/module_structure/)
