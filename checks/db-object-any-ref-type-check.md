# db-object-any-ref-type-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `db-object-any-ref-type-check` |
| **Title** | Restrictions on the use of any ref type attributes |
| **Description** | Checks for usage of composite types like AnyRef, CatalogRef, DocumentRef for database-stored attributes |
| **Severity** | `MAJOR` |
| **Type** | `ERROR` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check identifies metadata object attributes that use **composite reference types** like `AnyRef`, `CatalogRef`, `DocumentRef`, etc. for attributes stored in the database.

### Why This Is Important

- **Query performance**: Composite types slow down queries
- **Index efficiency**: Cannot create efficient indexes
- **Data integrity**: Loose type allows invalid references
- **Maintainability**: Hard to understand what data is expected

---

## ❌ Error Example

### Error Message

```
Do not use composite types AnyRef, CatalogRef, DocumentRef, and other for standard metadata objects stored in the infobase.
```

### Noncompliant XML Configuration

```xml
<!-- ❌ Wrong: Using AnyRef - too generic -->
<mdclass:Document uuid="..." name="Order">
  <attributes uuid="...">
    <name>RelatedObject</name>
    <type>
      <types>AnyRef</types>                      <!-- ❌ Too generic -->
    </type>
  </attributes>
</mdclass:Document>

<!-- ❌ Wrong: Using generic DocumentRef -->
<mdclass:Catalog uuid="..." name="Products">
  <attributes uuid="...">
    <name>LinkedDocument</name>
    <type>
      <types>DocumentRef</types>                 <!-- ❌ All documents allowed -->
    </type>
  </attributes>
</mdclass:Catalog>

<!-- ❌ Wrong: Using generic CatalogRef -->
<mdclass:InformationRegister uuid="..." name="Sales">
  <dimensions uuid="...">
    <name>Source</name>
    <type>
      <types>CatalogRef</types>                  <!-- ❌ All catalogs allowed -->
    </type>
  </dimensions>
</mdclass:InformationRegister>
```

---

## ✅ Compliant Solution

### Correct XML Configuration

```xml
<!-- ✅ Correct: Using specific document reference type -->
<mdclass:Document uuid="..." name="Order">
  <attributes uuid="...">
    <name>RelatedOrder</name>
    <type>
      <types>DocumentRef.Order</types>           <!-- ✅ Specific type -->
    </type>
  </attributes>
</mdclass:Document>

<!-- ✅ Correct: Using specific catalog reference type -->
<mdclass:Catalog uuid="..." name="Products">
  <attributes uuid="...">
    <name>PrimarySupplier</name>
    <type>
      <types>CatalogRef.Counterparties</types>   <!-- ✅ Specific type -->
    </type>
  </attributes>
</mdclass:Catalog>

<!-- ✅ Correct: Multiple specific types when needed -->
<mdclass:Document uuid="..." name="Payment">
  <attributes uuid="...">
    <name>PaymentBasis</name>
    <type>
      <types>DocumentRef.Invoice</types>         <!-- ✅ Specific type 1 -->
      <types>DocumentRef.Order</types>           <!-- ✅ Specific type 2 -->
      <types>DocumentRef.Contract</types>        <!-- ✅ Specific type 3 -->
    </type>
  </attributes>
</mdclass:Document>
```

### Use Specific Reference Types

```
Document: Order
└── Attributes
    └── RelatedOrder
        └── Type: DocumentRef.Order     ✅ Specific type
        
Catalog: Products
└── Attributes
    └── PrimarySupplier
        └── Type: CatalogRef.Counterparties     ✅ Specific type
        
Register: Sales
└── Dimensions
    └── Product
        └── Type: CatalogRef.Products     ✅ Specific type
```

### Composite Type with Specific References

```
// When multiple specific types are needed:
Document: Payment
└── Attributes
    └── PaymentBasis
        └── Type:
            ├── DocumentRef.Invoice         ✅
            ├── DocumentRef.Order           ✅
            └── DocumentRef.Contract        ✅
            
// NOT: DocumentRef (all documents)
```

---

## 📋 Generic Types to Avoid

### Overly Generic Types

| Type | Problem |
|------|---------|
| `AnyRef` | Any reference in configuration |
| `CatalogRef` | Any catalog reference |
| `DocumentRef` | Any document reference |
| `ChartsOfCharacteristicTypesRef` | Any characteristic ref |
| `ChartsOfAccountsRef` | Any chart of accounts ref |
| `ExchangePlanRef` | Any exchange plan ref |

### Why They're Problematic

```
AnyRef can hold:
├── CatalogRef.Products
├── CatalogRef.Customers
├── DocumentRef.Order
├── DocumentRef.Invoice
├── InformationRegisterRecordKey...
└── (literally anything)

// Query cannot be optimized!
```

---

## 📋 Performance Impact

### Query Example

```bsl
// ❌ With AnyRef - slow query
Query = New Query;
Query.Text = 
    "SELECT * 
    |FROM Document.Order
    |WHERE RelatedObject = &Ref";
Query.SetParameter("Ref", ProductRef);
// Platform checks ALL possible types!
```

### Indexing Problem

```
// AnyRef attribute cannot be efficiently indexed
// because it stores references to different tables

// Specific type allows proper index:
CREATE INDEX ON Document_Order (RelatedOrder)
// vs AnyRef which needs complex union index
```

---

## 📋 Valid Use Cases for Generic Types

### Characteristic Attributes

```
// CharacteristicTypes - designed for flexibility
ChartOfCharacteristicTypes: AdditionalAttributes
└── ValueType: CatalogRef + DocumentRef + ...
// ✅ This is expected by design
```

### Common Module Parameters

```bsl
// ✅ In code, generic types are fine
Procedure ProcessAnyReference(Ref) Export
    // Works with any reference
    Name = Common.ObjectAttributeValue(Ref, "Description");
EndProcedure
```

### Temporary Storage

```bsl
// ✅ ValueTable column can be generic
Table = New ValueTable;
Table.Columns.Add("Reference"); // Any type OK
```

---

## 🔧 How to Fix

### Step 1: Identify affected attributes

Find attributes with generic reference types.

### Step 2: Analyze actual usage

Determine what types are actually stored.

### Step 3: Create specific type description

List only the types that are actually used.

### Step 4: Update attribute type

Change from generic to specific composite type.

---

## 📋 Migration Example

### Before

```
Attribute: LinkedObject
Type: DocumentRef (any document)
```

### After Analysis

```
// Check what's actually stored:
// SELECT DISTINCT VALUETYPE(LinkedObject) FROM ...
// Results: Invoice, Order, Contract
```

### After Fix

```
Attribute: LinkedObject
Type:
├── DocumentRef.Invoice
├── DocumentRef.Order
└── DocumentRef.Contract
```

---

## 📋 Exception Cases

### When Generic Types Are Acceptable

| Case | Reason |
|------|--------|
| Characteristic values | By design |
| Log/audit tables | Need to store any type |
| Subsystem for all objects | Generic handler |

### Suppressing the Check

```
// In check configuration, exclude specific attributes
// Or document why generic type is needed
```

---

## 🔍 Technical Details

### What Is Checked

1. Catalog attributes
2. Document attributes
3. Register dimensions and resources
4. Type descriptions containing generic refs

### Check Implementation Class

```
com.e1c.v8codestyle.md.check.DbObjectAnyRefTypeCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.md/src/com/e1c/v8codestyle/md/check/
```

---

## 📚 References

- [1C Documentation: Type Descriptions](https://1c-dn.com/library/type_description/)
- [Query Optimization](https://1c-dn.com/library/query_optimization/)
- [Db Object Ref Non Ref Types Check](db-object-ref-non-ref-types-check.md)
