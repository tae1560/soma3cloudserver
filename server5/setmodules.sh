insmod /lib/modules/`uname -r`/kernel/fs/pvfs2/pvfs2.ko
echo "###load modules###"
lsmod | grep pvfs
