import org.openrndr.application
import org.openrndr.color.hsl
import org.openrndr.extra.noise.Random
import org.openrndr.extra.noise.uniform
import org.openrndr.extra.shapes.grid

fun main() = application {
    program {
        val cells = drawer.bounds.grid(
            cellWidth = 20.0,
            cellHeight = 20.0
        )

        extend {
            Random.resetState()
            drawer.rectangles {
                cells.flatten().forEach {
                    val hue = Double.uniform(0.0, 360.0, Random.rnd)
                    fill = hsl(hue, 0.5, 0.5).toRGBa()
                    rectangle(it)
                }
            }
        }
    }
}