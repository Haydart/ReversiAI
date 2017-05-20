package logic.ai.evaluation.field_weights

/**
 * Created by r.makowiecki on 17/05/2017.
 */
class StandardFieldWeightProvider : FieldWeightProvider {
    override fun getFieldWeights() = arrayOf(
            8f,  -2f,  3f,  3f,  3f,  3f,  -2f,  8f,
            -2f, -5f,  2f,  2f,  2f,  2f,  -5f, -2f,
            3f,   2f,  1f,  1f,  1f,  1f,   2f,  3f,
            3f,   2f,  1f,  1f,  1f,  1f,   2f,  3f,
            3f,   2f,  1f,  1f,  1f,  1f,   2f,  3f,
            3f,   2f,  1f,  1f,  1f,  1f,   2f,  3f,
            -2f, -5f,  2f,  2f,  2f,  2f,  -5f, -2f,
            8f,  -2f,  3f,  3f,  3f,  3f,  -2f,  8f
    )
}