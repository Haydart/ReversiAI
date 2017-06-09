package logic.ai.searching.move_ordering

import logic.ai.evaluation.Evaluator
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 04/06/2017.
 */
//this orderer evaluates all possible moves one level deeper and sorts possible moves list descending based on that
class PresumedMobilityDescOrderer(private val evaluator: Evaluator) : SearchMoveOrderer {
    override fun getOrderedPossibleMoves(board: GameBoard, possibleMoves: Set<Int>) = possibleMoves
            .toMutableList()
            .sortedByDescending { board.getCopy().gameState.whiteMobility }
}


