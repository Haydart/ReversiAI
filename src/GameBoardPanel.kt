import java.awt.Color
import java.awt.Point
import javax.swing.JPanel

/**
 * Created by r.makowiecki on 12/05/2017.
 */
class GameBoardPanel(cellColor: Color, initialCellSize: Int) : JPanel() {
    init {
        layout = ChessBoardLayoutManager(initialCellSize)
        for (index in 0..63) {
            add(Cell(cellColor, initialCellSize), Point(index / 8, index % 8))
        }
    }
}
