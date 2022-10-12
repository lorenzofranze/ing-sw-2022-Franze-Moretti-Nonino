# Software Engineering Project 2022

*This project was made between March and June 2022, at Politecnico University of Milan.*  
<p align="center">
  <img height="300" src="https://github.com/LaraNonino/ing-sw-2022-Franze-Moretti-Nonino/blob/master/src/main/Resources/images/imageStart/logo.png">
</p>

 
The project consits in the implementation of [Eriantys](https://www.craniocreations.it/prodotto/eriantys/) board game through a distributed system: the __server__ and multiple __clients__ that can connect to it via the network. Clients
allow you to interact with the game via command line (__CLI__) or graphical interface (__GUI__).


This project is written in __Java/JavaFx__ and is built using __Maven__.
All UML diagarms, protocols and documentations are in the *deliveries* folder.

__Students__:

- __Lorenzo Franzè__ ([@LorenzoFranze](https://github.com/lorenzofranze))
- __Valentina Moretti__ ([@ValentinaMoretti](https://github.com/valentina-moretti))
- __Lara Nonino__ ([@LaraNonino](https://github.com/LaraNonino))

## Implemented features

- Complete ruleset (Complex game mode)
- CLI
- GUI
- __Character cards__: implemeted all the 12 cards.
- __Multiple matches__: the server can handle several matches at the same time.
- __persistence__: the state of game is saved on hard disk in order to allow the players to reconnect and continue the game if the server should go down. Recconnection and savings are based on nicknames used.

## Jar execution

### Server
To start the Server run:

```
java -jar server.jar [port-number]
```
If port isn't specified, the default port is 35002.  
*note*: At firt lauch, *savings* folder is created. If you want to restart a match using same nicknames used previously delete the folder.

### Client

- Two clients available: Mac and Windows based on your OS (The difference is only in a configuration file to run javaFx).

```
java -jar client-(OS).jar [cli/gui  server-ip-address  server-port-number]
```
If arguments aren't specified, default is: gui localhost 35002 otherwise you have to specify all the parameters.







