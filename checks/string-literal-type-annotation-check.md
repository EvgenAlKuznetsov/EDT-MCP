# string-literal-type-annotation-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `string-literal-type-annotation-check` |
| **Title** | String literal type annotation check |
| **Description** | Checks for string literals used as type annotations instead of proper types |
| **Severity** | `MAJOR` |
| **Type** | `ERROR` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check identifies when a **string literal** is used where a **type annotation** should be used. String literals may contain typos and don't provide IDE support, while proper type annotations are validated.

### Why This Is Important

- **Type safety**: String literals are not validated
- **IDE support**: Proper types enable autocomplete
- **Refactoring**: Type references update automatically
- **Error detection**: Typos in strings are not caught

---

## ❌ Error Example

### Error Message

```
String literal used instead of Type() function. Use Type("TypeName") for type annotations.
```

### Noncompliant Code Example

```bsl
// ❌ String literal instead of Type in comparisons
Procedure ProcessValue(Value)
    // ❌ String comparison - no validation
    If TypeOf(Value) = "CatalogRef.Products" Then
        ProcessProduct(Value);
    EndIf;
    
    // ❌ String in Case statement
    Switch TypeOf(Value)
        Case "String":
            ProcessString(Value);
        Case "Number":
            ProcessNumber(Value);
    EndSwitch;
EndProcedure

// ❌ String literal in variable descriptions
// @param Value - "CatalogRef.Products" - product reference
Procedure ProcessProduct(Value)
    // ...
EndProcedure

// ❌ String in type casting
Function ConvertValue(Value)
    // ❌ Wrong usage
    Return Value As "Number";  // Invalid syntax anyway
EndFunction
```

---

## ✅ Compliant Solution

### Use Type() Function

```bsl
// ✅ Type() function for type comparison
Procedure ProcessValue(Value)
    // ✅ Proper type comparison
    If TypeOf(Value) = Type("CatalogRef.Products") Then
        ProcessProduct(Value);
    EndIf;
    
    // ✅ Type() in comparison
    If TypeOf(Value) = Type("String") Then
        ProcessString(Value);
    ElsIf TypeOf(Value) = Type("Number") Then
        ProcessNumber(Value);
    EndIf;
EndProcedure

// ✅ Proper type annotation in comments
// @param Value - CatalogRef.Products - product reference
Procedure ProcessProduct(Value)
    // ...
EndProcedure
```

---

## 📋 Correct Type Usage Patterns

### Type Comparison

```bsl
// ✅ Compare with Type()
If TypeOf(Value) = Type("String") Then
    // Value is a string
EndIf;

If TypeOf(Value) = Type("Array") Then
    // Value is an array
EndIf;

If TypeOf(Value) = Type("CatalogRef.Products") Then
    // Value is a product reference
EndIf;
```

### TypeDescription Construction

```bsl
// ✅ Create TypeDescription with types
TypesArray = New Array;
TypesArray.Add(Type("String"));
TypesArray.Add(Type("Number"));

TypeDesc = New TypeDescription(TypesArray);
```

### Type Check in Conditions

```bsl
// ✅ Multiple type checks
Procedure ProcessValue(Value)
    ValueType = TypeOf(Value);
    
    If ValueType = Type("String") Then
        Result = ProcessString(Value);
    ElsIf ValueType = Type("Number") Then
        Result = ProcessNumber(Value);
    ElsIf ValueType = Type("Date") Then
        Result = ProcessDate(Value);
    ElsIf ValueType = Type("Boolean") Then
        Result = ProcessBoolean(Value);
    Else
        Raise "Unsupported type";
    EndIf;
EndProcedure
```

---

## 📋 Common Type Names

### Primitive Types

| Type Name | Description |
|-----------|-------------|
| `"String"` | String value |
| `"Number"` | Numeric value |
| `"Date"` | Date/time value |
| `"Boolean"` | True/False |
| `"Undefined"` | Undefined value |
| `"Null"` | Null value |

### Collection Types

| Type Name | Description |
|-----------|-------------|
| `"Array"` | Array |
| `"Structure"` | Structure |
| `"Map"` | Map |
| `"ValueList"` | Value list |
| `"ValueTable"` | Value table |
| `"ValueTree"` | Value tree |

### Reference Types

| Type Name | Description |
|-----------|-------------|
| `"CatalogRef.Name"` | Catalog reference |
| `"DocumentRef.Name"` | Document reference |
| `"EnumRef.Name"` | Enumeration reference |
| `"ChartOfAccountsRef.Name"` | Chart of accounts ref |

---

## 📋 Type Annotations in Documentation

### Function Parameters

```bsl
// ✅ Correct type annotation in comments
// 
// Parameters:
//  Customer - CatalogRef.Customers - customer reference
//  Amount - Number - order amount
//  Date - Date - order date
//
Function CreateOrder(Customer, Amount, Date) Export
    // ...
EndFunction
```

### Return Values

```bsl
// ✅ Correct return type annotation
// 
// Returns:
//  Array of CatalogRef.Products - list of products
//
Function GetProductList() Export
    // ...
EndFunction
```

---

## 📋 Type Validation Examples

### Validating Parameter Type

```bsl
// ✅ Validate with Type()
Procedure ProcessCustomer(Customer)
    If TypeOf(Customer) <> Type("CatalogRef.Customers") Then
        Raise "Expected CatalogRef.Customers";
    EndIf;
    
    // Process customer
EndProcedure
```

### Checking Multiple Types

```bsl
// ✅ Check if value is one of expected types
Function IsNumericType(Value)
    ValueType = TypeOf(Value);
    Return ValueType = Type("Number");
EndFunction

Function IsReferenceType(Value)
    ValueType = TypeOf(Value);
    
    TypeDesc = New TypeDescription("CatalogRef, DocumentRef, EnumRef");
    
    Return TypeDesc.ContainsType(ValueType);
EndFunction
```

---

## 🔧 How to Fix

### Step 1: Find string type comparisons

Search for `TypeOf(...)` comparisons with strings.

### Step 2: Wrap with Type() function

Change `"TypeName"` to `Type("TypeName")`.

### Step 3: Verify type name is correct

Check that type name exists in configuration.

---

## 📋 Fixing Examples

### Example 1: Simple Comparison

```bsl
// ❌ Before
If TypeOf(Value) = "String" Then

// ✅ After
If TypeOf(Value) = Type("String") Then
```

### Example 2: Reference Type

```bsl
// ❌ Before
If TypeOf(Ref) = "CatalogRef.Products" Then

// ✅ After
If TypeOf(Ref) = Type("CatalogRef.Products") Then
```

### Example 3: Multiple Comparisons

```bsl
// ❌ Before
If TypeOf(Value) = "Number" Or TypeOf(Value) = "String" Then

// ✅ After
If TypeOf(Value) = Type("Number") Or TypeOf(Value) = Type("String") Then
```

---

## 📋 TypeDescription Usage

### Creating Type Description

```bsl
// ✅ Proper TypeDescription creation
StringType = Type("String");
NumberType = Type("Number");

// From array
Types = New Array;
Types.Add(StringType);
Types.Add(NumberType);
TypeDesc = New TypeDescription(Types);

// From string (comma-separated)
TypeDesc = New TypeDescription("String, Number, Date");
```

### Type Validation

```bsl
// ✅ Check if type is in description
TypeDesc = New TypeDescription("String, Number");

If TypeDesc.ContainsType(TypeOf(Value)) Then
    // Value is String or Number
EndIf;
```

---

## 🔍 Technical Details

### What Is Checked

1. String literals in type contexts
2. Comparisons with TypeOf()
3. Type annotations in code

### Check Implementation Class

```
com.e1c.v8codestyle.bsl.check.StringLiteralTypeAnnotationCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.bsl/src/com/e1c/v8codestyle/bsl/check/
```

---

## 📚 References

- [1C Documentation: Type Function](https://1c-dn.com/library/type/)
- [1C Documentation: TypeOf Function](https://1c-dn.com/library/typeof/)
- [1C Documentation: TypeDescription](https://1c-dn.com/library/typedescription/)
