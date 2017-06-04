package logic.ai.searching.move_ordering

import logic.ai.evaluation.Evaluator
import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 04/06/2017.
 */
//this orderer evaluates all possible moves one level deeper and sorts possible moves list descending based on that
class PresumedEvaluationBasedSearchMoveOrderer(private val evaluator: Evaluator) : SearchMoveOrderer {
    override fun getOrderedPossibleMoves(board: GameBoard, possibleMoves: Set<Int>, ownedFieldsType: FieldState) =
            possibleMoves.toMutableList().sortedByDescending { evaluator.evaluate(board, ownedFieldsType) }
}