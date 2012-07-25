#!/bin/sh

echo "nameserver 8.8.8.8" > /etc/resolv.conf
echo "nameserver 4.4.4.4" >> /etc/resolv.conf

/etc/init.d/network restart

