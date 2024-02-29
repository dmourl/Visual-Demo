================================================================
----------------------------------------------------------------

Important:

These 'hidden' files are hidden because they were deemed a bit 
complicated for the students. Thus, understanding and studying 
these files is completely OPTIONAL.

If you are interested in using audio data for your effect,
you are free (and encouraged!) to look into these files :)

Note: don't spend too much energy with this! This can get 
      quite tedious and tiresome :) (sorry!)

----------------------------------------------------------------
================================================================


Waveform.scala is an Effect that is essentially a visualiser tool for the audio spectrum data.
There is more information on this in the actual file.

AudioEffect.scala is an Effect that works as an example on how to utilize the audio spectrum data.
The effect also showcases how audio markers can be utilized.
The markers are essentially audio timestamps that fire off some user-given functionality when the timestamp is reached in the audio.



Notes on using audio spectrum data:

 If you are using the [[AudioPlayer]]'s [[getAudioAmplitudes]] for spectrum data & audio syncing, 
 it is best to not change the volume (originally set to 100) 
 since this affects the magnitudes of the spectrum, 
 and the Waveform y-axis is no longer accurate.

 note:    changing the volume affects the audio spectrum
          i.e., you get amplitudes of 0 with volume 0

 note:    the axis values for the Waveform tool are calculated w.r.t volume=100

 note:    changing the rate of the music does not affect the amplitudes
