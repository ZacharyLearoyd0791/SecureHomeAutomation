import tkinter as tk
import os
import ttkthemes
import pyrebase
from LoginLocalFirebase import *


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

root = tk.Tk()
root.title("Login")

ttkthemes.ThemedStyle().theme_use("arc")
doorStatus = "Locked"
def move_to_page1():
    
    doorStatus = "UnLocked"
    label1 = tk.Label(dashboard_frame, text=doorStatus)
    label1.grid(row=1, column=0, pady=10, padx=10)
    
    pass

def move_to_page2():
    pass

def move_to_page3():
    pass

def move_to_page4():
    pass

def sign_out():
    global dashboard_frame
    try:
        os.remove("login.txt")
    except:
        pass
    dashboard_frame.grid_forget()
    open_login()
    
def login():
    username = username_entry.get()
    password = password_entry.get()
    loginLocal(username,password)
    if auth.sign_in_with_email_and_password(username, password):
    
        # Successful login, move to dashboard
        with open("login.txt", "w") as f:
            f.write("loggedin")
        open_dashboard()
    else:
        # Unsuccessful login, display error message
        error_label.config(text="Invalid username or password.")

def open_login():
    global login_frame
    global username_entry
    global password_entry
    try:
        login_frame.grid_forget()
    except:
        pass
    login_frame = tk.Frame(root)
    login_frame.grid(row=0, column=0)

    username_label = tk.Label(login_frame, text="Username:")
    username_label.grid(row=0, column=0, pady=5, padx=5)

    username_entry = tk.Entry(login_frame)
    username_entry.grid(row=0, column=1, pady=5, padx=5)

    password_label = tk.Label(login_frame, text="Password:")
    password_label.grid(row=1, column=0, pady=5, padx=5)

    password_entry = tk.Entry(login_frame, show="*")
    password_entry.grid(row=1, column=1, pady=5, padx=5)

    error_label = tk.Label(login_frame, text="")
    error_label.grid(row=2, column=0, columnspan=2, pady=5, padx=5)

    login_button = tk.Button(login_frame, text="Login", command=login)
    login_button.grid(row=3, column=0, columnspan=2, pady=5, padx=5)

def open_dashboard():
    global dashboard_frame
    try:
        dashboard_frame.grid_forget()
    except:
        pass
    dashboard_frame = tk.Frame(root)
    dashboard_frame.grid(row=0, column=0)

    button1 = tk.Button(dashboard_frame, text="Door", command=move_to_page1)
    button1.grid(row=0, column=0, pady=10, padx=10)
    label1 = tk.Label(dashboard_frame, text=doorStatus)
    label1.grid(row=1, column=0, pady=10, padx=10)

    button2 = tk.Button(dashboard_frame, text="Light", command=move_to_page2)
    button2.grid(row=0, column=1, pady=10, padx=10)
    label2 = tk.Label(dashboard_frame, text="Description of page 2")
    label2.grid(row=1, column=1, pady=10, padx=10)

    button3 = tk.Button(dashboard_frame, text="Temp", command=move_to_page3)
    button3.grid(row=2, column=0, pady=10, padx=10)
    label3 = tk.Label(dashboard_frame, text="Description of page 3")
    label3.grid(row=3, column=0, pady=10, padx=10)

    button4 = tk.Button(dashboard_frame, text="Windows", command=move_to_page4)
    button4.grid(row=2, column=1, pady=10, padx=10)
    label4 = tk.Label(dashboard_frame, text="Description of page 4")
    label4.grid(row=3, column=1, pady=10, padx=10)
    
    signout_button = tk.Button(dashboard_frame, text="Sign Out", command=sign_out)
    signout_button.grid(row=4, column=0, columnspan=2, pady=10, padx=10)

root = tk.Tk()
root.title("Login")

if os.path.isfile("login.txt"):
    open_dashboard()
else:
    open_login()
root.mainloop()

