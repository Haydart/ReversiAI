package logic.ai.searching.move_ordering

import logic.board.GameBoard

/**
 * Created by r.makowiecki on 03/06/2017.
 */
interface SearchMoveOrderer {
    fun getOrderedPossibleMoves(board: GameBoard, possibleMoves: Set<Int>): List<Int>
}