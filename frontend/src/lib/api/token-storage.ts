const ACCESS_TOKEN_STORAGE_KEY = "educator.access_token";

let inMemoryAccessToken: string | null = null;

function isBrowser(): boolean {
  return typeof window !== "undefined";
}

export function getTokenStorageKey(): string {
  return ACCESS_TOKEN_STORAGE_KEY;
}

export function getAccessToken(): string | null {
  if (inMemoryAccessToken) {
    return inMemoryAccessToken;
  }

  if (!isBrowser()) {
    return null;
  }

  try {
    const token = window.localStorage.getItem(ACCESS_TOKEN_STORAGE_KEY);
    if (token) {
      inMemoryAccessToken = token;
    }
    return token;
  } catch {
    return null;
  }
}

export function setAccessToken(token: string | null): void {
  inMemoryAccessToken = token;

  if (!isBrowser()) {
    return;
  }

  try {
    if (token) {
      window.localStorage.setItem(ACCESS_TOKEN_STORAGE_KEY, token);
    } else {
      window.localStorage.removeItem(ACCESS_TOKEN_STORAGE_KEY);
    }
  } catch {
    // Intentionally ignore storage failures (private mode, blocked storage, etc).
  }
}

export function clearAccessToken(): void {
  setAccessToken(null);
}

