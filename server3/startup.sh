#!/bin/sh
#
# This script executed when system startup.

# IP binding
filepath=$0
folderpath=${filepath%/*}

# load modules
$folderpath/setmodules.sh

# virtual ip binding for load balancing
#$folderpath/../setip.sh bond0:0 10.12.17.200

# start pvfs2 server
pvfs2-server -a 10.12.17.214 /etc/pvfs2-fs.conf
