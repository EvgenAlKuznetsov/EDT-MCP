# db-object-max-number-length-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `db-object-max-number-length-check` |
| **Title** | Numeric DB field maximum length is 31 |
| **Description** | Checks that numeric fields in database objects do not exceed maximum length |
| **Severity** | `MAJOR` |
| **Type** | `ERROR` |
| **Complexity** | `TRIVIAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check identifies **numeric attributes** in metadata objects that have a **length exceeding 31 digits**. Numbers longer than 31 digits cannot be efficiently stored and processed.

### Why This Is Important

- **Database limits**: SQL databases have numeric precision limits
- **Performance**: Extremely large numbers slow operations
- **Practical use**: 31 digits covers virtually all business cases
- **Platform behavior**: May cause unexpected rounding

---

## ❌ Error Example

### Error Message

```
Numeric field "FieldName" length is more than 31
```

### Noncompliant XML Configuration

```xml
<!-- ❌ Wrong: Numeric precision exceeds 31 digits -->
<mdclass:Catalog uuid="..." name="Products">
  <attributes uuid="...">
    <name>ExtremelyLargeNumber</name>
    <type>
      <types>Number</types>
      <numberQualifiers>
        <precision>50</precision>              <!-- ❌ Too long: > 31 -->
        <scale>10</scale>
      </numberQualifiers>
    </type>
  </attributes>
</mdclass:Catalog>

<!-- ❌ Wrong: Document attribute with excessive precision -->
<mdclass:Document uuid="..." name="Order">
  <attributes uuid="...">
    <name>UnrealisticAmount</name>
    <type>
      <types>Number</types>
      <numberQualifiers>
        <precision>40</precision>              <!-- ❌ Too long: > 31 -->
        <scale>2</scale>
      </numberQualifiers>
    </type>
  </attributes>
</mdclass:Document>
```

---

## ✅ Compliant Solution

### Correct XML Configuration

```xml
<!-- ✅ Correct: Reasonable numeric precision -->
<mdclass:Catalog uuid="..." name="Products">
  <attributes uuid="...">
    <name>Quantity</name>
    <type>
      <types>Number</types>
      <numberQualifiers>
        <precision>15</precision>              <!-- ✅ Reasonable: ≤ 31 -->
        <scale>3</scale>
      </numberQualifiers>
    </type>
  </attributes>
</mdclass:Catalog>

<!-- ✅ Correct: Standard financial amount -->
<mdclass:Document uuid="..." name="Order">
  <attributes uuid="...">
    <name>Amount</name>
    <type>
      <types>Number</types>
      <numberQualifiers>
        <precision>15</precision>              <!-- ✅ Standard for amounts -->
        <scale>2</scale>
      </numberQualifiers>
    </type>
  </attributes>
</mdclass:Document>

<!-- ✅ Correct: Maximum allowed precision -->
<mdclass:AccumulationRegister uuid="..." name="BalanceOfGoods">
  <resources uuid="...">
    <name>Quantity</name>
    <type>
      <types>Number</types>
      <numberQualifiers>
        <precision>31</precision>              <!-- ✅ Maximum allowed -->
        <scale>10</scale>
      </numberQualifiers>
    </type>
  </resources>
</mdclass:AccumulationRegister>
```

### Use Appropriate Number Length

```
Catalog: Products
└── Attributes
    └── Quantity
        ├── Type: Number
        ├── Length: 15           ✅ Reasonable
        └── Precision: 3
        
Document: Order
└── Attributes
    └── Amount
        ├── Type: Number
        ├── Length: 15           ✅ Reasonable
        └── Precision: 2
```

---

## 📋 Understanding Number Length

### What Number Length Means

```
Length: Total number of digits (including decimal part)
Precision: Number of digits after decimal point

Example: Length=10, Precision=2
Maximum value: 99999999.99 (8 integer + 2 decimal = 10 total)
```

### Maximum Value by Length

| Length | Maximum Integer Value |
|--------|----------------------|
| 10 | 9,999,999,999 |
| 15 | 999,999,999,999,999 |
| 20 | 99,999,999,999,999,999,999 |
| 31 | 9,999,999,999,999,999,999,999,999,999,999 |

---

## 📋 Practical Guidelines

### Common Use Cases

| Use Case | Recommended Length |
|----------|-------------------|
| Quantity | 15 |
| Price | 15 |
| Amount | 15 |
| Percentage | 5-6 |
| Counter/ID | 10-15 |
| Currency rate | 15 |
| Weight/Volume | 15 |

### Why 15 Is Often Enough

```
15-digit integer: 999,999,999,999,999
= 999 trillion

For currency (2 decimal places):
9,999,999,999,999.99 = ~10 trillion

This exceeds most business scenarios!
```

---

## 📋 When Larger Numbers Are Needed

### Scientific Calculations

```
// For very large scientific numbers:
Length: 25-31 (if truly needed)
Precision: 10-15

// But consider: Do you really need to store this in DB?
// Maybe calculate on demand?
```

### Cryptocurrency/Special Cases

```
// For very small values (many decimals):
Catalog: CryptoAssets
└── Attributes
    └── SmallestUnit
        ├── Type: Number
        ├── Length: 20
        └── Precision: 18    // For Wei (Ethereum)
```

---

## 📋 Configuration Check

### Default Parameter

| Parameter | Default Value |
|-----------|---------------|
| Maximum Length | 31 |

### Customization

```
// In check settings:
maxLength = 31  // Default
// Can be adjusted if needed
```

---

## 🔧 How to Fix

### Step 1: Identify oversized fields

Find numeric attributes with length > 31.

### Step 2: Analyze actual data needs

Determine maximum value that will actually be stored.

### Step 3: Calculate appropriate length

```
Required digits = log10(max_value) + 1 + precision

Example: max 1 billion with 2 decimals
= 10 (for 1,000,000,000) + 2 (decimals) = 12
```

### Step 4: Update attribute

Reduce length to appropriate value.

---

## 📋 Migration Considerations

### Reducing Field Length

```
Before change:
1. Analyze existing data: SELECT MAX(FieldName) FROM ...
2. Verify max value fits in new length
3. Update metadata
4. Test thoroughly
```

### Example Query

```sql
-- Check maximum value in existing data
SELECT MAX(Amount), MIN(Amount), AVG(Amount)
FROM Document_Order
```

---

## 📋 Related Attributes

### Check All Numeric Attributes

```
Common places with numeric fields:
├── Catalog attributes
├── Document attributes
├── Register dimensions
├── Register resources
├── Tabular section columns
└── Form attributes (if persisted)
```

---

## 📋 Special Considerations

### Composite Types

```
// If attribute has composite type including Number:
Attribute: Value
├── Type: Number (Length: 50)   ❌ Check applies
└── Type: String (Length: 100)  // String part OK
```

### Calculated Fields

```
// Virtual fields don't need check
// Only stored fields matter
```

---

## 🔍 Technical Details

### What Is Checked

1. Catalog attributes (Type: Number)
2. Document attributes (Type: Number)
3. Register dimensions/resources (Type: Number)
4. Number length value

### Check Implementation Class

```
com.e1c.v8codestyle.md.check.DbObjectMaxNumberLengthCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.md/src/com/e1c/v8codestyle/md/check/
```

---

## 📚 References

- [1C Documentation: Primitive Types](https://1c-dn.com/library/primitive_types/)
- [Number Type Specification](https://1c-dn.com/library/number_type/)
- [Database Optimization](https://1c-dn.com/library/optimization/)
