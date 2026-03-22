package src.menu;

import src.game.AbstractInput;
import src.game.Art;
import src.game.Control;
import src.game.Draw;
import src.game.KeyEventPair;
import src.game.LogicalKey;
import src.game.Style;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

// Generic escape-key overlay menu. Pass a background Control to render behind the overlay,
// or null for a plain darkened screen. Pass null in the actions array for a no-op entry.
public class EscapeMenu extends Control {
	private AbstractInput input;
	private String title;
	private String[] options;
	private Runnable[] actions;
	private Control background;

	private int selection = 0;
	private boolean shouldExit = false;

	public EscapeMenu(AbstractInput input, String title, String[] options, Runnable[] actions, Control background) {
		this.input = input;
		this.title = title;
		this.options = options;
		this.actions = actions;
		this.background = background;
		input.keyqueue_reset();
	}

	public void tick() {
		if (input.key_clicked(LogicalKey.UP))
			selection = (selection + options.length - 1) % options.length;
		if (input.key_clicked(LogicalKey.DOWN))
			selection = (selection + 1) % options.length;

		if (input.key_clicked(LogicalKey.ACTION)) {
			Runnable action = actions[selection];
			if (action != null) action.run();
			shouldExit = true;
			return;
		}

		KeyEventPair pair;
		while ((pair = input.keyqueue_get_next()) != null) {
			if (pair.rawCode == KeyEvent.VK_ESCAPE && pair.is_clicked()) {
				shouldExit = true;
			}
		}
	}

	public void render(Graphics g, int width, int height) {
		if (background != null) background.render(g, width, height);
		Draw.darken(g, 0, 0, width, height);

		int optionH = 36;
		int panelW = 300;
		int panelH = 50 + options.length * optionH + 10;
		int px = width / 2 - panelW / 2;
		int py = height / 2 - panelH / 2;

		Draw.fill_bordered_rect(g, px, py, panelW, panelH, 4, 4, Menu.BoxOutsideCol, Menu.BoxInnerCol, true);

		int cx = width / 2;
		Art.font.draw_string_centered(g, title, cx, py + 28, 18, 18, Style.title_color_pair);

		for (int i = 0; i < options.length; i++) {
			int oy = py + 50 + i * optionH;
			if (i == selection) {
				Draw.fill_bordered_rect(g, px + 10, oy, panelW - 20, optionH - 4, 2, 2,
					Menu.SelectedBoxOutsideCol, Menu.SelectedBoxInnerCol, true);
			}
			Art.font.draw_string_centered(g, options[i], cx, oy + (optionH - 16) / 2, 16, 16,
				i == selection ? Style.option_color_pair : Style.neutral_color_pair);
		}
	}

	public boolean requestExit() {
		return shouldExit;
	}
}
