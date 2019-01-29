import skvideo.io
import cv2
import numpy as np
from time import time
import threading

def release_list(a):
   del a[:]
   del a

def importVideo(video):
    print("Importing...")
    Frames = []
    videogen = skvideo.io.vreader(video)

    cframe = 0 
    start_time = time()

    for frame in videogen:                    
        Frames.append(frame)
        if cframe == 50:
            break
        cframe+=1
    elapsed_time = time() - start_time
    print("Numero de frames: ", cframe)
    print("Tiempo transcurrido: %0.10f s." % elapsed_time)
    return Frames
##end def

def chooseFrames(video,start,end):
    print("Choosing...")
    Frames = []
    videogen = skvideo.io.vreader(video)
    data = skvideo.io.ffprobe(video)['video']
    rate = data['@r_frame_rate']
    rateint = int(rate[:2])
    vwidth, vheight  = data['@coded_width'] , data['@coded_height']

    cframe = 0 
    start_time = time()

    for frame in videogen:
        if cframe > start*rateint and cframe < end*rateint:                  
            Frames.append(frame)
        cframe+=1
    
    elapsed_time = time() - start_time
    print("Numero de frames: ", cframe)
    print("Tiempo transcurrido: %0.10f s." % elapsed_time)
    return Frames
##end def

def addImage(Frames,video,image,start,end,pos):
    print("Modifying...")
    videogen = skvideo.io.vreader(video)
    data = skvideo.io.ffprobe(video)['video']
    rate = data['@r_frame_rate']
    rateint = int(rate[:2])
    vwidth, vheight  = data['@coded_width'] , data['@coded_height']

    img = cv2.imread(image)
    iwidth, iheight, x = img.shape

    my, mx = pos[0],pos[1]

    awidth, aheight = mx+iwidth, my+iheight

    if awidth > int(vwidth):
        pass

    if aheight > int(vheight):
        pass

    cframe = 0 
    start_time = time()
    for h in range(0,len(Frames)):
        X = Frames[h]
        for i in range(0,iwidth):
            for j in range(0, iheight):
                X[j+my][i+mx] = img[j][i]    
        cframe+=1
    
    elapsed_time = time() - start_time
    print("Numero de frames: ", cframe)
    print("Tiempo transcurrido: %0.10f s." % elapsed_time)
    #return Frames
##end def

def saveVideo(video,vout,F):
    print("Saving...")
    videogen = video
    writer = skvideo.io.FFmpegWriter(vout)
    cframe = 0
    start_time = time()
    for frame in F:
        writer.writeFrame(frame)     
        cframe+=1
    elapsed_time = time() - start_time
    print("Numero de frames: ", cframe)
    print("Tiempo transcurrido: %0.10f s." % elapsed_time)
    writer.close()
##end def

def addImageThreads(Frames,video,image,start,end,pos):
    threads = []
    videogen = skvideo.io.vreader(video)
    data = skvideo.io.ffprobe(video)['video']
    rate = data['@r_frame_rate']
    rateint = int(rate[:2])
    dur = end - start
    s = 0
    e = rateint
    start_time = time()
    for i in range(0,dur):
        t = threading.Thread(target=addImage,args=(Frames[s:e],video,image,start,end,pos))
        threads.append(t)
        t.start()
        s = e 
        e = e +rateint 
    for i in range(0,len(threads)):
        threads[i].join()
    elapsed_time = time() - start_time
    print("Tiempo transcurrido: %0.10f s." % elapsed_time)
##end def

def bufferHandler(st,ed):
    dur = ed - st
    maxb = 10
    if dur < maxb:
        pass
    else:
        psec = 0
        while psec < 1:
            start = st
            end = st+maxb
            video = chooseFrames("sample.mp4",start,end)
            print(len(video))
            addImage(video,"sample.mp4","img.jpg",start,end,[200,300])
            saveVideo("sample.mp4","output.mp4", video)
            ##psec = psec + st +ed 
            psec+=1
            st = ed
            ed = ed + maxb
            release_list(video)

##end def

#video = importVideo("sample.mp4")
#video = chooseFrames("sample.mp4",start,end)
#print(len(video))
#addImage(video,"sample.mp4","img.jpg",start,end,[200,300])
#saveVideo("sample.mp4","output.mp4", video)

start = 10
end = 60
bufferHandler(start, end)
z = input()
