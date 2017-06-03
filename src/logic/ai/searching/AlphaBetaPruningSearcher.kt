package logic.ai.searching

import logic.ai.evaluation.Evaluator
import logic.ai.searching.move_ordering.SearchMoveOrderer
import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 31/05/2017.
 */
class AlphaBetaPruningSearcher : Searcher {
    override fun searchBestMove(board: GameBoard, ownedFieldState: FieldState, depth: Int, evaluator: Evaluator, moveOrderer: SearchMoveOrderer): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}