#!/bin/bash
chmod +x deploy/removeImage.sh
echo Check deployed...
deployed=true
./deploy/removeImage.sh rpgportal $TRAVIS_BUILD_NUMBER && deployed=false || deployed=true
if [[ $deployed == true ]]
then
    echo Not deployed! Removing current image...
    ./deploy/removeImage.sh rpgportal $TRAVIS_BUILD_NUMBER -v && echo Successfully removed rpgportal:$TRAVIS_BUILD_NUMBER.
    echo -e "\nDone. Your build exited with 1." && exit 1;
fi
prevNum=`expr $TRAVIS_BUILD_NUMBER - 1`
echo Deployed. Removing prevoius image rpgportal:$prevNum...
./deploy/removeImage.sh rpgportal $prevNum -v && echo Successfully removed rpgportal:$prevNum. || echo Cannot remove previous image! Do it manually.
exit 0;