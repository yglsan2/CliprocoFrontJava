const path = require('path');

module.exports = {
    entry: './src/main/webapp/resources/js/main.js',
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, 'src/main/webapp/resources/js/dist')
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env']
                    }
                }
            }
        ]
    },
    resolve: {
        extensions: ['.js']
    }
}; 