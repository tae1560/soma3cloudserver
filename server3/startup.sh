#!/bin/sh
#
# This script executed when system startup.

# IP binding
filepath=$0
folderpath=${filepath%/*}

# load modules
$folerpath/setmodules.sh

# start pvfs2 server
pvfs2-server -a 10.12.17.214 /etc/pvfs2-fs.conf
