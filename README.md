MDF3
====
- Mobile Development Frameworks 3.
- Full Sail University Online. 
- Java Android weekly projects.
- June 2013. 
- Instructor: Donna Gardinier
- Student: Monica Peters

====
PROJECT 1. APP INTEGRATION. ROCKMYPIC
* Due Thursday June 6th 2013
* Completed Wednesday June 5th 2013

REQUIREMENTS:
* use 1 min. screen management technique.
* app launches activity in response to external implicit intent.
-- Use Image App on Device.  Pick Image. Tap Share on Top Right. Choose RockMyPic App.
* app handles dynamic data (user input) sent by the launching intent.
-- image sent to app from gallery app on device.
* app uses orientation restrictions and/or window enhancements / alterations to frame the app with correct usability.
-- res/layout and res/layout-land
* ui includes personalization for the app.
* ui shows data.
* ui shows relevant nested layouts, colors, hints.
* app is structured with relevant organization & constructs.

====
PROJECT 2. ADVANCED FEATURES.
* Due Thursday June 13th 2013
* Completed

REQUIREMENTS:
* app uses 1 feature (minimum) from each group. 
* group 1: camera, video, or audio playback.
* group 2: location, sensors, gps, file storage, proximity detection.
* group 3: status management (phone or network or battery), notifications, hardware sensors.

Planned: 
Record Audio Notes about Specific Map Locations and Save to device.
When device is within x distance to Map Location, the Audio file Plays.
Toast shows Battery, Phone, and Network Status.

====
PROJECT 3. WIDGETS & ACTIONBARS.  ActionBarWidgetDemo.
* Due Thursday June 20th 2013
* Completed Monday June 17th 2013

REQUIREMENTS:
* action bar nav. with 2 or more activities.
* action bar shortcuts. min: 1 shortcut icon, 1 option in overflow nav.
* widget to add to home screen with usable view that will launch an intent handled by the app.  The intent displays dynamically changing data.
* 2 or more activities.
* activity nav handled through action bar.
* 1 or more quick launch icons to serve as either navigation or function (search).
* overflow menu with at least 1 functional option.
* 1 or more launch intent.
* able to add to home screen.
* 1 or more views.
* extra points:  resizable, multiple views, stackview, responsive intents.

 * ACTION BAR part one
 *     ACTIVITIES 2 OR MORE
 *     		MainFeedActivity, AuthenticateActivity, LoginOrSignUpActivity: 
 *     				login or register to use app.
 *     				parse.com db
 *     		AddLinkActivity: add url / note to parse.com db
 *     		SelectUsersActivity: view registered users of app. tap to check next to users.
 *     
 *     ACTIVITY NAVIGATION HANDLED THROUGH ACTION BAR
 *     1 or more quick launch icons that serve as either navigation or function (ie Search)
 *      	refresh list of links
 *      	add link
 *     		view list of users that use this mobile app
 *     		log out
 *     
 *     OVERFLOW MENU WITH AT LEAST 1 FUNCTIONAL OPTION
 *     		Follow: check to add other users to list.
 *     		Log Out: will only see login / register on main activity.
 *     		^both will show icons while in landscape view
 *     
 * WIDGET part two
 *     VIEWS 1 OR MORE
 *     1 OR MORE SUPPORTED LAUNCH INTENTS
 *     ABLE TO BE ADDED TO HOME SCREEN
 *     
 * ABOVE & BEYOND
 *     RESIZEABLE
 *     	http://developer.android.com/guide/topics/appwidgets/index.html#MetaData
 *     MULTIPLE VIEWS OR ADVANCED VIEW (STACKVIEW)
 *     	landscape layout, portrait layout
 *     RESPONSIVE INTENTS
 *     	progressbar
 *     	If user logged in last time, don't ask them to login this time.
 *      If user did not login last time, only show them the login or signup.
 *      If user logged out, only show them the login or signup view.
 *      If links are available, show them on main view.

Learning Resources / Tutorials
 * Parse.com used for managing database (links, users, authentication)
 * Widget http://www.vogella.com/articles/AndroidWidgets/article.html
 * Widget http://www.sitepoint.com/how-to-code-an-android-widget/
 * Responsive http://developer.android.com/training/articles/perf-anr.html
 * Action Bar http://www.vogella.com/articles/AndroidActionBar/article.html
 * Action Bar http://developer.android.com/reference/android/app/ActionBar.html

====
PROJECT 4. HYBRID APP.
* Due Thursday June 27th 2013
* Completed

REQUIREMENTS:
* WebView: 1 data collection control.
* WebView: 1 ui control (button...).
* WebView: 1 js method for enhanced ui.
* WebView: 1 js method for native integration.
* Native: 1 meaningful activity including a WebView.
* Native: Properly defined js interface.
* Native: 1 min. method to accept & handle data from WebView.
* Native: 1 intent initiated from method called from WebView.
* WebView.  user enter or create data to be used & integrated with native functionality.  The view contains the proper javascript methods to interface with the native functionality.
* Native.  app interfaces with a webview to obtain and manage or manipulate data.  The app handles a web initiated call to a native intent which seamlessly flows from web to native functionality.
* General. solution realistically solves a real world dev need.
* 
