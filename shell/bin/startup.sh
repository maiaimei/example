#!/bin/bash

if [[ $# -ne 1 ]]; then
    echo "Missing parameter. Usage: startup.sh [server_folder_name]"
    exit 1
fi

CURRENT_SERVER_NAME=$(hostname)
CURRENT_USER=$(whoami)
CURRENT_SCRIPT_NAME=$(basename $0)
CURRENT_SCRIPT_PATH=$(cd $(dirname $0); pwd)
PROJECT_PATH=${CURRENT_SCRIPT_PATH%%/release*}
DEPLOY_PATH=${PROJECT_PATH}/release
SERVER_FOLDER=$1

LOG_BASE_PATH="${PROJECT_PATH}/log"
LOG_ARCHIVE_PATH="${LOG_BASE_PATH}/archive"
LOG_FILE_NAME="test"
LOG_FILE_PATH="${LOG_BASE_PATH}/${LOG_FILE_NAME}.log"

returnValue=0
total=0
success=0
failure=0

log(){
    local log_level=$1
    local log_message=$2

    case ${log_level} in
    "INFO")
        echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} \033[32m[INFO]\033[0m ${CURRENT_SCRIPT_NAME} - ${log_message}" 2>&1 | tee -a ${LOG_FILE_PATH};;
    "WARN")
        echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} \033[33m[WARN]\033[0m ${CURRENT_SCRIPT_NAME} - ${log_message}" 2>&1 | tee -a ${LOG_FILE_PATH};;
    "ERROR")
        echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} \033[31m[ERROR]\033[0m ${CURRENT_SCRIPT_NAME} - ${log_message}" 2>&1 | tee -a ${LOG_FILE_PATH};;
    *)
        echo -e "$(date "+%Y-%m-%d %T.%N") ${CURRENT_SERVER_NAME} ${CURRENT_USER} ${CURRENT_SCRIPT_NAME} - ${@}" 2>&1 | tee -a ${LOG_FILE_PATH};;
    esac
}

create_log_file(){  
    local log_file=${LOG_FILE_PATH}
    if [[ ! -e ${log_file} ]]
    then
        echo "create ${log_file} start"
        touch ${log_file}
        echo "create ${log_file} end"
    fi
}

create_log_file_path(){ 
    if [[ ! -e ${LOG_BASE_PATH} ]]; then
        mkdir -p ${LOG_BASE_PATH}
    fi 
    if [[ ! -e ${LOG_ARCHIVE_PATH} ]]; then
        mkdir -p ${LOG_ARCHIVE_PATH}
    fi
}

create_archive_log_file(){
    local log_file=${LOG_FILE_PATH}
    # If the log file exists and the change date of the log file is less than the system current date, the log file needs to be archived
    if [[ -e ${log_file} ]]
    then
    	log_file_change_date_string=$(ls --full-time ${log_file} | cut -d ' ' -f 6)
        log_file_change_date=$(date -d "${log_file_change_date_string}" +%s)
        current_date_string=$(date +'%Y-%m-%d')
		current_date=$(date -d "${current_date_string}" +%s)
        if [ "$log_file_change_date" -lt "$current_date" ]
        then
        	archive_file="${LOG_ARCHIVE_PATH}/${LOG_FILE_NAME}.${log_file_change_date_string}.log.gz"
            gzip -c ${log_file} > ${archive_file} && rm -f ${log_file}
            echo "Archive created at $(date +'%Y-%m-%d %T.%N') with file ${archive_file}"
        fi
    fi
}

remove_archive_log_file(){
    if [[ -e ${LOG_BASE_PATH} ]]
    then
        # If the system current time minus the change time of the log file is greater than 90 days, the log file needs to be deleted
        find ${LOG_BASE_PATH} -type f -name "${LOG_FILE_NAME}.log" -mtime +90 -exec rm -f {} \;

        # If the system current time minus the change time of the archive log file is greater than 90 days, the archive log file needs to be deleted
        # -type f 表示查找文件
        # -mtime +90 表示只查找修改时间比当前时间早90天以上的文件
        # -exec rm -f {} \; 部分表示对每个找到的文件执行rm -f命令，即强制删除这些文件。
        find ${LOG_BASE_PATH} -type f -name "${LOG_FILE_NAME}.*.log.gz" -mtime +90 -exec rm -f {} \;    
    fi     
}

execute_script(){
    log "Start executing the script in the ${DEPLOY_PATH}/${SERVER_FOLDER}/${CURRENT_USER} directory"
    script_file_name_list=($(ls ${DEPLOY_PATH}/${SERVER_FOLDER}/${CURRENT_USER}))
    # 遍历指定目录下的所有文件和文件夹
    for script_file_name in ${script_file_name_list[@]}
    do
        script_file=${DEPLOY_PATH}/${SERVER_FOLDER}/${CURRENT_USER}/${script_file_name}
        # 检查是否是文件
        if [ -f "$script_file" ]; then
            # 检查文件扩展名是否为.sh
            if [[ "$script_file" == *.sh ]]; then
                # 执行文件
                log "Start executing the script $script_file"
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
                log "End executing the script $script_file"
            fi
        fi
    done
    total=$((success + failure))
    log "End executing the script in the ${DEPLOY_PATH}/${SERVER_FOLDER}/${CURRENT_USER} directory, total: ${total}, success: ${success}, failure: ${failure}"
}

create_log_file_path
remove_archive_log_file
create_archive_log_file
create_log_file
execute_script

exit ${returnValue}