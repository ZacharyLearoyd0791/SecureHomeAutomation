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
from subprocess import PIPE, run
import sys
from PyQt5.QtWidgets import QApplication, QWidget, QLabel, QLineEdit, QPushButton, QVBoxLayout
import pyrebase
import os
import maskpass

firebaseConfig= {
    "apiKey": "AIzaSyCUr-GKbaSz5KjHYe4ys3-MYzUEwqkC1wo",
    "authDomain": "homeautomation-90233.firebaseapp.com",
    "databaseURL": "https://homeautomation-90233-default-rtdb.firebaseio.com",
    "projectId": "homeautomation-90233",
    "storageBucket": "homeautomation-90233.appspot.com",
    "messagingSenderId": "831662568562",
    "appId": "1:831662568562:web:d1f0d87c12246a4185beeb",
    "measurementId": "G-GC600XPF6E"
   
};
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

 
firebase=pyrebase.initialize_app(firebaseConfig)
auth=firebase.auth()

# Create the main window
app = QApplication(sys.argv)
window = QWidget()

# Create the Email and password input fields
email_label = QLabel(window)
email_label.setText("Email:")
email_entry = QLineEdit(window)
password_label = QLabel(window)
password_label.setText("Password:")
password_entry = QLineEdit(window)
password_entry.setEchoMode(QLineEdit.Password)

# Create a function to check if the Email and password are not empty
def check_credentials():
  email = email_entry.text()
  password = password_entry.text()
  if not email or not password:
    # If either the Email or password is empty, show an error message and clear the fields
    error_label = QLabel(window)
    error_label.setText("Error: Email and password cannot be empty")
    email_entry.clear()
    password_entry.clear()
  else:
    # If the Email and password are not empty, proceed with the login
    id=""
    print("Welcome to Secure Home Automation")
    email=email
    
    pwd=password
    print("Email: ",email,"\nPassword",pwd)
#    print('Password : ', pwd)
    

    login = auth.sign_in_with_email_and_password(email, pwd)
    print("Successfully logged in!")
    print(auth.current_user['localId'])
    id= auth.current_user['localId']
    Run(id)
    window.hide()
    return id

# Create the submit button
submit_button = QPushButton(window)
submit_button.setText("Submit")
submit_button.clicked.connect(check_credentials)

# Create the layout
layout = QVBoxLayout()
layout.addWidget(email_label)
layout.addWidget(email_entry)
layout.addWidget(password_label)
layout.addWidget(password_entry)
layout.addWidget(submit_button)

# Set the layout and show the window
window.setLayout(layout)
window.show()

def Run(id):
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
    LightKey=("/users/"+id+"/userData/Light Status")
    DoorKey=("/users/"+id+"/userData/Door status:")
    MinKey=("/users/"+id+"/userData/Temperature/Min")
    MaxKey=("/users/"+id+"/userData/Temperature/Max")
    WindowKey=("/users/"+id+"/userData/Windows Sensor/Device Status Code")

    DistKey="/users/"+id+"/Raspberry/"+myserial+"/Ultrasonic/"

#print(key)#to print the key to test if it is sending to the right database key or not
    sensorfirebase = pyrebase.initialize_app(config)

    sensorstorage = sensorfirebase .storage()
    database = sensorfirebase .database()

    keyRead=("/users/"+id+"/userData/Light Status")
    key="/users/"+id+"/Raspberry/"+myserial+"/Ultrasonic/"

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
            if dist <20:
                now = datetime.now() # current date and time
                DateTime= now.strftime("%m-%d-%Y, %H:%M:%S")
                TimeKey=DistKey+"/"+DateTime
                print ("Movement detacted at ",DateTime,"\nMeasured Distance = %.1f cm" % dist)
                database.child(TimeKey)
                database.set(roundDist)
                database.child(LightKey)
                database.set("On")
                #time.sleep(10) 
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
                #print("Database reads on")
                RPi.GPIO.output(24, RPi.GPIO.HIGH)
                time.sleep(1)
            if databaseVal == "Off":
                #print("Database reads off")
                RPi.GPIO.output(24, RPi.GPIO.LOW)
                time.sleep(1)

    def DoorDB():
        while True:
            dataread=database.child(DoorKey).get()
            databaseVal=str(dataread.val())
            if databaseVal == "Unlocked":
                print("\nUnlock")

                #RPi.GPIO.output(24, RPi.GPIO.HIGH)
                time.sleep(1)
            if databaseVal == "Locked":
                #RPi.GPIO.output(24, RPi.GPIO.LOW)
                print("\nLock")

                time.sleep(1)
            
            #    print(databaseVal)

    def TempDB():
        while True:
            minRead=database.child(MinKey).get()
            maxRead=database.child(MaxKey).get()
            minVal=str(minRead.val())
            maxVal=str(maxRead.val())
            print ("\nMin value set is: ",minVal,"\nMax value set is: ",maxVal)

            #Code below is for setting temp!

    def WindowDB():
        while True:
            dataread=database.child(DoorKey).get()
            databaseVal=str(dataread.val())
            #code below is for setting the alarm!
            print("\nStatus code of Device: ",databaseVal) 
    def getTime():
        while True:
            timeNow=time_now = datetime.now().strftime("%H:%M:%S")
            print(timeNow)
            time.sleep(1)
        # Create the main btnWindow


# Run the main loop
    def run():
        
        Thread1 = threading.Thread(target=distanceRun)

        Thread2 = threading.Thread(target=blinking)
        
        Thread3 = threading.Thread(target=DistDB)

        Thread4 = threading.Thread(target=DoorDB)
        
        Thread5 = threading.Thread(target=TempDB)
        
        Thread6 = threading.Thread(target=WindowDB)

        #Thread7 = threading.Thread(target=getTime)
        
        Thread1.start()

        Thread2.start()

        Thread3.start()
        
        Thread4.start()

        Thread5.start()

        Thread6.start()

        #Thread7.start()
        
        Thread1.join()
        Thread2.join()
        Thread3.join()
        Thread4.join()
        Thread5.join()
        Thread6.join()
        #Thread7.join()


        
    try:
        run()
    except KeyboardInterrupt:
        print("Measurement stopped by User")
        
        exit(0);
app = QApplication(sys.argv)
btnWindow = QWidget()

# Create the buttons
button1 = QPushButton(btnWindow)
button1.setText("Button 1")
button2 = QPushButton(btnWindow)
button2.setText("Button 2")
button3 = QPushButton(btnWindow)
button3.setText("Button 3")
button4 = QPushButton(btnWindow)
button4.setText("Button 4")

# Create the layout
horizontal_layout = QHBoxLayout()
horizontal_layout.addWidget(button1)
horizontal_layout.addWidget(button2)
vertical_layout = QVBoxLayout()
vertical_layout.addLayout(horizontal_layout)
vertical_layout.addWidget(button3)
vertical_layout.addWidget(button4)

# Set the layout and show the btnWindow
btnWindow.setLayout(vertical_layout)
btnWindow.show()


# Run the main loop
sys.exit(app.exec_())