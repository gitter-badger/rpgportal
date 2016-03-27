#!/bin/bash
cd boxfuse
r=`wget -q http://rpgportal-test-deathman92.boxfuse.io/health`
if [ $? != 0 ]
then
    deployed=false
else
    deployed=true
fi
if [ $deployed == false ]
then
    exit 1
fi
prevNum=`expr $TRAVIS_BUILD_NUMBER - 1`
boxfuse rm rpgportal:$prevNum -vault
echo Deployed!