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

    private var prunesCount = 0

    override fun searchBestMove(board: GameBoard, ownedFieldState: FieldState, depth: Int, evaluator: Evaluator, moveOrderer: SearchMoveOrderer): Int {
        algorithmDepth = depth
        this.moveOrderer = moveOrderer
        this.evaluator = evaluator
        this.playerOwnedFieldState = ownedFieldState

        var bestMove = -1
        var bestScore = Float.MIN_VALUE

        //the player to make the move is always MAX player
        val possibleMoves = board.legalMoveManager.findLegalMoves(board, ownedFieldState)
        for (possibleMove in moveOrderer.getOrderedPossibleMoves(board, possibleMoves)) {
            //max player outer recursive loop to be able to determine best move

            //make move
            val flippedFields = makeMove(board, possibleMove, ownedFieldState)

            val alpha = alphaBeta(board, ownedFieldState.opposite(), depth - 1, bestScore, Float.MAX_VALUE)
//            println("Checking possible move to $possibleMove worth $alpha")

            //undo move
            undoMove(board, flippedFields, possibleMove)

            if (alpha > bestScore || bestMove == -1) {
                bestMove = possibleMove
                bestScore = alpha
            }
        }
        println("$prunesCount")
        prunesCount = 0
        return bestMove
    }

    @Suppress("NAME_SHADOWING")
    private fun alphaBeta(board: GameBoard, ownedFieldState: FieldState, depth: Int, alpha: Float, beta: Float): Float {
        var alpha = alpha
        var beta = beta

        if (depth <= 0) {
            val currentBoardValue = evaluator.evaluate(board, FieldState.WHITE) //always evaluate from the same perspective
//            board.printBoard()
//            println("This board was evaluated for $currentBoardValue")
            return currentBoardValue
        }
        board.gameState.recalculate(board)
        if (board.gameState.isEndOfGame()) {
            val gameResultValue = if (board.gameState.getGameResult() === GameResult.BLACK_WINS) Float.MIN_VALUE else Float.MAX_VALUE
            return gameResultValue
        }

        val possibleMoves = board.legalMoveManager.findLegalMoves(board, ownedFieldState)

        if (ownedFieldState === playerOwnedFieldState) {

            for (possibleMove in moveOrderer.getOrderedPossibleMoves(board, possibleMoves)) {
                //make move
                val flippedFields = makeMove(board, possibleMove, ownedFieldState)

                alpha = Math.max(alpha, alphaBeta(board, ownedFieldState.opposite(), depth - 1, alpha, beta))

                //undo move
                undoMove(board, flippedFields, possibleMove)

                if (alpha >= beta) {
                    prunesCount++
                    return alpha
                }
            }
            return alpha
        }

        for (possibleMove in moveOrderer.getOrderedPossibleMoves(board, possibleMoves)) {
            //make move
            val flippedFields = makeMove(board, possibleMove, ownedFieldState)

            beta = Math.min(beta, alphaBeta(board, ownedFieldState.opposite(), depth - 1, alpha, beta))

            //undo move
            undoMove(board, flippedFields, possibleMove)

            if (alpha >= beta) {
                prunesCount++
                return beta
            }
        }
        return beta
    }

    private fun makeMove(board: GameBoard, performedMove: Int, ownedFieldState: FieldState): Set<Int> {
        board.boardStateArray[performedMove].fieldState = ownedFieldState
        val flippedFields = board.legalMoveManager.findFieldsFlippedByMove(board, performedMove)
        board.flipFieldsAffectedByMove(flippedFields)
        return flippedFields
    }

    private fun undoMove(board: GameBoard, flippedFields: Set<Int>, performedMove: Int) {
        board.flipFieldsAffectedByMove(flippedFields)
        board.boardStateArray[performedMove].fieldState = FieldState.EMPTY
    }
}