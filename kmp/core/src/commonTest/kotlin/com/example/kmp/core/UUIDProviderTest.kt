package com.example.kmp.core

import com.example.kmp.core.di.BaseKoinTest
import com.example.kmp.core.di.platformModule
import com.example.kmp.core.platform.UUIDProvider
import org.koin.core.module.Module
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertTrue

class UUIDProviderTest : BaseKoinTest() {

    override val koinModules: List<Module>
        get() = listOf(
            platformModule(mockDependencies = true)
        )

    private val sut: UUIDProvider by inject()

    @Test
    fun randomUID_isNotEmpty() {
        assertTrue(sut.randomUID().isNotEmpty())
    }
}
