import type { Options } from '../options';

export function convertOptions(
  options: Options,
  jsonOptions?: Record<string, string>
): Options {
  if (!jsonOptions) {
    return options;
  }

  const result = { ...options };

  let jsonString = JSON.stringify(jsonOptions);
  jsonString = jsonString
    .replace(/\\"/g, '"')
    .replace(/\\n/g, '')
    .replace(/"{/g, '{')
    .replace(/}"/g, '}');
  const parsedOptions = JSON.parse(jsonString) as Options;

  if (parsedOptions.topBottomCorrectionRotationSupport) {
    result.topBottomCorrectionRotationSupport =
      parsedOptions.topBottomCorrectionRotationSupport;
  }

  return result;
}
