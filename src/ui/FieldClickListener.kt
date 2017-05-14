package ui

import logic.FieldState

/**
 * Created by r.makowiecki on 13/05/2017.
 */
interface FieldClickListener {
    fun onFieldClicked(index: Int, fieldState: FieldState) : FieldState
}