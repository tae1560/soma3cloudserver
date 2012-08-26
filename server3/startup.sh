#!/bin/sh
#
# This script executed when system startup.

# IP binding
filepath=$0
folderpath=${filepath%/*}

# load modules
$folderpath/setmodules.sh

# virtual ip binding for load balancing
$folderpath/../setip.sh bond0:0 10.12.17.200

# start pvfs2 server
pvfs2-server -a 10.12.17.214 /etc/pvfs2-fs.conf

# start pvfs2 client
pvfs2-client -p ./pvfs2-client-core

# mount 
pvfs2fuse /mnt/storage-server1 -o fs_spec="tcp://10.12.17.214:3334/pvfs2-fs" -o allow_other
pvfs2fuse /mnt/storage-server2 -o fs_spec="tcp://10.12.17.216:3334/pvfs2-fs" -o allow_other
pvfs2fuse /mnt/storage-server3 -o fs_spec="tcp://10.12.17.218:3334/pvfs2-fs" -o allow_other

#start logger
java CSDaemon pvfs2 traffic >> /tmp/cs.log