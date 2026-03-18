package top.fifthlight.touchcontroller.mixin.v26_1;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.Projection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface CameraAccessor {
    @Accessor("projection")
    Projection getProjection();
}
