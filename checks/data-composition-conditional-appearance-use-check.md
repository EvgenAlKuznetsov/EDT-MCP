# data-composition-conditional-appearance-use-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `data-composition-conditional-appearance-use-check` |
| **Title** | Use data composition conditional appearance |
| **Description** | Checks for usage of conditional appearance in forms |
| **Severity** | `MINOR` |
| **Type** | `CODE_SMELL` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check identifies when **conditional appearance** is used in forms via data composition. While conditional appearance is powerful, it can indicate overly complex form design or performance issues.

### Why This Is Important

- **Performance**: Conditional appearance recalculates on every change
- **Maintainability**: Complex appearance rules are hard to maintain
- **Debugging**: Appearance logic scattered across settings
- **Simplicity**: Sometimes direct property control is clearer

---

## ❌ Error Example

### Error Messages

```
Form "FormName" uses conditional appearance
Form attribute "AttributeName" uses conditional appearance
```

### Noncompliant Scenario

In form designer, conditional appearance is configured with multiple complex rules:

```
Conditional Appearance:
├── Rule 1: If Amount > 1000 Then BackColor = Red
├── Rule 2: If Status = "Closed" Then ReadOnly = True
├── Rule 3: If Priority = "High" Then Font.Bold = True
├── Rule 4: If Overdue = True Then TextColor = Red
└── ... (many more rules)
```

### Code Triggering Issue

```bsl
// Complex conditional appearance causes performance issues
// when form has many rows and frequent updates
&AtServer
Procedure OnCreateAtServer(Cancel, StandardProcessing)
    // Data loads and conditional appearance recalculates
    // for every row in the dynamic list
EndProcedure
```

---

## ✅ Compliant Solution

### Alternative 1: Use Code-Based Formatting

```bsl
// ✅ Control appearance directly in code
&AtClient
Procedure ItemsOnActivateRow(Item)
    CurrentData = Items.Items.CurrentData;
    If CurrentData = Undefined Then
        Return;
    EndIf;
    
    // Set appearance based on data
    If CurrentData.Amount > 1000 Then
        Items.Amount.BackColor = WebColors.LightCoral;
    Else
        Items.Amount.BackColor = New Color;
    EndIf;
EndProcedure
```

### Alternative 2: Simplify Appearance Rules

```bsl
// ✅ Use simple boolean attributes for appearance
&AtServer
Procedure OnCreateAtServer(Cancel, StandardProcessing)
    // Add calculated column for status
    Items.StatusIndicator.Visible = True;
EndProcedure
```

### Alternative 3: Pre-calculate Display Values

```bsl
// ✅ Add display columns with calculated values
&AtServer
Procedure FillDisplayData()
    For Each Row In Object.Items Do
        If Row.Amount > 1000 Then
            Row.AmountClass = "HighAmount";
        Else
            Row.AmountClass = "NormalAmount";
        EndIf;
    EndDo;
EndProcedure
```

---

## 📋 When Conditional Appearance Is Acceptable

### Simple Rules

```
// ✅ Acceptable: Few simple rules
Conditional Appearance:
├── If Posted = True Then TextColor = Gray
└── If Deleted = True Then StrikeThrough = True
```

### Static Data

```
// ✅ Acceptable: Rules on rarely changing data
Conditional Appearance:
└── If Type = "Service" Then BackColor = LightBlue
```

---

## 📋 When to Avoid Conditional Appearance

### Many Rules

Having more than 5-10 conditional appearance rules indicates complexity.

### Frequent Updates

When data changes often, conditional appearance recalculates repeatedly.

### Large Data Sets

With thousands of rows, appearance calculation impacts performance.

### Complex Conditions

Multi-condition rules are hard to maintain in designer.

---

## 🔧 How to Fix

### Option 1: Move to Code

Control appearance properties directly in event handlers.

### Option 2: Simplify Rules

Reduce number of conditional appearance rules.

### Option 3: Pre-compute States

Add status columns and use simpler appearance rules.

### Option 4: Use Form Item Properties

Set fixed properties instead of conditional rules.

---

## 📋 Performance Comparison

| Approach | Performance | Maintainability |
|----------|-------------|-----------------|
| Conditional Appearance (many rules) | Slow | Hard |
| Conditional Appearance (few rules) | OK | OK |
| Code-based (OnActivateRow) | Fast | Good |
| Pre-calculated columns | Fast | Good |

---

## 📋 Migration Example

### Before: Conditional Appearance

```
Form ConditionalAppearance:
├── Condition: Amount > 1000
│   └── Appearance: BackColor = Red
└── Condition: Status = "Cancelled"
    └── Appearance: TextColor = Gray
```

### After: Code-Based

```bsl
&AtClient
Procedure ItemsOnActivateRow(Item)
    CurrentData = Items.Items.CurrentData;
    If CurrentData = Undefined Then
        Return;
    EndIf;
    
    // Amount formatting
    If CurrentData.Amount > 1000 Then
        Items.Amount.TextColor = WebColors.Red;
    Else
        Items.Amount.TextColor = New Color;
    EndIf;
    
    // Status formatting
    If CurrentData.Status = Enums.OrderStatuses.Cancelled Then
        Items.ItemsRow.TextColor = WebColors.Gray;
    Else
        Items.ItemsRow.TextColor = New Color;
    EndIf;
EndProcedure
```

---

## 🔍 Technical Details

### What Is Checked

1. Form conditional appearance settings
2. Attribute-level conditional appearance
3. Complexity of rules

### Check Implementation Class

```
com.e1c.v8codestyle.form.check.DataCompositionConditionalAppearanceUseCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.form/src/com/e1c/v8codestyle/form/check/
```

---

## 📚 References

- [1C Documentation: Conditional Appearance](https://1c-dn.com/library/conditional_appearance/)
- [Form Performance Optimization](https://1c-dn.com/library/form_optimization/)
- [Data Composition System](https://1c-dn.com/library/data_composition/)
