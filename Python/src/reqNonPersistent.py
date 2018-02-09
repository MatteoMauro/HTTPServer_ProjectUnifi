import socket
import sys

'''
 Check that a correct port number is passed as argument of the script
'''
def checkArguments():
    valid = True
    if len(sys.argv)!=2:
        print("ERROR: you must define a port number.\n\n     Follow this -> \"python3 reqNonPersistent.py <port>\"")
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
        line = readLine(socket)
        if "Content-Length" in line:
            length = line.split(' ', 2)[1]
        if(line != '\n'):
            parameters += (line)
    body = socket.recv(int(length)).decode("ascii")
    return (parameters + '\n' + body + '\n')

'''
Send the following GET request:

    GET /index.html HTTP/1.0
    Host: www.miosito.it
    Connection: close
    
'''
def get10():
    if checkArguments():
        request = "GET /index.html HTTP/1.0\nHost: www.miosito.it\nConnection: close\n\n"
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect(("localhost", int(sys.argv[1])))
        s.sendall(request.encode())
        reply = readReply(s)
        print(reply)

'''
Send the following POST request:

    POST /index.html HTTP/1.0
    Host: www.miosito.it
    Connection: close
    Content-Length: 10
    
    SAY PEACE!
'''
def post10():
    if checkArguments():
        request = "POST /index.html HTTP/1.0\nHost: www.miosito.it\nConnection: close\nContent-Length: 10\n\nSAY PEACE!"
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect(("localhost", int(sys.argv[1])))
        s.sendall(request.encode())
        reply = readReply(s)
        print(reply)

'''
Send the following HEAD request:

    HEAD /index.html HTTP/1.0
    Host: www.miosito.it
    Connection: close
    
'''    
def head10():
    if checkArguments():
        request = "HEAD /index.html HTTP/1.0\nHost: www.miosito.it\nConnection: close\n\n"
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect(("localhost", int(sys.argv[1])))
        s.sendall(request.encode())
        reply = readReply(s)
        print(reply)

'''
Send the following GET request:

    GET /index.html HTTP/1.1
    Host: www.miosito.it
    Connection: keep-alive
    
'''   
def get11():
    if checkArguments():
        request = "GET /index.html HTTP/1.1\nHost: www.miosito.it\nConnection: close\n\n"
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect(("localhost", int(sys.argv[1])))
        s.sendall(request.encode())
        reply = readReply(s)
        print(reply)

'''
Send the following POST request:

    POST /index.html HTTP/1.1
    Host: www.miosito.it
    Connection: close
    Content-Length: 10
    
    SAY PEACE!
'''    
def post11():
    if checkArguments():
        request = "POST /index.html HTTP/1.1\nHost: www.miosito.it\nConnection: close\nContent-Length: 10\n\nSAY PEACE!"
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect(("localhost", int(sys.argv[1])))
        s.sendall(request.encode())
        reply = readReply(s)
        print(reply)

'''
Send the following HEAD request:

    HEAD /index.html HTTP/1.1
    Host: www.miosito.it
    Connection: close
    
'''    
def head11():
    if checkArguments():
        request = "HEAD /index.html HTTP/1.1\nHost: www.miosito.it\nConnection: close\n\n"
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect(("localhost", int(sys.argv[1])))
        s.sendall(request.encode())
        reply = readReply(s)
        print(reply)

'''
Send the following PUT request:

    PUT /index.html HTTP/1.1
    Host: www.miosito.it
    Connection: close
    Content-Length: 10
    
    SAY PEACE!
'''    
def put11():
    if checkArguments():
        request = "PUT /index.html HTTP/1.1\nHost: www.miosito.it\nConnection: close\nContent-Length: 10\n\nSAY PEACE!"
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect(("localhost", int(sys.argv[1])))
        s.sendall(request.encode())
        reply = readReply(s)
        print(reply)

'''
Send the following DELETE request:

    DELETE /index.html HTTP/1.1
    Host: www.miosito.it
    Connection: close
    
    
'''  
def delete11():
    if checkArguments():
        request = "DELETE /index.html HTTP/1.1\nHost: www.miosito.it\nConnection: close\n\n"
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect(("localhost", int(sys.argv[1])))
        s.sendall(request.encode())
        reply = readReply(s)
        print(reply)


'''
 Send 8 requests, one for each type described above. Every request is non persistent.
 WARNING: use only Python3 to launch this script (print is use as a function)
'''
print("---------------- GET 1.0 ----------------")    
get10()
print("---------------- POST 1.0 ----------------")
post10()
print("---------------- HEAD 1.0 ----------------")
head10()
print("---------------- GET 1.1 ----------------")
get11()
print("---------------- POST 1.1 ----------------")
post11()
print("---------------- HEAD 1.1 ----------------")
head11()
print("---------------- DELETE 1.1 ----------------")
delete11()
print("---------------- PUT 1.1 ----------------")
put11()

