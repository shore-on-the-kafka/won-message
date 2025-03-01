package com.won.message.infrastructure


abstract class HashMapRepository<T, ID> : Repository<T, ID> {
    protected val map = mutableMapOf<ID, T>()

    override fun create(entity: T): T {
        map[getKey(entity)] = entity
        return entity
    }


    override fun get(id: ID): T? {
        return map[id]
    }

    /**
     * This method will upsert the entity of the map
     */
    override fun update(entity: T): T {
        map[getKey(entity)] = entity
        return entity
    }

    override fun deleteById(id: ID) {
        map.remove(id)
    }

    abstract fun getKey(entity: T): ID

}
