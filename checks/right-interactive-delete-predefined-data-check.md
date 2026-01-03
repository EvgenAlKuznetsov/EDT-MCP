# Right set: Interactive Delete Predefined Data

## 📋 General Information

| Property | Value |
|----------|-------|
| **Check ID** | `right-interactive-delete-predefined-data` |
| **Category** | Role Rights |
| **Severity** | Critical |
| **Type** | Security |
| **Standard** | 1C:Enterprise Development Standards |

## 🎯 What This Check Does

Verifies the assignment of the `InteractiveDeletePredefinedData` right in roles. This right is extremely dangerous as it allows deleting data defined by the developer in the configuration. Should be disabled for all roles except full rights.

## ❌ Error Examples

```xml
<!-- Incorrect - InteractiveDeletePredefinedData enabled -->
<Rights>
  <Right>
    <Name>InteractiveDeletePredefinedData</Name>
    <Value>true</Value>
    <Object>Catalog.Countries</Object>
  </Right>
</Rights>

<!-- Incorrect - Predefined data deletion for manager role -->
<Rights>
  <Right>
    <Name>InteractiveDeletePredefinedData</Name>
    <Value>true</Value>
    <Object>Catalog.PaymentMethods</Object>
  </Right>
</Rights>
```

## ✅ Compliant Solution

```xml
<!-- Correct - Predefined data deletion disabled -->
<Rights>
  <Right>
    <Name>InteractiveDeletePredefinedData</Name>
    <Value>false</Value>
    <Object>Catalog.Countries</Object>
  </Right>
</Rights>

<!-- Correct - Only FullRights role may have this -->
<!-- Role: FullRights (for maintenance purposes only) -->
<Rights>
  <Right>
    <Name>InteractiveDeletePredefinedData</Name>
    <Value>true</Value>
    <Object>Catalog.Countries</Object>
  </Right>
</Rights>
```

## 🔧 How to Fix

1. Remove the InteractiveDeletePredefinedData right for all roles
2. If necessary, allow only for the FullRights role
3. Predefined data should be managed by the developer
4. Use configuration updates to modify predefined data

## 🔍 Technical Details

- **Check class**: `com.e1c.v8codestyle.right.check.RightInteractiveDeletePredefinedData`
- **Plugin**: `com.e1c.v8codestyle.right`

## 📚 References

- [Predefined data](https://1c-dn.com/library/predefined_items/)
- [Role-based access restriction](https://its.1c.ru/db/v8std/content/689/hdoc)
