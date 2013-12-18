#!/bin/bash

rm -r jcoap/
 mkdir jcoap
 javac  -d jcoap org/ws4d/coap/*.java
 javac -d jcoap org/ws4d/coap/connection/*.java
 javac  -d jcoap org/ws4d/coap/interfaces/*.java
 javac -d jcoap org/ws4d/coap/messages/*.java
 javac -d jcoap org/ws4d/coap/rest/*.java
 javac -d jcoap org/ws4d/coap/tools/*.java
 cd jcoap
 jar cf jcoap.jar org



