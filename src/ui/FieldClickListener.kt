package ui

import logic.board.FieldState

/**
 * Created by r.makowiecki on 13/05/2017.
 */
interface FieldClickListener {
    fun onFieldClicked(index: Int): FieldState?
}