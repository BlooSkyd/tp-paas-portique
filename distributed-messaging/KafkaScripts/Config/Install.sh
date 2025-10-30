#!/bin/bash

cd /opt
wget https://dlcdn.apache.org/kafka/4.1.0/kafka_2.13-4.1.0.tgz
tar -xvf kafka_2.13-4.1.0.tgz
mv kafka_2.13-4.1.0 kafka
rm kafka_2.13-4.1.0.tgz
