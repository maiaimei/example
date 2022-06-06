const { v4: uuidv4 } = require('uuid');
const log4js = require("log4js");
log4js.configure({
    // https://log4js-node.github.io/log4js-node/appenders.html
    appenders: {
        stdout: {
            type: 'stdout',
            layout: {
                //type: 'coloured'
                type: 'pattern',
                pattern: '%d %p %c %X{traceId} %m'
            }
        },
        fileout: {
            type: "file",
            filename: "logs/application.log"
        },
        datefileout: {
            type: "dateFile",
            filename: "logs/application.log",
            pattern: "yyyyMMdd",
            layout: {
                type: 'pattern',
                pattern: '%d %p %c %X{traceId} %m'
            },
            //compress: true,
            keepFileExt: true,
            numBackups: 5
        }
    },
    categories: {
        default: {
            level: "info",
            appenders: ["stdout", "datefileout"]
        }
    }
});

module.exports = (app) => {
    async function logHandler(ctx, next) {
        if (!ctx.logger) ctx.logger = log4js.getLogger();
        const traceId = ctx.get('X-Request-Id') || uuidv4();
        ctx.logger.addContext('traceId', traceId);
        await next();
    };

    app.use(logHandler);
}