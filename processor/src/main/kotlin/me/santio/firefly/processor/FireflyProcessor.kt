package me.santio.firefly.processor

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.validate
import me.santio.firefly.processor.helper.annotation
import me.santio.firefly.processor.helper.value
import me.santio.firefly.processor.helper.write

class FireflyProcessor(
    private val environment: SymbolProcessorEnvironment
): SymbolProcessor {

    private val serviceable = mutableMapOf<String, MutableList<String>>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotated = resolver.getSymbolsWithAnnotation(AutoService::class.qualifiedName!!)
            .filter { it.validate() }
            .filterIsInstance<KSClassDeclaration>()

        annotated.forEach { annotated ->
            val autoService = annotated.annotation<AutoService>() ?: return@forEach
            val classes = autoService.value<List<KSType>>("value") ?: return@forEach

            classes.forEach { clazz ->
                val className = clazz.declaration.qualifiedName!!.asString()
                val list = serviceable.getOrPut(className) { mutableListOf() }
                list.add(annotated.qualifiedName!!.asString())
            }
        }

        return annotated.toList()
    }

    override fun finish() {
        serviceable.forEach { (className, list) ->
            environment.codeGenerator.write(className, list.joinToString("\n"), "META-INF/services")
        }
    }

}