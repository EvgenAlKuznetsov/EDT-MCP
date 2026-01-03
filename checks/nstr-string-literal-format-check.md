# nstr-string-literal-format-check

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `nstr-string-literal-format-check` |
| **Title** | NStr string literal format check |
| **Description** | Checks that NStr function contains correctly formatted localization string |
| **Severity** | `MAJOR` |
| **Type** | `ERROR` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |

---

## 🎯 What This Check Does

This check validates that the string literal passed to the **`NStr()`** function follows the correct **localization format**. The NStr function requires a specific syntax with language codes and localized text.

### Why This Is Important

- **Localization failure**: Incorrect format prevents translation
- **Runtime errors**: NStr may fail to extract text
- **Standards compliance**: Platform requires specific format
- **Internationalization**: Proper format enables multi-language support

---

## ❌ Error Example

### Error Messages

```
NStr string literal has incorrect format
Missing language code in NStr string
Invalid language code format in NStr
```

### Noncompliant Code Example

```bsl
// ❌ Missing language code
Message(NStr("Save document?"));

// ❌ Wrong separator (using = instead of ')
Message(NStr("en=Save document?"));

// ❌ Missing quotes around value
Message(NStr("en = 'Save document?; ru = Сохранить документ?'"));

// ❌ Invalid language code
Message(NStr("english = 'Save document?'"));

// ❌ Missing semicolon between languages
Message(NStr("en = 'Save' ru = 'Сохранить'"));

// ❌ Unbalanced quotes
Message(NStr("en = 'Save document?"));

// ❌ Empty string
Message(NStr(""));

// ❌ Only language code, no text
Message(NStr("en = ''"));
```

---

## ✅ Compliant Solution

### Correct NStr Format

```bsl
// ✅ Single language
Message(NStr("en = 'Save document?'"));

// ✅ Multiple languages with semicolon separator
Message(NStr("en = 'Save document?'; ru = 'Сохранить документ?'"));

// ✅ Three or more languages
Message(NStr("en = 'Hello'; ru = 'Привет'; de = 'Hallo'"));

// ✅ With line breaks for readability
Message(NStr("en = 'Are you sure you want to delete this item?'; 
              |ru = 'Вы уверены, что хотите удалить этот элемент?'"));

// ✅ Empty text for specific language (intentional)
Message(NStr("en = 'Required'; ru = 'Обязательно'"));
```

---

## 📖 NStr Format Specification

### Basic Syntax

```
NStr("LanguageCode = 'LocalizedText'")
```

### Multiple Languages

```
NStr("LanguageCode1 = 'Text1'; LanguageCode2 = 'Text2'")
```

### Format Components

| Component | Description | Example |
|-----------|-------------|---------|
| Language Code | ISO 639-1 two-letter code | `en`, `ru`, `de`, `fr` |
| Equals Sign | Separator between code and text | `=` |
| Single Quotes | Wrap the localized text | `'text'` |
| Semicolon | Separator between languages | `;` |

### Valid Language Codes

| Code | Language |
|------|----------|
| `en` | English |
| `ru` | Russian |
| `de` | German |
| `fr` | French |
| `es` | Spanish |
| `it` | Italian |
| `pl` | Polish |
| `uk` | Ukrainian |
| `tr` | Turkish |
| `zh` | Chinese |

---

## 📋 Common Patterns

### Messages and Notifications

```bsl
// ✅ User message
ShowMessageBox(, NStr("en = 'Operation completed successfully'; 
                       |ru = 'Операция успешно завершена'"));

// ✅ Error message
Raise NStr("en = 'Access denied'; ru = 'Доступ запрещен'");

// ✅ Confirmation dialog
QueryText = NStr("en = 'Save changes?'; ru = 'Сохранить изменения?'");
```

### Form Elements

```bsl
// ✅ Form title
Items.DetailsGroup.Title = NStr("en = 'Details'; ru = 'Подробности'");

// ✅ Button caption
Items.SaveButton.Title = NStr("en = 'Save'; ru = 'Сохранить'");

// ✅ Column header
Items.NameColumn.Title = NStr("en = 'Name'; ru = 'Наименование'");
```

### Handling Special Characters

```bsl
// ✅ Apostrophe in text - double the quote
Message(NStr("en = 'Can''t save file'; ru = 'Невозможно сохранить файл'"));

// ✅ Line break in text
Message(NStr("en = 'Line 1
             |Line 2'; ru = 'Строка 1
             |Строка 2'"));
```

---

## 🔧 How to Fix

### Step 1: Check language code format

Use two-letter ISO language codes.

### Step 2: Verify equals sign usage

Use `=` with spaces around it.

### Step 3: Ensure quotes are balanced

Every opening `'` must have a closing `'`.

### Step 4: Add semicolons between languages

Separate multiple languages with `;`.

---

## 📋 Fixing Examples

### Example 1: Add Language Code

```bsl
// ❌ Before
NStr("Save document?")

// ✅ After
NStr("en = 'Save document?'")
```

### Example 2: Fix Separator

```bsl
// ❌ Before
NStr("en=Save; ru=Сохранить")

// ✅ After
NStr("en = 'Save'; ru = 'Сохранить'")
```

### Example 3: Balance Quotes

```bsl
// ❌ Before
NStr("en = 'Hello")

// ✅ After
NStr("en = 'Hello'")
```

### Example 4: Fix Language Code

```bsl
// ❌ Before
NStr("english = 'Hello'")

// ✅ After
NStr("en = 'Hello'")
```

---

## 📋 NStr Best Practices

### Do's

```bsl
// ✅ Always include at least one language
NStr("en = 'Text'")

// ✅ Use consistent formatting
NStr("en = 'Text'; ru = 'Текст'")

// ✅ Use multiline for long text
NStr("en = 'This is a long message that spans
     |multiple lines for readability';
     |ru = 'Это длинное сообщение, которое занимает
     |несколько строк для читаемости'")
```

### Don'ts

```bsl
// ❌ Don't use without language code
NStr("Just plain text")

// ❌ Don't mix formats
NStr("en='Text';ru = 'Текст'")

// ❌ Don't forget quotes
NStr("en = Text")
```

---

## 🔍 Technical Details

### What Is Checked

1. NStr function call parameters
2. String literal format validation
3. Language code validation
4. Quote balance verification
5. Semicolon placement

### Check Implementation Class

```
com.e1c.v8codestyle.bsl.check.NstrStringLiteralFormatCheck
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.bsl/src/com/e1c/v8codestyle/bsl/check/
```

---

## 📚 References

- [1C Documentation: NStr Function](https://1c-dn.com/library/nstr/)
- [1C Localization Guide](https://1c-dn.com/library/localization/)
- [ISO 639-1 Language Codes](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes)
