package logic

/**
 * Created by r.makowiecki on 13/05/2017.
 */
enum class FieldState {
    BLACK,
    WHITE,
    EMPTY,
    POSSIBLE;

    fun opposite() : FieldState = if (this == BLACK) WHITE else BLACK
}