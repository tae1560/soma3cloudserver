#!/bin/sh
#
# This script executed when system startup.

# IP binding
filepath=$0
folderpath=${filepath%/*}
$folderpath/setip.sh

# load modules
$folerpath/setmodules.sh
