# md-object-attribute-comment-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `md-object-attribute-comment-check` |
| **Title** | The attribute "Comment" has an invalid type |
| **Description** | Checks that the "Comment" attribute has the correct type for storing comments |
| **Severity** | `MINOR` |
| **Type** | `CODE_SMELL` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check validates that the standard **"Comment"** attribute in catalogs and documents has the correct type configuration:
- Type must be String
- Length should be unlimited
- Multiline edit should be enabled

### Why This Is Important

- **Data storage**: Comments can be long, need unlimited length
- **User experience**: Multiline comments need proper editing
- **Consistency**: Standard attribute should follow standards
- **Best practices**: Proper type avoids data truncation

---

## ❌ Error Example

### Error Messages

```
The attribute "Comment" has an invalid type: type is not a String
The attribute "Comment" has an invalid type: String must be of unlimited length
The attribute "Comment" has an invalid type: multiline edit is not enabled
The attribute "Comment" has an invalid type: attribute type is compound
```

### Noncompliant XML Configuration

```xml
<!-- ❌ Wrong: Fixed length 500 instead of unlimited -->
<mdclass:Attribute uuid="..." name="Comment">
  <type>
    <types>String</types>
    <stringQualifiers>
      <length>500</length>                    <!-- ❌ Should be 0 (unlimited) -->
    </stringQualifiers>
  </type>
  <multiLine>false</multiLine>                <!-- ❌ Should be true -->
</mdclass:Attribute>

<!-- ❌ Wrong: Number type instead of String -->
<mdclass:Attribute uuid="..." name="Comment">
  <type>
    <types>Number</types>                     <!-- ❌ Should be String -->
    <numberQualifiers>
      <precision>15</precision>
    </numberQualifiers>
  </type>
</mdclass:Attribute>

<!-- ❌ Wrong: Compound type -->
<mdclass:Attribute uuid="..." name="Comment">
  <type>
    <types>String</types>                     <!-- ❌ Multiple types -->
    <types>FormattedDocument</types>          <!-- ❌ Not allowed -->
    <stringQualifiers/>
  </type>
</mdclass:Attribute>
```

---

## ✅ Compliant Solution

### Correct XML Configuration

```xml
<!-- ✅ Correct: Unlimited length String with multiline enabled -->
<mdclass:Attribute uuid="..." name="Comment">
  <type>
    <types>String</types>
    <stringQualifiers/>                       <!-- Empty = unlimited length (0) -->
  </type>
  <multiLine>true</multiLine>                 <!-- ✅ Multiline editing enabled -->
</mdclass:Attribute>

<!-- ✅ Or with explicit length 0 -->
<mdclass:Attribute uuid="..." name="Comment">
  <type>
    <types>String</types>
    <stringQualifiers>
      <length>0</length>                      <!-- ✅ Explicit unlimited -->
    </stringQualifiers>
  </type>
  <multiLine>true</multiLine>
</mdclass:Attribute>
```

### Correct Comment Attribute Configuration

```
Catalog: Products
└── Attributes
    └── Comment
        ├── Type: String                 ✅
        ├── Length: 0 (unlimited)        ✅
        └── MultilineEdit: True          ✅
        
Document: Order
└── Attributes
    └── Comment
        ├── Type: String                 ✅
        ├── Length: 0 (unlimited)        ✅
        └── MultilineEdit: True          ✅
```

---

## 📋 Comment Attribute Requirements

### Required Settings

| Property | Required Value | Reason |
|----------|----------------|--------|
| Type | String | Text content |
| Length | 0 (unlimited) | Comments can be long |
| MultilineEdit | True | Comments often span lines |
| Compound Type | No | Single type only |

### Form Presentation

```
Form:
└── Items
    └── Comment (TextBox)
        ├── Height: 3+ lines
        ├── Wrap: True
        └── MultiLine: True
```

---

## 📋 Understanding Length Settings

### String Length Values

| Length Value | Meaning |
|--------------|---------|
| 0 | Unlimited (max varies by DB) |
| 1-1024 | Fixed length, stored in row |
| > 1024 | Stored in separate blob |

### Why Unlimited for Comments

```
// Fixed length problems:
Comment (Length: 500)
├── Long comment gets truncated
├── User loses data
└── No warning shown

// Unlimited (Length: 0):
Comment (Length: 0)
├── Any length accepted
├── Stored appropriately
└── No data loss
```

---

## 📋 Why MultilineEdit Matters

### Without MultilineEdit

```
┌────────────────────────────────────────────┐
│ This is a long comment that wraps but is  │
│ shown in single line input without proper  │ ← Hard to read/edit
│ scrolling...                               │
└────────────────────────────────────────────┘
```

### With MultilineEdit

```
┌────────────────────────────────────────────┐
│ This is a comment that spans              │
│ multiple lines for better                 │
│ readability and editing.                  │  ← Easy to read/edit
│                                           │
│ Can have paragraphs too.                  │
└────────────────────────────────────────────┘
```

---

## 📋 Configuration Parameters

### Check Parameters

| Parameter | Default | Description |
|-----------|---------|-------------|
| Check Catalogs | True | Check catalog Comment attributes |
| Check Documents | True | Check document Comment attributes |
| Attribute List | "Comment" | Attribute names to check |

### Customization

```
// Can configure which objects to check
checkCatalogs = True
checkDocuments = True
attributeNameList = "Comment,Note,Description"
```

---

## 🔧 How to Fix

### Step 1: Open metadata object

Find the catalog or document with Comment attribute.

### Step 2: Check attribute type

Ensure type is String, not compound.

### Step 3: Set length to 0

Change length from fixed value to 0 (unlimited).

### Step 4: Enable MultilineEdit

Set MultilineEdit = True in attribute properties.

---

## 📋 Form Considerations

### Input Field for Comment

```
Form:
└── Items
    └── Comment
        ├── Type: InputField
        ├── Height: 60 (or more)
        ├── Width: Auto
        ├── HorizontalStretch: True
        ├── VerticalStretch: True
        └── MultiLine: True
```

### Group for Comment

```
Form:
└── Items
    └── CommentGroup (Group)
        ├── Title: "Comment"
        ├── Collapsible: True
        └── Comment (InputField)
            └── TitleLocation: None
```

---

## 📋 Compound Type Warning

### Avoid Compound Types

```
// ❌ Wrong: Compound type for Comment
Comment
├── Type: String
└── Type: FormattedDocument   // Why?

// ✅ Correct: Single String type
Comment
└── Type: String
```

### If Rich Text Needed

```
// For formatted comments, use separate approach:
Attribute: CommentHTML (String, unlimited)
// OR
Attribute: CommentFormatted (ValueStorage) + FormattedDocument
```

---

## 📋 Related Attributes

### Similar Attributes to Check

| Attribute Name | Same Requirements |
|----------------|-------------------|
| Comment | String, unlimited, multiline |
| Note | String, unlimited, multiline |
| Description | May be fixed length if short |
| Remark | String, unlimited, multiline |

---

## 🔍 Technical Details

### What Is Checked

1. Catalogs (if enabled)
2. Documents (if enabled)
3. Attributes named "Comment" (or configured names)
4. Type must be String only
5. Length must be 0 (unlimited)
6. MultilineEdit must be True

### Check Implementation Class

```
com.e1c.v8codestyle.md.check.MdObjectAttributeCommentCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.md/src/com/e1c/v8codestyle/md/check/
```

---

## 📚 References

- [1C Documentation: String Type](https://1c-dn.com/library/string_type/)
- [Attribute Properties](https://1c-dn.com/library/attribute_properties/)
- [Md Object Attribute Comment Not Exist Check](md-object-attribute-comment-not-exist-check.md)
