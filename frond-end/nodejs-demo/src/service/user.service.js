// import 
const axios = require("axios").default;
const { v4: uuidv4 } = require('uuid');

// Custom instance defaults
const instance = axios.create({
    baseURL: process.env.JSON_SERVER_URL,
    headers: { 'X-Custom-Header': 'foobar' },
    timeout: 10000
});

async function list() {
    let responseData;
    await instance.get(`/users`)
        .then(response => {
            responseData = response.data;
        });
    return responseData;
}

async function get(id) {
    let responseData;
    await instance.get(`/users/${id}`)
        .then(response => {
            responseData = response.data;
        });
    return responseData;
}

// axios.post(url[, data[, config]])
async function create(user) {
    let responseData;
    if (!user.id) {
        user.id = uuidv4();
    }
    await instance.post('/users', user)
        .then(response => {
            responseData = response.data;
        });
    return responseData;
}

async function update(user) {
    let responseData;
    await instance.put(`/users/${user.id}`, user)
        .then(response => {
            responseData = response.data;
        });
    return responseData;
}

async function remove(id) {
    let responseData;
    await instance.delete(`/users/${id}`)
        .then(response => {
            responseData = response.data;
        });
    return responseData;
}

module.exports = {
    list,
    get,
    create,
    update,
    remove
};