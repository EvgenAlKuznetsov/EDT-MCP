# Query string literal contains non CamelCase content

## 📋 General Information

| Property | Value |
|----------|----------|
| **Check ID** | `ql-camel-case-string-literal` |
| **Category** | Query Language |
| **Severity** | Minor |
| **Type** | Code smell |
| **Standard** | 1C:Enterprise Development Standards |

## 🎯 What This Check Does

Checks that string literals in queries follow CamelCase style. This applies to field and table aliases.

## ❌ Error Examples

```bsl
// Incorrect - lowercase alias
Query.Text = "SELECT
    | Products.Name AS product_name
    |FROM
    | Catalog.Products AS Products";

// Incorrect - mixed case with underscore
Query.Text = "SELECT
    | Products.Code AS Product_Code
    |FROM
    | Catalog.Products AS products";
```

## ✅ Compliant Solution

```bsl
// Correct - CamelCase alias
Query.Text = "SELECT
    | Products.Name AS ProductName
    |FROM
    | Catalog.Products AS Products";

// Correct - proper CamelCase
Query.Text = "SELECT
    | Products.Code AS ProductCode
    |FROM
    | Catalog.Products AS Products";
```

## 🔧 How to Fix

1. Найдите все строковые литералы в запросе
2. Преобразуйте псевдонимы полей в формат CamelCase
3. Удалите подчёркивания между словами
4. Начинайте каждое слово с заглавной буквы

## 🔍 Technical Details

- **Check class**: `com.e1c.v8codestyle.ql.check.CamelCaseStringLiteral`
- **Plugin**: `com.e1c.v8codestyle.ql`

## 📚 References

- [1C:Enterprise Development Standards](https://its.1c.ru/db/v8std)
- [Query Language Documentation](https://1c-dn.com/library/query_language/)
