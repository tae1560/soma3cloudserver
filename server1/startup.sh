#!/bin/sh
#
# This script executed when system startup.

# IP binding
filepath=$0
folderpath=${filepath%/*}

$folderpath/../setip.sh eth0 10.12.17.210 10.12.17.1

$folderpath/../setip.sh eth1 11.12.17.1
