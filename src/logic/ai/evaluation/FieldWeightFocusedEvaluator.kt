package logic.ai.evaluation

import logic.ai.evaluation.field_weights.FieldWeightProvider
import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 17/05/2017.
 * Thanks to Kartik Kukreja
 * https://github.com/kartikkukreja/blog-codes/blob/master/src/Heuristic%20Function%20for%20Reversi%20(Othello).cpp
 */
class FieldWeightFocusedEvaluator(fieldValueWeights: FieldWeightProvider) : Evaluator {
    val fieldWeights = fieldValueWeights.getFieldWeights()

    override fun evaluate(board: GameBoard, ownedFieldsType: FieldState): Float {
        var ownTilesCount = 0
        var opponentTilesCount = 0
        var ownFrontierTiles = 0
        var opponentFrontierTiles = 0
        var ownCornerTilesCount = 0
        var opponentCornerTilesCount = 0

        val tilesPossessionFactor: Float
        val cornerPossessionFactor: Float
        val playerMobilityFactor: Float
        val frontierTilesPossessionPenaltyFactor: Float
        val cornerClosenessPenaltyFactor: Float
        var weightedTilesPossessionFactor = 0f

        val neighborsCount = 8
        val neighborsX = arrayOf(-1, -1, 0, 1, 1, 1, 0, -1)
        val neighborsY = arrayOf(0, 1, 1, 1, 0, -1, -1, -1)
        val cornerTilesIndices = arrayOf(0, 7, 56, 63)
        var x: Int
        var y: Int

        // Piece difference, frontier disks and disk squares
        for (index in board.boardStateArray.indices) {
            if (board.boardStateArray[index].fieldState === ownedFieldsType) {
                weightedTilesPossessionFactor += fieldWeights[index]
                ownTilesCount++
            } else if (board.boardStateArray[index].fieldState === ownedFieldsType.opposite()) {
                weightedTilesPossessionFactor -= fieldWeights[index]
                opponentTilesCount++
            }
            if (board.boardStateArray[index].fieldState !== FieldState.EMPTY) {
                for (i in 0..neighborsCount - 1) {
                    x = board.boardStateArray[index].coordinates.x + neighborsX[i]
                    y = board.boardStateArray[index].coordinates.y + neighborsY[i]

                    if (x in 0..7 && y in 0..7 && board.boardStateArray[y * 8 + x].fieldState === FieldState.EMPTY) {
                        if (board.boardStateArray[index].fieldState === ownedFieldsType) {
                            ownFrontierTiles++
                            break
                        } else if (board.boardStateArray[index].fieldState === ownedFieldsType.opposite()) {
                            opponentFrontierTiles++
                            break
                        }
                    }
                }
            }
        }

        if (ownTilesCount > opponentTilesCount)
            tilesPossessionFactor = ((100.0f * ownTilesCount) / (ownTilesCount + opponentTilesCount))
        else if (ownTilesCount < opponentTilesCount)
            tilesPossessionFactor = (-(100.0f * opponentTilesCount) / (ownTilesCount + opponentTilesCount))
        else tilesPossessionFactor = 0f

        if (ownFrontierTiles > opponentFrontierTiles)
            frontierTilesPossessionPenaltyFactor = (-(100.0f * ownFrontierTiles) / (ownFrontierTiles + opponentFrontierTiles))
        else if (ownFrontierTiles < opponentFrontierTiles)
            frontierTilesPossessionPenaltyFactor = ((100.0f * opponentFrontierTiles) / (ownFrontierTiles + opponentFrontierTiles))
        else frontierTilesPossessionPenaltyFactor = 0f

        // Corner possession factor
        for (i in 0 until cornerTilesIndices.size) {
            if (board.boardStateArray[cornerTilesIndices[i]].fieldState === ownedFieldsType) ownCornerTilesCount++
            else if (board.boardStateArray[cornerTilesIndices[i]].fieldState === ownedFieldsType.opposite()) opponentCornerTilesCount++
        }
        cornerPossessionFactor = 25f * (ownCornerTilesCount - opponentCornerTilesCount)

        // Corner closeness penalty
        var ownDangeredCornerCloseTilesCount = 0
        var opponentDangeredCornerCloseTilesCount = 0
        if (board.boardStateArray[cornerTilesIndices[0]].fieldState === FieldState.EMPTY) {
            if (board.boardStateArray[cornerTilesIndices[0]+1].fieldState === ownedFieldsType) ownDangeredCornerCloseTilesCount++
            else if (board.boardStateArray[cornerTilesIndices[0]+1].fieldState === ownedFieldsType.opposite()) opponentDangeredCornerCloseTilesCount++
            if (board.boardStateArray[cornerTilesIndices[0]+8].fieldState === ownedFieldsType) ownDangeredCornerCloseTilesCount++
            else if (board.boardStateArray[cornerTilesIndices[0]+8].fieldState === ownedFieldsType.opposite()) opponentDangeredCornerCloseTilesCount++
            if (board.boardStateArray[cornerTilesIndices[0]+9].fieldState === ownedFieldsType) ownDangeredCornerCloseTilesCount++
            else if (board.boardStateArray[cornerTilesIndices[0]+9].fieldState === ownedFieldsType.opposite()) opponentDangeredCornerCloseTilesCount++
        }
        if (board.boardStateArray[cornerTilesIndices[1]].fieldState === FieldState.EMPTY) {
            if (board.boardStateArray[cornerTilesIndices[1]-1].fieldState === ownedFieldsType) ownDangeredCornerCloseTilesCount++
            else if (board.boardStateArray[cornerTilesIndices[1]-1].fieldState === ownedFieldsType.opposite()) opponentDangeredCornerCloseTilesCount++
            if (board.boardStateArray[cornerTilesIndices[1]+7].fieldState === ownedFieldsType) ownDangeredCornerCloseTilesCount++
            else if (board.boardStateArray[cornerTilesIndices[1]+7].fieldState === ownedFieldsType.opposite()) opponentDangeredCornerCloseTilesCount++
            if (board.boardStateArray[cornerTilesIndices[1]+8].fieldState === ownedFieldsType) ownDangeredCornerCloseTilesCount++
            else if (board.boardStateArray[cornerTilesIndices[1]+8].fieldState === ownedFieldsType.opposite()) opponentDangeredCornerCloseTilesCount++
        }
        if (board.boardStateArray[cornerTilesIndices[2]].fieldState === FieldState.EMPTY) {
            if (board.boardStateArray[cornerTilesIndices[2]-8].fieldState === ownedFieldsType) ownDangeredCornerCloseTilesCount++
            else if (board.boardStateArray[cornerTilesIndices[2]-8].fieldState === ownedFieldsType.opposite()) opponentDangeredCornerCloseTilesCount++
            if (board.boardStateArray[cornerTilesIndices[2]-7].fieldState === ownedFieldsType) ownDangeredCornerCloseTilesCount++
            else if (board.boardStateArray[cornerTilesIndices[2]-7].fieldState === ownedFieldsType.opposite()) opponentDangeredCornerCloseTilesCount++
            if (board.boardStateArray[cornerTilesIndices[2]+1].fieldState === ownedFieldsType) ownDangeredCornerCloseTilesCount++
            else if (board.boardStateArray[cornerTilesIndices[2]+1].fieldState === ownedFieldsType.opposite()) opponentDangeredCornerCloseTilesCount++
        }
        if (board.boardStateArray[cornerTilesIndices[3]].fieldState === FieldState.EMPTY) {
            if (board.boardStateArray[cornerTilesIndices[3]-9].fieldState === ownedFieldsType) ownDangeredCornerCloseTilesCount++
            else if (board.boardStateArray[cornerTilesIndices[3]-9].fieldState === ownedFieldsType.opposite()) opponentDangeredCornerCloseTilesCount++
            if (board.boardStateArray[cornerTilesIndices[3]-8].fieldState == ownedFieldsType) ownDangeredCornerCloseTilesCount++
            else if (board.boardStateArray[cornerTilesIndices[3]-8].fieldState === ownedFieldsType.opposite()) opponentDangeredCornerCloseTilesCount++
            if (board.boardStateArray[cornerTilesIndices[3]-1].fieldState == ownedFieldsType) ownDangeredCornerCloseTilesCount++
            else if (board.boardStateArray[cornerTilesIndices[3]-1].fieldState === ownedFieldsType.opposite()) opponentDangeredCornerCloseTilesCount++
        }
        cornerClosenessPenaltyFactor = -12.5f * (ownDangeredCornerCloseTilesCount - opponentDangeredCornerCloseTilesCount)


        // Mobility factor
        val ownPossibleMoves = board.gameState.whiteMobility
        val opponentPossibleMoves = board.gameState.blackMobility
        if (ownPossibleMoves > opponentPossibleMoves)
            playerMobilityFactor = (100.0f * ownPossibleMoves) / (ownPossibleMoves + opponentPossibleMoves);
        else if (ownPossibleMoves < opponentPossibleMoves)
            playerMobilityFactor = -(100.0f * opponentPossibleMoves) / (ownPossibleMoves + opponentPossibleMoves);
        else playerMobilityFactor = 0f


        //overall board state score
        val score = (10f * tilesPossessionFactor) + (400.724f * cornerPossessionFactor) +
                (200.026f * cornerClosenessPenaltyFactor) + (78.922f * playerMobilityFactor) +
                (74.396f * frontierTilesPossessionPenaltyFactor) + (1000f * weightedTilesPossessionFactor)
        return score
    }
}
