package me.santio.firefly.instance

object InstanceManager {

    private val _instances = mutableSetOf<Instance<*>>()
    val instances: List<Instance<*>> get() = _instances.toList()

    fun register(instance: Instance<*>) {
        _instances.add(instance)
    }

    fun remove(instance: Instance<*>) {
        _instances.remove(instance)
    }

    fun fallback(): Instance<*>? = _instances.lastOrNull { it.fallback }

}