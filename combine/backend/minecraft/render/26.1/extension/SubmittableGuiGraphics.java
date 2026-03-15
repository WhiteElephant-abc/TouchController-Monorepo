package top.fifthlight.combine.backend.minecraft.render.extension.v26_1;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.state.GuiElementRenderState;

public interface SubmittableGuiGraphics {
    void combine$submitElement(GuiElementRenderState guiElementRenderState);
    ScreenRectangle combine$peekScissorStack();
}
