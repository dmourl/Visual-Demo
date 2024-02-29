package s1.demo.effects

import s1.demo.Effect
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color
import scalafx.scene.shape.*
import scalafx.scene.image.*
import scala.math.*
import scala.util.Random


  /** Tehtävänannosta 3D-starfield:
   * Tässä efektissä jokaisella pisteellä on x-, y- ja z- koordinaatti.
   * z-koordinaattia muutetaan vähitellen, ja x- ja y- koordinaatit
   * skaalataan seuraavasti: x_kuvassa = x * vakio / z.
   */
class PalloMeri extends Effect(width = 750,height = 750,name = "StarSky"):

  class Star(var x: Double, var y: Double):
    /**def starpic: Shape =
      val diameter = Random.between(0,allowedDiameter)
      val colors = Vector(Color.Blue, Color.Yellow, Color.Green, Color.Red, Color.Orange)
      val outline = Circle(diameter+(diameter/4), Color.White)
      val innerStar = Circle(diameter, colors(Random.between(0,5)))
      val finishedStar = Shape.intersect(outline,innerStar)
      finishedStar*/
    /**def updateLocation: Unit =
       x = x*2
       y = y*2*/

   // def updateLocation(): Unit =
    //      x += vx
    //      y += vy
  end Star

  def allowedDiameter: Int = 2

  val minimy = min(width,height)
    //minimy/starAmount

  val rand = new Random()
  var starAmount: Int = 10
  var stars =
    Seq.fill(starAmount)(Star(rand.between(0,width),rand.between(0,height)))

  var clock = 0
  var changingSpeed = 10

  def tick() =

      val speed = 2.0 // Adjust the speed as needed
      //var vx = speed * (if (Random.nextBoolean()) 1 else -1)
      //var vy = speed * (if (Random.nextBoolean()) 1 else -1)

      for star <- stars do
        star.y = rand.between(0,minimy) * speed
        star.x = rand.between(0,minimy) * speed
      clock += 1
      if clock%90 == 0 then
        changingSpeed = max(changingSpeed - 1,1)


  def drawEffect(g: GraphicsContext) =
    //a buffer for possible colors like in RasterBar
    //the colors are randomly picked from the buffer in this method
    //g.setFill(Color.Black)
    //g.fillRect(0,0,width,height)
    if clock % changingSpeed ==0 then
      g.setFill(Color.Black)
      g.fillRect(0,0,width,height)
      var colors = Vector(Color.Blue, Color.Yellow, Color.Green, Color.Red, Color.Orange)
     // g.setFill(Color.Black)
     //    g.fillRect(0, 0, width, height)
     //    for star <- stars do
     //      //colors(Random.between(0,4))
     //      g.setFill(Color.Pink)
     //      g.fillOval(star.x, star.y, 2, 2)
      val i: Int = 0
      var color = colors(Random.between(0,colors.length))
      for star <- stars do
        g.setFill(color)
        g.fillOval(star.x,star.y,40,40)
  end drawEffect

  def next = clock > 100

end PalloMeri




