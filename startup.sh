#!/bin/sh
#
# This script executed when system startup.

# START HOST's startup shell
filepath=$0
folderpath=${filepath%/*}
host_name=$(hostname)
$folderpath/$host_name/startup.sh

#pvfs module loading
insmod /lib/modules/`uname -r`/kernel/fs/pvfs2/pvfs2.ko
