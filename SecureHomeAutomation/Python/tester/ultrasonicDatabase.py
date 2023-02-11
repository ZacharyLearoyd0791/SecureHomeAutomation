import pyrebase
from distance import distance
import RPi.GPIO as GPIO


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

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(20,GPIO.OUT)
GPIO.output(20,GPIO.LOW)
firebase = pyrebase.initialize_app(config)

while True:

    maxDist=20
    storage = firebase.storage()
    database = firebase.database()
    a = distance()    
    if a<maxDist:
        
        print("Someone has entered")
        print (a)
        database.child("Ultrasonic Sensor")
        data = {"distance": a}
        database.set(data)
        GPIO.output(20,GPIO.HIGH)
        break 
    else:
        print (a)
        database.child("Ultrasonic Sensor")
        data = {"distance": a}
        database.set(data)
        GPIO.output(20,GPIO.LOW)

	    
    
    
    


