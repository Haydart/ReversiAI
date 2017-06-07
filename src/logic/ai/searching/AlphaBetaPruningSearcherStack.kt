package logic.ai.searching

import logic.GameResult
import logic.ai.evaluation.Evaluator
import logic.ai.searching.move_ordering.SearchMoveOrderer
import logic.board.FieldState
import logic.board.GameBoard


/**
 * Created by r.makowiecki on 31/05/2017.
 */
class AlphaBetaPruningSearcherStack : Searcher {
    private var algorithmDepth = 0
    private lateinit var moveOrderer: SearchMoveOrderer
    private lateinit var evaluator: Evaluator
    private lateinit var playerOwnedFieldState: FieldState
    private val initialAlpha = Float.MIN_VALUE
    private val initialBeta = Float.MAX_VALUE

    override fun searchBestMove(board: GameBoard, ownedFieldState: FieldState, depth: Int, evaluator: Evaluator, moveOrderer: SearchMoveOrderer): Int {
        algorithmDepth = depth
        this.moveOrderer = moveOrderer
        this.evaluator = evaluator
        this.playerOwnedFieldState = ownedFieldState

        var bestMove = -1
        var bestScore = Float.MIN_VALUE

        //the player to make the move is always MAX player
        val possibleMoves = board.legalMoveManager.findLegalMoves(board, ownedFieldState)
        for (possibleMove in moveOrderer.getOrderedPossibleMoves(board, possibleMoves, ownedFieldState)) {
            //max player outer recursive loop to be able to determine best move

            //make move
            board.boardStateArray[possibleMove].fieldState = ownedFieldState
            val flippedFields = board.legalMoveManager.findFieldsFlippedByMove(board, possibleMove)
            board.flipFieldsAffectedByMove(flippedFields)

            val alpha = miniMax(board, ownedFieldState.opposite(), depth - 1, bestScore, Float.MAX_VALUE)

            //undo move
            board.flipFieldsAffectedByMove(flippedFields)
            board.boardStateArray[possibleMove].fieldState = FieldState.EMPTY

            if (alpha > bestScore || bestMove == -1) {
                bestMove = possibleMove
                bestScore = alpha
            }
        }
        println("    AI chose move to position $bestMove with value of $bestScore")
        return bestMove
    }

    @Suppress("NAME_SHADOWING")
    private fun miniMax(board: GameBoard, ownedFieldState: FieldState, depth: Int, alpha: Float, beta: Float): Float {
        var alpha = alpha
        var beta = beta

        if (depth <= 0) {
            val currentBoardValue = evaluator.evaluate(board, FieldState.WHITE) //always evaluate from the same perspective
            return currentBoardValue
        }
        board.gameState.recalculate(board)
        if (board.gameState.isEndOfGame()) {
            val gameResultValue = if (board.gameState.getGameResult() === GameResult.BLACK_WINS) Float.MIN_VALUE else Float.MAX_VALUE
            return gameResultValue
        }

        val possibleMoves = board.legalMoveManager.findLegalMoves(board, ownedFieldState)

        if (ownedFieldState === playerOwnedFieldState) {

            var currentAlpha = Float.MIN_VALUE

            for (possibleMove in moveOrderer.getOrderedPossibleMoves(board, possibleMoves, ownedFieldState)) {

                //make move
                board.boardStateArray[possibleMove].fieldState = ownedFieldState
                val flippedFields = board.legalMoveManager.findFieldsFlippedByMove(board, possibleMove)
                board.flipFieldsAffectedByMove(flippedFields)

                currentAlpha = Math.max(currentAlpha, miniMax(board, ownedFieldState.opposite(), depth - 1, alpha, beta))
                alpha = Math.max(alpha, currentAlpha)

                //undo move
                board.flipFieldsAffectedByMove(flippedFields)
                board.boardStateArray[possibleMove].fieldState = FieldState.EMPTY

                if (alpha >= beta) {
                    println("Alpha cut performed")
                    return alpha
                }
            }
            return currentAlpha
        }

        var currentBeta = Float.MAX_VALUE

        for (possibleMove in moveOrderer.getOrderedPossibleMoves(board, possibleMoves, ownedFieldState)) {

            //make move
            board.boardStateArray[possibleMove].fieldState = ownedFieldState
            val flippedFields = board.legalMoveManager.findFieldsFlippedByMove(board, possibleMove)
            board.flipFieldsAffectedByMove(flippedFields)

            currentBeta = Math.min(currentBeta, miniMax(board, ownedFieldState.opposite(), depth - 1, alpha, beta))
            beta = Math.min(beta, currentBeta)

            //undo move
            board.flipFieldsAffectedByMove(flippedFields)
            board.boardStateArray[possibleMove].fieldState = FieldState.EMPTY

            if (beta <= alpha) {
                println("Beta cut performed")
                return beta
            }
        }
        return currentBeta
    }
}