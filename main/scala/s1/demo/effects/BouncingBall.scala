package s1.demo.effects

import s1.image.ImageExtensions.*
import s1.demo.*

import scala.util.Random
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color

/**
 * This simple effect features a bouncing ball
 */
class BouncingBall extends Effect(750, 750, "Bouncing ball"):

  // The ball has coordinates, size and speed. We also keep track whether the ball is going forwards or backwards
  class Ball(val x: Int, val y: Int, var xSpeed: Int, var ySpeed: Int, val ballSize: Int, val growing: Boolean)

  class BounceBorder(val x: Int, val y: Int, val size: Int)
  class Shadow(val x: Int, val y: Int)

  // ...and here is our initial instance
  val ySpeedInitial = 3
  val xSpeedInitial = 3
  var ball = new Ball(200, 200, xSpeedInitial, ySpeedInitial, 30, false)

  // Limits for ball sizes
  val minBallSize = ball.ballSize
  val maxBallSize = ball.ballSize + 75
  val diff = maxBallSize - minBallSize

  // How fast we go from the front to back
  val depthSpeed = 5

  val random = new Random

  // The strength of the bounce, see how the effect reacts when changing this value
  val bounceForce = -25
  var clock  = 0

  // The ball cannot go through this border
  var bounceBorder = BounceBorder(0, 0, width - depthSpeed * diff)

  //val wall = (bounceBorder.size - math.sqrt(2.0)*ball.ballSize).toInt
  val wall = bounceBorder.size - ball.ballSize

  val wallCoord = ((width - wall)/2.0).toInt

  /**
    * Draw the room and a ball (and its shadow) based on current ball position
    */
  def drawEffect(g: GraphicsContext): Unit = 

    // clear the previous drawing
    g.setFill(Color.White)
    g.fillRect(0, 0, width, height)

    // Set color for the lines and the ball
    g.setStroke(Color.Black)
    g.setFill(Color.Black)

    // Draw the image with this color
    g.strokeRect(wallCoord, wallCoord, wall, wall)
    g.strokeLine(0,0,wallCoord,wallCoord)
    g.strokeLine(width, 0, wallCoord + wall, wallCoord)
    g.strokeLine(0, height, wallCoord, wallCoord + wall)
    g.strokeLine(width, height, wallCoord + wall, wallCoord + wall)

    //bounceBox, uncomment the below line to see the border for the ball!
    //g.strokeRect(bounceBorder.x, bounceBorder.y, bounceBorder.size, bounceBorder.size)

    // shadow
    g.fillOval(ball.x, bounceBorder.y + bounceBorder.size - 10, ball.ballSize, ball.ballSize - 25)

    // the actual ball
    g.fillOval(ball.x, ball.y, ball.ballSize, ball.ballSize)

  end drawEffect

  
  /**
   * Here we modify the state (the position and speed of the ball)
   */
  def tick() =
    clock += 1

    // Set new variables
    var y = ball.y + ball.ySpeed
    var x = ball.x + ball.xSpeed
    var ax = 0
    var ay = 0
    var borderSize = bounceBorder.size

    // Set the values for growing and ballSize in the next frame
    val (nextGrowing, nextBallSize) =
      if ball.growing && ball.ballSize < maxBallSize then
        borderSize += depthSpeed
        (ball.growing, ball.ballSize + 1)
      else if !ball.growing && ball.ballSize > minBallSize then
        borderSize -= depthSpeed
        (ball.growing, ball.ballSize - 1)
      else
        (!ball.growing, ball.ballSize)

    // Adjust the ball speed slightly every five frames
    if ball.ballSize % 5 == 0 then
      if ball.ySpeed > 0 then ay = 1 else ay = -1
      if ball.xSpeed > 0 then ax = 1 else ax = -1
      if !ball.growing then
        ay *= -1
        ax *= -1
    end if

    // Set new values for coordinates and speed
    // When ball hits the border fix the coordinates to the edge of the border
    val (nextY, nextYSpeed) =
      if y > ((bounceBorder.y + bounceBorder.size) - nextBallSize) then
        ((bounceBorder.y + bounceBorder.size) - nextBallSize, -ball.ySpeed + 2)
      else if y < bounceBorder.y then
        (bounceBorder.y, -ball.ySpeed)
      else
         (y, ball.ySpeed + 4 + ay)

    val (nextX, nextXSpeed) =
      if x > ((bounceBorder.x + bounceBorder.size) - nextBallSize) then
        ((bounceBorder.x + bounceBorder.size) - nextBallSize, -ball.xSpeed)
      else if x < bounceBorder.x then
        (bounceBorder.x, -ball.xSpeed)
      else
        (x, ball.xSpeed + ax)

    // Update the border
    val bx = ((width - bounceBorder.size)/2.0).toInt
    val by = ((height - bounceBorder.size)/2.0).toInt

    bounceBorder = BounceBorder(bx, by, borderSize)

    // We could have done this with a ball with it's coordinates in var's
    // It can also be done in a more functional way, replacing the ball
    // itself
    ball = new Ball(nextX, nextY, nextXSpeed, nextYSpeed, nextBallSize, nextGrowing)

  end tick

  def next = clock > 300

end BouncingBall
