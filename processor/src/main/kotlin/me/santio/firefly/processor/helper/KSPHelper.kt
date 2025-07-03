package me.santio.firefly.processor.helper

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import kotlin.reflect.KProperty

inline fun <reified A: Annotation> KSAnnotated.annotation(): KSAnnotation? {
    return this.annotations.firstOrNull {
        it.annotationType.resolve().declaration.qualifiedName?.asString() == A::class.qualifiedName
    }
}

fun <T: Any> KSAnnotation.value(name: String, clazz: Class<T>): T? {
    val argument = this.arguments.firstOrNull {
        it.name?.asString() == name
    } ?: error("Failed to find annotation argument $name, this is a bug.")

    val value = argument.value
    if (value == null) return null

    if (clazz.isAssignableFrom(value::class.java)) {
        return clazz.cast(value)
    }

    if (clazz.isEnum) {
        return clazz.enumConstants.firstOrNull { it.toString().equals(value.toString().substringAfter('.'), ignoreCase = true) }
            ?: error("Failed to find enum constant '${value.toString().substringAfter('.')}' in enum class ${clazz.simpleName}.")
    }

    error("Failed to get annotation value $name, the types mismatched. Expected ${clazz.simpleName}, got ${value.javaClass.simpleName}")
}

inline fun <reified T: Any> KSAnnotation.value(name: String): T? {
    return value(name, T::class.java)
}

inline fun <reified T: Any> KSAnnotation.value(func: KProperty<T>): T? {
    return value(func.name, T::class.java)
}

fun KSClassDeclaration.subtypeOf(clazz: Class<*>): Boolean {
    return superTypes.any {
        val qualifiedName = it.resolve().declaration.qualifiedName ?: return false
        qualifiedName.asString() == clazz.name
    }
}

inline fun <reified T: Any> KSClassDeclaration.subtypeOf() = subtypeOf(T::class.java)

fun CodeGenerator.write(file: String, content: String, pkg: String = "me.santio.firefly") {
    check(file.contains(".")) { "file must contain file extension" }

    val stream = createNewFile(
        Dependencies.ALL_FILES,
        pkg,
        file.substringBeforeLast('.'),
        file.substringAfterLast('.')
    )

    stream.bufferedWriter(Charsets.UTF_8).use {
        it.write(content)
    }

}