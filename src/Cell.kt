import java.awt.Color
import java.awt.Dimension
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.border.BevelBorder

/**
 * Created by r.makowiecki on 12/05/2017.
 */
class Cell(background: Color, initialSize: Int, index: Int) : JButton() {
    private val initialSize = initialSize
    val index : Int = index

    init {
        isContentAreaFilled = true
        isBorderPainted = true
        isOpaque = true
        setBackground(background)
        border = BorderFactory.createBevelBorder(BevelBorder.RAISED)
    }

    override fun getPreferredSize(): Dimension = Dimension(initialSize, initialSize)
}