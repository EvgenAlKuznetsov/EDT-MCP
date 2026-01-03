# Right set: Interactive Open External Reports

## 📋 General Information

| Property | Value |
|----------|-------|
| **Check ID** | `right-interactive-open-external-reports` |
| **Category** | Role Rights |
| **Severity** | Critical |
| **Type** | Security |
| **Standard** | 1C:Enterprise Development Standards |

## 🎯 What This Check Does

Verifies the assignment of the `InteractiveOpenExtReports` right in roles. Similar to external data processors, external reports can contain arbitrary code and pose a security threat. The right should be strictly limited.

## ❌ Error Examples

```xml
<!-- Incorrect - External reports for regular user -->
<Rights>
  <Right>
    <Name>InteractiveOpenExtReports</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Incorrect - Analyst can open external reports -->
<!-- Role: Analyst -->
<Rights>
  <Right>
    <Name>InteractiveOpenExtReports</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## ✅ Compliant Solution

```xml
<!-- Correct - Only Administrator can open external reports -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>InteractiveOpenExtReports</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Users use built-in reports only -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>InteractiveOpenExtReports</Name>
    <Value>false</Value>
  </Right>
</Rights>
```

## 🔧 How to Fix

1. Remove the right for all roles except Administrator
2. External reports can contain malicious code
3. Use the additional reports catalog from SSL
4. Verify and approve reports before adding them to the catalog

## 🔍 Technical Details

- **Check class**: `com.e1c.v8codestyle.right.check.RightInteractiveOpenExternalReports`
- **Plugin**: `com.e1c.v8codestyle.right`

## 📚 References

- [External Reports Security](https://its.1c.ru/db/metod8dev/content/5785/hdoc)
- [System Rights](https://1c-dn.com/library/system_rights/)
