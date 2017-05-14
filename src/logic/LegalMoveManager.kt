package logic

import java.awt.Point


/**
 * Created by r.makowiecki on 13/05/2017.
 */
class LegalMoveManager {
    fun findLegalMoves(board: GameBoard, state: FieldState): Set<Int> {
        val possibleMoveIndices = HashSet<Int>()
        val statePoints = board.getSquaresWithState(state)
        for (startingPoint in statePoints) {
            for (direction in Direction.values()) {
                if (shouldSearch(board, startingPoint, direction)) {
                    var nextPoint = direction.next(startingPoint)
                    nextPoint = direction.next(nextPoint)
                    while (board.isPointValid(nextPoint)) {
                        if (board.getFieldState(nextPoint) == state) {
                            break
                        } else if (board.getFieldState(nextPoint) == FieldState.EMPTY) {
                            possibleMoveIndices.add(nextPoint.y * 8 + nextPoint.x)
                            break
                        }
                        nextPoint = direction.next(nextPoint)
                    }
                }
            }
        }
        return possibleMoveIndices
    }

    fun findFieldsFlippedByMove(board: GameBoard, performedMove: Int): Set<Int> {
        val filledlist = HashSet<Point>()
        val performedMoveState = board.boardStateArray[performedMove].fieldState
        val performedMoveCoordinates = board.boardStateArray[performedMove].coordinates
        for (direction in Direction.values()) {

            if (shouldSearch(board, performedMoveCoordinates, direction)) {
                var nextPoint = direction.next(performedMoveCoordinates)
                val tempSet = HashSet<Point>()
                while (board.isPointValid(nextPoint)) {
                    val nextState = board.getFieldState(nextPoint)
                    if (nextState === performedMoveState.opposite()) {
                        tempSet.add(nextPoint)
                    } else if (nextState === performedMoveState) {
                        filledlist.addAll(tempSet)
                        break
                    } else if (nextState === FieldState.EMPTY) {
                        break
                    }
                    nextPoint = direction.next(nextPoint)
                }
            }
        }
        return filledlist.map { it.y * 8 + it.x }.toSet()
    }

    private fun shouldSearch(board: GameBoard, seed: Point, direction: Direction): Boolean {
        val nextPoint = direction.next(seed)
        return board.isPointValid(nextPoint) && board.getFieldState(nextPoint) == board.getFieldState(seed).opposite()
    }
}