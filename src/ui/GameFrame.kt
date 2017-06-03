package ui

import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.JFrame

/**
 * Created by r.makowiecki on 03/06/2017.
 */
class GameFrame(gameBoardPanel: GameBoardPanel, gameStatePanel: GameStatePanel) : JFrame("Reversi") {
    init {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        layout = FlowLayout()
        preferredSize = Dimension(750, 750)
        minimumSize = Dimension(700, 750)
        add(gameBoardPanel)
        add(gameStatePanel)
        pack()
        setLocationRelativeTo(null)
    }

}