# module-undefined-function-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `module-undefined-function-check` |
| **Title** | Undefined function |
| **Description** | Checks for calls to functions that are not defined |
| **Severity** | `CRITICAL` |
| **Type** | `ERROR` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check identifies calls to **functions that are not defined** in the current scope. Calling an undefined function will cause a runtime error.

### Why This Is Important

- **Runtime errors**: Undefined function calls crash the application
- **Typo detection**: Catches misspelled function names
- **Refactoring safety**: Detects broken references after renaming
- **Code quality**: Ensures code will execute correctly

---

## ❌ Error Example

### Error Message

```
Function "{FunctionName}" is not defined
```

### Noncompliant Code Example

```bsl
// ❌ Calling undefined function
Procedure ProcessData() Export
    Result = CalculateTotal(); // ❌ Function doesn't exist
    ShowResult(Result);
EndProcedure

// ❌ Typo in function name
Function GetCustomerData(CustomerRef) Export
    Data = New Structure;
    Data.Insert("Name", GettCustomerName(CustomerRef)); // ❌ Typo: "Gett" instead of "Get"
    Return Data;
EndFunction

// ❌ Calling deleted function
Procedure OldProcess() Export
    LegacyFunction(); // ❌ Was deleted during refactoring
EndProcedure
```

---

## ✅ Compliant Solution

### Correct Code with Defined Functions

```bsl
// ✅ All called functions are defined
Procedure ProcessData() Export
    Result = CalculateTotal();
    ShowResult(Result);
EndProcedure

Function CalculateTotal() // ✅ Function is defined
    Return 100;
EndFunction

Procedure ShowResult(Result) // ✅ Procedure is defined
    Message(String(Result));
EndProcedure

// ✅ Correct function name
Function GetCustomerData(CustomerRef) Export
    Data = New Structure;
    Data.Insert("Name", GetCustomerName(CustomerRef)); // ✅ Correct spelling
    Return Data;
EndFunction

Function GetCustomerName(CustomerRef) // ✅ Defined
    Return CustomerRef.Description;
EndFunction
```

---

## 📋 Common Causes

### 1. Typos in Function Names

```bsl
// ❌ Common typos
CalculateTotla()     // "Totla" instead of "Total"
GerUserData()        // "Ger" instead of "Get"
ProcessItmes()       // "Itmes" instead of "Items"
```

### 2. Deleted Functions

```bsl
// Function was deleted but calls remain
// ❌ OldValidation() was removed
Procedure Process()
    OldValidation(); // No longer exists
EndProcedure
```

### 3. Renamed Functions

```bsl
// Function was renamed from ValidateData to ValidateInput
// ❌ Old name still used
Procedure Process()
    ValidateData(); // Should be ValidateInput()
EndProcedure
```

### 4. Missing Import/Reference

```bsl
// Function from common module not properly referenced
// ❌ Missing module prefix
Procedure Process()
    Result = CalculatePrice(); // Should be PriceCalculation.CalculatePrice()
EndProcedure
```

### 5. Wrong Context

```bsl
// ❌ Server function called from client
&AtClient
Procedure Process()
    ServerOnlyFunction(); // Only available on server
EndProcedure
```

---

## 🔧 How to Fix

### Step 1: Check the function name

Verify spelling matches the actual function definition.

```bsl
// Wrong
CalcualteTotal()

// Correct
CalculateTotal()
```

### Step 2: Check if function exists

Look for the function definition in the module or referenced modules.

### Step 3: Add missing function

If the function should exist, create it:

```bsl
Function CalculateTotal()
    // Implementation
    Return Total;
EndFunction
```

### Step 4: Check module reference

If calling a function from another module, include the module name:

```bsl
// Wrong
Result = CalculatePrice(Item);

// Correct
Result = PricingModule.CalculatePrice(Item);
```

### Step 5: Check compilation context

Ensure the function is available in the current context (client/server).

---

## 📖 Function Scope

### Where Functions Can Be Called From

| Definition Location | Callable From |
|-------------------|---------------|
| Same module | Same module |
| Common module (export) | Any module |
| Object module (export) | Via object reference |
| Form module | Same form only |

### Example

```bsl
// Common module "Calculations"
Function Add(A, B) Export
    Return A + B;
EndFunction

// Calling from another module
Result = Calculations.Add(1, 2); // ✅ Correct with module prefix
Result = Add(1, 2); // ❌ Wrong - needs module prefix
```

---

## ⚠️ Differences from Undefined Method

| Check | Applies To |
|-------|------------|
| `undefined-function-check` | Function calls (expect return value) |
| `undefined-method-check` | Procedure and function calls |
| `undefined-variable-check` | Variable references |

---

## 🔍 Technical Details

### What Is Checked

1. All function call expressions
2. Symbol resolution in current scope
3. Common module references
4. Object method availability

### Check Implementation Class

```
com.e1c.v8codestyle.bsl.check.ModuleUndefinedFunctionCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.bsl/src/com/e1c/v8codestyle/bsl/check/
```

---

## 📚 References

- [Module Undefined Method Check](module-undefined-method-check.md)
- [Module Undefined Variable Check](module-undefined-variable-check.md)
- [1C Function Calls](https://1c-dn.com/library/functions/)
