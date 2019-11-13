import requests.packages.urllib3
requests.packages.urllib3.disable_warnings()

from firebase import firebase
from serial import Serial

import time
import serial

firebase = firebase.FirebaseApplication('https://binboxx-29985.firebaseio.com/')

port = 'COM5'

ard = serial.Serial(port,9600,timeout=5)
bin_no='bin9876'
val = "door_open"
while 1:
    msg = (ard.readline().strip())
    msg = msg.decode('utf-8')
    print ("Message from arduino: ")
    print (msg)
    if(msg == val):
        result = firebase.put('bin9876','condition','OPEN')
        ##print(result)
        time.sleep(5) 
    else:
        result = firebase.put('bin9876','condition','CLOSED')
        ##print(result)
    
    
    
