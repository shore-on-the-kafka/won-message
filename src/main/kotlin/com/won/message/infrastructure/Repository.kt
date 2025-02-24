package com.won.message.infrastructure

interface Repository <T, ID> {
    fun create(entity: T): T
    fun getOrNull(id: ID): T?
    fun update(entity: T): T
    fun deleteById(id: ID)
}
