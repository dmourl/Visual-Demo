package s1.demo.effects

import s1.image.ImageExtensions.*
import s1.demo.Effect

import scala.collection.mutable.Queue
import scalafx.scene.image.WritableImage
import scalafx.scene.text.Font
import scalafx.scene.paint.Paint
import scalafx.scene.text.FontWeight
import scalafx.scene.text.FontPosture
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color




/**
 * The idea for this effect came from Felix Bade.
 *
 * The effect draws a continuous stream of filled
 * circles that changes it's course randomly.
 */

class Snakes extends Effect(750, 750, "Snakes"):

  class Circle(val x: Int, val y: Int)

  /**
   * This variable could hold a background image if wanted
   * See [[Dissolve]] for an example on how to load image files
   */  
  val background: Option[WritableImage] = None

  /**
    * The circles we draw are in a queue.
    * The latest is always stored in [[last]]
    * The normal effect circles are stored in snakeCircles
    */
  var last         = new Circle(100, 100)
  val snakeCircles = Queue[Circle](last)
  val mouseCircles = Queue[Circle]()
  var direction    = 0.0
  val step         = 10
  var queueLength  = 1

  val random = new util.Random

  def tick() =
    // Change the direction where to draw the next circle randomly
    direction = direction + (random.nextDouble - 0.5);

    // Calculate the new coordinates
    val xdiff = (math.cos(direction) * step).toInt
    val ydiff = (math.sin(direction) * step).toInt

    val x = (last.x + xdiff + width) % width
    val y = (last.y + ydiff + height) % height
    last  = new Circle(x,y)

    // Store the circle in a queue (Think of a buffer where you add in one end and take stuff out from the other)
    snakeCircles.enqueue( last )
    queueLength += 1

    // If the queue gets big, we delete older circles for a fun effect
    while (queueLength > 600) do
      snakeCircles.dequeue()
      queueLength -= 1
  
  end tick


  //------- drawing -------//

  // Nice font for the christmas message :)
  val christmasFont  = Font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 60)

  def drawEffect(g: GraphicsContext): Unit = 

    g.setFill(Color.White)
    g.fillRect(0,0,width,height)
    background foreach (image => g.drawImage(image, 0, 0))

    // Draw all the circles.
    def drawCircle(x: Int, y: Int) =
      g.setFill(Color.White)
      g.fillOval(x - 20, y - 20, 40, 40)

      g.lineWidth = 3
      g.setStroke(Color.Black)
      g.strokeOval(x - 20, y - 20, 40, 40)

    for c <- snakeCircles do
      drawCircle(c.x, c.y)

    for c <- mouseCircles do
      drawCircle(c.x, c.y)

    g.setFont(christmasFont)

    // Notice the fake "shadow" under the text
    g.setFill(Color.Black)
    g.fillText("Hyvää joulua!", 24, 48)

    g.setFill(Color.Green)
    g.fillText("Hyvää joulua!", 22, 46)

  end drawEffect

  // Effects can also receive information on mouse movements.
  // When a mouse goes to ne coordinates this method gets called.

  // We use it to draw still more circles at the mouse location
  override def mouseAt(x: Int, y: Int) =
    mouseCircles enqueue new Circle(x,y)
    if (mouseCircles.length > 300) then
      mouseCircles.dequeue()

  end mouseAt

  // This effect will never end
  def next = false

end Snakes
