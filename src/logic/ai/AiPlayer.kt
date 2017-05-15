package logic.ai

import logic.board.FieldState
import java.util.*

/**
 * Created by r.makowiecki on 15/05/2017.
 */
class AiPlayer {
    var resultPair : Pair<Int, FieldState> = Pair(0, FieldState.EMPTY)

    fun performMove(possibleMoves: Set<Int>) : Pair<Int, FieldState> {
        for(j in 0..10000000) { //simulate ai algorithm duration
            val index = Random().nextInt(possibleMoves.size)
            val iter = possibleMoves.iterator()
            for (i in 0..index - 1) {
                iter.next()
            }
            resultPair = Pair(iter.next(), FieldState.WHITE)
        }
        return resultPair
    }
}