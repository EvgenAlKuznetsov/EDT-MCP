# module-undefined-variable-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `module-undefined-variable-check` |
| **Title** | Undefined variable |
| **Description** | Checks for usage of variables that are not defined |
| **Severity** | `CRITICAL` |
| **Type** | `ERROR` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check identifies usage of **variables that are not defined** in the current scope. Using an undefined variable will cause a runtime error.

### Why This Is Important

- **Runtime errors**: Undefined variables cause exceptions
- **Typo detection**: Catches misspelled variable names
- **Logic errors**: Prevents incorrect data access
- **Code quality**: Ensures variables are properly declared

---

## ❌ Error Example

### Error Message

```
Variable "{VariableName}" is not defined
```

### Noncompliant Code Example

```bsl
// ❌ Using undefined variable
Procedure ProcessData() Export
    Result = Countr + 1; // ❌ Typo: "Countr" instead of "Counter"
    Message(String(Result));
EndProcedure

// ❌ Variable never declared
Function CalculateTotal() Export
    Total = Price * Quantity; // ❌ "Price" and "Quantity" not defined
    Return Total;
EndFunction

// ❌ Wrong scope
Procedure OuterProcedure()
    LocalVar = 10;
EndProcedure

Procedure AnotherProcedure()
    Result = LocalVar; // ❌ LocalVar is not in scope
EndProcedure
```

---

## ✅ Compliant Solution

### Correct Code with Defined Variables

```bsl
// ✅ Variable is defined before use
Procedure ProcessData() Export
    Counter = 0;
    Result = Counter + 1; // ✅ Counter is defined
    Message(String(Result));
EndProcedure

// ✅ Parameters are defined
Function CalculateTotal(Price, Quantity) Export
    Total = Price * Quantity; // ✅ Parameters are defined
    Return Total;
EndFunction

// ✅ Using module-level variable
Var SharedVariable;

Procedure OuterProcedure()
    SharedVariable = 10;
EndProcedure

Procedure AnotherProcedure()
    Result = SharedVariable; // ✅ Module variable is in scope
EndProcedure
```

---

## 📋 Variable Scope

### Scope Levels

| Scope | Visibility |
|-------|------------|
| Local | Within procedure/function only |
| Module | Entire module |
| Global | Entire configuration (export) |

### Examples

```bsl
// Module-level variable
Var ModuleVariable;

Procedure Example()
    // Local variable
    LocalVariable = 10;
    
    // Using module variable
    ModuleVariable = LocalVariable; // ✅ Both in scope
EndProcedure

Procedure AnotherExample()
    // ❌ LocalVariable not in scope here
    Result = LocalVariable; // Error!
    
    // ✅ ModuleVariable is in scope
    Result = ModuleVariable; // OK
EndProcedure
```

---

## 📖 Common Causes

### 1. Typos in Variable Names

```bsl
CustomerNmae = "John"; // Typo
Message(CustomerName); // ❌ Different spelling

// Correct
CustomerName = "John";
Message(CustomerName); // ✅ Same spelling
```

### 2. Wrong Scope Access

```bsl
Procedure ProcessOrder(Order)
    OrderTotal = CalculateTotal(Order);
EndProcedure

Procedure DisplayResults()
    Message(String(OrderTotal)); // ❌ Not in scope
EndProcedure
```

### 3. Conditional Definition

```bsl
// ❌ Variable may not be defined
If Condition Then
    Value = 10;
EndIf;
Result = Value; // ❌ Value might not exist

// ✅ Always define
Value = 0;
If Condition Then
    Value = 10;
EndIf;
Result = Value; // ✅ Always defined
```

### 4. Loop Variable Scope

```bsl
// In BSL, loop variables are defined within loop
For Each Item In Collection Do
    ProcessItem(Item);
EndDo;
// Item is still accessible after loop in BSL
```

---

## 🔧 How to Fix

### Step 1: Check variable spelling

```bsl
// Wrong
Custmer = GetCustomer();

// Correct
Customer = GetCustomer();
```

### Step 2: Declare the variable

```bsl
// For module-level
Var ModuleVariable;

// For local - just assign
LocalVariable = InitialValue;
```

### Step 3: Check scope

Ensure variable is defined in the same or higher scope.

```bsl
// Before
Procedure Process()
    If Condition Then
        Value = 10;
    EndIf;
    Use(Value); // ❌ May not exist
EndProcedure

// After
Procedure Process()
    Value = 0; // Define first
    If Condition Then
        Value = 10;
    EndIf;
    Use(Value); // ✅ Always exists
EndProcedure
```

### Step 4: Add as parameter

```bsl
// If variable should come from caller
Function Calculate(Price, Quantity) // ✅ Parameters
    Return Price * Quantity;
EndFunction
```

---

## 📋 Variable Declaration

### Module Variables

```bsl
#Region Variables

Var Counter;
Var Settings;
Var Cache Export; // Visible externally

#EndRegion
```

### Local Variables

```bsl
Procedure Example()
    // Implicit declaration on first assignment
    LocalVar = 10;
    
    // Now LocalVar exists
    Result = LocalVar + 5;
EndProcedure
```

### Parameters

```bsl
// Parameters are automatically in scope
Procedure Process(InputData, Options)
    // InputData and Options are defined
    Result = ProcessInput(InputData, Options);
EndProcedure
```

---

## ⚠️ Special Cases

### Object Properties

```bsl
// Not variables - object properties
Customer.Name = "John"; // Property access, not variable
```

### Form Attributes

```bsl
// Form attributes are accessed differently
Object.Description = "Test"; // Form attribute
ThisObject.Description = "Test"; // Also valid
```

### Built-in Variables

```bsl
// Platform variables are always available
CurrentDate = CurrentDate();
User = CurrentUser();
```

---

## 🔍 Technical Details

### What Is Checked

1. All variable references
2. Symbol resolution in current scope
3. Module variable declarations
4. Procedure/function parameters

### Check Implementation Class

```
com.e1c.v8codestyle.bsl.check.ModuleUndefinedVariableCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.bsl/src/com/e1c/v8codestyle/bsl/check/
```

---

## 📚 References

- [Module Undefined Function Check](module-undefined-function-check.md)
- [Module Undefined Method Check](module-undefined-method-check.md)
- [BSL Variable Naming](bsl-variable-name-invalid.md)
- [1C Variables](https://1c-dn.com/library/variables/)
