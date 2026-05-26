package ru.otus.otuskotlin.lrvch.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.web.reactive.config.WebFluxConfigurer
import ru.otus.otuskotlin.lrvch.api.v2.apiV2Mapper

//@Suppress("unused")
@Configuration
class SerializationConfiguration {
    @Bean
    fun messageConverter(): KotlinSerializationJsonHttpMessageConverter {
        return KotlinSerializationJsonHttpMessageConverter(apiV2Mapper)
    }
}
//@Configuration
//class WebFluxConfig : WebFluxConfigurer {
//
//    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
//        // Add kotlinx.serialization as a primary encoder/decoder
//        configurer.defaultCodecs().kotlinSerializationJsonEncoder(KotlinSerializationJsonEncoder())
//        configurer.defaultCodecs().kotlinSerializationJsonDecoder(KotlinSerializationJsonDecoder())
//    }
//}