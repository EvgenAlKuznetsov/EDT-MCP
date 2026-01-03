# module-unused-local-variable-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `module-unused-local-variable-check` |
| **Title** | Unused local variable check |
| **Description** | Checks for local variables that are declared but never used |
| **Severity** | `MINOR` |
| **Type** | `CODE_SMELL` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check identifies **local variables** that are declared or assigned but **never used**. Unused variables indicate dead code or incomplete implementation.

### Why This Is Important

- **Dead code**: Unused variables waste memory and confuse readers
- **Code clarity**: Removing unused variables makes code cleaner
- **Potential bugs**: May indicate forgotten logic
- **Maintenance**: Easier to maintain lean code

---

## ❌ Error Example

### Error Messages

```
Unused local variable "{VariableName}"
```

```
Probably local variable "{VariableName}" has not been initialized yet
```

### Noncompliant Code Example

```bsl
// ❌ Variable declared but never used
Procedure ProcessData() Export
    Counter = 0; // ❌ Counter is never used
    TempValue = GetValue(); // ❌ TempValue is never used
    
    Result = Calculate();
    Return Result;
EndProcedure

// ❌ Variable assigned but overwritten
Function GetTotal() Export
    Total = 0; // ❌ Immediately overwritten
    Total = CalculateSum();
    Return Total;
EndFunction

// ❌ Loop variable unused
Procedure IterateItems(Items)
    For Each Item In Items Do // ❌ Item is never used
        DoSomething();
    EndDo;
EndProcedure
```

---

## ✅ Compliant Solution

### Correct Code Without Unused Variables

```bsl
// ✅ All variables are used
Procedure ProcessData() Export
    Result = Calculate();
    Return Result;
EndProcedure

// ✅ No unnecessary initialization
Function GetTotal() Export
    Total = CalculateSum();
    Return Total;
EndFunction

// ✅ Loop variable is used
Procedure IterateItems(Items)
    For Each Item In Items Do
        ProcessItem(Item); // ✅ Item is used
    EndDo;
EndProcedure
```

---

## 📋 Common Patterns

### 1. Leftover Debug Variables

```bsl
// ❌ Debug variable left behind
Procedure Process()
    DebugValue = Object.Ref; // Was used for debugging
    
    // Actual processing
    Object.Write();
EndProcedure

// ✅ Remove debug variables
Procedure Process()
    Object.Write();
EndProcedure
```

### 2. Copy-Paste Remnants

```bsl
// ❌ Copied from another method, not needed here
Procedure SimpleProcess()
    Settings = GetSettings(); // Not used in this simple version
    ProcessData();
EndProcedure

// ✅ Remove unnecessary variables
Procedure SimpleProcess()
    ProcessData();
EndProcedure
```

### 3. Unused Function Results

```bsl
// ❌ Function result not used
Procedure UpdateData()
    Result = WriteToDatabase(); // Result is ignored
EndProcedure

// ✅ Either use the result or call as procedure
Procedure UpdateData()
    WriteToDatabase(); // If result not needed
EndProcedure

// Or use the result
Procedure UpdateData()
    Result = WriteToDatabase();
    If Not Result Then
        HandleError();
    EndIf;
EndProcedure
```

### 4. Overwritten Variables

```bsl
// ❌ First assignment is overwritten
Procedure Calculate()
    Value = 0;
    Value = 10;
    Value = GetActualValue(); // Previous values never used
    Return Value;
EndProcedure

// ✅ Single assignment
Procedure Calculate()
    Value = GetActualValue();
    Return Value;
EndProcedure
```

---

## 🔧 How to Fix

### Option 1: Remove the Variable

If the variable is not needed:

```bsl
// Before
Procedure Process()
    UnusedVar = GetData();
    DoSomething();
EndProcedure

// After
Procedure Process()
    DoSomething();
EndProcedure
```

### Option 2: Use the Variable

If the variable should be used:

```bsl
// Before
Procedure Process()
    Data = GetData(); // Unused
    DoSomething();
EndProcedure

// After
Procedure Process()
    Data = GetData();
    DoSomething(Data); // Now used
EndProcedure
```

### Option 3: Prefix with Underscore (Convention)

If intentionally unused (e.g., required by interface):

```bsl
// Some frameworks accept underscore prefix for intentionally unused
For Each _Item In Items Do
    Counter = Counter + 1; // Just counting
EndDo;
```

---

## 📖 Special Cases

### Loop Counter Only

```bsl
// When you only need to count iterations
For Index = 1 To 10 Do
    ProcessNext(); // Index not used
EndDo;

// Alternative: While loop
Counter = 0;
While Counter < 10 Do
    ProcessNext();
    Counter = Counter + 1;
EndDo;
```

### Destructuring Partial Values

```bsl
// When structure has unused parts
Result = GetResult();
Value = Result.Value; // Only need Value
// Status = Result.Status; // Not needed - don't declare
```

### Required Parameters

```bsl
// Event handlers may have required parameters
Procedure OnChange(Item)
    // Item not used but required by signature
    RecalculateForm();
EndProcedure
```

---

## 🔍 Technical Details

### What Is Checked

1. All local variable assignments
2. Variable usage analysis
3. Identifies never-read variables

### Not Flagged

- Module-level variables (different check)
- Parameters (handled separately)
- Variables used via reflection

### Check Implementation Class

```
com.e1c.v8codestyle.bsl.check.ModuleUnusedLocalVariableCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.bsl/src/com/e1c/v8codestyle/bsl/check/
```

---

## 📚 References

- [Module Unused Method Check](module-unused-method-check.md)
- [Module Empty Method Check](module-empty-method-check.md)
- [Code Quality Best Practices](https://1c-dn.com/library/clean_code/)
