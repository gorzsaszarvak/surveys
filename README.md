**Description:**

There is a list with the information of 300 people who participated in different surveys. For each survey the respondent can receive points that later can be claimed as different gifts.

You can find active and inactive members in the file, in a survey we only can target the active members, but it is possible that the currently inactive members participated in previous surveys.

We are storing 4 different participation statuses:
- (1) Not asked - Not participated in the questionnaire (was not asked)
- (2) Rejected - Not participated in the questionnaire (was asked, but no response)
- (3) Filtered - Based on the filtering questions, the member did not fit into the targeted group
- (4) Completed - Filled and finished the questionnaire

The respondent can receive point only in 3 and 4 cases (points may differ between Filtered or Completed).

**Task:**

1. Read all the data from the following text files: Members, Surveys, Statuses and Participation into the memory of your application
   - you can expect that the file content is not changed externally in the meanwhile
   - you should use the proper data structure to read the data into

2. Implement the following functions:
   - a) Fetch all the respondents who completed the questionnaire for the given survey id
   - b) Fetch all the surveys that were completed by the given member id
   - c) Fetch the list of points (with the related survey id) that the member collected so far (the input is the member id)
   - d) Fetch the list of members who can be invited for the given survey (not participated in that survey yet and active)
   - e) Fetch the list of surveys with statistics
      - it should contain the following fields:
        survey id,
        survey name,
        number of completes,
        number of filtered participants,
        number of rejected participants,
        average length of time spent on survey (Participation.length)

**Notes:**
- all functionality should be written in Java
- test coverage is a plus
