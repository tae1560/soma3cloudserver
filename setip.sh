echo "nameserver 8.8.8.8">/etc/resolv.conf
/ect/init.d/network restart
ifconfig eth0 down
ifconfig eth0 10.12.17.216 netmask 255.255.255.0 broadcast 255.255.255.255
ifconfig eth0 up
route add default gw 10.12.17.1 dev eth0
ifconfig -a
