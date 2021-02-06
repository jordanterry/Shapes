package uk.co.jordanterry.shapes.metrics

interface Metrics {

    fun sendEvent(event: String)

    fun sendMetric(metric: String, value: String)

}