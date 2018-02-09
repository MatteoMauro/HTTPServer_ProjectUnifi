import socket
import sys

'''
 Check that a correct port number is passed as argument of the script
'''
def checkArguments():
    valid = True
    if len(sys.argv)!=2:
        print("ERROR: you must define a port number.\n\n     Follow this -> \"python3 reqProxy.py <port>\"")
        valid = False
    if valid:
        try:
            int(sys.argv[1])
        except ValueError:
            print("ERROR: port is not alphanumeric.")
            valid = False
    return valid

'''
 Read and return a string line from the passed socket, every line is delimeted by '\n'
'''
def readLine(socket):
    line = ""
    data = ""
    while data != '\n':
        data = socket.recv(1).decode("ascii")
        line += data
    return line


'''
 Read a reply from the socket and return it as a string representation
'''
def readReply(socket):
    parameters = ""
    line = ""
    length = "0"
    while line != '\n':
        line = readLine(socket)
        if "Content-Length" in line:
            length = line.split(' ', 2)[1]
        if(line != '\n'):
            parameters += (line)     
    body = socket.recv(int(length)).decode("ascii")
    return (parameters + '\n' + body + '\n')

'''
Send the following GET request:

    GET /index.html HTTP/1.1
    Host: www.google.it
    Connection: close
    
'''
def get11():
    if checkArguments():
        request = "GET /index.html HTTP/1.1\nHost: www.google.it\nConnection: close\n\n"
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect(("localhost", int(sys.argv[1])))
        s.sendall(request.encode())
        reply = readReply(s)
        print(reply)


'''
 Send two request for the same "index.html" of the site "www.google.it", the second one
 is retrieved from the proxy. 
 WARNING: HTTPProxyHandler must be present in the server Java.
 WARNING: use only Python3 to launch this script (print is use as a function)
'''
print("---------------- First request ----------------")    
get11()
print("---------------- Second request ----------------")
get11()

