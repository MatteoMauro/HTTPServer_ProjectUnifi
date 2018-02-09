import socket
import sys 

'''
 Check that a correct port number is passed as argument of the script
'''
def checkArguments():
    valid = True
    if len(sys.argv)!=2:
        print("ERROR: you must define a port number.\n\n     Follow this -> \"python3 reqPersistent.py <port>\"")
        valid = False
    if valid:
        try:
            int(sys.argv[1])
        except ValueError:
            print("ERROR: port is not alphanumeric.")
            valid = False
    return valid

'''
 Read and return a string line from the passed socket, every line is delimited by '\n'
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
        # read every line of the reply, until end of header section
        line = readLine(socket)
        if "Content-Length" in line:
            # retrieve Content-Length value (second argument of split)
            length = line.split(' ', 2)[1]
        if(line != '\n'):
            parameters += (line)
    # at last retrieve exactly length bytes for the data body
    body = socket.recv(int(length)).decode("ascii")
    return (parameters + '\n' + body + '\n')

'''
 Simulate multiple requests, sent in a persistent connection. The first three
 requests are GET with Connection: keep-alive; the last one is a POST that
 asks for closing the connection.
WARNING: use only Python3 to launch this script (print is use as a function)
'''    
def get11():
    if checkArguments():
        request = "GET /index.html HTTP/1.1\nHost: www.miosito.it\nConnection: keep-alive\n\n"
        request2 = "GET /index.html HTTP/1.1\nHost: www.miosito.it\nConnection: keep-alive\n\n"
        request3 = "GET /index.html HTTP/1.1\nHost: www.miosito.it\nConnection: keep-alive\n\n"
        request4 = "POST /index.html HTTP/1.1\nHost: www.miosito.it\nConnection: close\nContent-Length: 10\n\nSay Peace!"
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect(("localhost", int(sys.argv[1])))
        # send all the requests in pipelining
        s.sendall(request.encode())
        s.sendall(request2.encode())
        s.sendall(request3.encode())
        s.sendall(request4.encode())
        # read and print exactly 4 replies
        reply = readReply(s)
        print(reply)
        reply = readReply(s)
        print(reply)
        reply = readReply(s)
        print(reply)
        reply = readReply(s)
        print(reply)


print("---------------- GET 1.1 ----------------")
get11()
