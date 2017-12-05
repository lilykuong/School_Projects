// USC ID # 8277174492
// Lily Kuong
// EE450 Project Phase 2 Summer 2017
// Follower.cpp - UDP and TCP connnections between Server


#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <netdb.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <sys/wait.h>

#include <fstream>
#include <sstream>
#include <vector>

#define portNum 3792 // Server TCP port number


using namespace std;


int main (){
    
    
    // ----------------------------- UDP ---------------------------------------------
    // Follower receive tweets via UDP from Server
    // Sends feedback to server via TCP ; port number for TCP dynamically assigned
    // Sends one packet for the server for each line in the feedback line
    
    // Print out
    // <Follower#> has UDP port __ and IP address __
    // <Follower#> has received <Tweet#>
    
    
    int follower = 0;
    int udpPortNum;
    int udpsock;
    struct sockaddr_in udp_addr, serv_addr;
    int udpbuffersize = 1024;
    char udpbuffer[udpbuffersize];
    
    
    //which follower contains which static portNum
    while (follower < 5){
        if (follower == 0)
            udpPortNum = 21892;
        else if (follower == 1)
            udpPortNum = 21992;
        else if (follower == 2)
            udpPortNum = 22092;
        else if (follower == 3)
            udpPortNum = 22192;
        else if (follower == 4)
            udpPortNum = 22292;
        
        
        //create a socket
        if ((udpsock = socket (AF_INET, SOCK_DGRAM, 0)) < 0)
            perror("UDP Sock error");
        
        
        //initialize variables in udp_addr
        udp_addr.sin_family = AF_INET;
        udp_addr.sin_addr.s_addr = htonl (INADDR_ANY);
        udp_addr.sin_port = htons(udpPortNum);
        memset(udp_addr.sin_zero, '\0', sizeof udp_addr.sin_zero);
        
        //bind the socket
        //bind socket to the static UDP number
        if (bind (udpsock, (struct sockaddr *) &udp_addr, sizeof (udp_addr)) < 0 )
            perror ("Error UDP binding");
        
        socklen_t addr_len = sizeof (serv_addr);
        
        
        //clear message
        memset (udpbuffer, 0, sizeof(udpbuffer));
        
        
        struct sockaddr_storage serv_stor;
        socklen_t serv_stor_len = sizeof serv_stor;
        
        
        //receive initial message from server
        if (recvfrom (udpsock, udpbuffer, udpbuffersize, 0, (struct sockaddr *) &serv_stor, &serv_stor_len) < 0)
            perror ("Error receiving");
        
        
        
        //get UDP port and IP address //Reused code #3 lines 98-104
        struct sockaddr_in addr;
        socklen_t len = sizeof (addr);
        if (getsockname(udpsock, (struct sockaddr *) &addr, &len) == -1)
            perror("Could not get socket name");
        else
            cout << "Follower" << follower+1 << " has UDP port " << ntohs(addr.sin_port) << " and IP address " << inet_ntoa(addr.sin_addr) << endl;
        
        
        //clear message
        memset (udpbuffer, 0, sizeof(udpbuffer));
        
        
        //send blank message to server
        int sent = sendto(udpsock, udpbuffer, udpbuffersize, 0, (struct sockaddr *) &udp_addr, sizeof(udp_addr));
        if (sent != udpbuffersize)
            perror ("Error Sending");

        
        //clear message
        memset (udpbuffer, 0, sizeof(udpbuffer));

        
        //"TweetC" is the last packet sent from the server
        while (strcmp(udpbuffer, "TweetC") != 0){
            
            memset (udpbuffer, 0, sizeof(udpbuffer));
            
            recvfrom (udpsock, udpbuffer, udpbuffersize, 0, (struct sockaddr *) &serv_addr, &addr_len);
            
            if ((strcmp(udpbuffer, "TweetA") == 0) || (strcmp(udpbuffer, "TweetB") == 0) || (strcmp(udpbuffer, "TweetC") == 0))
                cout << "Follower" << follower+1 << " has received " << udpbuffer << endl;
            
        }
        
        follower ++;
        
        //close udpsocket
        close (udpsock);
        
    }
    
    
    
    //---------------------------------------- TCP -------------------------------------
    // clientSock is the socket
    // server_address contains the static port number of the server
    
    //Prints out
    // <Follower# has TCP port __ and IP address __
    // Completed sending feedback for <Follower#>
    // End of phase 2 for <Follower#>
    
    //similar TCP code as Server.cpp
    
    for (int a = 0; a < 5; a++){
        int buffersize = 1024; //1024 is an arbitary number
        char buffer[buffersize];
        int clientSock;
        struct sockaddr_in servAddr;
        
        // SOCKET
        clientSock = socket(AF_INET, SOCK_STREAM, 0);
        
        
        if (clientSock < 0)
            perror ("Socket Error");
        
        
        // initiatlize the address
        memset(&servAddr, 0, sizeof(servAddr));
        
        servAddr.sin_family = AF_INET; //IPv4
        servAddr.sin_port = htons(portNum); // changes the presentation of port number to present to network
        
        
        // connect
        if (connect(clientSock,(struct sockaddr *)&servAddr, sizeof(servAddr)) < 0)
            perror( "Trouble connecting");
        
        
        // receive confirmation message from server
        recv(clientSock, buffer, buffersize, 0);
        
        // clear buffer (message) before sending messages
        memset (buffer, 0, sizeof(buffer));
        
        //get information from Follower text files
        string file;
        
        if (a == 0)
            file = "Follower1";
        else if (a == 1)
            file = "Follower2";
        else if (a == 2)
            file = "Follower3";
        else if (a == 3)
            file = "Follower4";
        else if (a == 4)
            file = "Follower5";
        
        //get TCP port and IP address of each Tweet //Reused code #3 lines 199-205
        struct sockaddr_in addr;
        socklen_t len = sizeof (addr);
        if (getsockname(clientSock, (struct sockaddr *) &addr, &len) == -1)
            perror("Could not get socket name");
        else
            cout << file << " has TCP port " << ntohs(addr.sin_port) << " and IP address " << inet_ntoa(addr.sin_addr) << endl;
        
        // open Follower feedback text files
        string line1;
        
        string tweetfile = file;
        tweetfile.append(".txt");
        ifstream myfile(tweetfile.c_str());
        
        if (myfile.is_open()){
            while (getline (myfile,line1)){

                strcpy (buffer, line1.c_str());
                
                //sends each line (1 packet per line) of the tweet to the server
                send(clientSock, buffer, buffersize, 0);
                
                //clear buffer everytime it sends message
                memset (buffer, 0, sizeof(buffer));
            }
            
        }
        
        //close file
        myfile.close();
        
        //End signifies the end of each Follower Feedback
        strcpy (buffer , "End");
        send(clientSock, buffer, buffersize, 0);
        
        cout << "Completed sending feedback for " << file << endl;
        
        //close TCP socket
        close(clientSock);
        cout << "End of Phase 2 for " << file << endl << endl;
    }
    
    
    
    return 0;
}
