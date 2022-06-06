const dotenv = require('dotenv');

module.exports = () => {
    switch (process.env.NODE_ENV) {
        case "development": {
            dotenv.config({ path: '.env.development' });
            break;
        }
        case "test": {
            dotenv.config({ path: '.env.test' });
            break;
        }
        case "production": {
            dotenv.config({ path: '.env.production' });
            break;
        }
        default: {
            dotenv.config();
            break;
        }
    }
}