#!/bin/bash

CURRENT_SERVER_NAME=$(hostname)
CURRENT_USER=$(whoami)
CURRENT_SCRIPT_PATH=$(cd $(dirname $0); pwd)
PROJECT_PATH=${CURRENT_SCRIPT_PATH%%/release*}
LOG_FILE_PATH="${PROJECT_PATH}/log/test.log"

EXECUTE_SCRIPT_NAME=$1
LOG_LEVEL=$2
LOG_MESSAGE=$3

case ${LOG_LEVEL} in
"INFO")
    echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} \033[32m[INFO]\033[0m ${EXECUTE_SCRIPT_NAME} - ${LOG_MESSAGE}" 2>&1 | tee -a ${LOG_FILE_PATH};;
"WARN")
    echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} \033[33m[WARN]\033[0m ${EXECUTE_SCRIPT_NAME} - ${LOG_MESSAGE}" 2>&1 | tee -a ${LOG_FILE_PATH};;
"ERROR")
    echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} \033[31m[ERROR]\033[0m ${EXECUTE_SCRIPT_NAME} - ${LOG_MESSAGE}" 2>&1 | tee -a ${LOG_FILE_PATH};;
*)
    echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} ${EXECUTE_SCRIPT_NAME} - ${LOG_MESSAGE}" 2>&1 | tee -a ${LOG_FILE_PATH};;
esac
