import RPi.GPIO as GPIO
import time



GPIO.setmode(GPIO.BOARD)

GPIO.setup(21,GPIO.IN)
a=1
b=4
while a<b:

    var1=GPIO.input(21)
    if var1:
        print("high")
        var1=GPIO.LOW
    else:
        print("low")
    a=a+1
GPIO.cleanup()
