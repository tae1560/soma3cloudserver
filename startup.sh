#!/bin/sh
#
# This script executed when system startup.

# IP binding
filepath=$0
folderpath=${filepath%/*}
$folderpath/setip.sh

#pvfs module loading
insmod /lib/modules/`uname -r`/kernel/fs/pvfs2/pvfs2.ko
