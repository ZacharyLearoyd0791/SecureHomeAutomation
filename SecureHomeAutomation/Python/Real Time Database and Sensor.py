#Libraries
import RPi.GPIO as GPIO
import time
from datetime import datetime
from time import sleep # Import the sleep function from the time module
from LED import *
import threading
import pyrebase
import RPi.GPIO as GPIO
from os import system
from distance import *
from subprocess import PIPE, run
from LoginLocalFirebase import checkUser


config = {
    "apiKey": "AIzaSyCUr-GKbaSz5KjHYe4ys3-MYzUEwqkC1wo",
    "authDomain": "homeautomation-90233.firebaseapp.com",
    "databaseURL": "https://homeautomation-90233-default-rtdb.firebaseio.com",
    "projectId": "homeautomation-90233",
    "storageBucket": "homeautomation-90233.appspot.com",
    "messagingSenderId": "831662568562",
    "appId": "1:831662568562:web:d1f0d87c12246a4185beeb",
    "measurementId": "G-GC600XPF6E"

};


try:
    id=checkUser()
    #print("User ID is: "+id)
    print ("Connected to Database. Starting to Read and Write to Database under your Account!")
except:
    print("No user info found. Make sure you are registered using our app.")
    exit (0)

def getserial():
  # Extract serial from cpuinfo file
  cpuserial = "0000000000000000"
  try:
    f = open('/proc/cpuinfo','r')
    for line in f:
      if line[0:6]=='Serial':
        cpuserial = line[10:26]
    f.close()
  except:
    cpuserial = "ERROR000000000"
 
  return cpuserial

myserial=getserial()
print("\nRaspberry Pi 4:\n Serial Number: ",myserial,"\n\nSecure Home Automation")


#Keys:

LightKey=("/users/"+id+"/userData/Light Status")
DoorKey=("/users/"+id+"/userData/Door status:")
MinKey=("/users/"+id+"/userData/Temperature/Min")
MaxKey=("/users/"+id+"/userData/Temperature/Max")
WindowKey=("/users/"+id+"/userData/Windows Sensor/Activities")
DistKey="/users/"+id+"/Raspberry/"+myserial+"/Ultrasonic/"

#print(key)#to print the key to test if it is sending to the right database key or not

#Database:
sensorfirebase = pyrebase.initialize_app(config)
sensorstorage = sensorfirebase .storage()
database = sensorfirebase .database()

#keys for read and write:
keyRead=("/users/"+id+"/userData/Light Status")
key="/users/"+id+"/Raspberry/"+myserial+"/Ultrasonic/"

#GPIO
#Rpi.GPIO.setup(24,OUTPUT)
RPi.GPIO.setwarnings(False) # Ignore warning for now
RPi.GPIO.setmode(RPi.GPIO.BCM) # Use physical pin numbering
RPi.GPIO.setup(24, RPi.GPIO.OUT) # Set pin 8 to be an output pin and set initial value to low (off)


def distanceOut():
    while True:
        dist = distance()
        #print ("Measured Distance = %.1f cm" % dist)
        time.sleep(1)
        if dist <20:
            print("Max Distance")
            now = datetime.now() # current date and time
            DateTime= now.strftime("%m-%d-%Y, %H:%M:%S")
            TimeKey=DistKey+"/"+DateTime
            #print ("\nMeasured Distance = %.1f cm" % dist)
            database.child(TimeKey)
            database.set("Sensor Triggered")
            database.child(LightKey)
            database.set("On")
             
        #else:
         #   time.sleep(1)
            #   print("no data sending to db")
def distanceRun():
    distanceOut()



def DistDB():
    while True:
        dataread=database.child(LightKey).get()
        #print(dataread.val())
        databaseVal=str(dataread.val())
        #print(databaseVal)
        if databaseVal == "On":
            print("\nLight is on")
            RPi.GPIO.output(24, RPi.GPIO.HIGH)
            time.sleep(1)
        if databaseVal == "Off":
            print("\nLight is off")
            RPi.GPIO.output(24, RPi.GPIO.LOW)
            time.sleep(1)

def DoorDB():
    while True:
        dataread=database.child(DoorKey).get()
        databaseVal=str(dataread.val())
        if databaseVal == "Unlocked":
            print("\nDoor is Unlock")

            #RPi.GPIO.output(24, RPi.GPIO.HIGH)
            time.sleep(1)
        if databaseVal == "Locked":
            #RPi.GPIO.output(24, RPi.GPIO.LOW)
            print("\nDoor is Lock")

            time.sleep(1)
        
        #    print(databaseVal)

def TempDB():
    while True:
        minRead=database.child(MinKey).get()
        maxRead=database.child(MaxKey).get()
        minVal=str(minRead.val())
        maxVal=str(maxRead.val())
        print ("\nMin value set is: ",minVal,"\nMax value set is: ",maxVal)
        time.sleep(1)

        #Code below is for setting temp!

def WindowDB():
    while True:
        dread=database.child(DoorKey).get()
        dbV=str(dread.val())
        #code below is for setting the alarm!
        #if dbV is None:
        #    print("\nNo Values for Windows\n")
        #else:
         #   print("\nStatus code of Device: ",dbV)
        time.sleep(1)

def run():
    
    Thread1 = threading.Thread(target=distanceOut)

    Thread2 = threading.Thread(target=blinking)
    
    Thread3 = threading.Thread(target=DistDB)

    Thread4 = threading.Thread(target=DoorDB)
    
    Thread5 = threading.Thread(target=TempDB)
    
    Thread6 = threading.Thread(target=WindowDB)

    
    Thread1.start()

    Thread2.start()

    Thread3.start()
    
    Thread4.start()

    Thread5.start()

    Thread6.start()

    
    Thread1.join()
    Thread2.join()
    Thread3.join()
    Thread4.join()
    Thread5.join()
    Thread6.join()


    
try:
    run()
except KeyboardInterrupt:
    print("Measurement stopped by User")
    
    exit(0);

    
