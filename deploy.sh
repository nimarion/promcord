#!/bin/bash
docker build -t biospheere/promcord .
docker login -u "$DOCKER_USERNAME" -p "$DOCKER_PASSWORD"
docker push biospheere/promcord