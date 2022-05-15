const bodyParser = require('koa-bodyparser');
// const logger = require('koa-logger');
// const compress = require('koa-compress');

const log4jsHandler = require("./log4js.middleware");
const requestHandler = require("./request.middleware");
const responseHandler = require("./response.middleware");
const errorHandler = require("./error.middleware");

module.exports = (app) => {
    // app.use(logger());
    app.use(bodyParser());
    log4jsHandler(app);
    requestHandler(app);
    responseHandler(app);
    errorHandler(app);
    // app.use(compress({
    //     filter(content_type) {
    //         return /text/i.test(content_type)
    //     },
    //     threshold: 2048,
    //     gzip: {
    //         flush: require('zlib').constants.Z_SYNC_FLUSH
    //     },
    //     deflate: {
    //         flush: require('zlib').constants.Z_SYNC_FLUSH,
    //     },
    //     br: false // disable brotli
    // }));
}