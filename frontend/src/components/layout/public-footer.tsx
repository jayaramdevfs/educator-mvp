import Link from "next/link";

export function PublicFooter() {
  return (
    <footer className="border-t border-slate-800 bg-slate-900/80 backdrop-blur-xl">
      <div className="mx-auto flex max-w-7xl flex-col items-start justify-between gap-4 px-6 py-6 text-sm text-slate-400 md:flex-row md:items-center">
        <p>© {new Date().getFullYear()} Educator Platform. All rights reserved.</p>
        <div className="flex items-center gap-5">
          <Link href="/catalog" className="transition-colors hover:text-purple-300">
            Catalog
          </Link>
          <Link href="/login-new" className="transition-colors hover:text-purple-300">
            Sign In
          </Link>
          <Link href="/register" className="transition-colors hover:text-purple-300">
            Register
          </Link>
        </div>
      </div>
    </footer>
  );
}
