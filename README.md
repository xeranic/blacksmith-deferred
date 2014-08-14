blacksmith-deferred
===================
Simplified Java implementation of Deferred and Promise

[![Build Status](https://travis-ci.org/xeranic/blacksmith-deferred.svg?branch=master)](https://travis-ci.org/xeranic/blacksmith-deferred) [![Coverage Status](https://coveralls.io/repos/xeranic/blacksmith-deferred/badge.png?branch=master)](https://coveralls.io/r/xeranic/blacksmith-deferred?branch=master)

Relationship with JDeferred
---------------------------
JDeferred a greate library that bring Deferred/Promise patten to Java world! You can find it here: http://jdeferred.org/ or here: https://github.com/jdeferred/jdeferred

We use JDeferred in few of projects, and pretty happy with the elegent and power come with it. However we believe it could be better if we get simplier interface. To achieve that we have to completely change the interface, it will break the backward compatibility of JDeferred (may be this could be JDeferred 2.0). That's why we got the idea from JDeferred and reimplemented here.

Why Simplified
--------------
To use JDeferred you need at least three Type Parameters (Result, Failure, Progress). If you don't need Progress, you can use Void as type, but you still need to mention it. For example if we are loading document. We will have code like this.

```java
Promise<Document, Throwable, Double> loadDocument(String documentId);
```

In our case, we use Document as result type, Throwable for failures, and Double for progress. In our projects, we found out two thing:

1. Failure type is always Throwable
2. Progress type is always Double or Void

So the idea we have is why not just lockdown those two, and have simpler code:

```java
Promise<Document> loadDocument(String documentId);
```

In blacksmith-deferred, the failure type is Throwable, and progress type is Progress (it's just a interface with a method return a double value).
