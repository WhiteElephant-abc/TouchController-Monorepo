package top.fifthlight.touchcontroller.common.config.platform

import kotlinx.serialization.Serializable

@Serializable
data class PlatformConfig(
    val blazesdl: BlazeSDLPlatformConfig = BlazeSDLPlatformConfig(),
)
