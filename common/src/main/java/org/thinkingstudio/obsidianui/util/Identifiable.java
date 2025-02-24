/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of ObsidianUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.obsidianui.util;

import net.minecraft.util.Identifier;

/**
 * Represents something that can be identified.
 *
 * @author LambdAurora
 * @version 3.2.0
 * @since 3.2.0
 */
public interface Identifiable extends Nameable {
	/**
	 * Gets the identifier of this object.
	 *
	 * @return the identifier of this object
	 */
	Identifier getIdentifier();

	@Override
	default String getName() {
		return this.getIdentifier().getPath();
	}
}
