package logic.ai

import logic.MoveCompletedCallback
import logic.Player
import logic.ai.evaluation.Evaluator
import logic.ai.searching.Searcher
import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 15/05/2017.
 */
class AiPlayer(searcher: Searcher, evaluator: Evaluator, depth: Int, ownedFieldsType: FieldState, callback: MoveCompletedCallback)
    : Player(callback, ownedFieldsType) {
    val searcher = searcher
    val evaluator = evaluator
    val depth = depth

    override fun performMove(board: GameBoard, possibleMoves: Set<Int>) {
        val moveResultPair = Pair(searcher.searchBestMove(board, possibleMoves, depth, evaluator), ownedFieldsType)
        moveCompletedCallback.onPlayerMoved(moveResultPair.first, moveResultPair.second)
    }
}