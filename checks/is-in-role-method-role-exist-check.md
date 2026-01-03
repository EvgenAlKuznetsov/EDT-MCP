# is-in-role-method-role-exist-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `is-in-role-method-role-exist-check` |
| **Title** | Referring to a non-existent role |
| **Description** | Checks that role names used in IsInRole() exist in the configuration |
| **Severity** | `MAJOR` |
| **Type** | `ERROR` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check verifies that role names specified in **`IsInRole()`** method calls actually exist in the configuration. It prevents runtime errors from referencing non-existent roles.

### Why This Is Important

- **Runtime errors**: Non-existent role references cause exceptions
- **Typos detection**: Catches spelling mistakes in role names
- **Configuration integrity**: Ensures code matches actual configuration
- **Refactoring safety**: Detects broken references after role renaming

---

## ❌ Error Example

### Error Message

```
Role named {RoleName} not exists in configuration
```

### Noncompliant Code Example

```bsl
// ❌ Role name with typo
If IsInRole("FullAcess") Then // "FullAccess" misspelled
    ShowAdminPanel();
EndIf;

// ❌ Referencing deleted role
If IsInRole("OldManagerRole") Then // Role was deleted
    EnableFeature();
EndIf;

// ❌ Wrong role name
If IsInRole("Administrator") Then // Actual role name is "FullAccess"
    DoSomething();
EndIf;
```

---

## ✅ Compliant Solution

### Correct Code with Existing Roles

```bsl
// ✅ Correct role name (must exist in configuration)
If IsInRole("FullAccess") Then
    ShowAdminPanel();
EndIf;

// ✅ Using actual role names from configuration
If IsInRole("SalesManager") Then // Role exists
    EnableSalesFeatures();
EndIf;
```

### Better Approach - Use AccessRight

```bsl
// ✅ Recommended: Use AccessRight instead of IsInRole
If AccessRight("Administration", Metadata) Then
    ShowAdminPanel();
EndIf;

// ✅ Check specific object rights
If AccessRight("Read", Metadata.Documents.SalesOrder) Then
    EnableSalesFeatures();
EndIf;
```

---

## 🔍 Common Causes

### 1. Typos in Role Names

```bsl
// ❌ Common typos
IsInRole("FullAcess")      // Missing 'c'
IsInRole("Administartor")  // Letters swapped
IsInRole("FullRights")     // Wrong name (should be FullAccess)
```

### 2. Deleted Roles

```bsl
// Role "LegacyManager" was deleted during refactoring
If IsInRole("LegacyManager") Then // ❌ No longer exists
    // ...
EndIf;
```

### 3. Renamed Roles

```bsl
// Role was renamed from "Manager" to "SalesManager"
If IsInRole("Manager") Then // ❌ Old name
    // ...
EndIf;

// ✅ Use new name
If IsInRole("SalesManager") Then
    // ...
EndIf;
```

### 4. Case Sensitivity

```bsl
// Role names are case-sensitive
If IsInRole("fullaccess") Then // ❌ Wrong case
    // ...
EndIf;

If IsInRole("FullAccess") Then // ✅ Correct case
    // ...
EndIf;
```

---

## 🔧 How to Fix

### Step 1: Identify the error

Review the error message for the role name that doesn't exist.

### Step 2: Check configuration

Open Configuration > Roles and find the correct role name.

### Step 3: Fix the role name

Update the code with the correct role name.

### Step 4: Consider using AccessRight instead

```bsl
// Before (fragile)
If IsInRole("FullAccess") Then

// After (recommended)
If AccessRight("Administration", Metadata) Then
```

---

## 📋 Role Validation Process

### How the Check Works

1. **Finds IsInRole calls**: Scans code for `IsInRole()` method invocations
2. **Extracts role name**: Gets the string literal argument
3. **Validates existence**: Checks if role exists in Metadata.Roles
4. **Reports errors**: Flags non-existent role references

### What Can Be Checked

| Scenario | Checkable |
|----------|-----------|
| String literal role name | ✅ Yes |
| Variable role name | ❌ No (dynamic) |
| Concatenated role name | ❌ No (dynamic) |
| Role from function result | ❌ No (dynamic) |

```bsl
// ✅ Can be checked
IsInRole("FullAccess")

// ❌ Cannot be checked (dynamic)
RoleName = "FullAccess";
IsInRole(RoleName)
```

---

## 🔍 Technical Details

### What Is Checked

1. All `IsInRole()` method calls
2. String literal arguments only
3. Role existence in Metadata.Roles

### Check Implementation Class

```
com.e1c.v8codestyle.bsl.check.IsInRoleMethodRoleExistCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.bsl/src/com/e1c/v8codestyle/bsl/check/
```

---

## 📚 References

- [IsInRole Check](is-in-role-check.md) - Related check about using AccessRight
- [1C:Enterprise Roles Documentation](https://1c-dn.com/library/roles/)
