# data-exchange-load

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `data-exchange-load` |
| **Title** | DataExchange.Load check is missing |
| **Description** | Check that data exchange load flag is verified before executing business logic |
| **Severity** | `MAJOR` |
| **Type** | `ERROR` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |
| **1C Standard** | [773](https://its.1c.ru/db/v8std/content/773/hdoc) |

---

## 🎯 What This Check Does

This check validates that object modules properly check the `DataExchange.Load` flag before executing business logic in event handlers like `BeforeWrite`, `OnWrite`, `Posting`, etc.

### Why This Is Important

- **Data exchange compatibility**: Logic should be skipped during data exchange
- **Performance**: Avoids redundant validations during import
- **Data integrity**: Prevents blocking of replicated data
- **Standards compliance**: Required by Standard 773

---

## ❌ Error Example

### Error Message

```
DataExchange.Load check is missing
```

**Russian:**
```
Отсутствует проверка ОбменДанными.Загрузка
```

### Noncompliant Code Example

```bsl
// Document module
Procedure BeforeWrite(Cancel, WriteMode, PostingMode)
    // ❌ Missing DataExchange.Load check
    // This code runs even during data exchange, which is wrong
    
    If Not ValueIsFilled(Date) Then
        Cancel = True;
        Message("Date is required!");
    EndIf;
    
    If Not ValueIsFilled(Customer) Then
        Cancel = True;
        Message("Customer is required!");
    EndIf;
EndProcedure
```

---

## ✅ Compliant Solution

### Correct Code Pattern

```bsl
// Document module
Procedure BeforeWrite(Cancel, WriteMode, PostingMode)
    // ✅ Check DataExchange.Load first
    If DataExchange.Load Then
        Return;  // Skip business logic during data exchange
    EndIf;
    
    // Business logic only runs in normal mode
    If Not ValueIsFilled(Date) Then
        Cancel = True;
        Message("Date is required!");
    EndIf;
    
    If Not ValueIsFilled(Customer) Then
        Cancel = True;
        Message("Customer is required!");
    EndIf;
EndProcedure
```

---

## 📖 Understanding DataExchange.Load

### What Is DataExchange.Load

The `DataExchange.Load` property indicates that an object is being written as part of a data exchange (import) process, not through normal user interaction.

### When DataExchange.Load = True

- During data exchange/replication
- When loading from XML/JSON exchange files
- During data migration
- When restoring from backup

### Why Skip Business Logic

| Aspect | Normal Write | Data Exchange |
|--------|--------------|---------------|
| Validation | Required | Already validated at source |
| Calculations | Required | Already calculated at source |
| Related updates | Required | Will be loaded separately |
| Performance | Normal | Must be optimized |

---

## 📋 Event Handlers That Need Check

### Document Module

| Event | Needs Check | Reason |
|-------|-------------|--------|
| `BeforeWrite` | ✓ Yes | Skip validation |
| `OnWrite` | ✓ Yes | Skip related logic |
| `Posting` | ✓ Yes | Skip posting logic |
| `UndoPosting` | ✓ Yes | Skip undo logic |
| `OnCopy` | ✗ No | Not called during exchange |
| `Filling` | ✗ No | Not called during exchange |

### Catalog Module

| Event | Needs Check |
|-------|-------------|
| `BeforeWrite` | ✓ Yes |
| `OnWrite` | ✓ Yes |
| `BeforeDelete` | ✓ Yes |

### Register Module

| Event | Needs Check |
|-------|-------------|
| `BeforeWrite` | ✓ Yes |
| `OnWrite` | ✓ Yes |

---

## 📋 Code Patterns

### Standard Pattern

```bsl
Procedure BeforeWrite(Cancel)
    If DataExchange.Load Then
        Return;
    EndIf;
    
    // Your business logic here
EndProcedure
```

### With Minimal Required Logic

```bsl
Procedure BeforeWrite(Cancel)
    // Some logic MUST run even during exchange
    ThisObject.ModificationTime = CurrentSessionDate();
    
    If DataExchange.Load Then
        Return;
    EndIf;
    
    // Business logic that should be skipped
    ValidateDocument();
    CalculateTotals();
EndProcedure
```

### Posting Handler

```bsl
Procedure Posting(Cancel, PostingMode)
    If DataExchange.Load Then
        Return;
    EndIf;
    
    // Register movements
    RegisterRecords.InventoryMovements.Write = True;
    RegisterRecords.InventoryMovements.Clear();
    
    // ... create register records
EndProcedure
```

---

## ⚠️ Common Mistakes

### Wrong: Check in the Middle

```bsl
// ❌ Wrong: check is not at the beginning
Procedure BeforeWrite(Cancel)
    ValidateSomething();  // This runs during exchange!
    
    If DataExchange.Load Then
        Return;
    EndIf;
    
    ValidateMore();
EndProcedure
```

### Wrong: Logic After Check

```bsl
// ❌ Wrong: code after Return is unreachable
Procedure BeforeWrite(Cancel)
    If DataExchange.Load Then
        Return;
    EndIf;
    
    // Correct position for business logic
EndProcedure
```

---

## 🔧 How to Fix

### Step 1: Identify affected handlers

Find all event handlers in object modules:
- `BeforeWrite`
- `OnWrite`
- `Posting`
- `UndoPosting`
- `BeforeDelete`

### Step 2: Add DataExchange.Load check

Add at the very beginning of the handler:

```bsl
If DataExchange.Load Then
    Return;
EndIf;
```

### Step 3: Review existing logic

Move any logic that should run during exchange **before** the check:

```bsl
Procedure BeforeWrite(Cancel)
    // Logic that MUST run during exchange
    ThisObject.SystemField = ComputeValue();
    
    If DataExchange.Load Then
        Return;
    EndIf;
    
    // Logic that should NOT run during exchange
    ValidateBusinessRules();
EndProcedure
```

---

## 🔍 Technical Details

### What Is Checked

1. Finds BeforeWrite, OnWrite, Posting handlers
2. Checks for `DataExchange.Load` check
3. Verifies check is at the beginning
4. Reports if check is missing

### Check Implementation Class

```
com.e1c.v8codestyle.bsl.check.DataExchangeLoad
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.bsl/src/com/e1c/v8codestyle/bsl/check/
```

---

## 📚 References

- [1C:Enterprise Development Standards - Standard 773](https://its.1c.ru/db/v8std/content/773/hdoc)
- [Data Exchange Development](https://1c-dn.com/library/data_exchange/)
