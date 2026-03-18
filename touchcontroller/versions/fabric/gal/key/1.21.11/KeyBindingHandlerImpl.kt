package top.fifthlight.touchcontroller.fabric.v1_21_11.gal.key

import net.minecraft.client.KeyMapping
import top.fifthlight.mergetools.api.ActualConstructor
import top.fifthlight.mergetools.api.ActualImpl
import top.fifthlight.touchcontroller.common.gal.key.KeyBindingHandler
import top.fifthlight.touchcontroller.gal.key.v1_21_11.AbstractKeyBindingHandlerImpl
import top.fifthlight.touchcontroller.mixin.v1_21_11.KeyMappingAccessor

@ActualImpl(KeyBindingHandler::class)
object KeyBindingHandlerImpl : AbstractKeyBindingHandlerImpl() {
    @JvmStatic
    @ActualConstructor
    fun of(): KeyBindingHandler = this

    override fun getKeyBinding(name: String): KeyMapping? = KeyMapping.get(name)

    override fun getAllKeyBinding(): Map<String, KeyMapping> =
        KeyMappingAccessor.`touchcontroller$getAllKeyMappings`()
}
