package logic.ai.searching.move_ordering

import logic.ai.evaluation.Evaluator
import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 04/06/2017.
 */
//this orderer evaluates all possible moves one level deeper and sorts possible moves list descending based on that
class PresumedEvaluationDescOrderer(private val evaluator: Evaluator) : SearchMoveOrderer {
    override fun getOrderedPossibleMoves(board: GameBoard, possibleMoves: Set<Int>) = possibleMoves
            .toMutableList()
            .sortedByDescending { evaluator.evaluate(makeMove(board.getCopy(), it, FieldState.WHITE), FieldState.WHITE) }

    fun makeMove(board: GameBoard, performedMove: Int, ownedFieldState: FieldState): GameBoard {
        board.boardStateArray[performedMove].fieldState = ownedFieldState
        val flippedFields = board.legalMoveManager.findFieldsFlippedByMove(board, performedMove)
        board.flipFieldsAffectedByMove(flippedFields)
        return board
    }
}


