const humanize = require('humanize-number');
const fileUtil = require("../util/file.util");

module.exports = (app) => {
    app.use(async (ctx, next) => {
        if (!fileUtil.isStaticResource(ctx.url)) {
            const headers = ctx.request.headers ? JSON.stringify(ctx.request.headers) : "";
            const payload = ctx.request.body ? JSON.stringify(ctx.request.body) : "";
            ctx.logger.info(`[${ctx.method}]${ctx.url}, headers: ${headers}, payload: ${payload}`);
            const start = Date.now();
            await next();
            if (ctx.status === 200) {
                const responseBody = ctx.body ? JSON.stringify(ctx.body) : "";
                ctx.logger.info(`[${ctx.method}]${ctx.url}, response: ${responseBody}, completed in ${time(start)}`);
            }
        }
        else {
            await next();
        }
    });
}

/**
 * Show the response time in a human readable format.
 * In milliseconds if less than 10 seconds,
 * in seconds otherwise.
 */

 function time (start) {
    const delta = Date.now() - start
    return humanize(delta < 10000
      ? delta + 'ms'
      : Math.round(delta / 1000) + 's')
  }