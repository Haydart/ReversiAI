import java.awt.Component
import java.awt.Dimension
import java.awt.Point
import java.util.*

/**
 * Created by r.makowiecki on 12/05/2017.
 */
class CellGrid(initialCellSize : Int, mapComps: Map<Point, Component>) {
    val preferredSize: Dimension
    var cellWidth: Int = initialCellSize
        private set
    var cellHeight: Int = initialCellSize
        private set

    private val mapRows: MutableMap<Int, List<Cell>>
    private val mapCols: MutableMap<Int, List<Cell>>

    init {
        mapRows = HashMap<Int, List<Cell>>(initialCellSize)
        mapCols = HashMap<Int, List<Cell>>(initialCellSize)
        for (p in mapComps.keys) {
            val row = p.y
            val column = p.x
            var rows: MutableList<Cell>? = mapRows[row] as MutableList<Cell>?
            var columns: MutableList<Cell>? = mapCols[column] as MutableList<Cell>?
            if (rows == null) {
                rows = ArrayList<Cell>(initialCellSize)
                mapRows.put(row, rows)
            }
            if (columns == null) {
                columns = ArrayList<Cell>(initialCellSize)
                mapCols.put(column, columns)
            }
            val cell = Cell(p, mapComps[p] as Component)
            rows.add(cell)
            columns.add(cell)
        }

        val rowCount = mapRows.size
        val colCount = mapCols.size

        cellWidth = 0
        cellHeight = 0

        for (comps in mapRows.values) {
            for (cell in comps) {
                val comp = cell.component
                cellWidth = Math.max(cellWidth, comp.preferredSize.width)
                cellHeight = Math.max(cellHeight, comp.preferredSize.height)
            }
        }

        val cellSize = Math.max(cellHeight, cellWidth)

        preferredSize = Dimension(cellSize * colCount, cellSize * rowCount)
        println(preferredSize)
    }

    val rowCount: Int
        get() = cellRows.size

    val columnCount: Int
        get() = cellColumns.size

    val cellColumns: Map<Int, List<Cell>>
        get() = mapCols

    val cellRows: Map<Int, List<Cell>>
        get() = mapRows

    inner class Cell(val point: Point, val component: Component)
}