import java.awt.EventQueue
import javax.swing.JFrame

class GameBoard : JFrame() {
    val screenWidth = 1920
    val screenHeight = 1080
    init {
        initUI()
    }

    private fun initUI() {
        title = "Reversi"
        setSize(screenWidth, screenHeight)
        setLocationRelativeTo(null)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    }

    fun createAndLaunchGuiWindow() = EventQueue.invokeLater { isVisible = true }
}