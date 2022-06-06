const path = require("path");
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    // 入口
    entry: "./src/main.js",
    // 输出
    output: {
        path: undefined,
        filename: "main.js",
        // 自动清空之前打包的文件
        // clean: true
    },
    // 加载器loader
    // webpack 只能理解 JavaScript 和 JSON 文件，
    // loader 让 webpack 能够去处理其他类型的文件，并将它们转换为有效 模块，以供应用程序使用，以及被添加到依赖图中。
    module: {
        rules: [
            {
                test: /\.m?js$/,
                exclude: /node_modules/,
                loader: 'babel-loader',
                // 以下配置提取到 babel.config.js 中
                // options: {
                //     presets: ['@babel/preset-env']
                // }
            }
        ],
    },
    // 插件
    // loader 用于转换某些类型的模块，而插件则可以用于执行范围更广的任务。包括：打包优化，资源管理，注入环境变量。
    plugins: [
        // 将 html 一起打包到 dist 中，避免手动引用 js
        new HtmlWebpackPlugin({
            template: path.resolve(__dirname, "../public/index.html")
        })
    ],
    // 模式：development 或 production
    mode: "development",
    // webpack-dev-server开发服务器：自动监听资源文件的变化，即热部署
    devServer: {
        host: "localhost",
        port: "3000",
        // 是否自动打开浏览器
        open: true
    }
}