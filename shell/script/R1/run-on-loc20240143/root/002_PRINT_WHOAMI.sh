#!/bin/bash

CURRENT_SERVER_NAME=$(hostname)
#CURRENT_SERVER_NAME=$(hostname -I) # get IP
CURRENT_USER=$(whoami)
CURRENT_SCRIPT_NAME=$(basename $0)

log(){
    local log_level=$1
    local log_message=$2

    case ${log_level} in
    "INFO")
        echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} \033[32m[INFO]\033[0m ${CURRENT_SCRIPT_NAME} - ${log_message}";;
    "WARN")
        echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} \033[33m[WARN]\033[0m ${CURRENT_SCRIPT_NAME} - ${log_message}";;
    "ERROR")
        echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} \033[31m[ERROR]\033[0m ${CURRENT_SCRIPT_NAME} - ${log_message}";;
    *)
        echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} ${CURRENT_SCRIPT_NAME} - ${@}";;
    esac
}

log INFO ${CURRENT_USER}
log WARN ${CURRENT_USER}
log ERROR ${CURRENT_USER}
log ${CURRENT_USER}

exit 1