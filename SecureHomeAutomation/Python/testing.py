import pyrebase
import os
import pwinput

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

firebase=pyrebase.initialize_app(firebaseConfig)
auth=firebase.auth()

def save_values(email, pwd):
# opening file in write mode
    with open("values.txt", "w") as file:
        file.write(f"{email},{pwd}\n")
        print("Values saved successfully.")

def read_values():
    # opening file in read mode
    with open("values.txt", "r") as file:
        # reading the file
        content = file.read()
        # splitting the content to get individual values
        email, pwd = content.strip().split(",")
        return email, pwd
#Login function
id=""
def login():
    id=""
    print("Welcome to Secure Home Automation")
    print("Log in...")
    email=input("Enter email: ")
    pwd = pwinput.pwinput(prompt='Password: ', mask='')
    #login = auth.sign_in_with_email_and_password(email, pwd)
    #print("Successfully logged in!")
    #print(auth.current_user['localId'])
    #id= auth.current_user['localId']
    save_values(email, pwd)

    #return id
try:
    with open("values.txt", "r") as file:
        file.read()
        email,pwd = read_values()
        print(f"Read values from file: email={email}, pwd={pwd}")
        userInfo=auth.sign_in_with_email_and_password(email,pwd)
        print("User Successfully Logged In")
        id= auth.current_user['localId']
        print(id)
        return id
except:
# if file is empty, ask user to enter values
    print("No val")
    login()