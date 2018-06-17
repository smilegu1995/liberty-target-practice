Client               Server                  Target
StartGame   Post  -> Create New Game   ->    newgamecycle()
Score update	  <-	 SSE method	   <- 	 Target Hit







0. Server side receive request from client to start game
1a. Server notify target to run diagnostic sequence
1b. Server notify client diagnostic sequence complete
2. Client notice server to start game streaming session
3. Target start game cycle on arduino
4. Server side TargetTCPClient listen to hit events from targets
5. Server side calculate score based on hit events
6. Server side broadcast score to client
7. When server side's timer's up, notifiy targets to end game cycle
8. Write final score to MongoDb
