# md-standard-attribute-synonym-empty-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `md-standard-attribute-synonym-empty-check` |
| **Title** | Synonym of the 'Owner' or 'Parent' standard attribute is not specified |
| **Description** | Checks that standard attributes have synonyms specified |
| **Severity** | `MINOR` |
| **Type** | `CODE_SMELL` |
| **Complexity** | `TRIVIAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check identifies when **standard attributes** like **Owner** or **Parent** have **empty synonyms**. Standard attributes should have meaningful synonyms for better user experience.

### Why This Is Important

- **User experience**: Users see synonyms, not internal names
- **Localization**: Synonyms can be translated
- **Clarity**: Business terms instead of technical names
- **Forms/Reports**: Synonyms appear in headers

---

## ❌ Error Example

### Error Messages

```
Synonym of the 'Owner' standard attribute is not specified
Synonym of the 'Parent' standard attribute is not specified
```

### Noncompliant XML Configuration

```xml
<!-- ❌ Wrong: Standard attribute Owner without synonym -->
<mdclass:Catalog uuid="..." name="Products">
  <standardAttributes>
    <dataHistory>Use</dataHistory>
    <name>Owner</name>
    <!-- Missing <synonym> element -->              <!-- ❌ Synonym not specified -->
    <type>
      <types>CatalogRef.ProductCategories</types>
    </type>
  </standardAttributes>
</mdclass:Catalog>

<!-- ❌ Wrong: Standard attribute Parent with empty synonym -->
<mdclass:Catalog uuid="..." name="Departments">
  <standardAttributes>
    <name>Parent</name>
    <synonym/>                                      <!-- ❌ Empty synonym -->
    <type>
      <types>CatalogRef.Departments</types>
    </type>
  </standardAttributes>
</mdclass:Catalog>
```

---

## ✅ Compliant Solution

### Correct XML Configuration

```xml
<!-- ✅ Correct: Standard attribute with synonym filled -->
<mdclass:Catalog uuid="..." name="Products">
  <standardAttributes>
    <dataHistory>Use</dataHistory>
    <name>Owner</name>
    <synonym>
      <key>en</key>
      <value>Category</value>                       <!-- ✅ Meaningful synonym -->
    </synonym>
    <type>
      <types>CatalogRef.ProductCategories</types>
    </type>
  </standardAttributes>
</mdclass:Catalog>

<!-- ✅ Correct: With multiple languages -->
<mdclass:Catalog uuid="..." name="Departments">
  <standardAttributes>
    <name>Parent</name>
    <synonym>
      <key>en</key>
      <value>Parent Department</value>              <!-- ✅ Clear synonym -->
    </synonym>
    <synonym>
      <key>ru</key>
      <value>Родительское подразделение</value>     <!-- ✅ Localized -->
    </synonym>
    <type>
      <types>CatalogRef.Departments</types>
    </type>
  </standardAttributes>
</mdclass:Catalog>
```

### Fill Synonyms for Standard Attributes

```
Catalog: Products
├── Standard Attributes
│   ├── Owner
│   │   └── Synonym: "Category"      ✅
│   └── Parent
│       └── Synonym: "Parent Group"  ✅

Catalog: Departments
├── Standard Attributes
│   └── Parent
│       └── Synonym: "Parent Department"  ✅
```

### With Localization

```
Catalog: Products
├── Standard Attributes
│   ├── Owner
│   │   └── Synonym:
│   │       ├── en: "Category"
│   │       └── ru: "Категория"
│   └── Parent
│       └── Synonym:
│           ├── en: "Parent Group"
│           └── ru: "Родительская группа"
```

---

## 📋 Standard Attributes Explained

### Owner Attribute

```
// Owner is used for subordinate catalogs
Catalog: Products (Owner: Catalog.Categories)
├── Owner points to parent catalog
├── Used for filtering/grouping
└── Should have business-meaningful synonym

Default name: "Owner"
Better synonym: "Category", "Supplier", "Company" (context-dependent)
```

### Parent Attribute

```
// Parent is used for hierarchical catalogs
Catalog: ProductGroups (Hierarchical: True)
├── Parent points to parent group
├── Used for tree structure
└── Should describe hierarchy relationship

Default name: "Parent"
Better synonym: "Parent Group", "Parent Folder", "Upper Level"
```

---

## 📋 Context-Specific Synonyms

### Examples by Business Domain

| Catalog | Attribute | Appropriate Synonym |
|---------|-----------|---------------------|
| Products | Owner | Category |
| Products | Parent | Product Group |
| Employees | Owner | Department |
| Documents (subordinate) | Owner | Main Document |
| Tasks | Owner | Project |
| Folders | Parent | Parent Folder |

### Don't Use Generic Names

```
// ❌ Too generic
Synonym: "Owner"
Synonym: "Parent"

// ✅ Business-meaningful
Synonym: "Company"
Synonym: "Division"
Synonym: "Parent Category"
```

---

## 📋 Where Synonyms Appear

### In Forms

```
Form:
├── Group Header: "Details"
│   ├── Code
│   ├── Name
│   └── Category (← Owner synonym)
├── Tree View
│   └── Parent Group (← Parent synonym)
```

### In Reports

```
Report columns:
├── Product Name
├── Category (← Owner synonym)
└── Product Group (← Parent synonym from hierarchy)
```

### In Queries

```bsl
// Query result headers use synonyms
Query = New Query;
Query.Text = "SELECT
    |   Products.Ref,
    |   Products.Owner AS Category  // Synonym appears in QueryResult
    |FROM Catalog.Products AS Products";
```

---

## 🔧 How to Fix

### Step 1: Open catalog in Designer

Find the catalog with empty standard attribute synonyms.

### Step 2: Navigate to Standard Attributes

Open standard attributes configuration.

### Step 3: Set synonym for Owner/Parent

Enter appropriate business term.

### Step 4: Add translations

If multi-language, add all translations.

---

## 📋 Configuration Steps

### In Designer

```
1. Right-click catalog
2. Properties → Standard Attributes
3. Find Owner or Parent
4. Set Synonym property
5. Save changes
```

### In EDT

```
1. Open catalog
2. Expand Standard Attributes
3. Select Owner or Parent
4. Edit Synonym in Properties view
5. Save
```

---

## 📋 Other Standard Attributes

### Complete List to Consider

| Attribute | Description | Needs Synonym |
|-----------|-------------|---------------|
| Code | Item code | Usually OK as "Code" |
| Description | Item name | Usually OK as "Description" |
| Owner | Subordinate catalog owner | Yes - needs context |
| Parent | Hierarchical parent | Yes - needs context |
| DeletionMark | Deletion flag | Rarely shown |
| Predefined | Predefined item flag | Rarely shown |
| PredefinedDataName | Internal name | Rarely shown |

### Focus on Visible Ones

```
Most important to set synonyms:
├── Owner (when catalog is subordinate)
└── Parent (when catalog is hierarchical)

Less critical:
├── Code (usually fine as-is)
└── Description (usually fine as-is)
```

---

## 📋 Multi-Owner Catalogs

### When Multiple Owners

```
Catalog: Products
├── Owner: Company (Company synonym: "Company")
├── Owner: Warehouse (Warehouse synonym: "Warehouse")
// Each owner needs appropriate synonym
```

---

## 🔍 Technical Details

### What Is Checked

1. Catalogs with Owner attribute
2. Hierarchical catalogs (Parent attribute)
3. Standard attribute synonym property
4. Empty synonym detection

### Check Implementation Class

```
com.e1c.v8codestyle.md.check.MdStandardAttributeSynonymEmpty
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.md/src/com/e1c/v8codestyle/md/check/
```

---

## 📚 References

- [1C Documentation: Standard Attributes](https://1c-dn.com/library/standard_attributes/)
- [Subordinate Catalogs](https://1c-dn.com/library/subordinate_catalogs/)
- [Hierarchical Catalogs](https://1c-dn.com/library/hierarchical_catalogs/)
