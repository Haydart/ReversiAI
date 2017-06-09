package logic.board

import logic.LegalMoveManager

/**
 * Created by r.makowiecki on 13/05/2017.
 */
class GameBoard {
    var boardStateArray = Array(64, { BoardField() })
    var possibleMoves: Set<Int> = setOf(34, 29, 20, 43)
    var gameState: GameState = GameState()
    var legalMoveManager = LegalMoveManager()

    init {
        setupInitialFields()
    }

    private fun setupInitialFields() {
        boardStateArray[27].fieldState = FieldState.BLACK
        boardStateArray[28].fieldState = FieldState.WHITE
        boardStateArray[35].fieldState = FieldState.WHITE
        boardStateArray[36].fieldState = FieldState.BLACK
    }

    fun getCopy(): GameBoard {
        val board = GameBoard()
        board.boardStateArray = Array(64, { this.boardStateArray[it].getCopy() })
        board.gameState = this.gameState.getCopy()
        board.legalMoveManager = this.legalMoveManager
        board.possibleMoves = HashSet<Int>(possibleMoves)
        return board
    }

    fun getFieldState(point: java.awt.Point): FieldState = boardStateArray[point.y * 8 + point.x].fieldState

    fun isPointValid(fieldCoordinates: java.awt.Point): Boolean = fieldCoordinates.x in 0..7 && fieldCoordinates.y in 0..7

    fun getSquaresWithState(state: FieldState): List<java.awt.Point> = boardStateArray.filter { it.fieldState === state }.map { it.coordinates }

    fun printBoard() {
        for (index in boardStateArray.indices) {
            if (index % 8 == 0) print("\n")
            print(
                    if (boardStateArray[index].fieldState == FieldState.BLACK) "x "
                    else if (boardStateArray[index].fieldState == FieldState.WHITE) "o "
                    else if (boardStateArray[index].fieldState == FieldState.POSSIBLE) "* "
                    else "- "
            )
        }
        println()
    }

    fun flipFieldsAffectedByMove(fieldsToFlip: Set<Int>) {
        for (index in fieldsToFlip) {
            boardStateArray[index].fieldState = boardStateArray[index].fieldState.opposite()
        }
    }
}