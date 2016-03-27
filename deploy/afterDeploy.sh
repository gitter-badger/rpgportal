#!/bin/bash
echo Check deployed
deployed=true
boxfuse/boxfuse rm rpgportal:$TRAVIS_BUILD_NUMBER && deployed=false
if [ $deployed == false ]
then
    echo Not deployed! Removing current image
    boxfuse/boxfuse rm rpgportal:$TRAVIS_BUILD_NUMBER -vault && echo Removed. Exit
    exit 1;
fi
prevNum=`expr $TRAVIS_BUILD_NUMBER - 1`
echo Deployed. Removing prevoius image num.$prevNum
boxfuse/boxfuse rm rpgportal:$prevNum -vault && echo Deployed! && exit 0;
echo Cannot remove previous image!
exit 0;