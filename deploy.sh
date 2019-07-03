#!/bin/bash
docker build -t biospheere/wherestheparty .
docker login -u "$DOCKER_USERNAME" -p "$DOCKER_PASSWORD"
docker push biospheere/wherestheparty