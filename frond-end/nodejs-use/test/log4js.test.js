const log4js = require("log4js");
log4js.configure({
    // https://log4js-node.github.io/log4js-node/appenders.html
    // Appenders serialise log events to some form of output. 
    // They can write to files, send emails, send data over the network. 
    // All appenders have a type which determines which appender gets used. 
    // https://log4js-node.github.io/log4js-node/layouts.html
    // Layouts are functions used by appenders to format log events for output. 
    // They take a log event as an argument and return a string. 
    // Log4js comes with several appenders built-in, and provides ways to create your own if these are not suitable.
    appenders: {
        stdout: {
            type: 'stdout',
            layout: {
                type: 'basic'
            }
        },
        colouredStdout: {
            type: 'stdout',
            layout: {
                type: 'coloured'
            }
        },
        app: {
            type: "file",
            filename: "application.log"
        }
    },
    // Categories are groups of log events. 
    // The category for log events is defined when you get a Logger from log4js (log4js.getLogger('somecategory')). 
    // Log events with the same category will go to the same appenders.
    // https://log4js-node.github.io/log4js-node/categories.html
    categories: {
        default: {
            level: "trace",
            appenders: ["colouredStdout"]
        },
        app: {
            level: 'trace',
            appenders: ['app']
        }
    }
});

const loggerToStdout = log4js.getLogger();
loggerToStdout.trace("Some trace messages");
loggerToStdout.debug("Some debug messages");
loggerToStdout.info("Some info messages");
loggerToStdout.warn("Some warn messages");
loggerToStdout.error("Some error messages");
loggerToStdout.fatal("Some fatal messages");

const logToFile = log4js.getLogger("app");
logToFile.trace("Some trace messages");
logToFile.debug("Some debug messages");
logToFile.info("Some info messages");
logToFile.warn("Some warn messages");
logToFile.error("Some error messages");
logToFile.fatal("Some fatal messages");