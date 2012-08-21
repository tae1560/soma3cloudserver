#!/bin/sh
#
# This script executed when system startup.

# START HOST's startup shell
#filepath=$0
#filepath=/root/startup.sh
#folderpath=${filepath%/*}

# modified because of symbolic link
folderpath=/root/soma3cloudserver/
host_name=$(hostname)
$folderpath/$host_name/startup.sh

# git auto pull
CUR_PWD=`pwd`
cd ${folderpath}
git pull
echo `pwd`
cd ${CUR_PWD}
