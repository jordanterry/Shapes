package uk.co.jordanterry.shapes

internal class GetCurrentTimeImpl :
    GetCurrentTime {
    override operator fun invoke(): Long = System.currentTimeMillis()
}

interface GetCurrentTime {
    operator fun invoke(): Long

    companion object {
        fun create(): GetCurrentTime {
            return GetCurrentTimeImpl()
        }
    }
}

