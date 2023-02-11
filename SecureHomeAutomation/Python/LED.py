import RPi.GPIO 
from time import sleep # Import the sleep function from the time module

RPi.GPIO.setwarnings(False) # Ignore warning for now
RPi.GPIO.setmode(RPi.GPIO.BCM) # Use physical pin numbering
RPi.GPIO.setup(17, RPi.GPIO.OUT, initial=RPi.GPIO.LOW) # Set pin 8 to be an output pin and set initial value to low (off)

def blinking():
    while True:
        RPi.GPIO.output(17, RPi.GPIO.HIGH)
        sleep(1)
        RPi.GPIO.output(17, RPi.GPIO.LOW)

        sleep(1) # Sleep for 1 second

