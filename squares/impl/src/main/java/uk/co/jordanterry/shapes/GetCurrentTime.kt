package uk.co.jordanterry.shapes

class GetCurrentTimeImpl : GetCurrentTime {
    override operator fun invoke(): Long = System.currentTimeMillis()
}

interface GetCurrentTime {
    operator fun invoke(): Long
}

