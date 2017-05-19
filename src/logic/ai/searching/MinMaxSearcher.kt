package logic.ai.searching

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

    override fun searchBestMove(board: GameBoard, possibleMoves: Set<Int>, depth: Int, evaluator: Evaluator): Int {
        algorithmDepth = depth
        val chosenMove = valueMax(board, FieldState.WHITE, depth, evaluator, -1)
        println("There were $boardStatesCount states visited")
        println("AI chose move evaluated at: ${chosenMove.first}")
        boardStatesCount = 0
        return chosenMove.second
    }

    fun valueMin(board: GameBoard, ownedFieldsType: FieldState, depth: Int, evaluator: Evaluator, firstMoveLeadingToCurrentState: Int, tab: String = ""): Pair<Int, Int> {
        var best = Integer.MAX_VALUE

        if (depth <= 0 || board.gameState.isEndOfGame()) {
            val currentBoardValue = evaluator.evaluate(board, ownedFieldsType.opposite())
            //board.printBoard()
            println("This board was evaluated for $currentBoardValue")
            return Pair(currentBoardValue, firstMoveLeadingToCurrentState)
        }
        val possibleMoves = board.legalMoveManager.findLegalMoves(board, ownedFieldsType)
        boardStatesCount += possibleMoves.size

        var firstMoveIndex = 0

        for (fieldIndex in possibleMoves) {
            println("${tab}Min player moved to: ${Pair(fieldIndex % 8 + 1, fieldIndex / 8 + 1)}")
            board.boardStateArray[fieldIndex].fieldState = ownedFieldsType
            val flippedFields = board.legalMoveManager.findFieldsFlippedByMove(board, fieldIndex)
            board.flipFieldsAffectedByMove(flippedFields)

            firstMoveIndex = if (depth == algorithmDepth) fieldIndex else firstMoveLeadingToCurrentState
            val maxValue = valueMax(board, ownedFieldsType.opposite(), depth - 1, evaluator, firstMoveIndex, tab + "    ")
            board.flipFieldsAffectedByMove(flippedFields)
            board.boardStateArray[fieldIndex].fieldState = FieldState.EMPTY
            if (maxValue.first < best) {
                best = maxValue.first
            }
        }
        println("${tab}${tab}Minimal player depth $depth chose: $best")
        return Pair(best, firstMoveIndex)
    }

    private fun valueMax(board: GameBoard, ownedFieldsType: FieldState, depth: Int, evaluator: Evaluator, firstMoveLeadingToCurrentState: Int, tab: String = ""): Pair<Int, Int> {
        var best = Integer.MIN_VALUE

        if (depth <= 0 || board.gameState.isEndOfGame()) {
            val currentBoardValue = evaluator.evaluate(board, ownedFieldsType.opposite())
            //board.printBoard()
            println("This board was evaluated for $currentBoardValue")
            return Pair(currentBoardValue, firstMoveLeadingToCurrentState)
        }
        val possibleMoves = board.legalMoveManager.findLegalMoves(board, ownedFieldsType)
        boardStatesCount += possibleMoves.size

        var firstMoveIndex = 0

        for (fieldIndex in possibleMoves) {
            println("${tab}Max player moved to: ${Pair(fieldIndex % 8 + 1, fieldIndex / 8 +1)}")
            board.boardStateArray[fieldIndex].fieldState = ownedFieldsType
            val flippedFields = board.legalMoveManager.findFieldsFlippedByMove(board, fieldIndex)
            board.flipFieldsAffectedByMove(flippedFields)

            firstMoveIndex = if (depth == algorithmDepth) fieldIndex else firstMoveLeadingToCurrentState
            val maxValue = valueMin(board, ownedFieldsType.opposite(), depth - 1, evaluator, firstMoveIndex, tab + "    ")
            board.flipFieldsAffectedByMove(flippedFields)
            board.boardStateArray[fieldIndex].fieldState = FieldState.EMPTY
            if (maxValue.first > best) {
                best = maxValue.first
            }
        }
        println("${tab}${tab}Maximal player depth $depth chose: $best")
        return Pair(best, firstMoveIndex)
    }
}