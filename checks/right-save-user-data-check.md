# Right set: Save User Data

## 📋 General Information

| Property | Value |
|----------|-------|
| **Check ID** | `right-save-user-data` |
| **Category** | Role Rights |
| **Severity** | Minor |
| **Type** | Security |
| **Standard** | 1C:Enterprise Development Standards |

## 🎯 What This Check Does

Verifies the assignment of the `SaveUserData` right in roles. This right allows saving user personal settings: form settings, report variants, desktop. Typically should be allowed for most roles.

## ❌ Error Examples

```xml
<!-- Potentially incorrect - SaveUserData disabled for regular user -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>SaveUserData</Name>
    <Value>false</Value>
  </Right>
</Rights>

<!-- Incorrect - Inconsistent settings across roles -->
<!-- Role: Operator (should have this right) -->
<Rights>
  <Right>
    <Name>SaveUserData</Name>
    <Value>false</Value>
  </Right>
</Rights>
```

## ✅ Compliant Solution

```xml
<!-- Correct - SaveUserData enabled for regular users -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>SaveUserData</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Disabled only for special cases like kiosk mode -->
<!-- Role: KioskUser -->
<Rights>
  <Right>
    <Name>SaveUserData</Name>
    <Value>false</Value>
  </Right>
</Rights>

<!-- Correct - Enabled for all standard roles -->
<!-- Role: Manager -->
<Rights>
  <Right>
    <Name>SaveUserData</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## 🔧 How to Fix

1. Allow the SaveUserData right for most roles
2. Disable only for special cases (kiosks, terminals)
3. Saving settings improves user experience
4. Document cases where this right is disabled

## 🔍 Technical Details

- **Check class**: `com.e1c.v8codestyle.right.check.RightSaveUserData`
- **Plugin**: `com.e1c.v8codestyle.right`

## 📚 References

- [System Rights](https://1c-dn.com/library/system_rights/)
- [User Settings](https://1c-dn.com/library/user_settings/)
