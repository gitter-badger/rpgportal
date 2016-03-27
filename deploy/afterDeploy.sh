#!/bin/bash
url=http://rpgportal-test-deathman92.boxfuse.io/health
echo Check deploy by url $url
r=`wget -q $url`
if [ $? != 0 ]
then
    deployed=false
else
    deployed=true
fi
if [ $deployed == false ]
then
    echo Not deployed. Exit
    travis_terminate 1
fi
prevNum=`expr $TRAVIS_BUILD_NUMBER - 1`
echo Deployed. Removing prevoius image num.$prevNum
boxfuse/boxfuse rm rpgportal:$prevNum -vault && echo Deployed!
echo Cannot remove previous image!