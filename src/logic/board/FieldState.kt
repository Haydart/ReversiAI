package logic.board

/**
 * Created by r.makowiecki on 13/05/2017.
 */
enum class FieldState {
    BLACK,
    WHITE,
    EMPTY,
    POSSIBLE;

    fun opposite() : logic.board.FieldState = if (this == logic.board.FieldState.BLACK) logic.board.FieldState.WHITE else logic.board.FieldState.BLACK
}