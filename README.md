# Alfred 
**_UNDER CONSTRUCTION_**

## Project Overview
Project Alfred is a java smart home framework. It was originally designed to have a server application running on a Raspberry Pi, and have Android clients connected through a home network.

The purpose is to give a user control over electrical devices in the home using a mobile device. 

#### Pedigree
There were two motivating factors behind creating Alfred:

1. My father-in-law said he couldn't hear his doorbell everywhere in his house, so he wanted some way to get notifications on his phone when someone was at the door
2. My wife and I have young children who nap during the day, and we don't want people ringing the doorbell during the day and waking up our kids

These two objectives drove a system design that could send notifications over a home network. As framework development progressed it became clear that the basic framework could be extended to control other components of the house (garage doors, lights, ceiling fans, etc) because they were all fundamentally the same - an electrical device that can be represented by a state (on, off, open, closed, etc).

The focus of the project shifted into creating a more scaelable framework. Today the project can be used to control almost any state driven device in a house (assuming you have domain knowledge about connecting electrical devices)

##Software Overview
The Alfred project contains three components at the moment:

- Core API (this project)
- [Raspberry Pi Server Application] (https://github.com/kkanzelmeyer/alfred-server "Alfred Server")
- [Android Client Application] (https://github.com/kkanzelmeyer/alfred-client "Alfred Android Client")

This project contains the common API for project Alfred. Client and server applications can include this API and develop applications for interacting with devices around their house.

The common API includes:

- Devices - the foundation for creating a new device in a server application
- Device Manager - a singleton class to manage the state of all connected devices
- Messaging - a Google Protocol Buffer messaging class to easily handle creating, sending, and receiving messages

### API Usage






