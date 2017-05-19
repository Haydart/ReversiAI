package logic.ai.evaluation

import logic.ai.evaluation.field_weights.FieldWeightProvider
import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 17/05/2017.
 */
class FieldOwnershipMobilityEvaluator(fieldValueWeights: FieldWeightProvider) : Evaluator {
    val fieldWeights = fieldValueWeights.getFieldWeights()

    private val POSESSION_FACTOR_WEIGHT: Float = 0.5f
    private val MOBILITY_FACTOR_WEIGHT = 1.5f

    override fun evaluate(board: GameBoard, ownedFieldsType: FieldState): Int {
        val possessedFieldsIndicesArray = board.boardStateArray.filter { it.fieldState === ownedFieldsType }
        return (possessedFieldsIndicesArray.size * POSESSION_FACTOR_WEIGHT + possessedFieldsIndicesArray.sumBy { fieldWeights[it.index] }).toInt()
    }
}