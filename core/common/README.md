Munit common core module
=========================

This module is the common api to make munit work. It contains all the classes that provides mocking, verification and assertion.

Packages
--------

* connectors: Mule connectors interceptors, it allows to mock connections of mule connectors at startup, for example,
the db server mule connector
* endpoint: Endpoints management like mocking, wrapping, etc
* matchers: The matchers for munit mocking/mel expressions etc
* mocking: Classes for message processors mocking.
* mp: mocking of message processors uses the representation of the message processor in this class, interceptors, etc
* spring: util classes to create bean definitions for mocking.

