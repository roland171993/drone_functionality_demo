# drone_functionality_demo
Waypoint Mission Test to try Mapping


My Config

Mac OS Big Sur
Android Studio 4.2
Phantom 4 Advanced
Android 11
Language Java

-Issue: Cannot Continu Next Mission After First is Done

-Where Found waypointMissionOperator: 
waypointMissionOperator.loadMission   -> MissionDJI.java -> line 667
waypointMissionOperator.uploadMission -> MissionDJI.java -> line 689 

-Test Found Error:
1-Grant Permission
2-Enable Location
3-Press Register Button 
4-Wait Register Done
5-Press Start Play Button(Right) to Start new Mission
6-Wait first Mission Complete
7-Press Start Play Button(Right) again => Will never start

-Video : WATCH ME - Issue Demo.mov
I use Simulator to Make a Video just to make you understand how it work and when you cannot continu next Mission. It the same behavior when i start mission outdoor and without enable simulator.



Thank 
Roland

