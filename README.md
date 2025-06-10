# **! [Under Construction]**

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
- ``name``: the host name
- ``role``: the role enacted by this endpoint
- ``<param-name>``: value for role parameter (if any)

example:

```bash 
docker run --network tardis-babel-backend-net --rm -h P_1_1 --name P_1_1 -it dcr-babel interface=eth0 role=P id=1 cid=1
```

### Run (TODO)
