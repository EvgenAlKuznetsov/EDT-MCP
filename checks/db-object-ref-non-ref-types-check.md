# db-object-ref-non-ref-types-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `db-object-ref-non-ref-types-check` |
| **Title** | Restrictions on the use of composite type attributes |
| **Description** | Checks that composite type attributes used for joins/filters contain only reference types |
| **Severity** | `MAJOR` |
| **Type** | `ERROR` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check identifies **composite type attributes** that mix **reference types** with **non-reference types** (String, Number, Date, UUID, Boolean, ValueStorage). Such mixed types cause query performance issues.

### Why This Is Important

- **Query performance**: Mixed types prevent query optimization
- **Index usage**: Indexes cannot be used efficiently
- **Join operations**: Joins with mixed types are slow
- **Filtering**: WHERE clauses become inefficient

---

## ❌ Error Example

### Error Message

```
Composite type attributes used in join conditions, filters, and for ordering must contain only reference attribute types (CatalogRef. ..., DocumentRef. ..., and other). Do not include any other non-reference types in this type.
```

### Noncompliant XML Configuration

```xml
<!-- ❌ Noncompliant: Composite type mixes references with non-reference types -->
<mdclass:Document xmlns:mdclass="http://g5.1c.ru/v8/dt/metadata/mdclass"
                  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <name>Order</name>
  <attributes>
    <name>RelatedEntity</name>
    <type>
      <types>CatalogRef.Products</types>           <!-- ✓ Reference -->
      <types>DocumentRef.Invoice</types>           <!-- ✓ Reference -->
      <types>String</types>                         <!-- ❌ Non-reference! -->
      <stringQualifiers>
        <length>100</length>
      </stringQualifiers>
    </type>
  </attributes>
  <attributes>
    <name>Source</name>
    <type>
      <types>CatalogRef.Suppliers</types>          <!-- ✓ Reference -->
      <types>Boolean</types>                        <!-- ❌ Non-reference! -->
      <types>Date</types>                           <!-- ❌ Non-reference! -->
    </type>
  </attributes>
</mdclass:Document>
```

### Noncompliant Configuration

```
Document: Order
└── Attributes
    └── RelatedEntity
        └── Type:
            ├── CatalogRef.Products      ✓ Reference
            ├── DocumentRef.Invoice      ✓ Reference
            ├── String                   ❌ Non-reference!
            └── Number                   ❌ Non-reference!
        
Catalog: Items
└── Attributes
    └── Source
        └── Type:
            ├── CatalogRef.Suppliers     ✓ Reference
            ├── Boolean                  ❌ Non-reference!
            └── Date                     ❌ Non-reference!
```

---

## ✅ Compliant Solution

### Correct XML Configuration

```xml
<!-- ✅ Correct: Only reference types in composite attribute -->
<mdclass:Document xmlns:mdclass="http://g5.1c.ru/v8/dt/metadata/mdclass"
                  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <name>Order</name>
  <attributes>
    <name>RelatedEntity</name>
    <type>
      <types>CatalogRef.Products</types>           <!-- ✅ Reference only -->
      <types>DocumentRef.Invoice</types>           <!-- ✅ Reference only -->
    </type>
  </attributes>
  <!-- Separate attributes for different data types -->
  <attributes>
    <name>RelatedDocument</name>
    <type>
      <types>DocumentRef.Invoice</types>           <!-- ✅ References only -->
      <types>DocumentRef.Contract</types>          <!-- ✅ References only -->
    </type>
  </attributes>
  <attributes>
    <name>ExternalCode</name>
    <type>
      <types>String</types>                         <!-- ✅ Separate attribute -->
      <stringQualifiers>
        <length>100</length>
      </stringQualifiers>
    </type>
  </attributes>
</mdclass:Document>
```

### Reference Types Only

```
Document: Order
└── Attributes
    └── RelatedEntity
        └── Type:
            ├── CatalogRef.Products      ✅ Reference only
            └── DocumentRef.Invoice      ✅ Reference only
        
Catalog: Items
└── Attributes
    └── Supplier
        └── Type:
            └── CatalogRef.Suppliers     ✅ Single reference
```

### Separate Attributes for Different Types

```
Document: Order
└── Attributes
    ├── RelatedDocument
    │   └── Type:
    │       ├── DocumentRef.Invoice      ✅
    │       └── DocumentRef.Contract     ✅
    │
    └── ExternalCode
        └── Type: String                  ✅ Separate attribute
```

---

## 📋 Types to Avoid Mixing

### Non-Reference Types

| Type | Why Problematic in Mix |
|------|------------------------|
| `String` | Different storage, no foreign key |
| `Number` | Different comparison logic |
| `Date` | Different indexing |
| `Boolean` | Rarely needed in composite |
| `UUID` | Not a reference, just identifier |
| `ValueStorage` | Binary data, cannot index |

### Reference Types (OK Together)

| Type | Can Mix Together |
|------|------------------|
| `CatalogRef.X` | ✅ Yes |
| `DocumentRef.Y` | ✅ Yes |
| `EnumRef.Z` | ✅ Yes |
| `ChartOfAccountsRef.W` | ✅ Yes |

---

## 📋 Why This Causes Performance Issues

### Query Optimization Problem

```bsl
// Query with mixed-type attribute
Query = New Query;
Query.Text = 
    "SELECT *
    |FROM Document.Order AS O
    |LEFT JOIN Catalog.Products AS P
    |ON O.RelatedEntity = P.Ref";  // ❌ Slow!

// Platform must:
// 1. Check if RelatedEntity is a reference
// 2. Check if it matches Products type
// 3. Handle String/Number cases
// 4. Cannot use simple index lookup
```

### Index Problem

```
// With reference-only type:
Index on RelatedEntity → Direct lookup in reference table

// With mixed type:
Index on RelatedEntity → Must check type discriminator first
                       → Then lookup in appropriate table
                       → Or compare with primitive value
```

---

## 📋 Design Alternatives

### Alternative 1: Separate Attributes

```
// Instead of one mixed attribute:
Attribute: Entity (Ref + String)  ❌

// Use two attributes:
Attribute: EntityRef (Ref only)   ✅
Attribute: EntityCode (String)    ✅
```

### Alternative 2: Always Use Reference

```
// If storing external codes, create a catalog:
Catalog: ExternalCodes
├── Code: String
└── LinkedObject: Ref

// Then use reference:
Document.Order.ExternalCodeRef → CatalogRef.ExternalCodes
```

### Alternative 3: Characteristic Values

```
// For truly flexible types, use characteristics:
ChartOfCharacteristicTypes: Properties
└── ValueType: (various types allowed here by design)
```

---

## 📋 Exception Cases

### Characteristic Value Type

```
// CharacteristicTypes are designed for mixed types
ChartOfCharacteristicTypes: AdditionalAttributes
└── ValueType:
    ├── String           ✅ OK here
    ├── Number           ✅ OK here
    ├── Boolean          ✅ OK here
    └── CatalogRef.X     ✅ OK here

// This is the intended use case for characteristics
```

### Calculated/Virtual Attributes

```
// Non-stored attributes can have mixed types
// Only check stored database fields
```

---

## 🔧 How to Fix

### Step 1: Identify mixed-type attributes

Find attributes with both reference and non-reference types.

### Step 2: Analyze usage

Determine why different types are stored together.

### Step 3: Redesign attribute structure

- Split into separate attributes
- Create intermediate catalogs
- Use characteristics if appropriate

### Step 4: Update code

Modify queries and code to use new structure.

---

## 📋 Migration Example

### Before

```
Document: Payment
└── Attributes
    └── Source
        └── Type:
            ├── DocumentRef.Invoice
            ├── DocumentRef.Order
            └── String (external reference)
```

### After

```
Document: Payment
└── Attributes
    ├── SourceDocument
    │   └── Type:
    │       ├── DocumentRef.Invoice
    │       └── DocumentRef.Order
    │
    └── ExternalSourceCode
        └── Type: String
```

### Updated Query

```bsl
// Before
Query.Text = "SELECT * FROM Document.Payment WHERE Source = &Ref";

// After
Query.Text = "SELECT * FROM Document.Payment 
              |WHERE SourceDocument = &Ref 
              |   OR ExternalSourceCode = &Code";
```

---

## 🔍 Technical Details

### What Is Checked

1. Catalog attributes
2. Document attributes
3. Register dimensions and resources
4. Composite type analysis
5. Mix of reference and non-reference types

### Check Implementation Class

```
com.e1c.v8codestyle.md.check.DbObjectRefNonRefTypesCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.md/src/com/e1c/v8codestyle/md/check/
```

---

## 📚 References

- [1C Documentation: Composite Types](https://1c-dn.com/library/composite_types/)
- [Query Optimization](https://1c-dn.com/library/query_optimization/)
- [Db Object Any Ref Type Check](db-object-any-ref-type-check.md)
