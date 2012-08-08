#!/bin/sh
IP="11.12.17.2"
GATEWAY="11.12.17.2"

if [ $# == 2 ] 
then
	IP=#1
	GATEWAY=#2
fi

DEVICE_NAME=`ifconfig -a | grep eth1`

if [ DEVICE_NAME == 0 ]
then
	DEVICE_NAME="p128p1"
else
	DEVICE_NAME="eth1"
fi

ifconfig $DEVICE_NAME down

ifconfig $DEVICE_NAME $IP netmask 255.255.255.0 broadcast 255.255.255.255

ifconfig $DEVICE_NAME up
