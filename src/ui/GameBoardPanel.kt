package ui

import logic.board.FieldState
import logic.FieldTypeImageProvider
import logic.board.GameBoard
import java.awt.Color
import java.awt.Point
import java.awt.event.MouseEvent
import javax.swing.JPanel

/**
 * Created by r.makowiecki on 12/05/2017.
 */
class GameBoardPanel(cellColor: Color, initialCellSize: Int, fieldClickListener: FieldClickListener, private val boardUpdateListener: BoardUpdateListener) : JPanel() {
    private val imageProvider = FieldTypeImageProvider(initialCellSize)
    private var uiCellArray = Array<UiCell?>(64, { null })

    init {
        layout = ChessBoardLayoutManager(initialCellSize)
        for (index in uiCellArray.indices) {
            val field = UiCell(cellColor, initialCellSize, index)

            field.addMouseListener(object : MouseListenerAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    super.mouseClicked(e)
                    println("Clicked field $index")
                    val newFieldState = fieldClickListener.onFieldClicked(field.index)
                    if(newFieldState != null) {
                        field.fieldState = newFieldState
                        //drawField(field.index, newFieldState)
                        boardUpdateListener.onBoardUiUpdatedAfterUserMove()
                    }
                }
            })
            uiCellArray[index] = field
            add(uiCellArray[index], Point(index % 8, index / 8))
        }
    }

    fun drawBoard(board: GameBoard) {
        for (index in board.boardStateArray.indices) {
            drawField(index, board.boardStateArray[index].fieldState)
        }
    }

    fun drawField(index: Int, fieldType: FieldState) {
        uiCellArray[index]!!.icon = imageProvider.getImageForFieldType(fieldType)
    }

    fun showPossibleMoves(possibleMoves: Set<Int>) {
        for (index in possibleMoves) {
            val field = uiCellArray[index]
            field!!.fieldState = FieldState.POSSIBLE
            field.icon = imageProvider.getImageForFieldType(field.fieldState)
        }
    }

    fun hidePossibleMoves(possibleMoves: Set<Int>) {
        for (index in possibleMoves) {
            uiCellArray[index]!!.icon = imageProvider.getImageForFieldType(FieldState.EMPTY)
        }
    }
}
