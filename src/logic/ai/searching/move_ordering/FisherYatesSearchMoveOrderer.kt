package logic.ai.searching.move_ordering

import logic.board.GameBoard

/**
 * Created by r.makowiecki on 04/06/2017.
 */
class FisherYatesSearchMoveOrderer : SearchMoveOrderer {
    override fun getOrderedPossibleMoves(board: GameBoard, possibleMoves: Set<Int>): List<Int> {
        val list = possibleMoves.toMutableList()
        val collectionSize = list.size
        for (i in 0..possibleMoves.size - 1) {
            val randomIndex = i + (Math.random() * (collectionSize - i)).toInt()
            val randomElement = list[randomIndex]
            list[randomIndex] = list[i]
            list[i] = randomElement
        }
        return list
    }
}