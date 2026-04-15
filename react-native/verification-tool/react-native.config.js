const path = require('path');

/**
 * theta-client-react-native is referenced via file:..,
 * so we explicitly set the root to ensure autolinking detects it correctly.
 */
module.exports = {
  dependencies: {
    'theta-client-react-native': {
      root: path.join(__dirname, '..'),
    },
  },
};
