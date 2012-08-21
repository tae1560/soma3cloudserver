ipvsadm -C
ipvsadm -A -t 10.12.17.200:80 -s rr
ipvsadm -a -t 10.12.17.200:80 -r 10.12.17.214:80 -g
ipvsadm -a -t 10.12.17.200:80 -r 10.12.17.216:80 -g
ipvsadm -a -t 10.12.17.200:80 -r 10.12.17.218:80 -g
