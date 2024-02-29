package s1.demo.effects

import s1.demo.Effect
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color


/**
  * In this effect, a square grows in size and the audio gets distorted.
  * 
  * The audio is distorted by increasing its speed every tick.
  */
class SquareGrowingDistortion extends Effect(500, 500, "SquareGrowingDistortion"):

  var clock = 0

  def tick() =

    clock += 1
    audioPlayer.foreach(_.setRelativeRate(1.0 + clock/100.0))
  end tick

  

  //------- drawing -------//
  def drawEffect(g: GraphicsContext): Unit = 

      g.setFill(Color.Burlywood)
      g.fillRect(0, 0, width, height)

      g.setFill(Color.Beige)
      g.fillRect(50, 50, (30 + clock/0.2), (30 + clock/0.2))

  end drawEffect

  def next = clock > 70
  
  override def onEnd(): Unit = audioPlayer.foreach(_.setRelativeRate(1.0))

end SquareGrowingDistortion
