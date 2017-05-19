package logic

import logic.ai.AiPlayer
import logic.ai.evaluation.FieldOwnershipEvaluator
import logic.ai.evaluation.field_weights.StandardFieldWeightProvider
import logic.ai.searching.MinMaxSearcher
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
    private val aiPlayer = AiPlayer(MinMaxSearcher(), FieldOwnershipEvaluator(StandardFieldWeightProvider()), 3, FieldState.WHITE)
    private val minimalAiTurnDurationMillis = 100L

    fun startReversiGame() {
        launchGui()
        beginUserTurn()
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
        board.possibleMoves = board.legalMoveManager.findLegalMoves(board, FieldState.WHITE)
        board.gameState.whiteFieldsCount = board.getSquaresWithState(FieldState.WHITE).size
        board.gameState.whiteMobility = board.possibleMoves.size
        board.gameState.blackMobility = board.legalMoveManager.findLegalMoves(board, FieldState.BLACK).size

        if (board.gameState.whiteMobility != 0) {
            object : Thread() {
                override fun run() {
                    Thread.sleep(minimalAiTurnDurationMillis)
                    val moveFieldIndex = aiPlayer.performMove(board, board.possibleMoves)
                    board.boardStateArray[moveFieldIndex.first].fieldState = moveFieldIndex.second
                    gameBoardPanel.hidePossibleMoves(board.possibleMoves)
                    board.flipFieldsAffectedByMove(board.legalMoveManager.findFieldsFlippedByMove(board, moveFieldIndex.first))
                    board.possibleMoves = board.legalMoveManager.findLegalMoves(board, FieldState.BLACK)
                    board.gameState.whiteFieldsCount = board.getSquaresWithState(FieldState.WHITE).size
                    board.gameState.whiteMobility = board.possibleMoves.size

                    gameBoardPanel.drawBoard(board)
                    board.printBoard()

                    beginUserTurn()
                    this.interrupt()
                }
            }.start()
        } else if (board.gameState.whiteMobility == 0 && !board.gameState.isEndOfGame()) {
            println("No moves possible, giving up the turn to BLACK")
            beginUserTurn()
        } else if (board.gameState.isEndOfGame()) {
            println(board.gameState.getGameResult())
        }

    }

    fun beginUserTurn() {
        println("User turn began")
        playerTurn = PlayerTurn.BLACK
        board.possibleMoves = board.legalMoveManager.findLegalMoves(board, FieldState.BLACK)
        board.gameState.blackFieldsCount = board.getSquaresWithState(FieldState.BLACK).size
        board.gameState.blackMobility = board.possibleMoves.size
        board.gameState.whiteMobility = board.legalMoveManager.findLegalMoves(board, FieldState.WHITE).size
        gameBoardPanel.showPossibleMoves(board.possibleMoves)

        if (board.gameState.blackMobility == 0) {
            println("No moves possible, giving up the turn to WHITE")
            beginAiTurn()
        } else if (board.gameState.isEndOfGame()) {
            println(board.gameState.getGameResult())
        }
    }

    override fun onFieldClicked(index: Int): FieldState? {
        if (board.possibleMoves.contains(index) && playerTurn === PlayerTurn.BLACK) {
            board.boardStateArray[index].fieldState = FieldState.BLACK
            gameBoardPanel.hidePossibleMoves(board.possibleMoves)
            println("fields flipped by player move: ${board.legalMoveManager.findFieldsFlippedByMove(board, index)}")
            board.flipFieldsAffectedByMove(board.legalMoveManager.findFieldsFlippedByMove(board, index))

            gameBoardPanel.drawBoard(board)
            board.printBoard()

            return FieldState.BLACK
        }
        return null
    }
}
