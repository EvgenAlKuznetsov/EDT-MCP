# md-object-attribute-comment-not-exist-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `md-object-attribute-comment-not-exist-check` |
| **Title** | Md Object attribute "Comment" does not exist |
| **Description** | Checks that catalogs and documents have a "Comment" attribute for user notes |
| **Severity** | `MINOR` |
| **Type** | `CODE_SMELL` |
| **Complexity** | `TRIVIAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check identifies **catalogs and documents** that are missing a **"Comment"** attribute. The Comment attribute is a standard best practice for storing user notes and additional information.

### Why This Is Important

- **User experience**: Users often need to add notes
- **Flexibility**: Comment field stores arbitrary information
- **Standards compliance**: BSP and 1C standards recommend it
- **Audit trail**: Users can document changes/reasons

---

## ❌ Error Example

### Error Message

```
Md Object attribute "Comment" does not exist
```

### Noncompliant XML Configuration

```xml
<!-- ❌ Wrong: Catalog without Comment attribute -->
<mdclass:Catalog uuid="..." name="Products">
  <attributes uuid="...">
    <name>Code</name>
    <type><types>String</types></type>
  </attributes>
  <attributes uuid="...">
    <name>Name</name>
    <type><types>String</types></type>
  </attributes>
  <attributes uuid="...">
    <name>Price</name>
    <type><types>Number</types></type>
  </attributes>
  <!-- ❌ Missing Comment attribute -->
</mdclass:Catalog>

<!-- ❌ Wrong: Document without Comment attribute -->
<mdclass:Document uuid="..." name="Order">
  <attributes uuid="...">
    <name>Customer</name>
    <type><types>CatalogRef.Customers</types></type>
  </attributes>
  <!-- ❌ Missing Comment attribute -->
</mdclass:Document>
```

---

## ✅ Compliant Solution

### Correct XML Configuration

```xml
<!-- ✅ Correct: Catalog with Comment attribute -->
<mdclass:Catalog uuid="..." name="Products">
  <attributes uuid="...">
    <name>Code</name>
    <type><types>String</types></type>
  </attributes>
  <attributes uuid="...">
    <name>Name</name>
    <type><types>String</types></type>
  </attributes>
  <attributes uuid="...">
    <name>Price</name>
    <type><types>Number</types></type>
  </attributes>
  <!-- ✅ Comment attribute added -->
  <attributes uuid="..." name="Comment">
    <synonym>
      <key>en</key>
      <value>Comment</value>
    </synonym>
    <type>
      <types>String</types>
      <stringQualifiers/>             <!-- Unlimited length -->
    </type>
    <multiLine>true</multiLine>       <!-- Multiline editing -->
  </attributes>
</mdclass:Catalog>

<!-- ✅ Correct: Document with Comment attribute -->
<mdclass:Document uuid="..." name="Order">
  <attributes uuid="...">
    <name>Customer</name>
    <type><types>CatalogRef.Customers</types></type>
  </attributes>
  <!-- ✅ Comment attribute added -->
  <attributes uuid="..." name="Comment">
    <synonym>
      <key>en</key>
      <value>Comment</value>
    </synonym>
    <type>
      <types>String</types>
      <stringQualifiers/>
    </type>
    <multiLine>true</multiLine>
  </attributes>
</mdclass:Document>
```

### Add Comment Attribute

```
Catalog: Products
├── Attributes
│   ├── Name
│   ├── Code
│   ├── Price
│   └── Comment                   ✅
│       ├── Type: String
│       ├── Length: 0 (unlimited)
│       └── MultilineEdit: True
│
Document: Order
├── Attributes
│   ├── Date
│   ├── Number
│   ├── Customer
│   └── Comment                   ✅
│       ├── Type: String
│       ├── Length: 0 (unlimited)
│       └── MultilineEdit: True
```

---

## 📋 Standard Comment Attribute

### Recommended Configuration

| Property | Value |
|----------|-------|
| Name | Comment |
| Synonym | Comment |
| Type | String |
| Length | 0 (unlimited) |
| MultilineEdit | True |
| Indexing | Don't index |

### In Form

```
Form:
└── Items
    └── CommentGroup (Collapsible)
        └── Comment
            ├── Type: InputField
            ├── Height: 3 lines
            └── MultiLine: True
```

---

## 📋 When Comment Is Essential

### Primary Use Cases

| Object Type | Why Comment Is Needed |
|-------------|----------------------|
| Catalogs | Notes about items, special handling |
| Documents | Reason for document, special conditions |
| Business Processes | Instructions, additional context |
| Tasks | Details, clarifications |

### User Scenarios

```
// Product comment
"Discontinued item - sell remaining stock at 20% discount"

// Order comment  
"Customer requested expedited shipping - approved by manager"

// Customer comment
"VIP customer - always offer 10% discount"
```

---

## 📋 Configuration Parameters

### Check Parameters

| Parameter | Default | Description |
|-----------|---------|-------------|
| Check Catalogs | True | Check catalog for Comment |
| Check Documents | True | Check documents for Comment |
| Attribute Name List | "Comment" | Names to look for |

### Customization

```
// Different attribute name in your config:
attributeNameList = "Comment,Note,Remark"

// Check only specific object types:
checkCatalogs = True
checkDocuments = True
```

---

## 📋 Alternative Attribute Names

### Acceptable Alternatives

```
// Some configurations use different names:
Comment     ← Standard
Note        ← Alternative
Remark      ← Alternative  
Description ← Usually for different purpose
```

### Configuring Check

If your configuration uses "Note" instead of "Comment":

```
// Configure check to look for "Note":
attributeNameList = Note
```

---

## 📋 Objects That May Not Need Comment

### Technical Objects

```
// These may not need Comment attribute:
├── Settings (InformationRegister)
├── Sequences
├── System catalogs
├── Log registers
└── Temporary storage objects
```

### Subordinate Objects

```
// Tabular sections usually don't have Comment:
Document: Order
├── Comment: Yes (in header)        ✅
└── TabularSections
    └── Items
        └── (no Comment needed)     // OK
```

---

## 🔧 How to Fix

### Step 1: Open metadata object

Find the catalog or document without Comment.

### Step 2: Add new attribute

Create attribute named "Comment".

### Step 3: Configure attribute

- Type: String
- Length: 0 (unlimited)
- MultilineEdit: True

### Step 4: Add to forms

Add Comment field to object forms.

---

## 📋 Form Placement

### Object Form

```
Form Layout:
├── Header Group
│   ├── Number
│   ├── Date
│   └── ...
├── Details Group
│   └── ...
└── Comment Group (Collapsible)    ← At bottom
    └── Comment
```

### Best Practices

```
// Comment placement:
├── Usually at bottom of form
├── In collapsible group
├── Large enough for multiline
├── Spans full width
```

---

## 📋 List Form Considerations

### Show in List?

```
// Usually not in list:
List Form:
├── Code        ✓ Show
├── Name        ✓ Show
├── Price       ✓ Show
└── Comment     ✗ Don't show (too long)

// But can be added for filtering:
├── Comment (hidden, for search)
```

---

## 📋 Migration for Existing Data

### Adding Comment to Existing Object

```
// Simple addition - no data migration needed
1. Add Comment attribute (String, unlimited)
2. Update forms
3. Deploy
4. Existing objects have empty Comment
5. Users fill as needed
```

---

## 📋 Related Checks

| Check | Purpose |
|-------|---------|
| `md-object-attribute-comment-not-exist-check` | Comment exists |
| `md-object-attribute-comment-check` | Comment has correct type |

---

## 🔍 Technical Details

### What Is Checked

1. Catalogs (if enabled)
2. Documents (if enabled)
3. Presence of Comment attribute
4. Attribute name matching

### Check Implementation Class

```
com.e1c.v8codestyle.md.check.MdObjectAttributeCommentNotExistCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.md/src/com/e1c/v8codestyle/md/check/
```

---

## 📚 References

- [1C Standards: Standard Attributes](https://its.1c.ru/db/v8std)
- [BSP Guidelines](https://1c-dn.com/library/bsp/)
- [Md Object Attribute Comment Check](md-object-attribute-comment-check.md)
