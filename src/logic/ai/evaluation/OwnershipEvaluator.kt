package logic.ai.evaluation

import logic.ai.evaluation.field_weights.FieldWeightProvider
import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 17/05/2017.
 */
class OwnershipEvaluator(fieldValueWeights: FieldWeightProvider) : Evaluator {
    val fieldWeights = fieldValueWeights.getFieldWeights()

    override fun evaluate(board: GameBoard, ownedFieldsType: FieldState) = board.boardStateArray
            .filter { it.fieldState === ownedFieldsType }
            .sumBy { it.index * fieldWeights[it.index] }
}