# EDT MCP Server

MCP (Model Context Protocol) server plugin for 1C:EDT, enabling AI assistants (Claude, GitHub Copilot, Cursor, etc.) to interact with EDT workspace.

## Features

- 🔧 **MCP Protocol 2025-11-25** - Streamable HTTP transport with SSE support
- 📊 **Project Information** - List workspace projects and configuration properties
- 🔴 **Error Reporting** - Get errors, warnings, problem summaries with filters
- 📝 **Check Descriptions** - Get check documentation from markdown files
- 🔄 **Project Revalidation** - Trigger revalidation when validation gets stuck
- 🔖 **Bookmarks & Tasks** - Access bookmarks and TODO/FIXME markers
- 🎯 **Status Bar** - Real-time server status indicator with request counter

## Installation

### From Update Site

1. In EDT: **Help → Install New Software...**
2. Add update site URL
3. Select **EDT MCP Server Feature**
4. Restart EDT

### Configuration

Go to **Window → Preferences → MCP Server**:
- **Server Port**: HTTP port (default: 8765)
- **Check descriptions folder**: Path to check description markdown files
- **Auto-start**: Start server on EDT launch

## Connecting AI Assistants

### VS Code / GitHub Copilot

Create `.vscode/mcp.json`:
```json
{
  "servers": {
    "EDT MCP Server": {
      "type": "sse",
      "url": "http://localhost:8765/mcp"
    }
  }
}
```

### Cursor IDE

Create `.cursor/mcp.json`:
```json
{
  "mcpServers": {
    "EDT MCP Server": {
      "url": "http://localhost:8765/mcp"
    }
  }
}
```

### Claude Desktop

Add to `claude_desktop_config.json`:
```json
{
  "mcpServers": {
    "EDT MCP Server": {
      "url": "http://localhost:8765/mcp"
    }
  }
}
```

## Available Tools

| Tool | Description |
|------|-------------|
| `get_edt_version` | Returns current EDT version |
| `list_projects` | Lists workspace projects with properties |
| `get_configuration_properties` | Gets 1C configuration properties |
| `get_project_errors` | Returns EDT problems with severity/checkId filters |
| `get_problem_summary` | Problem counts grouped by project and severity |
| `clean_project` | Cleans project markers and triggers full revalidation |
| `revalidate_objects` | Revalidates specific objects by FQN (e.g. "Document.MyDoc") |
| `get_bookmarks` | Returns workspace bookmarks |
| `get_tasks` | Returns TODO/FIXME task markers |
| `get_check_description` | Returns check documentation from .md files |
| `get_content_assist` | Get content assist proposals (type info, method hints) |

### Content Assist Tool

**`get_content_assist`** - Get content assist proposals at a specific position in BSL code. Returns type information, available methods, properties, and platform documentation.

**Parameters:**
| Parameter | Required | Description |
|-----------|----------|-------------|
| `projectName` | Yes | EDT project name |
| `filePath` | Yes | Path relative to `src/` folder (e.g. `CommonModules/MyModule/Module.bsl`) |
| `line` | Yes | Line number (1-based) |
| `column` | Yes | Column number (1-based) |
| `limit` | No | Maximum proposals to return (default: 50) |

**Important Notes:**
1. **Save the file first** - EDT must read the current content from disk to provide accurate proposals
2. **Column position** - Place cursor after the dot (`.`) for method/property suggestions
3. **Works for:**
   - Global platform methods (e.g. `NStr(`, `Format(`)
   - Methods after dot (e.g. `Structure.Insert`, `Array.Add`)
   - Object properties and fields
   - Configuration objects and modules

**Example - Get methods for Structure:**
```json
{
  "projectName": "MyProject",
  "filePath": "CommonModules/MyCommonModule/Module.bsl",
  "line": 15,
  "column": 12
}
```

If line 15 contains `MyStruct.` and cursor is after the dot, returns:
```json
{
  "success": true,
  "proposals": [
    {
      "displayString": "Insert(Key) ~ Structure",
      "documentation": "Procedure Structure.Insert(Key, [Value])..."
    },
    {
      "displayString": "Property(Key) : <Boolean> ~ Structure", 
      "documentation": "Function <Boolean> Structure.Property(Key, [ValueFound])..."
    }
  ]
}
```

### Validation Tools

- **`clean_project`**: Refreshes project from disk, clears all validation markers, and triggers full revalidation using EDT's ICheckScheduler
- **`revalidate_objects`**: Revalidates specific metadata objects by their FQN:
  - `Document.MyDocument`, `Catalog.MyCatalog`, `CommonModule.MyModule`
  - `Document.MyDoc.Form.MyForm` for nested objects

### Output Formats

- **Markdown tools**: `list_projects`, `get_project_errors`, `get_bookmarks`, `get_tasks`, `get_problem_summary`, `get_check_description` - return Markdown as EmbeddedResource with `mimeType: text/markdown`
- **JSON tools**: `get_configuration_properties`, `clean_project`, `revalidate_objects` - return JSON with `structuredContent`
- **Text tools**: `get_edt_version` - return plain text

## API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/mcp` | POST | MCP JSON-RPC (initialize, tools/list, tools/call) |
| `/mcp` | GET | Server info |
| `/health` | GET | Health check |

## Status Bar

Click the status indicator in EDT status bar:
- 🟢 Green - Server running
- ⚫ Grey - Server stopped
- **[N]** - Request counter

## Requirements

- 1C:EDT 2025.2 (Ruby) or later
- Java 17+

## Version History

### 1.6.16
- **New**: `get_content_assist` tool - Get content assist proposals at any code position
  - Returns type information, methods, properties with full platform documentation
  - Supports global methods and dot-notation methods
  - Uses EDT's ICompletionProposalExtension5 for async documentation retrieval
  - HTML documentation converted to Markdown using CopyDown library

### 1.6.10
- **Refactored**: All JSON responses now use Gson serialization instead of manual StringBuilder
- **New**: `ToolResult` class - fluent API for building JSON responses
- **Improved**: More reliable JSON output (no more missing braces or escaping issues)
- **Internal**: Uses `JsonParser.parseString()` for input JSON array parsing

### 1.6.9
- Fixed: `revalidate_objects` JSON response was missing closing brace causing parse error

### 1.6.8
- Fixed: `revalidate_objects` now uses 4-parameter scheduleValidation (without IBmTransaction) to avoid null transaction error

### 1.6.7
- Fixed: `revalidate_objects` now uses bmGetId() (Long) instead of URI - fixes NullPointerException in CheckScheduler

### 1.6.6
- Added: Custom PNG icons for Start (green), Stop (red), Restart (blue) buttons
- Improved: Additional defensive null filtering in revalidate_objects
- Added: Debug logging for object lookup in revalidate_objects

### 1.6.5
- Fixed: Port and checks folder changes now apply immediately when clicking Start/Restart (no need to click Apply first)
- Fixed: Restart button now has a distinct icon (Redo instead of disabled Synced)

### 1.6.4
- Fixed: `revalidate_objects` null URI handling (objects with null bmGetUri() are now skipped)
- Fixed: Full project revalidation when objects array is empty
- Improved: Service retrieval via Activator (not static fields)
- Added: JSON field `objectsSkippedNullUri` for objects with null URI

### 1.6.3
- Fixed: `clean_project` now uses Eclipse CLEAN_BUILD (same as Project → Clean menu)

### 1.6.2
- Fixed: `revalidate_objects` now passes object URI to scheduleValidation (was ClassCastException)

### 1.6.1
- Fixed: Array parameters parsing for `revalidate_objects` tool
- Fixed: Removed JSON duplication in text field (now only in structuredContent)
- Improved: FQN examples in tool descriptions

### 1.6.0
- **New**: `clean_project` tool - clears markers and triggers full revalidation via EDT ICheckScheduler
- **New**: `revalidate_objects` tool - revalidates specific objects by FQN
- Removed old `revalidate_project` (used Eclipse build, not EDT validation)
- Added ICheckScheduler and IBmModelManager service integration

### 1.5.1
- Dynamic file names for EmbeddedResource (e.g., `begin-transaction.md` instead of `tool-result`)
- Added `getResultFileName()` method to IMcpTool interface

### 1.5.0
- Explicit ResponseType per tool (TEXT, JSON, MARKDOWN)
- Markdown returned as EmbeddedResource with mimeType
- JSON returned with structuredContent support

### 1.4.0
- Converted list tools to Markdown output
- Fixed unused code warnings

### 1.3.0
- MCP Protocol 2025-11-25 with Streamable HTTP
- SSE transport support
- Session management with MCP-Session-Id header

### 1.2.0
- EDT IMarkerManager integration
- EDT severity levels (BLOCKER, CRITICAL, MAJOR, MINOR, TRIVIAL)

### 1.0.0
- Initial release

## License

Copyright (c) 2025 DitriX. All rights reserved.

---
*EDT MCP Server v1.6.16*
