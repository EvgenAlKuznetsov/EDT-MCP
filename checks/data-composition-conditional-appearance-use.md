# data-composition-conditional-appearance-use

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `data-composition-conditional-appearance-use` |
| **Title** | Conditional appearance should be used |
| **Description** | Check for proper use of conditional appearance in data composition |
| **Severity** | `MINOR` |
| **Type** | `CODE_SMELL` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check validates that **conditional appearance** settings in Data Composition Schema (DCS) reports are properly configured and actively used.

### Why This Is Important

- **User experience**: Conditional formatting improves report readability
- **Data clarity**: Highlights important values (negative, overdue, etc.)
- **Best practices**: Unused appearance settings indicate dead code
- **Performance**: Unnecessary appearance rules may impact rendering

---

## ❌ Error Example

### Error Message

```
Conditional appearance should be used
```

**Russian:**
```
Условное оформление должно использоваться
```

### Noncompliant Code Example

```xml
<!-- Data Composition Schema -->
<ConditionalAppearance>
    <Item>
        <Condition>
            <!-- Empty or always false condition -->
        </Condition>
        <Appearance>
            <!-- Appearance that never applies -->
        </Appearance>
    </Item>
</ConditionalAppearance>
```

Or in form module:

```bsl
// ❌ Setting appearance but not using it
Procedure OnCreateAtServer(Cancel, StandardProcessing)
    ConditionalAppearance = Report.SettingsComposer.Settings.ConditionalAppearance;
    Item = ConditionalAppearance.Items.Add();
    Item.Use = False;  // ❌ Disabled appearance item
EndProcedure
```

---

## ✅ Compliant Solution

### Correct Conditional Appearance

```bsl
// ✅ Active conditional appearance
Procedure OnCreateAtServer(Cancel, StandardProcessing)
    SetupConditionalAppearance();
EndProcedure

Procedure SetupConditionalAppearance()
    ConditionalAppearance = Report.SettingsComposer.Settings.ConditionalAppearance;
    ConditionalAppearance.Items.Clear();
    
    // Highlight negative amounts in red
    Item = ConditionalAppearance.Items.Add();
    Item.Use = True;  // ✅ Enabled
    
    // Condition: Amount < 0
    FilterItem = Item.Filter.Items.Add(Type("DataCompositionFilterItem"));
    FilterItem.LeftValue = New DataCompositionField("Amount");
    FilterItem.ComparisonType = DataCompositionComparisonType.Less;
    FilterItem.RightValue = 0;
    FilterItem.Use = True;  // ✅ Filter enabled
    
    // Appearance: Red text
    AppearanceItem = Item.Appearance.Items.Find("TextColor");
    AppearanceItem.Value = WebColors.Red;
    AppearanceItem.Use = True;  // ✅ Appearance enabled
    
    // Fields to format
    FieldItem = Item.Fields.Items.Add();
    FieldItem.Field = New DataCompositionField("Amount");
    FieldItem.Use = True;  // ✅ Field enabled
EndProcedure
```

---

## 📖 Conditional Appearance Components

### Structure Overview

```
ConditionalAppearance
├── Items[] - List of appearance rules
│   ├── Use - Boolean (must be True)
│   ├── Filter - Conditions when to apply
│   │   └── Items[]
│   │       ├── LeftValue - Field to check
│   │       ├── ComparisonType - How to compare
│   │       ├── RightValue - Value to compare
│   │       └── Use - Boolean (must be True)
│   ├── Appearance - Formatting to apply
│   │   └── Items[]
│   │       ├── Value - Format value
│   │       └── Use - Boolean (must be True)
│   └── Fields - Where to apply
│       └── Items[]
│           ├── Field - Target field
│           └── Use - Boolean (must be True)
```

### Common Patterns

| Scenario | Condition | Appearance |
|----------|-----------|------------|
| Negative amounts | Amount < 0 | TextColor = Red |
| Overdue dates | DueDate < Today | BackColor = LightCoral |
| Completed status | Status = Completed | TextColor = Gray |
| High priority | Priority = High | Font.Bold = True |
| Empty values | Value = "" | TextColor = LightGray |

---

## 📋 Examples by Use Case

### Highlight Negative Values

```bsl
Item = ConditionalAppearance.Items.Add();
Item.Use = True;

// Condition
Filter = Item.Filter.Items.Add(Type("DataCompositionFilterItem"));
Filter.LeftValue = New DataCompositionField("Balance");
Filter.ComparisonType = DataCompositionComparisonType.Less;
Filter.RightValue = 0;
Filter.Use = True;

// Red text
Item.Appearance.SetParameterValue("TextColor", WebColors.Red);

// Apply to Balance field
Field = Item.Fields.Items.Add();
Field.Field = New DataCompositionField("Balance");
Field.Use = True;
```

### Highlight Overdue Items

```bsl
Item = ConditionalAppearance.Items.Add();
Item.Use = True;

// Condition: DueDate < CurrentDate
Filter = Item.Filter.Items.Add(Type("DataCompositionFilterItem"));
Filter.LeftValue = New DataCompositionField("DueDate");
Filter.ComparisonType = DataCompositionComparisonType.Less;
Filter.RightValue = CurrentSessionDate();
Filter.Use = True;

// Background color
Item.Appearance.SetParameterValue("BackColor", WebColors.MistyRose);

// Apply to entire row
Field = Item.Fields.Items.Add();
Field.Use = True;  // No specific field = entire row
```

---

## 🔧 How to Fix

### Step 1: Review conditional appearance items

Check all `ConditionalAppearance.Items`:
- Is `Use = True`?
- Are filter conditions active?
- Are appearance settings active?

### Step 2: Enable or remove unused items

**If the rule should be active:**
```bsl
Item.Use = True;
FilterItem.Use = True;
AppearanceItem.Use = True;
FieldItem.Use = True;
```

**If the rule is not needed:**
```bsl
// Remove the item entirely
ConditionalAppearance.Items.Delete(Item);
```

### Step 3: Verify conditions are reachable

Ensure filter conditions can actually match data:
- Check field names are correct
- Verify comparison values are valid

---

## 🔍 Technical Details

### What Is Checked

1. Finds conditional appearance settings in DCS
2. Checks that `Use` property is True
3. Verifies filters and appearance are active
4. Reports disabled or unreachable appearance rules

### Check Implementation Class

```
com.e1c.v8codestyle.bsl.check.DataCompositionConditionalAppearanceUse
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.bsl/src/com/e1c/v8codestyle/bsl/check/
```

---

## 📚 References

- [Data Composition System Documentation](https://1c-dn.com/library/data_composition_system/)
- [Conditional Appearance in Reports](https://its.1c.ru/)
