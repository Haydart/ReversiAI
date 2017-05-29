package logic

import java.awt.Color

/**
 * Created by r.makowiecki on 13/05/2017.
 */
enum class PlayerTurn {
    BLACK {
        override fun getFieldBackgroundColor(): Color = Color.BLACK
        override fun toString() = "Black turn"
    },
    WHITE {
        override fun getFieldBackgroundColor(): Color = Color.WHITE
        override fun toString() = "White turn"
    };

    abstract fun getFieldBackgroundColor() : Color
}