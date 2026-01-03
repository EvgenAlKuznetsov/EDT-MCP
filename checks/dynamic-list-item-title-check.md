# dynamic-list-item-title-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `dynamic-list-item-title-check` |
| **Title** | Dynamic list field title is empty |
| **Description** | Checks that dynamic list fields have filled titles |
| **Severity** | `MINOR` |
| **Type** | `CODE_SMELL` |
| **Complexity** | `TRIVIAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check identifies **dynamic list fields** that have **empty titles**. Each visible field in a dynamic list should have a clear, localized title.

### Why This Is Important

- **User experience**: Users see column names
- **Localization**: Titles should be translatable
- **Accessibility**: Screen readers use titles
- **Clarity**: Users understand what data is displayed

---

## ❌ Error Example

### Error Message

```
Title of field of dynamic list is not filled
```

### Noncompliant XML Configuration

```xml
<!-- ❌ Wrong: Dynamic list fields without titles -->
<Form.Form xmlns="...">
  <items>
    <FormTable name="List">
      <columns>
        <FormField>
          <name>Number</name>
          <title/>                               <!-- ❌ Empty title -->
          <dataPath>List.Number</dataPath>
        </FormField>
        <FormField>
          <name>Date</name>
          <title/>                               <!-- ❌ Empty title -->
          <dataPath>List.Date</dataPath>
        </FormField>
        <FormField>
          <name>Company</name>
          <!-- Missing title element -->          <!-- ❌ No title -->
          <dataPath>List.Company</dataPath>
        </FormField>
        <FormField>
          <name>Amount</name>
          <title/>                               <!-- ❌ Empty title -->
          <dataPath>List.Amount</dataPath>
        </FormField>
      </columns>
    </FormTable>
  </items>
</Form.Form>
```

### Resulting UI

| (empty) | (empty) | (empty) | (empty) |
|---------|---------|---------|---------|
| 001 | 01.01.2024 | ABC Corp | 1000.00 |
| 002 | 02.01.2024 | XYZ Inc | 2500.00 |

---

## ✅ Compliant Solution

### Correct XML Configuration

```xml
<!-- ✅ Correct: Dynamic list fields with proper titles -->
<Form.Form xmlns="...">
  <items>
    <FormTable name="List">
      <columns>
        <FormField>
          <name>Number</name>
          <title>
            <key>en</key>
            <value>Number</value>                <!-- ✅ Title filled -->
          </title>
          <dataPath>List.Number</dataPath>
        </FormField>
        <FormField>
          <name>Date</name>
          <title>
            <key>en</key>
            <value>Date</value>                  <!-- ✅ Title filled -->
          </title>
          <dataPath>List.Date</dataPath>
        </FormField>
        <FormField>
          <name>Company</name>
          <title>
            <key>en</key>
            <value>Company</value>               <!-- ✅ Title filled -->
          </title>
          <dataPath>List.Company</dataPath>
        </FormField>
        <FormField>
          <name>Amount</name>
          <title>
            <key>en</key>
            <value>Amount</value>                <!-- ✅ Title filled -->
          </title>
          <dataPath>List.Amount</dataPath>
        </FormField>
      </columns>
    </FormTable>
  </items>
</Form.Form>
```

### Filled Titles

```
Form: DocumentListForm
└── Items
    └── List (DynamicList)
        ├── Number (Title: "Number")          ✅ Title filled
        ├── Date (Title: "Date")              ✅ Title filled
        ├── Company (Title: "Company")        ✅ Title filled
        └── Amount (Title: "Amount")          ✅ Title filled
```

### Localized Titles

```
Form: DocumentListForm
└── Items
    └── List (DynamicList)
        ├── Number (Title: NStr("en='Number'; ru='Номер'"))
        ├── Date (Title: NStr("en='Date'; ru='Дата'"))
        ├── Company (Title: NStr("en='Company'; ru='Организация'"))
        └── Amount (Title: NStr("en='Amount'; ru='Сумма'"))
```

### Resulting UI

| Number | Date | Company | Amount |
|--------|------|---------|--------|
| 001 | 01.01.2024 | ABC Corp | 1000.00 |
| 002 | 02.01.2024 | XYZ Inc | 2500.00 |

---

## 📋 Best Practices for Titles

### Clear and Concise

```
// ✅ Good titles
"Number"
"Document Date"
"Customer Name"
"Total Amount"

// ❌ Avoid
"Doc_Number_Field"
"Field1"
"TheDocumentDateValue"
```

### Consistent Terminology

```
// ✅ Use consistent terms across forms
"Amount" (not sometimes "Sum", sometimes "Total")
"Date" (not sometimes "Document Date", sometimes "Created")
"Company" (not sometimes "Organization", sometimes "Firm")
```

### Localization

```
// ✅ Always use NStr for localization
Title = NStr("en = 'Customer'; ru = 'Покупатель'")

// ❌ Avoid hardcoded language
Title = "Покупатель"  // Only Russian
```

---

## 📋 When Title May Be Empty

### Intentionally Hidden Fields

Fields that are always hidden don't need titles:

```
Form: DocumentListForm
└── Items
    └── List (DynamicList)
        ├── Ref (Title: "", Visible: False)   // Hidden, OK
        ├── Number (Title: "Number", Visible: True)
        └── ...
```

### Technical Fields

Internal fields used only in code:

```
// Field used only for filtering, not displayed
RowID (Title: "", Visible: False)
```

---

## 🔧 How to Fix

### Step 1: Open form in Designer

Navigate to the form with the dynamic list.

### Step 2: Find fields with empty titles

Check each column in the dynamic list.

### Step 3: Set appropriate titles

Enter localized titles using NStr.

### Step 4: Verify in UI

Preview the form to confirm titles appear correctly.

---

## 📋 Setting Titles in Designer

### Form Designer Steps

1. Open form in Designer
2. Expand dynamic list in form items tree
3. Select field with empty title
4. In Properties panel, find "Title"
5. Enter title text or NStr expression

### Example Title Values

| Field | Title |
|-------|-------|
| `Number` | `NStr("en='Number'; ru='Номер'")` |
| `Date` | `NStr("en='Date'; ru='Дата'")` |
| `Company` | `NStr("en='Company'; ru='Организация'")` |
| `Counterparty` | `NStr("en='Counterparty'; ru='Контрагент'")` |
| `Amount` | `NStr("en='Amount'; ru='Сумма'")` |
| `Comment` | `NStr("en='Comment'; ru='Комментарий'")` |

---

## 📋 Automatic Title Inference

The platform can sometimes infer titles from metadata:

```
// If field corresponds to attribute with synonym
Attribute: Number (Synonym: "Document Number")
→ Column may inherit title

// But explicit title is always better
Column.Title = NStr("en='Doc Number'; ru='Номер документа'")
```

---

## 📋 Title vs Synonym

| Property | Purpose |
|----------|---------|
| **Attribute Synonym** | Default display name in metadata |
| **Column Title** | Specific display name in this form |

```
// Attribute may have synonym
Documents.Order.Attributes.Number.Synonym = "Order Number"

// But form column can have different title
ListForm.List.Number.Title = "№"  // Shorter for compact view
```

---

## 🔍 Technical Details

### What Is Checked

1. Dynamic list form items
2. Title property value
3. Visibility of the field

### Check Implementation Class

```
com.e1c.v8codestyle.form.check.DynamicListItemTitleCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.form/src/com/e1c/v8codestyle/form/check/
```

---

## 📚 References

- [1C Documentation: Dynamic Lists](https://1c-dn.com/library/dynamic_lists/)
- [Form Item Properties](https://1c-dn.com/library/form_items/)
- [Localization with NStr](nstr-string-literal-format-check.md)
