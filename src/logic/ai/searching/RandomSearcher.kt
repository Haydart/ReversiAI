package logic.ai.searching

import logic.board.GameBoard
import java.util.*

/**
 * Created by r.makowiecki on 17/05/2017.
 */
class RandomSearcher : Searcher() {
    override fun search(board: GameBoard, possibleMoves: Set<Int>, depth: Int): Int {
            val index = Random().nextInt(possibleMoves.size)
            val iterator = possibleMoves.iterator()
            for (i in 0..index - 1) {
                iterator.next()
            }
        return iterator.next()
    }
}