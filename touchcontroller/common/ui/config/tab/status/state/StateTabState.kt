package top.fifthlight.touchcontroller.common.ui.config.tab.status.state

import top.fifthlight.combine.data.Text

data class StateTabState(
    val currentPlatform: Text? = null,
    val systemInfo: SystemInfo? = null,
    val warningMessage: Text? = null,
) {
    data class SystemInfo(
        val system: String,
        val arch: String,
    )
}
