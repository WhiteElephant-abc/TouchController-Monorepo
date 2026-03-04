package top.fifthlight.blazerod.render.version_1_21_8.runtime.node

import net.minecraft.client.renderer.MultiBufferSource
import org.joml.Matrix4f
import org.joml.Matrix4fc

sealed class UpdatePhase(
    val type: Type,
) {
    enum class Type {
        IK_UPDATE,
        INFLUENCE_TRANSFORM_UPDATE,
        PHYSICS_UPDATE_PRE,
        PHYSICS_UPDATE_POST,
        GLOBAL_TRANSFORM_PROPAGATION,
        RENDER_DATA_UPDATE,
        CAMERA_UPDATE,
        DEBUG_RENDER,
    }

    data object IkUpdate : UpdatePhase(Type.IK_UPDATE)

    data object InfluenceTransformUpdate : UpdatePhase(Type.INFLUENCE_TRANSFORM_UPDATE)

    data object PhysicsUpdatePre : UpdatePhase(Type.PHYSICS_UPDATE_PRE)

    data object PhysicsUpdatePost : UpdatePhase(Type.PHYSICS_UPDATE_POST)


    data object GlobalTransformPropagation : UpdatePhase(Type.GLOBAL_TRANSFORM_PROPAGATION)

    data object RenderDataUpdate : UpdatePhase(Type.RENDER_DATA_UPDATE)

    data object CameraUpdate : UpdatePhase(Type.CAMERA_UPDATE)

    data class DebugRender(
        val viewProjectionMatrix: Matrix4fc,
        val multiBufferSource: MultiBufferSource,
    ) : UpdatePhase(
        type = Type.DEBUG_RENDER,
    )
}
