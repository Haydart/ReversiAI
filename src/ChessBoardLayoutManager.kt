import java.awt.*
import java.util.*

/**
 * Created by r.makowiecki on 12/05/2017.
 */
class ChessBoardLayoutManager(initialCellSize: Int) : LayoutManager2 {
    private val componentsMap: MutableMap<Point, Component>
    private val initialCellSize = initialCellSize

    init {
        componentsMap = HashMap<Point, Component>(initialCellSize)
    }

    override fun addLayoutComponent(comp: Component, constraints: Any) {
        if (constraints is Point) {
            componentsMap.put(constraints, comp)
        } else {
            throw IllegalArgumentException("ChessBoard constraints must be a Point")
        }
    }

    override fun maximumLayoutSize(target: Container): Dimension = preferredLayoutSize(target)

    override fun getLayoutAlignmentX(target: Container): Float = 0.5f

    override fun getLayoutAlignmentY(target: Container): Float = 0.5f

    override fun invalidateLayout(target: Container) {
    }

    override fun addLayoutComponent(name: String, comp: Component) {
    }

    override fun removeLayoutComponent(comp: Component) {
        val keys = componentsMap.keys.toTypedArray()
        for (p in keys) {
            if (componentsMap[p] == comp) {
                componentsMap.remove(p)
                break
            }
        }
    }

    override fun preferredLayoutSize(parent: Container): Dimension = CellGrid(initialCellSize, componentsMap).preferredSize

    override fun minimumLayoutSize(parent: Container): Dimension = preferredLayoutSize(parent)

    override fun layoutContainer(parent: Container) {
        val width = parent.width
        val height = parent.height

        val gridSize = Math.min(width, height)

        val grid = CellGrid(initialCellSize, componentsMap)
        val rowCount = grid.rowCount
        val columnCount = grid.columnCount

        val cellSize = gridSize / Math.max(rowCount, columnCount)

        val xOffset = (width - cellSize * columnCount) / 2
        val yOffset = (height - cellSize * rowCount) / 2

        val cellRows = grid.cellRows
        for (row in cellRows.keys) {
            val rows = cellRows[row]
            for (cell in rows!!) {
                val p = cell.point
                val comp = cell.component

                val x = xOffset + p.x * cellSize
                val y = yOffset + p.y * cellSize

                comp.setLocation(x, y)
                comp.setSize(cellSize, cellSize)

            }
        }

    }
}