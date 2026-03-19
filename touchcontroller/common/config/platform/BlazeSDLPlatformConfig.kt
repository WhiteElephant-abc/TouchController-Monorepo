package top.fifthlight.touchcontroller.common.config.platform

import kotlinx.serialization.Serializable

@Serializable
data class BlazeSDLPlatformConfig(
    val vibrationStrength: Float = 0.5f,
    val vibrationLength: Int = 200,
)
