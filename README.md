# tgNBA

## Overview

Lists NBA teams, their games and allows to search players. Has infinite scroll capability when
showing list of games.
[Workflow diagram (drawio)](https://drive.google.com/file/d/1YadgljemUh4-bdYyK4udD27gbQdjg-yj/view?usp=sharing)

## Why this project

This is a technical task provided by [TGlab](https://www.linkedin.com/company/tg-lab/) for Android
Developer position. The exact requirements were as follows:

**Home page tab**

1. List following data in a list (full_name, city, conference).
2. Tapping each row should navigate to a new bottom sheet view and list all games for the selected
   team. See Selected Team Data section.
3. Tapping Name button should show a dialog where user can select from these order types: Name,
   City, Conference. Selected value should update the button title and order the list.
4. Use this API to get list of NBA teams: https://www.balldontlie.io/api/v1/teams

**Selected Team Data**

5. From Home page Tab user can select a team to see recent games. TEAM ID must be passed to this
   View and then used here to geta list of games for specific team. User can get back using Back
   button or via
   <Home button at the top.
6. List following details: (home_team.full_name, home_team_score, visitor_team.full_name,
   visitor_team_score).
7. Implement endless scroll.
8. Use this API to get list of Games: https://www.balldontlie.io/api/v1/games?&team&ids[]=TEAM_ID

**Search Players Tab**

9. List data: (first_name, last_name, team.full_name).
10. Each row should show player's team data on touch. Reuse previously created Selected Team View.
11. Implement Search.
12. Use this API to search players by
    name: https://www.balldontlie.io/api/v1/players?search=PLAYER_NAME

## Screenshots

![tgNBA user interface](https://i.ibb.co/C0nWGY1/tg-NBA-screenshot.png "tgNBA user interface")

## What Did I Use?

- MVVM architecture pattern
- Kotlin Coroutines
- Koin DI
- Jetpack Navigation