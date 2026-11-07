package dev.swinganimations;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.lwjgl.glfw.GLFW;

public class SwingAnimationsMod implements ClientModInitializer {
    private boolean wasDown;

    @Override
    public void onInitializeClient() {
        SwingConfig.load();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.getWindow() == null) {
                return;
            }
            boolean down = GLFW.glfwGetKey(client.getWindow().getHandle(), GLFW.GLFW_KEY_M) == GLFW.GLFW_PRESS;
            if (down && !wasDown && client.currentScreen == null && client.player != null) {
                client.setScreen(new SwingScreen(null));
            }
            wasDown = down;
        });
    }
}
