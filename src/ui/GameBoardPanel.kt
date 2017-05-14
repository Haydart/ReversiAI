package ui

import logic.FieldTypeImageProvider
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import java.awt.event.MouseEvent
import javax.swing.JPanel

/**
 * Created by r.makowiecki on 12/05/2017.
 */
class GameBoardPanel(cellColor: Color, initialCellSize: Int, fieldClickListener: FieldClickListener) : JPanel() {
    private val imageProvider = FieldTypeImageProvider()
    private var fieldArray = Array<UiCell?>(64, { i -> null})
    init {
        layout = ChessBoardLayoutManager(initialCellSize)
        for (index in fieldArray.indices) {
            val field = UiCell(cellColor, initialCellSize, index)

            field.addMouseListener(object : MouseListenerAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    super.mouseClicked(e)
                    println("Clicked field $index")
                    val newFieldType = fieldClickListener.onFieldClicked(field.index)
                    field.fieldType = newFieldType
                    field.icon = imageProvider.getImageForFieldType(
                            newFieldType, Dimension(initialCellSize, initialCellSize)
                    )
                }
            })
            fieldArray[index] = field
            add(fieldArray[index], Point(index % 8, index / 8))
        }
    }
}