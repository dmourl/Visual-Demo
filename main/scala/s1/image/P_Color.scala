package s1.image

import math._

/**
 * The class [[P_Color]] holds a single color consisting of an alpha component (transparency) and red, green and blue components.
 * [[P_Color]] objects are immutable, so you can reuse them freely.
 *
 * @constructor Creates a new color
 * @param red Red value   [0, 255]
 * @param green Green Value [0, 255]
 * @param blue Blue Value  [0, 255]
 * @param alpha Transparency  [0, 255], where 255 is opaque, 0 is completely transparent
 * 
 * {{{import s1.image.P_Color}}}
 * 
 */

class P_Color(red: Int, green: Int, blue: Int, alpha: Int = 255):

  // Make using the existing method easier
  import P_Color.{clamp => cl}

  /** Alpha component */
  val a = alpha

  /** Red component */
  val r = red

  /** Green component */
  val g = green

  /** Blue Component */
  val b = blue

  /**
   * Clamps the parameter values to be in the range [0 .. 255]
   *
   * @return a new [[P_Color]] object with component values clamped to  0 ... 255
   */
  def clamp: P_Color = new P_Color(cl(r), cl(g), cl(b), a)

  /**
   * This one value contains the whole color in one Int, it is mostly used when painting the image
   */
  val argb = a << 24 | r << 16 | g << 8 | b

  /**
   * Sums two colors by summing their RGB components, the resulting color is opaque
   */
  def +(another: P_Color) = P_Color(this.r + another.r, this.g + another.g, this.b + another.b)

  /**
   * Subtracts two colors by subtracting their RGB components, the resulting color is opaque
   */
  def -(another: P_Color) = P_Color(this.r - another.r, this.g - another.g, this.b - another.b)

  /**
   * Multiplies the components of a color by a double value, creating a new color
   */
  def *(value: Double) =
    new P_Color(
      (this.r * value).toInt,
      (this.g * value).toInt,
      (this.b * value).toInt)

  def *(values: (Double, Double, Double)) = new P_Color((this.r * values._1).toInt, (this.g * values._2).toInt, (this.b *  values._3).toInt)

  def ==(another: P_Color) = this.argb == another.argb
  def !=(another: P_Color) = this.argb != another.argb

  override def toString = s"r: $r, g: $g, b: $b, argb: $argb"

end P_Color



object P_Color:

   def clamp(i : Int) : Int = min(max(i, 0), 255)

   def apply(argb: Int) =
     val a =  argb >>> 24  & 0xFF
     val r = (argb >>> 16) & 0xFF
     val g = (argb >>>  8) & 0xFF
     val b =  argb         & 0xFF
     new P_Color(r, g, b, a)

   def apply(red: Int, green: Int, blue: Int, alpha: Int) = new P_Color(red, green, blue, alpha)
   def apply(red: Int, green: Int, blue: Int) = new P_Color(red, green, blue)

end P_Color
