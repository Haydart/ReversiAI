package ui

import java.awt.FlowLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.EmptyBorder


/**
 * Created by r.makowiecki on 29/05/2017.
 */
class GameStatePanel : JPanel() {
    private val blackCountLabel: JLabel = JLabel("Black fields: ")
    private val whiteCountLabel: JLabel = JLabel("White fields: ")
    private val gameStateLabel: JLabel = JLabel("Now it`s black turn")

    private var blackCount = 2
    private var whiteCount = 2
    private var gameStateMessage = "Black turn"

    init {
        layout = FlowLayout()
        blackCountLabel.border = EmptyBorder(10,10,10,10)
        whiteCountLabel.border = EmptyBorder(10,10,10,10)
        gameStateLabel.border = EmptyBorder(10,10,10,10)

        add(blackCountLabel)
        add(whiteCountLabel)
        add(gameStateLabel)
        isVisible = true
    }

    fun updateState(blackCount: Int = this.blackCount, whiteCount: Int = this.whiteCount, gameStateMessage: String) {
        blackCountLabel.text = "Black fields: " + blackCount
        whiteCountLabel.text = "White fields: " + whiteCount
        this.gameStateLabel.text = gameStateMessage
    }
}