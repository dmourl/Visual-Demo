package s1.demo.effects.hidden

import s1.demo._
import s1.image.ImageExtensions.*
import s1.gui.DemoGUI
import scalafx.beans.property.StringProperty

import scala.collection.mutable.{Buffer, Queue}
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color
import scalafx.Includes._
import s1.gui.DemoGUI.waveformBand
import s1.gui.DemoGUI.waveformThres
import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global



/** STUDENTS DON'T HAVE TO TOUCH THIS CLASS OR UNDERSTAND IT
  * 
  * You are not required to understand this class! This is just a helper tool if 
  * you want to utilize audio spectrum data for syncing music with your effect :)
  * 
  * This tool is used by selecting it in the Effects list in the GUI ("Change Effect").
  * 
  * To include this in the Effects list, change the packaging to [[s1.demo.effects]] (first line of this file)
  * and move this to the effects folder (one level above the current residence)
  * 
  * 
  * ---------------------------------------------------------
  * 
  * 
  * The tool is a visualiser for the audio spectrum data.
  * 
  * You can utilize the spectrum data in your own effect with [[AudioPlayer]]'s [[getAudioAmplitudes]].
  * The returned vector (i.e., the audio amplitudes) is different every time the spectrum updates.
  * 
  * You can e.g. call audioPlayer.getAudioAmplitudes in your [[tick]] method to get the latest data.
  * See [[AudioEffect]] for an example effect that utilizes the spectrum data.
  * 
  */
class Waveform extends Effect(math.max(1100,(MainApp.audioPlayer.numOfBands / 128.0 * 800).toInt), 500, "Waveform"):

    var clock = 0

    override def onStart(): Unit = 
      audioPlayer.foreach(ap => 

        numOfBands = ap.numOfBands
        bands = Vector.fill(ap.numOfBands)(0f)
        
        val startTime = try DemoGUI.waveformStarttime.value.toDouble catch case _ => 0.0
        ap.startingTimestamp = startTime.s

        // force the change in starting time to become effective immediately by restarting the audioPlayer
        ap.stop()
        ap.play()
        )
    end onStart

    def tick() =

      clock += 1
      this.audioPlayer.foreach( ap => bands = ap.getAudioAmplitudes)
    end tick

    // note: these values are overwritten inside onStart() and tick() by the values in the AudioPlayer-class.
    var numOfBands = 128
    var bands = Vector.fill(128)(0f)

    val leftMargin = 70

    var toggler = true
    var second_toggler = false
    var color2 = Color.Black

    //------- drawing -------//
    def drawEffect(g: GraphicsContext): Unit = 

      // ----- BACKGROUND----- //
      g.setFill(Color.Burlywood)
      g.fillRect(0, 0, width, height)

      // ----- X-AXIS ----- //
      g.setStroke(Color.Black)
      g.setLineWidth(1)
      for (i <- 0 to (numOfBands-1)/5) do
        val t = leftMargin + 2
        if i % 2 == 0 then
          g.strokeText((i*5).toString, leftMargin + 25*i, 83, 22)
          g.strokeLine(t + 25*i, 97, t + 25*i, 90)
        else
          g.strokeLine(t + 25*i, 97, t + 25*i, 94)
      
      if (numOfBands-1) % 5 != 0 then
        // draw an indicator for the final index as well if not drawn in the previous loop
        g.strokeText((numOfBands-1).toString, leftMargin + 25*((numOfBands-1)/5) + 5*((numOfBands-1)%5), 83, 22)
        val t = leftMargin + 2 + 25*((numOfBands-1)/5) + 5*((numOfBands-1)%5)
        g.strokeLine(t, 97, t, 90)

      // ----- AMPLITUDE AXIS TEXT ----- //
      val textRotate = -90d
      g.rotate(textRotate)
      g.strokeText("Amplitude", -165, 25)
      g.rotate(-textRotate)
      
      // ----- Y-AXIS & HORIZONTAL LINES & BAND INDEX AXIS TEXT ----- //
      g.strokeText("Index of the band", leftMargin + 2, 60)
      for (i <- 0 until 25) do 
        if i % 2 == 0 then
          g.strokeLine(leftMargin - 10, 100 + 10*i, width - (80 * width/800.0), 100 + 10*i)
          g.strokeText((2*i).toString, leftMargin-30, 105+10*i, 15)
        else
          g.strokeLine(leftMargin - 6, 100 + 10*i, width - (80 * width/800.0), 100 + 10*i)
      
      // ----- TIMESTAMP ----- //
      var timeText = "Timestamp: "
      audioPlayer.foreach(timeText += _.timestamp.toSeconds().toString().take(7) + " s")

      g.strokeText(timeText, leftMargin + 500, 60)

      // ----- AMPLITUDE BARS FOR EACH BAND ----- //
      var color = Color.Beige
      for (i <- 0 until numOfBands) do 
        if (i % 5 == 0) then color = Color.Beige
        else color = color.darker

        g.setFill(color)
        g.fillRect(leftMargin + 5*i, 99, 4, bands(i)*5+1)
      
      // ----- RECTANGLE THAT CHANGES COLORS ON BEAT ----- //
      val threshold = try DemoGUI.waveformThres.value.toFloat.abs catch case _ => 60
      val bandIndex = try math.min(numOfBands-1,DemoGUI.waveformBand.value.toInt.abs) catch case _ => numOfBands-1
      if (bands(bandIndex) > threshold) then
        if toggler then
          toggler = false
          if second_toggler then
            color2 = Color.Black
          else color2 = Color.Beige
          second_toggler = !second_toggler
      if (bands(bandIndex) <= threshold)//math.max(0,threshold-2.5f))
        toggler = true

      g.setFill(color2)
      g.fillRect(width/2 - 30, height - 140, 60, 60)
      g.strokeText("change color\nwhen band (" + bandIndex +")\nsurpasses amplitude (" + threshold + ")", width/2 - 220, height-120)

      // ----- PAUSE BUTTON ----- //
      g.setFill(Color.Beige.darker)
      g.fillRect(width/2 + 200, height - 120, 60, 40)
      val pauseText = if audioPlayer.isDefined && audioPlayer.get.paused then "play" else "pause"
      g.strokeText(pauseText, width/2 + 210, height - 100, 40)

    end drawEffect

    // ----- PAUSE BUTTON FUNCTIONALITY WITH MOUSE INPUTS ----- //
    var overPauseButton = false
    override def mouseAt(x: Int, y: Int): Unit = 
      if (x > (width/2 + 200) && x < (width/2 + 260)
              && y > height - 120 && y < height - 80) then
        overPauseButton = true
      else
        overPauseButton = false

    override def mousePress(x: Int, y: Int): Unit = 
      if overPauseButton then audioPlayer.foreach(_.togglePause())

    // This effect will never end
    def next = false

    override def onEnd(): Unit = 
      audioPlayer.foreach(ap => ap.startingTimestamp = 0.s)
    
end Waveform
