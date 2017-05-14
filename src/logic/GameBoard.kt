package logic

import java.awt.Point

/**
 * Created by r.makowiecki on 13/05/2017.
 */
class GameBoard {
    val boardStateArray = Array(64, { BoardField() })

    init {
        setupInitialFields()
    }

    private fun setupInitialFields() {
        boardStateArray[27].fieldState = FieldState.BLACK
        boardStateArray[28].fieldState = FieldState.WHITE
        boardStateArray[35].fieldState = FieldState.WHITE
        boardStateArray[36].fieldState = FieldState.BLACK
    }

    fun getSquareState(point: Point): FieldState = boardStateArray[point.y * 8 + point.x].fieldState

    fun isPointValid(nextPoint: Point): Boolean = nextPoint.x in 0..7 && nextPoint.y in 0..7

    fun getSquaresWithState(state: FieldState): List<Point> = boardStateArray.filter { it.fieldState === state }.map { it.coordinates }

    fun printBoard() {
        for (index in boardStateArray.indices) {
            if (index % 8 == 0) print("\n")
            print(
                    if(boardStateArray[index].fieldState == FieldState.BLACK) "x "
                    else if (boardStateArray[index].fieldState == FieldState.WHITE) "o "
                    else "- "
            )
        }
    }
}