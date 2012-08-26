rx1=`grep bond0 /proc/net/dev | awk '{print $1}' | sed 's/.*://'`
tx1=`grep bond0 /proc/net/dev | awk '{print $9}'`
sleep 3
rx2=`grep bond0 /proc/net/dev | awk '{print $1}' | sed 's/.*://'`
tx2=`grep bond0 /proc/net/dev | awk '{print $9}'`

rx3=$((rx2-rx1))
tx3=$((tx2-tx1))

echo $rx3
echo $tx3