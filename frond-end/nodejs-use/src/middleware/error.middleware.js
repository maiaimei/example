/**
 * 异常统一处理
 * @param {*} app 
 */
module.exports = (app) => {
    async function errorHandler(ctx, next) {
        try {
            await next();
        } catch (err) {
            ctx.status = err.statusCode || err.status || 500;
            ctx.body = err.message;
            ctx.app.emit("error", err, ctx);
        }
    };

    app.use(errorHandler);
    app.on("error", (err, ctx) => {
        ctx.logger.error(err);
    });
}