from Models.Models import *
from moviepy.editor import *
import os

videoclip = VideoFileClip("sample.mp4")   #Video
#audio = videoclip.audio     #Sonido como Variable
background_music = AudioFileClip("olas2.mp3") # Leer audio nuevo
new_clip = videoclip.set_audio(background_music) # Cambiar audio del video original por audio nuevo
new_clip.write_videofile("sample.mp4") #Guardar nuevo video