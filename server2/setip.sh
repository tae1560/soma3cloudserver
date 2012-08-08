#!/bin/sh
IP="10.12.17.212"
GATEWAY="10.12.17.1"

if [ $# == 2 ] 
then
	IP=#1
	GATEWAY=#2
fi

DEVICE_NAME=`ifconfig -a | grep eth0`

if [ DEVICE_NAME == 0 ]
then
	DEVICE_NAME="p128p1"
else
	DEVICE_NAME="eth0"
fi

ifconfig $DEVICE_NAME down

ifconfig $DEVICE_NAME $IP netmask 255.255.255.0 broadcast 255.255.255.255

ifconfig $DEVICE_NAME up

route add default gw $GATEWAY dev $DEVICE_NAME

ifconfig -a