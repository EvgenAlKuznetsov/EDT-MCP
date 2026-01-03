# constructor-function-return-section

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `constructor-function-return-section` |
| **Title** | Constructor function must have return section in documentation |
| **Description** | Check that constructor functions have proper return section documentation |
| **Severity** | `MINOR` |
| **Type** | `CODE_SMELL` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |
| **1C Standard** | [453](https://its.1c.ru/db/v8std/content/453/hdoc) |

---

## 🎯 What This Check Does

This check validates that **constructor functions** (functions that create and return objects/structures) have a properly documented **return section** in their documentation comments.

### Why This Is Important

- **API clarity**: Users know what the function returns
- **Type information**: Return type is documented
- **Standards compliance**: Follows 1C documentation standard (Standard 453)
- **Code maintenance**: Easier to understand function purpose

---

## ❌ Error Example

### Error Message

```
Constructor function must have return section in documentation
```

**Russian:**
```
Функция-конструктор должна иметь секцию возвращаемого значения в документации
```

### Noncompliant Code Example

```bsl
// ❌ Missing return section in documentation
// Creates a new person structure.
//
// Parameters:
//  Name - String - Person's name
//  Age - Number - Person's age
//
Function CreatePerson(Name, Age) Export
    Person = New Structure;
    Person.Insert("Name", Name);
    Person.Insert("Age", Age);
    Person.Insert("CreatedAt", CurrentSessionDate());
    Return Person;
EndFunction
```

---

## ✅ Compliant Solution

### Correct Documentation

```bsl
// ✅ Has return section
// Creates a new person structure.
//
// Parameters:
//  Name - String - Person's name
//  Age - Number - Person's age
//
// Returns:
//  Structure - Person data:
//   * Name - String - Person's name
//   * Age - Number - Person's age
//   * CreatedAt - Date - Creation timestamp
//
Function CreatePerson(Name, Age) Export
    Person = New Structure;
    Person.Insert("Name", Name);
    Person.Insert("Age", Age);
    Person.Insert("CreatedAt", CurrentSessionDate());
    Return Person;
EndFunction
```

---

## 📖 Constructor Function Patterns

### What Is a Constructor Function

A constructor function:
- Creates a new object (Structure, Map, Array, etc.)
- Returns the created object
- Often named with `New`, `Create`, or object name prefix

### Common Constructor Patterns

```bsl
// Pattern 1: NewXxx()
Function NewDocumentData() Export
    // ...
EndFunction

// Pattern 2: CreateXxx()
Function CreateOrder(Customer, Date) Export
    // ...
EndFunction

// Pattern 3: XxxConstructor()
Function PersonConstructor(Name) Export
    // ...
EndFunction
```

---

## 📋 Documentation Format

### Standard Return Section Format

```bsl
// Returns:
//  <Type> - <Description>:
//   * <PropertyName> - <Type> - <Description>
//   * <PropertyName> - <Type> - <Description>
```

### Examples by Return Type

**Structure:**
```bsl
// Returns:
//  Structure - Order data:
//   * Number - String - Order number
//   * Date - Date - Order date
//   * Customer - CatalogRef.Customers - Customer reference
//   * Total - Number - Order total amount
```

**Array:**
```bsl
// Returns:
//  Array of Structure - List of items:
//   * Code - String - Item code
//   * Name - String - Item name
```

**Map:**
```bsl
// Returns:
//  Map of KeyAndValue:
//   * Key - String - Setting name
//   * Value - Arbitrary - Setting value
```

**ValueTable:**
```bsl
// Returns:
//  ValueTable - Products table:
//   * Product - CatalogRef.Products - Product reference
//   * Quantity - Number - Quantity
//   * Price - Number - Unit price
```

---

## 🔧 How to Fix

### Step 1: Identify constructor functions

Look for functions that:
- Create and return objects
- Have names like `New...`, `Create...`, `...Constructor`

### Step 2: Add return section

Add `// Returns:` section after parameters:

```bsl
// Returns:
//  <ReturnType> - <Description>
```

### Step 3: Document structure properties

For Structure/Map returns, document each property:

```bsl
// Returns:
//  Structure:
//   * PropertyName - PropertyType - Description
```

---

## 📋 Complete Example

### Before (Noncompliant)

```bsl
// Creates settings structure for report generation.
//
// Parameters:
//  ReportName - String - Report identifier
//
Function CreateReportSettings(ReportName) Export
    Settings = New Structure;
    Settings.Insert("ReportName", ReportName);
    Settings.Insert("Period", New StandardPeriod);
    Settings.Insert("OutputType", "PDF");
    Settings.Insert("SendByEmail", False);
    Return Settings;
EndFunction
```

### After (Compliant)

```bsl
// Creates settings structure for report generation.
//
// Parameters:
//  ReportName - String - Report identifier
//
// Returns:
//  Structure - Report generation settings:
//   * ReportName - String - Report identifier
//   * Period - StandardPeriod - Report period
//   * OutputType - String - Output format (PDF, Excel, HTML)
//   * SendByEmail - Boolean - Whether to send result by email
//
Function CreateReportSettings(ReportName) Export
    Settings = New Structure;
    Settings.Insert("ReportName", ReportName);
    Settings.Insert("Period", New StandardPeriod);
    Settings.Insert("OutputType", "PDF");
    Settings.Insert("SendByEmail", False);
    Return Settings;
EndFunction
```

---

## 🔍 Technical Details

### What Is Checked

1. Identifies functions that appear to be constructors
2. Checks for presence of `// Returns:` section
3. Reports if return documentation is missing

### Check Implementation Class

```
com.e1c.v8codestyle.bsl.check.ConstructorFunctionReturnSection
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.bsl/src/com/e1c/v8codestyle/bsl/check/
```

---

## 📚 References

- [1C:Enterprise Documentation Standard - Standard 453](https://its.1c.ru/db/v8std/content/453/hdoc)
- [Function Documentation Best Practices](https://1c-dn.com/library/documentation/)
