import { toast as sonnerToast, type ExternalToast } from "sonner";
import { CheckCircle2, XCircle, AlertCircle, Info } from "lucide-react";

interface ToastOptions extends ExternalToast {
  duration?: number;
}

const defaultDuration = 4000;

/**
 * Success toast with check icon
 */
export function toastSuccess(message: string, options?: ToastOptions) {
  return sonnerToast.success(message, {
    duration: defaultDuration,
    icon: <CheckCircle2 className="h-5 w-5" />,
    ...options,
  });
}

/**
 * Error toast with X icon
 */
export function toastError(message: string, options?: ToastOptions) {
  return sonnerToast.error(message, {
    duration: defaultDuration,
    icon: <XCircle className="h-5 w-5" />,
    ...options,
  });
}

/**
 * Warning toast with alert icon
 */
export function toastWarning(message: string, options?: ToastOptions) {
  return sonnerToast.warning(message, {
    duration: defaultDuration,
    icon: <AlertCircle className="h-5 w-5" />,
    ...options,
  });
}

/**
 * Info toast with info icon
 */
export function toastInfo(message: string, options?: ToastOptions) {
  return sonnerToast.info(message, {
    duration: defaultDuration,
    icon: <Info className="h-5 w-5" />,
    ...options,
  });
}

/**
 * Default toast (neutral)
 */
export function toast(message: string, options?: ToastOptions) {
  return sonnerToast(message, {
    duration: defaultDuration,
    ...options,
  });
}

/**
 * Promise toast for async operations
 */
export function toastPromise<T>(
  promise: Promise<T> | (() => Promise<T>),
  messages: {
    loading: string;
    success: string | ((data: T) => string);
    error: string | ((error: unknown) => string);
  },
  options?: ToastOptions,
) {
  return sonnerToast.promise(promise, {
    loading: messages.loading,
    success: messages.success,
    error: messages.error,
    duration: defaultDuration,
    ...options,
  });
}

/**
 * Dismiss a specific toast
 */
export function toastDismiss(toastId?: string | number) {
  return sonnerToast.dismiss(toastId);
}

/**
 * Dismiss all toasts
 */
export function toastDismissAll() {
  return sonnerToast.dismiss();
}

// Re-export Toaster component from sonner
export { Toaster } from "sonner";
