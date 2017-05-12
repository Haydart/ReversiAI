/**
 * Created by r.makowiecki on 12/05/2017.
 */
class GameManager {
    fun startReversiGame() {
        launchGui()
    }

    private fun launchGui() {
        GameBoard().createAndLaunchGuiWindow()
    }
}