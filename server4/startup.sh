#!/bin/sh
#
# This script executed when system startup.

# IP binding
filepath=$0
folderpath=${filepath%/*}
#$folderpath/setip.sh

# load modules
$folderpath/setmodules.sh

# virtual ip binding for load balancing
$folderpath/../setip.sh bond0:0 10.12.17.200
