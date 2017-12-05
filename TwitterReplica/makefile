all : Server.o Tweet.o Follower.o

Server.o: Server.cpp
	g++ -o Server Server.cpp -lsocket -lnsl -lresolv

Tweet.o: Tweet.cpp
	g++ -o Tweet Tweet.cpp -lsocket -lnsl -lresolv

Follower.o: Follower.cpp
	g++ -o Follower Follower.cpp -lsocket -lnsl -lresolv