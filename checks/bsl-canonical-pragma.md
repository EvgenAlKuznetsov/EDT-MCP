# bsl-canonical-pragma

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `bsl-canonical-pragma` |
| **Title** | Pragma is written canonically |
| **Description** | Check pragma is written canonically |
| **Severity** | `BLOCKER` |
| **Type** | `ERROR` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check verifies that **method annotations (pragmas)** are written with the **correct canonical spelling**, including proper letter case. 

In BSL (Built-in Scripting Language), annotations/pragmas must be written exactly as specified in the platform documentation. Case-sensitive spelling matters for code consistency and readability.

### Why This Is Important

- **Code consistency**: Canonical spelling ensures uniform code style across the project
- **Readability**: Properly spelled annotations are easier to read and understand
- **Professionalism**: Consistent naming follows 1C development standards
- **IDE support**: Some tools may rely on canonical spelling for proper syntax highlighting and code analysis

---

## ❌ Error Example

### Error Message

```
Annotation {ActualSpelling} is not written canonically, correct spelling is {CorrectSpelling}
```

**Example:**
```
Annotation ATCLIENT is not written canonically, correct spelling is AtClient
```

**Russian:**
```
Аннотация {ActualSpelling} написана не канонически, правильное написание {CorrectSpelling}
```

### Noncompliant Code Examples

```bsl
// ❌ Wrong case - all uppercase
&ATCLIENT
Procedure MyProcedure()
EndProcedure

// ❌ Wrong case - all lowercase
&atserver
Function MyFunction()
    Return 1;
EndFunction

// ❌ Wrong case - mixed incorrect case
&Atclient
Procedure AnotherProcedure()
EndProcedure

// ❌ Wrong case for extension annotations
&before
Procedure Ext_BeforeWrite(Cancel)
EndProcedure

// ❌ Wrong case
&AROUND
Function Ext_Calculate() 
    Return ProceedWithCall();
EndFunction
```

---

## ✅ Compliant Solution

### Correct Canonical Spelling

```bsl
// ✅ Correct: AtClient
&AtClient
Procedure MyProcedure()
EndProcedure

// ✅ Correct: AtServer
&AtServer
Function MyFunction()
    Return 1;
EndFunction

// ✅ Correct: AtServerNoContext
&AtServerNoContext
Function GetData()
    Return "Data";
EndFunction

// ✅ Correct: AtClientAtServerNoContext
&AtClientAtServerNoContext
Function Calculate(Value)
    Return Value * 2;
EndFunction

// ✅ Correct: AtClientAtServer
&AtClientAtServer
Function CommonFunction()
    Return True;
EndFunction
```

### Extension Module Annotations

```bsl
// ✅ Correct: Before
&Before
Procedure Ext_BeforeWrite(Cancel)
    // Pre-processing logic
EndProcedure

// ✅ Correct: After
&After
Procedure Ext_AfterWrite(Cancel)
    // Post-processing logic
EndProcedure

// ✅ Correct: Around
&Around
Function Ext_OnCalculate()
    // Custom logic before
    Result = ProceedWithCall();
    // Custom logic after
    Return Result;
EndFunction

// ✅ Correct: ChangeAndValidate
&ChangeAndValidate
Function Ext_OnValidate()
    Result = ProceedWithCall();
    // Validation logic
    Return Result;
EndFunction
```

---

## 📖 Canonical Pragma Names Reference

### Standard Module Annotations

| Canonical Name | Description |
|----------------|-------------|
| `&AtClient` | Method executes on client side |
| `&AtServer` | Method executes on server side |
| `&AtServerNoContext` | Server method without form context |
| `&AtClientAtServer` | Method available on both client and server |
| `&AtClientAtServerNoContext` | Client/server method without context |

### Extension Module Annotations

| Canonical Name | Description |
|----------------|-------------|
| `&Before` | Execute before the original method |
| `&After` | Execute after the original method |
| `&Around` | Replace the original method (use `ProceedWithCall()`) |
| `&ChangeAndValidate` | Modify and validate (use when no `ProceedWithCall()`) |

### Russian Equivalents

| English | Russian |
|---------|---------|
| `&AtClient` | `&НаКлиенте` |
| `&AtServer` | `&НаСервере` |
| `&AtServerNoContext` | `&НаСервереБезКонтекста` |
| `&AtClientAtServer` | `&НаКлиентеНаСервере` |
| `&AtClientAtServerNoContext` | `&НаКлиентеНаСервереБезКонтекста` |
| `&Before` | `&Перед` |
| `&After` | `&После` |
| `&Around` | `&Вместо` |
| `&ChangeAndValidate` | `&ИзменениеИКонтроль` |

---

## 🔧 How to Fix

### Step 1: Identify the incorrect annotation

Look at the error message to see:
- The **current spelling** you used
- The **correct canonical spelling**

### Step 2: Replace with canonical spelling

Simply replace the annotation with the correct case:

**Before:**
```bsl
&atclient
Procedure Test()
EndProcedure
```

**After:**
```bsl
&AtClient
Procedure Test()
EndProcedure
```

### Step 3: Use IDE quick-fix (if available)

Most 1C:EDT IDEs provide quick-fix suggestions:
1. Place cursor on the error
2. Press `Ctrl+1` or use the lightbulb icon
3. Select "Fix annotation spelling" or similar option

---

## 📁 File Structure

This check applies to:

| File Type | Description |
|-----------|-------------|
| `*.bsl` | Any BSL module file |
| Common modules | Server/client common modules |
| Object modules | Catalog, Document object modules |
| Manager modules | Catalog, Document manager modules |
| Form modules | Form module code |
| Extension modules | Extension configuration modules |

---

## 🔍 Technical Details

### How the Check Works

1. Finds all `Pragma` nodes in the module AST
2. Compares the pragma symbol against the list of canonical annotation symbols
3. Performs **case-sensitive comparison**
4. Reports error if the case doesn't match the canonical form

### Check Implementation Class

```
com.e1c.v8codestyle.bsl.check.CanonicalPragmaCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.bsl/src/com/e1c/v8codestyle/bsl/check/CanonicalPragmaCheck.java
```

---

## 📚 References

- [1C:Enterprise Development Standards - Code Conventions](https://its.1c.ru/db/v8std)
- [1C:Enterprise Platform - Method Annotations](https://1c-dn.com/library/method_annotations/)
