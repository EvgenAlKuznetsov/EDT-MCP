# query-in-loop-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `query-in-loop-check` |
| **Title** | Query in loop |
| **Description** | Checks for database queries executed inside loops |
| **Severity** | `CRITICAL` |
| **Type** | `PERFORMANCE` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |
| **1C Standard** | [735](https://its.1c.ru/db/v8std/content/735/hdoc) |

---

## 🎯 What This Check Does

This check identifies **database queries executed inside loops**. Executing queries in loops causes severe performance degradation due to repeated database roundtrips.

### Why This Is Important

- **Performance**: Each iteration causes database roundtrip
- **Scalability**: Performance degrades linearly with data size
- **Server load**: Excessive database connections
- **User experience**: Slow operations frustrate users

---

## ❌ Error Example

### Error Messages

```
Loop has query
```

```
Loop has method with query "{MethodName}"
```

### Noncompliant Code Example

```bsl
// ❌ Query in For Each loop
Procedure ProcessOrders(OrderRefs) Export
    For Each OrderRef In OrderRefs Do
        Query = New Query;
        Query.Text = "SELECT * FROM Document.SalesOrder WHERE Ref = &Ref";
        Query.SetParameter("Ref", OrderRef);
        Result = Query.Execute(); // ❌ Query in loop!
        ProcessOrderData(Result);
    EndDo;
EndProcedure

// ❌ Query in While loop
Procedure LoadItems(StartIndex, Count)
    Index = StartIndex;
    While Index < StartIndex + Count Do
        Query = New Query;
        Query.Text = "SELECT * FROM Catalog.Products WHERE Code = &Code";
        Query.SetParameter("Code", Format(Index, "ND=6; NZ="));
        Query.Execute(); // ❌ Query in loop!
        Index = Index + 1;
    EndDo;
EndProcedure

// ❌ Method with query called in loop
Procedure ProcessAllCustomers(Customers)
    For Each Customer In Customers Do
        Data = GetCustomerData(Customer); // ❌ Method contains query
    EndDo;
EndProcedure

Function GetCustomerData(CustomerRef)
    Query = New Query("SELECT * FROM Catalog.Customers WHERE Ref = &Ref");
    Query.SetParameter("Ref", CustomerRef);
    Return Query.Execute().Unload();
EndFunction
```

---

## ✅ Compliant Solution

### Solution 1: Single Query with IN Clause

```bsl
// ✅ One query for all items
Procedure ProcessOrders(OrderRefs) Export
    Query = New Query;
    Query.Text = 
        "SELECT * 
        |FROM Document.SalesOrder
        |WHERE Ref IN (&OrderRefs)";
    Query.SetParameter("OrderRefs", OrderRefs);
    
    Result = Query.Execute(); // ✅ Single query
    Selection = Result.Select();
    
    While Selection.Next() Do
        ProcessOrderData(Selection);
    EndDo;
EndProcedure
```

### Solution 2: Batch Query with Temporary Table

```bsl
// ✅ Using temporary table for complex scenarios
Procedure ProcessItems(ItemCodes) Export
    Query = New Query;
    Query.SetParameter("Codes", ItemCodes);
    Query.Text = 
        "SELECT Code INTO TempCodes FROM &Codes AS Codes
        |;
        |SELECT Products.*
        |FROM Catalog.Products AS Products
        |INNER JOIN TempCodes AS TempCodes
        |ON Products.Code = TempCodes.Code";
    
    Result = Query.Execute();
    // Process result
EndProcedure
```

### Solution 3: Pre-fetch Data

```bsl
// ✅ Load all data first, then process
Procedure ProcessAllCustomers(CustomerRefs) Export
    // Load all customer data at once
    AllCustomerData = GetAllCustomerData(CustomerRefs);
    
    // Process without database access
    For Each CustomerRef In CustomerRefs Do
        If AllCustomerData.Find(CustomerRef, "Ref") <> Undefined Then
            ProcessCustomer(AllCustomerData.Find(CustomerRef, "Ref"));
        EndIf;
    EndDo;
EndProcedure

Function GetAllCustomerData(CustomerRefs)
    Query = New Query;
    Query.Text = "SELECT * FROM Catalog.Customers WHERE Ref IN (&Refs)";
    Query.SetParameter("Refs", CustomerRefs);
    Return Query.Execute().Unload(); // ✅ Single query
EndFunction
```

### Solution 4: Using Map for Lookup

```bsl
// ✅ Build lookup map from single query
Procedure ProcessWithLookup(Items) Export
    // Build map from single query
    LookupMap = BuildProductLookup();
    
    // Use map for lookups (no database access)
    For Each Item In Items Do
        ProductData = LookupMap.Get(Item.ProductCode);
        If ProductData <> Undefined Then
            ProcessProduct(ProductData);
        EndIf;
    EndDo;
EndProcedure

Function BuildProductLookup()
    Query = New Query("SELECT Code, Description, Price FROM Catalog.Products");
    Result = Query.Execute().Unload();
    
    LookupMap = New Map;
    For Each Row In Result Do
        LookupMap.Insert(Row.Code, Row);
    EndDo;
    Return LookupMap;
EndFunction
```

---

## 📋 Performance Impact

### Comparison

| Approach | Items | Queries | Relative Time |
|----------|-------|---------|---------------|
| Query in loop | 1000 | 1000 | 100x slower |
| Single query with IN | 1000 | 1 | 1x (baseline) |

### Real-World Example

```
Processing 10,000 orders:
- Query in loop: ~100 seconds
- Single query: ~0.1 seconds
```

---

## 📖 Refactoring Patterns

### Pattern 1: Collect IDs First

```bsl
// ❌ Before
For Each Row In Table Do
    Data = QueryByRef(Row.ProductRef);
    Row.Price = Data.Price;
EndDo;

// ✅ After
ProductRefs = Table.UnloadColumn("ProductRef");
ProductData = QueryAllByRefs(ProductRefs);
ProductMap = BuildMap(ProductData, "Ref");

For Each Row In Table Do
    Data = ProductMap.Get(Row.ProductRef);
    Row.Price = Data.Price;
EndDo;
```

### Pattern 2: Join Instead of Lookup

```bsl
// ❌ Before - lookup in loop
For Each OrderRow In OrderRows Do
    Query = New Query("SELECT Price FROM Products WHERE Ref = &Ref");
    Query.SetParameter("Ref", OrderRow.Product);
    OrderRow.Price = Query.Execute().Unload()[0].Price;
EndDo;

// ✅ After - join in single query
Query = New Query;
Query.Text = 
    "SELECT Orders.*, Products.Price
    |FROM TempOrders AS Orders
    |LEFT JOIN Catalog.Products AS Products
    |ON Orders.Product = Products.Ref";
```

---

## 🔧 How to Fix

### Step 1: Identify the query in loop

Find queries inside For, While, or For Each loops.

### Step 2: Analyze the query pattern

Determine what data is being fetched and why.

### Step 3: Refactor to batch query

Use one of these approaches:
- `IN (&Array)` clause
- Temporary tables
- Pre-fetch with Map
- Query joins

### Step 4: Update processing logic

Modify code to work with batch results instead of individual queries.

---

## ⚠️ Hidden Query Loops

### Method Calls That Query

```bsl
// ❌ Method contains hidden query
For Each Item In Items Do
    Item.CategoryName = Item.Category.Description; // ❌ Lazy load!
EndDo;

// ✅ Eager load in query
Query = New Query(
    "SELECT Item.*, Category.Description AS CategoryName
    |FROM Items AS Item
    |LEFT JOIN Catalog.Categories AS Category
    |ON Item.Category = Category.Ref");
```

### Built-in Methods

Some platform methods also cause queries:
- `Reference.GetObject()`
- `Reference.Attribute` (lazy loading)
- `FindByCode()`, `FindByDescription()`

---

## 🔍 Technical Details

### What Is Checked

1. Query execution inside loops
2. Method calls that contain queries
3. For, For Each, While loops

### Check Implementation Class

```
com.e1c.v8codestyle.bsl.check.QueryInLoopCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.bsl/src/com/e1c/v8codestyle/bsl/check/
```

---

## 📚 References

- [1C:Enterprise Performance Standards - 735](https://its.1c.ru/db/v8std/content/735/hdoc)
- [Query Optimization](https://1c-dn.com/library/query_optimization/)
- [Database Performance](https://1c-dn.com/library/performance/)
