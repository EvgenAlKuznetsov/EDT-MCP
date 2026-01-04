/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;

import com.ditrix.edt.mcp.server.Activator;

/**
 * Utility methods for EDT build jobs.
 */
public final class BuildUtils
{
    private static final String AUTOBUILD_FAMILY = "org.eclipse.core.resources.autoBuildFamily"; //$NON-NLS-1$
    
    private BuildUtils()
    {
        // Utility class
    }
    
    /**
     * Waits for all build jobs to complete.
     * 
     * @param monitor progress monitor
     */
    public static void waitForBuildJobs(IProgressMonitor monitor)
    {
        try
        {
            IJobManager jobManager = Job.getJobManager();
            // Wait for auto-build family
            jobManager.join(AUTOBUILD_FAMILY, monitor);
            // Also wait for any remaining build jobs
            jobManager.join(ResourcesPlugin.FAMILY_AUTO_BUILD, monitor);
            jobManager.join(ResourcesPlugin.FAMILY_MANUAL_BUILD, monitor);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            Activator.logError("Wait for build jobs interrupted", e); //$NON-NLS-1$
        }
    }
}
