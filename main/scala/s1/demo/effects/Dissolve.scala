package s1.demo.effects

import java.io.FileInputStream
import s1.image.ImageExtensions._
import scala.util.Random
import s1.demo.Effect

import scalafx.scene.image.{Image, WritableImage}
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color

/**
 * This example demonstrates manipulating single pixels
 */
class Dissolve extends Effect(750, 750, "Dissolve"):

  // This variable is used to find out when to end this effect
  var clock = 0

  // Here we load the image we are going to manipulate
  var current: WritableImage = 

    // get the Aalto logo from the pictures directory
    val stream = FileInputStream("./pictures/Aalto_logo.png");
    val aaltoLogo = Image(stream)

    // set up an empty image that spans the whole width and height
    val start = emptyImage(width, height)

    // .. and draw the aalto logo on it
    aaltoLogo.writeOnto(start,0,0)

    start
  end current

  val random = new Random

  // A completely random color used later in the algorithm
  val transparent = s1.image.P_Color(0,0,0,0)

  // Clamp used to keep (randomly generated) pixel coordinates inside the image area
  def clamp(v: Int, maxValue: Int) = math.max(0, math.min(v, maxValue))

  def drawEffect(g: GraphicsContext): Unit = 
    g.setFill(Color.White)
    g.fillRect(0, 0, width, height)
    g.drawImage(current, 0, 0)

  end drawEffect

  /**
   * This method updates the state of the effect - In this particular algorithm it
   * actually manipulates the image.
   */
  def tick() =

    // The current bitmap image can be copied if we do not wish to edit the original instance.
    // (In this example editing the original would have been fine - this is to explain things)
    val nextImage = current.copy

    for (i <- 1 to 300) do
      // pick a random coordinate
      val x1 = random.nextInt(width)
      val y1 = random.nextInt(height)

      val n = nextImage(x1, y1)

      // ...and another one nearby
      val x2 = clamp(x1 - 10 + random.nextInt(20), width-1)
      val y2 = clamp(y1 - 10 + random.nextInt(20), height-1)

      // We can check if pixels are opaque
      if (nextImage(x1,y1).opaque && !nextImage(x2, y2).opaque) then

        // We can read and write the value of a pixel
        nextImage(x2, y2).color = nextImage(x1,y1).color

        // We can set the color of any pixel to a predefined color
        nextImage(x1, y1).color = transparent
      end if
    end for

    // This helper variable is used to check when to change effects
    clock += 1

    current = nextImage
  end tick

  // The effect ends when we have changed the model 300 times
  def next = clock > 300
  
end Dissolve