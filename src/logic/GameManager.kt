package logic

import logic.ai.AiPlayer
import logic.board.FieldState
import logic.board.GameBoard
import ui.BoardUpdateListener
import ui.FieldClickListener
import ui.GameBoardPanel
import ui.MouseListenerAdapter
import java.awt.Color
import java.awt.Dimension
import java.awt.EventQueue
import java.awt.FlowLayout
import java.awt.event.MouseEvent
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.UIManager

/**
 * Created by r.makowiecki on 12/05/2017.
 */
class GameManager : FieldClickListener, BoardUpdateListener {
    private val preferredCellSize = 80
    private val cellColor = Color(61, 168, 3)
    var playerTurn: PlayerTurn = PlayerTurn.BLACK
        private set
    private val board = GameBoard()
    private val gameBoardPanel = GameBoardPanel(cellColor, preferredCellSize, this, this)
    private val legalMoveManager = LegalMoveManager()
    private val aiPlayer = AiPlayer()

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
                    gameBoardPanel.showPossibleMoves(board.possibleMoves)
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
        gameBoardPanel.drawBoard(board)
    }

    override fun onBoardUiUpdatedAfterUserMove() {
        beginAiTurn()
    }

    fun beginAiTurn() {
        println("AI turn began")
        playerTurn = PlayerTurn.WHITE
        board.possibleMoves = legalMoveManager.findLegalMoves(board, FieldState.WHITE)
        board.boardState.whiteFieldsCount = board.getSquaresWithState(FieldState.WHITE).size
        board.boardState.whiteMobility = board.possibleMoves.size

        if (!board.possibleMoves.isEmpty()) {
            val movedField = aiPlayer.performMove(board.possibleMoves)
            board.boardStateArray[movedField.first].fieldState = movedField.second
            gameBoardPanel.hidePossibleMoves(board.possibleMoves)
            board.flipFieldsAffectedByMove(legalMoveManager.findFieldsFlippedByMove(board, movedField.first))
            board.possibleMoves = legalMoveManager.findLegalMoves(board, FieldState.BLACK)
            board.boardState.whiteFieldsCount = board.getSquaresWithState(FieldState.WHITE).size
            board.boardState.whiteMobility = board.possibleMoves.size
            gameBoardPanel.drawBoard(board)
            board.printBoard()

            if (!board.boardState.isEndOfGame()) {
                beginUserTurn()
            } else println(board.boardState.getGameResult())
        } else if (board.possibleMoves.isEmpty() && !board.boardState.isEndOfGame()) {
            beginUserTurn()
            println("No moves possible, giving up the turn to $playerTurn")
        } else if (board.boardState.isEndOfGame()) {
            println(board.boardState.getGameResult())
        }
    }

    fun beginUserTurn() {
        println("User turn began")
        playerTurn = PlayerTurn.BLACK
        board.possibleMoves = legalMoveManager.findLegalMoves(board, FieldState.BLACK)
        board.boardState.blackFieldsCount = board.getSquaresWithState(FieldState.BLACK).size
        board.boardState.blackMobility = board.possibleMoves.size

        if (board.boardState.isEndOfGame()) {
            println(board.boardState.getGameResult())
        } else if(board.possibleMoves.isEmpty()) {
            beginAiTurn()
        }
    }

    override fun onFieldClicked(index: Int): FieldState? {
        if (board.possibleMoves.contains(index) && playerTurn === PlayerTurn.BLACK) {
            board.boardStateArray[index].fieldState = FieldState.BLACK
            gameBoardPanel.hidePossibleMoves(board.possibleMoves)
            board.flipFieldsAffectedByMove(legalMoveManager.findFieldsFlippedByMove(board, index))

            gameBoardPanel.drawBoard(board)
            board.printBoard()

            return FieldState.BLACK
        } else if (board.possibleMoves.isEmpty() && !board.boardState.isEndOfGame()) {
            playerTurn = PlayerTurn.WHITE
            board.possibleMoves = legalMoveManager.findLegalMoves(board, FieldState.WHITE)
            board.boardState.whiteFieldsCount = board.getSquaresWithState(FieldState.WHITE).size
            board.boardState.whiteMobility = board.possibleMoves.size
            println("No moves possible, giving up the turn to $playerTurn")
        } else if (board.boardState.isEndOfGame()) {
            println(board.boardState.getGameResult())
        }
        return null
    }
}
