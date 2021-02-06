package uk.co.jordanterry.shapes.metrics

/**
 * Send metrics to any metric providers composed together in this repository.
 *
 * @param metrics a list of metric providers
 */
class CompositeMetricImpl(
    private val metrics: List<Metrics>
) : Metrics {
    override fun sendEvent(event: String) {
        metrics.forEach { it.sendEvent(event) }
    }

    override fun sendMetric(metric: String, value: String) {
        metrics.forEach { it.sendMetric(metric, value) }
    }
}