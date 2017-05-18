package logic.ai

import logic.Player
import logic.ai.evaluation.Evaluator
import logic.ai.searching.Searcher
import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 15/05/2017.
 */
class AiPlayer(searcher: Searcher, evaluator: Evaluator, depth: Int, ownedFieldsType: FieldState) : Player(ownedFieldsType) {
    val searcher = searcher
    val evaluator = evaluator
    val depth = depth

    fun performMove(board: GameBoard, possibleMoves: Set<Int>) = Pair(searcher.search(board, possibleMoves, depth), ownedFieldsType)
}