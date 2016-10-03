#!/usr/bin/env bash

input_filename=$1
data_file=$2

while IFS='' read -r line || [[ -n "$line" ]]; do
    average_info=`grep ${line} ${data_file} | awk '{print $9}' | awk '{sum+=$1;}END{print "average: ",sum/NR}'`
    echo $line $average_info

done < ${input_filename}