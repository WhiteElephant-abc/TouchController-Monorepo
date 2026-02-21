package top.fifthlight.armorstand.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import top.fifthlight.armorstand.extension.internal.PlayerRenderStateExtInternal;
import top.fifthlight.armorstand.state.ModelInstanceManager;

@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin {
    @WrapOperation(
        method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/ArmedEntityRenderState;FF)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/ItemInHandLayer;renderArmWithItem(Lnet/minecraft/client/renderer/entity/state/ArmedEntityRenderState;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    )
    private void armorstand$cancelVanillaHeldItemRender(
        ItemInHandLayer instance,
        ArmedEntityRenderState arg,
        net.minecraft.world.item.ItemDisplayContext arg2,
        net.minecraft.world.entity.HumanoidArm arg3,
        PoseStack arg4,
        MultiBufferSource arg5,
        int i,
        Operation<Void> original
    ) {
        if (!(arg instanceof PlayerRenderState)) {
            original.call(instance, arg, arg2, arg3, arg4, arg5, i);
            return;
        }

        var uuid = ((PlayerRenderStateExtInternal) arg).armorstand$getUuid();
        if (uuid == null) {
            original.call(instance, arg, arg2, arg3, arg4, arg5, i);
            return;
        }

        var entry = ModelInstanceManager.INSTANCE.get(uuid, System.nanoTime(), false);
        if (entry instanceof ModelInstanceManager.ModelInstanceItem.Model) {
            // Cancel vanilla render
            return;
        }

        original.call(instance, arg, arg2, arg3, arg4, arg5, i);
    }
}
