package s1.demo.effects

import s1.demo.Effect
import s1.image.ImageExtensions.*

import scala.collection.mutable.{Buffer, Queue}
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color

/**
 * This is a classic visual effect that has been used
 * in demos and old video games
 *
 * The effect creates horizontal lines that move
 * up and down. Read more here: https://en.wikipedia.org/wiki/Raster_bar
 */


class RasterBar extends Effect(1000, 1000, "Raster Bar"):

  /**
   * There are two classes for creating the effect:
          Rectangle class is for one rectangle within the Bar itself,
          Bar consists of multiple rectangles that are stacked on top of each other.
   */
  class Rectangle (var x: Int, var y: Int, val width: Int, val height: Int)
  class Bar(var rectangles: Buffer[Rectangle], var dir_down: Boolean, var speed: Int)

  /**
   * Here the initial bars are created.
   * Initially the buffer containing the rectangles is empty,
   * and the direction of the bars is upwards.
   */ 
  val bar1      = Bar(Buffer[Rectangle](), false, 5)
  val bar2      = Bar(Buffer[Rectangle](), false, 12)
  val bar3      = Bar(Buffer[Rectangle](), false, 8)
  val bar4      = Bar(Buffer[Rectangle](), false, 20)
  val bar5      = Bar(Buffer[Rectangle](), false, 6)

  // Buffer for storing multiple bars
  val bars      = Buffer[Bar]()

  // Width and height of a single rectangle
  val recWidth         = width
  val recHeight        = height / 50

  // Create the first bar with the function declared below
  createBar(bar1)

  // initiate a variable that holds the number of ticks called (i.e., a clock)
  var clock = 0

  /**
    * Helper function to create a single bar.
    * Creates 5 Rectangles and adds them to the given Bar object.
    * The created Bar is stored to the bars Buffer.
    */
  def createBar(bar: Bar) =

    // The first rectangle's top left corner
    var y = height

    for i <- 0 until 5 do

      // Add a new rectangle to the variable
      bar.rectangles += new Rectangle(0, y, recWidth, recHeight)
      // Next rectangle is built below the previous one
      y -= recHeight
    end for

    // Add the complete bar to the bars buffer
    bars += bar
  end createBar

  //------ update the states of each bar (update their positions and directions) ------//
  def tick() =

    // Add new bars when the clock goes on
    if clock == 20 then
      createBar(bar2)
    if clock == 60 then
      createBar(bar3)
      createBar(bar5)
    if clock == 35 then
      createBar(bar4)

    // For each Bar check if the direction needs to change
    // Then change the y value for each of the rectangles according to the bar's speed
    for bar <- bars do
      if bar.rectangles.last.y >= height + 10 * recHeight then
        bar.dir_down = false
      if bar.rectangles.head.y <= -10 * recHeight then
        bar.dir_down = true

      if bar.dir_down then
        for rec <- bar.rectangles do
          rec.y += bar.speed
      else
        for rec <- bar.rectangles do
          rec.y -= bar.speed

    clock += 1
  end tick

  //------- drawing -------//
  def drawEffect(g: GraphicsContext): Unit = 
    
    g.setFill(Color.Black)
    g.fillRect(0, 0, width, height)

    // Container for the desired colors for the bars
    val colors = Vector(Color.Blue, Color.Yellow, Color.Green, Color.Red, Color.Orange)
    // Index in order to use one color for a single bar
    var colorIndex = 0

    // Iterate through the bars
    for bar <- bars do
      // Iterate through the rectangles of a single bar
      for r <- bar.rectangles do
        // For the middle rectangle set the color light gray
        if bar.rectangles.indexOf(r) == 2 then

          g.setFill(Color.LightGray.brighter)

        else if (bar.rectangles.indexOf(r) + 1) % 2 == 0 then

          // For the second and fourth rectangle set the color from the vector
          var color = colors(colorIndex)
          g.setFill(color)

        else

          // For the first and last rectangle make the color transparent
          val c = colors(colorIndex)
          val newColor = Color(c.getRed, c.getGreen, c.getBlue, 0.4)
          g.setFill(newColor)

        // Fill the rectangle with the color set above
        g.fillRect(r.x, r.y, r.width, r.height)
      // After one bar grow the index so that next bar has different color
      colorIndex += 1
  end drawEffect

  def next = clock > 300

end RasterBar