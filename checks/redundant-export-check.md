# redundant-export-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `redundant-export-check` |
| **Title** | Redundant Export keyword |
| **Description** | Checks for export procedures and functions that are never called from outside the module |
| **Severity** | `MINOR` |
| **Type** | `CODE_SMELL` |
| **Complexity** | `COMPLEX` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check identifies **export procedures and functions** that are **never used** from outside their module. If a method is marked Export but isn't called externally, the Export keyword is redundant.

### Why This Is Important

- **API clarity**: Export should indicate public interface
- **Encapsulation**: Non-public methods should be private
- **Maintenance**: Clearer distinction between public/private
- **Code quality**: Reduces confusion about method visibility

---

## ❌ Error Example

### Error Message

```
Unused export method "{MethodName}"
```

### Noncompliant Code Example

```bsl
// ❌ Export method never called from outside
#Region Public

Procedure PublicMethod() Export // ❌ Never called externally
    InternalLogic();
EndProcedure

Function GetData() Export // ❌ Only called within this module
    Return LoadData();
EndFunction

#EndRegion

#Region Private

Procedure CallerMethod()
    PublicMethod(); // Only internal call
    Data = GetData(); // Only internal call
EndProcedure

#EndRegion
```

---

## ✅ Compliant Solution

### Remove Unnecessary Export

```bsl
// ✅ Methods without Export if not called externally
#Region Private

Procedure InternalMethod() // ✅ No Export - correct
    InternalLogic();
EndProcedure

Function GetData() // ✅ No Export - only used internally
    Return LoadData();
EndFunction

Procedure CallerMethod()
    InternalMethod();
    Data = GetData();
EndProcedure

#EndRegion
```

### Keep Export for Truly Public Methods

```bsl
// ✅ Export only for methods called externally
#Region Public

// Called from other modules
Procedure ProcessOrder(OrderRef) Export
    ValidateOrder(OrderRef);
    WriteOrder(OrderRef);
EndProcedure

// Called from forms
Function GetOrderData(OrderRef) Export
    Return LoadOrderData(OrderRef);
EndFunction

#EndRegion

#Region Private

// Internal helpers - no Export
Procedure ValidateOrder(OrderRef)
EndProcedure

Procedure WriteOrder(OrderRef)
EndProcedure

#EndRegion
```

---

## 📋 When Export Is Required

### External Callers

| Caller Type | Export Needed |
|-------------|---------------|
| Other modules | ✅ Yes |
| Forms (server methods) | ✅ Yes |
| Scheduled jobs | ✅ Yes |
| HTTP services | ✅ Yes |
| Event subscriptions | ✅ Yes |
| Command handlers | ✅ Yes |
| Only same module | ❌ No |

### Examples

```bsl
// ✅ Called from form
Function LoadFormData(FormParameters) Export
    // Called via form module
EndFunction

// ✅ Called from other common module
Procedure SendNotification(User, Message) Export
    // Called from various modules
EndProcedure

// ✅ Subscription handler
Procedure OnWriteDocument(Source, Cancel) Export
    // Registered in event subscriptions
EndProcedure

// ❌ Only used internally
Function FormatMessage(Template, Parameters) // No Export needed
    // Only called from this module
EndFunction
```

---

## 📖 Understanding Export Scope

### Module Types and Export

| Module Type | Export Visibility |
|-------------|------------------|
| Common module | Global (within server/client) |
| Object module | Via object reference |
| Manager module | Via manager reference |
| Form module | Limited (callbacks) |
| Command module | Not recommended |

### Common Module Example

```bsl
// Common module "Calculations"

// ✅ Called externally
Function CalculatePrice(Product, Quantity) Export
    Return Product.Price * Quantity;
EndFunction

// ❌ Redundant Export - only used internally
Function ApplyDiscount(Price, DiscountPercent) Export
    Return Price * (1 - DiscountPercent / 100);
EndFunction

// Calling code in same module
Function CalculateTotalWithDiscount(Product, Quantity, Discount) Export
    Price = CalculatePrice(Product, Quantity);
    Return ApplyDiscount(Price, Discount); // Internal call
EndFunction
```

---

## ⚙️ Check Parameters

| Parameter | Default | Description |
|-----------|---------|-------------|
| `excludeRegionNames` | `""` | Comma-separated list of region names to exclude |

### Configuration Example

```
# Exclude certain regions from check
excludeRegionNames = Interface, EventHandlers
```

---

## 🔧 How to Fix

### Step 1: Find all usages of the method

Use IDE's "Find All References" feature.

### Step 2: Determine if external calls exist

Check if method is called from:
- Other modules
- Forms
- Event subscriptions
- Scheduled jobs

### Step 3: Remove Export if not needed

```bsl
// Before
Procedure InternalHelper() Export

// After
Procedure InternalHelper()
```

### Step 4: Move to Private region

```bsl
#Region Private

Procedure InternalHelper()
    // Implementation
EndProcedure

#EndRegion
```

---

## ⚠️ False Positives

The check may incorrectly flag methods that are:

### 1. Called via Reflection

```bsl
// Called dynamically
Execute("ModuleName." + MethodName + "()");
```

### 2. Used in Event Subscriptions

```bsl
// Registered in configuration, not visible in code
Procedure OnWriteDocument(Source, Cancel) Export
```

### 3. Called from External Systems

```bsl
// Web service endpoint
Function GetData(Parameters) Export
```

### 4. Future API

```bsl
// Planned for use but not yet called
Procedure ReservedForFuture() Export
```

---

## 📋 Investigation Process

```
1. Search for method name in all modules
2. Check event subscriptions in configuration
3. Check scheduled jobs
4. Check HTTP/web services
5. Check form server methods
6. If no external usage found → remove Export
```

---

## 🔍 Technical Details

### What Is Checked

1. All Export procedures and functions
2. Searches for external references
3. Flags methods with no external calls

### Note on Performance

> **Warning**: This check searches for all references to methods across the entire configuration. It may take significant time to complete on large configurations.

### Check Implementation Class

```
com.e1c.v8codestyle.bsl.check.RedundantExportCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.bsl/src/com/e1c/v8codestyle/bsl/check/
```

---

## 📚 References

- [Module Unused Method Check](module-unused-method-check.md)
- [Export Procedure Missing Comment Check](export-procedure-missing-comment.md)
- [1C Module Structure](https://1c-dn.com/library/modules/)
