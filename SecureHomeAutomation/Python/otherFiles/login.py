import sys
from PyQt5.QtWidgets import QApplication, QMainWindow, QPushButton, QLabel, QLineEdit, QVBoxLayout, QHBoxLayout, QWidget
from PyQt5.QtGui import QFont
from PyQt5.QtCore import Qt
import pyrebase
import os

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
id=""

class MainWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        self.initUI()

    def initUI(self):
        # Create a widget and set its layout to a vertical box layout
        widget = QWidget(self)
        self.setCentralWidget(widget)
        vbox = QVBoxLayout()
        widget.setLayout(vbox)

        # Create a label and line edit for the username
        usernameLabel = QLabel("Username:", self)
        usernameLabel.setFont(QFont("Arial", 14))
        self.usernameLineEdit = QLineEdit(self)
        self.usernameLineEdit.setFont(QFont("Arial", 14))

        # Create a horizontal box layout for the username
        hbox1 = QHBoxLayout()
        hbox1.addWidget(usernameLabel)
        hbox1.addWidget(self.usernameLineEdit)

        # Create a label and line edit for the password
        passwordLabel = QLabel("Password:", self)
        passwordLabel.setFont(QFont("Arial", 14))
        self.passwordLineEdit = QLineEdit(self)
        self.passwordLineEdit.setFont(QFont("Arial", 14))
        self.passwordLineEdit.setEchoMode(QLineEdit.Password)

        # Create a horizontal box layout for the password
        hbox2 = QHBoxLayout()
        hbox2.addWidget(passwordLabel)
        hbox2.addWidget(self.passwordLineEdit)

        # Create a login button
        loginButton = QPushButton("Login", self)
        loginButton.setFont(QFont("Arial", 14))

        # Add the username, password, and login button to the vertical box layout
        vbox.addLayout(hbox1)
        vbox.addLayout(hbox2)
        vbox.addWidget(loginButton, alignment=Qt.AlignCenter)

        # Connect the login button to an event handler
        loginButton.clicked.connect(self.loginClicked)

        self.show()

    def loginClicked(self):
        username = self.usernameLineEdit.text()
        password = self.passwordLineEdit.text()
        self.login(username, password)
    def userID(self,id):
        if id is None:
            print ("error")
        else:
            print ("User ID is :", id)
    def login(self, username, password):
        print("Username:", username)
        print("Password:", password)
        id=""
        print("Welcome to Secure Home Automation")
        #print("Log in...")
        email=username
        pwd=password
    
        #pwd=input("Enter password:") 
#    	print('Password : ', pwd)
    

        login = auth.sign_in_with_email_and_password(email, pwd)
        print("Successfully logged in!")
        print(auth.current_user['localId'])
        id= auth.current_user['localId']
        
        self.userID(id)
if __name__ == '__main__':
    app = QApplication(sys.argv)
    mainWindow = MainWindow()
    sys.exit(app.exec_())


