#!/bin/sh
if [ $# -eq 3 ]
then
	DEVICE_NAME=$1
	IP=$2
	GATEWAY=$3
elif [ $# -eq 2 ] 
then
	DEVICE_NAME=$1
	IP=$2
else
	echo "Usage : $0 <DEVICE_NAME> <IP> <GATEWAY>"
	echo "or"
	echo "Usage : $0 <DEVICE_NAME> <IP>"
	exit 0
fi

ifconfig $DEVICE_NAME down

ifconfig $DEVICE_NAME $IP netmask 255.255.255.0 broadcast 255.255.255.255

ifconfig $DEVICE_NAME up

if [ $# -eq 3 ]
then
	route add default gw $GATEWAY dev $DEVICE_NAME
fi
