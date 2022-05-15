/**
 * 接口返回值统一处理
{
	"code": 500,
	"message": "登录账号或密码错误！",
	"data": null,
	"traceId": "124d27c1-614a-4e22-b4da-efb8aa9c9609"
}
 * @param {*} app 
 */
module.exports = (app) => {
    app.use(async (ctx, next) => {
        await next();
        const code = ctx.status;
        const message = code != 200 ? ctx.body : null;
        const data = code == 200 ? ctx.body : null;
        const traceId = ctx.logger.context['traceId'];
        ctx.body = { code, message, data,traceId };
    });
}