# EmoText
## Table of Contents
* [Link](#Link)
* [Overview](#Overview)
* [Technologies](#Technologies)
* [App Details](#App-Details)
* [Model Details](#Model-Details)

## Link
* [Android App](https://play.google.com/store/apps/details?id=com.emotext.chatapp)

## Overview
This app is a Machine Learning based messaging app which uses Natural Language processing to detect the emotions of a user through the last few texts he/she sent to others. Using that information, friends of the user can have an idea of how to approach the person.

## Technologies
* This app's frontend is written in Android Studio, and uses 2 main languages: Java and XML. XML is used for the entire design and layout of the app, while Java is responsible for the responsiveness of the GUI generated by the XML files.

* The backend uses JavaScript, Node.js, and Socket.io for the backend, along with PostgreSQL databases. All of these are used with Docker containers. All the API's for the app have been made by us, and are in another GitHub repository.

* Technologies like Flask and Python, with libraries like Pandas and Numpy, are used for the Machine Learning part of the app.

* We are also making an iOS app which uses the same backend, but with technologies such as Swift, SwiftUI, and XCode.

## App Details
This app consists of the following major activities:

* [Start](#Start)
* [Sign Up/Sign In](#Sign-Up/Sign-In)
* [Main(People, Camera, Chats)](#Main)
* [Chat](#Chat)
* [Add Friends/Requests](#Add-Friends/Requests)
* [User Info](#User-Info)
* [Settings](#Settings)

### Start
This is the first activity which a first-time user(or a returning one) will see.

<img src="https://github.com/devr0306/EmoText/blob/master/App-Pics/Start.jpg" height="400" width="225">

The user can either click on the sign up button or the sign in one.

### Sign Up/Sign In
The sign up and the sign in activities are 2 different ones, which open based on the button selected in the start activity.

<img src="https://github.com/devr0306/EmoText/blob/master/App-Pics/Sign-In.jpg" height="400" width="225">

The sign in activity consists of 2 simple fields(username/email and password). After entering those fields, the user can advance to the main activity.

<img src="https://github.com/devr0306/EmoText/blob/master/App-Pics/Sign-Up-1.jpg" height="400" width="225">     <img src="https://github.com/devr0306/EmoText/blob/master/App-Pics/Sign-Up-2.jpg" height="400" width="225">      <img src="https://github.com/devr0306/EmoText/blob/master/App-Pics/Sign-Up-3.jpg" height="400" width="225">

The sign up activity consists of more fields. It starts off with the email, followed by an email code the user will receive. After that, the final 4 fields will be username, email, password, and confirm password, along with a place to pick a profile picture. After entering everything, the user can advance to the main activity.

### Main
This will be a swipable main activity consisting of 3 tabs: People, Camera, and Chats. This activity will have a bar at the top consisting of 3 items: the profile picture of the user, which can direct him/her to the User Info Activity, a search bar, and a button which will direct the user to the Add Friends Activity.

<img src="https://github.com/devr0306/EmoText/blob/master/App-Pics/People.jpg" height="400" width="225">

The People tab will consist of a list of added friends a user has. Upon clicking on any item in the list, the user will be taken to the Chat Activity.

<img src="https://github.com/devr0306/EmoText/blob/master/App-Pics/Camera.jpg" height="400" width="225">

The Camera tab will consist of exactly what it says, a camera along with a couple of buttons to make it functional.

<img src="https://github.com/devr0306/EmoText/blob/master/App-Pics/Chats.jpg" height="400" width="225">

The Chats tab will contain a list of all the people the user has started a chat with. Upon clicking on any item in the list, the user will be taken to the Chat Activity.

### Chat
This will be the activity where message sending and receiving will take place. It contains many things and everything functions just like you would expect it to.

<img src="https://github.com/devr0306/EmoText/blob/master/App-Pics/Chat.jpg" height="400" width="225">

It also has the feature to swipe in and out of the Activity.

### Add Friends/Requests
The Add Friends Activity will be where the user can search for and add people. Within the Activity will be a button/bar to go to the Requests Activity, which will contain the requests other people have sent to the user.

<img src="https://github.com/devr0306/EmoText/blob/master/App-Pics/Add-People.jpg" height="400" width="225">      <img src="https://github.com/devr0306/EmoText/blob/master/App-Pics/Requests.jpg" height="400" width="225">

### User Info
This will contain basic info on the user such as his/her profile picture, some stats, and buttons that will redirect the user to adding people or starting chats.

<img src="https://github.com/devr0306/EmoText/blob/master/App-Pics/User-Info.jpg" height="400" width="225">

### Settings
This will be the Activity which will handle the settings of the app.

<img src="https://github.com/devr0306/EmoText/blob/master/App-Pics/Settings-1.jpg" height="400" width="225">      <img src="https://github.com/devr0306/EmoText/blob/master/App-Pics/Settings-2.jpg" height="400" width="225">

As of right now, the Settings Activity can only be used to sign out of the app, but it will include more things later on.

## Model Details
The machine learning model uses nltk, sklearn, and TensorFlow to predict sentiment(positive/negative) from texts. I trained the model in the following steps:
* It reads around a million tweets from a downloaded file and cleans the data up using nltk. 
* A TensorFlow model is created and trained using the cleaned data.
* The model is then tested using a validation data set, which is a part of the train data set that was taken out before training.
* The model is then saved to a file.

The saved model is then used to predict the emotions of new sets of data(texts from the app) in the following steps:
* The data is imported from API's in a JSON format and is cleaned up in the same way as the process in training.
* The model is downloaded and the data is given to the model as input, for which it outputs a number(0 or 1).
* A zero means "negative sentiment" while a 1 means "positive sentiment".
* The output is then sent to the app via API's in Flask and the app displays the model's prediction in the Chat Activity.
