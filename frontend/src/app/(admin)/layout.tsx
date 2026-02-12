export default function AdminLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className="min-h-screen bg-[#f2efe7]">
      <header className="border-b bg-[#fffdf8] px-6 py-4">
        <p className="font-mono text-xs uppercase tracking-[0.22em] text-zinc-600">Admin Console</p>
      </header>
      {children}
    </div>
  );
}

