# new-font-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `new-font-check` |
| **Title** | Using the "New Font" construction |
| **Description** | Checks for direct font definitions instead of style elements |
| **Severity** | `MINOR` |
| **Type** | `CODE_SMELL` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check identifies usage of **`New Font`** constructor for setting fonts directly. Instead, you should use **style elements** to maintain consistent typography across the application.

### Why This Is Important

- **Consistency**: Same fonts used throughout the application
- **Maintainability**: Change fonts in one place (style)
- **Accessibility**: Centralized font management
- **Cross-platform**: Fonts may render differently on platforms

---

## ❌ Error Example

### Error Message

```
To change the font you should use style elements
```

### Noncompliant Code Example

```bsl
// ❌ Direct font creation
Procedure SetHeaderFont()
    Items.Title.Font = New Font(, 14, True); // ❌ Bold 14pt font
EndProcedure

// ❌ Hardcoded font properties
Procedure FormatTotal()
    Items.TotalAmount.Font = New Font("Arial", 12, True, False, False); 
    // ❌ Arial, 12pt, Bold
EndProcedure

// ❌ Conditional font formatting
Procedure HighlightImportant(IsImportant)
    If IsImportant Then
        Items.Status.Font = New Font(, , True); // ❌ Bold
    Else
        Items.Status.Font = New Font(, , False); // ❌ Regular
    EndIf;
EndProcedure
```

---

## ✅ Compliant Solution

### Using Style Elements

```bsl
// ✅ Using predefined style fonts
Procedure SetHeaderFont()
    Items.Title.Font = StyleFonts.LargeTextFont;
EndProcedure

// ✅ Using style for totals
Procedure FormatTotal()
    Items.TotalAmount.Font = StyleFonts.ImportantLabelFont;
EndProcedure

// ✅ Style-based conditional formatting
Procedure HighlightImportant(IsImportant)
    If IsImportant Then
        Items.Status.Font = StyleFonts.ImportantLabelFont;
    Else
        Items.Status.Font = StyleFonts.NormalTextFont;
    EndIf;
EndProcedure
```

---

## 📋 Available StyleFonts

### Common Style Fonts

| Font | Usage |
|------|-------|
| `StyleFonts.NormalTextFont` | Regular text |
| `StyleFonts.SmallTextFont` | Small/secondary text |
| `StyleFonts.LargeTextFont` | Headers, titles |
| `StyleFonts.ImportantLabelFont` | Emphasized labels |
| `StyleFonts.ExtraLargeTextFont` | Main headings |

### Example Usage

```bsl
// Header styling
Items.FormTitle.Font = StyleFonts.LargeTextFont;

// Regular text
Items.Description.Font = StyleFonts.NormalTextFont;

// Important totals
Items.GrandTotal.Font = StyleFonts.ImportantLabelFont;

// Secondary information
Items.Footnote.Font = StyleFonts.SmallTextFont;
```

---

## 📖 Font Constructor Parameters

### New Font Signature

```bsl
New Font([FaceName], [Size], [Bold], [Italic], [Underline], [Strikeout], [Scale])
```

### Example (What to Avoid)

```bsl
// ❌ All these should use StyleFonts instead
Font1 = New Font("Arial", 12); // Font face and size
Font2 = New Font(, 14, True); // Size 14, bold
Font3 = New Font(, , True, True); // Bold and italic
Font4 = New Font("Courier New", 10, False, False, True); // Underlined
```

---

## 🔧 How to Fix

### Step 1: Identify the purpose

Determine what the font represents:
- Header → `StyleFonts.LargeTextFont`
- Body text → `StyleFonts.NormalTextFont`
- Important → `StyleFonts.ImportantLabelFont`
- Small text → `StyleFonts.SmallTextFont`

### Step 2: Replace New Font with StyleFonts

```bsl
// Before
Items.Field.Font = New Font(, 14, True);

// After
Items.Field.Font = StyleFonts.LargeTextFont;
```

### Step 3: Create custom style if needed

If no suitable style font exists, add one to the configuration's styles.

---

## 📋 Font Purpose Mapping

| Purpose | Bad (New Font) | Good (StyleFonts) |
|---------|---------------|-------------------|
| Header | `New Font(, 16, True)` | `StyleFonts.LargeTextFont` |
| Body | `New Font(, 10)` | `StyleFonts.NormalTextFont` |
| Emphasis | `New Font(, , True)` | `StyleFonts.ImportantLabelFont` |
| Small | `New Font(, 8)` | `StyleFonts.SmallTextFont` |
| Title | `New Font(, 18, True)` | `StyleFonts.ExtraLargeTextFont` |

---

## 📖 Creating Custom Style Fonts

### When Standard Fonts Aren't Enough

If you need a font not in standard styles:

1. Open Configuration → Styles
2. Add new font style item
3. Set font properties
4. Reference in code

```bsl
// After adding to style
Items.SpecialField.Font = StyleFonts.MyCustomMonospaceFont;
```

---

## ⚠️ When Direct Fonts Might Be Acceptable

### 1. Report Generation

```bsl
// Reports may need specific font requirements
SpreadsheetDocument.Area("R1C1").Font = New Font("Courier New", 10);
```

### 2. Print Forms

```bsl
// Print layouts may have exact specifications
PrintFont = New Font("Times New Roman", 12);
```

### 3. Export to External Formats

```bsl
// When exporting to specific format requirements
ExportFont = New Font(RequiredFontName, RequiredSize);
```

---

## 📋 Benefits of Style Fonts

| Aspect | Direct Font | Style Font |
|--------|-------------|------------|
| Consistency | ❌ May vary | ✅ Uniform |
| Maintenance | ❌ Change everywhere | ✅ Change once |
| Theming | ❌ Hardcoded | ✅ Theme-aware |
| Accessibility | ❌ Fixed size | ✅ User preferences |
| Updates | ❌ Manual | ✅ Automatic |

---

## 🔍 Technical Details

### What Is Checked

1. All `New Font` constructor calls
2. Reports on each occurrence
3. Suggests using style elements

### Check Implementation Class

```
com.e1c.v8codestyle.bsl.check.NewFontCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.bsl/src/com/e1c/v8codestyle/bsl/check/
```

---

## 📚 References

- [New Color Check](new-color-check.md) - Similar check for colors
- [1C Style Elements](https://1c-dn.com/library/styles/)
- [UI Design Guidelines](https://1c-dn.com/library/ui_guidelines/)
