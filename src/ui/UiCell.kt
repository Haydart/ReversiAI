package ui

import logic.FieldState
import java.awt.Color
import java.awt.Dimension
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.border.BevelBorder

/**
 * Created by r.makowiecki on 12/05/2017.
 */
class UiCell(background: Color, initialSize: Int, index: Int) : JButton() {
    private val initialSize = initialSize
    val index : Int = index
    var fieldState: FieldState = FieldState.EMPTY

    init {
        isContentAreaFilled = true
        isBorderPainted = true
        isOpaque = true
        setBackground(background)
        border = BorderFactory.createBevelBorder(BevelBorder.LOWERED)
    }

    override fun getPreferredSize(): Dimension = Dimension(initialSize, initialSize)
}