package logic

import java.awt.Point

/**
 * Created by r.makowiecki on 13/05/2017.
 */
enum class Direction(private val rowstep: Int, private val colstep: Int) {
    /* ↑ */ NORTH(-1, 0),
    /* ↓ */ SOUTH(+1, 0),
    /* ← */ WEST(0, -1),
    /* → */ EAST(0, +1),
    /* ↖ */ NORTHWEST(-1, -1),
    /* ↘ */ SOUTHEAST(+1, +1),
    /* ↙ */ SOUTHWEST(+1, -1),
    /* ↗ */ NORTHEAST(-1, +1);

    fun next(point: Point): Point {
        return Point(point.x + rowstep, point.y + colstep)
    }
}