package com.santansarah.blescanner.util

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.core.module.Module

class AddKoinModules(private vararg val modules: Module = emptyArray())
    : BeforeAllCallback {

    override fun beforeAll(context: ExtensionContext?) {
        loadKoinModules(modules.asList())
    }
}
