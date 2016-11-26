import socket
import sys
import time
from thread import *
#Function for handling connections. This will be used to create threads
def send_client_thread(conn):
    #Sending message to connected client
     #send only takes string
    try: 
    	print("Se conecto sender")
    	#infinite loop so that function do not terminate and thread do not end.
        i = 0
        while(True):
            conn.send('\ndata'+ str(i) +'\n' )
            i += 1
            time.sleep(0.5)
    finally:
    	#came out of loop
        print("Termino")
        conn.close
def reciber_client_thread(conn):
    #Sending message to connected client
    #infinite loop so that function do not terminate and thread do not end.
    try: 
    	print("Se conecto reciber")

    	while True:
        	data = conn.recv(4096)
        	print data.strip()
        	time.sleep(0.5)
    finally:
		#came out of loop
    	conn.close()
# Create a TCP/IP socket
sock_reciber = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock_sender = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Bind the socket to the port
server_address_reciber = ('192.168.1.8', 5589)
server_address_sender = ('192.168.1.8', 5590)

print >>sys.stderr, 'starting up on %s port %s' % server_address_reciber
sock_reciber.bind(server_address_reciber)
# Listen for incoming connections
sock_reciber.listen(10)

print >>sys.stderr, 'starting up on %s port %s' % server_address_sender
sock_sender.bind(server_address_sender)
# Listen for incoming connections
sock_sender.listen(10)

while True:
	# Wait for a connection
	print >>sys.stderr, 'waiting for a connection'
	connection_sender, client_address_sender = sock_sender.accept()
	start_new_thread(send_client_thread ,(connection_sender,))
	connection_reciber, client_address_reciber = sock_reciber.accept()
	start_new_thread(reciber_client_thread ,(connection_reciber,))
sock_sender.close()



