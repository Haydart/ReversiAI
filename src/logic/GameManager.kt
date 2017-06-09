package logic

import logic.ai.AiPlayer
import logic.ai.evaluation.CornerFocusedCombinedEvaluator
import logic.ai.evaluation.FieldOwnershipEvaluator
import logic.ai.evaluation.field_weights.NewFieldWeightProvider
import logic.ai.evaluation.field_weights.StandardFieldWeightProvider
import logic.ai.searching.AlphaBetaPruningSearcherStack
import logic.ai.searching.MinMaxSearcher
import logic.ai.searching.move_ordering.PresumedEvaluationAscOrderer
import logic.board.FieldState
import logic.board.GameBoard
import ui.FieldClickListener
import ui.GameBoardPanel
import ui.GameFrame
import ui.GameStatePanel
import java.awt.Color
import java.awt.EventQueue
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
    private val isGuiModeEnabled = true
    private var playerTurn: PlayerTurn = PlayerTurn.BLACK

    //    private val whitePlayer = AiPlayer(MinMaxSearcher(), MobilityRestrictingCombinedEvaluator(StandardFieldWeightProvider()), 4, FieldState.WHITE, isGuiModeEnabled, this)

//        private val blackPlayer = HumanPlayer(this, FieldState.BLACK)

    private val blackPlayer = AiPlayer(MinMaxSearcher(), CornerFocusedCombinedEvaluator(StandardFieldWeightProvider()),
            4, FieldState.BLACK, isGuiModeEnabled, this)

    private val whitePlayer = AiPlayer(AlphaBetaPruningSearcherStack(), FieldOwnershipEvaluator(NewFieldWeightProvider()),
            7, FieldState.WHITE, isGuiModeEnabled, this, PresumedEvaluationAscOrderer(FieldOwnershipEvaluator(NewFieldWeightProvider())))

    private val board = GameBoard()
    private val gameBoardPanel = GameBoardPanel(cellColor, preferredCellSize, this)
    private val gameStatePanel = GameStatePanel()

    fun startReversiGame() {
        if (isGuiModeEnabled)
            launchGui()
        beginTurn(playerTurn)
    }

    private fun launchGui() {
        EventQueue.invokeLater {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
            } catch (ex: Exception) {
            }
            GameFrame(gameBoardPanel, gameStatePanel).isVisible = true
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
