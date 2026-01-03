# Query cast to max number

## 📋 General Information

| Property | Value |
|----------|----------|
| **Check ID** | `ql-cast-to-max-number` |
| **Category** | Query Language |
| **Severity** | Major |
| **Type** | Code smell |
| **Standard** | 1C:Enterprise Development Standards |

## 🎯 What This Check Does

Checks correctness of numeric value casting in queries. It's not recommended to cast to maximum number length as this can lead to overflow or unexpected results.

## ❌ Error Examples

```bsl
// Incorrect - casting to maximum precision
Query.Text = "SELECT
    | CAST(Products.Quantity AS NUMBER(31, 15))
    |FROM
    | Catalog.Products AS Products";

// Incorrect - excessive number length
Query.Text = "SELECT
    | CAST(Document.Amount AS NUMBER(25, 10))
    |FROM
    | Document.Invoice AS Document";
```

## ✅ Compliant Solution

```bsl
// Correct - appropriate precision for the data type
Query.Text = "SELECT
    | CAST(Products.Quantity AS NUMBER(15, 3))
    |FROM
    | Catalog.Products AS Products";

// Correct - reasonable number length
Query.Text = "SELECT
    | CAST(Document.Amount AS NUMBER(15, 2))
    |FROM
    | Document.Invoice AS Document";
```

## 🔧 How to Fix

1. Проанализируйте фактический диапазон значений в поле
2. Определите оптимальную точность и разрядность
3. Используйте минимально достаточную длину числа
4. Учитывайте ограничения СУБД

## 🔍 Technical Details

- **Check class**: `com.e1c.v8codestyle.ql.check.CastToMaxNumber`
- **Plugin**: `com.e1c.v8codestyle.ql`

## 📚 References

- [1C:Enterprise Development Standards](https://its.1c.ru/db/v8std)
- [Query Language - Data Types](https://1c-dn.com/library/query_language/)
