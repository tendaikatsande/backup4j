module.exports = {
    transform: {
        "^.+\\.jsx?$": "babel-jest",
    },
    transformIgnorePatterns: [
        "/node_modules/(?!(axios)/)"
    ],
    moduleNameMapper: {
        "\\.(css|less)$": "identity-obj-proxy"
    },
    testEnvironment: "jsdom"
};
