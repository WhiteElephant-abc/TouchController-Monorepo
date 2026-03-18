package top.fifthlight.touchcontroller.mixin.v1_21_11;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(KeyMapping.class)
public interface KeyMappingAccessor {
    @Accessor("ALL")
    static Map<String, KeyMapping> touchcontroller$getAllKeyMappings() {
        throw new AssertionError();
    }
}
