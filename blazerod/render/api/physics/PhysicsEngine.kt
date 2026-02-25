package top.fifthlight.blazerod.api.physics

import top.fifthlight.blazerod.render.api.resource.ModelInstance

object PhysicsEngine {
    private val activeWorlds = mutableMapOf<ModelInstance, World>()
    
    fun register(instance: ModelInstance, provider: Provider) {
        if (!activeWorlds.containsKey(instance)) {
            activeWorlds[instance] = provider.createWorld(instance)
        }
    }

    fun unregister(instance: ModelInstance) {
        activeWorlds.remove(instance)?.dispose()
    }

    fun getWorld(instance: ModelInstance): World? {
        return activeWorlds[instance]
    }

    fun update(time: Float) {
        val iterator = activeWorlds.iterator()
        while (iterator.hasNext()) {
            val (instance, world) = iterator.next()
            if (instance.referenceCount <= 0) {
                world.dispose()
                iterator.remove()
            }
        }
    }

    interface World {
        fun applyVelocityDamping(rigidBodyIndex: Int, linearAttenuation: Float, angularAttenuation: Float)
        fun resetRigidBody(rigidBodyIndex: Int, position: org.joml.Vector3f, rotation: org.joml.Quaternionf)
        fun pullTransforms(dst: FloatArray)
        fun pushTransforms(src: FloatArray)
        fun step(deltaTime: Float, maxSubSteps: Int, fixedTimeStep: Float)
        fun dispose()
    }

    fun interface Provider {
        fun createWorld(instance: ModelInstance): World
    }
}
