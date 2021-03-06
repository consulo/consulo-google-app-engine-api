/*
 * Copyright 2013-2016 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package consulo.google.appengine.java.sdk;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.execution.util.ExecUtil;
import com.intellij.openapi.projectRoots.JavaSdk;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import consulo.google.appengine.sdk.GoogleAppEngineSdkType;
import consulo.roots.types.BinariesOrderRootType;
import consulo.vfs.util.ArchiveVfsUtil;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class JavaAppSdkType extends GoogleAppEngineSdkType
{
	public static final String APPCFG = "appcfg";

	public JavaAppSdkType()
	{
		super("GOOGLE_APP_ENGINE_JAVA");
	}

	@Override
	public void setupSdkPaths(Sdk sdk)
	{
		SdkModificator sdkModificator = sdk.getSdkModificator();

		VirtualFile homeDirectory = sdk.getHomeDirectory();

		VirtualFile sharedDir = homeDirectory.findFileByRelativePath("lib/shared");

		if(sharedDir != null)
		{
			for(VirtualFile virtualFile : sharedDir.getChildren())
			{
				VirtualFile archiveRootForLocalFile = ArchiveVfsUtil.getArchiveRootForLocalFile(virtualFile);
				if(archiveRootForLocalFile != null)
				{
					sdkModificator.addRoot(archiveRootForLocalFile, BinariesOrderRootType.getInstance());
				}
			}
		}

		sdkModificator.commitChanges();
	}

	@Nonnull
	public static String getExecutable(String sdkHome, String file)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(sdkHome);
		builder.append(File.separator);
		builder.append("bin");
		builder.append(File.separator);
		builder.append(file);
		if(SystemInfo.isWindows)
		{
			builder.append(".cmd");
		}
		return builder.toString();
	}

	public static File getWebSchemeFile(@Nonnull Sdk sdk)
	{
		return getWebSchemeFile(sdk.getHomePath());
	}

	public static File getWebSchemeFile(@Nonnull String home)
	{
		return new File(FileUtil.toSystemDependentName(home + "/docs/appengine-web.xsd"));
	}

	@Nonnull
	@Override
	public String getLanguageName()
	{
		return "Java";
	}

	@Nonnull
	@Override
	public SdkType getLanguageSdkType()
	{
		return JavaSdk.getInstance();
	}

	@Nullable
	@Override
	public String getVersionString(String s)
	{
		try
		{
			ProcessOutput processOutput = ExecUtil.execAndGetOutput(Arrays.asList(getExecutable(s, APPCFG), "version"), s);
			List<String> stdoutLines = processOutput.getStdoutLines();
			if(stdoutLines.isEmpty())
			{
				return null;
			}
			String line = stdoutLines.get(0);
			if(!line.contains(":"))
			{
				return null;
			}
			List<String> split = StringUtil.split(line, ":");
			return split.get(1).trim();
		}
		catch(ExecutionException e)
		{
			return null;
		}
	}

	@Override
	public boolean isValidSdkHome(String path)
	{
		return new File(getExecutable(path, APPCFG)).exists();
	}
}
