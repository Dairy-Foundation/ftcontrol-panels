package com.bylazar.configurables.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Sorter(val sort: Int)
