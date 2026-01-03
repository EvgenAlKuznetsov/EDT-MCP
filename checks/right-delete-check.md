# Right set: Delete

## 📋 General Information

| Property | Value |
|----------|-------|
| **Check ID** | `right-delete` |
| **Category** | Role Rights |
| **Severity** | Critical |
| **Type** | Security |
| **Standard** | 1C:Enterprise Development Standards |

## 🎯 What This Check Does

Verifies the assignment of the `Delete` right in roles. The right to directly delete data should be limited to a minimum number of roles for security reasons. In most cases, the deletion mark mechanism should be used instead of direct deletion.

## ❌ Error Examples

```xml
<!-- Incorrect - Delete right enabled for regular user role -->
<Rights>
  <Right>
    <Name>Delete</Name>
    <Value>true</Value>
    <Object>Catalog.Products</Object>
  </Right>
</Rights>

<!-- Incorrect - Delete right without proper consideration -->
<Rights>
  <Right>
    <Name>Delete</Name>
    <Value>true</Value>
    <Object>Document.SalesOrder</Object>
  </Right>
</Rights>
```

## ✅ Compliant Solution

```xml
<!-- Correct - Delete right only for administrative role -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>Delete</Name>
    <Value>true</Value>
    <Object>Catalog.Products</Object>
  </Right>
</Rights>

<!-- Correct - Use deletion mark instead of direct delete -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>InteractiveSetDeletionMark</Name>
    <Value>true</Value>
    <Object>Catalog.Products</Object>
  </Right>
</Rights>
```

## 🔧 How to Fix

1. Remove the Delete right for regular user roles
2. Keep the Delete right only for administrative roles
3. Use the deletion mark mechanism for regular operations
4. Implement scheduled deletion of marked objects

## 🔍 Technical Details

- **Check class**: `com.e1c.v8codestyle.right.check.RightDelete`
- **Plugin**: `com.e1c.v8codestyle.right`

## 📚 References

- [Role-based access restriction](https://its.1c.ru/db/v8std/content/689/hdoc)
- [Deletion mark mechanism](https://1c-dn.com/library/deletion_mark/)
