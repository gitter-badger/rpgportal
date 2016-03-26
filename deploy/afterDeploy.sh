#!/bin/bash
cd boxfuse
logfile = '*.logs'
logs = `cat $logfile`
deployed = false
echo Parse logs
for log in $logs ; do
    if [[ $log == *"Deployment completed successfully"* ]]
    then
        deployed = true
    fi
done
if [[ $deployed == false ]]
then
    exit 1
fi
prevNum = $TRAVIS_BUILD_NUMBER - 1
boxfuse rm rpgportal:$prevNum -vault
echo Deployed!