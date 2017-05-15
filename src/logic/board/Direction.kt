package logic.board

import java.awt.Point

/**
 * Created by r.makowiecki on 13/05/2017.
 */
enum class Direction(private val colstep: Int, private val rowstep: Int) {
    /* ↑ */ NORTH(0, -1),
    /* ↓ */ SOUTH(0, 1),
    /* ← */ WEST(-1, 0),
    /* → */ EAST(1, 0),
    /* ↖ */ NORTHWEST(-1, -1),
    /* ↘ */ SOUTHEAST(+1, +1),
    /* ↙ */ SOUTHWEST(-1, 1),
    /* ↗ */ NORTHEAST(1, -1);

    fun next(point: Point): Point {
        return Point(point.x + colstep, point.y + rowstep)
    }
}