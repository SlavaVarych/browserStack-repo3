browserStack-repo3

    This projectis is sample code repo to reproduce browserStack issue with Appium 2

Getting local copy of the project

    Git Clone the project:

$ cd <Path to git folder>
$ git clone https://github.com/SlavaVarych/browserStack-repo3.git

    You should now see the folders

Folder name 	Description
main 	contains the page objects and infrastructure tools
test 	contains web application tests

Compiling the browserStack-repo3 project using maven

$ maven clean install

Running test using maven


# the actual command to run tests locally:
$ mvn clean test -Dsuite=TestSuite -Dprovider=local -Dplatform=android -DautomationBranch=main

# the actual command to run tests with browserStack:
$ mvn clean test -Dsuite=TestSuite -Dprovider=browserStack -Dplatform=android -DautomationBranch=main
(you should add browserStack credentials to browserStack.properties file)

