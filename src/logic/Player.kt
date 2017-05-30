package logic

import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 17/05/2017.
 */
abstract class Player(callback: MoveCompletedCallback, fieldState: FieldState) {
    val ownedFieldsType : FieldState = fieldState
    val moveCompletedCallback : MoveCompletedCallback = callback

    abstract fun performMove(board: GameBoard, possibleMoves: Set<Int>)
}

interface MoveCompletedCallback {
    fun onPlayerMoved(index: Int, fieldState: FieldState)
}
