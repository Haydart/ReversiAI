package logic.ai.searching

import logic.ai.evaluation.Evaluator
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 17/05/2017.
 */
abstract class Searcher {
    abstract fun searchBestMove(board: GameBoard, possibleMoves: Set<Int>, depth: Int, evaluator: Evaluator): Int
}