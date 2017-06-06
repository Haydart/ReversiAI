package logic

import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 17/05/2017.
 */
abstract class Player(protected val callback: MoveCompletedCallback, protected val ownedFieldState: FieldState) {
    abstract fun performMove(board: GameBoard, possibleMoves: Set<Int>)
}

interface MoveCompletedCallback {
    fun onPlayerMoved(index: Int, fieldState: FieldState)
}
