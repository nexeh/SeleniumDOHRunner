SeleniumDOHRunner
=================

This is a Selenium script written in java to run your Dojo DOH tests. I  made this for my Jenkins environment. I originally made it very specific for my environment but i have been slowly working to make it more generic for everyone else to use as well. My environment is running Jenkins, jscover, selenium hub, and a custom plugin to the doh testharness. However my intent is to remove any dependancy to those. Feel free to contribute and/or submit issues/bug/enhancments.

Syntax
------

	java SeleniumDohRunner [-a] [-c=<jsocverContent>] [-h=<hubUrl>] [-p=<pluginString>] [-b=<browser>] [-l=<false>] [-o=<false>] HostURL HostPort RunnerPath TestModule Path

Example

	java SeleniumDohRunner -a -c=jscoverage.html -h=http://localhost:4444/wd/hub -p=lmig/patches/doh/DohRunner;lmig/patches/doh/DohRunnerCI -b=firefox localhost 8080 PmDojo/util/doh/runner.html com.lmig.modulename test,com.lmig.modulename

Options

	-a  : Async on. Default is false.
	-b=<browser : If you have a preference on the browser to run on
	-x : This is used to add a custom firefox profile that turns off the unresponsive script dialog in firefox. In the future id like a way to pass this in a better way.
	-c=<jsocverContent>: Indicates that jscover will be used by passing the relative url
	-h=<hubUrl> : Indicates that a selenium hub is in by by passing the Selenium hub url
	-p=<pluginString> : Used to add monkey patches to the url.
	-l=<false> : Default is true. This indicates if you are using a patch to the doh runner.html that adds a div with an id of 'loaded' when the tests begin to run. If this is true there will be a selenium WebDriverWait setup to timeout after 30 seconds. This is used because DOH will give no indication that something has gone wrong and will sit there forever.
	-o=<false> : Default is true. This indicates if you are using a patch to the doh runner.html that adds a div with an id of 'consoleLog' that contains the browsers console log. If this is true then we will take the contents of this div and put it into the CI console log for debugging purposes.

TODO
-----
- There is currently a dependency on a plugin that adds a "loaded" class to the DOM to indicate the test harness, tests, and required files loaded correctly
- There is currently a dependency on a plugin that adds a div where we dump the browser console too
- add a option attribute to the CLI that accepts 1...n key value pairs for firefox profile options
- passing in a timeout configuration
- implement non selenium hub. options are in place but really only focused on selenium hub implementation
