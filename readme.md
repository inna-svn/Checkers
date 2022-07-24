# Checkers

## Decisions

* Simplistic approach of abandoned games - don't include in scores
* A user is limited to single active Game. To change, update anything related to `User.activeGame`
* Lobby per game type
* A user is automatically added to the only lobby we have (the checkers game lobby)
