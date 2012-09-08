#!/bin/sh
#
# This script executed when system startup.

# IP binding
filepath=$0
folderpath=${filepath%/*}
#$folderpath/../setip.sh

# load modules
$folderpath/setmodules.sh

# virtual ip binding for load balancing
$folderpath/../setip.sh bond0:0 10.12.17.200

# load module
insmod /lib/modules/`uname -r`/kernel/fs/pvfs2/pvfs2.ko

# start pvfs2 server
/usr/local/sbin/pvfs2-server -a 10.12.17.218 /etc/pvfs2-fs.conf

# start pvfs2 client
/usr/local/sbin/pvfs2-client -p /usr/local/sbin/pvfs2-client-core

# mount 
/usr/local/bin/pvfs2fuse /mnt/storage-server1 -o fs_spec="tcp://10.12.17.214:3334/pvfs2-fs" -o allow_other
/usr/local/bin/pvfs2fuse /mnt/storage-server2 -o fs_spec="tcp://10.12.17.216:3334/pvfs2-fs" -o allow_other
/usr/local/bin/pvfs2fuse /mnt/storage-server3 -o fs_spec="tcp://10.12.17.218:3334/pvfs2-fs" -o allow_other

#start logger
cd $folderpath/../daemon
java CSDaemon traffic >> /tmp/cs.log
