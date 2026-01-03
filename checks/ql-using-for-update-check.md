# Using "FOR UPDATE" clause

## 📋 General Information

| Property | Value |
|----------|-------|
| **Check ID** | `ql-using-for-update` |
| **Category** | Query Language |
| **Severity** | Minor |
| **Type** | Code smell |
| **Standard** | 1C:Enterprise Development Standards |

## 🎯 What This Check Does

Verifies the use of the FOR UPDATE clause in queries. The FOR UPDATE clause is designed to lock certain data (available for reading from a transaction belonging to another connection) during reading to avoid deadlocks later when writing. Using FOR UPDATE is not recommended as it is not relevant in managed lock mode.

## ❌ Error Examples

```bsl
// Incorrect - using FOR UPDATE clause
Query.Text = "SELECT
    | Doc.Ref
    |FROM
    | Document.RetailSale AS Doc
    |WHERE
    | Doc.Ref = &DocumentRef
    |FOR UPDATE AccumulationRegister.MutualSettlementsByAgreement.Balance";

// Incorrect - FOR UPDATE with document
Query.Text = "SELECT
    | Invoice.Ref,
    | Invoice.Number
    |FROM
    | Document.Invoice AS Invoice
    |WHERE
    | Invoice.Posted = TRUE
    |FOR UPDATE Document.Invoice";

// Incorrect - FOR UPDATE with catalog
Query.Text = "SELECT
    | Products.Ref,
    | Products.Code
    |FROM
    | Catalog.Products AS Products
    |FOR UPDATE Catalog.Products";
```

## ✅ Compliant Solution

```bsl
// Correct - without FOR UPDATE (use managed locks)
Query.Text = "SELECT
    | Doc.Ref
    |FROM
    | Document.RetailSale AS Doc
    |WHERE
    | Doc.Ref = &DocumentRef";

// Correct - use managed lock mode in code
Query.Text = "SELECT
    | Invoice.Ref,
    | Invoice.Number
    |FROM
    | Document.Invoice AS Invoice
    |WHERE
    | Invoice.Posted = TRUE";

// Use DataLock for explicit locking
DataLock = New DataLock;
LockItem = DataLock.Add("AccumulationRegister.MutualSettlements");
LockItem.Mode = DataLockMode.Exclusive;
LockItem.SetValue("Document", DocumentRef);
DataLock.Lock();
```

## 🔧 How to Fix

1. Remove the FOR UPDATE clause from the query text
2. Switch the configuration to managed lock mode
3. Use the `DataLock` object for explicit data locking
4. Set locks programmatically before reading data

## 🔍 Technical Details

- **Check class**: `com.e1c.v8codestyle.ql.check.UsingForUpdateCheck`
- **Plugin**: `com.e1c.v8codestyle.ql`

## 📚 References

- [Using managed lock mode](https://support.1ci.com/hc/en-us/articles/360011002120-Using-managed-lock-mode)
