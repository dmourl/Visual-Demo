package s1.demo.effects
import s1.demo.*
import scala.math.*
import scala.util.Random
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color
import scalafx.Includes._
import Util.Vector3D
import scalafx.scene.media.MediaMarkerEvent
import scalafx.util.Duration

class FireEffect extends Effect(width = 750, height = 400, name = "FireEffect"):
  val fire: Array[Array[Int]] = Array.fill(height, width)(0)
  val palette: Array[Color] = createPalette()
  var clock = 0

  private def createPalette(): Array[Color] =
    val palette = new Array[Color](256)
    for x <- 0 until 256 do
      val color = Color.hsb(x / 3, 1.0, math.min(1.0, x * 2 / 255.0))
      palette(x) = color
    palette

  def tick(): Unit =
    clock += 1
    for (x <- 0 until width) do
      fire(height - 1)(x) = Random.nextInt(256)
    for (y <- 0 until height - 1; x <- 0 until width) do
      val sum =
        fire((y + 1) % height)((x - 1 + width) % width) + fire((y + 1) % height)(x) +
        fire((y + 1) % height)((x + 1) % width) + fire((y + 2) % height)(x)
      fire(y)(x) = (sum * 32)/129
     /*   fire((y + 1) % height)((x - 1 + width) % width) +
        fire((y + 2) % height)(x) +
        fire((y + 1) % height)((x + 1) % width) +      fire((y + 3) % height)(x)
      fire(y)(x) = (sum * 64) / 257 */

  def drawEffect(g: GraphicsContext): Unit =
    val cellWidth = width.toDouble / fire.head.length
    val cellHeight = height.toDouble / fire.length
    for y <- fire.indices; x <- fire.head.indices do
      val color = palette(fire(y)(x))
      g.setFill(color)
      g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight)

  def next = clock > 1000

  override def onEnd(): Unit =
      {}
  end onEnd

  override def mouseAt(x: Int, y: Int): Unit =      {}
  end mouseAt
  override def mousePress(x: Int, y: Int): Unit =      {}
  end mousePress
  override def mouseRelease(x: Int, y: Int): Unit =      {}
  end mouseRelease
end FireEffect