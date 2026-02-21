package top.fifthlight.armorstand.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.fifthlight.armorstand.extension.internal.PlayerRenderStateExtInternal;
import top.fifthlight.armorstand.state.ModelInstanceManager;

@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin {
    @Inject(
        method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/ArmedEntityRenderState;FF)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void armorstand$cancelVanillaHeldItemRender(
        PoseStack arg,
        MultiBufferSource arg2,
        int arg3,
        ArmedEntityRenderState arg4,
        float arg5,
        float arg6,
        CallbackInfo ci
    ) {
        if (!(arg4 instanceof PlayerRenderState)) {
            return;
        }

        var uuid = ((PlayerRenderStateExtInternal) arg4).armorstand$getUuid();
        if (uuid == null) {
            return;
        }

        var entry = ModelInstanceManager.INSTANCE.get(uuid, System.nanoTime(), false);
        if (entry instanceof ModelInstanceManager.ModelInstanceItem.Model) {
            ci.cancel();
        }
    }
}
