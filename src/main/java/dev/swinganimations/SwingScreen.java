package dev.swinganimations;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

public class SwingScreen extends Screen {
    private final Screen parent;
    private final List<ButtonWidget> styleButtons = new ArrayList<>();

    public SwingScreen(Screen parent) {
        super(Text.literal("Swing Animations"));
        this.parent = parent;
    }

    private static Text styleLabel(SwingStyle s) {
        return Text.literal((SwingConfig.style == s ? "> " : "") + s.name());
    }

    private static Text onOff(String name, boolean v) {
        return Text.literal(name + ": " + (v ? "ON" : "OFF"));
    }

    @Override
    protected void init() {
        styleButtons.clear();
        int cx = this.width / 2;
        int w = 140;
        int leftX = cx - w - 10;
        int rightX = cx + 10;
        int y = 34;

        for (SwingStyle s : SwingStyle.values()) {
            ButtonWidget b = ButtonWidget.builder(styleLabel(s), btn -> {
                SwingConfig.style = s;
                for (int i = 0; i < styleButtons.size(); i++) {
                    styleButtons.get(i).setMessage(styleLabel(SwingStyle.values()[i]));
                }
            }).dimensions(leftX, y, w, 20).build();
            styleButtons.add(b);
            this.addDrawableChild(b);
            y += 22;
        }

        int ry = 34;
        this.addDrawableChild(ButtonWidget.builder(onOff("Enabled", SwingConfig.enabled), btn -> {
            SwingConfig.enabled = !SwingConfig.enabled;
            btn.setMessage(onOff("Enabled", SwingConfig.enabled));
        }).dimensions(rightX, ry, w, 20).build());
        ry += 22;

        this.addDrawableChild(new IntSlider(rightX, ry, w, 20, "Swing Speed", 25, 200, SwingConfig.speed, v -> SwingConfig.speed = v));
        ry += 22;
        this.addDrawableChild(new IntSlider(rightX, ry, w, 20, "Strength", 20, 200, SwingConfig.strength, v -> SwingConfig.strength = v));
        ry += 22;

        this.addDrawableChild(ButtonWidget.builder(onOff("No Equip Dip", SwingConfig.noEquip), btn -> {
            SwingConfig.noEquip = !SwingConfig.noEquip;
            btn.setMessage(onOff("No Equip Dip", SwingConfig.noEquip));
        }).dimensions(rightX, ry, w, 20).build());
        ry += 22;

        this.addDrawableChild(new IntSlider(rightX, ry, w, 20, "Scale", 40, 160, SwingConfig.scale, v -> SwingConfig.scale = v));
        ry += 22;
        this.addDrawableChild(new IntSlider(rightX, ry, w, 20, "Offset X", -60, 60, SwingConfig.offsetX, v -> SwingConfig.offsetX = v));
        ry += 22;
        this.addDrawableChild(new IntSlider(rightX, ry, w, 20, "Offset Y", -60, 60, SwingConfig.offsetY, v -> SwingConfig.offsetY = v));
        ry += 22;
        this.addDrawableChild(new IntSlider(rightX, ry, w, 20, "Offset Z", -60, 60, SwingConfig.offsetZ, v -> SwingConfig.offsetZ = v));

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Done"), btn -> this.close())
                .dimensions(cx - 50, this.height - 28, 100, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 14, 0xFFFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void removed() {
        SwingConfig.save();
    }

    @Override
    public void close() {
        SwingConfig.save();
        this.client.setScreen(parent);
    }

    private static final class IntSlider extends SliderWidget {
        private final String label;
        private final int min;
        private final int max;
        private final IntConsumer setter;

        IntSlider(int x, int y, int w, int h, String label, int min, int max, int value, IntConsumer setter) {
            super(x, y, w, h, Text.empty(), (value - min) / (double) (max - min));
            this.label = label;
            this.min = min;
            this.max = max;
            this.setter = setter;
            this.updateMessage();
        }

        private int current() {
            return min + (int) Math.round(this.value * (max - min));
        }

        @Override
        protected void updateMessage() {
            this.setMessage(Text.literal(label + ": " + current()));
        }

        @Override
        protected void applyValue() {
            setter.accept(current());
        }
    }
}
