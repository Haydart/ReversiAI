package logic

import logic.ai.AiPlayer
import logic.ai.evaluation.FieldWeightFocusedEvaluator
import logic.ai.evaluation.field_weights.StandardFieldWeightProvider
import logic.ai.searching.MinMaxSearcher
import logic.board.FieldState
import logic.board.GameBoard
import ui.BoardUpdateListener
import ui.GameBoardPanel
import ui.GameStatePanel
import java.awt.Color
import java.awt.Dimension
import java.awt.EventQueue
import java.awt.FlowLayout
import javax.swing.JFrame
import javax.swing.UIManager

/**
 * Created by r.makowiecki on 12/05/2017.
 */
class GameManager : BoardUpdateListener, MoveCompletedCallback {

    private val preferredCellSize = 80
    private val cellColor = Color(61, 168, 3)
    var playerTurn: PlayerTurn = PlayerTurn.BLACK
        private set

    private val blackPlayer = HumanPlayer(this, FieldState.BLACK)
    private val whitePlayer = AiPlayer(MinMaxSearcher(), FieldWeightFocusedEvaluator(StandardFieldWeightProvider()), 5, FieldState.WHITE, this)

    private val board = GameBoard()
    private val gameBoardPanel = GameBoardPanel(cellColor, preferredCellSize, blackPlayer, this)
    private val gameStatePanel = GameStatePanel()

    fun startReversiGame() {
        launchGui()
        beginBlackTurn()
    }

    private fun launchGui() {
        EventQueue.invokeLater {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
            } catch (ex: Exception) {
            }
            val frame = JFrame("Reversi")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.layout = FlowLayout()
            frame.preferredSize = Dimension(750, 750)
            frame.minimumSize = Dimension(700, 750)
            frame.add(gameBoardPanel)
            frame.add(gameStatePanel)
            frame.pack()
            frame.setLocationRelativeTo(null)
            frame.isVisible = true
        }
        gameBoardPanel.drawBoard(board)
    }

    override fun onBoardUiUpdatedAfterUserMove() {
        //beginWhiteTurn()
    }

    fun beginWhiteTurn() {
        playerTurn = PlayerTurn.WHITE
        board.possibleMoves = board.legalMoveManager.findLegalMoves(board, FieldState.WHITE)
        board.gameState.whiteFieldsCount = board.getSquaresWithState(FieldState.WHITE).size
        board.gameState.whiteMobility = board.possibleMoves.size
        board.gameState.blackMobility = board.legalMoveManager.findLegalMoves(board, FieldState.BLACK).size

        if (board.gameState.whiteMobility != 0) {
            whitePlayer.performMove(board, board.possibleMoves)
        } else if (board.gameState.whiteMobility == 0 && !board.gameState.isEndOfGame()) {
            beginBlackTurn()
        } else if (board.gameState.isEndOfGame()) {
            printEndGameState()
        }
    }

    fun beginBlackTurn() {
        playerTurn = PlayerTurn.BLACK
        board.possibleMoves = board.legalMoveManager.findLegalMoves(board, FieldState.BLACK)
        board.gameState.blackFieldsCount = board.getSquaresWithState(FieldState.BLACK).size
        board.gameState.blackMobility = board.possibleMoves.size
        board.gameState.whiteMobility = board.legalMoveManager.findLegalMoves(board, FieldState.WHITE).size
        gameBoardPanel.showPossibleMoves(board.possibleMoves)

        if (board.gameState.blackMobility == 0) {
            beginWhiteTurn()
        } else if (board.gameState.isEndOfGame()) {
            printEndGameState()
        } else {
            blackPlayer.performMove(board, board.possibleMoves)
        }
    }

    override fun onPlayerMoved(index: Int, fieldState: FieldState) {
        board.boardStateArray[index].fieldState = fieldState
        gameBoardPanel.hidePossibleMoves(board.possibleMoves)
        board.flipFieldsAffectedByMove(board.legalMoveManager.findFieldsFlippedByMove(board, index))
        gameBoardPanel.drawBoard(board)
        board.printBoard()

        updateGameStatePanel()

        if(fieldState === FieldState.BLACK) beginWhiteTurn() else beginBlackTurn()
    }

    private fun updateGameStatePanel() {
        gameStatePanel.updateState(board.gameState.blackFieldsCount, board.gameState.whiteFieldsCount, playerTurn.toString())
    }

    private fun printEndGameState() {
        val gameResult = board.gameState.getGameResult()
        println(gameResult)
        gameStatePanel.updateState(gameStateMessage = gameResult.toString())
    }
}
