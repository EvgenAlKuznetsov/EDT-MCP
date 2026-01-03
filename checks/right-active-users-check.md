# Right set: Active Users

## 📋 General Information

| Property | Value |
|----------|----------|
| **Check ID** | `right-active-users` |
| **Category** | Role Rights |
| **Severity** | Major |
| **Type** | Security |
| **Standard** | 1C:Enterprise Development Standards |

## 🎯 What This Check Does

Checks assignment of `ActiveUsers` (Active users) right in roles. This right allows viewing list of active system users. Should be restricted to administrative roles for security reasons.

## ❌ Error Examples

```xml
<!-- Incorrect - ActiveUsers right for regular user -->
<Rights>
  <Right>
    <Name>ActiveUsers</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Incorrect - Every role has ActiveUsers -->
<!-- Role: Operator -->
<Rights>
  <Right>
    <Name>ActiveUsers</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## ✅ Compliant Solution

```xml
<!-- Correct - ActiveUsers only for admin roles -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>ActiveUsers</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Regular roles have no ActiveUsers right -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>ActiveUsers</Name>
    <Value>false</Value>
  </Right>
</Rights>
```

## 🔧 How to Fix

1. Снимите право ActiveUsers для обычных пользовательских ролей
2. Оставьте право только для административных ролей
3. Список активных пользователей — административная информация
4. При необходимости создайте отдельный отчёт с ограниченными данными

## 🔍 Technical Details

- **Check class**: `com.e1c.v8codestyle.right.check.RightActiveUsers`
- **Plugin**: `com.e1c.v8codestyle.right`

## 📚 References

- [System Rights](https://1c-dn.com/library/system_rights/)
- [Role-based access restriction](https://its.1c.ru/db/v8std/content/689/hdoc)
