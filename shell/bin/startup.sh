#!/bin/bash

# check script parameter
if [[ $# -ne 1 ]]; then
    echo "Error: Missing parameter. Usage: startup.sh [server_folder_name]"
    exit 1
fi

# Initialize current user and server information
CURRENT_USER=$(whoami)
CURRENT_SERVER_NAME=$(hostname)

# Initialize current script information
CURRENT_SCRIPT_NAME=$(basename $0)
CURRENT_SCRIPT_PATH=$(cd $(dirname $0); pwd)

# Initialize directories and files
PROJECT_PATH=${CURRENT_SCRIPT_PATH%%/release*}
DEPLOY_PATH=${PROJECT_PATH}/release
LOG_BASE_PATH="${PROJECT_PATH}/log"
LOG_ARCHIVE_PATH="${LOG_BASE_PATH}/archive"
LOG_FILE_NAME="application"
LOG_FILE_PATH="${LOG_BASE_PATH}/${LOG_FILE_NAME}.log"
SERVER_FOLDER=$1

# Initialize counter
total=0
success=0
failure=0

# Initialize script return value
return_value=0

# Define the check_path function
check_path(){
    for dir in "${PROJECT_PATH}" "${DEPLOY_PATH}"
    do
        if [ ! -e "$dir" ]; then
            echo "Error: No directory found, directory: $dir"
            exit 1
        fi
    done    
}

# Define the create_log_file function
create_log_file(){  
    if [[ ! -e ${LOG_FILE_PATH} ]]
    then
        echo "Start create file ${LOG_FILE_PATH}"
        touch ${LOG_FILE_PATH}
        echo "End create file ${LOG_FILE_PATH}"
        echo "Before change mode for ${LOG_FILE_PATH}"
        ls -l ${LOG_FILE_PATH}
        chmod 664 ${LOG_FILE_PATH}
        echo "After change mode for ${LOG_FILE_PATH}" 
        ls -l ${LOG_FILE_PATH}
    fi
}

# Define the create_log_file_path function
create_log_file_path(){ 
    if [[ ! -e ${LOG_BASE_PATH} ]]; then
        echo "Start create directory ${LOG_BASE_PATH}"
        mkdir -p ${LOG_BASE_PATH}
        echo "End create directory ${LOG_BASE_PATH}"
        echo "Before change mode for ${LOG_BASE_PATH}"
        ls -l ${LOG_BASE_PATH}
        chmod 775 ${LOG_BASE_PATH}
        echo "After change mode for ${LOG_BASE_PATH}" 
        ls -l ${LOG_BASE_PATH}        
    fi 
    if [[ ! -e ${LOG_ARCHIVE_PATH} ]]; then
        echo "Start create directory ${LOG_ARCHIVE_PATH}"
        mkdir -p ${LOG_ARCHIVE_PATH}
        echo "End create directory ${LOG_ARCHIVE_PATH}"
        echo "Before change mode for ${LOG_ARCHIVE_PATH}"
        ls -l ${LOG_ARCHIVE_PATH}
        chmod 775 ${LOG_ARCHIVE_PATH}
        echo "After change mode for ${LOG_ARCHIVE_PATH}" 
        ls -l ${LOG_ARCHIVE_PATH}        
    fi
}

# Define the create_archive_log_file function
create_archive_log_file(){
    # If the log file exists and the change date of the log file is less than the system current date, the log file needs to be archived
    if [[ -e ${LOG_FILE_PATH} ]]
    then
    	log_file_change_date_string=$(ls --full-time ${LOG_FILE_PATH} | cut -d ' ' -f 6)
        log_file_change_date=$(date -d "${log_file_change_date_string}" +%s)
        current_date_string=$(date +'%Y-%m-%d')
		current_date=$(date -d "${current_date_string}" +%s)
        if [ "$log_file_change_date" -lt "$current_date" ]
        then
        	archive_file="${LOG_ARCHIVE_PATH}/${LOG_FILE_NAME}.${log_file_change_date_string}.log.gz"
            echo "Start create archive file for ${archive_file}"
            gzip -c ${LOG_FILE_PATH} > ${archive_file} && rm -f ${LOG_FILE_PATH}
            echo "End create archive file for ${archive_file} at $(date +'%Y-%m-%d %T.%N')"
            echo "Before change mode for ${archive_file}"
            ls -l ${archive_file}
            chmod 664 ${archive_file}
            echo "After change mode for ${archive_file}" 
            ls -l ${archive_file}            
        fi
    fi
}

# Define the remove_archive_log_file function
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

# Define the execute_script function
execute_script(){
    log ${CURRENT_SCRIPT_NAME} "Start executing the script in the ${DEPLOY_PATH}/${SERVER_FOLDER}/${CURRENT_USER} directory"
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
                log ${CURRENT_SCRIPT_NAME} "Start executing the script $script_file"
                sh "$script_file"
                result=$?
                if [[ $result -ne 0 ]]; then
                    failure=$((failure + 1))
                    if [[ ${return_value} -eq 0 ]]; then
                        return_value=$result
                    fi
                else
                    success=$((success + 1))
                fi
                log ${CURRENT_SCRIPT_NAME} "End executing the script $script_file"
            fi
        fi
    done
    total=$((success + failure))
    log ${CURRENT_SCRIPT_NAME} "End executing the script in the ${DEPLOY_PATH}/${SERVER_FOLDER}/${CURRENT_USER} directory, total: ${total}, success: ${success}, failure: ${failure}"
}

# Define the main function
main(){
    # Call the following functions in sequence
    create_log_file_path
    remove_archive_log_file
    create_archive_log_file
    create_log_file
    execute_script  
}

# Check path
check_path

# Call the script log.sh
source ${CURRENT_SCRIPT_PATH}/log.sh ${LOG_FILE_PATH}

# Call the function main
main

exit ${return_value}
