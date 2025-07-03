package me.santio.firefly.paper.coroutine

import kotlinx.coroutines.*
import me.santio.firefly.data.inWholeTicks
import me.santio.firefly.paper.plugin
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.milliseconds

@OptIn(InternalCoroutinesApi::class) // it's fine, we want the delay stuff
object PaperServerDispatcher: CoroutineDispatcher(), Delay {

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        plugin.server.scheduler.runTask(plugin, block)
    }

    override fun scheduleResumeAfterDelay(
        timeMillis: Long,
        continuation: CancellableContinuation<Unit>
    ) {
        plugin.server.scheduler.runTaskLater(
            plugin,
            Runnable { continuation.tryResume(Unit) },
            timeMillis.milliseconds.inWholeTicks
        )
    }


}