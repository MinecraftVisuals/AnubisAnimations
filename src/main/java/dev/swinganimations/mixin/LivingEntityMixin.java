package dev.swinganimations.mixin;

import dev.swinganimations.SwingAnimator;
import dev.swinganimations.SwingConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "method_6028", at = @At("RETURN"), cancellable = true, require = 0, remap = false)
    private void swinganimations$duration(CallbackInfoReturnable<Integer> cir) {
        if ((Object) this != MinecraftClient.getInstance().player) {
            return;
        }
        if (SwingConfig.enabled) {
            cir.setReturnValue(SwingAnimator.duration(cir.getReturnValue()));
        }
    }
}
