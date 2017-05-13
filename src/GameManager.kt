import java.awt.Color
import java.awt.EventQueue
import javax.swing.JFrame
import javax.swing.UIManager

/**
 * Created by r.makowiecki on 12/05/2017.
 */
class GameManager : FieldClickListener {
    private val preferredCellSize = 64
    private val cellColor = Color(0, 147, 6)
    var playerTurn: PlayerTurn = PlayerTurn.BLACK
        private set

    fun startReversiGame() {
        launchGui()
    }

    private fun launchGui() {
        EventQueue.invokeLater {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
            } catch (ex: Exception) {
            }

            val frame = JFrame("Reversi")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.add(GameBoardPanel(cellColor, preferredCellSize, this))
            frame.pack()
            frame.setLocationRelativeTo(null)
            frame.isVisible = true
        }
    }

    override fun onFieldClicked(index: Int): Color {
        when (playerTurn) {
            PlayerTurn.BLACK -> playerTurn = PlayerTurn.WHITE
            PlayerTurn.WHITE -> playerTurn = PlayerTurn.BLACK
        }
        return playerTurn.getFieldBackgroundColor()
    }
}