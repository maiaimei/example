#!/bin/bash

if [[ $# -ne 1 ]]; then
    echo "Missing parameter. Usage: startup.sh [server_folder_name]"
    exit 1
fi

CURRENT_SERVER_NAME=$(hostname)
#CURRENT_SERVER_NAME=$(hostname -I) # get IP
CURRENT_USER=$(whoami)
CURRENT_SCRIPT_NAME=$(basename $0)
# Get the absolute path where the current script is located 
CURRENT_SCRIPT_PATH=$(cd "$(dirname "$0")"; pwd)
# Get the workspace where the current script is located 
CURRENT_WORKSPACE=$(dirname "${CURRENT_SCRIPT_PATH}")
SERVER_FOLDER=$1

LOG_FILE_NAME="nohup.log"
LOG_FILE_PATH="/opt/app/log"
LOG_FILE_FULL_PATH="${LOG_FILE_PATH}/${LOG_FILE_NAME}"

returnValue=0
total=0
success=0
failure=0

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

create_log_file(){
    if [[ ! -e ${LOG_FILE_PATH} ]]
    then
        mkdir -p ${LOG_FILE_PATH}
    fi    
    local log_file=${LOG_FILE_FULL_PATH}
    if [[ ! -e ${log_file} ]]
    then
        echo "create ${log_file} start"
        touch ${log_file}
        echo "create ${log_file} end"
    fi
}

create_archive_log_file(){
    local log_file=${LOG_FILE_FULL_PATH}
    # If the log file exists and the creation date of the log file is less than the current system date, the log file needs to be archived
    if [[ -e ${log_file} ]]
    then
    	log_file_create_date_string=$(ls --full-time ${log_file} | cut -d ' ' -f 6)
        log_file_create_date=$(date -d "${log_file_create_date_string}" +%s)
        current_date_string=$(date +'%Y-%m-%d')
		current_date=$(date -d "${current_date_string}" +%s)
        if [ "$log_file_create_date" -lt "$current_date" ]
        then
        	archive_file="${LOG_FILE_NAME}.${current_date_string}.gz"
            gzip -c ${log_file} > ${archive_file} && rm -f ${log_file}
            echo "Archive created at $(date +'%Y-%m-%d %T.%N') with file name ${archive_file}"
        fi
    fi
}

remove_archive_log_file(){
    if [[ -e ${LOG_FILE_PATH} ]]
    then
        # If the current system time minus the change time of the archive log file is greater than 90 days, the archive log file needs to be deleted
        # -type f 表示查找文件
        # -mtime +90 表示只查找修改时间比当前时间早90天以上的文件
        # -exec rm -f {} \; 部分表示对每个找到的文件执行rm -f命令，即强制删除这些文件。
        find ${LOG_FILE_PATH} -type f -name "${LOG_FILE_NAME}.*.gz" -mtime +90 -exec rm -f {} \;    
    fi     
}

execute_script(){
    log_to_file "Start executing the script in the ${CURRENT_WORKSPACE}/${SERVER_FOLDER}/${CURRENT_USER} directory"
    script_file_list=($(ls ${CURRENT_WORKSPACE}/${SERVER_FOLDER}/${CURRENT_USER}))
    # 遍历指定目录下的所有文件和文件夹
    for script_file_name in ${script_file_list[@]}
    do
        script_file=${CURRENT_WORKSPACE}/${SERVER_FOLDER}/${CURRENT_USER}/${script_file_name}
        # 检查是否是文件
        if [ -f "$script_file" ]; then
            # 检查文件扩展名是否为.sh
            if [[ "$script_file" == *.sh ]]; then
                # 执行文件
                log_to_file "Start executing the script $script_file"
                sh "$script_file"
                result=$?
                if [[ $result -ne 0 ]]; then
                    failure=$((failure + 1))
                    if [[ ${returnValue} -eq 0 ]]; then
                        returnValue=$result
                    fi
                else
                    success=$((success + 1))
                fi
                log_to_file "End executing the script $script_file"
            fi
        fi
    done
    total=$((success + failure))
    log_to_file "End executing the script in the ${CURRENT_WORKSPACE}/${SERVER_FOLDER}/${CURRENT_USER} directory, total: ${total}, success: ${success}, failure: ${failure}"
}

remove_archive_log_file
create_archive_log_file
create_log_file
execute_script

exit ${returnValue}