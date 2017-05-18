package logic.ai.searching

import logic.ai.evaluation.Evaluator
import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 17/05/2017.
 */
class MinMaxSearcher : Searcher() {
    override fun search(board: GameBoard, possibleMoves: Set<Int>, depth: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun valueMin(board: GameBoard, ownedFieldsType: FieldState, depth: Int, evaluator: Evaluator): Int {
        var best = Integer.MAX_VALUE

        if (depth <= 0 || board.boardState.isEndOfGame()) {
            return evaluator.evaluate(board, ownedFieldsType)
        }
        val possibleMoves = board.legalMoveManager.findLegalMoves(board, ownedFieldsType)

        for(fieldIndex in possibleMoves) {
            board.boardStateArray[fieldIndex].fieldState = ownedFieldsType
            val maxValue = valueMax(board, ownedFieldsType.opposite(), depth - 1, evaluator)
            if (maxValue < best) {
                best = maxValue
            }
        }
        return best
    }

    private fun valueMax(board: GameBoard, ownedFieldsType: FieldState, depth: Int, evaluator: Evaluator): Int {
        var best = Integer.MIN_VALUE

        if (depth <= 0 || board.boardState.isEndOfGame()) {
            return evaluator.evaluate(board, ownedFieldsType)
        }
        val possibleMoves = board.legalMoveManager.findLegalMoves(board, ownedFieldsType)

        for(fieldIndex in possibleMoves) {
            board.boardStateArray[fieldIndex].fieldState = ownedFieldsType
            val maxValue = valueMin(board, ownedFieldsType.opposite(), depth - 1, evaluator)
            if (maxValue < best) {
                best = maxValue
            }
        }
        return best
    }
}