#!/bin/sh
#
# This script executed when system startup.

# IP binding
filepath=$0
folderpath=${filepath%/*}

$folderpath/../setip.sh eth0 11.12.17.2

# heartbeat start
/etc/init.d/heartbeat start

# start load balancing
${folderpath}/../ha/load_balancing.sh
