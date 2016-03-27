#!/bin/bash
if [ $3 == "-v" ]
    then vault="-vault"
    else vault=""
fi
boxfuse/boxfuse rm $1:$2 $vault