package uk.co.jordanterry.metrics

import dagger.Provides
import uk.co.jordanterry.shapes.metrics.CompositeMetricImpl
import uk.co.jordanterry.shapes.metrics.Metrics

@dagger.Module
class MetricsModule {

    @Provides
    fun providesMetrics(): Metrics {
        return CompositeMetricImpl(emptyList())
    }
}