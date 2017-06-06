package logic

import logic.board.FieldState
import logic.board.GameBoard
import ui.FieldClickListener

/**
 * Created by r.makowiecki on 30/05/2017.
 */
class HumanPlayer(callback: MoveCompletedCallback, ownedFieldsType: FieldState) : Player(callback, ownedFieldsType), FieldClickListener {
    var possibleMoves : Set<Int> = emptySet()

    override fun performMove(board: GameBoard, possibleMoves: Set<Int>) {
        this.possibleMoves = possibleMoves
    }

    override fun onFieldClicked(index: Int): FieldState? {
        if (possibleMoves.contains(index)) { //add turn check somehow
            callback.onPlayerMoved(index, ownedFieldState)
            return ownedFieldState
        }
        return null
    }
}