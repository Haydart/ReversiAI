package ui

import logic.FieldState
import logic.FieldTypeImageProvider
import logic.GameBoard
import java.awt.Color
import java.awt.Point
import java.awt.event.MouseEvent
import javax.swing.JPanel

/**
 * Created by r.makowiecki on 12/05/2017.
 */
class GameBoardPanel(cellColor: Color, initialCellSize: Int, fieldClickListener: FieldClickListener) : JPanel() {
    private val imageProvider = FieldTypeImageProvider(initialCellSize)
    private var uiCellArray = Array<UiCell?>(64, { null })
    private var possibleMoveIndices = emptySet<Int>()

    init {
        layout = ChessBoardLayoutManager(initialCellSize)
        for (index in uiCellArray.indices) {
            val field = UiCell(cellColor, initialCellSize, index)

            field.addMouseListener(object : MouseListenerAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    super.mouseClicked(e)
                    println("Clicked field $index")

                    if(field.fieldState == FieldState.POSSIBLE) {
                        val newFieldState = fieldClickListener.onFieldClicked(field.index, field.fieldState)
                        field.fieldState = newFieldState
                        drawField(field.index, newFieldState)
                    }
                }
            })
            uiCellArray[index] = field
            add(uiCellArray[index], Point(index % 8, index / 8))
        }
    }

    fun drawBoard(board: GameBoard) {
        for(index in board.boardStateArray.indices) {
            drawField(index, board.boardStateArray[index].fieldState)
        }
    }

    private fun drawField(index: Int, fieldType: FieldState) {
        uiCellArray[index]!!.icon = imageProvider.getImageForFieldType(fieldType)
    }

    fun showPossibleMoves(validMovesIndices: Set<Int>) {
        possibleMoveIndices = validMovesIndices
        for (index in validMovesIndices) {
            val field = uiCellArray[index]
            field!!.fieldState = FieldState.POSSIBLE
            field.icon = imageProvider.getImageForFieldType(field.fieldState)
        }
    }

    fun hidePossibleMoves() {
        for (index in possibleMoveIndices) {
            uiCellArray[index]!!.icon = imageProvider.getImageForFieldType(FieldState.EMPTY)
        }
    }
}
