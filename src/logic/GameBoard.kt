package logic

import java.awt.Point

/**
 * Created by r.makowiecki on 13/05/2017.
 */
class GameBoard {
    val boardStateArray = Array(64, { BoardField() })

    fun getSquareState(point: Point): FieldState = boardStateArray[point.x * 8 + point.y].fieldState

    fun isPointValid(nextPoint: Point): Boolean = nextPoint.x >= 0 && nextPoint.x < 8 && nextPoint.y >= 0 && nextPoint.y < 8

    fun getSquaresWithState(state: FieldState): List<Point> = boardStateArray.filter { it.fieldState == state }.map { it.coordinates }
}