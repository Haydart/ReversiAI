package logic.ai

import logic.board.FieldState
import java.util.*

/**
 * Created by r.makowiecki on 15/05/2017.
 */
class AiPlayer {
    var resultPair : Pair<Int, FieldState> = Pair(0, FieldState.EMPTY)

    fun performMove(possibleMoves: Set<Int>) : Pair<Int, FieldState> {
        for(j in 0..100000) { //simulate ai algorithm duration
            val index = Random().nextInt(possibleMoves.size)
            val iterator = possibleMoves.iterator()
            for (i in 0..index - 1) {
                iterator.next()
            }
            resultPair = Pair(iterator.next(), FieldState.WHITE)
        }
        return resultPair
    }
}