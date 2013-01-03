Munit common core module
=========================

This module is the common api to make munit work. It contains all the classes that provides mocking, verification and assertion.

Packages
--------

1) connectors: Mule connectors interceptors, it allows to mock connections of mule connectors at startup, for example,
the db server mule connector
2) endpoint: Endpoints management like mocking, wrapping, etc
3) matchers: The matchers for munit mocking/mel expressions etc
4) mocking: Classes for message processors mocking.
5) mp: mocking of message processors uses the representation of the message processor in this class, interceptors, etc
6) spring: util classes to create bean definitions for mocking.

