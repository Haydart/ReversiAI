package logic.ai.searching

import logic.ai.evaluation.Evaluator
import logic.ai.searching.move_ordering.SearchMoveOrderer
import logic.board.FieldState
import logic.board.GameBoard
import java.util.*

/**
 * Created by r.makowiecki on 17/05/2017.
 */
class RandomSearcher : Searcher {
    override fun searchBestMove(board: GameBoard, ownedFieldState: FieldState, depth: Int, evaluator: Evaluator, moveOrderer: SearchMoveOrderer): Int {
            val index = Random().nextInt(board.possibleMoves.size)
            val iterator = board.possibleMoves.iterator()
            for (i in 0..index - 1) {
                iterator.next()
            }
        return iterator.next()
    }
}