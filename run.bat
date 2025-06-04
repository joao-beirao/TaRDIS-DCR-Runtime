
@echo off
start cmd /k "docker run --network tardis-babel-backend-net --rm -h P_2_1 --name P_2_1 -it dcr-babel interface=eth0 role=P id=2 cid=1"
start cmd /k "docker run --network tardis-babel-backend-net --rm -h P_3_1 --name P_3_1 -it dcr-babel interface=eth0 role=P id=3 cid=1"
start cmd /k "docker run --network tardis-babel-backend-net --rm -h P_4_1 --name P_4_1 -it dcr-babel interface=eth0 role=P id=4 cid=1"