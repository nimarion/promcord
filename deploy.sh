#!/bin/bash
docker build -t biospheere/promcord .
docker login -u "$DOCKER_USERNAME" -p "c"
docker push biospheere/promcord