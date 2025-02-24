/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of ObsidianUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.obsidianui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.thinkingstudio.obsidianui.Position;
import org.thinkingstudio.obsidianui.Tooltip;
import org.thinkingstudio.obsidianui.Tooltipable;
import org.thinkingstudio.obsidianui.util.ColorUtil;

import java.util.Optional;

/**
 * Represents a separator element.
 *
 * @author LambdAurora
 * @version 5.0.0
 * @since 1.0.1
 */
public class SpruceSeparatorWidget extends AbstractSpruceWidget implements Tooltipable {
	private final MinecraftClient client = MinecraftClient.getInstance();
	private Text title;
	private Text tooltip;
	private int tooltipTicks;
	private long lastTick;

	public SpruceSeparatorWidget(Position position, int width, @Nullable Text title) {
		super(position);
		this.width = width;
		this.height = 9;
		this.title = title;
	}

	@Deprecated
	public SpruceSeparatorWidget(@Nullable Text title, int x, int y, int width) {
		this(Position.of(x, y), width, title);
	}

	/**
	 * Gets the title of this separator widget.
	 *
	 * @return the title
	 */
	public Optional<Text> getTitle() {
		return Optional.ofNullable(this.title);
	}

	/**
	 * Sets the title of this separator widget.
	 *
	 * @param title the title
	 */
	public void setTitle(@Nullable Text title) {
		this.title = title;
	}

	@Override
	public Optional<Text> getTooltip() {
		return Optional.ofNullable(this.tooltip);
	}

	@Override
	public void setTooltip(@Nullable Text tooltip) {
		this.tooltip = tooltip;
	}

	/* Navigation */

	@Override
	public boolean requiresCursor() {
		return this.tooltip == null;
	}

	/* Rendering */

	@Override
	protected void renderWidget(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		if (this.title != null) {
			int titleWidth = this.client.textRenderer.getWidth(this.title);
			int titleX = this.getX() + (this.getWidth() / 2 - titleWidth / 2);
			if (this.width > titleWidth) {
				drawContext.fill(this.getX(), this.getY() + 4, titleX - 5, this.getY() + 6, ColorUtil.TEXT_COLOR);
				drawContext.fill(titleX + titleWidth + 5, this.getY() + 4, this.getX() + this.getWidth(), this.getY() + 6, ColorUtil.TEXT_COLOR);
			}
			drawContext.drawTextWithShadow(this.client.textRenderer, this.title, titleX, this.getY(), ColorUtil.WHITE);
		} else {
			drawContext.fill(this.getX(), this.getY() + 4, this.getX() + this.getWidth(), this.getY() + 6, ColorUtil.TEXT_COLOR);
		}

		Tooltip.queueFor(this, mouseX, mouseY, this.tooltipTicks, i -> this.tooltipTicks = i, this.lastTick, i -> this.lastTick = i);
	}

	/* Narration */

	@Override
	protected Text getNarrationMessage() {
		return this.getTitle().map(Text::getString)
				.filter(title -> !title.isEmpty())
				.map(title -> Text.translatable("obsidianui.narrator.separator", title))
				.orElse(null);
	}
}
