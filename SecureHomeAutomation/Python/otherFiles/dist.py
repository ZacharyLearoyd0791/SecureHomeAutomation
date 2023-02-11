#Libraries
import RPi.GPIO as GPIO
import time
from time import sleep # Import the sleep function from the time module
import RPi.GPIO as GPIO
keyRead=("/users/"+id+"/userData/Light Status")
key="/users/"+id+"/Raspberry/"+myserial+"/Ultrasonic/"



GPIO.setmode(GPIO.BCM)
 
GPIO_TRIGGER = 4
GPIO_ECHO = 23
GPIO.setup(24,GPIO.OUT)
GPIO.output(24,GPIO.LOW)
 
GPIO.setup(GPIO_TRIGGER, GPIO.OUT)
GPIO.setup(GPIO_ECHO, GPIO.IN)
 

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
            database.child(DistKey)
            database.set(roundDist)
            database.child(LightKey)
            database.set("On")
            #time.sleep(10) 
        #else:
         #   time.sleep(1)
            #   print("no data sending to db")
def distanceRun():
    distanceOut()
