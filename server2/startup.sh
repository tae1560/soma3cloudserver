#!/bin/sh
#
# This script executed when system startup.

# IP binding
filepath=$0
folderpath=${filepath%/*}
$folderpath/setip.sh

$folderpath/setip_eth1.sh
