# Recruiting Mobile Developer (iOS/Android): Simple weather Application Requirements

Create a simple weather application that allows to retrieve the current weather condition at a specified location and get a couple days forecast for the same location.
The application should have at least 3 different screens :

1. Welcome page
2. Current weather conditions
3. Forecast weather conditions for the next days

The Welcome page should be displayed only the first time the App is opened - it can be automatically dismissed after a certain period of time or contains a button that allow the user to start his journey.

The current weather conditions should allow the user to input his location (City) to retrieve the current weather conditions.

The current weather condition should at least display

- Current weather conditions (Sunny, Cloudy, Rain, Snow, etc…) in text and icons Current Temperature and Feels like temperature
- Humidity ( % )
- Wind speed

There should be a link and/or an icon that user can click to get the forecast for next days The forecast weather should display

Weather conditions (Sunny, Cloudy, Rain, Snow, etc…) in text and icons

Temperature and Feels like temperature

The Application should retain the last location that was retrieved when it gets re-opened. Error Handling : If API is not reachable / timeout an error should be displayed in the App to tell the user that data is not available at this time with a retry mechanism.
Note : You can use any Open Weather API available on the net.

## Criteria

We’ll be evaluating your submission according to several criteria:

1. Was a reviewer able to run your code/application on their computer?
2. Does it contains expected screens and displays based on the requirements? 3. Does it handle errors on API requests/responses?
3. How readable is your submission?
4. How efficient is the code?
5. How well-structured is your submission?
6. How well-documented is your submission?
7. How well-tested is your submission?

## FAQ

1. What programming language should I use?
   For iOS swift (swift UI) would be preferred, for Android Kotlin (Compose) would be preferred 2. Can I use an ABC library or a XYZ framework?
   Yes, you are free to choose any framework or library you want. However you can’t use a full library that implement the UI/Logic of weather API directly or you can’t clone an already existing project/code.
2. How long should I take to complete this?
   Please don’t spend more than 4-5 hours to create your submission. You can take as much time as necessary to design and research a solution.
3. What should I include with my submission?

Please include a README.md with
a. Explanation of the Application flow that you’ve implemented
b. Explanation of your choice framework, library, API
c. Explanation of assumptions you made
d. Explanation of improvements you wish to make 5. Where should I submit the code?
a. Ideally the code would be submitted in a Github/Gitlab repo. 6. Should I include unit tests?
Yes, please provide unit tests with your submission.
