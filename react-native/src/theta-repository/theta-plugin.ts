/**
 * plugin information used by ThetaRepository.listPlugins().
 */
export type PluginInfo = {
  /** Plugin name */
  name: string;
  /** Package name */
  packageName: string;
  /** Plugin version */
  version: string;
  /** Pre-installed plugin or not */
  isPreInstalled: boolean;
  /** Is the plugin running or not */
  isRunning: boolean;
  /** Is the plugin foreground or not */
  isForeground: boolean;
  /** Is the plugin to be started on boot or not */
  isBoot: boolean;
  /** Does the plugin have web server or not */
  hasWebServer: boolean;
  /** Exit status of the plugin */
  exitStatus: string;
  /** Message of the plugin */
  message: string;
};
