// USC ID # 8277174492
// Lily Kuong
// EE450 Project Phase 2 Summer 2017
// Server.cpp - TCP connections between Tweet and Follower and UDP connections between Follower


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
#include <vector>

#include <fstream>
#include <sstream>

#define portNum 3792



using namespace std;

struct follow{
    string follower;
    string like;
    
};

//  Messages that neede to be printed out
//  The server has TCP port ___ and IP address __
//  Recevived the tweet list from <Tweet#>
//  End of Phase 1 for this server

int main()
{
    
    
    
    //initialize variables that will be used (reused code #2 lines 36-41)
    // client socket will listen for clients
    // server socket will communicate (send and receive data with clients)
    int parentSock, childSock;
    int buffersize = 1024; //arbitary number
    char buffer[buffersize];
    
    struct sockaddr_in servAddr; // contains port and IP address
    socklen_t size;
    
    
    // socket
    parentSock = socket(AF_INET, SOCK_STREAM, 0);
    
    if (parentSock < 0)
    {
        perror ("Socket Error");
        exit(1);
    }
    
    //initialize address
    memset(&servAddr, 0, sizeof(servAddr));
    servAddr.sin_family = AF_INET; //IPv4
    servAddr.sin_addr.s_addr = htons(INADDR_ANY); //connect to all addresses to server
    servAddr.sin_port = htons(portNum); // sets server port number
    
    
    
    //bind
    if ((bind(parentSock, (struct sockaddr*)&servAddr,sizeof(servAddr))) < 0){
        perror("Binding Error");
    }
    
    
    //listen
    listen(parentSock, 5);
    
    
    int count = 0;
    string file = " ";
    size = sizeof(servAddr);

    
    
    //outfile for Tweet
    ofstream out;
    out.open("tweetfile.txt");
    
    //sets which file it is
    while (count < 3){
        if (count == 0)
            file = "TweetA";
        else if (count == 1)
            file = "TweetB";
        else if (count == 2)
            file = "TweetC";
        
        
        //accept
        childSock = accept(parentSock,(struct sockaddr *)&servAddr, &size);
        
        
        //get TCP port and IP address of the server //Reused code #3 lines 111-120
        // getsockname() must occur after accepting a socket has been made
        // if statement used to only produce what the server is connected to once
        if (count == 0){
            
            struct sockaddr_in addr;
            socklen_t len = sizeof (addr);
            if (getsockname(childSock, (struct sockaddr *) &addr, &len) == -1)
                perror("Could not get socket name");
            else{
                cout << "The server has TCP port " << ntohs(addr.sin_port) << " and IP address " << inet_ntoa(addr.sin_addr) << endl;
            }
        }
        
        
        // first check if it is valid or not
        if (childSock < 0)
            perror("Can't accept socket");
        
        //Fork between commands
        //reused code #1 (line 128-138)
        int pid = fork();
        
        
        if (pid < 0){
            perror("fork error");
            return -1;
        }
        
        // Child Socket
        if (pid == 0){
            close(parentSock);
            
            //sends confirmation message to client
            send(childSock, buffer, buffersize, 0);
            
            
            //save each line in a file tweetfile.txt
            while (strcmp(buffer, "End") != 0){
                
                
                //continue to receive until end of tweet is sent
                recv(childSock, buffer, buffersize, 0);
                if (strcmp(buffer, "End") != 0){
                    out << buffer << "\n";
                }
                
            }
            out << file << "\n";
            out.close();
            cout << "Received the tweet list from " << file << endl;
            
            //if all tweets have been received
            if (count == 2)
                cout << "End of Phase 1 for this server" << endl;
            exit (0);
            
        }
        else{
            close (childSock);
        }
        count++;
    }
    
    
    sleep(3);
    
    
    
    // --------------------------------PHASE 2 -----------------------------------------
    //Server sends the tweets to the followers through UDP. Server knows the static UDP port of each of the Followers
    //UDP port for server is dynamic
    
    // Prints out
    // The server has UDP port __ and IP address __
    // The server has sent <Tweet#> to the <Follower#>
    
    
    
    //-------------------------------- UDP ---------------------------------------------
    
    int follower = 0;
    int udpPortNum;
    int udpsock;
    string tweetName;
    string followerName;
    
    struct sockaddr_in udp_addr;
    socklen_t udp_len = sizeof(udp_addr);
    int udpbuffersize = 1024; // same buffer size as before
    char udpbuffer[udpbuffersize];
    
    struct sockaddr_storage serv_stor;
    
    
    while (follower < 5){
        
        if (follower == 0){
            udpPortNum = 21892;
            followerName = "Follower1";
        }
        else if (follower == 1){
            udpPortNum = 21992;
            followerName = "Follower2";
        }
        else if (follower == 2){
            udpPortNum = 22092;
            followerName = "Follower3";
        }
        else if (follower == 3){
            udpPortNum = 22192;
            followerName = "Follower4";
        }
        else if (follower == 4){
            udpPortNum = 22292;
            followerName = "Follower5";
        }
        
        //create a socket
        udpsock = socket (AF_INET, SOCK_DGRAM, 0);
        if (udpsock < 0)
            perror("UDP Sock error");
        
        
        //initialize variables in udp_addr
        udp_addr.sin_family = AF_INET;
        udp_addr.sin_addr.s_addr = htonl (INADDR_ANY);
        udp_addr.sin_port = htons(udpPortNum);
        memset(udp_addr.sin_zero, '\0', sizeof udp_addr.sin_zero);
        
        
        //send blank message to follower
        int sent = sendto(udpsock, udpbuffer, udpbuffersize, 0, (struct sockaddr *) &udp_addr, sizeof(udp_addr));
        if (sent != udpbuffersize)
            perror ("Error Sending");
        
        
        //get UDP port and IP address of server //Reused code #3 lines 246-251
        struct sockaddr_in addr;
        socklen_t len = sizeof (addr);
        if (getsockname(udpsock, (struct sockaddr *) &addr, &len) == -1)
            perror("Could not get socket name");
        else
            cout << "The server has UDP port " << ntohs(addr.sin_port) << " and IP address " << inet_ntoa(addr.sin_addr) << endl;
        
        //open tweet file
        ifstream tweetsend;
        tweetsend.open("tweetfile.txt");
        
        //clear message
        memset (udpbuffer, 0, sizeof(udpbuffer));
        
        //send Tweet to Follower
        string line;
        if (tweetsend.is_open()){
            
            while (getline (tweetsend,line)){
                strcpy(udpbuffer, line.c_str());
                udpbuffersize = sizeof(udpbuffer);
                
                //Tweet File specifies which Tweet is which.
                if (line == "TweetA" || line == "TweetB" || line == "TweetC"){
                    tweetName = line;
                    cout << "The server has sent " << tweetName << " to the " << followerName << endl;
                }
                
                sent = sendto(udpsock, udpbuffer, udpbuffersize, 0, (struct sockaddr *) &udp_addr, udp_len);
                
                if (sent < 0)
                    perror ("Issue sending");
                
            }
        }
        //close file
        tweetsend.close();
        
        
        follower ++;
        cout << endl;

        //closing socket
        close (udpsock);
    }
    
    
    
    //------------------------- TCP server and followers -------------------------------
    //receive feedback from Followers via TCP;
    //When server receives all packets from a Follower, it forwards the feedback to the specific tweet person through TCP
    
    // Prints out:
    // The server has TCP port __ and IP address __
    // Server receive the feedback from <Follower#>
    
    //outfile for Tweet
    ofstream outfollow;
    outfollow.open("followfeedback.txt");
    
    
    //sets which file it is
    count = 0;
    while (count < 5){
        if (count == 0)
            file = "Follower1";
        else if (count == 1)
            file = "Follower2";
        else if (count == 2)
            file = "Follower3";
        else if (count == 3)
            file = "Follower4";
        else if (count == 4)
            file = "Follower5";
        
        //accept
        childSock = accept(parentSock,(struct sockaddr *)&servAddr, &size);
        
        
        //get TCP port and IP address //Reused code #3 lines 326-335
        if (count == 0){
            
            struct sockaddr_in addr;
            socklen_t len = sizeof (addr);
            if (getsockname(childSock, (struct sockaddr *) &addr, &len) == -1)
                perror("Could not get socket name");
            else{
                cout << "The server has TCP port " << ntohs(addr.sin_port) << " and IP address " << inet_ntoa(addr.sin_addr) << endl;
            }
        }
        
        
        // first check if it is valid or not
        if (childSock < 0){
            perror("Can't accept socket");
            exit(0);
        }
        
        //Fork between commands
        //reused code #1 (line 346-357)
        int pid = fork();
        
        
        if (pid < 0){
            perror("fork error");
            return -1;
        }
        
        
        // Child Socket
        if (pid == 0){
            close(parentSock);
            
            // clear buffer (message) before sending messages
            memset (buffer, 0, sizeof(buffer));
            
            //sends confirmation message to client
            send(childSock, buffer, buffersize, 0);
            
            //remember which follower
            outfollow << file;
            
            //receive data from client
            while (!strstr(buffer,"End")){
                
                outfollow << buffer << endl;
                
                recv(childSock, buffer, buffersize, 0);
            }
            cout << "Server receive the feedback from " << file << endl;
            
            exit (0);
        }
        else{
            close (childSock);
        }
        count++;
        
        
    }
    //close file
    outfollow.close();
    
    //wait for all followers to send feedback to server before sorting out feedback
    sleep(1);
    cout << endl;
    
    
    // ---------------------- Sort out Feedback --------------------------------------
   
    //feedbacka stores the feedback from followers in file followfeedback.txt
    vector <string> feedbacka;
    
    //read in file with all the feedback
    string line1;
    ifstream feedbackfoll ("followfeedback.txt");
    if (feedbackfoll.is_open()){
        while (getline (feedbackfoll, line1)){
            feedbacka.push_back(line1);
        }
    }
    
    
    //struct follow contains: string follower (which follower is following specific tweet and string like (whether they liked the tweet or not)
    //TweetA represents they followed TweetA, etc.
    vector <follow> TweetAFollow;
    vector <follow> TweetBFollow;
    vector <follow> TweetCFollow;
    

    //create temporary struct follow varables used to push into the vector
    follow tempA;
    follow tempB;
    follow tempC;
    
    
    
    //Each Follower file will have 5 lines
    //  Line 1: Follower #
    //  Line 2: Following who
    //  Line 3: TweetA:like?
    //  Line 4: TweetB:like?
    //  Line 5: TweetC:like?
    // increments through feedbacka vector by 5 as a result
    for ( int i = 0 ; i < feedbacka.size(); i= i+5){
        
        //In Line 2: look for who the Follower is following and whether Follower liked the Tweet. If equals to TweetA save it in temp A and push to TweetAFollow. Same for TweetB and TweetC.
        if (feedbacka[i+1].find("TweetA") != feedbacka[i+1].npos){
            tempA.follower = feedbacka[i];
            
            if(feedbacka[i+2] == "TweetA:like"){
                tempA.like = "like";
            }
            else {
                tempA.like = "no";
            }
            TweetAFollow.push_back(tempA);
        }
        
        if (feedbacka[i+1].find("TweetB") != feedbacka[i+1].npos){
            tempB.follower = feedbacka[i];

            if (feedbacka[i+3] == "TweetB:like"){
                tempB.like = "like";
            }
            else{
                tempB.like = "no";
            }
            TweetBFollow.push_back(tempB);
        }
        
        if (feedbacka[i+1].find("TweetC") != feedbacka[i+1].npos){
            tempC.follower = feedbacka[i];
            if(feedbacka[i+4] == "TweetC:like"){
                tempC.like = "like";
            }
            else{
                tempC.like = "no";
            }
            TweetCFollow.push_back(tempC);
        }
        
    }
    
    
    //--------------------------- TCP Server and Tweet ----------------------------------
    // Forwards the feedback to the specific tweet person through TCP
    
    // Prints out:
    // The server has TCP port __ and IP address __
    // The server has send the feedback result to <Tweet#>
    // End of Phase 2 for the server
    
    count = 0;
    while (count < 3){
        if (count == 0)
            file = "TweetA";
        else if (count == 1)
            file = "TweetB";
        else if (count == 2)
            file = "TweetC";
        
        
        childSock = accept(parentSock,(struct sockaddr *)&servAddr, &size);
        
        //get TCP port and IP address //Reused code #3 lines 491-500
        if (count == 0){
            
            struct sockaddr_in addr;
            socklen_t len = sizeof (addr);
            if (getsockname(childSock, (struct sockaddr *) &addr, &len) == -1)
                perror("Could not get socket name");
            else{
                cout << "The server has TCP port " << ntohs(addr.sin_port) << " and IP address " << inet_ntoa(addr.sin_addr) << endl;
            }
        }
        
        // first check if it is valid or not
        if (childSock < 0){
            perror("Can't accept socket");
            exit(0);
        }
        
        //Fork between commands
        //reused code #1 (line 510-523)
        int pid = fork();
        
        
        if (pid < 0){
            perror("fork error");
            return -1;
        }
        
        
        
        // Child Socket
        if (pid == 0){
            close(parentSock);
            
            // clear buffer (message) before sending messages
            memset (buffer, 0, sizeof(buffer));
            
            //sends confirmation message to client
            send(childSock, buffer, buffersize, 0);
            
            
            //receive a message fromc client
            recv(childSock, buffer, buffersize, 0);
            
            // clear buffer (message) before sending messages
            memset (buffer, 0, sizeof(buffer));
            
            int i;
            if (file == "TweetA"){
                for (i = 0; i < TweetAFollow.size(); i++){
                    
                    //send which follower
                    strcpy(buffer, TweetAFollow[i].follower.c_str());
                    send(childSock, buffer, buffersize, 0);
                    
                    // clear buffer (message) before sending messages
                    memset (buffer, 0, sizeof(buffer));
                    
                    //send if they liked it or not
                    if (TweetAFollow[i].like == "like"){
                        strcpy(buffer, TweetAFollow[i].like.c_str());
                        send(childSock, buffer, buffersize, 0);
                    }
                }
            }
            
            if (file == "TweetB"){
                for (i = 0; i < TweetBFollow.size(); i++){
                    
                    //send which follower
                    strcpy(buffer, TweetBFollow[i].follower.c_str());
                    send(childSock, buffer, buffersize, 0);
                    
                    // clear buffer (message) before sending messages
                    memset (buffer, 0, sizeof(buffer));
                    
                    //send if they liked it or not
                    if (TweetBFollow[i].like == "like"){
                        strcpy(buffer, TweetBFollow[i].like.c_str());
                        send(childSock, buffer, buffersize, 0);
                    }
                }
            }
            
            if (file == "TweetC"){
                for (i = 0; i < TweetCFollow.size(); i++){
                    
                    //send which follower
                    strcpy(buffer, TweetCFollow[i].follower.c_str());
                    send(childSock, buffer, buffersize, 0);
                    
                    // clear buffer (message) before sending messages
                    memset (buffer, 0, sizeof(buffer));
                    
                    //send if they liked it or not
                    if (TweetCFollow[i].like == "like"){
                        strcpy(buffer, TweetCFollow[i].like.c_str());
                        send(childSock, buffer, buffersize, 0);
                    }
                }
            }
            
            //End signifies the send of the feedback for that Tweet
            strcpy (buffer, "End");
            send(childSock, buffer, buffersize,0);
            
            //The feedback is done sending
            cout << "The server has send the feedback result to " << file << endl;
            exit(0);
            
        }
        else{
            close (childSock);
        }
        count++;
        sleep(1);
        
        
    }
    sleep (1);
    cout << "End of Phase 2 for this server" << endl;
    
    return 0;
}
