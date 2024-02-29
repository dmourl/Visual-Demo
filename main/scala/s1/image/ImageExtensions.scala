package s1.image

import scalafx.scene.image.{Image, WritableImage, PixelReader, PixelWriter}
import scalafx.scene.canvas.GraphicsContext



package object ImageExtensions:

   /**
    * This allows easier creation of a [[WritableImage]]
    *
     {{{
        val example = emptyImage(100,100)
     }}}
     @param width the width of the image
     @param height the height of the image
     @return a new, completely transparent image (All pixels are initially Color(0,0,0,0))
     */

  def emptyImage(width: Int, height: Int) = WritableImage(width, height)



  // Extensions are essentially auxiilary methods for the class that is specified as a parameter.
  // E.g., the copy method below is now a new method of the class Image if you import it (import s1.image.ImageExtensions.copy).

  extension (img: Image)

     /**
       * Create a copy of the caller [[Image]].
       *
       * @return a [[WritableImage]] that is a copy of the caller
       */
    def copy: WritableImage = 
      val w = img.width.toInt
      val h = img.height.toInt
      try
        val pr = img.pixelReader.get
        val copyImg = WritableImage(w, h)
        val pw = copyImg.pixelWriter

        for
          y <- 0 until h
          x <- 0 until w
        do
          val c = pr.getArgb(x,y)
          pw.setArgb(x,y,c)
        
        copyImg
      catch
        case _ => throw Exception("Something went wrong when copying an image")

    /**
      * Write the caller [[Image]] onto a section of the target [[WritableImage]].
      * I.e., essentially copy an [[Image]] to a section of a possibly bigger [[WritableImage]].
      * The section of the target where the image should be written is given by the parameters.
      * 
      * This method always tries to copy the entire caller [[Image]], 
      * i.e., the width and height of the written section are taken from the caller [[Image]].
      * If the caller [[Image]] does not fit into the target, the result is that only a portion of 
      * the caller is written to the target.
      *
      * @param target the target image where this caller image should be written
      * @param x the x-coordinate of the upper left corner of the target section
      * @param y the y-coordinate of the upper left corner of the target section
      */
    def writeOnto(target: WritableImage, x: Int, y: Int): Unit =
      val w = img.width.toInt
      val h = img.height.toInt
      val target_w = target.width.toInt
      val target_h = target.height.toInt

      try
        val pr = img.pixelReader.get
        val pw = target.pixelWriter

        for
          j <- 0 until math.min(y + h, target_h)
          i <- 0 until math.min(x + w, target_w)
        do
          val c = pr.getArgb(i,j)
          pw.setArgb(x+i,y+j,c)

      catch
        case _ => throw Exception("Something went wrong when copying an image")
    
  end extension


  extension (img: WritableImage)

    /**
      * Get the mutable [[Pixel]] from a given position.
      *
      * @param x the x-coordinate of the pixel
      * @param y the y-coordinate of the pixel
      * @return a mutable [[Pixel]] of the [[WritableImage]] that is located at the given coordinates
      */
    def apply(x: Int, y: Int): Pixel = 
      require(x >= 0)
      require(x < img.width.toInt)
      require(y >= 0)
      require(y < img.height.toInt)
      try
        Pixel(img.pixelReader.get, img.pixelWriter, x, y)
      catch
        case _ => throw Exception("Something went wrong when reading pixels of the image")
   
  end extension



  /**
    * The [[Pixel]] class represents a mutable pixel of a [[WritableImage]].
    * By calling the apply method, e.g.,
    * {{{
    *   val my_cute_pixel = my_cute_image(x, y)
    * }}}
    * you can get the pixels of an image (the image must be an instance of [[WritableImage]]) and 
    * modify them. By modifying the pixels, the original [[WritableImage]] is modified accordingly.
    *
    * import it by calling
    * {{{
    *   import s1.image.ImageExtensions.Pixel
    * }}}
    */
  class Pixel(pr: PixelReader, pw: PixelWriter, x: Int, y: Int):

    def opaque: Boolean = (pr.getArgb(x, y) & 0xFF000000) == 0xFF000000

    def color: P_Color = P_Color(pr.getArgb(x, y))

    def color_=(c: P_Color) = pw.setArgb(x, y, c.argb)

  end Pixel

end ImageExtensions