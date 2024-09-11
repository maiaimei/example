#!/bin/bash

if [[ $# -ne 1 ]]; then
    echo "Missing parameter. Usage: package.sh [release_folder_name]"
    exit 1
fi

CURRENT_SCRIPT_PATH=$(cd $(dirname $0); pwd)
PROJECT_PATH=$(dirname "${CURRENT_SCRIPT_PATH}")
DEFAULTS_PATH=${PROJECT_PATH}/script/defaults
RELEASE_SOURCE_PATH=${PROJECT_PATH}/script/$1
RELEASE_TARGET_PATH=${PROJECT_PATH}/release
BIN_SOURCE_PATH=${PROJECT_PATH}/bin
BIN_TARGET_PATH=${PROJECT_PATH}/release/bin

echo "Build package begin"

# Create target release folder if it is absent, otherwise clean it up
if [[ ! -e ${RELEASE_TARGET_PATH} ]]; then
    mkdir -p ${RELEASE_TARGET_PATH}
else
    rm -rf ${RELEASE_TARGET_PATH}
fi
# Create bin folder in target release folder
if [[ ! -e ${BIN_TARGET_PATH} ]]; then
    mkdir -p ${BIN_TARGET_PATH}
fi

echo "Copy startup.sh begin"
cp ${BIN_SOURCE_PATH}/startup.sh ${BIN_TARGET_PATH}/
echo "Copy startup.sh end"

echo "Copy files from ${RELEASE_SOURCE_PATH} begin"
server_name_list=($(ls ${RELEASE_SOURCE_PATH}))
for server_name in ${server_name_list[@]}
do
    remote_user_list=($(ls ${RELEASE_SOURCE_PATH}/${server_name}))
    for remote_user in ${remote_user_list[@]}
    do
        script_name_list=($(ls ${RELEASE_SOURCE_PATH}/${server_name}/${remote_user}))
        if [[ ${#script_name_list[@]} -gt 0 ]]; then
            mkdir -p ${RELEASE_TARGET_PATH}/${server_name}/${remote_user}
            cp ${RELEASE_SOURCE_PATH}/${server_name}/${remote_user}/* ${RELEASE_TARGET_PATH}/${server_name}/${remote_user}/
            mkdir -p ${RELEASE_TARGET_PATH}/${server_name}/${remote_user}/defaults
            cp ${DEFAULTS_PATH}/* ${RELEASE_TARGET_PATH}/${server_name}/${remote_user}/defaults/        
        fi
    done
done
echo "Copy files from ${RELEASE_SOURCE_PATH} end"

echo "Build package end"

exit 0