package logic.ai

import logic.MoveCompletedCallback
import logic.Player
import logic.ai.evaluation.Evaluator
import logic.ai.searching.Searcher
import logic.ai.searching.move_ordering.IdleSearchMoveOrderer
import logic.ai.searching.move_ordering.SearchMoveOrderer
import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 15/05/2017.
 */
class AiPlayer(private val searcher: Searcher, private val evaluator: Evaluator, private val depth: Int,
               private val ownedFieldsType: FieldState, private val guiModeEnabled: Boolean,
               callback: MoveCompletedCallback, private val moveOrderer: SearchMoveOrderer = IdleSearchMoveOrderer())
    : Player(callback, ownedFieldsType) {

    override fun performMove(board: GameBoard, possibleMoves: Set<Int>) {
        val startTime = System.currentTimeMillis()
        val moveResultPair = Pair(searcher.searchBestMove(board, ownedFieldsType, depth, evaluator, moveOrderer), ownedFieldsType)
        Thread.sleep(
                if (guiModeEnabled) Math.max(startTime - System.currentTimeMillis() + 500, 0)
                else 0
        )
        callback.onPlayerMoved(moveResultPair.first, moveResultPair.second)
    }
}