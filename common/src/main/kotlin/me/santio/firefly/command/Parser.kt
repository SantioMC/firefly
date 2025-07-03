package me.santio.firefly.command

import kotlinx.coroutines.runBlocking
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.context.CommandInput
import org.incendo.cloud.parser.ArgumentParseResult
import org.incendo.cloud.parser.ArgumentParser
import org.incendo.cloud.suggestion.Suggestion
import org.incendo.cloud.suggestion.SuggestionProvider
import java.util.concurrent.CompletableFuture

interface Parser<C, T>: ArgumentParser.FutureArgumentParser<C, T>, SuggestionProvider<C> {

    suspend fun resolve(id: String): T?
    suspend fun classType(): Class<T>

    suspend fun suggestions(): Iterable<String> {
        return emptyList()
    }

    override fun parseFuture(
        commandContext: CommandContext<C?>,
        commandInput: CommandInput
    ): CompletableFuture<ArgumentParseResult<T?>> {
        return CompletableFuture.supplyAsync {
            return@supplyAsync runBlocking {
                return@runBlocking resolve(commandInput.peekString())?.let {
                    commandInput.readString()
                    ArgumentParseResult.success(it)
                } ?: ArgumentParseResult.failure(IllegalStateException("Failed to parse the provided value"))
            }
        }
    }

    override fun suggestionsFuture(
        context: CommandContext<C?>,
        input: CommandInput
    ): CompletableFuture<out Iterable<Suggestion>> {
        return CompletableFuture.supplyAsync {
            return@supplyAsync runBlocking {
                return@runBlocking suggestions().map(Suggestion::suggestion)
            }
        }
    }

}