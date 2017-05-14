package logic

import ui.FieldClickListener
import ui.GameBoardPanel
import ui.MouseListenerAdapter
import java.awt.*
import java.awt.event.MouseEvent
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.UIManager


/**
 * Created by r.makowiecki on 12/05/2017.
 */
class GameManager : FieldClickListener {
    private val preferredCellSize = 80
    private val cellColor = Color(61, 168, 3)
    var playerTurn: PlayerTurn = PlayerTurn.BLACK
        private set
    private val board = GameBoard()
    private val gameBoardPanel = GameBoardPanel(cellColor, preferredCellSize, this)
    private val moveFinder = LegalMoveFinder()
    private var validMoves: Set<Point> = emptySet()

    fun startReversiGame() {
        launchGui()
    }

    private fun launchGui() {
        EventQueue.invokeLater {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
            } catch (ex: Exception) {
            }

            val findButton = JButton("Find legal moves")
            findButton.addMouseListener(object : MouseListenerAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    super.mouseClicked(e)
                    validMoves = moveFinder.findLegalMoves(board, FieldState.BLACK)
                    gameBoardPanel.showValidMoves(validMoves)
                }
            })
            val resetButton = JButton("Reset board")

            val frame = JFrame("Reversi")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.layout = FlowLayout()
            frame.preferredSize = Dimension(750, 750)
            frame.minimumSize = Dimension(700, 750)
            frame.add(gameBoardPanel)
            frame.add(findButton)
            frame.add(resetButton)
            frame.pack()
            frame.setLocationRelativeTo(null)
            frame.isVisible = true
        }
    }

    override fun onFieldClicked(index: Int): FieldState {
        when (playerTurn) {
            PlayerTurn.BLACK -> {
                playerTurn = PlayerTurn.WHITE
                board.boardStateArray[index].fieldState = FieldState.BLACK
                printBoard()
                return FieldState.BLACK
            }
            PlayerTurn.WHITE -> {
                playerTurn = PlayerTurn.BLACK
                board.boardStateArray[index].fieldState = FieldState.WHITE
                printBoard()
                return FieldState.WHITE
            }
        }
    }

    private fun printBoard() {
        for (index in board.boardStateArray.indices) {
            if (index % 8 == 0) print("\n")
            if (board.boardStateArray[index].fieldState == FieldState.BLACK)
                print("x ")
            else if (board.boardStateArray[index].fieldState == FieldState.WHITE)
                print("o ")
            else
                print("- ")
        }
    }
}
