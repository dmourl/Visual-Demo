//package s1.demo.effects
//
//import s1.demo.*
//
//import scala.math.*
//import scala.util.Random
//import scalafx.scene.canvas.GraphicsContext
//import scalafx.scene.paint.Color
//import scalafx.Includes.*
//import Util.Vector3D
//import s1.image.P_Color.clamp
//import scalafx.scene.media.MediaMarkerEvent
//import scalafx.util.Duration
//
//import java.awt.image.BufferedImage
//import s1.*
//import s1.demo.Effect
//import s1.image.ImageExtensions.*
//
//import scala.util.Random
//import s1.demo.Effect
//import s1.image.P_Color.clamp
//import scalafx.scene.image.WritableImage
//
//import java.awt.image.{BufferedImage, RescaleOp}
//import javax.swing.plaf.metal.MetalIconFactory.PaletteCloseIcon
//import scala.collection.mutable.Buffer
//import java.awt.Graphics
//import java.awt.Color
//import javax.imageio.ImageIO
//import java.io.File
//import java.sql.DataTruncation
//import math.Fractional.Implicits.infixFractionalOps
//import math.Integral.Implicits.infixIntegralOps
//import math.Numeric.Implicits.infixNumericOps
//import scala.language.postfixOps
//
//
//
//class fireBlur extends Effect(500,500,"fireBlur"){
//  var buffer1 = Array.ofDim[Int](width,height)
//  var buffer2  = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB)
//  def setPixel(x:Int,y:Int,c:java.awt.Color) = buffer2.setRGB(x,y,c.getRGB())
//  var clock = 30
//
//  def lined(img: WritableImage): BufferedImage =
//        val w = width
//        val h = height
//
//        //val buffer1 = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB)
//        //val buffer2 = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB)
//
//
//        for x <- 0 until w do
//            val y = h - 1
//            val random = Random.nextInt(2) * 255
//            buffer1(y)(x) = random
//
//            val random2 = Random.nextInt(2) * 255
//            buffer1(y - 1)(x) = random2
//
//        for x <- 1 until w - 1 do
//            for y <- 0 until h - 3 do
//                val fire = (buffer1(y + 1)(x) + buffer1(y + 1)(x - 1) + buffer1(y + 2)(x) + buffer1(y + 1)(x + 1)) * 0.2478 // <- VAIHDA TUOTA VIKAA ARVOA VÄLILLÄ 0.245-0.255 SAAT ERI MUOTOISIJA LIEKKEJÄ. ARVOLLA 0.3 JOTAI JÄNNÄÄ TAPAHTUU
//                buffer1(y)(x) = fire.toInt
//
//                val c1: s1.image.P_Color = new s1.image.P_Color(clamp(fire.toInt * 5), clamp(fire.toInt * 1), clamp(fire.toInt * 4)) // <- muuttamalla kertoimia voit vaihtaa eri värien intensiteettiä
//
//                buffer2.setRGB(x, y, c1.argb)
//
//        buffer2
//
//
//  def tick() = ()
//
//  override def makePic() =
//    val photo1 = emptyImage
//    val photo2 = lined(emptyImage)
//    var temp = photo2
//    temp = buffer2
//    buffer2
//
///*
//    for x <- 1 until width do
//      for y <- 1 until height do
//        val index0 = x+y*width
//        val index1 = (x+1) + y *width
//        val index2 = (x-1) + y *width
//        val index3 = x + (y+1) *width
//        val index4 = x + (y-1) *width
//        val c1 = buffer1.setRGB(x,y,index1)
//        val c2 = buffer1.setRGB(x,y,index2)
//        val c3 = buffer1.setRGB(x,y,index3)
//        val c4 = buffer1.setRGB(x,y,index4)
//
//        val newC: Float = */
//
//
//  def newInstance = new fireBlur
//  def next = false
