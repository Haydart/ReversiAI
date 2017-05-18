package logic

import logic.board.FieldState

/**
 * Created by r.makowiecki on 17/05/2017.
 */
abstract class Player(fieldState: FieldState) {
    val ownedFieldsType : FieldState = fieldState
}