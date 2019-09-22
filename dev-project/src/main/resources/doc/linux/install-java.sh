#!/bin/bash
#java-installer

if type -p java; then
    echo 'java installed.'
    exit 0
else
    echo 'installing java...'
    wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "https://download.oracle.com/otn-pub/java/jdk/8u201-b09/42970487e3af4f5aa5bca3f542482c60/jdk-8u201-linux-x64.rpm" 
    #yum localinstall jdk-8u201-linux-x64.rpm
    rpm -i jdk-8u201-linux-x64.rpm
    if ! type -p java; then
        echo 'FAILED'
        exit 1
    else
        echo 'setting classpath'
        echo 'export JAVA_HOME="/usr/java/default"' >> /etc/environment
        source /etc/environment
        echo 'SUCCESS'
    fi
fi