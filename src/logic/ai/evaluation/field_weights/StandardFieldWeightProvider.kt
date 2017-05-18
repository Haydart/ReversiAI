package logic.ai.evaluation.field_weights

/**
 * Created by r.makowiecki on 17/05/2017.
 */
class StandardFieldWeightProvider : FieldWeightProvider {
    override fun getFieldWeights() = arrayOf(
            8,  -2,  3,  3,  3,  3,  -2,  8,
            -2, -5,  2,  2,  2,  2,  -5, -2,
            3,   2,  1,  1,  1,  1,   2,  3,
            3,   2,  1,  1,  1,  1,   2,  3,
            3,   2,  1,  1,  1,  1,   2,  3,
            3,   2,  1,  1,  1,  1,   2,  3,
            -2, -5,  2,  2,  2,  2,  -5, -2,
            8,  -2,  3,  3,  3,  3,  -2,  8
    )
}