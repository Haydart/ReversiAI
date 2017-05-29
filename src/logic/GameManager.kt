package logic

import logic.ai.AiPlayer
import logic.ai.evaluation.FieldWeightFocusedEvaluator
import logic.ai.evaluation.field_weights.NewFieldWeightProvider
import logic.ai.searching.MinMaxSearcher
import logic.board.FieldState
import logic.board.GameBoard
import ui.*
import java.awt.Color
import java.awt.Dimension
import java.awt.EventQueue
import java.awt.FlowLayout
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
    private val gameStatePanel = GameStatePanel()
    private val aiPlayer = AiPlayer(MinMaxSearcher(), FieldWeightFocusedEvaluator(NewFieldWeightProvider()), 5, FieldState.WHITE)

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
        beginAiTurn()
    }

    fun beginAiTurn() {
        playerTurn = PlayerTurn.WHITE
        board.possibleMoves = board.legalMoveManager.findLegalMoves(board, FieldState.WHITE)
        board.gameState.whiteFieldsCount = board.getSquaresWithState(FieldState.WHITE).size
        board.gameState.whiteMobility = board.possibleMoves.size
        board.gameState.blackMobility = board.legalMoveManager.findLegalMoves(board, FieldState.BLACK).size

        gameStatePanel.updateState(board.gameState.blackFieldsCount, board.gameState.whiteFieldsCount, "white turn")

        if (board.gameState.whiteMobility != 0) {
            object : Thread() {
                override fun run() {
                    val moveFieldIndex = aiPlayer.performMove(board, board.possibleMoves)
                    board.boardStateArray[moveFieldIndex.first].fieldState = moveFieldIndex.second
                    gameBoardPanel.hidePossibleMoves(board.possibleMoves)
                    board.flipFieldsAffectedByMove(board.legalMoveManager.findFieldsFlippedByMove(board, moveFieldIndex.first))
                    board.possibleMoves = board.legalMoveManager.findLegalMoves(board, FieldState.BLACK)
                    board.gameState.whiteFieldsCount = board.getSquaresWithState(FieldState.WHITE).size
                    board.gameState.whiteMobility = board.possibleMoves.size
                    gameBoardPanel.drawBoard(board)
                    beginUserTurn()
                    this.interrupt()
                }
            }.start()
        } else if (board.gameState.whiteMobility == 0 && !board.gameState.isEndOfGame()) {
            beginUserTurn()
        } else if (board.gameState.isEndOfGame()) {
            printEndGameState()
        }
    }

    fun beginUserTurn() {
        playerTurn = PlayerTurn.BLACK
        board.possibleMoves = board.legalMoveManager.findLegalMoves(board, FieldState.BLACK)
        board.gameState.blackFieldsCount = board.getSquaresWithState(FieldState.BLACK).size
        board.gameState.blackMobility = board.possibleMoves.size
        board.gameState.whiteMobility = board.legalMoveManager.findLegalMoves(board, FieldState.WHITE).size
        gameBoardPanel.showPossibleMoves(board.possibleMoves)

        gameStatePanel.updateState(board.gameState.blackFieldsCount, board.gameState.whiteFieldsCount, "black turn")

        if (board.gameState.blackMobility == 0) {
            beginAiTurn()
        } else if (board.gameState.isEndOfGame()) {
            printEndGameState()
        }
    }

    private fun printEndGameState() {
        val gameResult = board.gameState.getGameResult()
        println(gameResult)
        gameStatePanel.updateState(gameStateMessage = gameResult.toString())
    }

    override fun onFieldClicked(index: Int): FieldState? {
        if (board.possibleMoves.contains(index) && playerTurn === PlayerTurn.BLACK) {
            board.boardStateArray[index].fieldState = FieldState.BLACK
            gameBoardPanel.hidePossibleMoves(board.possibleMoves)
            board.flipFieldsAffectedByMove(board.legalMoveManager.findFieldsFlippedByMove(board, index))
            gameBoardPanel.drawBoard(board)
            return FieldState.BLACK
        }
        return null
    }
}
