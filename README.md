blacksmith-deferred
===================
Simplified Java implementation of Deferred and Promise

[![Build Status](https://travis-ci.org/xeranic/blacksmith-deferred.svg?branch=master)](https://travis-ci.org/xeranic/blacksmith-deferred) [![Coverage Status](https://coveralls.io/repos/xeranic/blacksmith-deferred/badge.png?branch=master)](https://coveralls.io/r/xeranic/blacksmith-deferred?branch=master)

Relationship with JDeferred
---------------------------
JDeferred is a greate library that bring Deferred/Promise patten to Java world! You can find it here: http://jdeferred.org/ or here: https://github.com/jdeferred/jdeferred

We use JDeferred in past year and pretty happy with the elegent and power. However we believe it could be better if we get simpler interface. To achieve that we have to break the backward compatibility of JDeferred (may be this could be JDeferred 2.0). That's why we reimplemented here.

How it simplified?
------------------
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

It seems not much improvement at this point, but when you are using Filter and Pipe this can save you a lot. Keep reading.


Problem of progress
-------------------
Most of asychronized call doesn't need a progress, but some of them do. So what happend in our old project is some API using Void for progress, some of them using Double for progress. For example:

```java
Promise<String, Throwable, Void> findDocument(String name);
Promise<Document, Throwable, Double> loadDocument(String documentId);
```

It's really pain to chain them together:

```java
Promise<Document, Throwable, Double> loadDocumentByName(String name) {
    return findDocumentId("123")
        .then(new DoneFilter<String, String>() {
          public String filterDone(String result) {
            return result;
          }
        }, new FailFilter<Throwable, Throwable>() {
          public Throwable filterFail(Throwable failure) {
            return failure;
          }
        }, new ProgressFilter<Void, Double>() {
          public Double filterProgress(Void progress) {
            return 0.5d;
          }
        }).then(new DonePipe<String, Document, Throwable, Double>() {
          public Promise<Document, Throwable, Double> pipeDone(String result) {
            return getDocument(result);
          }
        });
}
```

Look at that three boilerplate Filters for converting Progress type from Void to Double, and you don't even need it. I know that we should be consistent in one project, but truth is as long as there are different ways to do things people will do it.

