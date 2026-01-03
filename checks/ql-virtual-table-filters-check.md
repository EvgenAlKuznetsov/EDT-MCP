# Virtual table filters should be in parameters

## 📋 General Information

| Property | Value |
|----------|-------|
| **Check ID** | `ql-virtual-table-filters` |
| **Category** | Query Language |
| **Severity** | Major |
| **Type** | Performance |
| **Standard** | 1C:Enterprise Development Standards |

## 🎯 What This Check Does

Verifies that virtual table filter conditions are passed to table parameters, not to the WHERE clause. When using virtual tables in queries, pass all conditions related to the virtual table to table parameters. Using virtual tables with WHERE clause conditions is not recommended.

Such a query returns the correct result, but it is more difficult for the DBMS to choose the optimal query execution method. In some cases, this may lead to DBMS optimizer errors and significant slowdown of query execution.

## ❌ Error Examples

```bsl
// Incorrect - filter in WHERE section
Query.Text = "SELECT
    | Products
    |FROM
    | AccumulationRegister.Stock.Balance()
    |WHERE
    | Warehouse = &Warehouse";

// Incorrect - multiple filters in WHERE
Query.Text = "SELECT
    | Goods,
    | QuantityBalance
    |FROM
    | AccumulationRegister.GoodsInWarehouses.Balance()
    |WHERE
    | Warehouse = &Warehouse
    | AND Goods.Category = &Category";

// Incorrect - virtual table without parameters
Query.Text = "SELECT
    | Period,
    | QuantityTurnover
    |FROM
    | AccumulationRegister.Sales.Turnovers()
    |WHERE
    | Product = &Product
    | AND Period >= &StartDate";
```

## ✅ Compliant Solution

```bsl
// Correct - filter in table parameters
Query.Text = "SELECT
    | Products
    |FROM
    | AccumulationRegister.Stock.Balance(, Warehouse = &Warehouse)";

// Correct - all conditions in parameters
Query.Text = "SELECT
    | Goods,
    | QuantityBalance
    |FROM
    | AccumulationRegister.GoodsInWarehouses.Balance(
    |   ,
    |   Warehouse = &Warehouse
    |   AND Goods.Category = &Category)";

// Correct - parameters with date range
Query.Text = "SELECT
    | Period,
    | QuantityTurnover
    |FROM
    | AccumulationRegister.Sales.Turnovers(
    |   &StartDate,
    |   &EndDate,
    |   Auto,
    |   Product = &Product)";
```

## 🔧 How to Fix

1. Identify conditions related to the virtual table
2. Move these conditions from the WHERE clause to virtual table parameters
3. Use period parameters to restrict by date
4. Leave only conditions not related to the virtual table in WHERE

## 🔍 Technical Details

- **Check class**: `com.e1c.v8codestyle.ql.check.VirtualTableFiltersCheck`
- **Plugin**: `com.e1c.v8codestyle.ql`

## 📚 References

- [Accessing virtual table](https://support.1ci.com/hc/en-us/articles/360011121039-Accessing-virtual-table)
- [Using filters in queries with virtual tables](https://1c-dn.com/library/using_filters_in_queries_with_virtual_tables/)
- [Virtual table parameters](https://1c-dn.com/library/tutorials/practical_developer_guide_virtual_table_parameters3/)
