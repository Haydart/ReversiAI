package logic

import java.awt.Point

/**
 * Created by r.makowiecki on 13/05/2017.
 */
class GameBoard {
    val boardStateArray = Array(64, { BoardField() })

    fun getSquareState(point: Point): FieldState = boardStateArray[point.y * 8 + point.x].fieldState

    fun isPointValid(nextPoint: Point): Boolean = nextPoint.x in 0..7 && nextPoint.y in 0..7

    fun getSquaresWithState(state: FieldState): List<Point> = boardStateArray.filter { it.fieldState === state }.map { it.coordinates }
}