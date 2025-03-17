package com.github.sarunasbucius.nutriprice.core.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val nutriPriceAppDispatchers: NutriPriceAppDispatchers)

enum class NutriPriceAppDispatchers {
    IO,
}
