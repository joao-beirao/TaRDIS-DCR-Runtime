# Step F - Dcr Choreographies

## How to compile

To compile your code, run the following commands:

- ``mvn clean package``
- ``docker build -t dcr-babel .``

## How to run

### Setup

You need to create a docker network for the tutorial:

``docker network create tardis-babel-backend-net``

To remove the network:

``docker network rm tardis-babel-backend-net``

### Args

The protocol accepts the following arguments:

- ``interface``: the network interface to use (e.g., eth0)
- ``target-name``: the host name

### Run

To run the protocol, for the currently hardcoded example, you need to run the following commands,
one for each end-point projection:

``docker run --network tardis-babel-backend-net --rm -h Prosumer_p1 --name Prosumer_p1 -it
dcr-babel interface=eth0 target-name=Prosumer_p1``

``docker run --network tardis-babel-backend-net --rm -h Prosumer_p2 --name Prosumer_p2 -it dcr-babel
interface=eth0 target-name=Prosumer_p2``

``docker run --network tardis-babel-backend-net --rm -h Prosumer_p3 --name Prosumer_p3 -it
dcr-babel interface=eth0 target-name=Prosumer_p2``