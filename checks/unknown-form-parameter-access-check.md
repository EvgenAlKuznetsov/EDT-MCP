# unknown-form-parameter-access-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `unknown-form-parameter-access-check` |
| **Title** | Access to unknown form opening parameter |
| **Description** | Checks for access to form parameters that are not defined in the form |
| **Severity** | `MAJOR` |
| **Type** | `ERROR` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check identifies when code accesses a **form parameter** that is **not defined** in the form's parameter list. This indicates either a typo, outdated code, or missing parameter definition.

### Why This Is Important

- **Runtime error**: Accessing undefined parameter fails
- **Maintainability**: Code references non-existent parameters
- **Clarity**: Parameter list should be complete
- **Documentation**: Parameters should be explicitly defined

---

## ❌ Error Example

### Error Message

```
Access to unknown form parameter 'ParameterName'. Parameter is not defined in the form.
```

### Noncompliant Code Example

```bsl
// ❌ Accessing parameter not defined in form
&AtServer
Procedure OnCreateAtServer(Cancel, StandardProcessing)
    // ❌ ERROR: "CustomFilter" is not in form parameters list!
    If Parameters.Property("CustomFilter") Then
        ApplyFilter(Parameters.CustomFilter);
    EndIf;
    
    // ❌ ERROR: Typo - "Onwer" instead of "Owner"
    If Parameters.Property("Onwer") Then
        FillByOwner(Parameters.Onwer);
    EndIf;
EndProcedure

// ❌ Parameter existed before but was removed
&AtServer
Procedure OnCreateAtServer(Cancel, StandardProcessing)
    // ❌ ERROR: "LegacyMode" was removed from parameters
    If Parameters.LegacyMode Then
        EnableLegacyMode();
    EndIf;
EndProcedure

// ❌ Misspelled parameter name
&AtClient
Procedure OnOpen(Cancel)
    // ❌ ERROR: Should be "AutoStart" not "AutoStrat"
    If Parameters.Property("AutoStrat") And Parameters.AutoStrat Then
        StartProcess();
    EndIf;
EndProcedure
```

---

## ✅ Compliant Solution

### Define Parameters in Form

First, add the parameter to the form's parameter list:

```
Form Properties → Parameters:
  - Name: CustomFilter, Type: ValueList
  - Name: Owner, Type: CatalogRef.Companies
  - Name: AutoStart, Type: Boolean
```

### Then Access in Code

```bsl
// ✅ Parameter is defined in form
&AtServer
Procedure OnCreateAtServer(Cancel, StandardProcessing)
    // ✅ "CustomFilter" is now defined in form parameters
    If Parameters.Property("CustomFilter") Then
        ApplyFilter(Parameters.CustomFilter);
    EndIf;
    
    // ✅ Correct spelling - "Owner" is defined
    If Parameters.Property("Owner") Then
        FillByOwner(Parameters.Owner);
    EndIf;
EndProcedure

// ✅ Using correctly defined parameter
&AtClient
Procedure OnOpen(Cancel)
    // ✅ "AutoStart" is properly defined and spelled
    If Parameters.Property("AutoStart") And Parameters.AutoStart Then
        StartProcess();
    EndIf;
EndProcedure
```

---

## 📖 Understanding Form Parameters

### Where Parameters Are Defined

Form parameters are defined in the form's configuration:

```
Configuration
└── Documents
    └── Order
        └── Forms
            └── ItemForm
                └── Parameters (Form attribute)
                    ├── Key (DocumentRef.Order)
                    ├── Mode (String)
                    └── Filter (Structure)
```

### Parameter Properties

| Property | Description |
|----------|-------------|
| Name | Parameter identifier |
| Type | Expected data type |
| Required | Whether parameter must be passed |

---

## 📋 Common Causes

### 1. Typos in Parameter Names

```bsl
// ❌ Typo
Parameters.Comapny  // Should be "Company"
Parameters.Filtre   // Should be "Filter"
Parameters.Mode_    // Should be "Mode"
```

### 2. Removed Parameters

```bsl
// Parameter was removed from form but code still references it
// ❌ Parameter no longer exists
If Parameters.Property("OldParameter") Then
    // This parameter was deleted
EndIf;
```

### 3. Parameters from Different Form

```bsl
// Copy-pasted code from another form
// ❌ This parameter belongs to a different form
Value = Parameters.DocumentMode; // Only exists in DocumentForm
```

### 4. Missing Parameter Definition

```bsl
// Developer forgot to add parameter to form
// ❌ Using parameter without defining it first
If Parameters.CustomSetting Then
    ApplySetting();
EndIf;
```

---

## 🔧 How to Fix

### Option 1: Add Missing Parameter

1. Open form in Designer
2. Go to Form Properties → Parameters
3. Add the missing parameter
4. Set appropriate type and properties

### Option 2: Fix Typo

```bsl
// ❌ Before
Parameters.Comapny

// ✅ After
Parameters.Company
```

### Option 3: Remove Obsolete Code

```bsl
// ❌ Before - accessing removed parameter
If Parameters.Property("LegacyMode") Then
    EnableLegacyMode();
EndIf;

// ✅ After - remove obsolete code
// (Parameter was intentionally removed)
```

### Option 4: Use Correct Parameter

```bsl
// ❌ Before - using parameter from different form
Value = Parameters.OtherFormParameter;

// ✅ After - use correct parameter for this form
Value = Parameters.ThisFormParameter;
```

---

## 📋 Best Practices for Form Parameters

### Define All Expected Parameters

```
Form Parameters:
  - Key: Required, DocumentRef.Order
  - Mode: Optional, String
  - Filter: Optional, Structure
  - Owner: Optional, CatalogRef.Companies
  - CloseOnChoice: Optional, Boolean
```

### Document Parameter Purpose

```bsl
// Form Parameters:
// - Key: Document reference for editing
// - Mode: "Create", "Edit", or "View"
// - Filter: Structure with filter conditions
// - Owner: Parent catalog item
// - CloseOnChoice: Close form after selection
```

### Validate Required Parameters

```bsl
&AtServer
Procedure OnCreateAtServer(Cancel, StandardProcessing)
    // Validate required parameter
    If Not ValueIsFilled(Parameters.Key) Then
        Raise NStr("en = 'Key parameter is required'");
    EndIf;
EndProcedure
```

---

## 📋 Checking Before Fixing

### Verify Parameter Should Exist

Before adding a "missing" parameter:

1. Check if parameter is actually needed
2. Review where form is opened from
3. Verify parameter is passed when opening form

```bsl
// Check how form is opened
OpenFormParameters = New Structure;
OpenFormParameters.Insert("Key", DocumentRef);
OpenFormParameters.Insert("Mode", "Edit");
// Is "CustomFilter" passed? If not, why access it?

OpenForm("Document.Order.Form.ItemForm", OpenFormParameters);
```

---

## 🔍 Technical Details

### What Is Checked

1. All `Parameters.` access in form module
2. Parameter names against form definition
3. Property method parameter names

### Check Implementation Class

```
com.e1c.v8codestyle.bsl.check.UnknownFormParameterAccessCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.bsl/src/com/e1c/v8codestyle/bsl/check/
```

---

## 📋 Related Parameters

### Standard Form Parameters

These parameters are always available and don't need definition:

| Parameter | Description |
|-----------|-------------|
| `Key` | Object reference (for object forms) |
| `FillingValues` | Values for new object |
| `CopyingValue` | Source for copy |

### Custom Parameters

These must be defined:

```
Form Parameters:
  - Mode
  - Filter
  - Owner
  - CustomSetting
  - etc.
```

---

## 📚 References

- [1C Documentation: Form Parameters](https://1c-dn.com/library/form_parameters/)
- [Opening Forms with Parameters](https://1c-dn.com/library/opening_forms/)
- [Optional Form Parameter Access Check](optional-form-parameter-access-check.md)
