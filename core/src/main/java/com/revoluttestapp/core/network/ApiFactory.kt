package com.revoluttestapp.core.network

abstract class ApiFactory {

    inline fun <reified T> build(): T {
        return create(T::class.java)
    }

    abstract fun <T> create(api: Class<T>): T
}

