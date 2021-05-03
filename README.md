# FIT3077 Assignments

Download and open the project with Android Studio and it will setup all of the necessary dependencies.(Look in build.gradle if it does not do it automatically for you)
In build.gradle(module level) click on the sync now button if it is visible

If you're using another IDE please refer to this link to set up your SDKs and AVD 
https://medium.com/michael-wallace/how-to-install-android-sdk-and-setup-avd-emulator-without-android-studio-aeb55c014264

Please make sure that you have an Android Virtual Device(AVD) on your PC. 
To start just select the AVD and run the program that will install the app on your AVD.
To add your API key to the obtain access to the web service, you can either,

1. Add a file "secrets.properties" at the root level of the application, and write
   FIT3077_API_KEY=YOUR_API_KEY   
   (Set FIT3077_API_KEY to YOUR_API_KEY directly, do not enclose in quotes like a string)
   
2. Add your API key directly in the APIUtils.java file
   (From the Project root level, the APIUtils file may be found in app\src\main\java\com\example\studenttutormatchapp\remote)

Refer the demonstration video to understand the process flow of the app