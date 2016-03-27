#!/bin/bash
echo Check deployed
deployed=true
./removeImage.sh rpgportal $TRAVIS_BUILD_NUMBER &>/dev/null && deployed=false
if [ $deployed == false ]
then
    echo Not deployed! Removing current image
    ./removeImage.sh rpgportal $TRAVIS_BUILD_NUMBER -v &>/dev/null && echo Successfully removed rpgportal:$TRAVIS_BUILD_NUMBER
    echo -e "\nDone. Your build exited with 1." && exit 1;
fi
prevNum=`expr $TRAVIS_BUILD_NUMBER - 1`
echo Deployed. Removing prevoius image rpgportal:$prevNum
./removeImage.sh rpgportal $prevNum -v &>/dev/null && echo Deployed! || echo Cannot remove previous image!
exit 0;