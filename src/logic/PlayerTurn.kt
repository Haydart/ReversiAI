package logic

import logic.board.FieldState

/**
 * Created by r.makowiecki on 13/05/2017.
 */
enum class PlayerTurn {
    BLACK {
        override fun getOwnedFieldState() = FieldState.BLACK
        override fun toString() = "Black turn"
    },
    WHITE {
        override fun getOwnedFieldState() = FieldState.WHITE
        override fun toString() = "White turn"
    };

    abstract fun getOwnedFieldState(): FieldState
    fun opposite(): PlayerTurn = if (this === BLACK) WHITE else BLACK
}