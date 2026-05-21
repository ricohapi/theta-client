import type { Options } from '../options';

export function toNumber(value: unknown): number | undefined {
  if (typeof value === 'number' && Number.isFinite(value)) {
    return value;
  }
  if (typeof value === 'string') {
    const parsed = Number(value);
    return Number.isFinite(parsed) ? parsed : undefined;
  }
  if (value != null && typeof value === 'object' && 'valueOf' in value) {
    const primitive = (value as { valueOf: () => unknown }).valueOf();
    return toNumber(primitive);
  }
  return undefined;
}

export function toStringValue(value: unknown): string | undefined {
  return typeof value === 'string' && value.length > 0 ? value : undefined;
}

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
