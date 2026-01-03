# form-module-missing-pragma

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `form-module-missing-pragma` |
| **Title** | Always use compilation pragma in form module |
| **Description** | Form module methods should have compilation directives |
| **Severity** | `MINOR` |
| **Type** | `CODE_STYLE` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |
| **1C Standard** | [748](https://its.1c.ru/db/v8std/content/748/hdoc) |

---

## 🎯 What This Check Does

This check validates that **methods in form modules** have explicit **compilation directives** (pragmas) like `&AtServer`, `&AtClient`, `&AtServerNoContext`, etc.

### Why This Is Important

- **Explicit execution context**: Clear where code runs
- **Performance awareness**: Understand client/server calls
- **Error prevention**: Avoid runtime context errors
- **Code clarity**: Easier to understand data flow
- **Standards compliance**: Follows Standard 748

---

## ❌ Error Example

### Error Message

```
Missing compilation directives
```

**Russian:**
```
Отсутствуют директивы компиляции
```

### Noncompliant Code Example

```bsl
// Form module
// ❌ Methods without compilation directives

Procedure OnCreateAtServer(Cancel, StandardProcessing)
    // Server method - pragma is implied but should be explicit
EndProcedure

// ❌ No pragma - unclear where this runs
Procedure ProcessData()
    // What context does this run in?
EndProcedure

// ❌ No pragma
Function CalculateTotal()
    Return Items.Total("Amount");
EndFunction
```

---

## ✅ Compliant Solution

### Correct Code with Pragmas

```bsl
// Form module
// ✅ All methods have explicit compilation directives

&AtServer
Procedure OnCreateAtServer(Cancel, StandardProcessing)
    // Clearly runs on server
EndProcedure

&AtClient
Procedure ProcessDataClient()
    // Clearly runs on client
EndProcedure

&AtServer
Function CalculateTotal()
    // Clearly runs on server
    Return Items.Total("Amount");
EndFunction

&AtServerNoContext
Function GetSettingsFromServer()
    // Runs on server without form context
    Return CommonSettings.GetSettings();
EndFunction
```

---

## 📖 Compilation Directives

### Available Directives

| Directive | Context | Form Access |
|-----------|---------|-------------|
| `&AtClient` | Client | Yes |
| `&AtServer` | Server | Yes |
| `&AtServerNoContext` | Server | No |
| `&AtClientAtServerNoContext` | Both | No |
| `&AtClientAtServer` | Both | Yes (deprecated) |

### When to Use Each

| Use Case | Directive |
|----------|-----------|
| UI interactions | `&AtClient` |
| Database access | `&AtServer` |
| Database access (no form data needed) | `&AtServerNoContext` |
| Utility calculations | `&AtClientAtServerNoContext` |
| Form data modifications | `&AtServer` |

---

## 📋 Form Module Structure

### Standard Template

```bsl
#Region FormEventHandlers

&AtServer
Procedure OnCreateAtServer(Cancel, StandardProcessing)
    // Initialize form
EndProcedure

&AtClient
Procedure OnOpen(Cancel)
    // Handle form opening
EndProcedure

&AtClient
Procedure BeforeClose(Cancel, Exit, WarningText, StandardProcessing)
    // Handle form closing
EndProcedure

#EndRegion

#Region FormHeaderItemsEventHandlers

&AtClient
Procedure CustomerOnChange(Item)
    CustomerOnChangeAtServer();
EndProcedure

&AtServer
Procedure CustomerOnChangeAtServer()
    // Server-side processing for customer change
EndProcedure

#EndRegion

#Region FormCommandsEventHandlers

&AtClient
Procedure Calculate(Command)
    CalculateAtServer();
EndProcedure

&AtServer
Procedure CalculateAtServer()
    // Perform calculation
EndProcedure

#EndRegion

#Region Private

&AtClient
Procedure UpdateUIState()
    // Update UI elements
EndProcedure

&AtServer
Procedure LoadAdditionalData()
    // Load data from database
EndProcedure

&AtServerNoContext
Function GetCurrentUserSettings()
    Return Users.GetUserSettings();
EndFunction

&AtClientAtServerNoContext
Function FormatValue(Value)
    Return Format(Value, "NFD=2");
EndFunction

#EndRegion
```

---

## 📋 Choosing the Right Directive

### Decision Tree

```
Does the method access database?
├── Yes → Use server directive
│   └── Does it need form attributes?
│       ├── Yes → &AtServer
│       └── No  → &AtServerNoContext
│
└── No → Can it run on both client and server?
    ├── Yes → &AtClientAtServerNoContext
    └── No  → &AtClient
```

### Performance Considerations

| Pattern | Performance |
|---------|-------------|
| `&AtClient` only | Best (no server call) |
| `&AtServerNoContext` | Good (minimal data transfer) |
| `&AtServer` | More data transfer (form context) |
| Multiple `&AtServer` calls | Worst (multiple round trips) |

---

## 🔧 How to Fix

### Step 1: Identify methods without pragmas

Find all methods in form modules that don't have a compilation directive.

### Step 2: Determine appropriate directive

For each method, ask:
- Does it access database/server resources? → Server
- Does it need form attributes? → `&AtServer` vs `&AtServerNoContext`
- Is it UI-related? → `&AtClient`
- Is it pure calculation? → `&AtClientAtServerNoContext`

### Step 3: Add the directive

**Before:**
```bsl
Procedure DoSomething()
```

**After:**
```bsl
&AtServer
Procedure DoSomething()
```

### Step 4: Verify functionality

Test the form to ensure methods work correctly with the new directives.

---

## ⚠️ Common Mistakes

### Wrong: Mismatched Context

```bsl
&AtClient
Procedure LoadData()
    // ❌ This won't work - Query requires server context
    Query = New Query;
    Query.Text = "SELECT ...";
EndProcedure
```

### Wrong: Unnecessary Server Calls

```bsl
&AtServer
Procedure UpdateCounter()
    // ❌ This could be &AtClient - no server access needed
    Counter = Counter + 1;
EndProcedure
```

### Correct: Proper Separation

```bsl
&AtClient
Procedure ProcessButton()
    // Client-side UI handling
    If ValidateInput() Then
        ProcessAtServer();
    EndIf;
EndProcedure

&AtServer
Procedure ProcessAtServer()
    // Server-side database operations
    Object.Write();
EndProcedure
```

---

## 🔍 Technical Details

### What Is Checked

1. Finds methods in form modules
2. Checks for presence of compilation directive
3. Reports methods without directive

### Check Implementation Class

```
com.e1c.v8codestyle.bsl.check.FormModuleMissingPragmaCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.bsl/src/com/e1c/v8codestyle/bsl/check/
```

---

## 📚 References

- [1C:Enterprise Development Standards - Standard 748](https://its.1c.ru/db/v8std/content/748/hdoc)
- [Form Development Best Practices](https://1c-dn.com/library/forms/)
- [Client-Server Architecture](https://1c-dn.com/library/client_server/)
