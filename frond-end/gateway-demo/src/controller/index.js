const compose = require('koa-compose');
const glob = require('glob');
const { resolve } = require('path');

module.exports = () => {
    let routers = [];
    // 递归获取当前文件夹下所有js文件
    const path = resolve(__dirname, './', '*.js').replace(/\\/g,'/');
    glob.sync(path)
        // 排除index.js文件
        .filter(value => (value.indexOf('index.js') === -1))
        .forEach(router => {
            const r = require(router);
            routers.push(r.routes());
            routers.push(r.allowedMethods());
        });
    return compose(routers);
}