package me.santio.firefly.paper.command.parser

import com.google.auto.service.AutoService
import me.santio.firefly.command.Parser
import me.santio.firefly.identity.createId
import me.santio.firefly.instance.InstanceManager
import me.santio.firefly.paper.command.Source
import me.santio.firefly.paper.instance.PaperInstance

@AutoService(Parser::class)
class PaperInstanceParser: Parser<Source, PaperInstance> {

    override suspend fun classType(): Class<PaperInstance> {
        return PaperInstance::class.java
    }

    override suspend fun resolve(id: String): PaperInstance? {
        return InstanceManager.instances
            .filterIsInstance<PaperInstance>()
            .find { createId(it).asString == id }
    }

    override suspend fun suggestions(): Iterable<String> {
        return InstanceManager.instances
            .filterIsInstance<PaperInstance>()
            .map { createId(it).asString }
    }

}