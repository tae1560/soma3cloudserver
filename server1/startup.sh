#!/bin/sh
#
# This script executed when system startup.

# IP binding
filepath=$0
folderpath=${filepath%/*}

$folderpath/../setip.sh eth1 11.12.17.1

# heartbeat start
/etc/init.d/heartbeat start

# start load balancing
${folderpath}/../ha/load_balancing.sh

# start logger
java CSDaemon ipvsadm >> /tmp/cs.log