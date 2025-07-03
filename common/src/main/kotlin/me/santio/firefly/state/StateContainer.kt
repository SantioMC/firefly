package me.santio.firefly.state

import me.santio.firefly.objenesis
import java.util.concurrent.ConcurrentHashMap

abstract class StateContainer {

    private val map = ConcurrentHashMap<Class<out State>, State>()

    inline fun <reified S : State> set(state: S) = set(S::class.java, state)
    fun <S: State> set(clazz: Class<S>, state: State): S {
        map[clazz] = state
        return clazz.cast(state)
    }

    inline fun <reified S : State> get() = get(S::class.java)
    fun <S: State> get(clazz: Class<S>): S? {
        return map[clazz]?.let { clazz.cast(it) }
    }

    inline fun <reified S : State> getOrPut(noinline defaultValue: () -> S) = getOrPut(S::class.java, defaultValue)
    @Suppress("UNCHECKED_CAST") fun <S: State> getOrPut(clazz: Class<out S>, defaultValue: () -> S): S {
        return map.getOrPut(clazz, defaultValue) as S
    }

    inline fun <reified S: State> update(noinline block: S.() -> Unit) = update(S::class.java, block)
    fun <S: State> update(clazz: Class<S>, block: S.() -> Unit) {
        getOrPut(clazz) { objenesis.newInstance(clazz) }.block()
    }

    inline fun <reified S: State> delete() = delete(S::class.java)
    fun <S: State> delete(clazz: Class<S>) {
        map.remove(clazz)
    }

}