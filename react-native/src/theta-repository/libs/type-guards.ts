/**
 * Type guard to check if a value is promise-like (has a `then` method).
 */
export function isPromiseLike<T>(value: unknown): value is Promise<T> {
  return (
    value != null &&
    (typeof value === 'object' || typeof value === 'function') &&
    typeof (value as { then?: unknown }).then === 'function'
  );
}
