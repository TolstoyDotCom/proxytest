# proxytest

This is part of an attempt to get the [Twitter censorship checker](https://github.com/TolstoyDotCom/more-speech) working again.

This has only been tested on Ubuntu 18 using JDK 17 and Firefox 105.

1. Install JDK 17, make sure mvn is using it.

2. Create a new Firefox profile named "twtr_censorship". Login to Twitter and Yahoo mail. It's probably better to use a throwaway Twitter account. You can log in to some other site than Yahoo mail, the goal is to have a site that a) works, and b) shows that you're logged in.

3. mvn clean compile exec:java -D"exec.mainClass"="com.tolstoy.proxytest.ProxyTest"

As downloaded, this will load Twitter OK. However, I need the proxy so I can examine the Twitter AJAX messages. If you set USE_PROXY to true, Twitter will just show a "something went wrong" message. If you then change it to use Yahoo mail, it will work with the proxy.
