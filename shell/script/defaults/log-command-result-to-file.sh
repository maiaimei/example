#!/bin/bash

CURRENT_SCRIPT_PATH=$(cd $(dirname $0); pwd)
PROJECT_PATH=${CURRENT_SCRIPT_PATH%%/release*}
LOG_FILE_PATH="${PROJECT_PATH}/log/test.log"
EXECUTE_COMMAND=$1

eval ${EXECUTE_COMMAND} 2>&1 | tee -a ${LOG_FILE_PATH}
