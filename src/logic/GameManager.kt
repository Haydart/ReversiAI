package logic

import logic.ai.AiPlayer
import logic.ai.evaluation.CornerFocusedCombinedEvaluator
import logic.ai.evaluation.MobilityRestrictingCombinedEvaluator
import logic.ai.evaluation.field_weights.NewFieldWeightProvider
import logic.ai.evaluation.field_weights.StandardFieldWeightProvider
import logic.ai.searching.MinMaxSearcher
import logic.board.FieldState
import logic.board.GameBoard
import ui.FieldClickListener
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
class GameManager : MoveCompletedCallback, FieldClickListener {
    override fun onFieldClicked(index: Int): FieldState? {
        //no-op
        return null
    }

    private val preferredCellSize = 80
    private val cellColor = Color(61, 168, 3)
    var playerTurn: PlayerTurn = PlayerTurn.BLACK
        private set

    private val blackPlayer = AiPlayer(MinMaxSearcher(), CornerFocusedCombinedEvaluator(NewFieldWeightProvider()), 5, FieldState.BLACK, this)
    private val whitePlayer = AiPlayer(MinMaxSearcher(), MobilityRestrictingCombinedEvaluator(StandardFieldWeightProvider()), 5, FieldState.WHITE, this)

    private val board = GameBoard()
    private val gameBoardPanel = GameBoardPanel(cellColor, preferredCellSize, this)
    private val gameStatePanel = GameStatePanel()

    fun startReversiGame() {
        launchGui()
        beginTurn(PlayerTurn.BLACK)
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

    fun beginTurn(playerTurn: PlayerTurn) {
        this.playerTurn = playerTurn
        board.possibleMoves = board.legalMoveManager.findLegalMoves(board, playerTurn.getOwnedFieldState())
        board.gameState.whiteMobility = board.legalMoveManager.findLegalMoves(board, FieldState.WHITE).size
        board.gameState.blackMobility = board.legalMoveManager.findLegalMoves(board, FieldState.BLACK).size
        gameBoardPanel.showPossibleMoves(board.possibleMoves)

        if (board.possibleMoves.isNotEmpty()) {
            (if (playerTurn === PlayerTurn.WHITE) whitePlayer else blackPlayer).performMove(board, board.possibleMoves)
        } else if (board.possibleMoves.isEmpty() && !board.gameState.isEndOfGame()) {
            beginTurn(playerTurn.opposite())
        } else if (board.gameState.isEndOfGame()) {
            printEndGameState()
        }
    }

    override fun onPlayerMoved(index: Int, fieldState: FieldState) {
        board.boardStateArray[index].fieldState = fieldState
        gameBoardPanel.hidePossibleMoves(board.possibleMoves)
        board.flipFieldsAffectedByMove(board.legalMoveManager.findFieldsFlippedByMove(board, index))

        board.gameState.blackFieldsCount = board.getSquaresWithState(FieldState.BLACK).size
        board.gameState.whiteFieldsCount = board.getSquaresWithState(FieldState.WHITE).size

        gameBoardPanel.drawBoard(board)

        updateGameStatePanel()

        beginTurn(playerTurn.opposite())
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
