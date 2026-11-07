package dev.swinganimations.mixin;

import dev.swinganimations.SwingAnimator;
import dev.swinganimations.SwingConfig;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    // Intermediary names taken from the client build for 1.21.11.
    @Inject(method = "method_65816", at = @At("HEAD"), cancellable = true, require = 0, remap = false)
    private void swinganimations$swing(float swingProgress, MatrixStack matrices, int side, Arm arm, CallbackInfo ci) {
        if (SwingConfig.enabled && SwingAnimator.swing(matrices, swingProgress, side)) {
            ci.cancel();
        }
    }

    @ModifyVariable(method = "method_3224", at = @At("HEAD"), argsOnly = true, ordinal = 0, require = 0, remap = false)
    private float swinganimations$equip(float equipProgress) {
        return SwingConfig.enabled && SwingConfig.noEquip ? 0.0f : equipProgress;
    }

    @Inject(method = "method_3224", at = @At("TAIL"), require = 0, remap = false)
    private void swinganimations$offset(MatrixStack matrices, Arm arm, float equipProgress, CallbackInfo ci) {
        if (SwingConfig.enabled) {
            SwingAnimator.offset(matrices);
        }
    }
}
