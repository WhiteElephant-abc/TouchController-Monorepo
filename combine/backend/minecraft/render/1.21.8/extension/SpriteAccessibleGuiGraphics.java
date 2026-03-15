package top.fifthlight.combine.backend.minecraft.render.extension.v1_21_8;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public interface SpriteAccessibleGuiGraphics {
    TextureAtlasSprite combine$getSprite(ResourceLocation resourceLocation);
}
