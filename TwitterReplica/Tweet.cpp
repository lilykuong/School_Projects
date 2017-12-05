// USC ID # 8277174492
// Lily Kuong
// EE450 Project Phase 2 Summer 2017
// Tweet.cpp - TCP connections with Server


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


#define portNum 3792 // NOTE that this is the port number of the server

using namespace std;

//  Messages that needs to be printed out FOR TWEETS
//  <Tweet#> has TCP port __ and IP address__ for Phase 1
//  <Tweet#> is now connected to the server
//  <Tweet# has sent <tweet> to the server
//  Updating the server is done for <Tweet#>
//  End of Phase1 for <Tweet#>

int main()
{
    vector <string> tweet;
    
    //sets the file in order of Tweets
    //used for reading the data from txt files and command outputs
    string file;
    for (int a = 0; a < 3; a++){
        if (a == 0)
            file = "TweetA";
        else if (a == 1)
            file = "TweetB";
        else if (a == 2)
            file = "TweetC";
        
        // open file
        // adds the tweets to a vector string
        string line1;
        string tweetfile = file;
        tweetfile.append(".txt");
        ifstream myfile(tweetfile.c_str());
        
        if (myfile.is_open()){
            while (getline (myfile,line1)){
                tweet.push_back (line1);
                //cout << line1 << endl;
            }
            myfile.close();
        }
        tweet.push_back("End"); // add to vector to symbolize the tweet file has ended
        
        // set up the socket
        // clientSock is the socket
        // server_address contains the static port number of the server
        // (reused code #2 lines 72-75)
        
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
        
        
        //get TCP port and IP address of each Tweet //Reused code #3 lines 99-104
        // getsockname() must occur after a connection with the server has been made
        struct sockaddr_in addr;
        socklen_t len = sizeof (addr);
        if (getsockname(clientSock, (struct sockaddr *) &addr, &len) == -1)
            perror("Could not get socket name");
        else
            cout << file << " has TCP port " << ntohs(addr.sin_port) << " and IP address " << inet_ntoa(addr.sin_addr) << " for Phase 1" << endl;
        
        // receive confirmation message from server
        recv(clientSock, buffer, buffersize, 0);
        cout << file << " is now connected to the server" << endl;
        
        // clear buffer (message) before sending messages
        memset (buffer, 0, sizeof(buffer));
        
        
        //takes the tweet and sets it as the message
        for (int c = 0; c < tweet.size(); c++){
            for (int i = 0 ; i < tweet[c].length(); i++){
                buffer[i] = tweet[c][i];
            }
            
            //sends each line (1 packet per line) of the tweet to the server
            send(clientSock, buffer, buffersize, 0);
            
            //End represents the end of a tweet. It is sent over to the server so the server knows when the file has ended but does not store end.
            if (strcmp(buffer, "End") != 0){
                cout << file << " has sent '" << buffer << "' to the server" << endl;
            }
            
            //clears buffer for the next packet
            memset (buffer, 0, sizeof(buffer));
        }
        //clears the vector for the next file
        tweet.clear();
        
        //close socket
        cout << "Updating the server is done for " << file << endl;
        close(clientSock);
        cout << "End of Phase 1 for " << file << endl << endl;
    }
    
    
    //---------------------------- Phase 2 --------------------------------------
    //need to list out TweetA and all the followers and who liked who. If they did not like, don't list
    
    // Prints out
    // <Tweet#> has TCP port __ and IP address __ for Phase 2
    // <Follower#> is following <Tweet#>
    // <Follower#> has liked <Tweet#>
    // End of Phase 2 for <Tweet#>
    
    //similar code to all TCP connections used in this project
    
    //wait for Follower and server to do their thing before getting feedback from server
    sleep (5);
    
    for (int a = 0; a < 3; a++){
        if (a == 0)
            file = "TweetA";
        else if (a == 1)
            file = "TweetB";
        else if (a == 2)
            file = "TweetC";
        
        
        // set up the socket
        // clientSock is the socket
        // servAddr contains the static port number of the server
        
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
        
        if (connect(clientSock,(struct sockaddr *)&servAddr, sizeof(servAddr)) < 0)
            perror( "Trouble connecting");
        
        
        //get TCP port and IP address of each Tweet //Reused code #3 lines 193-198
        struct sockaddr_in addr;
        socklen_t len = sizeof (addr);
        if (getsockname(clientSock, (struct sockaddr *) &addr, &len) == -1)
            perror("Could not get socket name");
        else
            cout << file << " has TCP port " << ntohs(addr.sin_port) << " and IP address " << inet_ntoa(addr.sin_addr) << " for Phase 2" << endl;
        
        
        //receive confirmation message from server
        recv(clientSock, buffer, buffersize, 0);
        
        //send confirmation
        send(clientSock, buffer, buffersize, 0);
        
        //clear buffer (message) before sending messages
        memset (buffer, 0, sizeof(buffer));
        
        //stores all the feedback sent my the server for tweet
        vector <string> feedback;
        
        //End signifies the end fo the feedback for each Tweet
        while (strcmp(buffer, "End") != 0){
            
            //receive message from server
            recv(clientSock, buffer, buffersize, 0);
            feedback.push_back(buffer);
        }
        
        //print out who is following tweet and if they liked it yet
        for (int i = 0; i < feedback.size(); i++){
            if (feedback[i] != "End"){
                if (feedback[i] != "like")
                    cout << feedback[i] << " is following " << file << endl;
                
                if (feedback[i] == "like")
                    cout << feedback[i-1] << " has liked " << file << endl;
            }
        }
        
        //delete all elements in vector
        feedback.clear();
        
        //close
        close(clientSock);
        cout << "End of Phase 2 for " << file << endl;
    }
    
    
    
    return 0;
}
