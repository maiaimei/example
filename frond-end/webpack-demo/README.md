[https://www.bilibili.com/video/BV14T4y1z7sw](https://www.bilibili.com/video/BV14T4y1z7sw)

[https://webpack.docschina.org/](https://webpack.docschina.org/)

[https://webpack.docschina.org/plugins/eslint-webpack-plugin/](https://webpack.docschina.org/plugins/eslint-webpack-plugin/)

[https://webpack.docschina.org/loaders/babel-loader/](https://webpack.docschina.org/loaders/babel-loader/)

[https://www.npmjs.com/package/webpack-dev-server](https://www.npmjs.com/package/webpack-dev-server)



```shell
# 不使用配置打包
npx webpack ./src/main.js --mode=development
npx webpack ./src/main.js --mode=production

# 使用自定义的 webpack.config.js 打包
npx webpack

# 在 webpack.config.js 中配置 devServer，自动监听资源文件的变化，用于开发环境下
npx webpack serve
```



TODO:

- multiple entry
- multiple html

