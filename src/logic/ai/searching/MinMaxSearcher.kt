package logic.ai.searching

import logic.GameResult
import logic.ai.evaluation.Evaluator
import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 17/05/2017.
 */
class MinMaxSearcher : Searcher() {

    private var algorithmDepth = 0

    companion object {
        var boardStatesCount: Int = 0
    }

    override fun searchBestMove(board: GameBoard, ownedFieldState: FieldState, depth: Int, evaluator: Evaluator): Int {
        algorithmDepth = depth
        val chosenMove = if (ownedFieldState === FieldState.WHITE) valueMax(board, depth, evaluator, -1) else valueMin(board, depth, evaluator, -1)
        println("       AI chose move evaluated at: ${chosenMove.first}. Move leading to this state is ${chosenMove.second}")
        boardStatesCount = 0
        return chosenMove.second
    }

    //player whose turn is it is max player
    private fun valueMax(board: GameBoard, depth: Int, evaluator: Evaluator, firstMoveLeadingToCurrentState: Int, tab: String = ""): Pair<Float, Int> {
        var bestValue = Integer.MIN_VALUE.toFloat()

        if(depth <= 0) {
            val currentBoardValue = evaluator.evaluate(board, FieldState.WHITE) //always evaluate from the same perspective
            return Pair(currentBoardValue, firstMoveLeadingToCurrentState)
        }

        board.gameState.recalculate(board)
        if(board.gameState.isEndOfGame()) {
            val gameResultValue = if(board.gameState.getGameResult() === GameResult.WHITE_WINS) Integer.MAX_VALUE.toFloat() else bestValue
            return Pair(gameResultValue, firstMoveLeadingToCurrentState)
        }

        var firstMoveIndex: Int
        var bestMoveIndex = -1

        val whitePossibleMoves = board.legalMoveManager.findLegalMoves(board, FieldState.WHITE)

        if(whitePossibleMoves.isNotEmpty()) {
            for (fieldIndex in whitePossibleMoves) {
                board.boardStateArray[fieldIndex].fieldState = FieldState.WHITE
                val flippedFields = board.legalMoveManager.findFieldsFlippedByMove(board, fieldIndex)
                board.flipFieldsAffectedByMove(flippedFields)

                firstMoveIndex = if (depth == algorithmDepth) fieldIndex else firstMoveLeadingToCurrentState

                val maxValue = valueMin(board, depth - 1, evaluator, firstMoveIndex, tab + "    ")
                board.flipFieldsAffectedByMove(flippedFields)
                board.boardStateArray[fieldIndex].fieldState = FieldState.EMPTY
                if (maxValue.first >= bestValue) {
                    bestValue = maxValue.first
                    bestMoveIndex = maxValue.second
                }
            }
        } else {
            val opponentPossibleMoves = board.legalMoveManager.findLegalMoves(board, FieldState.BLACK)

            for (fieldIndex in opponentPossibleMoves) {
                board.boardStateArray[fieldIndex].fieldState = FieldState.BLACK
                val flippedFields = board.legalMoveManager.findFieldsFlippedByMove(board, fieldIndex)
                board.flipFieldsAffectedByMove(flippedFields)

                firstMoveIndex = if (depth == algorithmDepth) fieldIndex else firstMoveLeadingToCurrentState
                val maxValue = valueMax(board, depth, evaluator, firstMoveIndex, tab + "    ")
                board.flipFieldsAffectedByMove(flippedFields)
                board.boardStateArray[fieldIndex].fieldState = FieldState.EMPTY
                if (maxValue.first >= bestValue) {
                    bestValue = maxValue.first
                    bestMoveIndex = maxValue.second
                }
            }
        }
        return Pair(bestValue, bestMoveIndex)
    }

    //minimum player is always black in this implementation
    fun valueMin(board: GameBoard, depth: Int, evaluator: Evaluator, firstMoveLeadingToCurrentState: Int, tab: String = ""): Pair<Float, Int> {
        var bestValue = Integer.MAX_VALUE.toFloat()

        if(depth <= 0) {
            val currentBoardValue = evaluator.evaluate(board, FieldState.WHITE) //always evaluate from the same perspective
            return Pair(currentBoardValue, firstMoveLeadingToCurrentState)
        }
        board.gameState.recalculate(board)
        if(board.gameState.isEndOfGame()) {
            val gameResultValue = if(board.gameState.getGameResult() === GameResult.BLACK_WINS) Integer.MIN_VALUE.toFloat() else bestValue
            return Pair(gameResultValue, firstMoveLeadingToCurrentState)
        }

        var firstMoveIndex : Int
        var bestMoveIndex = -1

        val blackPossibleMoves = board.legalMoveManager.findLegalMoves(board, FieldState.BLACK)

        if(blackPossibleMoves.isNotEmpty()) {
            for (fieldIndex in blackPossibleMoves) {
                board.boardStateArray[fieldIndex].fieldState = FieldState.BLACK
                val flippedFields = board.legalMoveManager.findFieldsFlippedByMove(board, fieldIndex)
                board.flipFieldsAffectedByMove(flippedFields)

                firstMoveIndex = if (depth == algorithmDepth) fieldIndex else firstMoveLeadingToCurrentState
                val maxValue = valueMax(board, depth - 1, evaluator, firstMoveIndex, tab + "    ")
                board.flipFieldsAffectedByMove(flippedFields)
                board.boardStateArray[fieldIndex].fieldState = FieldState.EMPTY
                if (maxValue.first <= bestValue) {
                    bestValue = maxValue.first
                    bestMoveIndex = maxValue.second
                }
            }
        } else {
            val whitePossibleMoves = board.legalMoveManager.findLegalMoves(board, FieldState.WHITE)

            for (fieldIndex in whitePossibleMoves) {
                board.boardStateArray[fieldIndex].fieldState = FieldState.WHITE
                val flippedFields = board.legalMoveManager.findFieldsFlippedByMove(board, fieldIndex)
                board.flipFieldsAffectedByMove(flippedFields)

                firstMoveIndex = if (depth == algorithmDepth) fieldIndex else firstMoveLeadingToCurrentState
                val maxValue = valueMin(board, depth, evaluator, firstMoveIndex, tab + "    ")
                board.flipFieldsAffectedByMove(flippedFields)
                board.boardStateArray[fieldIndex].fieldState = FieldState.EMPTY
                if (maxValue.first <= bestValue) {
                    bestValue = maxValue.first
                    bestMoveIndex = maxValue.second
                }
            }
        }
        return Pair(bestValue, bestMoveIndex)
    }
}














