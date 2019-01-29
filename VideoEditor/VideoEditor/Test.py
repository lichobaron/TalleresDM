import cv2

vidcap = cv2.VideoCapture('sample.mp4')
success,image = vidcap.read()
count = 0
success = True
while success:
    # save frame as JPEG file      
    success,image = vidcap.read()
    print('Read a new frame: ', success)
    count += 1