package uk.co.jordanterry.shapes

internal class GetCurrentTimeImpl : GetCurrentTime {
    override operator fun invoke(): Long {
        return System.currentTimeMillis()
    }
}

interface GetCurrentTime {
    operator fun invoke(): Long
}

