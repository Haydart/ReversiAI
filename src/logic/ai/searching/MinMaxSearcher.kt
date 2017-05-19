package logic.ai.searching

import logic.ai.evaluation.Evaluator
import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 17/05/2017.
 */
class MinMaxSearcher : Searcher() {

    companion object {
        var boardStatesCount: Int = 0
    }

    override fun search(board: GameBoard, possibleMoves: Set<Int>, depth: Int, evaluator: Evaluator): Int {
        val asd = valueMax(board, FieldState.WHITE, depth, evaluator)
        print("There were $boardStatesCount states visited")
        boardStatesCount = 0
        return asd
    }

    fun valueMin(board: GameBoard, ownedFieldsType: FieldState, depth: Int, evaluator: Evaluator): Int {
        var best = Integer.MAX_VALUE

        if (depth <= 0 || board.gameState.isEndOfGame()) {
            return evaluator.evaluate(board, ownedFieldsType)
        }
        val possibleMoves = board.legalMoveManager.findLegalMoves(board, ownedFieldsType)
        boardStatesCount += possibleMoves.size

        for (fieldIndex in possibleMoves) {
            board.boardStateArray[fieldIndex].fieldState = ownedFieldsType
            val flippedFields = board.legalMoveManager.findFieldsFlippedByMove(board, fieldIndex)
            board.flipFieldsAffectedByMove(flippedFields)
            val maxValue = valueMax(board, ownedFieldsType.opposite(), depth - 1, evaluator)
            board.flipFieldsAffectedByMove(flippedFields)
            board.boardStateArray[fieldIndex].fieldState = FieldState.EMPTY
            if (maxValue < best) {
                best = maxValue
            }
        }
        return best
    }

    private fun valueMax(board: GameBoard, ownedFieldsType: FieldState, depth: Int, evaluator: Evaluator): Int {
        var best = Integer.MIN_VALUE

        if (depth <= 0 || board.gameState.isEndOfGame()) {
            return evaluator.evaluate(board, ownedFieldsType)
        }
        val possibleMoves = board.legalMoveManager.findLegalMoves(board, ownedFieldsType)
        boardStatesCount += possibleMoves.size

        for (fieldIndex in possibleMoves) {
            board.boardStateArray[fieldIndex].fieldState = ownedFieldsType
            val flippedFields = board.legalMoveManager.findFieldsFlippedByMove(board, fieldIndex)
            board.flipFieldsAffectedByMove(flippedFields)
            val maxValue = valueMin(board, ownedFieldsType.opposite(), depth - 1, evaluator)
            board.flipFieldsAffectedByMove(flippedFields)
            board.boardStateArray[fieldIndex].fieldState = FieldState.EMPTY
            if (maxValue > best) {
                best = maxValue
            }
        }
        return best
    }
}