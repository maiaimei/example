#!/bin/bash

CURRENT_SERVER_NAME=$(hostname)
#CURRENT_SERVER_NAME=$(hostname -I) # get IP
CURRENT_USER=$(whoami)
CURRENT_SCRIPT_NAME=$(basename $0)

LOG_FILE_FULL_PATH="/opt/app/log/nohup.log"

log_to_file(){
    local log_level=$1
    local log_message=$2

    case ${log_level} in
    "INFO")
        echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} \033[32m[INFO]\033[0m ${CURRENT_SCRIPT_NAME} - ${log_message}" 2>&1 | tee -a ${LOG_FILE_FULL_PATH};;
    "WARN")
        echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} \033[33m[WARN]\033[0m ${CURRENT_SCRIPT_NAME} - ${log_message}" 2>&1 | tee -a ${LOG_FILE_FULL_PATH};;
    "ERROR")
        echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} \033[31m[ERROR]\033[0m ${CURRENT_SCRIPT_NAME} - ${log_message}" 2>&1 | tee -a ${LOG_FILE_FULL_PATH};;
    *)
        echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} ${CURRENT_SCRIPT_NAME} - ${@}" 2>&1 | tee -a ${LOG_FILE_FULL_PATH};;
    esac
}

log_to_file ERROR "===> disk usage"
df -h 2>&1 | tee -a ${LOG_FILE_FULL_PATH}
log_to_file ERROR "<=== disk usage"

exit 0