package logic

import ui.FieldClickListener
import ui.GameBoardPanel
import java.awt.*
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
            val resetButton = JButton("Reset board")

            val frame = JFrame("Reversi")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.layout = FlowLayout()
            frame.preferredSize = Dimension(750, 750)
            frame.minimumSize = Dimension(700, 750)
            frame.add(GameBoardPanel(cellColor, preferredCellSize, this))
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
                return FieldState.BLACK
            }
            PlayerTurn.WHITE -> {
                playerTurn = PlayerTurn.BLACK
                return FieldState.WHITE
            }
        }
    }
}
