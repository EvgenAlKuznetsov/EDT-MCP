# Right set: Interactive Delete

## 📋 General Information

| Property | Value |
|----------|-------|
| **Check ID** | `right-interactive-delete` |
| **Category** | Role Rights |
| **Severity** | Critical |
| **Type** | Security |
| **Standard** | 1C:Enterprise Development Standards |

## 🎯 What This Check Does

Verifies the assignment of the `InteractiveDelete` right in roles. This right allows users to delete objects directly through the interface. Should be limited to administrative roles only.

## ❌ Error Examples

```xml
<!-- Incorrect - InteractiveDelete right enabled for regular role -->
<Rights>
  <Right>
    <Name>InteractiveDelete</Name>
    <Value>true</Value>
    <Object>Catalog.Products</Object>
  </Right>
</Rights>

<!-- Incorrect - InteractiveDelete for documents -->
<Rights>
  <Right>
    <Name>InteractiveDelete</Name>
    <Value>true</Value>
    <Object>Document.Invoice</Object>
  </Right>
</Rights>
```

## ✅ Compliant Solution

```xml
<!-- Correct - InteractiveDelete only for Administrator role -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>InteractiveDelete</Name>
    <Value>true</Value>
    <Object>Catalog.Products</Object>
  </Right>
</Rights>

<!-- Correct - Use InteractiveSetDeletionMark for users -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>InteractiveSetDeletionMark</Name>
    <Value>true</Value>
    <Object>Catalog.Products</Object>
  </Right>
  <Right>
    <Name>InteractiveDelete</Name>
    <Value>false</Value>
    <Object>Catalog.Products</Object>
  </Right>
</Rights>
```

## 🔧 How to Fix

1. Remove the InteractiveDelete right for regular roles
2. Allow InteractiveDelete only for the Administrator role
3. For users, use the InteractiveSetDeletionMark right
4. Set up a scheduled procedure for deleting marked objects

## 🔍 Technical Details

- **Check class**: `com.e1c.v8codestyle.right.check.RightInteractiveDelete`
- **Plugin**: `com.e1c.v8codestyle.right`

## 📚 References

- [Role-based access restriction](https://its.1c.ru/db/v8std/content/689/hdoc)
- [User Rights](https://1c-dn.com/library/user_rights/)
