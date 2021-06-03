package uk.co.jordanterry.shapes

class GetCurrentTime  {
     operator fun invoke(): Long = System.currentTimeMillis()
}

