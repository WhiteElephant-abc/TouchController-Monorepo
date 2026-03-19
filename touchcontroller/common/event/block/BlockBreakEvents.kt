package top.fifthlight.touchcontroller.common.event.block

import top.fifthlight.touchcontroller.common.config.GlobalConfig
import top.fifthlight.touchcontroller.common.config.data.StatusConfig
import top.fifthlight.touchcontroller.common.config.holder.GlobalConfigHolder
import top.fifthlight.touchcontroller.common.model.ControllerHudModel

object BlockBreakEvents {
    fun afterBlockBreak() {
        val config = GlobalConfigHolder.config.value
        if (config.status.status == StatusConfig.Status.DISABLED) {
            return
        }
        if (config.regular.vibration) {
            ControllerHudModel.status.vibrate = true
        }
    }
}
