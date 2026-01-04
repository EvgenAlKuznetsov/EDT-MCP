/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.protocol;

import java.util.Map;

/**
 * JSON utility methods.
 */
public final class JsonUtils
{
    private JsonUtils()
    {
        // Utility class
    }
    
    /**
     * Escapes special characters for JSON string.
     * Note: Prefer using Gson for JSON serialization.
     * This method is kept for legacy compatibility.
     * 
     * @param s input string
     * @return escaped string
     */
    public static String escapeJson(String s)
    {
        if (s == null)
        {
            return ""; //$NON-NLS-1$
        }
        return s.replace("\\", "\\\\") //$NON-NLS-1$ //$NON-NLS-2$
                .replace("\"", "\\\"") //$NON-NLS-1$ //$NON-NLS-2$
                .replace("\n", "\\n") //$NON-NLS-1$ //$NON-NLS-2$
                .replace("\r", "\\r") //$NON-NLS-1$ //$NON-NLS-2$
                .replace("\t", "\\t"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Extracts a string argument from params map.
     * 
     * @param params the params map
     * @param argumentName the argument name to extract
     * @return value or null if not found
     */
    public static String extractStringArgument(Map<String, String> params, String argumentName)
    {
        if (params == null || argumentName == null)
        {
            return null;
        }
        return params.get(argumentName);
    }
}
