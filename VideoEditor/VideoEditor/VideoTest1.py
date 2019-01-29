import skvideo.io
import cv2
import numpy as np
from moviepy.editor import *
from time import time

def openVideo(filename):
    return skvideo.io.vreader(filename)
##end def

def getVideoData(filename):
    data = skvideo.io.ffprobe(filename)['video']
    rate = data['@r_frame_rate']
    frames = data['@nb_frames']
    rateint = int(rate[:2])
    vwidth, vheight  = int(data['@coded_width']) , int(data['@coded_height'])
    return [rateint, vwidth, vheight, frames]
##end def

def openImage(filename):
    return cv2.imread(filename)
##end def

def getImageData(image):
    iwidth, iheight, depht = image.shape
    return [iwidth,iheight,depht]
##end def

def setNewAudio(filename,audiofile):
    videoclip = VideoFileClip(filename)
    newaudio = AudioFileClip(audiofile)
    new_clip = videoclip.set_audio(newaudio)
    new_clip.write_videofile(filename)
##end def

def setOriginalAudio(video,tempfile):
    videoclip = VideoFileClip(video)
    videoclip2 = VideoFileClip(tempfile)
    new_clip = videoclip2.set_audio(videoclip.audio)
    new_clip.write_videofile("temp.mp4")
    os.remove(tempfile)
    os.rename("temp.mp4",tempfile)
##end def

def addImage(video,videotemp,img,start,end,pos):
    print("Adding Image...")
    reader = skvideo.io.FFmpegReader(video)
    rateint = getVideoData(video)[0]
    writer = skvideo.io.FFmpegWriter(videotemp)
    image = openImage(img)
    cframe = 0 
    start_time = time()
    for frame in reader.nextFrame():
        if cframe >= start*rateint and cframe < end*rateint:
            addImageHelper(frame,image,pos,getVideoData(video))
            writer.writeFrame(frame)
        else:
            ##pass
            writer.writeFrame(frame)
        cframe+=1
    writer.close()
    setOriginalAudio(video,videotemp)
    elapsed_time = time() - start_time
    print("Numero de frames: ", cframe)
    print("Tiempo total transcurrido: %0.10f s." % elapsed_time)
##end def

def addImageHelper(frame,image,pos,videoinfo):
    iwidth = getImageData(image)[0]
    iheight = getImageData(image)[1]
    vwidth = videoinfo[1]
    vheight = videoinfo[2]
    y, x = pos[0],pos[1]
    
    #start_time = time()
    for i in range(0,iwidth):
        for j in range(0, iheight):
            if j + x < vheight and i + y < vwidth:
                frame[j+x][i+y] = image[j][i]    
    #elapsed_time = time() - start_time
    #print("Tiempo transcurrido: %0.10f s." % elapsed_time)
##end def

def clearMemory(L):
   del L[:]
   del L
##end def

stime = 0
etime = 2
x = 1100#widht
y = 600#height
addImage("sample.mp4","sample - copia.mp4","imagen.jpg",stime,etime,[x,y])
#setOriginalAudio("sample.mp4","sample - copia.mp4")
#setNewAudio("sample.mp4","olas2.mp3")