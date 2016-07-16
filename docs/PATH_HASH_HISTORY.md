# Path URL history

Here we are talking about path URL history or 'Push State' with modern JavaScript frameworks and how to take care of them while working with Liferay. For example Liferay page where application sits on `/web/guest/home` and one route may look like `/web/guest/home/todo/1`.

#### Pros

- Most of the modern frameworks support this by default
- Google and other search engines can index pages with push state URLs

#### Cons

- Need to do some work to make it with Liferay




# Hash URL history

Here we talk about applications with hash urls. For example `/web/guest/home#/todo/1`.

With Liferay the easiest ways to set up a JavaScript application is to use hash URLs. Then there are no need to set base urls or context path into your JavaScript application. This document explains step by step how to take hash URLs in use on different JavaScript frameworks.

#### Pros

- Easy to set up
- No danger of clash URLs with Liferay

#### Cons

- Google and other search engines don't index pages with hash \# .
