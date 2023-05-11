#!/bin/sh

#source config/setvars.sh
source ~/.bash_profile

#bash setup.sh

#mvn clean

# short command
mvn clean package deploy -DskipTests

#java -jar $1/target/$1-0.0.1-SNAPSHOT.jar $2 $3 $4 $5 $6
