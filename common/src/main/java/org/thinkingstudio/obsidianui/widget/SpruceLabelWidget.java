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

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.thinkingstudio.obsidianui.Position;
import org.thinkingstudio.obsidianui.Tooltip;
import org.thinkingstudio.obsidianui.Tooltipable;
import org.thinkingstudio.obsidianui.border.Border;
import org.thinkingstudio.obsidianui.border.EmptyBorder;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Represents a label widget.
 *
 * @author LambdAurora
 * @version 5.0.0
 * @since 1.0.0
 */
public class SpruceLabelWidget extends AbstractSpruceWidget implements Tooltipable, WithBorder {
	public static final Consumer<SpruceLabelWidget> DEFAULT_ACTION = label -> {
	};

	private final Consumer<SpruceLabelWidget> action;
	private final int maxWidth;
	private int baseX;
	//private final int                         maxHeight;
	private Text text;
	private List<OrderedText> lines;
	private Text tooltip;
	private boolean centered;
	private Border border = EmptyBorder.EMPTY_BORDER;

	public SpruceLabelWidget(Position position, Text text, int maxWidth, Consumer<SpruceLabelWidget> action, boolean centered) {
		super(position);
		this.maxWidth = maxWidth;
		this.baseX = position.getRelativeX();
		this.action = action;
		this.centered = centered;
		this.setText(text);
	}

	public SpruceLabelWidget(Position position, Text text, int maxWidth, Consumer<SpruceLabelWidget> action) {
		this(position, text, maxWidth, action, false);
	}

	public SpruceLabelWidget(Position position, Text text, int maxWidth, boolean centered) {
		this(position, text, maxWidth, DEFAULT_ACTION, centered);
	}

	public SpruceLabelWidget(Position position, Text text, int maxWidth) {
		this(position, text, maxWidth, DEFAULT_ACTION);
	}

	private int getInnerX() {
		return this.getPosition().getAnchor().getX() + this.baseX;
	}

	/**
	 * Gets the text of the label.
	 *
	 * @return the text
	 */
	public Text getText() {
		return this.text;
	}

	/**
	 * Sets the text of this label.
	 *
	 * @param text the text to set
	 */
	public void setText(Text text) {
		this.text = text;
		this.lines = this.client.textRenderer.wrapLines(text, this.maxWidth);

		int width = this.lines.stream().mapToInt(this.client.textRenderer::getWidth).max().orElse(this.maxWidth);
		if (width > this.maxWidth) {
			width = this.maxWidth;
		}

		if (this.isCentered()) {
			this.position.setRelativeX(this.baseX + this.maxWidth / 2 - width / 2);
		} else {
			this.position.setRelativeX(this.baseX);
		}
		this.width = width;
		this.height = this.lines.size() * this.client.textRenderer.fontHeight + 2;
	}

	/**
	 * Returns whether this label is centered or not.
	 *
	 * @return {@code true} if this label is centered, else {@code false}
	 */
	public boolean isCentered() {
		return this.centered;
	}

	/**
	 * Sets whether this label is centered or not.
	 *
	 * @param centered {@code true} if this label is centered, else {@code false}
	 */
	public void setCentered(boolean centered) {
		this.centered = centered;
	}

	@Override
	public Optional<Text> getTooltip() {
		return Optional.ofNullable(this.tooltip);
	}

	@Override
	public void setTooltip(@Nullable Text tooltip) {
		this.tooltip = tooltip;
	}

	@Override
	public Border getBorder() {
		return this.border;
	}

	@Override
	public void setBorder(Border border) {
		this.border = border;
	}

	/**
	 * Fires the press event on this label widget.
	 */
	public void onPress() {
		this.action.accept(this);
	}

	/* Navigation */

	@Override
	public boolean requiresCursor() {
		return this.action == DEFAULT_ACTION;
	}

	/* Input */

	@Override
	protected boolean onMouseClick(double mouseX, double mouseY, int button) {
		if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
			if (this.hovered) {
				this.onPress();
				return true;
			}
		}
		return false;
	}

	/* Rendering */

	@Override
	protected void renderWidget(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		int y = this.getY() + 2;
		for (var it = this.lines.iterator(); it.hasNext(); y += 9) {
			var line = it.next();
			int x = this.centered ? (this.getInnerX() + this.maxWidth / 2) - this.client.textRenderer.getWidth(line) / 2
					: this.getInnerX();
			drawContext.drawText(this.client.textRenderer, line, x, y, 10526880, true);
		}

		this.getBorder().render(drawContext, this, mouseX, mouseY, delta);

		if (this.tooltip != null) {
			if (!this.tooltip.getString().isEmpty()) {
				var wrappedTooltipText = this.client.textRenderer.wrapLines(this.tooltip, Math.max(this.width / 2, 200));
				if (this.hovered)
					Tooltip.create(mouseX, mouseY, wrappedTooltipText).queue();
				else if (this.focused)
					Tooltip.create(this.getX() - 12, this.getY(), wrappedTooltipText).queue();
			}
		}
	}

	/* Narration */

	@Override
	protected Text getNarrationMessage() {
		return this.getText();
	}
}
