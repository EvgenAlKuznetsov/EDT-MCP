/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;

import com.ditrix.edt.mcp.server.Activator;
import com.ditrix.edt.mcp.server.preferences.PreferenceConstants;
import com.ditrix.edt.mcp.server.protocol.JsonSchemaBuilder;
import com.ditrix.edt.mcp.server.protocol.JsonUtils;
import com.ditrix.edt.mcp.server.tools.IMcpTool;

/**
 * Tool to get check description by check ID.
 * Reads markdown files from the configured checks folder.
 */
public class GetCheckDescriptionTool implements IMcpTool
{
    public static final String NAME = "get_check_description"; //$NON-NLS-1$
    
    @Override
    public String getName()
    {
        return NAME;
    }
    
    @Override
    public String getDescription()
    {
        return "Get detailed description of an EDT check by its ID. " + //$NON-NLS-1$
               "Returns markdown content with check explanation, examples, and how to fix."; //$NON-NLS-1$
    }
    
    @Override
    public String getInputSchema()
    {
        return JsonSchemaBuilder.object()
            .stringProperty("checkId", "Check ID (e.g. 'begin-transaction', 'ql-temp-table-index')", true) //$NON-NLS-1$ //$NON-NLS-2$
            .build();
    }
    
    @Override
    public String getResultFileName(Map<String, String> params)
    {
        String checkId = JsonUtils.extractStringArgument(params, "checkId"); //$NON-NLS-1$
        if (checkId != null && !checkId.isEmpty())
        {
            return checkId + ".md"; //$NON-NLS-1$
        }
        return getName() + ".md"; //$NON-NLS-1$
    }
    
    @Override
    public String execute(Map<String, String> params)
    {
        String checkId = JsonUtils.extractStringArgument(params, "checkId"); //$NON-NLS-1$
        return getCheckDescription(checkId);
    }
    
    /**
     * Gets check description from the configured folder.
     * 
     * @param checkId the check ID
     * @return Markdown string with check description or error
     */
    public static String getCheckDescription(String checkId)
    {
        // Validate checkId parameter
        if (checkId == null || checkId.isEmpty())
        {
            return "**Error:** checkId parameter is required"; //$NON-NLS-1$
        }
        
        try
        {
            // Get checks folder from preferences
            IPreferenceStore store = Activator.getDefault().getPreferenceStore();
            String checksFolder = store.getString(PreferenceConstants.PREF_CHECKS_FOLDER);
            
            if (checksFolder == null || checksFolder.isEmpty())
            {
                return "**Error:** Check descriptions folder is not configured.\n\n" + //$NON-NLS-1$
                       "Please set it in Preferences -> MCP Server."; //$NON-NLS-1$
            }
            
            Path folderPath = Paths.get(checksFolder);
            if (!Files.exists(folderPath) || !Files.isDirectory(folderPath))
            {
                return "**Error:** Check descriptions folder does not exist: " + checksFolder; //$NON-NLS-1$
            }
            
            // Sanitize checkId to prevent path traversal
            String sanitizedCheckId = checkId.replaceAll("[^a-zA-Z0-9_-]", ""); //$NON-NLS-1$ //$NON-NLS-2$
            if (!sanitizedCheckId.equals(checkId))
            {
                return "**Error:** Invalid checkId format. Only alphanumeric characters, dashes and underscores are allowed."; //$NON-NLS-1$
            }
            
            // Try to find the file with .md extension
            Path checkFile = folderPath.resolve(checkId + ".md"); //$NON-NLS-1$
            
            if (!Files.exists(checkFile))
            {
                // Try lowercase version
                Path checkFileLower = folderPath.resolve(checkId.toLowerCase() + ".md"); //$NON-NLS-1$
                if (Files.exists(checkFileLower))
                {
                    checkFile = checkFileLower;
                }
                else
                {
                    return "**Error:** Check description not found for: " + checkId; //$NON-NLS-1$
                }
            }
            
            // Read and return file content directly (it's already Markdown)
            return Files.readString(checkFile, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            Activator.logError("Error reading check description for: " + checkId, e); //$NON-NLS-1$
            return "**Error:** Failed to read check description: " + e.getMessage(); //$NON-NLS-1$
        }
        catch (Exception e)
        {
            Activator.logError("Error getting check description", e); //$NON-NLS-1$
            return "**Error:** " + e.getMessage(); //$NON-NLS-1$
        }
    }
}
