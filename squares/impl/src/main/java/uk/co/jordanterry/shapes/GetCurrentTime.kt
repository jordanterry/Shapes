package uk.co.jordanterry.shapes

import javax.inject.Inject

class GetCurrentTime @Inject constructor()  {
     operator fun invoke(): Long = System.currentTimeMillis()
}

