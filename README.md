# EV-SC
Electric vehicle charging station location app

![Logo](https://user-images.githubusercontent.com/73894107/212471151-60c35a79-c509-49da-80b0-ff368ce6de34.png)

### Contributers: 

* [Shaked's github](https://github.com/20shaked20)
* [Lior's github](https://github.com/liornagar799)
* [Yuval's github](https://github.com/YuvalBubnovsky)

## Introduction
In order to see our work, we've dedicated a simply wiki page for it - </br>
[wiki app](https://github.com/20shaked20/EV-SC/wiki) where you can see more about our project. </br>
This project was created in order to to provide accessibility for electric vechicle charging stations. </br>
In our project we used already known locations and created our own database for stations. </br>
our application is divided supports two types of users: </br>
### Admin
* Can Add new stations to the database. </br>
* Can modify stations. </br>
### User
* Can search for exisisting stations. </br>
* Can add favorite stations. </br>


## Approach
We used the Agile methodolgy, in particular Scrum (By using [Trello](https://www.googleadservices.com/pagead/aclk?sa=L&ai=DChcSEwj9nNX_hsf8AhVKn9UKHVlrD00YABAAGgJ3cw&ohost=www.google.com&cid=CAESbOD2oQoQKLGiVC_4y3WTu3qcmqBP_Lk5_aNd4mEmrmSd7_Ofutytrnj-teGZZCnMNpISLhBT8qGa46Pmak96B-gtiUDrcFk3Tbk82E643haEgk0ETpRUz-RXRGlW6jOud4zB35Vil3QJx3sWhA&sig=AOD64_0a9TKqqI8Pci6TUr8_LRExrLLbbw&q&adurl&ved=2ahUKEwje4M__hsf8AhXnQaQEHddNDR4Q0Qx6BAgKEAE)). </br>
Our appoarch for this project was at first to model the entire classes and interfaces. </br>
We created a [NODE JS](https://github.com/20shaked20/EV-SC-API) express server to communicate with our application while sending post get requests to it.</br>
Our server is connected to a firebase database which we created using accuarte data we found. </br>
Moving on we started working on our project bits by bits. </br>
1. implement all the objects needed for the project.</br>
2. start with simple XML design. </br>
3. connect the Activity and start implementing a working application. </br>
When we we're on the verge of finishing the project, we remodeled it, giving it a brand new UI. </br>

The project is created in a view of MVC - </br>
```Modeling```  - client side in our application which connects to the NodeJS server.</br>
```View```  - The UI side of the project - XML files.</br>
```Controller```  - classes dedicated to update each Activity (Screen) in the application. </br>


## Search Algorithm
In order to get a good and fast search, we used Fuzzy Wuzzy algorithm. </br>
Fuzzy algorithm is a method that offers an improved ability to identify two elements of text, strings, or entries that are approximately similar but are not precisely the same. </br>
In other words, a fuzzy method may be applied when an exact match is not found for phrases or sentences on a database.</br>

## Reading Material
* [Scrum](https://en.wikipedia.org/wiki/Scrum_(software_development))
