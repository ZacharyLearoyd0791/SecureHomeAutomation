#Libraries
import RPi.GPIO as GPIO
import time
from time import sleep # Import the sleep function from the time module
from LED import *
import threading
import pyrebase
import RPi.GPIO as GPIO
from os import system
from subprocess import PIPE, run
from LoginLocalFirebase import login


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
    id=login()
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
#print("Raspberry Pi 4:\n Serial Number: ",myserial,"\nSecure Home Automation")
#id="101507953414540013396"
keyRead=("/users/"+id+"/userData/Light Status")
key="/users/"+id+"/Raspberry/"+myserial+"/Ultrasonic/"

#print(key)#to print the key to test if it is sending to the right database key or not
sensorfirebase = pyrebase.initialize_app(config)

sensorstorage = sensorfirebase .storage()
database = sensorfirebase .database()



firebase = pyrebase.initialize_app(config)
storage=firebase.storage()
database=firebase.database()
maxDist=20



GPIO.setmode(GPIO.BCM)
 
GPIO_TRIGGER = 4
GPIO_ECHO = 23
GPIO.setup(24,GPIO.OUT)
GPIO.output(24,GPIO.LOW)
 
GPIO.setup(GPIO_TRIGGER, GPIO.OUT)
GPIO.setup(GPIO_ECHO, GPIO.IN)
 
RPi.GPIO.setwarnings(False) # Ignore warning for now
RPi.GPIO.setmode(RPi.GPIO.BCM) # Use physical pin numbering
RPi.GPIO.setup(24, RPi.GPIO.OUT, initial=RPi.GPIO.LOW) # Set pin 8 to be an output pin and set initial value to low (off)



def distance():
    GPIO.output(GPIO_TRIGGER, True)
 
    time.sleep(0.00001)
    GPIO.output(GPIO_TRIGGER, False)
 
    StartTime = time.time()
    StopTime = time.time()
 
    while GPIO.input(GPIO_ECHO) == 0:
        StartTime = time.time()
 
    while GPIO.input(GPIO_ECHO) == 1:
        StopTime = time.time()
 
    TimeElapsed = StopTime - StartTime
    distance = (TimeElapsed * 34300) / 2
 
    return distance
def distanceOut():
    distance()

    while True:
        dist = distance()
        
        roundDist=round(dist,1)
        #time.sleep(1)
        if dist <maxDist:
            print ("Measured Distance = %.1f cm" % dist)
            database.child(key)
            database.set(roundDist)
            database.child(keyRead)
            database.set("On")
            #time.sleep(10) 
        #else:
         #   time.sleep(1)
            #   print("no data sending to db")



def readDB():
    while True:
        
        dataread=database.child(keyRead).get()
        #print(dataread.val())
        databaseVal=str(dataread.val())
        #print(databaseVal)
        if databaseVal == "On":
            #print("Database reads on")
            RPi.GPIO.output(24, RPi.GPIO.HIGH)
            time.sleep(1)
            
            
        if databaseVal == "Off":
            #print("Database reads off")
            RPi.GPIO.output(24, RPi.GPIO.LOW)

            time.sleep(1)


def run():
    
    Thread1 = threading.Thread(target=distanceOut)

    # Create another new thread
    Thread2 = threading.Thread(target=blinking)
    
    Thread3 = threading.Thread(target=readDB)


    # Start the thread
    Thread1.start()

    # Start the thread
    Thread2.start()

    Thread3.start()
    
    # Wait for the threads to finish
    Thread1.join()
    Thread2.join()
    Thread3.join()


    
try:
    run()
except KeyboardInterrupt:
    print("Measurement stopped by User")
    
    exit(0);

