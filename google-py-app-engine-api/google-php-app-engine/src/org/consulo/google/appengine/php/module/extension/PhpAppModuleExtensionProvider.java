/*
 * Copyright 2013 must-be.org
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

package org.consulo.google.appengine.php.module.extension;

import org.consulo.google.appengine.module.extension.GoogleAppEngineModuleExtensionProvider;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class PhpAppModuleExtensionProvider extends GoogleAppEngineModuleExtensionProvider<PhpAppModuleExtension, PhpAppMutableModuleExtension>
{
	@NotNull
	@Override
	public PhpAppModuleExtension createImmutable(@NotNull String s, @NotNull Module module)
	{
		return new PhpAppModuleExtension(s, module);
	}

	@NotNull
	@Override
	public PhpAppMutableModuleExtension createMutable(@NotNull String s, @NotNull Module module)
	{
		return new PhpAppMutableModuleExtension(s, module);
	}
}
