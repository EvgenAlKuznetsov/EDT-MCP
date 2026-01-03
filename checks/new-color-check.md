# new-color-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `new-color-check` |
| **Title** | Using the "New Color" construction |
| **Description** | Checks for direct color definitions instead of style elements |
| **Severity** | `MINOR` |
| **Type** | `CODE_SMELL` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check identifies usage of **`New Color`** constructor for setting colors directly. Instead, you should use **style elements** to maintain consistent appearance across the application.

### Why This Is Important

- **Consistency**: Same colors used throughout the application
- **Maintainability**: Change colors in one place (style)
- **Theming**: Easier to support different visual themes
- **Accessibility**: Centralized color management for accessibility

---

## ❌ Error Example

### Error Message

```
To change the design, you should use style elements, and not set specific values directly in the controls. This is required in order for similar controls to look the same in all forms where they occur.
```

### Noncompliant Code Example

```bsl
// ❌ Direct color creation
Procedure SetItemColor()
    Items.TotalAmount.TextColor = New Color(255, 0, 0); // ❌ Direct red color
EndProcedure

// ❌ Hardcoded RGB values
Procedure HighlightRow()
    Items.Items.CurrentRow.BackColor = New Color(255, 255, 200); // ❌ Yellow background
EndProcedure

// ❌ Color in conditional formatting
Procedure FormatCell(Condition)
    If Condition Then
        Items.Status.TextColor = New Color(0, 128, 0); // ❌ Green
    Else
        Items.Status.TextColor = New Color(255, 0, 0); // ❌ Red
    EndIf;
EndProcedure
```

---

## ✅ Compliant Solution

### Using Style Elements

```bsl
// ✅ Using predefined style colors
Procedure SetItemColor()
    Items.TotalAmount.TextColor = StyleColors.ErrorTextColor;
EndProcedure

// ✅ Using style for highlighting
Procedure HighlightRow()
    Items.Items.CurrentRow.BackColor = StyleColors.SelectedFieldBackColor;
EndProcedure

// ✅ Style-based conditional formatting
Procedure FormatCell(Condition)
    If Condition Then
        Items.Status.TextColor = StyleColors.SuccessTextColor;
    Else
        Items.Status.TextColor = StyleColors.ErrorTextColor;
    EndIf;
EndProcedure
```

---

## 📋 Available StyleColors

### Common Style Colors

| Color | Usage |
|-------|-------|
| `StyleColors.ErrorTextColor` | Error messages |
| `StyleColors.WarningTextColor` | Warning messages |
| `StyleColors.SuccessTextColor` | Success indicators |
| `StyleColors.InformationTextColor` | Informational text |
| `StyleColors.HyperlinkColor` | Links |
| `StyleColors.FieldBackColor` | Field backgrounds |
| `StyleColors.FormBackColor` | Form backgrounds |
| `StyleColors.BorderColor` | Borders |
| `StyleColors.SelectedFieldBackColor` | Selected items |
| `StyleColors.AlternativeRowColor` | Alternate table rows |

### Example Usage

```bsl
// Error styling
Items.ErrorLabel.TextColor = StyleColors.ErrorTextColor;
Items.ErrorLabel.BackColor = StyleColors.ErrorBackColor;

// Success styling
Items.SuccessLabel.TextColor = StyleColors.SuccessTextColor;

// Selection highlighting
Items.DataTable.CurrentRowColor = StyleColors.SelectedFieldBackColor;
```

---

## 📖 Creating Custom Style Colors

### When Standard Colors Aren't Enough

If you need a color not in standard styles, add it to the configuration's style:

1. Open Configuration → Styles
2. Add new style item
3. Set the color value
4. Reference in code

```bsl
// After adding to style
Items.CustomField.TextColor = StyleColors.MyCustomHighlightColor;
```

---

## 🔧 How to Fix

### Step 1: Identify the purpose

Determine what the color represents:
- Error → `StyleColors.ErrorTextColor`
- Warning → `StyleColors.WarningTextColor`
- Success → `StyleColors.SuccessTextColor`
- Selection → `StyleColors.SelectedFieldBackColor`

### Step 2: Replace New Color with StyleColors

```bsl
// Before
Items.Field.TextColor = New Color(255, 0, 0);

// After
Items.Field.TextColor = StyleColors.ErrorTextColor;
```

### Step 3: Create custom style if needed

If no suitable style color exists, add one to the configuration.

---

## 📋 Color Purpose Mapping

| Purpose | Bad (New Color) | Good (StyleColors) |
|---------|----------------|-------------------|
| Error text | `New Color(255, 0, 0)` | `StyleColors.ErrorTextColor` |
| Success | `New Color(0, 128, 0)` | `StyleColors.SuccessTextColor` |
| Warning | `New Color(255, 165, 0)` | `StyleColors.WarningTextColor` |
| Disabled | `New Color(128, 128, 128)` | `StyleColors.InaccessibleDataColor` |
| Link | `New Color(0, 0, 255)` | `StyleColors.HyperlinkColor` |
| Highlight | `New Color(255, 255, 0)` | `StyleColors.SelectedFieldBackColor` |

---

## ⚠️ When Direct Colors Might Be Acceptable

### 1. Report Generation

```bsl
// Report colors might need specific values
SpreadsheetDocument.Area("R1C1").TextColor = New Color(128, 0, 0);
```

### 2. Chart Colors

```bsl
// Charts may need specific brand colors
Chart.Series[0].Color = New Color(0, 120, 215);
```

### 3. Export to External Formats

```bsl
// When exporting to specific format requirements
ExportColor = New Color(RequiredRed, RequiredGreen, RequiredBlue);
```

---

## 🔍 Technical Details

### What Is Checked

1. All `New Color` constructor calls
2. Reports on each occurrence
3. Suggests using style elements

### Check Implementation Class

```
com.e1c.v8codestyle.bsl.check.NewColorCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.bsl/src/com/e1c/v8codestyle/bsl/check/
```

---

## 📚 References

- [New Font Check](new-font-check.md) - Similar check for fonts
- [1C Style Elements](https://1c-dn.com/library/styles/)
- [UI Design Guidelines](https://1c-dn.com/library/ui_guidelines/)
