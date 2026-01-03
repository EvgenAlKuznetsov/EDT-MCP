# Right set: Interactive Open External Data Processors

## 📋 General Information

| Property | Value |
|----------|-------|
| **Check ID** | `right-interactive-open-external-data-processors` |
| **Category** | Role Rights |
| **Severity** | Critical |
| **Type** | Security |
| **Standard** | 1C:Enterprise Development Standards |

## 🎯 What This Check Does

Verifies the assignment of the `InteractiveOpenExtDataProcessors` right in roles. This right is critical from a security standpoint as external data processors can contain arbitrary code. Should be strictly limited.

## ❌ Error Examples

```xml
<!-- Incorrect - External data processors for regular user -->
<Rights>
  <Right>
    <Name>InteractiveOpenExtDataProcessors</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Incorrect - Multiple roles can open external processors -->
<!-- Role: Operator -->
<Rights>
  <Right>
    <Name>InteractiveOpenExtDataProcessors</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## ✅ Compliant Solution

```xml
<!-- Correct - Only Administrator can open external processors -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>InteractiveOpenExtDataProcessors</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Users cannot open external processors -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>InteractiveOpenExtDataProcessors</Name>
    <Value>false</Value>
  </Right>
</Rights>
```

## 🔧 How to Fix

1. Remove the right for all roles except Administrator
2. External data processors can execute any code
3. Use the additional data processors catalog from SSL
4. Implement a mechanism for verifying and approving data processors

## 🔍 Technical Details

- **Check class**: `com.e1c.v8codestyle.right.check.RightInteractiveOpenExternalDataProcessors`
- **Plugin**: `com.e1c.v8codestyle.right`

## 📚 References

- [External Data Processors Security](https://its.1c.ru/db/metod8dev/content/5785/hdoc)
- [System Rights](https://1c-dn.com/library/system_rights/)
