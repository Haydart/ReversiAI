package logic

import java.awt.Point
import java.util.*

/**
 * Created by r.makowiecki on 13/05/2017.
 */
class LegalMoveFinder {
    fun findLegalMoves(board: GameBoard, state: FieldState): Set<Int> {
        val possibleMoveIndices = HashSet<Int>()
        val statePoints = board.getSquaresWithState(state)
        for (seed in statePoints) {
            for (direction in Direction.values()) {
                if (shouldSearch(board, seed, direction)) {
                    var nextPoint = direction.next(seed)
                    nextPoint = direction.next(nextPoint)
                    while (board.isPointValid(nextPoint)) {
                        if (board.getSquareState(nextPoint) == state) {
                            break
                        } else if (board.getSquareState(nextPoint) == FieldState.EMPTY) {
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

    private fun shouldSearch(board: GameBoard, seed: Point, direction: Direction): Boolean {
        val nextPoint = direction.next(seed)
        return board.getSquareState(nextPoint) == board.getSquareState(seed).opposite()
    }
}