package logic.ai.searching

import logic.ai.evaluation.Evaluator
import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 17/05/2017.
 */
class DebugMinMaxSearcher : Searcher() {

    private var algorithmDepth = 0

    companion object {
        var boardStatesCount: Int = 0
    }

    override fun searchBestMove(board: GameBoard, possibleMoves: Set<Int>, depth: Int, evaluator: Evaluator): Int {
        algorithmDepth = depth
        val chosenMove = valueMax(board, FieldState.WHITE, depth, evaluator, -1)
        println("There were $boardStatesCount states visited")
        println("AI chose move evaluated at: ${chosenMove.first}. Move leading to this state is ${chosenMove.second}")
        boardStatesCount = 0
        return chosenMove.second
    }

    fun valueMin(board: GameBoard, ownedFieldsType: FieldState, depth: Int, evaluator: Evaluator, firstMoveLeadingToCurrentState: Int, tab: String = ""): Pair<Float, Int> {
        var best = Integer.MAX_VALUE.toFloat()

        if (depth <= 0 || board.gameState.isEndOfGame()) {
            val currentBoardValue = evaluator.evaluate(board, ownedFieldsType)
            println()
            board.printBoard()
            println("This board was evaluated for $currentBoardValue. Move leading to this state is $firstMoveLeadingToCurrentState")
            return Pair(currentBoardValue, firstMoveLeadingToCurrentState)
        }

        var possibleMoves = board.legalMoveManager.findLegalMoves(board, ownedFieldsType)

        /*if (possibleMoves.isEmpty()) {
            println("Player $ownedFieldsType has no moves")
//            possibleMoves = performOpponentTurnUntilMovesFound(board, ownedFieldsType)
            possibleMoves = board.legalMoveManager.findLegalMoves(board, ownedFieldsType.opposite())
        }

        if(possibleMoves.isEmpty()) {
            val currentBoardValue = evaluator.evaluate(board, ownedFieldsType)
            println()
            board.printBoard()
            println("This board was evaluated for $currentBoardValue. Move leading to this state is $firstMoveLeadingToCurrentState")
            return Pair(currentBoardValue, firstMoveLeadingToCurrentState)
        }*/

        boardStatesCount += possibleMoves.size

        var firstMoveIndex : Int
        var bestMoveIndex = possibleMoves.first()

        for (fieldIndex in possibleMoves) {
//            println("${tab}Min player moved to: ${Pair(fieldIndex % 8 + 1, fieldIndex / 8 + 1)}")
            board.boardStateArray[fieldIndex].fieldState = ownedFieldsType
            val flippedFields = board.legalMoveManager.findFieldsFlippedByMove(board, fieldIndex)
            board.flipFieldsAffectedByMove(flippedFields)

            firstMoveIndex = if (depth == algorithmDepth) fieldIndex else firstMoveLeadingToCurrentState
            val maxValue = valueMax(board, ownedFieldsType.opposite(), depth - 1, evaluator, firstMoveIndex, tab + "    ")
            board.flipFieldsAffectedByMove(flippedFields)
            board.boardStateArray[fieldIndex].fieldState = FieldState.EMPTY
            if (maxValue.first <= best) {
                best = maxValue.first
                bestMoveIndex = maxValue.second
            }
        }

//        println("${tab}${tab}Minimal player depth $depth chose: $best. Move leading to this state is $firstMoveIndex")
        return Pair(best, bestMoveIndex)
    }

    private fun valueMax(board: GameBoard, ownedFieldsType: FieldState, depth: Int, evaluator: Evaluator, firstMoveLeadingToCurrentState: Int, tab: String = ""): Pair<Float, Int> {
        var bestValue = Integer.MIN_VALUE.toFloat()

        if (depth <= 0 || board.gameState.isEndOfGame()) {
            val currentBoardValue = evaluator.evaluate(board, ownedFieldsType)
            println()
            board.printBoard()
            println("This board was evaluated for $currentBoardValue. Move leading to this state is $firstMoveLeadingToCurrentState")
            return Pair(currentBoardValue, firstMoveLeadingToCurrentState)
        }
        var possibleMoves = board.legalMoveManager.findLegalMoves(board, ownedFieldsType)

        println("Possible AI moves found in searcher: $possibleMoves")

        /*if (possibleMoves.isEmpty()) {
            println("Player $ownedFieldsType has no moves")
            possibleMoves = board.legalMoveManager.findLegalMoves(board, ownedFieldsType.opposite())
        }

        if(possibleMoves.isEmpty()) {
            val currentBoardValue = evaluator.evaluate(board, ownedFieldsType)
            println()
            board.printBoard()
            println("This board was evaluated for $currentBoardValue. Move leading to this state is $firstMoveLeadingToCurrentState")
            return Pair(currentBoardValue, firstMoveLeadingToCurrentState)
        }*/

        boardStatesCount += possibleMoves.size

        var firstMoveIndex: Int
        var bestMoveIndex = possibleMoves.first()

        for (fieldIndex in possibleMoves) {
//            println("${tab}Max player moved to: ${Pair(fieldIndex % 8 + 1, fieldIndex / 8 + 1)}")
            board.boardStateArray[fieldIndex].fieldState = ownedFieldsType
            val flippedFields = board.legalMoveManager.findFieldsFlippedByMove(board, fieldIndex)
            board.flipFieldsAffectedByMove(flippedFields)

            firstMoveIndex = if (depth == algorithmDepth) fieldIndex else firstMoveLeadingToCurrentState
            val maxValue = valueMin(board, ownedFieldsType.opposite(), depth - 1, evaluator, firstMoveIndex, tab + "    ")
            board.flipFieldsAffectedByMove(flippedFields)
            board.boardStateArray[fieldIndex].fieldState = FieldState.EMPTY
            if (maxValue.first >= bestValue) {
                bestValue = maxValue.first
                bestMoveIndex = maxValue.second
            }
        }

//        println("${tab}${tab}Maximal player depth $depth chose: $bestValue. Move leading to this state is $firstMoveIndex")
        return Pair(bestValue, bestMoveIndex)
    }

    /*
     * treat a few moves in a row as one move, flattening the moves into streak's initial move tree level
     */
    private fun performOpponentTurnUntilMovesFound(board: GameBoard, blockedPlayer: FieldState): Set<Int> {
        var blockedPlayerMoves = emptySet<Int>()
        var i = 1
        while (blockedPlayerMoves.isEmpty() || !board.gameState.isEndOfGame()) {
            val oppositePossibleMoves = board.legalMoveManager.findLegalMoves(board, blockedPlayer.opposite())
            if (oppositePossibleMoves.isEmpty()) break
            val flippedFields = board.legalMoveManager.findFieldsFlippedByMove(board, oppositePossibleMoves.first())
            board.flipFieldsAffectedByMove(flippedFields)
            board.printBoard()
            println()
            println("${oppositePossibleMoves.first()} was ${i++} th move of ${blockedPlayer.opposite()}")
            board.boardStateArray[oppositePossibleMoves.first()].fieldState = blockedPlayer.opposite()
            blockedPlayerMoves = board.legalMoveManager.findLegalMoves(board, blockedPlayer)
        }
        return blockedPlayerMoves
    }
}














