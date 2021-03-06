package io.github.haopooby.model

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import io.github.haopooby.entity.Ads
import org.checkerframework.checker.nullness.qual.NonNull
import java.util.*
import java.util.concurrent.TimeUnit

data class Counter(val ads: Ads = Ads(),
                   val count: @NonNull Cache<String, String> = buildCache(ads)
) {
    companion object {
        fun buildCache(ads: Ads): @NonNull Cache<String, String> {
            return Caffeine.newBuilder()
                    .expireAfterWrite(ads.capIntervalMin.toLong(), TimeUnit.MINUTES)
                    .build<String, String>()
        }
    }

    fun allowed() = count.estimatedSize() < ads.capNum
    fun increase() = count.put(UUID.randomUUID().toString(), "")
}