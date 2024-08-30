package com.example.kmp.core.platform

import java.util.UUID

class UUIDProviderImpl : UUIDProvider {
    override fun randomUID(): String {
        return UUID.randomUUID().toString()
    }
}