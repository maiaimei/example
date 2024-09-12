#!/bin/bash

# Initialize current user and server information
CURRENT_SERVER_NAME=$(hostname)
CURRENT_USER=$(whoami)

# Initialize log file path
LOG_FILE_PATH=$1

# Define the log function
log(){
    # Initialize local variable
    local log_level=""
    local log_message=""
    local script_name=""
    if [[ $# -eq 3 ]]; then
        log_level=$1
        log_message=$2
        script_name=$3
    elif [[ $# -eq 2 ]]; then
        log_message=$1
        script_name=$2
    elif [[ $# -eq 1 ]]; then
        log_message=$1
        script_name=$CURRENT_EXECUTE_SCRIPT_NAME
    else
        echo "Error: Invalid parameter"
        exit 1
    fi

    # Output logs based on levels
    case ${log_level} in
    "INFO")
        echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} \033[32m[INFO]\033[0m ${script_name} - ${log_message}" 2>&1 | tee -a ${LOG_FILE_PATH};;
    "WARN")
        echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} \033[33m[WARN]\033[0m ${script_name} - ${log_message}" 2>&1 | tee -a ${LOG_FILE_PATH};;
    "ERROR")
        echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} \033[31m[ERROR]\033[0m ${script_name} - ${log_message}" 2>&1 | tee -a ${LOG_FILE_PATH};;
    *)
        echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} ${script_name} - ${log_message}" 2>&1 | tee -a ${LOG_FILE_PATH};;
    esac
}

# Define the log_cmd_result function
log_cmd_result(){
    local execute_command=$1
    eval ${execute_command} 2>&1 | tee -a ${LOG_FILE_PATH}
}

# Export the function log and log_cmd_result
export -f log
export -f log_cmd_result
