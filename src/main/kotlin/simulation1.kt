import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.noise.scatter
import org.openrndr.extra.noise.uniform
import org.openrndr.math.Vector2

/*
中心から、核が成長していくsimulation
 */
fun main() = application {
    program {
        var points = drawer.bounds.scatter(5.0)
        val step = 3.0
        val radii = DoubleArray(points.size) { 3.0 }.toList()
        val moving = BooleanArray(points.size) { true }

        var targetPoint = drawer.bounds.center
        var targetRadius = 10.0
        val stoppedPoints = mutableListOf<Vector2>()

        extend {
            drawer.clear(ColorRGBa.WHITE)
            drawer.stroke = null
            drawer.fill = ColorRGBa.BLACK.opacify(0.2)
            drawer.circles(
                points.filterIndexed { index, _ -> moving[index] },
                radii
            )

            drawer.fill = ColorRGBa.BLACK.opacify(0.6)
            drawer.circles(
                points.filterIndexed { index, _ -> !moving[index] },
                radii
            )

            drawer.fill = ColorRGBa.RED.opacify(0.5)
            drawer.circle(targetPoint, targetRadius)

            points = points.mapIndexed { index, point ->
                if (moving[index]) {
                    val u = Vector2.uniform(-1.0, 1.0)
                    val velocity = u.normalized * step
                    point + velocity
                } else {
                    point
                }
            }

            // ターゲット点および停止した点との衝突判定
            for (i in points.indices) {
                if (moving[i] && (points[i] - targetPoint).length <= radii[i] + targetRadius) {
                    moving[i] = false
                    stoppedPoints.add(points[i])
                }
            }

            // 停止した点同士の衝突判定
            for (i in points.indices) {
                if (moving[i]) {
                    for (stoppedPoint in stoppedPoints) {
                        if ((points[i] - stoppedPoint).length <= radii[i] * 2) {
                            moving[i] = false
                            stoppedPoints.add(points[i])
                            break
                        }
                    }
                }
            }
        }
    }
}