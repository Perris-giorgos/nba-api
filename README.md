# General Info
This micro-service exposes REST API using API https://rapidapi.com/theapiguy/api/free-nba/endpoints and delivers
 the following functionality:
1. Lists all NBA matches for a given date.
2. Returns data just for a single game specified by gameID
3. Allows adding multiple custom comments to any NBA match. A comment is just a simple text entry and a timestamp of when the comment was added or modified.
4. It should be possible to remove and update each individual comment.
We are only interested in following data for each match:
1. Game ID
2. Date of the game
3. Home and Away team names
4. Home and Away team scores
5. List of all custom comments (if such were manually added to the match) ordered by timestamp (newest first)
6. We would also like to have a sum of how many points each player scored in the game (calculated value). Any player who didn’t score a single point in the match can be excluded from the list of players.

# Clarifications
## Database
In this project h2 in memory database has been used in order to achieve the fastest and easiest run process for
 testing purposes. In this way the reviewer does not need any external tooling like Docker or and installed Database
  locally. 
  Running the application will initialize all the comment data included in the file src/main/resources/data.sql. 
Database accessible through browser in http://localhost:8086/h2-console.
- username: sa
- password: pass

# Caching
The same database is used to store info received from the external API as requested in the assignment utilizing  a
 custom logic. Another way to cache data would be to use spring-boot-starter-cache for "automated" in memory caching.
 Retrieve data from cache is used only for the getGamesById operation. Application is not searching in cache
  for getGamesByDate because we cannot assure if we have cached the total number of games for a given date. But cache
   is update after calling any of the above.
A custom annotation created in order to handle the create cache logic.  
 - @PutCache: add created response into the db

#Endpoint
There are currently two Controllers exposing the functionalities of this test project.
## CommentController
This controller manages comments regarding specific games.
Endpoints
- Create comment: POST /comment/create 
- Modify comment: PATCH /comment/{commentId}
- Delete comment: DELETE /comment/delete/{commentId}
 
## MatchController
This controller returns match details as described in [General Info](#general-information).
- Retrieve games by date: GET localhost:8085/games/{date} (date format: YYYY-MM-DD)
- Retrieve game details by the game id: GET localhost:8085/games?gameId={gameId}

# Action Audit
Table ActionsAudit is used to log the actions executed through the controllers. Saving the action type and if it was
 successful we can monitor the proper functionality of our application. 
 In order to extract the auditing logic from the service and controller code, a custom annotation was created to
  generally handle the logging of all the requests. 

# Additional Comments
During the analysis of the Free NBA API a lot of problems found in the documentation and the actual functionalities
 of the API. The goal that this project tries to achieve is to provide the requested information executing the
  minimal call to the external API. For this purpose Stats endpoint was selected because it provides info about
   players and the actual match details in one call and there is no need to make other followup calls for the
    requested parameters. 
    However,  the problem with stats endpoint is that it could not process correctly the date params provided. Test
     call made using dates[] param such as combinations with start_date/end_date. Comparing to other online APIs it
      seems that the games returned are not the correct for several request dates.

      
      
