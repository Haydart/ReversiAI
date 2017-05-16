package logic.ai.evaluation

import logic.PlayerTurn
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 16/05/2017.
 */
interface Evaluator {
    fun evaluate(board: GameBoard, player: PlayerTurn) : Int
}