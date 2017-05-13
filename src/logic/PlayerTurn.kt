package logic

import java.awt.Color

/**
 * Created by r.makowiecki on 13/05/2017.
 */
enum class PlayerTurn {
    BLACK {
        override fun getFieldBackgroundColor(): Color = Color.BLACK
    },
    WHITE {
        override fun getFieldBackgroundColor(): Color = Color.WHITE
    };

    abstract fun getFieldBackgroundColor() : Color
}