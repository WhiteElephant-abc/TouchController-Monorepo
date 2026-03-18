package top.fifthlight.touchcontroller.fabric.v26_1.gal.key

import net.minecraft.client.KeyMapping
import top.fifthlight.mergetools.api.ActualConstructor
import top.fifthlight.mergetools.api.ActualImpl
import top.fifthlight.touchcontroller.common.gal.key.KeyBindingHandler
import top.fifthlight.touchcontroller.gal.key.v26_1.AbstractKeyBindingHandlerImpl
import top.fifthlight.touchcontroller.mixin.v26_1.KeyMappingAccessor

@ActualImpl(KeyBindingHandler::class)
object KeyBindingHandlerImpl : AbstractKeyBindingHandlerImpl() {
    @JvmStatic
    @ActualConstructor
    fun of(): KeyBindingHandler = this

    override fun getKeyBinding(name: String): KeyMapping? = KeyMapping.get(name)

    override fun getAllKeyBinding(): Map<String, KeyMapping> =
        KeyMappingAccessor.`touchcontroller$getAllKeyMappings`()
}
