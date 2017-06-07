package logic.ai.searching

import logic.GameResult
import logic.ai.evaluation.Evaluator
import logic.ai.searching.move_ordering.SearchMoveOrderer
import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 31/05/2017.
 */
class AlphaBetaPruningSearcher : Searcher {
    private var algorithmDepth = 0
    private lateinit var moveOrderer: SearchMoveOrderer
    private val initialAlpha = Float.MIN_VALUE
    private val initialBeta = Float.MAX_VALUE

    override fun searchBestMove(board: GameBoard, ownedFieldState: FieldState, depth: Int, evaluator: Evaluator, moveOrderer: SearchMoveOrderer): Int {
        algorithmDepth = depth
        this.moveOrderer = moveOrderer
        val chosenMove = if (ownedFieldState === FieldState.WHITE) valueMax(board, depth, initialAlpha, initialBeta, evaluator, -1) else valueMin(board, depth, initialAlpha, initialBeta, evaluator, -1)
        println("       $ownedFieldState AI chose move evaluated at: ${chosenMove.first}. Move leading to this state is ${chosenMove.second}")
        MinMaxSearcher.boardStatesCount = 0
        return chosenMove.second
    }

    //player whose turn is it is max player
    private fun valueMax(board: GameBoard, depth: Int, alpha: Float, beta: Float, evaluator: Evaluator, firstMoveLeadingToCurrentState: Int): Pair<Float, Int> {

        @Suppress("NAME_SHADOWING")
        var alpha = alpha
        var currentAlpha = Float.MIN_VALUE
        var bestValue = Float.MIN_VALUE

        if (depth <= 0) {
            val currentBoardValue = evaluator.evaluate(board, FieldState.WHITE) //always evaluate from the same perspective
            return Pair(currentBoardValue, firstMoveLeadingToCurrentState)
        }

        board.gameState.recalculate(board)
        if (board.gameState.isEndOfGame()) {
            val gameResultValue = if (board.gameState.getGameResult() === GameResult.WHITE_WINS) Integer.MAX_VALUE.toFloat() else alpha
            return Pair(gameResultValue, firstMoveLeadingToCurrentState)
        }

        var firstMoveIndex: Int
        var bestMoveIndex = -1

        val possibleMoves = board.legalMoveManager.findLegalMoves(board, FieldState.WHITE)
        var potentialBestValue: Pair<Float, Int> = Pair(Float.MIN_VALUE, -1)

        if (possibleMoves.isNotEmpty()) {
            for (fieldIndex in moveOrderer.getOrderedPossibleMoves(board, possibleMoves, FieldState.WHITE)) {
                board.boardStateArray[fieldIndex].fieldState = FieldState.WHITE
                val flippedFields = board.legalMoveManager.findFieldsFlippedByMove(board, fieldIndex)
                board.flipFieldsAffectedByMove(flippedFields)

                firstMoveIndex = if (depth == algorithmDepth) fieldIndex else firstMoveLeadingToCurrentState

                potentialBestValue = valueMin(board, depth - 1, alpha, beta, evaluator, firstMoveIndex)
                currentAlpha = Math.max(currentAlpha, potentialBestValue.first)
                alpha = Math.max(alpha, currentAlpha)

                board.flipFieldsAffectedByMove(flippedFields)
                board.boardStateArray[fieldIndex].fieldState = FieldState.EMPTY

                if (alpha >= beta) {
                    println("Tree cut performed")
                    return Pair(alpha, firstMoveIndex)
                }
            }
        } else {
            println("NO MOVES LEFT")
        }
        return Pair(potentialBestValue.first, potentialBestValue.second)
    }

    //minimum player is always black in this implementation
    fun valueMin(board: GameBoard, depth: Int, alpha: Float, beta: Float, evaluator: Evaluator, firstMoveLeadingToCurrentState: Int): Pair<Float, Int> {
        @Suppress("NAME_SHADOWING")
        var beta = beta
        var currentBeta = Float.MAX_VALUE
        var bestValue = Float.MAX_VALUE

        if (depth <= 0) {
            val currentBoardValue = evaluator.evaluate(board, FieldState.WHITE) //always evaluate from the same perspective
            return Pair(currentBoardValue, firstMoveLeadingToCurrentState)
        }
        board.gameState.recalculate(board)
        if (board.gameState.isEndOfGame()) {
            val gameResultValue = if (board.gameState.getGameResult() === GameResult.BLACK_WINS) Integer.MIN_VALUE.toFloat() else bestValue
            return Pair(gameResultValue, firstMoveLeadingToCurrentState)
        }

        var firstMoveIndex: Int
        var bestMoveIndex = -1

        val possibleMoves = board.legalMoveManager.findLegalMoves(board, FieldState.BLACK)
        var potentialBestValue: Pair<Float, Int> = Pair(Float.MAX_VALUE, -1)

        if (possibleMoves.isNotEmpty()) {
            for (fieldIndex in moveOrderer.getOrderedPossibleMoves(board, possibleMoves, FieldState.BLACK)) {
                board.boardStateArray[fieldIndex].fieldState = FieldState.BLACK
                val flippedFields = board.legalMoveManager.findFieldsFlippedByMove(board, fieldIndex)
                board.flipFieldsAffectedByMove(flippedFields)

                firstMoveIndex = if (depth == algorithmDepth) fieldIndex else firstMoveLeadingToCurrentState

                potentialBestValue = valueMax(board, depth - 1, alpha, beta, evaluator, firstMoveIndex)
                currentBeta = Math.min(currentBeta, potentialBestValue.first)
                beta = Math.min(beta, currentBeta)

                board.flipFieldsAffectedByMove(flippedFields)
                board.boardStateArray[fieldIndex].fieldState = FieldState.EMPTY

                if (beta <= alpha) {
                    println("Tree cut performed")
                    return Pair(beta, firstMoveIndex)
                }
            }
        } else {
            println("NO MOVES LEFT")
        }
        return Pair(potentialBestValue.first, potentialBestValue.second)
    }
}