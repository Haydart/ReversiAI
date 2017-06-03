package logic.ai.searching

import logic.ai.evaluation.Evaluator
import logic.ai.searching.move_ordering.IdleSearchMoveOrderer
import logic.ai.searching.move_ordering.SearchMoveOrderer
import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 17/05/2017.
 */
interface Searcher {
    fun searchBestMove(board: GameBoard, ownedFieldState: FieldState, depth: Int, evaluator: Evaluator, moveOrderer: SearchMoveOrderer = IdleSearchMoveOrderer()): Int
}