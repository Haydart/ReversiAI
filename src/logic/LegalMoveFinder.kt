package logic

import java.awt.Point
import java.util.*

/**
 * Created by r.makowiecki on 13/05/2017.
 */
class LegalMoveFinder {
    fun findLegalMoves(board: GameBoard, state: FieldState): Set<Point> {
        val possibleMoves = HashSet<Point>()
        val statePoints = board.getSquaresWithState(state)
        for (seed in statePoints) {
            for (direction in Direction.values()) {
                if (shouldSearch(board, seed, direction)) {
                    var nextPoint = direction.next(seed)
                    nextPoint = direction.next(nextPoint)
                    while (board.isPointValid(nextPoint)) {
                        if (board.getSquareState(nextPoint) === state) {
                            break
                        } else if (board.getSquareState(nextPoint) === FieldState.EMPTY) {
                            possibleMoves.add(nextPoint)
                            break
                        }
                        nextPoint = direction.next(nextPoint)
                    }
                }
            }
        }
        return possibleMoves
    }

    private fun shouldSearch(board: GameBoard, seed: Point, direction: Direction): Boolean {
        val nextPoint = direction.next(seed)
        return board.getSquareState(nextPoint) === board.getSquareState(seed).opposite()
    }
}