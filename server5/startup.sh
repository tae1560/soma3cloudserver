#!/bin/sh
#
# This script executed when system startup.

# IP binding
filepath=$0
folderpath=${filepath%/*}

$folderpath/../setip.sh eth1 10.12.17.218 10.12.17.200

# load modules
$folderpath/setmodules.sh

# virtual ip binding for load balancing
#$folderpath/../setip.sh bond0:0 10.12.17.200
$folderpath/../setip.sh eth1:0 10.12.17.200
